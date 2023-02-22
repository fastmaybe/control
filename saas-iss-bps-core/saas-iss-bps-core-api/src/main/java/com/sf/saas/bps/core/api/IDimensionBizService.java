package com.sf.saas.bps.core.api;

import com.sf.saas.bps.core.dto.vo.DimensionVo;

import java.util.List;


/**
 * @author 01407460
 * @date 2022/9/7 11:15
 */
public interface IDimensionBizService {

    List<DimensionVo> listAll();

    boolean resetDimensionMap();
}
