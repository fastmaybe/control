package com.sf.saas.bps.core.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description StrategyRemarkVo
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
@Data
public class StrategyRemarkVo {


    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long id;

    /**
     * 策略id
     */
    @ApiModelProperty(value = "策略id")
    private Long strategyId;

    /**
     * 语种
     */
    @ApiModelProperty(value = "语种")
    private String lang;

    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;

    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String tenantId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updater;
}
