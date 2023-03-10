package com.sf.saas.bps.core.service.biz;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.ImmutableMap;
import com.sf.saas.bps.core.api.IStrategyBizService;
import com.sf.saas.bps.core.common.constans.CommonStatusEnum;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.utils.*;
import com.sf.saas.bps.core.dao.entity.Dimension;
import com.sf.saas.bps.core.dao.entity.Strategy;
import com.sf.saas.bps.core.dao.entity.StrategyCondition;
import com.sf.saas.bps.core.dto.base.Page;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.enums.*;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import com.sf.saas.bps.core.dto.vo.*;
import com.sf.saas.bps.core.manager.convert.StrategyConvert;
import com.sf.saas.bps.core.manager.convert.StrategyDimensionConvert;
import com.sf.saas.bps.core.service.crud.IDimensionService;
import com.sf.saas.bps.core.service.crud.IStrategyConditionService;
import com.sf.saas.bps.core.service.crud.IStrategyRemarkService;
import com.sf.saas.bps.core.service.crud.IStrategyService;
import com.sf.saas.bps.core.service.event.spring.StrategyUpdateEvent;
import com.sf.saas.bps.core.service.strategy.dimension.valid.IStrategyDimensionValidator;
import com.sf.saas.bps.core.service.strategy.dimension.valid.StrategyDimensionGetValidatorUtil;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description StrategyBizServiceImpl
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
@Log4j2
@Service
public class StrategyBizServiceImpl implements IStrategyBizService {

    @Autowired
    private IStrategyService strategyService;
    @Autowired
    private IDimensionService dimensionService;
    @Autowired
    private IStrategyConditionService strategyConditionService;
    @Autowired
    private IStrategyRemarkService strategyRemarkService;

    private Map<String, IStrategyDimensionValidator> dimensionValidatorMap;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    @Autowired
    public StrategyBizServiceImpl(Set<IStrategyDimensionValidator> handlerSet) {
        Assert.notEmpty(handlerSet);
        ImmutableMap.Builder<String, IStrategyDimensionValidator> builder = ImmutableMap.builder();
        for (IStrategyDimensionValidator handler : handlerSet) {
            builder.put(handler.getType(), handler);
        }
        this.dimensionValidatorMap = builder.build();
    }

    /**
     * ????????????
     * ??????????????????
     * ????????????
     * @param strategyVo
     * @return
     */
    @Transactional
    @Override
    public Response<String> saveStrategy(StrategySaveVo strategyVo) {
        String tenantId = CurrentReqInfoUtil.currentTenantId();
        String currentUser = CurrentReqInfoUtil.currentUser();
        // ????????????
        List<String> collect = strategyVo.getDimensionList().stream().map(StrategyDimensionVo::getDimension).collect(Collectors.toList());
        List<Dimension> dimensionList = dimensionService.selectByDimensionKey(collect);
        // ???????????????
        List<StrategyRemarkVo> tipContent = strategyVo.getTipContent();

        ValidCodeMsg<ResponseCodeEnum> stringValidCodeMsg = validateSaveStrategyParam(strategyVo, dimensionList, tipContent, false);
        if (!stringValidCodeMsg.isOK()) {
            return ResponseHelper.buildFail(stringValidCodeMsg.getData());
        }

        long currentTimeMillis = System.currentTimeMillis();
        strategyVo.setCreateTime(currentTimeMillis);
        strategyVo.setUpdateTime(currentTimeMillis);
        strategyVo.setCreator(currentUser);
        strategyVo.setUpdater(currentUser);
        // ???????????????
        strategyVo.setStatus(StrategyStatusEnum.INEFFECTIVE.getStatus());
        // ???????????????
        strategyVo.setPriority(strategyVo.getPriority());
        strategyVo.setVersion(StrategyVersionUtil.getVersion());
        strategyVo.setTenantId(tenantId);
        strategyVo.setTipFlag(strategyVo.getTipFlag());

        StrategySaveVo strategyVoSaved = strategyService.saveStrategy(strategyVo);
        if (strategyVoSaved != null) {
            // ??????????????????
            strategyConditionService.saveStrategyCondition(strategyVo.getDimensionList(), strategyVoSaved);

            // ?????? ??????????????? ???StrategyRemark???
            strategyRemarkService.saveStrategyLang(tipContent, strategyVoSaved);

            return ResponseHelper.buildSuccess(strategyVoSaved.getId()+"");
        }
        return ResponseHelper.buildFail(ResponseCodeEnum.FAILURE);
    }

    /**
     * ?????????????????? ??????
     * @param strategyId
     * @param tipContent
     * @return
     */
    private ValidCodeMsg<ResponseCodeEnum> validateStrategyLang(Long strategyId, List<StrategyRemarkVo> tipContent) {
        String cn = LanguageEnum.CN.getLang();
        String en = LanguageEnum.EN.getLang();

        Map<String, StrategyRemarkVo> strategyRemarkMap = tipContent.stream().collect(Collectors.toMap(StrategyRemarkVo::getLang, a -> a,(key1 , key2)-> key2 ));
        boolean isValid = strategyRemarkMap.containsKey(cn) && strategyRemarkMap.containsKey(en);
        isValid &= (strategyRemarkMap.get(cn) != null && StringUtils.isNotBlank(strategyRemarkMap.get(cn).getRemark()));
        isValid &= (strategyRemarkMap.get(en) != null && StringUtils.isNotBlank(strategyRemarkMap.get(en).getRemark()));
        if (!isValid) {
            log.info("saveStrategy ??????????????? ????????? ???????????? strategyId={},tipContent={}", strategyId, JSON.toJSONString(tipContent));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        return ValidCodeMsg.success();
    }

    /**
     * ?????????????????? ??????
     * @param strategyVo
     * @param tipContent
     * @param isUpdate ?????????????????????
     * @return
     */
    private ValidCodeMsg<ResponseCodeEnum> validateSaveStrategyParam(StrategySaveVo strategyVo, List<Dimension> dimensionList,
                                                                     List<StrategyRemarkVo> tipContent, boolean isUpdate) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        if (dimensionList.size() != strategyVo.getDimensionList().size()) {
            log.info("saveStrategy ???????????????????????? reqList={}, dbList={}", JSON.toJSONString(strategyVo.getDimensionList()), JSON.toJSONString(dimensionList));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_INPUT_ERROR);
        }
        StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getStrategyTypeEnumByValue(strategyVo.getType());
        if (strategyTypeEnum == null) {
            log.info("saveStrategy ?????????????????? strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        StrategyPriorityEnum strategyPriorityEnum = StrategyPriorityEnum.getByStrategyType(strategyTypeEnum);
        if (strategyPriorityEnum == null) {
            log.info("saveStrategy ?????????????????? strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        boolean validateTipFlag = StrategyTipFlagEnum.validateTipFlag(strategyTypeEnum, strategyVo.getTipFlag());
        if (!validateTipFlag) {
            log.info("saveStrategy ???????????????????????????????????? strategyTipFlag={}", strategyVo.getTipFlag());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TIP_FLAG_INPUT_ERROR);
        }

        // ???????????? ???????????? ???????????????
        strategyVo.setPriority(strategyPriorityEnum.getPriority());

        // ??????????????????==
        if (CollectionUtils.isEmpty(tipContent)) {
            log.info("saveStrategy ??????????????????????????? strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_INPUT_ERROR);
        }
        if (!validateStrategyLang(strategyVo.getId(), tipContent).isOK()) {
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        // ??????????????????==

        // ???????????????????????????????????????
        StrategySaveVo strategyExist = strategyService.selectByStrategyName(strategyVo.getName(), CurrentReqInfoUtil.currentTenantId());
        if (isUpdate) {
            // ??????????????? ??????????????????????????? ??????????????????????????????
            Strategy serviceById = strategyService.getById(strategyVo.getId());
            if (serviceById == null) {
                return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_NOT_EXIST);
            }
            if (strategyExist != null) {
                // ??????????????????????????????????????????????????????????????????
                strategyExist = strategyService.selectOtherByStrategyName(strategyVo.getName(), serviceById.getId(), CurrentReqInfoUtil.currentTenantId());
            }
        }
        // ??????????????????
        if (strategyExist != null) {
            log.info("saveStrategy ??????????????????????????? strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_NAME_SAME_ERROR);
        }

        return ValidCodeMsg.success();
    }

    @Transactional
    @Override
    public Response<String> updateStrategy(StrategySaveVo strategyVo) {
        Strategy strategyDb = strategyService.getById(strategyVo.getId());
        if (strategyDb == null) {
            log.info("updateStrategy ???????????????-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        String currentUser = CurrentReqInfoUtil.currentUser();
        strategyVo.setTenantId(strategyDb.getTenantId());

        boolean validTenantId = TenantIdUtil.validTenantId(strategyVo.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("updateStrategy ???????????????-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        // ????????????(?????? dimension)
        List<String> collect = strategyVo.getDimensionList().stream().map(StrategyDimensionVo::getDimension).collect(Collectors.toList());
        List<Dimension> dimensionList = dimensionService.selectByDimensionKey(collect);
        // ???????????????
        List<StrategyRemarkVo> tipContent = strategyVo.getTipContent();

        ValidCodeMsg<ResponseCodeEnum> stringValidCodeMsg = validateSaveStrategyParam(strategyVo, dimensionList, tipContent, true);
        if (!stringValidCodeMsg.isOK()) {
            return ResponseHelper.buildFail(stringValidCodeMsg.getData());
        }
        //????????????????????????
        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyDb.getStatus())) {
            ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(strategyVo.getDimensionList());
            if (codeMsgTuple.isErr()) {
                log.info("?????????????????????");
                return ResponseHelper.buildFailWithErrorData(codeMsgTuple.getError(), codeMsgTuple.getData());
            }
        }

        long currentTimeMillis = System.currentTimeMillis();
        strategyDb.setName(strategyVo.getName());
        strategyDb.setType(strategyVo.getType());
        strategyDb.setBeginDate(strategyVo.getBeginDate());
        strategyDb.setEndDate(strategyVo.getEndDate());
        strategyDb.setUpdateTime(currentTimeMillis);
        strategyDb.setUpdater(currentUser);
        // ???????????????
        strategyDb.setPriority(strategyVo.getPriority());
        strategyDb.setVersion(StrategyVersionUtil.getVersion());
        strategyDb.setTipFlag(strategyVo.getTipFlag());

        boolean saved = strategyService.updateById(strategyDb);
        if (saved) {
            //
            StrategySaveVo strategySaveVo = StrategyConvert.convertDo2Vo(strategyDb);
            strategyVo.setId(strategyDb.getId());
            // ??????????????????
            strategyConditionService.updateStrategyCondition(strategyVo.getDimensionList(), strategySaveVo);


            // ?????? ??????????????? ???StrategyRemark???
            strategyRemarkService.updateStrategyLang(tipContent, strategySaveVo);

            //???????????????
            sendUpdateEvent(strategyVo.getId());

            return ResponseHelper.buildSuccess(strategyVo.getId()+"");
        }

        return ResponseHelper.buildFail(ResponseCodeEnum.FAILURE);
    }

    @Override
    public Response<StrategyDetailVo> getStrategyDetail(long id) {
        StrategyDetailVo strategyDetail = strategyService.getStrategyDetail(id);
        if (strategyDetail == null) {
            log.info("getStrategyDetail ???????????????-id={}", id);
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        return ResponseHelper.buildSuccess(strategyDetail);
    }

    @Override
    public Response<String> updateStatus(StrategyUpdateStatusVo strategyVo) {
        StrategyDetailVo strategy = strategyService.getStrategyDetail(strategyVo.getId());
        if (strategy == null) {
            log.info("updateStrategy ???????????????-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        boolean validTenantId = TenantIdUtil.validTenantId(strategy.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("updateStrategy ???????????????-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        if (!StrategyTypeEnum.contains(strategy.getType())) {
            log.info("updateStrategy ??????????????????-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        // ????????????????????????
        List<StrategyRemarkVo> tipContent = strategy.getTipContent();
        if (!validateStrategyLang(strategyVo.getId(), tipContent).isOK()) {
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        //????????????????????????
        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyVo.getStatus())) {
            ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(strategy.getDimensionList());
            if (codeMsgTuple.isErr()) {
                log.info("?????????????????????");
                return ResponseHelper.buildFailWithErrorData(codeMsgTuple.getError(), codeMsgTuple.getData());
            }
        }


        StrategySaveVo strategySaveVo = strategyService.updateStatus(strategyVo.getId(), strategyVo.getStatus());

        if (strategySaveVo != null){
            //?????? ?????? ?????? ????????? ???????????????
            sendUpdateEvent(strategyVo.getId());

          return ResponseHelper.buildSuccess(StringUtils.EMPTY);
        }

       return ResponseHelper.buildSuccess(StringUtils.EMPTY);

    }



    @Override
    public Page<StrategyPageVo> queryPage(StrategyVoQueryReq query) {
        query.setTenantId(CurrentReqInfoUtil.currentTenantId());
        if (StringUtils.isBlank(CurrentReqInfoUtil.currentLang())) {
            query.setLang(LanguageEnum.EN.getLang());
        } else {
            query.setLang(CurrentReqInfoUtil.currentLang());
        }
        return strategyService.queryPage(query);
    }

    @Override
    public Response<String> strategySetting(StrategyDetailVo strategyDetailVo) {
        Strategy dbBean = strategyService.getById(strategyDetailVo.getId());
        if (dbBean == null) {
            log.info("strategySetting ???????????????-id={}", strategyDetailVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        boolean validTenantId = TenantIdUtil.validTenantId(dbBean.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("strategySetting ???????????????-id={}", strategyDetailVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        List<StrategyDimensionVo> dimensionList = strategyDetailVo.getDimensionList();

        //????????????????????????
        // code ??? ?????????dimensionName
        ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(dimensionList);
        if (codeMsgTuple.isErr()) {
            log.info("?????????????????????{}", codeMsgTuple.getData());
            return ResponseHelper.buildFailWithErrorData(codeMsgTuple.getError(), codeMsgTuple.getData());
        }

        List<StrategyCondition> conditions = StrategyDimensionConvert.convertVo2DoList(dimensionList);
        strategyConditionService.updateBatchById(conditions);

        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(dbBean.getStatus())) {
            //?????????????????? ????????????  ?????? ???????????????

            sendUpdateEvent(strategyDetailVo.getId());

        }

        return ResponseHelper.buildSuccess(StringUtils.EMPTY);
    }


    private void sendUpdateEvent(Long strategyId) {
        if (TransactionSynchronizationManager.isActualTransactionActive()){
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    applicationEventPublisher.publishEvent(new StrategyUpdateEvent(this,strategyId));
                }
            });
        }else {
            applicationEventPublisher.publishEvent(new StrategyUpdateEvent(this,strategyId));
        }
    }

    /**
     * ?????????????????????
     * @param dimensionList
     * @return <Code, dimensionName>
     */
    private ValidCodeMsgTuple<ResponseCodeEnum, String> conditionsIntegrityCheck(List<StrategyDimensionVo> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            log.info("??????????????????");
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        int maxSize = 6;
        if (dimensionList.size() > maxSize) {
            log.info("??????????????????6");
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        for (StrategyDimensionVo strategyDimensionVo : dimensionList) {
            String dimensionName = strategyDimensionVo.getDimension();
            if (StringUtils.isBlank(dimensionName)) {
                log.info("?????????????????????strategyDimension={}", dimensionName);
                return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_INPUT_ERROR);
            }
            Dimension dimension = dimensionService.mapAll().get(dimensionName);
            if(dimension.getCurrencyFlag() != null && CommonStatusEnum.STATUS_ENABLED.getStatus().equals(dimension.getCurrencyFlag())) {
                if (StringUtils.isBlank(strategyDimensionVo.getCurrency())) {
                    log.info("???????????????????????????strategyDimension={}", dimensionName);
                    return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_SETTING_CURRENCY_ERROR, dimensionName);
                }
            }
            // ?????????????????????type  ??????????????????validator?????????
            // ValidCodeMsgTuple<ResponseCodeEnum, String> : ?????? code ??? ??????????????????type??????
            ValidCodeMsgTuple<ResponseCodeEnum, String> validatorInstanceTypeTuple =
                    StrategyDimensionGetValidatorUtil.getValidatorInstanceType(strategyDimensionVo);
            if (validatorInstanceTypeTuple.isErr()) {
                log.info("??????function??????parameter???????????????????????????strategyDimension={}, function={}, parameter={}",
                        dimensionName, strategyDimensionVo.getFunction(), strategyDimensionVo.getParameter());
                return ValidCodeMsgTuple.error(validatorInstanceTypeTuple.getError(), dimensionName);
            }
            // ?????????type
            String validatorInstanceType = validatorInstanceTypeTuple.getData();
            // ??????????????????type???????????????????????????????????????
            IStrategyDimensionValidator strategyDimensionValidator = dimensionValidatorMap.get(validatorInstanceType);
            ValidCodeMsg<ResponseCodeEnum> validate = strategyDimensionValidator.validate(strategyDimensionVo);
            if (validate.isErr()) {
                log.info("??????parameter???????????????,strategyDimension={}", JSON.toJSONString(strategyDimensionVo));
                return ValidCodeMsgTuple.error(validate.getData(), dimensionName);
            }
        }
        return ValidCodeMsgTuple.success();
    }

}
