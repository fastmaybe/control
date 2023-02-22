package com.sf.saas.bps.core.service.strategy.bo;

import lombok.Data;

import java.util.Set;

/**
 * @author 01407460
 * @date 2022/9/14 16:57
 */
@Data
public class ConditionStringParams implements  ConditionParams {

    private Set<String>  paramsSet;

}
