package com.sf.saas.bps.core.service.crud;

import com.sf.saas.bps.core.dao.entity.Dimension;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sf.saas.bps.core.dto.vo.DimensionVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 维度表 服务类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface IDimensionService extends IService<Dimension> {

    List<DimensionVo> listAll();

    List<Dimension> listAllDo();

    /**
     * 根据dimensionKey 查询维度
     * sourceCountry，sourceCityCode
     * @param dimensionList
     * @return
     */
    List<Dimension> selectByDimensionKey(List<String> dimensionList);


    /**
     * mapAll
     * @return
     */
    Map<String, Dimension> mapAll();

    boolean resetDimensionMap();
}
