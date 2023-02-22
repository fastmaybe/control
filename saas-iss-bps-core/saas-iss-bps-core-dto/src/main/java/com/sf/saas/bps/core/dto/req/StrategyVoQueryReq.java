package com.sf.saas.bps.core.dto.req;

import com.sf.saas.bps.core.dto.base.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Description StrategyVoQueryReq
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
@Data
public class StrategyVoQueryReq extends PageParam {

    /**
     * 策略名
     */
    @ApiModelProperty(value = "策略名")
    private String name;

    /**
     * 租户
     */
    @ApiModelProperty(value = "租户")
    private String tenantId;

    /**
     * 策略类型 reject  charge  prompt
     */
    @ApiModelProperty(value = "策略类型 reject  charge  prompt")
    private String type;

    /**
     * 管控时段
     */
    @ApiModelProperty(value = "管控时段")
    private Long validDate;


    /**
     * 条件维度
     */
    @ApiModelProperty(value = "条件维度")
    private String dimension;

    /**
     * 策略状态
     */
    @ApiModelProperty(value = "策略状态")
    private Integer status;

    /**
     * 维度关联的策略id
     */
    private List<Long> strategyIds;
    /**
     * 语言
     */
    private String lang;
}
