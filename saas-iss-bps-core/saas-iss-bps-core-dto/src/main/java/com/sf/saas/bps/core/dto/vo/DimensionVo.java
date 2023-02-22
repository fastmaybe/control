package com.sf.saas.bps.core.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Description DimensionVo
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
@Data
public class DimensionVo {

    private static final long serialVersionUID = 4451695769395793129L;
    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Long id;

    /**
     * 维度名
     */
    @ApiModelProperty(value = "维度名")
    private String name;

    /**
     * 维度名
     */
    @ApiModelProperty(value = "维度名国际化key，从字典里取")
    private String nameI18n;

    /**
     * 维度key
     */
    @ApiModelProperty(value = "维度key")
    private String dimensionKey;

    /**
     * 值类型 String,  BigDecimal
     */
    @ApiModelProperty(value = "值类型 String,  BigDecimal")
    private String type;

    /**
     * 字典key  不为空取值来源字典表
     */
    @ApiModelProperty(value = "字典key  不为空取值来源字典表")
    private String dictionaryKeyValue;

    /**
     * 是否支持分组 0-不支持 1-支持
     */
    @ApiModelProperty(value = "是否支持分组 0-不支持 1-支持")
    private Integer groupFlag;

    /**
     * 支持按值累加  0-不支持 1-支持
     */
    @ApiModelProperty(value = "支持按值累加  0-不支持 1-支持")
    private Integer sumFlag;

    /**
     * 是否支持导入 0-不支持 1-支持
     */
    @ApiModelProperty(value = "是否支持导入 0-不支持 1-支持")
    private Integer importFlag;

    /**
     * 是否支持统计条件(即IOMS订单和运单都有相应字段) 0-不支持 1-支持
     */
    @ApiModelProperty(value = "是否支持统计条件(即IOMS订单和运单都有相应字段) 0-不支持 1-支持")
    private Integer statisticsConditionFlag;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位")
    private String unit;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sorting;

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

    /**
     * 是否要求必填 1-是
     */
    @ApiModelProperty(value = "是否要求必填 1-是")
    private Integer requireFlag;

    /**
     * 是否要求有币种配置 1-是
     */
    @ApiModelProperty(value = "是否要求有币种配置 1-是")
    private Integer currencyFlag;

}
