package com.sf.saas.bps.core.common.constans;

/**
 * @author 01407460
 * @date 2022/9/7 10:19
 */
public class BpsConstant {
    private BpsConstant(){}

    public static final int i_0 = 0;
    public static final int i_1 = 1;
    public static final int i_2 = 2;
    public static final int i_3 = 3;
    public static final int i_4 = 4;

    public static final int BATCH_LIMIT = 100;

    public static final String HEADER_USER = "username";
    public static final String HEADER_LAN = "language";
    public static final String HEADER_TENANT_ID = "tenantId";


    public static  final  String TRACE_ID="sf_trace_id";

    public static final String EDIT_BY_SYSTEM = "SYSTEM";

    public static final String DEFAULT_LANG="en";

    public static final String ADMIN_USER = "admin";

    /**
     * 常用分隔符
     */
    public static final String SPLIT_COMMON = ",";
    /**
     * 分号
     */
    public static final String SPLIT_SEMICOLON = ";";
    /**
     * 下划线
     */
    public static final String SPLIT_UNDERLINE = "_";
    /**
     * 点
     */
    public static final String SPLIT_SPOT = ".";
    /**
     * STRATEGY_SAVE
     */
    public static final String STRATEGY_SAVE = "bps:strategy:save:";
    public static final String STRATEGY_UPDATE = "bps:strategy:update:";


}
