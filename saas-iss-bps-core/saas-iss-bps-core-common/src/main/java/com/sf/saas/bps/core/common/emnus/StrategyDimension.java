package com.sf.saas.bps.core.common.emnus;

/**
 * @author 01407460
 * @date 2022/9/20 15:39
 *
 * 唯独枚举   具体查看bps_dimension
 * 可自行加入
 */
public enum StrategyDimension {

    /**
     * 运单号
     */
    WAYBILL_NO("waybillNo","运单号"),


    ;

    private String key;
    private String desc;



    StrategyDimension(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }
}
