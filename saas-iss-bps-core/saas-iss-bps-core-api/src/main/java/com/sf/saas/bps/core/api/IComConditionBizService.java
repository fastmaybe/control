package com.sf.saas.bps.core.api;

import com.sf.saas.bps.core.dto.req.user.CustomerAccountVo;
import com.sf.saas.bps.core.dto.req.user.CustomerUserVo;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerAccountReq;
import com.sf.saas.bps.core.dto.req.user.QueryCustomerUserReq;

import java.util.List;

/**
 * Description IComConditionBizService
 *
 * @author suntao(01408885)
 * @since 2022-09-21
 */
public interface IComConditionBizService {
    /**
     * 查询cdm用户
     * @param req
     * @return
     */
    List<CustomerUserVo> queryCdmAllUser(QueryCustomerUserReq req);

    /**
     * 查询cdm月结卡号
     * @param req
     * @return
     */
    List<CustomerAccountVo> queryCdmAllCustomerAccount(QueryCustomerAccountReq req);
}
