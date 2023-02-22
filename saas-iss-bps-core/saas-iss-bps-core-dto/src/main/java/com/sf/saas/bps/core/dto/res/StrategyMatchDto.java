package com.sf.saas.bps.core.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 01407460
 * @date 2022/9/16 11:04
 */
@Data
public class StrategyMatchDto implements Serializable {
    private static final long serialVersionUID = 4600858949030862149L;

    @ApiModelProperty(value = "策略名称")
    private String name;

    @ApiModelProperty(value = " 策略类型字典 reject charge prompt （含义依次为 拒收，加价，提示）")
    private String type;


    @ApiModelProperty(value = "提示相关 1不提示 2提示")
    private Integer tipFlag;

    @ApiModelProperty(value = "提示内容")
    private String tipContent;

}
