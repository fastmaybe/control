package com.sf.saas.bps.core.service.strategy.bo;

import com.sf.saas.bps.core.dto.vo.StrategyRemarkVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author 01407460
 * @date 2022/9/14 16:39
 */
@Data
public class StrategyMatchBo {


    /**
     * 自增id
     */
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
     * 更新时间
     */
    private Long createTime;

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

    private Map<String, StrategyRemarkVo> strategyRemarkMap;

    private List<StrategyConditionMatchBo> conditionBos;

}
