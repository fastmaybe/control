package com.sf.saas.bps.core.service.strategy;

import com.sf.saas.bps.core.dto.enums.StrategyStatusEnum;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.service.crud.IStrategyService;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 01407460
 * @date 2022/9/15 16:55
 */
@Slf4j
@Component
public class StrategyPoolServiceImpl implements IStrategyPoolService{

    @Autowired
    private IStrategyService strategyService;

    /**
     * 项目启动 初始化加载
     */
    @PostConstruct
    public void init(){
        log.info("boot start load StrategyPool  begin....");
        long begin = System.currentTimeMillis();

        reLoadStrategyPool();

        long end = System.currentTimeMillis();

        log.info("boot start load StrategyPool  end ... 耗时 {} 毫秒 ....",(end-begin));
    }



    @Override
    public void reLoadStrategyPool() {
        Integer effectiveStatus = StrategyStatusEnum.EFFECTIVE.getStatus();
        List<StrategyDetailVo> detailVos = strategyService.loadStrategyByTenantState(null, effectiveStatus);
        if (CollectionUtils.isEmpty(detailVos)){
            return;
        }
        Map<String, List<StrategyMatchBo>> collect = detailVos.stream()
                .map(StrategyMatchBoBuilder::builder)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(StrategyMatchBo::getTenantId));

        StrategyMatchPoolUtil.reLoadStrategyPool(collect);
    }

}
