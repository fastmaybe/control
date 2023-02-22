package com.sf.saas.bps.core.manager.convert;

import com.sf.saas.bps.core.dao.entity.Dimension;
import com.sf.saas.bps.core.dto.vo.DimensionVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description DimensionConvert
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
public class DimensionConvert {

    public static List<DimensionVo> convertDo2BoList(List<Dimension> dimensionList) {
        if (CollectionUtils.isEmpty(dimensionList)) {
            return new ArrayList<>();
        }
        return dimensionList.stream().map(DimensionConvert::convertDo2Bo).collect(Collectors.toList());
    }

    public static DimensionVo convertDo2Bo(Dimension dimension) {
        if (dimension == null) {
            return null;
        }
        DimensionVo dimensionVo = new DimensionVo();
        dimensionVo.setId(dimension.getId());
        dimensionVo.setName(dimension.getName());
        dimensionVo.setDimensionKey(dimension.getDimensionKey());
        dimensionVo.setType(dimension.getType());
        dimensionVo.setDictionaryKeyValue(dimension.getDictionaryKeyValue());
//        dimensionVo.setGroupFlag(dimension.getGroupFlag());
//        dimensionVo.setSumFlag(dimension.getSumFlag());
        dimensionVo.setImportFlag(dimension.getImportFlag());
//        dimensionVo.setStatisticsConditionFlag(dimension.getStatisticsConditionFlag());
        dimensionVo.setUnit(dimension.getUnit());
        dimensionVo.setSorting(dimension.getSorting());
        dimensionVo.setCreateTime(dimension.getCreateTime());
        dimensionVo.setCreator(dimension.getCreator());
        dimensionVo.setUpdateTime(dimension.getUpdateTime());
        dimensionVo.setUpdater(dimension.getUpdater());
        dimensionVo.setRequireFlag(dimension.getRequireFlag());
        dimensionVo.setCurrencyFlag(dimension.getCurrencyFlag());
        return dimensionVo;
    }
}
