package com.example.entity;/*
 *@author yviang wi
 *@date  2024/4/26 15:58
 *@version 1.0
 *@email：3060491854@qq.com
 */

public class Comment {
    /** ID */
    private Integer id;
    /** 评论 */
    private String content;
    /** 评分 */
    private Double star;
    /** 用户ID */
    private Integer userId;
    /** 骑手ID */
    private Integer acceptId;
    /** 订单ID */
    private Integer orderId;
    /** 时间 */
    private String time;
    /**
     * 评论用户名
     */
    private String userName;
    /**
     * 骑手用户名
     */
    private String acceptName;
    /**
     * 订单号
     */
    private String orderNo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(Integer acceptId) {
        this.acceptId = acceptId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
