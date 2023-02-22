package com.sf.saas.bps.core.dto.req.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description CustomerUserVo
 *
 * @author suntao(01408885)
 * @since 2022-07-21
 */
public class CustomerAccountVo {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "租户Id")
    private String tenantId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;


    @ApiModelProperty(value = "月结卡号")
    private String customerCode;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


}
