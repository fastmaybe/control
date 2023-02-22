package com.sf.saas.bps.core.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Description StrategyVo
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
public class StrategySaveVo {

    private static final long serialVersionUID = -1787606468847721318L;

    /**
     * 自增id
     */
    @ApiModelProperty(value = "自增id")
    private Long id;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    /**
     * 策略名（唯一）
     */
    @ApiModelProperty(value = "策略名（唯一）", required = true)
    @NotBlank(message = "STRATEGY_NAME_CANNOT_NULL")
    @Length(max = 200, message = "STRATEGY_NAME_LENGTH_ERROR")
    private String name;

    /**
     * 状态 0-待生效 1-生效
     */
    @ApiModelProperty(value = "状态 0-待生效 1-生效")
    @Range(min = 0, max = 1)
    private Integer status;


    /**
     * hash版本
     */
    @ApiModelProperty(value = "hash版本")
    private String version;

    /**
     * 策略开始时间  闭区间
     */
    @ApiModelProperty(value = "策略开始时间  闭区间", required = false)
    private Long beginDate;

    /**
     * 策略结束时间 开区间
     */
    @ApiModelProperty(value = "策略结束时间 开区间")
    private Long endDate;

    /**
     * 策略类型 字典 （拒收，加价，提示）
     */
    @ApiModelProperty(value = "策略类型 字典 （拒收，加价，提示）", required = true)
    @NotBlank(message = "STRATEGY_TYPE_INPUT_ERROR")
    private String type;

    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private Long enableDate;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述 暂时不用")
    private String description;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者")
    private String updater;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
    /**
     * 是否白名单
     */
    @ApiModelProperty(value = "是否白名单")
    private Integer positive;
    /**
     * 优先级
     */
    @ApiModelProperty(value = "优先级")
    private Integer priority;
    /**
     * 提示相关 1不提示 2提示
     */
    @ApiModelProperty(value = "提示相关 1不提示 2提示(提示和拒绝必须提示,加价可以选择不提示或者提示两种)")
    private Integer tipFlag;
    /**
     * 维度
     */
    @ApiModelProperty(value = "维度字段 数组[sourceCountry,sourceCityCode]", required = true)
    @Size(max = 6, message = "STRATEGY_DIMENSION_LIMIT_ERROR")
    @NotEmpty(message = "STRATEGY_DIMENSION_LIMIT_ERROR")
    private List<StrategyDimensionVo> dimensionList;
    /**
     * 提示描述多语言
     */
    @ApiModelProperty(value = "提示描述 多语言", required = true)
    @NotEmpty(message = "STRATEGY_LANG_INPUT_ERROR")
    private List<StrategyRemarkVo> tipContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Long enableDate) {
        this.enableDate = enableDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPositive() {
        return positive;
    }

    public void setPositive(Integer positive) {
        this.positive = positive;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getTipFlag() {
        return tipFlag;
    }

    public void setTipFlag(Integer tipFlag) {
        this.tipFlag = tipFlag;
    }

    public List<StrategyDimensionVo> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(List<StrategyDimensionVo> dimensionList) {
        this.dimensionList = dimensionList;
    }

    public List<StrategyRemarkVo> getTipContent() {
        return tipContent;
    }

    public void setTipContent(List<StrategyRemarkVo> tipContent) {
        this.tipContent = tipContent;
    }
}
