package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/14 12:18
 *@version 1.0
 *@email：3060491854@qq.com
 */
import com.example.entity.Order;
import java.util.List;

/**
 * 操作Order相关数据接口
 */
public interface OrderMapper {

    /**
     * 新增
     */
    int insert(Order Order);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Order Order);

    /**
     * 根据ID查询
     */
    Order selectById(Integer id);

    /**
     * 查询所有
     */
    List<Order> selectAll(Order Order);

}
