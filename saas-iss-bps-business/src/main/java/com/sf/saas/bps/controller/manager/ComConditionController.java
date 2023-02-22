package com.sf.saas.bps.controller.manager;

import com.sf.saas.bps.core.api.IComConditionBizService;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.req.user.CustomerAccountVo;
import com.sf.saas.bps.core.dto.req.user.CustomerUserVo;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerAccountReq;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerUserReq;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @company: SF-Tech国际业务中心研发部
 * @description: Dimension Controller
 * @author： 01383484
 * @Date： 20220327
 */
@Log4j2
@RestController
@RequestMapping("/common/condition")
@Api(tags = "通用条件查询")
public class ComConditionController {

    @Autowired
    private IComConditionBizService comConditionBizService;


    /**
     * 查询CDM所有用户
     *
     * @return
     */
    @PostMapping("/queryCdmAllUser")
    @ApiOperation(value = "查询CDM所有用户", notes = "查询CDM所有用户 支持模糊查询")
    public Response<List<CustomerUserVo>> queryCdmAllUser(@RequestBody QueryCustomerUserReq req) {
        log.info("cdmAllUser list called");
        req.setTenantId(CurrentReqInfoUtil.currentTenantId());
        List<CustomerUserVo> customerUserVos = comConditionBizService.queryCdmAllUser(req);
        return ResponseHelper.buildSuccess(customerUserVos);
    }

    /**
     * 查询CDM所有月结卡号
     *
     * @return
     */
    @PostMapping("/queryCdmAllCustomerAccount")
    @ApiOperation(value = "查询CDM所有月结卡号", notes = "查询CDM所有月结卡号 支持模糊查询")
    public Response<List<CustomerAccountVo>> queryCdmAllCustomerAccount(@RequestBody QueryCustomerAccountReq req) {
        log.info("cdmAllUser list called");
        req.setTenantId(CurrentReqInfoUtil.currentTenantId());
        List<CustomerAccountVo> customerAccountVos = comConditionBizService.queryCdmAllCustomerAccount(req);
        return ResponseHelper.buildSuccess(customerAccountVos);
    }


}
