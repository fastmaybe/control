package com.sf.saas.bps.core.dto.enums;

/**
 * Description StrategyTipFlagEnum
 *
 * @author suntao(01408885)
 * @since 2022-09-26
 */
public enum StrategyTipFlagEnum {

    NO_PROMPT(1, "不提示"),
    TIPS(2, "提示"),
    ;


    private Integer tipFlag;
    private String desc;

    StrategyTipFlagEnum(Integer tipFlag, String desc) {
        this.tipFlag = tipFlag;
        this.desc = desc;
    }

    public int getTipFlag() {
        return tipFlag;
    }

    public static boolean contains(Integer tipFlag) {
        if (TIPS.tipFlag.equals(tipFlag) || NO_PROMPT.tipFlag.equals(tipFlag)) {
            return true;
        }
        return false;
    }

    /**
     * 提示和拒绝必须提示
     * 加价可以选择不提示，提示两种
     * @param strategyTypeEnum
     * @param tipFlag
     * @return
     */
    public static boolean validateTipFlag(StrategyTypeEnum strategyTypeEnum, Integer tipFlag) {
        if (strategyTypeEnum == null) {
            return false;
        }
        switch (strategyTypeEnum) {
            case PROMPT:
            case REJECT:
                return TIPS.tipFlag.equals(tipFlag);
            case CHARGE:
                return contains(tipFlag);
        }
        return false;
    }
}
