package com.sf.saas.bps.core.service.crud;

import com.sf.saas.bps.core.dao.entity.StrategyCondition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sf.saas.bps.core.dto.vo.StrategyDimensionVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;

import java.util.List;

/**
 * <p>
 * 策略条件 服务类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface IStrategyConditionService extends IService<StrategyCondition> {

    /**
     * 通过维度查询策略id
     * @param dimension
     * @return
     */
    List<Long> selectStrategyIds(String dimension);

    /**
     * 更新  strategy & dimension 关系
     * @param dimensionList 静态dimension
     * @param strategyDetail 详情 保存function和parameter
     * @return
     */
    boolean updateStrategyCondition(List<StrategyDimensionVo> dimensionList, StrategySaveVo strategyDetail);

    /**
     * 新增保存
     * @param dimensionList
     * @param strategyVoSaved
     * @return
     */
    boolean saveStrategyCondition(List<StrategyDimensionVo> dimensionList, StrategySaveVo strategyVoSaved);

}
