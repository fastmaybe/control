package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 01407460
 * @since 2022-09-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_strategy_match_log")
public class StrategyMatchLog implements Serializable {


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 租户
     */
    private String tenantId;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 链路
     */
    private String traceId;

    /**
     * 请求入参
     */
    private String request;

    /**
     * 匹配策略快照
     */
    private String response;

    /**
     * 类型 备用
     */
    private Integer requestType;

    /**
     * 备注 备用
     */
    private String remark;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;


}
