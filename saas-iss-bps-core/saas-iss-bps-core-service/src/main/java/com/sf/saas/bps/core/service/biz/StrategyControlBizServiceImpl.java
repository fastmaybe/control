package com.sf.saas.bps.core.service.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sf.saas.bps.core.api.IStrategyControlBizService;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.AppResponse;
import com.sf.saas.bps.core.dto.res.StrategyMatchDto;
import com.sf.saas.bps.core.service.aspect.matchlog.MatchLog;
import com.sf.saas.bps.core.service.convert.StrategyMatchBoConvert;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchBo;
import com.sf.saas.bps.core.service.strategy.match.IStrategyMatchHandler;
import com.sf.saas.bps.core.service.strategy.match.StrategyMatchHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author 01407460
 * @date 2022/9/16 11:00
 */
@Slf4j
@Service
public class StrategyControlBizServiceImpl implements IStrategyControlBizService {

    @Autowired
    private StrategyMatchHandlerFactory handlerFactory;

    @Override
    @MatchLog
    public AppResponse<List<StrategyMatchDto>> strategyMatchDispatch(JSONObject jsonObject, String lang) {
        String tenantId = jsonObject.getString(BpsConstant.HEADER_TENANT_ID);
        if (StringUtils.isBlank(tenantId)) {
            log.error("strategyMatchDispatch tenantId is isBlank, Ignore directly .");
            return ResponseHelper.buildAppSuccess(Collections.emptyList());
        }

        IStrategyMatchHandler matchHandler = handlerFactory.attachHandler(tenantId);

        Collection<StrategyMatchBo> strategyMatchBos = matchHandler.controlCheck(jsonObject);

        log.info("strategyMatchDispatch StrategyMatchBo json is :{}", JSON.toJSONString(strategyMatchBos));


        List<StrategyMatchDto> matchDtos = StrategyMatchBoConvert.convertMatchBo2MatchDto(strategyMatchBos, lang);

        return ResponseHelper.buildAppSuccess(matchDtos);

    }
}
