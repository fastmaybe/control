package com.sf.saas.bps.core.service.strategy.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 01407460
 * @date 2022/9/14 16:55
 */
@Data
public class ConditionNumParams extends ConditionBaseNumParams  implements ConditionParams {


    /**
     * 与 b1 成对出现  是否需要处理 临界点 目前只存在在 between combinationInterval两种区间
     */
    private boolean criticalFlag1;

    /**
     * 与 b2 成对出现  是否需要处理 临界点
     */
    private boolean criticalFlag2;


    private BigDecimal b2;
}
