package com.sf.saas.bps.core.manager.convert;

import com.sf.saas.bps.core.dao.entity.Strategy;
import com.sf.saas.bps.core.dto.bo.StrategyBo;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategyPageVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description StrategyConvert
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
public class StrategyConvert {

    public static Strategy convertVo2Do(StrategySaveVo strategyVo) {
        if (strategyVo == null) {
            return null;
        }
        Strategy strategy = new Strategy();
        strategy.setId(strategyVo.getId());
        strategy.setTenantId(strategyVo.getTenantId());
        strategy.setName(strategyVo.getName());
        strategy.setStatus(strategyVo.getStatus());
        strategy.setVersion(strategyVo.getVersion());
        strategy.setBeginDate(strategyVo.getBeginDate());
        strategy.setEndDate(strategyVo.getEndDate());
        strategy.setType(strategyVo.getType());
        strategy.setEnableDate(strategyVo.getEnableDate());
        strategy.setDescription(strategyVo.getDescription());
        strategy.setCreator(strategyVo.getCreator());
        strategy.setCreateTime(strategyVo.getCreateTime());
        strategy.setUpdater(strategyVo.getUpdater());
        strategy.setUpdateTime(strategyVo.getUpdateTime());
        strategy.setPositive(strategyVo.getPositive());
        strategy.setPriority(strategyVo.getPriority());
        strategy.setTipFlag(strategyVo.getTipFlag());
        return strategy;
    }

    public static StrategySaveVo convertDo2Vo(Strategy strategy) {
        if (strategy == null) {
            return null;
        }
        StrategySaveVo strategyVo = new StrategySaveVo();
        strategyVo.setId(strategy.getId());
        strategyVo.setTenantId(strategy.getTenantId());
        strategyVo.setName(strategy.getName());
        strategyVo.setStatus(strategy.getStatus());
        strategyVo.setVersion(strategy.getVersion());
        strategyVo.setBeginDate(strategy.getBeginDate());
        strategyVo.setEndDate(strategy.getEndDate());
        strategyVo.setType(strategy.getType());
        strategyVo.setEnableDate(strategy.getEnableDate());
        strategyVo.setDescription(strategy.getDescription());
        strategyVo.setCreator(strategy.getCreator());
        strategyVo.setCreateTime(strategy.getCreateTime());
        strategyVo.setUpdater(strategy.getUpdater());
        strategyVo.setUpdateTime(strategy.getUpdateTime());
        strategyVo.setPositive(strategy.getPositive());
        strategyVo.setPriority(strategy.getPriority());
        strategyVo.setTipFlag(strategy.getTipFlag());
        return strategyVo;
    }

    public static StrategyPageVo convertBo2PageVo(StrategyBo strategyBo) {
        if (strategyBo == null) {
            return null;
        }
        StrategyPageVo strategyVo = new StrategyPageVo();
        strategyVo.setId(strategyBo.getId());
        strategyVo.setTenantId(strategyBo.getTenantId());
        strategyVo.setName(strategyBo.getName());
        strategyVo.setStatus(strategyBo.getStatus());
        strategyVo.setVersion(strategyBo.getVersion());
        strategyVo.setBeginDate(strategyBo.getBeginDate());
        strategyVo.setEndDate(strategyBo.getEndDate());
        strategyVo.setType(strategyBo.getType());
        strategyVo.setEnableDate(strategyBo.getEnableDate());
        strategyVo.setDescription(strategyBo.getDescription());
        strategyVo.setCreator(strategyBo.getCreator());
        strategyVo.setCreateTime(strategyBo.getCreateTime());
        strategyVo.setUpdater(strategyBo.getUpdater());
        strategyVo.setUpdateTime(strategyBo.getUpdateTime());
        strategyVo.setPositive(strategyBo.getPositive());
        strategyVo.setPriority(strategyBo.getPriority());
        strategyVo.setTipFlag(strategyBo.getTipFlag());
        strategyVo.setDimensionList(strategyBo.getDimensionList());
        strategyVo.setTipContent(strategyBo.getTipContent());
        return strategyVo;
    }
    public static List<StrategySaveVo> convertDo2VoList(List<Strategy> strategys) {
        if (CollectionUtils.isEmpty(strategys)) {
            return Collections.emptyList();
        }
        return strategys.stream().map(StrategyConvert::convertDo2Vo).collect(Collectors.toList());
    }
    public static List<StrategyPageVo> convertBo2PageVoList(List<StrategyBo> strategys) {
        if (CollectionUtils.isEmpty(strategys)) {
            return new ArrayList<>();
        }
        return strategys.stream().map(StrategyConvert::convertBo2PageVo).collect(Collectors.toList());
    }
    public static List<StrategyDetailVo> convertBo2DetailVoList(List<StrategyBo> strategys) {
        if (CollectionUtils.isEmpty(strategys)) {
            return new ArrayList<>();
        }
        return strategys.stream().map(StrategyConvert::convertBo2DetailVo).collect(Collectors.toList());
    }

    public static StrategyDetailVo convertBo2DetailVo(StrategyBo one) {
        if (one == null) {
            return null;
        }
        StrategyDetailVo vo = new StrategyDetailVo();
        vo.setId(one.getId());
        vo.setTenantId(one.getTenantId());
        vo.setName(one.getName());
        vo.setStatus(one.getStatus());
        vo.setVersion(one.getVersion());
        vo.setBeginDate(one.getBeginDate());
        vo.setEndDate(one.getEndDate());
        vo.setType(one.getType());
        vo.setEnableDate(one.getEnableDate());
        vo.setDescription(one.getDescription());
        vo.setCreator(one.getCreator());
        vo.setCreateTime(one.getCreateTime());
        vo.setUpdater(one.getUpdater());
        vo.setUpdateTime(one.getUpdateTime());
        vo.setPositive(one.getPositive());
        vo.setPriority(one.getPriority());
        vo.setTipFlag(one.getTipFlag());
        vo.setDimensionList(one.getDimensionList());
        vo.setTipContent(one.getTipContent());
        return vo;
    }
}
