package com.example.entity;/*
 *@author yviang wi
 *@date  2024/4/26 14:28
 *@version 1.0
 *@email：3060491854@qq.com
 */

import java.math.BigDecimal;
public class Record {
    /**
     * ID
     */
    private Integer id;
    /**
     * 事件
     */
    private String content;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 用户
     */
    private Integer userId;
    /**
     * 时间
     */
    private String time;
    /**
     * 类型
     */
    private String type;
    /**
     * 用户名
     */
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
