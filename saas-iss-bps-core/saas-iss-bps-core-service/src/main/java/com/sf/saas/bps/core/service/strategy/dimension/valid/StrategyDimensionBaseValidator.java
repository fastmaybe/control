package com.sf.saas.bps.core.service.strategy.dimension.valid;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.emnus.StrategyFunctionEnum;
import com.sf.saas.bps.core.common.utils.ValidCodeMsg;
import lombok.extern.log4j.Log4j2;

/**
 * Description StrategyDimensionStringValidator
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
@Log4j2
public class StrategyDimensionBaseValidator {

    protected String function;

    /**
     * @return
     */
    protected ValidCodeMsg<ResponseCodeEnum> validFunction() {
        if (!StrategyFunctionEnum.contains(function)) {
            log.info("维度function[{}]未在配置列表中", function);
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_FUNCTION_ERROR);
        }
        return StringUtils.isNotBlank(function) ? ValidCodeMsg.success(ResponseCodeEnum.SUCCESS) : ValidCodeMsg.error(ResponseCodeEnum.FAILURE);
    }

}
