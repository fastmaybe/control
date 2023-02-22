package com.sf.saas.bps.core.dto.req.user;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description CustomerUserVo
 *
 * @author suntao(01408885)
 * @since 2022-07-21
 */
public class CustomerUserVo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户uuid")
    private String userUid;

    @ApiModelProperty(value = "租户Id")
    private String tenantId;

    @ApiModelProperty(value = "用户编码")
    private String userCode;


    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户全称")
    private String nickname;

    @ApiModelProperty(value = "国家地区（国家二字码）")
    private String countryCode;

    @ApiModelProperty(value = "客户编码")
    private String accountCode;

    @ApiModelProperty(value = "是否重置过密码", notes = "0:否；1：是")
    private Integer isDefaultPwd;

    @ApiModelProperty(value = "用户状态", notes = "0:不可用；1：启用；2：注销")
    private Integer status;

    @ApiModelProperty(value = "账号层级", notes = "0:普通账号；1：主账号")
    private Integer userLevel;

    @ApiModelProperty(value = "账号注册系统", notes = "WEBORDER/JVGO/CDM")
    private String regFrom;

    @ApiModelProperty(value = "创建时间13位时间戳")
    private Long createTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getRegFrom() {
        return regFrom;
    }

    public void setRegFrom(String regFrom) {
        this.regFrom = regFrom;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }


    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getIsDefaultPwd() {
        return isDefaultPwd;
    }

    public void setIsDefaultPwd(Integer isDefaultPwd) {
        this.isDefaultPwd = isDefaultPwd;
    }
}
