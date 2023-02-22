package com.sf.saas.bps.core.service.strategy.predicatepool;

import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.service.strategy.bo.ConditionParams;
import com.sf.saas.bps.core.service.strategy.bo.ConditionStringParams;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author 01407460
 * @date 2022/9/14 15:54
 */
@Component
public class InBiPredicate implements CondBiPredicate {


    @Override
    public boolean test(String orderParam, ConditionParams conditionParams) {
        if (conditionParams instanceof ConditionStringParams){
            Set<String> paramsSet = ((ConditionStringParams) conditionParams).getParamsSet();
            return paramsSet.contains(orderParam);
        }
        return false;
    }

    @Override
    public String getType() {
        return StrategyFunctionEnum.IN.getFunction();
    }
}
