package com.sf.saas.bps.core.service.strategy.predicatepool;

import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.service.strategy.bo.ConditionBaseNumParams;
import com.sf.saas.bps.core.service.strategy.bo.ConditionParams;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author 01407460
 * @date 2022/9/14 10:21
 */
@Component
public class GTBiPredicate implements CondBiPredicate {


    /**
     * 大于  -> orderParam  > conditionParams
     * @param orderParam 订单入参
     * @param conditionParams  条件参数
     * @return
     */
    @Override
    public boolean test(String orderParam, ConditionParams conditionParams) {
        if (conditionParams instanceof ConditionBaseNumParams){
            BigDecimal b1 = ((ConditionBaseNumParams) conditionParams).getB1();
            return new BigDecimal(orderParam).compareTo(b1) > 0;
        }
        return false;
    }

    @Override
    public String getType() {
        return StrategyFunctionEnum.GT.getFunction();
    }
}
