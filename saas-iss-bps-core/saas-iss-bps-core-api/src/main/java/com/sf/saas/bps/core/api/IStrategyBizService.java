package com.sf.saas.bps.core.api;

import com.sf.saas.bps.core.dto.base.Page;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.req.StrategyVoQueryReq;
import com.sf.saas.bps.core.dto.vo.StrategyDetailVo;
import com.sf.saas.bps.core.dto.vo.StrategyPageVo;
import com.sf.saas.bps.core.dto.vo.StrategySaveVo;
import com.sf.saas.bps.core.dto.vo.StrategyUpdateStatusVo;

/**
 * Description IStrategyBizService
 *
 * @author suntao(01408885)
 * @since 2022-09-08
 */
public interface IStrategyBizService {
    /**
     * 新增保存
     * @param strategy
     * @return
     */
    Response<String> saveStrategy(StrategySaveVo strategy);

    /**
     * 更新策略
     * @param strategy
     * @return
     */
    Response<String> updateStrategy(StrategySaveVo strategy);

    /**
     * 获取详情
     * @param id
     * @return
     */
    Response<StrategyDetailVo> getStrategyDetail(long id);

    /**
     * 更新状态
     * @param strategy
     * @return
     */
    Response<String> updateStatus(StrategyUpdateStatusVo strategy);

    /**
     * 分页查询
     * @param query
     * @return
     */
    Page<StrategyPageVo> queryPage(StrategyVoQueryReq query);

    /**
     * 保存策略配置
     * @param strategyDetailVo
     * @return
     */
    Response<String> strategySetting(StrategyDetailVo strategyDetailVo);
}
