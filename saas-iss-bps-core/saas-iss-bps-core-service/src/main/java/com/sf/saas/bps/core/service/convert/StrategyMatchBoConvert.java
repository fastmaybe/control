package com.sf.saas.bps.core.service.convert;

import com.sf.saas.bps.core.dto.enums.StrategyTypeEnum;
import com.sf.saas.bps.core.dto.res.StrategyMatchDto;
import com.sf.saas.bps.core.dto.vo.StrategyRemarkVo;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 01407460
 * @date 2022/9/16 11:43
 */
public class StrategyMatchBoConvert {

    private static String REJECT_DEFAULT= "This order has been restricted and cannot be placed/picked up!";

    private StrategyMatchBoConvert(){}

    public static List<StrategyMatchDto> convertMatchBo2MatchDto(Collection<StrategyMatchBo> strategyMatchBos,String lang) {
        if (CollectionUtils.isEmpty(strategyMatchBos)){
            return new ArrayList<>();
        }
       return strategyMatchBos
                .stream()
                .map(bo->{
                    StrategyMatchDto dto = new StrategyMatchDto();
                    dto.setType(bo.getType());
                    dto.setName(bo.getName());
                    dto.setTipFlag(bo.getTipFlag());
                    Map<String, StrategyRemarkVo> tipMap = bo.getStrategyRemarkMap();

                    StrategyRemarkVo remarkVo= null;
                    if (!CollectionUtils.isEmpty(tipMap)){
                        remarkVo = tipMap.get(lang);
                    }
                    if (Objects.nonNull(remarkVo)){
                        dto.setTipContent(remarkVo.getRemark());
                    }else {
                        if (StrategyTypeEnum.REJECT.getType().equals(bo.getType())){
                            dto.setTipContent(REJECT_DEFAULT);
                        }
                    }
                    return dto;
                }).collect(Collectors.toList());

    }


}
