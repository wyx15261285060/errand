package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/14 12:17
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.common.Result;
import com.example.common.enums.OrderStatus;
import com.example.common.enums.RecordEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.OrderMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Resource
    private AddressService addressService;
    @Resource
    private CertificationService certificationService;
    @Resource
    private RecordService recordService;
    @Resource
    private RiderService riderService;


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
    @Transactional
    public void updateById(Order order) {
        Double orderTip = order.getTip();
        Double orderPrice = order.getPrice();
        // 处理费用和小费为空的情况
        if (orderTip==null || orderPrice==null){
            orderTip = 0.0;
            orderPrice =0.0;
        }
        Double sumPrice = orderPrice + orderTip;
        if (OrderStatus.NO_RECEIVE.getValue().equals(order.getStatus())) {
            // 骑手送达订单情况
            Integer acceptId = order.getAcceptId();
            // 查找到接单的骑手
            User user = userService.selectById(acceptId);
            // 将骑手的余额账户加上费用并更新
            user.setAccount(user.getAccount().add(BigDecimal.valueOf(sumPrice)));
            userService.updateById(user);
            // 接单收支明细
            RecordService.addRecord("接单" + order.getName(), BigDecimal.valueOf(sumPrice), RecordEnum.INCOME.getValue(), order.getUserId());
        } else if (OrderStatus.CANCEL.getValue().equals(order.getStatus())) {
            RecordService.addRecord("用户主动取消" + order.getName(), BigDecimal.valueOf(sumPrice), RecordEnum.CANCEL.getValue(), order.getUserId());
        }
        // 更新订单状态
        orderMapper.updateById(order);
    }

    /**
     * 根据ID查询
     */
    public Order selectById(Integer id) {
        Order order = orderMapper.selectById(id);
        Address address = addressService.selectById(order.getAddressId());
        order.setAddress(address);  // 取货地址
        Address targetAddress = addressService.selectById(order.getTargetId());
        order.setTargetAddress(targetAddress);  // 收货地址
        Certification certification = certificationService.selectByUserId(order.getAcceptId());
        order.setCertification(certification);
        return order;
    }

    /**
     * 小程序查询所有订单信息
     * 根据用户角色(只有骑手方可查询全部订单)
     * 当前用户角色为普通用户 只可以查看自己的订单
     */
    public List<Order> selectAllByRole(Order order) {
        List<Order> list = new ArrayList<>();
        // 查询到订单对应的用户
        User user = userService.selectById(order.getUserId());
        Account currentUser = TokenUtils.getCurrentUser();
        // 当前用户身份为骑手可以查看所有人订单
        if (currentUser.getRole().equals("RIDER")) {
            list = orderMapper.selectAll(order);
        }
        // 当前用户身份为普通用户
        if (currentUser.getRole().equals("USER")){
            // 将当前用户放进order中
            order.setUserId(currentUser.getId());
            list = orderMapper.selectByUserId(order);
        }
        // 计算下单时长
        for (Order o : list) {
            String time = o.getTime();
            Date date = new Date();
            int i = (int) DateUtil.between(DateUtil.parseDateTime(time), date, DateUnit.MINUTE);
            o.setMinutes(i);
        }
        return list;
    }
    /**
     * 后台管理系统查看全部订单
     */
    public List<Order> selectAll(Order order) {
        List<Order> list = orderMapper.selectAll(order);
        // 计算下单时长
        list.forEach(o -> {
            String time = o.getTime();
            Date date = new Date();
            int i = (int) DateUtil.between(DateUtil.parseDateTime(time), date, DateUnit.MINUTE);
            o.setMinutes(i);
        });
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
     * 小程序下单
     */
    public void addOrder(Order order) {
        Account account = TokenUtils.getCurrentUser();
        BigDecimal personalAccount = account.getAccount();
        Double orderPrice = order.getPrice();
        Double orderTip = order.getTip();
        // 下单时 如果为null 则默认为0.0
        orderTip = (orderTip == null) ? 0.0 : orderTip;
        Double sumPrice = orderPrice + orderTip;
        // 余额不足
        if (sumPrice > personalAccount.doubleValue()) {
            throw new CustomException(ResultCodeEnum.PERSONAL_ACCOUNT_LIMIT_ERROR);
        }
        // 将账户余额更新
        account.setAccount(personalAccount.subtract(BigDecimal.valueOf(sumPrice)));
        userService.updateById((User) account);
        // 设置该条订单的用户id和订单编号
        order.setUserId(account.getId());
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());//自动生成唯一的订单编号
        order.setStatus(OrderStatus.NO_ACCEPT.getValue());
        order.setTime(DateUtil.now());
        orderMapper.insert(order);
        // 下单明细记录
        RecordService.addRecord("下单" + order.getName(), BigDecimal.valueOf(sumPrice), RecordEnum.OUT.getValue(), order.getUserId());
    }

    /**
     * 乐观锁+事务解决高并发问题
     *
     * @param order
     */
    @Transactional
    public void acceptOrder(Order order) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 骑手不能接自己下的订单
        order.setAcceptId(currentUser.getId());
        order.setAcceptTime(DateUtil.now());
        order.setStatus(OrderStatus.NO_ARRIVE.getValue());
        // 获取当前版本号
        Integer currentVersion = order.getVersion();
        // 尝试更新订单，使用版本号进行条件更新
        boolean update = orderMapper.updateByIdAndVersion(order.getId(), currentVersion, order.getAcceptId(), order.getStatus(), order.getAcceptTime());
        if (!update) {
            throw new RuntimeException("订单已被其他骑手接单");
        }
    }

}
