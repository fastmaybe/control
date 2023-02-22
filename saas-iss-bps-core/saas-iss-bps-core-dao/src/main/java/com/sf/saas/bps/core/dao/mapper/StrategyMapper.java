package com.sf.saas.bps.core.dao.mapper;

import com.sf.saas.bps.core.dao.entity.Strategy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sf.saas.bps.core.dto.bo.StrategyBo;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 策略表 Mapper 接口
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface StrategyMapper extends BaseMapper<Strategy> {


    List<StrategyBo> queryByParam(StrategyVoQueryReq req);


    StrategyBo queryRelationById(@Param("id") long id);


    List<StrategyBo> loadStrategyByTenantState(@Param("tenantId")String tenantId, @Param("status")Integer status);
}
