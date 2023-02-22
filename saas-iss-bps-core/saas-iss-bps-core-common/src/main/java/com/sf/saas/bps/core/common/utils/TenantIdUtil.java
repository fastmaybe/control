package com.sf.saas.bps.core.common.utils;

import com.sf.saas.bps.core.common.constans.BpsConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * Description TenantIdUtil
 *
 * @author suntao(01408885)
 * @since 2022-09-14
 */
public class TenantIdUtil {

    public static boolean validTenantId(String reqTenantId, String currentTenantId, String currentUser) {

        if (!StringUtils.equals(reqTenantId, currentTenantId)) {
            if (BpsConstant.ADMIN_USER.equals(currentUser)) {
                return true;
            }
            return false;
        }
        return true;
    }
}
