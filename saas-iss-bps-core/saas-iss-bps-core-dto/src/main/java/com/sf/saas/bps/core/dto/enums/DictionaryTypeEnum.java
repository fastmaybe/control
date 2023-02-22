package com.sf.saas.bps.core.dto.enums;

/**
 * Description DictionaryType
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
public enum DictionaryTypeEnum {
    STRATEGY_TYPE("strategyType", "策略类型"),
    STRATEGY_STATUS("strategyStatus", "策略状态"),
    DIMENSION_PAY_TYPE("payType", "支付方式"),
    DIMENSION_FUNCTION("function", "维度计算方式");


    private String type;
    private String desc;

    DictionaryTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
