package com.sf.saas.bps.core.common.utils;

/**
 * Description StrategyVersionUtil
 *
 * @author suntao(01408885)
 * @since 2022-09-09
 */
public class StrategyVersionUtil {


    public static String getVersion() {
        return Long.toHexString(System.currentTimeMillis() + NumberUtils.geneRandomCode(3));
    }
}
