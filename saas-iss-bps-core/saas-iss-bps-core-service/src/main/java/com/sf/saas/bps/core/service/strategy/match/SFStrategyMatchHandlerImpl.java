package com.sf.saas.bps.core.service.strategy.match;

import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.common.emnus.StrategyMatchHandlerType;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * @author 01407460
 * @date 2022/9/19 10:34
 */

@Slf4j
@Service
public class SFStrategyMatchHandlerImpl implements IStrategyMatchHandler {
    @Override
    public String getTenantType() {
        return StrategyMatchHandlerType.SF.toString();
    }

    @Override
    public Collection<StrategyMatchBo> controlCheck(JSONObject paramsObject) {

        log.warn("自营 待接入");
        return Collections.emptyList();
    }
}
