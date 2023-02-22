package com.sf.saas.bps.core.dto.req.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description UserCodeFuzzyQueryCustomerUseReq
 *
 * @author suntao(01408885)
 * @since 2022-07-21
 */
public class QueryCustomerAccountReq {

    private static final long serialVersionUID = 1L;

    /**
     * tenantId
     */
    @ApiModelProperty(value = "tenantId")
    private String tenantId;

    /**
     * 用户编码
     */
    @ApiModelProperty(value = "用户编码/用户名 支持模糊")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
