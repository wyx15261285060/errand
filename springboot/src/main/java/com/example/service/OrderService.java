package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/14 12:17
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrderService {

    @Resource
    private OrderMapper OrderMapper;


    /**
     * 新增
     */
    public void add(Order Order) {
        OrderMapper.insert(Order);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        OrderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            OrderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Order Order) {
        OrderMapper.updateById(Order);
    }

    /**
     * 根据ID查询
     */
    public Order selectById(Integer id) {
        return OrderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Order> selectAll(Order Order) {
        return OrderMapper.selectAll(Order);
    }

    /**
     * 分页查询
     */
    public PageInfo<Order> selectPage(Order Order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = OrderMapper.selectAll(Order);
        return PageInfo.of(list);
    }

}
