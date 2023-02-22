package com.sf.saas.bps.controller.manager;

import com.alibaba.fastjson.JSON;
import com.sf.saas.bps.core.api.IStrategyBizService;
import com.sf.saas.bps.core.common.anno.RedisLock;
import com.sf.saas.bps.core.common.constans.BpsConstant;
import com.sf.saas.bps.core.common.emnus.LockLevel;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.Page;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyPageVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import com.sf.saas.bps.core.dto.vo.StrategyUpdateStatusVo;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 01407460
 * @since 2022/9/7 9:27
 */
@Log4j2
@RestController
@RequestMapping("/manage/strategy")
@Api(tags = "维度信息API")
public class StrategyController {

    @Autowired
    private IStrategyBizService strategyBizService;

    /**
     * 保存
     *
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "新增保存策略")
    @RedisLock(lockLevel = LockLevel.GLOBAL, preKey = BpsConstant.STRATEGY_SAVE, keys = {"name"})
    public Response<String> save(@RequestBody StrategySaveVo strategyVo) {
        String currentUser = CurrentReqInfoUtil.currentUser();
        String currentTenantId = CurrentReqInfoUtil.currentTenantId();
        String currentLang = CurrentReqInfoUtil.currentLang();
        log.info("strategy save called, user[{}]-{}: strategy[{}]", currentUser, currentTenantId, JSON.toJSONString(strategyVo));

        return strategyBizService.saveStrategy(strategyVo);

    }

    /**
     * 更新
     *
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新保存策略")
    @RedisLock(lockLevel = LockLevel.GLOBAL, preKey = BpsConstant.STRATEGY_UPDATE, keys = {"name"})
    public Response<String> update(@RequestBody StrategySaveVo strategy) {
        log.info("strategy update called, user[{}]-{}: strategy[{}]", CurrentReqInfoUtil.currentUser(), CurrentReqInfoUtil.currentTenantId(), JSON.toJSONString(strategy));
        return strategyBizService.updateStrategy(strategy);
    }

    /**
     * 保存策略配置
     *
     * @return
     */
    @PostMapping("/strategySetting")
    @ApiOperation(value = "保存策略配置")
    @RedisLock(lockLevel = LockLevel.GLOBAL, preKey = BpsConstant.STRATEGY_UPDATE, keys = {"name"})
    public Response<String> strategySetting(@RequestBody StrategyDetailVo strategyDetailVo) {
        log.info("strategySetting update called, user[{}]-{}: strategy[{}]", CurrentReqInfoUtil.currentUser(), CurrentReqInfoUtil.currentTenantId(), JSON.toJSONString(strategyDetailVo));
        return strategyBizService.strategySetting(strategyDetailVo);
    }

    /**
     * 详情
     *
     * @return
     */
    @GetMapping("/get")
    public Response<StrategyDetailVo> get(@RequestParam long id) {
        log.info("strategy get called, user[{}]-{}: id[{}]",  CurrentReqInfoUtil.currentUser(), CurrentReqInfoUtil.currentTenantId(), id);
        return strategyBizService.getStrategyDetail(id);
    }

    /**
     * 更新状态
     * @param strategy
     * @return
     */
    @PostMapping("/updateStatus")
    public Response<String> updateStatus(@RequestBody StrategyUpdateStatusVo strategy){
        log.info("strategy updateStatus called, user[{}]-{}: id[{}] status[{}]", CurrentReqInfoUtil.currentUser(), CurrentReqInfoUtil.currentTenantId(), strategy.getId(), strategy.getStatus());
        return strategyBizService.updateStatus(strategy);
    }

    @PostMapping("/queryPage")
    public Response<Page<StrategyPageVo>> queryPage(@RequestBody StrategyVoQueryReq query) {
        log.info("strategy list called, user[{}]-{}:  req[{}]",
                 CurrentReqInfoUtil.currentUser(), CurrentReqInfoUtil.currentTenantId(), JSON.toJSON(query));
        return ResponseHelper.buildSuccess(strategyBizService.queryPage(query));
    }

}
