package com.sf.saas.bps.core.service.strategy.predicatepool;

import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.service.strategy.bo.ConditionNumParams;
import com.sf.saas.bps.core.service.strategy.bo.ConditionParams;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author 01407460
 * @date 2022/9/14 17:03
 * 组合区间    <100 或者 大于 100
 */
@Component
public class NotBetweenBiPredicate implements CondBiPredicate {


    /**
     * 反区间  小于 5 （b1） 或者  大于100 （b2）
     *
     * @param orderParam
     * @param conditionParams
     * @return
     */
    @Override
    public boolean test(String orderParam, ConditionParams conditionParams) {
        if (conditionParams instanceof ConditionNumParams) {
            boolean criticalFlag1 = ((ConditionNumParams) conditionParams).isCriticalFlag1();
            boolean criticalFlag2 = ((ConditionNumParams) conditionParams).isCriticalFlag2();

            BigDecimal b1 = ((ConditionNumParams) conditionParams).getB1();
            BigDecimal b2 = ((ConditionNumParams) conditionParams).getB2();

            BigDecimal orderParamsNum = new BigDecimal(orderParam);

            return (criticalFlag1 ? orderParamsNum.compareTo(b1) <= 0 : orderParamsNum.compareTo(b1) < 0)
                    || (criticalFlag2 ? orderParamsNum.compareTo(b2) >= 0 : orderParamsNum.compareTo(b2) > 0);
        }

        return false;
    }

    @Override
    public String getType() {
        return StrategyFunctionEnum.COMBINATION_INTERVAL.getFunction();
    }
}
