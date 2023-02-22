package com.sf.saas.bps.core.api;

import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.dto.base.AppResponse;
import com.sf.saas.bps.core.dto.res.StrategyMatchDto;

import java.util.List;

/**
 * @author 01407460
 * @date 2022/9/16 10:58
 */
public interface IStrategyControlBizService {

    /**
     * 匹配
     * @param jsonObject
     * @return
     */
    AppResponse<List<StrategyMatchDto>> strategyMatchDispatch(JSONObject jsonObject, String lang);
}
