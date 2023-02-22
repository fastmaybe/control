package com.sf.saas.bps.core.service.strategy.predicatepool;

import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.service.strategy.bo.ConditionBaseNumParams;
import com.sf.saas.bps.core.service.strategy.bo.ConditionParams;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author 01407460
 * @date 2022/9/14 15:48
 */
@Component
public class LTBiPredicate implements CondBiPredicate {

    @Override
    public boolean test(String orderParam, ConditionParams conditionParams) {
        if (conditionParams instanceof ConditionBaseNumParams){
            BigDecimal conditionB1 = ((ConditionBaseNumParams) conditionParams).getB1();
            return new BigDecimal(orderParam).compareTo(conditionB1) < 0;
        }
        return false;
    }

    @Override
    public String getType() {
        return StrategyFunctionEnum.LT.getFunction();
    }
}
