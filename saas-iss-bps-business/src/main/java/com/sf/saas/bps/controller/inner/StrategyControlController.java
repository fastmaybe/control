package com.sf.saas.bps.controller.inner;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.sf.saas.bps.core.api.IStrategyControlBizService;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.constans.ResponseCodeEnum;
import com.sf.saas.bps.core.common.emnus.StrategyDimension;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.AppResponse;
import com.sf.saas.bps.core.dto.res.StrategyBatchMatchDto;
import com.sf.saas.bps.core.dto.res.StrategyMatchDto;
import com.sf.saas.bps.core.service.strategy.StrategyMatchPoolUtil;
import com.sf.saas.bps.core.service.strategy.bo.StrategyMatchPool;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 01407460
 * @date 2022/9/7 14:39
 */
@Slf4j
@RestController
@RequestMapping("/inner/control")
@Api(tags = "【策略控制器】")
public class StrategyControlController {


    @Autowired
    private IStrategyControlBizService strategyControlBizService;

    @Autowired
    private ThreadPoolTaskExecutor matchCheckExecutor;


    @PostMapping("/match")
    @ApiOperation(value = "策略匹配")
    public AppResponse<List<StrategyMatchDto>> strategyMatch(@RequestBody JSONObject jsonObject) {
        String currentLang = CurrentReqInfoUtil.currentLang();
        return strategyControlBizService.strategyMatchDispatch(jsonObject, currentLang);
    }

    @PostMapping("/batch/match")
    @ApiOperation(value = "策略匹配")
    public AppResponse<List<StrategyBatchMatchDto>> strategyBatchMatch(@RequestBody List<JSONObject> param) {
        if (CollectionUtils.isEmpty(param)) {
            return ResponseHelper.buildAppSuccess();
        }
        if (param.size() > BpsConstant.BATCH_LIMIT) {
            return ResponseHelper.buildAppFail(ResponseCodeEnum.STRATEGY_BATCH_OUT_OF_RANGE);
        }
        String currentLang = CurrentReqInfoUtil.currentLang();
        int partitionSize = param.size() > BpsConstant.BATCH_LIMIT / 2 ? 6 : 4;
        List<List<JSONObject>> partition = Lists.partition(param, partitionSize);
        List<CompletableFuture<List<StrategyBatchMatchDto>>> futureList = partition.stream().map(onePartition -> CompletableFuture.supplyAsync(() -> {
            List<StrategyBatchMatchDto> res = new ArrayList<>();
            for (JSONObject jsonObject : onePartition) {
                StrategyBatchMatchDto batchMatchDto = new StrategyBatchMatchDto();
                String waybillNo = jsonObject.getString(StrategyDimension.WAYBILL_NO.getKey());
                batchMatchDto.setWaybillNo(waybillNo);
                try {
                    AppResponse<List<StrategyMatchDto>> response = strategyControlBizService.strategyMatchDispatch(jsonObject, currentLang);
                    batchMatchDto.setMatchStrategy(response.getObj());
                } catch (Exception e) {
                    log.error("onePartition error waybillNo={} ", waybillNo, e);
                }
                res.add(batchMatchDto);
            }
            return res;
        }, matchCheckExecutor)).collect(Collectors.toList());
        List<StrategyBatchMatchDto> res = new ArrayList<>();
        for (CompletableFuture<List<StrategyBatchMatchDto>> future : futureList) {
            try {
                List<StrategyBatchMatchDto> matchDtos = Optional.ofNullable(future.get(45, TimeUnit.SECONDS)).orElseGet(ArrayList::new);
                res.addAll(matchDtos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseHelper.buildAppSuccess(res);
    }


    @GetMapping("/pool/info")
    @ApiOperation(value = "查看信息")
    public AppResponse<Map<String,Object>> strategyMatch(@RequestParam String tenantId) {
        if (StringUtils.isEmpty(tenantId)) {
            return ResponseHelper.buildAppSuccess();
        }
        Map<String, Object> map = new HashMap<>(4);
        StrategyMatchPool strategyPool = StrategyMatchPoolUtil.strategyPool(tenantId);
        map.put("strategyPool",strategyPool);
        map.put("podHostName", NetUtil.getLocalHostName());
        return ResponseHelper.buildAppSuccess(map);
    }

}
