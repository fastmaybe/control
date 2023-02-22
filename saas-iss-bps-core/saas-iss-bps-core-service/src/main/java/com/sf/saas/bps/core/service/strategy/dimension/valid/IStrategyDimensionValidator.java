package com.sf.saas.bps.core.service.strategy.dimension.valid;

import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.utils.ValidCodeMsg;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;

/**
 * Description IStrategyDimensionValidator
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
public interface IStrategyDimensionValidator {


    ValidCodeMsg<ResponseCodeEnum> validate(StrategyDimensionVo dimension);

    String getType();
}
