package com.sf.saas.bps.core.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bps_dictionary")
public class Dictionary implements Serializable {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据字典类型标识
     */
    private String name;

    /**
     * 字典key
     */
    private String dictKey;

    /**
     * 字典value
     */
    private String dictValue;

    /**
     * 显示排序
     */
    private Integer sort;

    /**
     * 语言：中文-zh-CN；英文-en
     */
    private String lang;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效状态：0-无效；1-有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 修改人
     */
    private String updater;


}
