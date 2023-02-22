package com.sf.saas.bps.core.service.biz;

import com.sf.saas.bps.core.api.IDimensionBizService;
import com.sf.saas.bps.core.dto.vo.DimensionVo;
import com.sf.saas.bps.core.service.crud.IDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description DimensionBizServiceImpl
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
@Service
public class DimensionBizServiceImpl implements IDimensionBizService {

    @Autowired
    private IDimensionService dimensionService;

    @Override
    public List<DimensionVo> listAll() {
        return dimensionService.listAll();
    }

    @Override
    public boolean resetDimensionMap() {
        return dimensionService.resetDimensionMap();
    }
}
