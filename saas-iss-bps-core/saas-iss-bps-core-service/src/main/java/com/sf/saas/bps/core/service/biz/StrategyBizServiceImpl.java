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
     * 保存策略
     * 保存策略维度
     * 保存语言
     * @param strategyVo
     * @return
     */
    @Transactional
    @Override
    public Response<String> saveStrategy(StrategySaveVo strategyVo) {
        String tenantId = CurrentReqInfoUtil.currentTenantId();
        String currentUser = CurrentReqInfoUtil.currentUser();
        // 策略维度
        List<String> collect = strategyVo.getDimensionList().stream().map(StrategyDimensionVo::getDimension).collect(Collectors.toList());
        List<Dimension> dimensionList = dimensionService.selectByDimensionKey(collect);
        // 策略多语言
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
        // 默认未生效
        strategyVo.setStatus(StrategyStatusEnum.INEFFECTIVE.getStatus());
        // 策略优先级
        strategyVo.setPriority(strategyVo.getPriority());
        strategyVo.setVersion(StrategyVersionUtil.getVersion());
        strategyVo.setTenantId(tenantId);
        strategyVo.setTipFlag(strategyVo.getTipFlag());

        StrategySaveVo strategyVoSaved = strategyService.saveStrategy(strategyVo);
        if (strategyVoSaved != null) {
            // 策略保存成功
            strategyConditionService.saveStrategyCondition(strategyVo.getDimensionList(), strategyVoSaved);

            // 保存 策略多语言 （StrategyRemark）
            strategyRemarkService.saveStrategyLang(tipContent, strategyVoSaved);

            return ResponseHelper.buildSuccess(strategyVoSaved.getId()+"");
        }
        return ResponseHelper.buildFail(ResponseCodeEnum.FAILURE);
    }

    /**
     * 验证策略语言 参数
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
            log.info("saveStrategy 策略多语言 中英文 不能为空 strategyId={},tipContent={}", strategyId, JSON.toJSONString(tipContent));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        return ValidCodeMsg.success();
    }

    /**
     * 验证策略语言 参数
     * @param strategyVo
     * @param tipContent
     * @param isUpdate 是否是修改保存
     * @return
     */
    private ValidCodeMsg<ResponseCodeEnum> validateSaveStrategyParam(StrategySaveVo strategyVo, List<Dimension> dimensionList,
                                                                     List<StrategyRemarkVo> tipContent, boolean isUpdate) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        if (dimensionList.size() != strategyVo.getDimensionList().size()) {
            log.info("saveStrategy 策略维度选择非法 reqList={}, dbList={}", JSON.toJSONString(strategyVo.getDimensionList()), JSON.toJSONString(dimensionList));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_INPUT_ERROR);
        }
        StrategyTypeEnum strategyTypeEnum = StrategyTypeEnum.getStrategyTypeEnumByValue(strategyVo.getType());
        if (strategyTypeEnum == null) {
            log.info("saveStrategy 策略类型非法 strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        StrategyPriorityEnum strategyPriorityEnum = StrategyPriorityEnum.getByStrategyType(strategyTypeEnum);
        if (strategyPriorityEnum == null) {
            log.info("saveStrategy 策略类型非法 strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        boolean validateTipFlag = StrategyTipFlagEnum.validateTipFlag(strategyTypeEnum, strategyVo.getTipFlag());
        if (!validateTipFlag) {
            log.info("saveStrategy 策略是否需要提示选择有误 strategyTipFlag={}", strategyVo.getTipFlag());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_TIP_FLAG_INPUT_ERROR);
        }

        // 处理前端 策略类型 得到优先级
        strategyVo.setPriority(strategyPriorityEnum.getPriority());

        // 语言校验开始==
        if (CollectionUtils.isEmpty(tipContent)) {
            log.info("saveStrategy 策略多语言不能为空 strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_INPUT_ERROR);
        }
        if (!validateStrategyLang(strategyVo.getId(), tipContent).isOK()) {
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        // 语言校验结束==

        // 校验是否存在策略名（全局）
        StrategySaveVo strategyExist = strategyService.selectByStrategyName(strategyVo.getName(), CurrentReqInfoUtil.currentTenantId());
        if (isUpdate) {
            // 修改策略名 可以和自己原本一样 但是不能和别人的一样
            Strategy serviceById = strategyService.getById(strategyVo.getId());
            if (serviceById == null) {
                return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_NOT_EXIST);
            }
            if (strategyExist != null) {
                // 修改可以用自己原来的名字，但是不能和别的同名
                strategyExist = strategyService.selectOtherByStrategyName(strategyVo.getName(), serviceById.getId(), CurrentReqInfoUtil.currentTenantId());
            }
        }
        // 名字不能重复
        if (strategyExist != null) {
            log.info("saveStrategy 策略多语言不能为空 strategyType={}", strategyVo.getType());
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_NAME_SAME_ERROR);
        }

        return ValidCodeMsg.success();
    }

    @Transactional
    @Override
    public Response<String> updateStrategy(StrategySaveVo strategyVo) {
        Strategy strategyDb = strategyService.getById(strategyVo.getId());
        if (strategyDb == null) {
            log.info("updateStrategy 策略不存在-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        String currentUser = CurrentReqInfoUtil.currentUser();
        strategyVo.setTenantId(strategyDb.getTenantId());

        boolean validTenantId = TenantIdUtil.validTenantId(strategyVo.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("updateStrategy 租户不匹配-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        // 策略维度(静态 dimension)
        List<String> collect = strategyVo.getDimensionList().stream().map(StrategyDimensionVo::getDimension).collect(Collectors.toList());
        List<Dimension> dimensionList = dimensionService.selectByDimensionKey(collect);
        // 策略多语言
        List<StrategyRemarkVo> tipContent = strategyVo.getTipContent();

        ValidCodeMsg<ResponseCodeEnum> stringValidCodeMsg = validateSaveStrategyParam(strategyVo, dimensionList, tipContent, true);
        if (!stringValidCodeMsg.isOK()) {
            return ResponseHelper.buildFail(stringValidCodeMsg.getData());
        }
        //校验条件是否规范
        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyDb.getStatus())) {
            ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(strategyVo.getDimensionList());
            if (codeMsgTuple.isErr()) {
                log.info("维度校验未通过");
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
        // 策略优先级
        strategyDb.setPriority(strategyVo.getPriority());
        strategyDb.setVersion(StrategyVersionUtil.getVersion());
        strategyDb.setTipFlag(strategyVo.getTipFlag());

        boolean saved = strategyService.updateById(strategyDb);
        if (saved) {
            //
            StrategySaveVo strategySaveVo = StrategyConvert.convertDo2Vo(strategyDb);
            strategyVo.setId(strategyDb.getId());
            // 策略保存成功
            strategyConditionService.updateStrategyCondition(strategyVo.getDimensionList(), strategySaveVo);


            // 保存 策略多语言 （StrategyRemark）
            strategyRemarkService.updateStrategyLang(tipContent, strategySaveVo);

            //更新策略池
            sendUpdateEvent(strategyVo.getId());

            return ResponseHelper.buildSuccess(strategyVo.getId()+"");
        }

        return ResponseHelper.buildFail(ResponseCodeEnum.FAILURE);
    }

    @Override
    public Response<StrategyDetailVo> getStrategyDetail(long id) {
        StrategyDetailVo strategyDetail = strategyService.getStrategyDetail(id);
        if (strategyDetail == null) {
            log.info("getStrategyDetail 策略不存在-id={}", id);
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        return ResponseHelper.buildSuccess(strategyDetail);
    }

    @Override
    public Response<String> updateStatus(StrategyUpdateStatusVo strategyVo) {
        StrategyDetailVo strategy = strategyService.getStrategyDetail(strategyVo.getId());
        if (strategy == null) {
            log.info("updateStrategy 策略不存在-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        boolean validTenantId = TenantIdUtil.validTenantId(strategy.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("updateStrategy 租户不匹配-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        if (!StrategyTypeEnum.contains(strategy.getType())) {
            log.info("updateStrategy 策略类型非法-id={}", strategyVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_TYPE_INPUT_ERROR);
        }
        // 语言必须有中英文
        List<StrategyRemarkVo> tipContent = strategy.getTipContent();
        if (!validateStrategyLang(strategyVo.getId(), tipContent).isOK()) {
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_LANG_EN_CN_CANNOT_NULL);
        }
        //校验条件是否规范
        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyVo.getStatus())) {
            ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(strategy.getDimensionList());
            if (codeMsgTuple.isErr()) {
                log.info("维度校验未通过");
                return ResponseHelper.buildFailWithErrorData(codeMsgTuple.getError(), codeMsgTuple.getData());
            }
        }


        StrategySaveVo strategySaveVo = strategyService.updateStatus(strategyVo.getId(), strategyVo.getStatus());

        if (strategySaveVo != null){
            //生效 或者 失效 都需要 更新策略池
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
            log.info("strategySetting 策略不存在-id={}", strategyDetailVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.STRATEGY_NOT_EXIST);
        }
        boolean validTenantId = TenantIdUtil.validTenantId(dbBean.getTenantId(), CurrentReqInfoUtil.currentTenantId(), CurrentReqInfoUtil.currentUser());
        if (!validTenantId) {
            log.info("strategySetting 租户不匹配-id={}", strategyDetailVo.getId());
            return ResponseHelper.buildFail(ResponseCodeEnum.TENANT_ID_IS_ERROR);
        }
        List<StrategyDimensionVo> dimensionList = strategyDetailVo.getDimensionList();

        //校验条件是否规范
        // code 和 错误的dimensionName
        ValidCodeMsgTuple<ResponseCodeEnum, String> codeMsgTuple = conditionsIntegrityCheck(dimensionList);
        if (codeMsgTuple.isErr()) {
            log.info("维度校验未通过{}", codeMsgTuple.getData());
            return ResponseHelper.buildFailWithErrorData(codeMsgTuple.getError(), codeMsgTuple.getData());
        }

        List<StrategyCondition> conditions = StrategyDimensionConvert.convertVo2DoList(dimensionList);
        strategyConditionService.updateBatchById(conditions);

        if (StrategyStatusEnum.EFFECTIVE.getStatus().equals(dbBean.getStatus())) {
            //在生效状态下 更新配置  需要 更新策略池

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
     * 校验维度有效性
     * @param dimensionList
     * @return <Code, dimensionName>
     */
    private ValidCodeMsgTuple<ResponseCodeEnum, String> conditionsIntegrityCheck(List<StrategyDimensionVo> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            log.info("策略维度为空");
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        int maxSize = 6;
        if (dimensionList.size() > maxSize) {
            log.info("维度数量大于6");
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_LIMIT_ERROR);
        }
        for (StrategyDimensionVo strategyDimensionVo : dimensionList) {
            String dimensionName = strategyDimensionVo.getDimension();
            if (StringUtils.isBlank(dimensionName)) {
                log.info("维度字段未配置strategyDimension={}", dimensionName);
                return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_INPUT_ERROR);
            }
            Dimension dimension = dimensionService.mapAll().get(dimensionName);
            if(dimension.getCurrencyFlag() != null && CommonStatusEnum.STATUS_ENABLED.getStatus().equals(dimension.getCurrencyFlag())) {
                if (StringUtils.isBlank(strategyDimensionVo.getCurrency())) {
                    log.info("维度数量币种未配置strategyDimension={}", dimensionName);
                    return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_SETTING_CURRENCY_ERROR, dimensionName);
                }
            }
            // 返回一个自定义type  获取到对应的validator处理器
            // ValidCodeMsgTuple<ResponseCodeEnum, String> : 返回 code 和 获取校验器的type类型
            ValidCodeMsgTuple<ResponseCodeEnum, String> validatorInstanceTypeTuple =
                    StrategyDimensionGetValidatorUtil.getValidatorInstanceType(strategyDimensionVo);
            if (validatorInstanceTypeTuple.isErr()) {
                log.info("维度function或者parameter未配置或配置错误，strategyDimension={}, function={}, parameter={}",
                        dimensionName, strategyDimensionVo.getFunction(), strategyDimensionVo.getParameter());
                return ValidCodeMsgTuple.error(validatorInstanceTypeTuple.getError(), dimensionName);
            }
            // 校验器type
            String validatorInstanceType = validatorInstanceTypeTuple.getData();
            // 通过【校验器type】获取对应类型的校验执行器
            IStrategyDimensionValidator strategyDimensionValidator = dimensionValidatorMap.get(validatorInstanceType);
            ValidCodeMsg<ResponseCodeEnum> validate = strategyDimensionValidator.validate(strategyDimensionVo);
            if (validate.isErr()) {
                log.info("维度parameter校验未通过,strategyDimension={}", JSON.toJSONString(strategyDimensionVo));
                return ValidCodeMsgTuple.error(validate.getData(), dimensionName);
            }
        }
        return ValidCodeMsgTuple.success();
    }

}
