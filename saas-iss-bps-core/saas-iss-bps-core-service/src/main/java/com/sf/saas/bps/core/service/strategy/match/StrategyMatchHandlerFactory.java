package com.sf.saas.bps.core.service.strategy.match;

import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.emnus.StrategyMatchHandlerType;
import com.sf.saas.bps.core.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 01407460
 * @date 2022/9/19 10:35
 */

@Slf4j
@Component
public class StrategyMatchHandlerFactory implements InitializingBean {

    @Autowired
    private List<IStrategyMatchHandler> strategyMatchHandlers;

    private Map<String, IStrategyMatchHandler> factory = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        for (IStrategyMatchHandler handler : strategyMatchHandlers) {
            factory.put(handler.getTenantType(), handler);
        }
        // Saas默认处理器
        if (!factory.containsKey(StrategyMatchHandlerType.SAAS.toString())) {
            log.error("Expect a saas StrategyMatchHandlerImpl,but no one. ...");
            throw new BizException(ResponseCodeEnum.STRATEGY_SAAS_HANDLER_NOT_EXIST);
        }
    }

    public IStrategyMatchHandler attachHandler(String tenantId) {
        return factory.containsKey(tenantId) ? factory.get(tenantId) : factory.get(StrategyMatchHandlerType.SAAS.toString());
    }
}
