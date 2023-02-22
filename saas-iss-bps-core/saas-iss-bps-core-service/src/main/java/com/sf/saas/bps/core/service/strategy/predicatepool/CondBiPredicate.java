package com.sf.saas.bps.core.service.strategy.predicatepool;

import com.sf.saas.bps.core.service.strategy.bo.ConditionParams;

import java.util.function.BiPredicate;

/**
 * @author 01407460
 * @date 2022/9/14 20:15
 */
public interface CondBiPredicate extends BiPredicate<String, ConditionParams> {

    String getType();
}
