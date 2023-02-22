package com.sf.saas.bps.core.service.strategy.dimension.valid.validator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.ImmutableList;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.common.utils.ValidCodeMsg;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.service.strategy.dimension.valid.IStrategyDimensionValidator;
import com.sf.saas.bps.core.service.strategy.dimension.valid.StrategyDimensionBaseValidator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description StrategyDimensionStringValidator
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
@Log4j2
@Component
public class StrategyDimensionBetweenValidator extends StrategyDimensionBaseValidator implements IStrategyDimensionValidator {

    static final private String TYPE="BigDecimal_between";

    @Override
    public ValidCodeMsg<ResponseCodeEnum> validate(StrategyDimensionVo dimension) {
        super.function = dimension.getFunction();

        String parameter = dimension.getParameter();

        ValidCodeMsg<ResponseCodeEnum> validFunction = validFunction();
        if (!validFunction.isOK()) {
            log.info("[{}]，function为空", TYPE);
            return validFunction;
        }
        if (StringUtils.isBlank(parameter)) {
            log.info("[{}]，parameter为空", TYPE);
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_PARAMETER_ERROR);
        }
        JSONArray objects = JSONObject.parseArray(parameter);
        if (objects.size() != BpsConstant.i_4) {
            log.info("[{}]，parameter配置错误【{}】", TYPE, parameter);
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR);
        }
        String f1 = objects.getString(BpsConstant.i_0);
        String f2 = objects.getString(BpsConstant.i_1);

        // 大于符号
        List<String> geGtFunction = ImmutableList.of(StrategyFunctionEnum.GE.getFunction(), StrategyFunctionEnum.GT.getFunction());
        // 小于符号
        List<String> leLtFunction = ImmutableList.of(StrategyFunctionEnum.LE.getFunction(), StrategyFunctionEnum.LT.getFunction());

        if (StrategyFunctionEnum.BETWEEN.getFunction().equals(dimension.getFunction())) {
            // between 开始符号一定是大于 gt或者ge，第二个符号一定是小于 le或者lt
            // a < x < b   ,  ab是区间两头
            if (!leLtFunction.contains(f1) || !leLtFunction.contains(f2)) {
                log.info("[{}]，parameter配置错误【{}】", TYPE, parameter);
                return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR);
            }
        } else if (StrategyFunctionEnum.COMBINATION_INTERVAL.getFunction().equals(dimension.getFunction())){
            // between 开始符号 一定是小于 le或者lt，第二个符号一定是大于 gt或者ge
            // x < a  ||  x > b  ,  ab是区间的两个数字
            if (!leLtFunction.contains(f1) || !geGtFunction.contains(f2)) {
                log.info("[{}]，parameter配置错误【{}】", TYPE, parameter);
                return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR);
            }
        }

        double d1 = 0;
        double d2 = 0;
        try {
            d1 = objects.getDoubleValue(BpsConstant.i_2);
            d2 = objects.getDoubleValue(BpsConstant.i_3);
        } catch (NumberFormatException e) {
            log.info("数值类型转换错误e={}", ExceptionUtils.getStackTrace(e));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_PARAMETER_NUM_ERROR);
        } catch (Exception e) {
            log.info("数值类型转换错误e={}", ExceptionUtils.getStackTrace(e));
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_PARAMETER_ERROR);
        }

        if (d2 < d1) {
            log.info("[{}]，parameter配置错误,前者大于了后者【{}】", TYPE, parameter);
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_BETWEEN_PARAM_ERROR);
        }
        return ValidCodeMsg.success();
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
