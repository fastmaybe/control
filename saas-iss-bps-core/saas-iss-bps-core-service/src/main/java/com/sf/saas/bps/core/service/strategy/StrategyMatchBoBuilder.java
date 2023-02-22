package com.sf.saas.bps.core.service.strategy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategyRemarkVo;
import com.sf.saas.bps.core.service.strategy.bo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author 01407460
 * @date 2022/9/15 11:11
 */
@Slf4j
public class StrategyMatchBoBuilder {

    private StrategyMatchBoBuilder(){}

    public static StrategyMatchBo builder(StrategyDetailVo strategyDetailVo) {
        if (Objects.isNull(strategyDetailVo)) {
            return null;
        }
        try {
            StrategyMatchBo bo = new StrategyMatchBo();
            //step1 基础信息
            buildBaseInfo(bo, strategyDetailVo);
            // step2 策略条件
            buildConditionBos(bo, strategyDetailVo.getDimensionList());
            // step3 语言信息
            buildTipMap(bo, strategyDetailVo.getTipContent());
            // step4
            return bo;
        } catch (Exception e) {
            log.error("[build error] builder error strategyDetailVo(ID={},name={}) json is {}",strategyDetailVo.getId(),strategyDetailVo.getName(), JSON.toJSONString(strategyDetailVo), e);
        }
        return null;
    }



    private static void buildConditionBos(StrategyMatchBo bo, List<StrategyDimensionVo> dimensionList) {
        List<StrategyConditionMatchBo> conditionBos = new ArrayList<>();
        bo.setConditionBos(conditionBos);
        if (CollectionUtils.isEmpty(dimensionList)) {
            log.error("[build error] buildConditionBos found dimensionList is isEmpty Strategy(ID={},name={}))",bo.getId(),bo.getName());
            return;
        }
        for (StrategyDimensionVo dimensionVo : dimensionList) {
            StrategyConditionMatchBo cbo = new StrategyConditionMatchBo();
            cbo.setTenantId(dimensionVo.getTenantId());
            cbo.setStrategyId(dimensionVo.getStrategyId());
            cbo.setDimension(dimensionVo.getDimension());
            cbo.setFunction(dimensionVo.getFunction());
            cbo.setParameter(dimensionVo.getParameter());
            cbo.setCurrency(dimensionVo.getCurrency());

            cbo.setId(dimensionVo.getId());

            try {
                ConditionParams conditionParams = buildCondParams(dimensionVo.getFunction(), dimensionVo.getParameter());
                if (Objects.isNull(conditionParams)){
                    log.error("[build error] buildCondParams found return null Strategy(ID={},name={}))",bo.getId(),bo.getName());
                }
                cbo.setConditionParams(conditionParams);
            } catch (Exception e) {
                log.error("[build error] buildCondParams error ,StrategyId is {},dimensionVo id is {}, parameter is {},simple error msg is {}. "
                        ,dimensionVo.getStrategyId(),dimensionVo.getId(),dimensionVo.getParameter(),e.getMessage(),e);
            }

            conditionBos.add(cbo);
        }

    }

    private static void buildTipMap(StrategyMatchBo bo, List<StrategyRemarkVo> tipContent) {
        Map<String, StrategyRemarkVo> tipRemark = new HashMap<>();
        bo.setStrategyRemarkMap(tipRemark);
        if (CollectionUtils.isEmpty(tipContent)) {
            return;
        }
        for (StrategyRemarkVo remarkVo : tipContent) {
            if (StringUtils.isNotBlank(remarkVo.getRemark())){
                tipRemark.put(remarkVo.getLang(),remarkVo);
            }
        }
    }


    private static void buildBaseInfo(StrategyMatchBo bo, StrategyDetailVo vo) {
        bo.setId(vo.getId());
        bo.setTenantId(vo.getTenantId());
        bo.setName(vo.getName());
        bo.setStatus(vo.getStatus());
        bo.setVersion(vo.getVersion());
        bo.setBeginDate(vo.getBeginDate());
        bo.setEndDate(vo.getEndDate());
        bo.setType(vo.getType());
        bo.setEnableDate(vo.getEnableDate());
        bo.setDescription(vo.getDescription());
        bo.setCreateTime(vo.getCreateTime());
        bo.setPositive(vo.getPositive());
        bo.setPriority(vo.getPriority());
        bo.setTipFlag(vo.getTipFlag());
    }

    /**
     * 构建ConditionParams
     * @param function 方法
     * @param parameter 参数类型
     * @return
     */
    private static ConditionParams buildCondParams(String function, String parameter) {
        JSONArray jsonArray = JSON.parseArray(parameter);
        StrategyFunctionEnum functionEnum = StrategyFunctionEnum.getByFunction(function);
        if (Objects.isNull(functionEnum)){
            return null;
        }
        switch (functionEnum){
            case GE:
            case GT:
            case LT:
            case LE:
                ConditionBaseNumParams params1 = new ConditionBaseNumParams();
                params1.setB1(jsonArray.getBigDecimal(0));
                return params1;
            case BETWEEN:
                //  x < ? < Y
                ConditionNumParams params20 = new ConditionNumParams();
                params20.setCriticalFlag1(StrategyFunctionEnum.LE.getFunction().equals(jsonArray.getString(0)));
                params20.setCriticalFlag2(StrategyFunctionEnum.LE.getFunction().equals(jsonArray.getString(1)));
                params20.setB1(jsonArray.getBigDecimal(2));
                params20.setB2(jsonArray.getBigDecimal(3));
                return params20;
            case COMBINATION_INTERVAL:
                // ? < b1 或者  ? > b2
                ConditionNumParams params300 = new ConditionNumParams();
                params300.setCriticalFlag1(StrategyFunctionEnum.LE.getFunction().equals(jsonArray.getString(0)));
                params300.setCriticalFlag2(StrategyFunctionEnum.GE.getFunction().equals(jsonArray.getString(1)));
                params300.setB1(jsonArray.getBigDecimal(2));
                params300.setB2(jsonArray.getBigDecimal(3));
                return params300;
            case IN:
            case NOT_IN:
                // ? < x 或者  ? > Y
                ConditionStringParams params4000 = new ConditionStringParams();
                HashSet<String> paramsSet = new HashSet<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    paramsSet.add(jsonArray.getString(i));
                }
                params4000.setParamsSet(paramsSet);
                return params4000;
            default:
                return null;
        }
    }
}
