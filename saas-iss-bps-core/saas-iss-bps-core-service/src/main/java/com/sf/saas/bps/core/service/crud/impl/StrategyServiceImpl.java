package com.sf.saas.bps.core.service.crud.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sf.saas.bps.core.dao.entity.Dimension;
import com.sf.saas.bps.core.dao.entity.Strategy;
import com.sf.saas.bps.core.dao.mapper.StrategyMapper;
import com.sf.saas.bps.core.dto.base.Page;
import com.sf.saas.bps.core.dto.bo.StrategyBo;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategyPageVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import com.sf.saas.bps.core.manager.convert.StrategyConvert;
import com.sf.saas.bps.core.service.crud.IDimensionService;
import com.sf.saas.bps.core.service.crud.IStrategyConditionService;
import com.sf.saas.bps.core.service.crud.IStrategyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 策略表 服务实现类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Log4j2
@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements IStrategyService {

    @Autowired
    private StrategyMapper strategyMapper;
    @Autowired
    private IStrategyConditionService strategyConditionService;
    @Autowired
    private IDimensionService dimensionService;

    @Override
    public StrategySaveVo saveStrategy(StrategySaveVo strategyVo) {

        Strategy strategy = StrategyConvert.convertVo2Do(strategyVo);
        int insert = baseMapper.insert(strategy);
        if (insert > 0) {
            log.info("saveStrategy success, strategy={}", JSON.toJSONString(strategyVo));
            strategyVo.setId(strategy.getId());
            return strategyVo;
        } else {
            log.info("saveStrategy failed, strategy={}", JSON.toJSONString(strategyVo));
            return null;
        }
    }

    @Override
    public StrategySaveVo updateStrategy(StrategySaveVo strategy) {
        Strategy reqStrategy = StrategyConvert.convertVo2Do(strategy);
        int sucCount = baseMapper.updateById(reqStrategy);
        if (sucCount > 0) {
            log.info("updateStrategy success, strategy={}", JSON.toJSONString(strategy));
            strategy.setId(reqStrategy.getId());
            return strategy;
        } else {
            log.info("updateStrategy failed, strategy={}", JSON.toJSONString(strategy));
            return null;
        }
    }

    @Override
    public StrategyDetailVo getStrategyDetail(long id) {
        StrategyBo one = strategyMapper.queryRelationById(id);
        StrategyDetailVo strategyDetail = StrategyConvert.convertBo2DetailVo(one);

        populateDimensionBaseInfo(strategyDetail);

        return strategyDetail;
    }

    @Override
    public StrategySaveVo updateStatus(Long id, Integer status) {
        if (status == 1 || status == 0) {
            Strategy strategy = baseMapper.selectById(id);
            strategy.setStatus(status);
            baseMapper.updateById(strategy);
            return StrategyConvert.convertDo2Vo(strategy);
        }
        log.info("updateStatus status is invalid");
        return null;
    }

    @Override
    public Page<StrategyPageVo> queryPage(StrategyVoQueryReq req) {
        List<Long> strategyIds = new ArrayList<>();
        if (StringUtils.isNotBlank(req.getDimension())) {
            strategyIds = strategyConditionService.selectStrategyIds(req.getDimension());
            if (CollectionUtils.isEmpty(strategyIds)) {
                log.info("条件dimension={}查询结果为空，没有策略对应", req.getDimension());
                return new Page(req.getPageNum(), req.getPageSize(), 0L, null);
            }
        }

        LambdaQueryWrapper<Strategy> wrapper = Wrappers.<Strategy>lambdaQuery()
                .in(StringUtils.isNotBlank(req.getDimension()), Strategy::getId, CollectionUtils.isEmpty(strategyIds) ? Collections.singletonList(-1L) : strategyIds)
                .like(StringUtils.isNotBlank(req.getName()), Strategy::getName, req.getName())
                .eq(StringUtils.isNotBlank(req.getType()), Strategy::getType, req.getType())
                .eq(req.getStatus() != null, Strategy::getStatus, req.getStatus())
                .eq(Strategy::getTenantId, req.getTenantId());
        if (req.getValidDate() != null) {
            wrapper = wrapper.and(i -> i.isNull(Strategy::getBeginDate).or().le(Strategy::getBeginDate, req.getValidDate()));
            wrapper = wrapper.and(i -> i.isNull(Strategy::getEndDate).or().gt(Strategy::getEndDate, req.getValidDate()));
        }
        Integer count = baseMapper.selectCount(wrapper);

        req.setStrategyIds(strategyIds);
        List<StrategyBo> result = strategyMapper.queryByParam(req);
        List<StrategyPageVo> strategyPageVos = StrategyConvert.convertBo2PageVoList(result);

        return new Page(req.getPageNum(), req.getPageSize(), Long.valueOf(count), strategyPageVos);
    }

    @Override
    public StrategySaveVo selectByStrategyName(String name, String tenantId) {
        Strategy strategy = lambdaQuery()
                .eq(Strategy::getName, name)
                .eq(Strategy::getTenantId, tenantId)
                .last(" limit 1").one();
        return StrategyConvert.convertDo2Vo(strategy);
    }

    @Override
    public StrategySaveVo selectOtherByStrategyName(String name, Long selfStrategyId, String tenantId) {
        Strategy strategy = lambdaQuery()
                .eq(Strategy::getName, name)
                .eq(Strategy::getTenantId, tenantId)
                .ne(Strategy::getId, selfStrategyId)
                .last(" limit 1").one();
        return StrategyConvert.convertDo2Vo(strategy);
    }

    @Override
    public List<StrategyDetailVo> loadStrategyByTenantState(String tenantId, Integer status) {

        List<StrategyBo> strategyBos = strategyMapper.loadStrategyByTenantState(tenantId, status);
        List<StrategyDetailVo> voList = StrategyConvert.convertBo2DetailVoList(strategyBos);

        for (StrategyDetailVo strategyDetailVo : voList) {
            populateDimensionBaseInfo(strategyDetailVo);
        }
        return voList;
    }

    /**
     * 封装维度 基本信息
     * @param strategyDetailVo
     */
    private void populateDimensionBaseInfo(StrategyDetailVo strategyDetailVo) {
        if (null == strategyDetailVo || CollectionUtils.isEmpty(strategyDetailVo.getDimensionList())){
            return;
        }
        Map<String, Dimension> stringDimensionMap = dimensionService.mapAll();
        for (StrategyDimensionVo dimensionVo : strategyDetailVo.getDimensionList()) {
            Dimension dimension = stringDimensionMap.get(dimensionVo.getDimension());
            if (dimension != null) {
                dimensionVo.setValueType(dimension.getType());
                dimensionVo.setDictValueKey(dimension.getDictionaryKeyValue());
                dimensionVo.setCurrencyFlag(dimension.getCurrencyFlag());
                dimensionVo.setImportFlag(dimension.getImportFlag());
                dimensionVo.setUnit(dimension.getUnit());
            }
        }

    }
}
