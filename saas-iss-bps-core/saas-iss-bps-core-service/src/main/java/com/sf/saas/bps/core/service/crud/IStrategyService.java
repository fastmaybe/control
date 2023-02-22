package com.sf.saas.bps.core.service.crud;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sf.saas.bps.core.dao.entity.Strategy;
import com.sf.saas.bps.core.dto.base.Page;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyPageVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;

import java.util.List;

/**
 * <p>
 * 策略表 服务类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface IStrategyService extends IService<Strategy> {

    StrategySaveVo saveStrategy(StrategySaveVo strategy);

    StrategySaveVo updateStrategy(StrategySaveVo strategy);

    StrategyDetailVo getStrategyDetail(long id);

    StrategySaveVo updateStatus(Long id, Integer status);

    Page<StrategyPageVo> queryPage(StrategyVoQueryReq query);

    /**
     * 根据策略名查询
     * @param name
     * @param tenantId
     * @return
     */
    StrategySaveVo selectByStrategyName(String name, String tenantId);

    /**
     * 根绝策略名称查询是否有别的策略和自己同名
     * @param name
     * @param tenantId
     * @param selfStrategyId 自己的主键id
     * @return
     */
    StrategySaveVo selectOtherByStrategyName(String name, Long selfStrategyId, String tenantId);

    /**
     * 根据租户 和 状态 获取策略 VO
     * @param tenantId
     * @param status
     * @return
     */
    List<StrategyDetailVo> loadStrategyByTenantState(String tenantId, Integer status);
}
