package com.sf.saas.bps.core.common.constans;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * @author 01407460
 * @date 2022/9/7 10:28
 */
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {


    //通用成功
    SUCCESS("OK", "操作成功"),
    //通用失败
    FAILURE("FAIL", "操作失败"),
    STRATEGY_NAME_CANNOT_NULL("STRATEGY_NAME_CANNOT_NULL", "策略名称不能为空！"),// The strategy name cannot be empty!
    STRATEGY_NAME_LENGTH_ERROR("STRATEGY_NAME_LENGTH_ERROR", "策略名称最大长度为200个字符！"),// The maximum length of the policy name is 200 characters!
    STRATEGY_NAME_SAME_ERROR("STRATEGY_NAME_SAME_ERROR", "名称信息已重复, 请修改！"),// The name information has been repeated, please modify!
    STRATEGY_DIMENSION_LIMIT_ERROR("STRATEGY_DIMENSION_LIMIT_ERROR", "管控维度最少选择1个，最多选择6个！"),// Select a minimum of 1 and a maximum of 6 control dimensions.
    STRATEGY_DIMENSION_INPUT_ERROR("STRATEGY_DIMENSION_INPUT_ERROR", "策略维度选择非法"),// Illegal policy dimension selection
    STRATEGY_DIMENSION_SETTING_ERROR("STRATEGY_DIMENSION_SETTING_ERROR", "策略维度配置错误"),// Policy dimension setting error
    STRATEGY_DIMENSION_FUNCTION_ERROR("STRATEGY_DIMENSION_FUNCTION_ERROR", "维度管控条件:命中条件配置错误"),// Dimension control condition: hit condition configuration error
    STRATEGY_DIMENSION_PARAMETER_ERROR("STRATEGY_DIMENSION_PARAMETER_ERROR", "维度管控条件:命中参数配置错误"),// Dimension control condition: hit parameter configuration error
    STRATEGY_DIMENSION_PARAMETER_NUM_ERROR("STRATEGY_DIMENSION_PARAMETER_NUM_ERROR", "维度命中参数中有数值类型配置错误"),// Wrong value type configuration in dimension hit parameter
    STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR("STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR", "维度管控条件:命中参数区间配置错误"),// Dimension control condition: hit parameter range configuration error
    STRATEGY_DIMENSION_SETTING_CURRENCY_ERROR("STRATEGY_DIMENSION_SETTING_CURRENCY_ERROR", "策略维度币种配置错误"),// Policy dimension Currency setting error
    STRATEGY_TYPE_INPUT_ERROR("STRATEGY_TYPE_INPUT_ERROR", "策略类型非法"),// Type of policy is invalid
    STRATEGY_LANG_INPUT_ERROR("STRATEGY_LANG_INPUT_ERROR", "策略提示语言不能为空"),// Policy prompt language cannot be empty
    STRATEGY_TIP_FLAG_INPUT_ERROR("STRATEGY_TIP_FLAG_INPUT_ERROR", "策略是否需要提示选择有误"),// Whether the policy needs to be prompted for incorrect selection
    STRATEGY_LANG_EN_CN_CANNOT_NULL("STRATEGY_LANG_EN_CN_CANNOT_NULL", "策略提示中英文不能为空"),// Policy prompt: Chinese and English cannot be blank
    STRATEGY_NOT_EXIST("STRATEGY_NOT_EXIST", "策略不存在"),// Policy dose not exist!


    STRATEGY_SAAS_HANDLER_NOT_EXIST("STRATEGY_SAAS_HANDLER_NOT_EXIST", "SAAS 通用策略匹配器缺失 请检测IStrategyMatchHandler"),

    USERNAME_IS_NULL("USERNAME_IS_NULL", "用户名缺失"),
    TENANT_ID_IS_NULL("TENANT_ID_IS_NULL", "租户属性缺失"),
    TENANT_ID_IS_ERROR("TENANT_ID_IS_ERROR", "租户错误"),

    //================= 通用错误码开始



    //================= 某业务域错误码
    STRATEGY_BATCH_OUT_OF_RANGE("STRATEGY_BATCH_OUT_OF_RANGE", "批量数量过多"),

    //================= 某业务域错误码


    ;

    /**
     * code 标识 国际化标识
     */
    private String code;


    /**
     * 描述
     */
    private String msg;

    private static final Map<String,ResponseCodeEnum> resCodeSet = new HashMap<>();
    static {
        ResponseCodeEnum[] enums = ResponseCodeEnum.values();
        for (ResponseCodeEnum anEnum : enums) {
            resCodeSet.put(anEnum.getCode(),anEnum);
        }
    }

    public static ResponseCodeEnum getCodeEnum(String code){
        ResponseCodeEnum codeEnum = resCodeSet.get(code);
        if (Objects.isNull(codeEnum)){
            return ResponseCodeEnum.FAILURE;
        }
        return  codeEnum;
    }

}
