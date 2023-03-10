package com.sf.saas.bps.core.service.strategy.match;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.emnus.StrategyMatchHandlerType;
import com.sf.saas.bps.core.dto.enums.StrategyStatusEnum;
import com.sf.saas.bps.core.dto.enums.StrategyTypeEnum;
import com.sf.saas.bps.core.service.strategy.StrategyConditionMatchHandler;
import com.sf.saas.bps.core.service.strategy.StrategyMatchPoolUtil;
import com.sf.saas.bps.core.service.strategy.bo.StrategyConditionMatchBo;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 01407460
 * @date 2022/9/14 19:03
 */
@Slf4j
@Service
public class StrategyMatchHandlerImpl implements IStrategyMatchHandler {

    @Autowired
    private StrategyConditionMatchHandler conditionMatchHandler;

    @Override
    public String getTenantType() {
        return StrategyMatchHandlerType.SAAS.toString();
    }


    @Override
    public Collection<StrategyMatchBo> controlCheck(JSONObject paramsObject) {
        if (null == paramsObject) {
            log.warn("[controlCheck]..JSONObject is  null , ignored!");
            return Collections.emptyList();
        }
        String tenantId = paramsObject.getString(BpsConstant.HEADER_TENANT_ID);
        if (StringUtils.isBlank(tenantId)) {
            log.warn("[controlCheck]..JSONObject tenantId isBlank , ignored!");
            return Collections.emptyList();
        }


        StrategyMatchPool strategyPool = StrategyMatchPoolUtil.strategyPool(tenantId);

        log.info("[controlCheck]...[podHost is {}],[strategyPool] is {}", NetUtil.getLocalHostName(),JSON.toJSONString(strategyPool));

       return doControlCheck(paramsObject, strategyPool);
    }


    /**
     * @param paramsObject
     * @param strategyPool step1  ??????????????????
     *                     step2  ????????????
     *                     step3 ??????????????????
     *                     step4 ??????????????????
     *                     step5 ??????
     */
    private Collection<StrategyMatchBo> doControlCheck(JSONObject paramsObject, StrategyMatchPool strategyPool) {
        long now = System.currentTimeMillis();
        log.info("doControlCheck paramsObject is {},now time is {}", paramsObject.toJSONString(), now);

        if (Objects.isNull(strategyPool) || CollectionUtils.isEmpty(strategyPool.getStrategyMatchBos())){
            log.info("doControlCheck strategyPool is isEmpty,so return...");
            return Collections.emptyList();
        }

        List<StrategyMatchBo> strategyMatchBos = strategyPool.getStrategyMatchBos();
        Map<String, StrategyMatchBo> hasMatchedType = new HashMap<>(4);



        for (StrategyMatchBo strategyMatchBo : strategyMatchBos) {

            if (checkBreak(hasMatchedType)){
                return hasMatchedType.values();
            }

            boolean match = false;
            try {
                match = doControlCheck0(paramsObject, hasMatchedType, strategyMatchBo, now);
            } catch (Exception e) {
                log.error("match strategy found error skip this one,strategyMatchBo json is {} ", JSON.toJSONString(strategyMatchBo),e);
            }

            if (match){
                //?????????
                hasMatchedType.put(strategyMatchBo.getType(),strategyMatchBo);
            }
        }

        // ???????????????????????? ?????? ??????????????????????????????
        return hasMatchedType.values();
    }

    /**
     *    ????????????
     * @param paramsObject
     * @param hasMatchedType
     * @param strategyMatchBo
     * @param now
     * @return
     */
    private boolean doControlCheck0(JSONObject paramsObject, Map<String, StrategyMatchBo> hasMatchedType, StrategyMatchBo strategyMatchBo, long now) {

        //step1 ??????
        if (hasMatchedType.containsKey(strategyMatchBo.getType())) {
            return false;
        }

        //step1 ??????
        if (!StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyMatchBo.getStatus())) {
            return false;
        }
        //step3 ??????????????????  ???????????? ??????????????? ???????????????
        // ?????? ???????????? ??? ?????? ??????
        Long beginDate = strategyMatchBo.getBeginDate();
        Long endDate = strategyMatchBo.getEndDate();
        if (Objects.nonNull(beginDate) && Objects.nonNull(endDate)) {
            // ????????? ???????????????
            if (now < beginDate || now >= endDate) {
                return false;
            }
        }
        //step4 ????????????
        List<StrategyConditionMatchBo> conditionBos = strategyMatchBo.getConditionBos();
        return conditionMatchHandler.doConditionMatch(paramsObject, conditionBos);

    }

    /**
     * @param hasMatchedType
     * @return
     */
    private boolean checkBreak(Map<String, StrategyMatchBo> hasMatchedType) {
        boolean b = hasMatchedType.containsKey(StrategyTypeEnum.REJECT.getType());
        if (b){
            return true;
        }
        return hasMatchedType.containsKey(StrategyTypeEnum.CHARGE.getType()) && hasMatchedType.containsKey(StrategyTypeEnum.PROMPT.getType());
    }

}
