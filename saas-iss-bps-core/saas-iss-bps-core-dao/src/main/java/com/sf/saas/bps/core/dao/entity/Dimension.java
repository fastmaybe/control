package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 维度表
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_dimension")
public class Dimension implements Serializable {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 维度名
     */
    private String name;

    /**
     * 维度key
     */
    private String dimensionKey;

    /**
     * 值类型 String,  BigDecimal
     */
    private String type;

    /**
     * 字典key  不为空取值来源字典表
     */
    private String dictionaryKeyValue;

    /**
     * 是否支持分组 0-不支持 1-支持
     */
    private Integer groupFlag;

    /**
     * 支持按值累加  0-不支持 1-支持
     */
    private Integer sumFlag;

    /**
     * 是否支持导入 0-不支持 1-支持
     */
    private Integer importFlag;

    /**
     * 是否支持统计条件(即IOMS订单和运单都有相应字段) 0-不支持 1-支持
     */
    private Integer statisticsConditionFlag;

    /**
     * 单位
     */
    private String unit;

    /**
     * 顺序
     */
    private Integer sorting;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 是否要求必填 1-是
     */
    private Integer requireFlag;

    /**
     * 是否要求有币种配置 1-是
     */
    private Integer currencyFlag;




}
