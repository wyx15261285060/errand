package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/14 12:17
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Order;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.OrderMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private UserService userService;


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

    /**
     *小程序下单
     */
    public void addOrder(Order order) {
        Account account = TokenUtils.getCurrentUser();
        BigDecimal personalAccount = account.getAccount();
        Double orderPrice = order.getPrice();
        // 余额不足
        if (orderPrice > personalAccount.doubleValue()){
            throw new CustomException(ResultCodeEnum.PERSONAL_ACCOUNT_LIMIT_ERROR);
        }
        // 将账户余额更新
        account.setAccount(personalAccount.subtract(BigDecimal.valueOf(orderPrice)));
        userService.updateById((User) account);
        // 设置该条订单的用户id和订单编号
        order.setUserId(account.getId());
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());//自动生成唯一的订单编号
        order.setStatus(OrderStatus.NO_ACCEPT.getValue());
        order.setTime(DateUtil.now());
        orderMapper.insert(order);
    }
}
