package com.sf.saas.bps.core.service.strategy.dimension.valid.validator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.utils.ValidCodeMsg;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.service.strategy.dimension.valid.IStrategyDimensionValidator;
import com.sf.saas.bps.core.service.strategy.dimension.valid.StrategyDimensionBaseValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Description StrategyDimensionStringValidator
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
@Log4j2
@Component
public class StrategyDimensionStringValidator extends StrategyDimensionBaseValidator implements IStrategyDimensionValidator {

    static final private String TYPE="String";

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
        if (objects.size() == BpsConstant.i_0) {
            log.info("[{}]，parameter配置错误【{}】", TYPE, parameter);
            return ValidCodeMsg.error(ResponseCodeEnum.STRATEGY_DIMENSION_PARAMETER_ERROR);
        }
        return ValidCodeMsg.success();
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
