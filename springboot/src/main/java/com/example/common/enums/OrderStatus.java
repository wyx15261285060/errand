package com.example.common.enums;/*
 *@author yviang wi
 *@date  2024/4/17 22:33
 *@version 1.0
 *@email：3060491854@qq.com
 */


public enum OrderStatus {

    CANCEL("已取消"),
    NO_ACCEPT("待接单"),
    NO_ARRIVE("待送达"),
    NO_RECEIVE("待收货"),
    NO_COMMENT("待评价"),
    DONE("已完成"),
    ;

    OrderStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
