package com.sf.saas.bps.core.dto.enums;

/**
 * Description StrategyPriority
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
@SuppressWarnings("all")
public enum StrategyPriorityEnum {

    PRIORITY_1(10,"拒收"),

    /**
     * 中间备用
     */

    PRIORITY_2(30,"加价 | 提示");


    private Integer priority;
    private String desc;

    StrategyPriorityEnum(Integer priority, String desc) {
        this.priority = priority;
        this.desc = desc;
    }

    public Integer getPriority() {
        return priority;
    }


    public static StrategyPriorityEnum getByStrategyType(StrategyTypeEnum strategyTypeEnum) {
        if (strategyTypeEnum == null) {
            return null;
        }
        switch (strategyTypeEnum) {
            case REJECT: return PRIORITY_1;
            case CHARGE:
            case PROMPT:
                return PRIORITY_2;
            default: return null;
        }
    }
}
