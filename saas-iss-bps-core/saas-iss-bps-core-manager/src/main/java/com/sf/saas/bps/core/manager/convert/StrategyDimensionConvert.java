package com.sf.saas.bps.core.manager.convert;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sf.saas.bps.core.dao.entity.Dimension;
import com.sf.saas.bps.core.dao.entity.StrategyCondition;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description StrategyDimensionConvert
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
public class StrategyDimensionConvert {

    public static List<StrategyCondition> convertVo2DoList(List<StrategyDimensionVo> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            return Collections.EMPTY_LIST;
        }
        return dimensionList.stream().map(StrategyDimensionConvert::convertVo2Do).collect(Collectors.toList());
    }

    public static StrategyCondition convertVo2Do(StrategyDimensionVo strategyDimensionVo) {
        if (strategyDimensionVo == null) {
            return null;
        }
        StrategyCondition strategyCondition = new StrategyCondition();
        strategyCondition.setId(strategyDimensionVo.getId());
        strategyCondition.setTenantId(strategyDimensionVo.getTenantId());
        strategyCondition.setStrategyId(strategyDimensionVo.getStrategyId());
        strategyCondition.setDimension(strategyDimensionVo.getDimension());
        strategyCondition.setFunction(strategyDimensionVo.getFunction());
        strategyCondition.setParameter(strategyDimensionVo.getParameter());
        strategyCondition.setParameterDisplay(strategyDimensionVo.getParameterDisplay());
        strategyCondition.setCurrency(strategyDimensionVo.getCurrency());
        strategyCondition.setCreator(strategyDimensionVo.getCreator());
        strategyCondition.setCreateTime(strategyDimensionVo.getCreateTime());
        strategyCondition.setUpdater(strategyDimensionVo.getUpdater());
        strategyCondition.setUpdateTime(strategyDimensionVo.getUpdateTime());
        return strategyCondition;
    }

    /**
     * 修改获取 StrategyCondition
     * @param dimensionList
     * @param strategySaveVo
     * @param isUpdate
     * @return
     */
    public static List<StrategyCondition> getStrategyConditionsByDetail(List<StrategyDimensionVo> dimensionList, StrategySaveVo strategySaveVo, boolean isUpdate) {
        long currentTimeMillis = System.currentTimeMillis();

        return dimensionList.stream().map(dmVo->{
            if (dmVo == null) {
                return null;
            }
            StrategyCondition strategyCondition = new StrategyCondition();
            if (isUpdate) {
                strategyCondition.setId(dmVo.getId());
            }
            strategyCondition.setTenantId(dmVo.getTenantId());
            strategyCondition.setStrategyId(strategySaveVo.getId());
            strategyCondition.setDimension(dmVo.getDimension());
            strategyCondition.setFunction(dmVo.getFunction());
            strategyCondition.setParameter(dmVo.getParameter());
            strategyCondition.setParameterDisplay(dmVo.getParameterDisplay());
            strategyCondition.setCurrency(dmVo.getCurrency());
            strategyCondition.setCreateTime(currentTimeMillis);
            strategyCondition.setUpdater(strategySaveVo.getCreator());
            strategyCondition.setUpdateTime(currentTimeMillis);
            strategyCondition.setTenantId(strategySaveVo.getTenantId());
            return strategyCondition;
        }).collect(Collectors.toList());

    }

    /**
     * 新增  获取 StrategyCondition
     * @param dimensionList
     * @param strategyVoSaved
     * @return
     */
    public static List<StrategyCondition> getStrategyConditions(List<Dimension> dimensionList, StrategySaveVo strategyVoSaved) {
        long currentTimeMillis = System.currentTimeMillis();
        return dimensionList.stream().map(dm -> {
            StrategyCondition strategyCondition = new StrategyCondition();
            strategyCondition.setTenantId(strategyVoSaved.getTenantId());
            strategyCondition.setStrategyId(strategyVoSaved.getId());
            strategyCondition.setDimension(dm.getDimensionKey());
            strategyCondition.setCreator(strategyVoSaved.getCreator());
            strategyCondition.setCreateTime(currentTimeMillis);
            strategyCondition.setUpdater(strategyVoSaved.getCreator());
            strategyCondition.setUpdateTime(currentTimeMillis);
            strategyCondition.setTenantId(strategyVoSaved.getTenantId());
            return strategyCondition;
        }).collect(Collectors.toList());
    }
}
