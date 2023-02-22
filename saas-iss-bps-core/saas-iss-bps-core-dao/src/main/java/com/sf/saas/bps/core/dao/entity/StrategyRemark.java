package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 策略多语言备注说明
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_strategy_remark")
public class StrategyRemark implements Serializable {


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
     * 策略id
     */
    private Long strategyId;

    /**
     * 语种
     */
    private String lang;

    /**
     * 备注说明
     */
    private String remark;

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


}
