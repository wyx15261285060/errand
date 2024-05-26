package com.example.VO.DTO;/*
 *@author yviang wi
 *@date  2024/5/18 21:55
 *@version 1.0
 *@email：3060491854@qq.com
 */
// 协同过滤算法模型
public class RelateDTO {
    /** 骑手id */
    private Integer riderId;
    /** 用户 */
    private Integer userId;
    /** 相关性指数 */
    private Integer index;

    public RelateDTO(Integer userId, Integer riderId, Integer index) {
        this.userId = userId;
        this.riderId = riderId;
        this.index = index;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRiderId() {
        return riderId;
    }

    public void setRiderId(Integer riderId) {
        this.riderId = riderId;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
