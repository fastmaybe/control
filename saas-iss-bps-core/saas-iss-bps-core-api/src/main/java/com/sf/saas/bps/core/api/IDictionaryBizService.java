package com.sf.saas.bps.core.api;

import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.vo.DictionarySelectVo;

import java.util.List;
import java.util.Map;

/**
 * @author 01407460
 * @date 2022/9/7 11:15
 */
public interface IDictionaryBizService {

    /**
     * 根据字典表name查询字段表，供前端下拉选
     * @param dictionaryName
     * @return
     */
    List<DictionarySelectVo> getDictionarySelectByType(String dictionaryName);

    /**
     * 字典组
     * @return
     */
    Response<Map<String, List<DictionarySelectVo>>> dictionaryGroup();

    /**
     *重新 加载字典组
     * @return
     */
    Response<Boolean> reloadDictionaryGroup();
}
