package com.sf.saas.bps.core.service.crud.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sf.saas.bps.core.dao.entity.StrategyRemark;
import com.sf.saas.bps.core.dao.mapper.StrategyRemarkMapper;
import com.sf.saas.bps.core.dto.vo.StrategyRemarkVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import com.sf.saas.bps.core.service.crud.IStrategyRemarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 策略多语言备注说明 服务实现类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Service
public class StrategyRemarkServiceImpl extends ServiceImpl<StrategyRemarkMapper, StrategyRemark> implements IStrategyRemarkService {

    @Override
    public boolean saveStrategyLang(List<StrategyRemarkVo> tipContent, StrategySaveVo strategyVoSaved) {
        // 保存 策略多语言 （StrategyRemark）
        List<StrategyRemark> strategyRemarkList = getStrategyRemarks(tipContent, strategyVoSaved);
        return saveBatch(strategyRemarkList);
    }

    @Override
    public boolean updateStrategyLang(List<StrategyRemarkVo> tipContent, StrategySaveVo strategyVoSaved) {
        LambdaQueryWrapper<StrategyRemark> wrapper = Wrappers.<StrategyRemark>lambdaQuery()
                .eq(StrategyRemark::getStrategyId, strategyVoSaved.getId());
        remove(wrapper);

        return saveStrategyLang(tipContent, strategyVoSaved);
    }

    private List<StrategyRemark> getStrategyRemarks(List<StrategyRemarkVo> tipContent, StrategySaveVo strategyVoSaved) {
        long currentTimeMillis = System.currentTimeMillis();
        return tipContent.stream().map(tip -> {
            StrategyRemark strategyRemark = new StrategyRemark();
            strategyRemark.setTenantId(strategyVoSaved.getTenantId());
            strategyRemark.setStrategyId(strategyVoSaved.getId());
            strategyRemark.setLang(tip.getLang());
            strategyRemark.setRemark(tip.getRemark());
            strategyRemark.setCreator(strategyVoSaved.getCreator());
            strategyRemark.setCreateTime(currentTimeMillis);
            strategyRemark.setUpdater(strategyVoSaved.getCreator());
            strategyRemark.setUpdateTime(currentTimeMillis);
            strategyRemark.setTenantId(strategyVoSaved.getTenantId());
            return strategyRemark;
        }).collect(Collectors.toList());
    }
}
