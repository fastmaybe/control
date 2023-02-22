package com.sf.saas.bps.core.service.strategy;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.service.crud.IStrategyService;
import com.sf.saas.bps.core.service.event.redis.message.MessageBase;
import com.sf.saas.bps.core.service.event.redis.message.RedisPublisher;
import com.sf.saas.bps.core.service.event.redis.message.bizmsg.StrategyCacheMessage;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import com.sf.saas.bps.core.service.event.spring.StrategyUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author 01407460
 * @date 2022/9/14 20:59
 */
@Slf4j
@Async("strategyUpdateExecutor")
@Component
public class StrategyUpdateEventHandler {

    @Autowired
    private RedisPublisher<MessageBase> redisPublisher;

    @Autowired
    private IStrategyService strategyService;


    @EventListener
    public void strategyUpdateEventHProcess(StrategyUpdateEvent event){
        // do StrategyMatchUtil 刷新 remove or add
        //  生效 代表加入  失效代表移除
        //
        StrategyDetailVo strategyDetail = strategyService.getStrategyDetail(event.getId());
        if (Objects.isNull(strategyDetail)){
            log.warn("strategyUpdateEventHProcess no strategyDetail ID is {}",event.getId());
            return;
        }

        log.info("strategyUpdateEventHProcess [pod host is {}],now strategyDetail is {}", NetUtil.getLocalHostName(), JSON.toJSONString(strategyDetail));

        // step2 发redis事件  包含租户  被修改的策略ID  来源地 加入还是移除

        StrategyCacheMessage cacheMessage = new StrategyCacheMessage();
        cacheMessage.setStrategyId(strategyDetail.getId());
        cacheMessage.setStrategyVersion(strategyDetail.getVersion());
        redisPublisher.publish(cacheMessage);


        // step3 刷新 进行加入或者移除
        StrategyMatchBo strategyMatchBo = StrategyMatchBoBuilder.builder(strategyDetail);
        StrategyMatchPoolUtil.refreshStrategy(strategyMatchBo);

    }
}
