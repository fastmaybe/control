package com.sf.saas.bps.core.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Description StrategyVo
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
public class StrategyUpdateStatusVo {

    private static final long serialVersionUID = -1787606468847721318L;

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 状态 0-待生效 1-生效
     */
    @ApiModelProperty(value = "状态 0-待生效 1-生效")
    @Range(min = 0, max = 1)
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
