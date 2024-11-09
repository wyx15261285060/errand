package com.example.VO.DTO;/*
 *@author yviang wi
 *@date  2024/5/18 21:55
 *@version 1.0
 *@email：3060491854@qq.com
 */
// 协同过滤算法模型
public class RelateVO {
    /** 骑手id */
    private Integer orderId;
    /** 订单Id */
    private Integer addressId;
    /** 相关性指数 */
    private Integer index;

    public RelateVO(Integer orderId, Integer addressId, Integer index) {
        this.orderId = orderId;
        this.addressId = addressId;
        this.index = index;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
