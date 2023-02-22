package com.sf.saas.bps.core.service.crud.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sf.saas.bps.core.manager.convert.DimensionConvert;
import com.sf.saas.bps.core.dao.entity.Dimension;
import com.sf.saas.bps.core.dao.mapper.DimensionMapper;
import com.sf.saas.bps.core.dto.vo.DimensionVo;
import com.sf.saas.bps.core.service.crud.IDimensionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>
 * 维度表 服务实现类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Log4j2
@Service
public class DimensionServiceImpl extends ServiceImpl<DimensionMapper, Dimension> implements IDimensionService {

    private static Map<String, Dimension> dimensionMap = new ConcurrentHashMap<>(256);

    @Override
    public List<DimensionVo> listAll() {
        LambdaQueryWrapper<Dimension> queryWrapper = Wrappers.<Dimension>lambdaQuery()
                .orderByAsc(Dimension::getSorting);
        List<Dimension> dimensions = baseMapper.selectList(queryWrapper);
        return DimensionConvert.convertDo2BoList(dimensions);
    }

    /**
     * 数据库对象 查询所有
     * @return
     */
    @Override
    public List<Dimension> listAllDo() {
        log.info("查询数据库所有维度");
        LambdaQueryWrapper<Dimension> queryWrapper = Wrappers.<Dimension>lambdaQuery()
                .orderByAsc(Dimension::getSorting);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Dimension> selectByDimensionKey(List<String> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<Dimension> queryWrapper = Wrappers.<Dimension>lambdaQuery()
                .in(Dimension::getDimensionKey, dimensionList);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Dimension> mapAll() {
        if (CollectionUtils.isEmpty(dimensionMap)) {
            dimensionMap = listAllDo().stream().collect(Collectors.toMap(Dimension::getDimensionKey, a -> a, (key1, key2) -> key2));
        }
        return dimensionMap;
    }

    @Override
    public boolean resetDimensionMap() {
        dimensionMap.clear();
        return true;
    }
}
