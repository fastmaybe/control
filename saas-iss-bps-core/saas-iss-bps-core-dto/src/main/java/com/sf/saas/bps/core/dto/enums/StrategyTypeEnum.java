package com.sf.saas.bps.core.dto.enums;

import lombok.Getter;


/**
 * description: 策略类型枚举
 *
 * @company 顺丰科技有限公司国际业务科技部
 * @author 01408885
 * @version 1.2
 * @date 2022/3/30
 */
@Getter
public enum StrategyTypeEnum {

    /**
     * 拒收
     */
    REJECT("reject"),

    /**
     * 加价
     */
    CHARGE("charge"),

    /**
     * 提示
     */
    PROMPT("prompt"),


    ;

    private final String type;

    StrategyTypeEnum(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }

    public static StrategyTypeEnum getStrategyTypeEnumByValue(String type) {
        for (StrategyTypeEnum strategyTypeEnum : StrategyTypeEnum.values()) {
            if (strategyTypeEnum.getType().equals(type)) {
                return strategyTypeEnum;
            }
        }
        return null;
    }



    public static boolean contains(String type){
        return REJECT.getType().equals(type) ||
                CHARGE.getType().equals(type) ||
                PROMPT.getType().equals(type);
    }

}
