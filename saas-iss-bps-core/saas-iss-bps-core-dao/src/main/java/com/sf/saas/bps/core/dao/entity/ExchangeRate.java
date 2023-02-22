package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 汇率配置
 * </p>
 *
 * @author 01407460
 * @since 2022-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_exchange_rate")
public class ExchangeRate implements Serializable {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 基础货币
     */
    private String sourceCurrencyCode;

    /**
     * 目标货币
     */
    private String destCurrencyCode;

    /**
     * 生效日期
     */
    private Long validTm;

    /**
     * 失效日期
     */
    private Long invalidTm;

    /**
     * 是否停用[0:正常,1:停用]
     */
    private Integer deleteFlg;

    /**
     * 汇率
     */
    private Double rate;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后修改人
     */
    private String updater;

    /**
     * 最后修改时间
     */
    private Long updateTime;


}
