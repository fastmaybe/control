package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 对接系统信息
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_access_info")
public class AccessInfo implements Serializable {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 系统编码
     */
    private String systemCode;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 系统名
     */
    private String systemName;

    /**
     * appkey
     */
    private String appKey;

    /**
     * appsecret
     */
    private String appSecret;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private Long updateTime;


}
