package com.sf.saas.bps.core.service.crud.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sf.saas.bps.core.dao.entity.Dictionary;
import com.sf.saas.bps.core.dao.mapper.DictionaryMapper;
import com.sf.saas.bps.core.dto.vo.DictionarySelectVo;
import com.sf.saas.bps.core.manager.convert.DictionaryConvert;
import com.sf.saas.bps.core.service.crud.IDictionaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author 01407460
 * @since 2022-09-06
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements IDictionaryService {

    @Override
    public List<DictionarySelectVo> getDictionarySelectByType(String dictionaryName) {
        LambdaQueryWrapper<Dictionary> queryWrapper = Wrappers.<Dictionary>lambdaQuery()
                .eq(Dictionary::getName, dictionaryName);
        List<Dictionary> dictionaries = baseMapper.selectList(queryWrapper);
        return DictionaryConvert.convertDo2VoList(dictionaries);
    }
}
