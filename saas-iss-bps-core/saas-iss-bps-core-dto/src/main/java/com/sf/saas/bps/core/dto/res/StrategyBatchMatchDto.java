package com.sf.saas.bps.core.dto.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 01407460
 * @date 2022/10/11 11:21
 */
@Data
public class StrategyBatchMatchDto implements Serializable {
    private static final long serialVersionUID = 4600858949030862149L;

    @ApiModelProperty(value = "策略")
    private List<StrategyMatchDto> matchStrategy;

    @ApiModelProperty(value = "运单")
    private String waybillNo;

}
