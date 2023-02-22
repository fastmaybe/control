package com.sf.saas.bps.core.service.strategy.bo;

import lombok.Data;

import java.util.List;

/**
 * @author 01407460
 * @date 2022/9/14 18:17
 */
@Data
public class StrategyMatchPool {

    private String tenantId;

    private List<StrategyMatchBo> strategyMatchBos;


    public StrategyMatchPool(String tenantId, List<StrategyMatchBo> strategyMatchBos) {
        this.tenantId = tenantId;
        this.strategyMatchBos = strategyMatchBos;
    }
}
