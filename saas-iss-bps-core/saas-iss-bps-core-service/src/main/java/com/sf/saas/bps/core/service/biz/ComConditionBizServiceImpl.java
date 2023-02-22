package com.sf.saas.bps.core.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.sf.saas.bps.core.api.IComConditionBizService;
import com.sf.saas.bps.core.dto.req.user.CustomerAccountVo;
import com.sf.saas.bps.core.dto.req.user.CustomerUserVo;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerAccountReq;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerUserReq;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import com.sf.saas.cdm.api.ICdmCustomerRestApi;
import com.sf.saas.cdm.api.ICdmCustomerUserRestApi;
import com.sf.saas.cdm.api.dto.CustomerAccountResp;
import com.sf.saas.cdm.api.dto.CustomerAccountSearchCdmReq;
import com.sf.saas.cdm.api.dto.CustomerUserResp;
import com.sf.saas.cdm.api.dto.UserQueryCustomerUseCdmReq;
import com.sf.saas.cdm.resp.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description ComConditionBizServiceImpl
 *
 * @author suntao(01408885)
 * @since 2022-09-21
 */
@Service
public class ComConditionBizServiceImpl implements IComConditionBizService {

    @Autowired
    private ICdmCustomerUserRestApi cdmCustomerUserRestApi;
    @Autowired
    private ICdmCustomerRestApi cdmCustomerRestApi;

    @Override
    public List<CustomerUserVo> queryCdmAllUser(QueryCustomerUserReq req) {

        UserQueryCustomerUseCdmReq cdmReq = new UserQueryCustomerUseCdmReq();
        BeanUtils.copyProperties(req, cdmReq);

        Result<List<CustomerUserResp>> listResult = cdmCustomerUserRestApi.allCustomerUser(cdmReq);
        List<CustomerUserResp> obj = listResult.getObj();
        if (listResult.isSuccess() && CollectionUtils.isNotEmpty(obj)) {
            return obj.stream().map(cu -> {
                CustomerUserVo vo = new CustomerUserVo();
                BeanUtils.copyProperties(cu, vo);
                return vo;
            }).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public List<CustomerAccountVo> queryCdmAllCustomerAccount(QueryCustomerAccountReq req) {
        CustomerAccountSearchCdmReq cdmReq = new CustomerAccountSearchCdmReq();
        BeanUtils.copyProperties(req, cdmReq);
        Result<List<CustomerAccountResp>> listResult = cdmCustomerRestApi.queryAllCustomerAccount(cdmReq);
        List<CustomerAccountResp> resultObj = listResult.getObj();
        if (listResult.isSuccess() && CollectionUtils.isNotEmpty(resultObj)) {
            return resultObj.stream().map(ca->{
                CustomerAccountVo customerAccountVo = new CustomerAccountVo();
                BeanUtils.copyProperties(ca, customerAccountVo);
                return customerAccountVo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
