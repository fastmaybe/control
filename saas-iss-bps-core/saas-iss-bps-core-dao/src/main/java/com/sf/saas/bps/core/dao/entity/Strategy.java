package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 策略表
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_strategy")
public class Strategy implements Serializable {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 策略名（唯一）
     */
    private String name;

    /**
     * 状态 0-待生效 1-生效
     */
    private Integer status;

    /**
     * hash版本
     */
    private String version;

    /**
     * 策略开始时间  闭区间
     */
    private Long beginDate;

    /**
     * 策略结束时间 开区间
     */
    private Long endDate;

    /**
     * 策略类型 字典 （拒收，加价，提示）
     */
    private String type;

    /**
     * 生效时间
     */
    private Long enableDate;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 是否白名单
     */
    private Integer positive;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 提示相关 1不提示 2提示
     */
    private Integer tipFlag;

    @TableField(exist = false)
    private List<StrategyRemark> strategyRemarks;

    @TableField(exist = false)
    private List<StrategyCondition> strategyConditions;
}
