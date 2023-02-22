package com.sf.saas.bps.core.common.emnus;

/**
 * Description StrategyFunctionEnum
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
public enum StrategyFunctionEnum {

    BETWEEN("between", "区间"),
    COMBINATION_INTERVAL("notBetween", "组合区间"),
    GT("gt", "大于"),
    GE("ge", "大于等于"),
    LT("lt", "小于"),
    LE("le", "小于等于"),
    IN("in", "包含"),
    NOT_IN("notIn", "不包含"),


    ;



    private String function;
    private String desc;

    StrategyFunctionEnum(String function, String desc) {
        this.function = function;
        this.desc = desc;
    }

    public String getFunction() {
        return function;
    }

    public static StrategyFunctionEnum getByFunction(String function) {
        for (StrategyFunctionEnum value : StrategyFunctionEnum.values()) {
            if (value.function.equals(function)) {
                return value;
            }
        }
        return null;
    }

    public static boolean contains(String function) {
        for (StrategyFunctionEnum value : StrategyFunctionEnum.values()) {
            if (value.function.equals(function)) {
                return true;
            }
        }
        return false;
    }
}
