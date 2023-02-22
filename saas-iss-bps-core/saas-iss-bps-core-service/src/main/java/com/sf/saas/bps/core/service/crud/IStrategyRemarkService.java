package com.sf.saas.bps.core.service.crud;

import com.sf.saas.bps.core.dao.entity.StrategyRemark;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sf.saas.bps.core.dto.vo.StrategyRemarkVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;

import java.util.List;

/**
 * <p>
 * 策略多语言备注说明 服务类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface IStrategyRemarkService extends IService<StrategyRemark> {

    /**
     * 新增保存 策略 语言
     * @param tipContent
     * @param strategyVoSaved
     * @return
     */
    boolean saveStrategyLang(List<StrategyRemarkVo> tipContent, StrategySaveVo strategyVoSaved);

    /**
     * 更新修改 策略语言
     * @param tipContent
     * @param strategyVoSaved
     * @return
     */
    boolean updateStrategyLang(List<StrategyRemarkVo> tipContent, StrategySaveVo strategyVoSaved);
}
