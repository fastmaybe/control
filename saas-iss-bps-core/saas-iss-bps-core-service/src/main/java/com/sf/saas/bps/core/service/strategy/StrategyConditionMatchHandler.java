package com.sf.saas.bps.core.service.strategy;

import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.service.strategy.bo.StrategyConditionMatchBo;
import com.sf.saas.bps.core.service.strategy.predicatepool.CondBiPredicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 01407460
 * @date 2022/9/14 20:12
 */
@Slf4j
@Service
public class StrategyConditionMatchHandler implements InitializingBean {

    @Autowired
    private List<CondBiPredicate> condBiPredicateList;

    private Map<String,CondBiPredicate> condBiPredicateMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        for (CondBiPredicate condBiPredicate : condBiPredicateList) {
            condBiPredicateMap.put(condBiPredicate.getType(),condBiPredicate);
        }
    }

    public boolean doConditionMatch(JSONObject paramsObject, List<StrategyConditionMatchBo> conditionMatchBos){
        for (StrategyConditionMatchBo conditionMatchBo : conditionMatchBos) {
            CondBiPredicate predicate = condBiPredicateMap.get(conditionMatchBo.getFunction());
            if (Objects.isNull(predicate)){
                log.error("doConditionMatch function has no predicate,function is {} . ",conditionMatchBo.getFunction());
                return false;
            }
            String predicateParams = paramsObject.getString(conditionMatchBo.getDimension());
            if (StringUtils.isBlank(predicateParams)){
                log.error("Dimension[{}] Params isBlank. so return . ",conditionMatchBo.getDimension());
                return false;
            }

            boolean predicateFlag = predicate.test(predicateParams, conditionMatchBo.getConditionParams());
            if (!predicateFlag){
                return false;
            }
        }
        return true;
    }
}
