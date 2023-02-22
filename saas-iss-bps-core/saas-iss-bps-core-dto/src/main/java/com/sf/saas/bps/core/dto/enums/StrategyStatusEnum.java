package com.sf.saas.bps.core.dto.enums;

/**
 * Description CommonStatus
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
public enum StrategyStatusEnum {

    INEFFECTIVE(0,"未生效"),
    EFFECTIVE(1,"有效"),
    INVALID(2,"失效的"),
    ;


    private Integer status;
    private String desc;

    StrategyStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }
}
