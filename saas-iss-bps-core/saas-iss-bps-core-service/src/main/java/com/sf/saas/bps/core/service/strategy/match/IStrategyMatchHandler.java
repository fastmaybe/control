package com.sf.saas.bps.core.service.strategy.match;

import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;

import java.util.Collection;

/**
 * @author 01407460
 * @date 2022/9/19 10:26
 */
public interface IStrategyMatchHandler {


    /**
     * 租户隔离
     * @return
     */
    String getTenantType();


    /**
     * 匹配
     * @return
     */
    Collection<StrategyMatchBo> controlCheck(JSONObject paramsObject);


}