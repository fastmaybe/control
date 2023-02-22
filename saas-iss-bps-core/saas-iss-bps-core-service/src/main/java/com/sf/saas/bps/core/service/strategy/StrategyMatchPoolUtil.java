package com.sf.saas.bps.core.service.strategy;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.sf.saas.bps.core.dto.enums.StrategyStatusEnum;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 01407460
 * @date 2022/9/14 10:42
 */
@Slf4j
public class StrategyMatchPoolUtil {

    private StrategyMatchPoolUtil(){}

    /**
     * 租户 - 策略ID -策略
     */
    private static Map<String,ConcurrentHashMap<Long, StrategyMatchBo>> strategyHashMap = new ConcurrentHashMap<>();

    /**
     * 租户 -  策略池
     */
    private static Map<String, StrategyMatchPool> strategyPool= new ConcurrentHashMap<>();



    private static void put(StrategyMatchBo strategyMatchBo){
        String tenantId = strategyMatchBo.getTenantId();
        if (StringUtils.isBlank(tenantId)){
            log.error("StrategyMatchUtil.put tenantId is null,strategyMacthBo Id is {}",strategyMatchBo.getId());
            return;
        }
        ConcurrentHashMap<Long, StrategyMatchBo> strategyMatchMap = strategyHashMap.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>(32));
        strategyMatchMap.put(strategyMatchBo.getId(),strategyMatchBo);
        refreshPool(tenantId);

    }


    private static void remove(String tenantId,Long strategyId){
        if (StringUtils.isBlank(tenantId) || Objects.isNull(strategyId)){
            log.error("StrategyMatchUtil.remove tenantId{}  isBlank or strategyId{} is null ",tenantId,strategyId);
            return;
        }
        ConcurrentHashMap<Long, StrategyMatchBo> strategyMatchMap = strategyHashMap.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>(32));

        strategyMatchMap.remove(strategyId);
        refreshPool(tenantId);
    }

    public static StrategyMatchPool strategyPool(String tenantId){
        return strategyPool.get(tenantId);
    }


    private static void refreshPool(String tenantId){
        if (StringUtils.isBlank(tenantId)){
            log.error("StrategyMatchUtil.refreshPool tenantId is null");
        }
        ConcurrentHashMap<Long, StrategyMatchBo> concurrentHashMap = strategyHashMap.get(tenantId);
        if (CollectionUtils.isEmpty(concurrentHashMap)){
            StrategyMatchPool emptyMatchPool = new StrategyMatchPool(tenantId, new ArrayList<>());
            strategyPool.put(tenantId,emptyMatchPool);
            return;
        }

        List<StrategyMatchBo> collect = concurrentHashMap.values().stream().sorted((o1, o2) -> {
            // 排序规则
            //1 ，优先级  level 越小优先级 越高
            //  2 ， 优先级相同  按照维度dimensionCount  维度 越多优先级 越高
            //  3 ，12 相同，按照修改时间 时间越大 排序越高
            Integer level1 = o1.getPriority();
            Integer level2 = o2.getPriority();
            if (!Objects.equals(level1, level2)) {
                return level1 - level2;
            }
            int dimensionCount1 = Optional.ofNullable(o1.getConditionBos()).orElseGet(ArrayList::new).size();
            int dimensionCount2 = Optional.ofNullable(o2.getConditionBos()).orElseGet(ArrayList::new).size();
            if (dimensionCount1 != dimensionCount2) {
                return dimensionCount2 - dimensionCount1;
            }
            long createTime1 = o1.getCreateTime();
            long createTime2 = o2.getCreateTime();
            return Long.compare(createTime2, createTime1);
        }).collect(Collectors.toList());
        StrategyMatchPool strategyMatchPool = new StrategyMatchPool(tenantId, collect);
        strategyPool.put(tenantId,strategyMatchPool);
    }



    public static void refreshStrategy(StrategyMatchBo strategyMatchBo) {
        if (Objects.isNull(strategyMatchBo) || StringUtils.isBlank(strategyMatchBo.getTenantId()) || Objects.isNull(strategyMatchBo.getId())){
            log.error("refreshStrategy strategyMatchBo data is error data is {}", JSON.toJSON(strategyMatchBo));
            return;
        }
        boolean effective = StrategyStatusEnum.EFFECTIVE.getStatus().equals(strategyMatchBo.getStatus());
        if (effective){
            log.info("[pod={}]-refreshStrategy-put,strategyMatchBo is {}", NetUtil.getLocalHostName(), JSON.toJSONString(strategyMatchBo));
            StrategyMatchPoolUtil.put(strategyMatchBo);
        }else {
            log.info("[pod={}]-refreshStrategy-remove,strategyMatchBo is {}", NetUtil.getLocalHostName(), JSON.toJSONString(strategyMatchBo));
            StrategyMatchPoolUtil.remove(strategyMatchBo.getTenantId(),strategyMatchBo.getId());
        }
    }



    /**
     * 重新加载 策略池
     * @param collect  此刻生效的策略 由外部保证
     */
    public static void reLoadStrategyPool(Map<String, List<StrategyMatchBo>> collect) {
        Set<String> newTenantPool = new HashSet<>();

        for (Map.Entry<String, List<StrategyMatchBo>> entry : collect.entrySet()) {
            String tenantId = entry.getKey();
            List<StrategyMatchBo> matchBos = entry.getValue();
            if (CollectionUtils.isEmpty(matchBos)|| StringUtils.isBlank(tenantId)){
                // 理论上不可能发生 在外部查询过滤分组
                log.error("reLoadStrategyPool tenantId is {},matchBos is{},",tenantId, JSON.toJSONString(matchBos));
                continue;
            }
            ConcurrentHashMap<Long, StrategyMatchBo> strategyMatchMap = strategyHashMap.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>(32));
            //清除全部
            strategyMatchMap.clear();
            // 加入
            for (StrategyMatchBo matchBo : matchBos) {
                strategyMatchMap.put(matchBo.getId(),matchBo);
            }
            // 刷新 策略池
            refreshPool(tenantId);

            //记录reload 全部策略池 对应租户
            newTenantPool.add(tenantId);
        }

        //step2  处理极限偏差
        // 等待上面处理完  再拿当前 全部大key 即 租户
        Set<String> strategyPoolTenant = strategyHashMap.keySet();
        for (String tenantId : strategyPoolTenant) {
            if (newTenantPool.contains(tenantId)){
                // 属于此次需要更新的数据
                continue;
            }
            ConcurrentHashMap<Long, StrategyMatchBo> needClearPool = strategyHashMap.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>(32));
            //清除一级pool
            needClearPool.clear();
            // 刷新策略池
            refreshPool(tenantId);
        }


    }

}
