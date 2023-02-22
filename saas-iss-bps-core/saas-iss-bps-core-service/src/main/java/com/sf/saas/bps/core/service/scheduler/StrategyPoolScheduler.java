package com.sf.saas.bps.core.service.scheduler;

import com.sf.saas.bps.core.service.strategy.IStrategyPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 01407460
 * @date 2022/9/15 19:55
 */
@Slf4j
@Component
public class StrategyPoolScheduler {


    @Autowired
    private IStrategyPoolService strategyPoolService;

    /**
     * 刷新策略池
     */
    @Scheduled(cron = "${bps.scheduler.strategyPool.refresh.cron:0 0 * * * ?}")
    public void refreshStrategyPool(){
        long begin = System.currentTimeMillis();

        strategyPoolService.reLoadStrategyPool();

        long end = System.currentTimeMillis();

        log.info("StrategyPoolScheduler.refreshStrategyPool  end , 耗时 {} 毫秒 ....",(end-begin));
    }
}
