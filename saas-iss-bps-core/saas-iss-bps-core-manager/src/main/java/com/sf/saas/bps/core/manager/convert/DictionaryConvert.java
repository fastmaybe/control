package com.sf.saas.bps.core.manager.convert;

import com.sf.saas.bps.core.dao.entity.Dictionary;
import com.sf.saas.bps.core.dto.vo.DictionarySelectVo;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description DictionaryConvert
 *
 * @author suntao(01408885)
 * @since 2022-09-07
 */
public class DictionaryConvert {


    public static List<DictionarySelectVo> convertDo2VoList(List<Dictionary> dictionaries) {
        if (CollectionUtils.isEmpty(dictionaries)){
            return Collections.emptyList();
        }
        return dictionaries.stream().map(DictionaryConvert::convertDo2Vo).collect(Collectors.toList());
    }

    private static DictionarySelectVo convertDo2Vo(Dictionary dictionary) {
        if (dictionary == null) {
            return null;
        }
        DictionarySelectVo vo = new DictionarySelectVo();
        vo.setName(dictionary.getName());
        vo.setDictKey(dictionary.getDictKey());
        vo.setDictValue(dictionary.getDictValue());
        vo.setSort(dictionary.getSort());
        vo.setLang(dictionary.getLang());
        vo.setRemark(dictionary.getRemark());
        return vo;
    }
}
