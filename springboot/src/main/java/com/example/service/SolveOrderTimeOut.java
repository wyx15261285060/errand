package com.example.service;/*
 *@author yviang wi
 *@date  2024/5/9 14:03
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.RecordEnum;
import com.example.entity.Order;
import com.example.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Component
public class SolveOrderTimeOut {

    private static Logger log = LoggerFactory.getLogger(SolveOrderTimeOut.class);

    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;
    // 一分钟扫描一次
    @Scheduled(fixedRate = 60000)
    // 定时取消订单任务
    public void task() {
        log.info("========================订单扫描任务开始========================");
        Order params = new Order();
        // 获取所有待接单状态的订单
        params.setStatus(OrderStatus.NO_ACCEPT.getValue());
        List<Order> orderList = orderService.selectAll(params);
        for (Order order:orderList){
            String time = order.getTime();
            DateTime dateTime = DateUtil.parseDateTime(time);
            // 计算下单时间和当前时间间隔秒数
            long betweenTime = DateUtil.between(dateTime, new Date(), DateUnit.SECOND);
            if (betweenTime >= 3000){
                // 超时之后即取消订单
                order.setStatus(OrderStatus.TIMEOUT.getValue());
                orderService.updateById(order);
                // 取消订单之后需要归还金额
                User user = userService.selectById(order.getUserId());
                user.setAccount(user.getAccount().add(BigDecimal.valueOf(order.getPrice())));
                userService.updateById(user);
                Double orderTip = order.getTip();
                Double orderPrice = order.getPrice();
                // 处理费用和小费为空的情况
                if (orderTip==null || orderPrice==null){
                    orderTip = 0.0;
                    orderPrice =0.0;
                }
                RecordService.addRecord("下单超时，自动取消订------" + order.getName(),BigDecimal.valueOf(orderPrice + orderTip), RecordEnum.TIMEOUT.getValue(),order.getUserId());
            }
        }
        log.info("========================订单扫描任务结束========================");
    }
}
