package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/14 12:17
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;


    /**
     * 新增
     */
    public void add(Order Order) {
        orderMapper.insert(Order);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        orderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            orderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Order Order) {
        orderMapper.updateById(Order);
    }

    /**
     * 根据ID查询
     */
    public Order selectById(Integer id) {
        return orderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Order> selectAll(Order order) {
        List<Order> list = orderMapper.selectAll(order);
        for (Order o : list){
            String time = o.getTime();
            Date date = new Date();
            int i = (int) DateUtil.between(DateUtil.parseDateTime(time),date, DateUnit.MINUTE);
            o.setMinutes(i);
        }
        return list;
    }

    /**
     * 分页查询
     */
    public PageInfo<Order> selectPage(Order order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> list = orderMapper.selectAll(order);
        return PageInfo.of(list);
    }

}
