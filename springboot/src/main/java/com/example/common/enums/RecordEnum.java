package com.example.common.enums;/*
 *@author yviang wi
 *@date  2024/5/10 19:39
 *@version 1.0
 *@email：3060491854@qq.com
 */

public enum RecordEnum {
    OUT("支出"),
    CHARGE("充值"),
    INCOME("收入"),
    CANCEL("取消"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    RecordEnum(String value) {
        this.value = value;
    }
}
