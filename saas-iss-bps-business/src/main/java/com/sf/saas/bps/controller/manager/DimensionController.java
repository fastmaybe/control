package com.sf.saas.bps.controller.manager;

import com.sf.saas.bps.core.api.IDimensionBizService;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.vo.DimensionVo;
import com.sf.saas.bps.core.dto.vo.base.ResultVo;
import com.sf.saas.bps.core.service.crud.IDimensionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @company: SF-Tech国际业务中心研发部
 * @description: Dimension Controller
 * @author： 01383484
 * @Date： 20220327
 */
@Log4j2
@RestController
@RequestMapping("/manage/dimension")
@Api(tags = "维度信息API")
public class DimensionController {

    @Autowired
    private IDimensionBizService dimensionBizService;
    /**
     * 查询所有维度
     * @return
     */
    @Deprecated
    @GetMapping("/list")
    @ApiOperation(value = "查询所有维度" ,  notes="查询所有维度")
    public Response<List<DimensionVo>> list(){
        log.info("dimension list called");
        List<DimensionVo> dimensionVos = dimensionBizService.listAll();
        return ResponseHelper.buildSuccess(dimensionVos);
    }


    /**
     * 重置维度查询缓存
     * @return
     */
    @GetMapping("/resetDimensionMap")
    @ApiOperation(value = "重置维度查询缓存" ,  notes="重置维度查询缓存")
    public Response<Boolean> resetDimensionMap(){
        log.info("resetDimensionMap list called");
        boolean success = dimensionBizService.resetDimensionMap();
        return ResponseHelper.buildSuccess(success);
    }


}
