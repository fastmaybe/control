package com.sf.saas.bps.core.service.crud;

import com.sf.saas.bps.core.dao.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sf.saas.bps.core.dto.vo.DictionarySelectVo;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
public interface IDictionaryService extends IService<Dictionary> {

    List<DictionarySelectVo> getDictionarySelectByType(String dictionaryName);
}
