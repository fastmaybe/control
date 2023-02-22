package com.sf.saas.bps.core.service.crud.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sf.saas.bps.core.dao.entity.StrategyCondition;
import com.sf.saas.bps.core.dao.mapper.StrategyConditionMapper;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import com.sf.saas.bps.core.manager.convert.StrategyDimensionConvert;
import com.sf.saas.bps.core.service.crud.IStrategyConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 策略条件 服务实现类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Service
public class StrategyConditionServiceImpl extends ServiceImpl<StrategyConditionMapper, StrategyCondition> implements IStrategyConditionService {

    @Override
    public List<Long> selectStrategyIds(String dimension) {
        LambdaQueryWrapper<StrategyCondition> queryWrapper = Wrappers.<StrategyCondition>lambdaQuery()
                .eq(StringUtils.isNotBlank(dimension), StrategyCondition::getDimension, dimension);
        List<StrategyCondition> strategyConditions = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(strategyConditions)) {
            return strategyConditions.stream().map(StrategyCondition::getStrategyId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateStrategyCondition(List<StrategyDimensionVo> dimensionList, StrategySaveVo strategySaveVo) {

        List<StrategyCondition> strategyConditionList = StrategyDimensionConvert.getStrategyConditionsByDetail(dimensionList, strategySaveVo, true);

        // 保存 策略包含维度
        List<StrategyCondition> updateList = strategyConditionList.stream().filter(sc -> sc.getId() != null).collect(Collectors.toList());
        List<StrategyCondition> insertList = strategyConditionList.stream().filter(sc -> sc.getId() == null).collect(Collectors.toList());


        // 删除不要的已存在的维度
        List<Long> updateDimensionIds = updateList.stream().map(StrategyCondition::getId).collect(Collectors.toList());

        LambdaQueryWrapper<StrategyCondition> queryWrapper = Wrappers.<StrategyCondition>lambdaQuery()
                .eq(StrategyCondition::getStrategyId, strategySaveVo.getId());
        List<StrategyCondition> strategyConditions = baseMapper.selectList(queryWrapper);
        List<Long> needDeleteDimensionIds = strategyConditions.stream()
                .filter(item -> !updateDimensionIds.contains(item.getId()))
                .map(StrategyCondition::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(needDeleteDimensionIds)) {
            baseMapper.deleteBatchIds(needDeleteDimensionIds);
        }

        // 保存 策略包含维度
        boolean saveUpdate = updateBatchById(updateList);
        boolean saveInsert = saveBatch(insertList);
        return saveUpdate && saveInsert;
    }


    @Override
    public boolean saveStrategyCondition(List<StrategyDimensionVo> dimensionList, StrategySaveVo strategyVoSaved) {
        List<StrategyCondition> strategyConditionList = StrategyDimensionConvert.getStrategyConditionsByDetail(dimensionList, strategyVoSaved, false);

        // 保存 策略包含维度
        return saveBatch(strategyConditionList);
    }

}
