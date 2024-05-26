package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/14 12:18
 *@version 1.0
 *@email：3060491854@qq.com
 */
import com.example.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.omg.PortableInterceptor.INACTIVE;

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
    List<Order> selectAll(Order order);
    @Update("UPDATE wyx_order SET " +
            "accept_id = #{acceptId}, " +
            "accept_time = #{acceptTime}, " +
            "status = #{status}, " +
            "version = version + 1 WHERE id = #{id} AND version = #{version}")
    boolean updateByIdAndVersion(Integer id, Integer version, Integer acceptId,String status,String acceptTime);

    // 查询所有已完成的订单信息
    @Select("select * from wyx_order where status='已完成' or status='待评价'")
    List<Order> selectFinishedAll();


    /**
     * 只有骑手和本人用户方可查看全部订单
     */
    List<Order> selectByUserId(Order order);
}
