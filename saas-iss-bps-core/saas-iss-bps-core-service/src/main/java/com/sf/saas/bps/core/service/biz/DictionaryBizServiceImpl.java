package com.sf.saas.bps.core.service.biz;

import com.sf.saas.bps.core.api.IDictionaryBizService;
import com.sf.saas.bps.core.common.utils.ResponseHelper;
import com.sf.saas.bps.core.dto.base.Response;
import com.sf.saas.bps.core.dto.vo.DictionarySelectVo;
import com.sf.saas.bps.core.manager.cache.IDictionaryCache;
import com.sf.saas.bps.core.service.crud.IDictionaryService;
import com.sf.saas.bps.core.service.event.redis.message.MessageBase;
import com.sf.saas.bps.core.service.event.redis.message.RedisPublisher;
import com.sf.saas.bps.core.service.event.redis.message.bizmsg.DictionaryCacheMessage;
import com.sf.saas.bps.core.service.utils.CurrentReqInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 01407460
 * @date 2022/9/7 11:16
 */
@Service
public class DictionaryBizServiceImpl implements IDictionaryBizService {

    @Autowired
    private RedisPublisher<MessageBase> redisPublisher;

    @Autowired
    private IDictionaryService dictionaryService;

    @Autowired
    private IDictionaryCache dictionaryCache;

    @Override
    public List<DictionarySelectVo> getDictionarySelectByType(String dictionaryName) {
        return dictionaryService.getDictionarySelectByType(dictionaryName);
    }

    @Override
    public Response<Map<String, List<DictionarySelectVo>>> dictionaryGroup() {
        String currentLang = CurrentReqInfoUtil.currentLang();
        Map<String, List<DictionarySelectVo>> map = dictionaryCache.dictionaryGroupByLang(currentLang);
        return ResponseHelper.buildSuccess(map);
    }

    @Override
    public Response<Boolean> reloadDictionaryGroup() {

        DictionaryCacheMessage dictionaryCacheMessage = new DictionaryCacheMessage();
        redisPublisher.publish(dictionaryCacheMessage);

        dictionaryCache.cleanCache();
        return ResponseHelper.buildSuccess();
    }
}
