package com.sf.saas.bps.core.service.strategy.dimension.valid;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.utils.ValidCodeMsgTuple;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;

/**
 * Description StrategyDimensionGetValidtoerUtil
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
public class StrategyDimensionGetValidatorUtil {

    public static ValidCodeMsgTuple<ResponseCodeEnum, String> getValidatorInstanceType(StrategyDimensionVo strategyDimensionVo) {
        if (strategyDimensionVo == null) {
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_INPUT_ERROR);
        }
        if (StringUtils.isBlank(strategyDimensionVo.getFunction())) {
            return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_FUNCTION_ERROR);
        }
        switch (strategyDimensionVo.getFunction()) {
            case "between" :
            case "notBetween" : return ValidCodeMsgTuple.success("BigDecimal_between");
            case "gt" :
            case "ge" :
            case "lt" :
            case "le" :
                return ValidCodeMsgTuple.success("BigDecimal");
            case "in" :
            case "notIn" :
                return ValidCodeMsgTuple.success("String");
            default: return ValidCodeMsgTuple.error(ResponseCodeEnum.STRATEGY_DIMENSION_FUNCTION_ERROR);
        }
    }
}
