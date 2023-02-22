package com.sf.saas.bps.core.service.event.redis.handler;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.service.crud.IStrategyService;
import com.sf.saas.bps.core.service.event.redis.message.MessageBase;
import com.sf.saas.bps.core.service.event.redis.message.bizmsg.StrategyCacheMessage;
import com.sf.saas.bps.core.service.strategy.StrategyMatchBoBuilder;
import com.sf.saas.bps.core.service.strategy.StrategyMatchPoolUtil;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 01407460
 * @date 2022/9/8 17:15
 */
@Slf4j
@Component
public class StrategyCacheMessageHandler implements  IMessageHandler<MessageBase> {

    @Autowired
    private IStrategyService strategyService;


    @Override
    public boolean support(MessageBase messageBase) {
        return messageBase instanceof StrategyCacheMessage;
    }

    @Override
    public void onMessage(MessageBase messageBase) {
        StrategyCacheMessage cacheMessage = (StrategyCacheMessage) messageBase;
        log.info("[pod={}]-[redis message-StrategyCacheMessage] is {} .",NetUtil.getLocalHostName(), JSON.toJSON(cacheMessage));

        //do something
        boolean needIgnore = needIgnore(cacheMessage.getSourceHostName());
        if (needIgnore){
            log.info("[pod={}] StrategyCacheMessageHandler found source is self so return", NetUtil.getLocalHostName());
            return;
        }

        // step2 刷新策略
        Long strategyId = cacheMessage.getStrategyId();
        StrategyDetailVo strategyDetail = strategyService.getStrategyDetail(strategyId);
        StrategyMatchBo matchBo = StrategyMatchBoBuilder.builder(strategyDetail);
        StrategyMatchPoolUtil.refreshStrategy(matchBo);
    }
}
