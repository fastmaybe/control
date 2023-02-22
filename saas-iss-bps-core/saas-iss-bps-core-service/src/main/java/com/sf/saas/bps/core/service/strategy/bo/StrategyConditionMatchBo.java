package com.sf.saas.bps.core.service.strategy.bo;

import lombok.Data;

/**
 * @author 01407460
 * @date 2022/9/14 16:40
 *
 */
@Data
public class StrategyConditionMatchBo {


    /**
     * 自增id
     */
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 条件维度
     */
    private String dimension;

    /**
     * 条件方法
     */
    private String function;

    /**
     * 条件参数
     */
    private String parameter;

    /**
     * 币种
     */
    private String currency;


    /**
     * 解析对象参数
     */
    private ConditionParams conditionParams;
}
