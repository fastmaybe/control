package com.sf.saas.bps.core.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description DimensionVo
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
@Data
public class StrategyDimensionVo {

    private static final long serialVersionUID = 4451695769395793129L;
    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Long id;
    /**
     * 自增id
     */
    @ApiModelProperty(value = "策略id")
    private Long strategyId;

    /**
     * 维度名
     */
    @ApiModelProperty(value = "维度字段")
    @NotBlank(message = "STRATEGY_DIMENSION_INPUT_ERROR")
    private String dimension;

    /**
     * 维度计算规则
     */
    @ApiModelProperty(value = "维度计算规则")
    private String function;

    /**
     * 条件对应参数
     */
    @ApiModelProperty(value = "条件对应参数")
    private String parameter;

    /**
     * 条件对应参数
     */
    @ApiModelProperty(value = "条件对应参数 前端展示用")
    private String parameterDisplay;

    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
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

    // ===  以下字段 只查询  不写 前端显示用
    /**
     * 维度值类型 String，BigDecimal
     */
    @ApiModelProperty(value = "维度值类型 String，BigDecimal")
    private String valueType;

    /**
     * 字段取值key
     */
    @ApiModelProperty(value = "字段取值key:payType,city,country")
    private String dictValueKey;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 是否导入
     */
    @ApiModelProperty(value = "是否支持导入 0-不支持 1-支持")
    private Integer importFlag;

    /**
     * 是否要求币种
     */
    @ApiModelProperty(value = "是否要求有币种配置 1-是")
    private Integer currencyFlag;

}
