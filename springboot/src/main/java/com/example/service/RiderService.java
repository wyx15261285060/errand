package com.example.service;/*
 *@author yviang wi
 *@date  2024/5/18 21:32
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.collection.CollectionUtil;
import com.example.VO.DTO.RelateDTO;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
//import com.example.utils.itemCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiderService {
    @Resource
    private RiderMapper riderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private AddressMapper addressMapper;


    /**
     * 新增
     */
    public void add(Rider rider) {
        riderMapper.insert(rider);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        riderMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            riderMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Rider rider) {
        riderMapper.updateById(rider);
    }

    /**
     * 根据ID查询
     */
    public Rider selectById(Integer id) {
        return riderMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Rider> selectAll(Rider rider) {
        return riderMapper.selectAll(rider);
    }

    /**
     * 分页查询
     */
    public PageInfo<Rider> selectPage(Rider rider, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Rider> list = riderMapper.selectAll(rider);
        return PageInfo.of(list);
    }
    /**
     * 基于用户行为的协同推荐算法
     * 给骑手推荐用户
     * @return
     */
    public List<User> recommendUser() {
        // 用户哪些行为可以和骑手产生关系  （'订单'，‘评论’）
        // 已完成订单订单
        List<Order> orderList = orderMapper.selectFinishedAll();
        // 所有评论
        List<Comment> commentList = commentMapper.selectAll(null);
        // 所有普通用户
        List<User> userList = userMapper.selectAllUSER("USER");
        // 所有的骑手信息
        List<Rider> riderList = riderMapper.selectAll(null);
        // 开始计算每个骑手和每个普通用户之间的相关指数 笛卡尔积 index
        List<RelateDTO> list = new ArrayList<>();
        for (User user : userList) {
            //用户id
            Integer userId = user.getId();
            for (Rider rider : riderList) {
                //骑手id
                Integer riderId = rider.getId();
                // 相关性指数 默认为1
                int index = 1;
                // 判断订单中该用户和该骑手是否与遍历得到的骑手用户一致
                Optional<Order> optionalOrder = orderList.stream().filter(x -> x.getUserId().equals(userId) && x.getAcceptId().equals(riderId)).findFirst();
                // 如果该用户曾和该骑手有过交易关系 index权重+1
                if (optionalOrder.isPresent()) {
                    index += 1;
                }
                // 判断评论中该用户和该骑手是否有关联
                Optional<Comment> commentOptional = commentList.stream().filter(x -> x.getUserId().equals(userId) && x.getAcceptId().equals(riderId)).findFirst();
                if (commentOptional.isPresent()) {
                    index += 1;
                }
                if (index > 1){
                    RelateDTO relateDTO = new RelateDTO(userId, riderId, index);
                    list.add(relateDTO);
                }
            }
        }
        // 数据准备结束后，将这些数据传递给推荐算法
        Account currentUser = TokenUtils.getCurrentUser();
        // 返回 推荐的用户id
        List<Integer> userIds = UserCF.recommend(currentUser.getId(), list);
        // 把骑手id转换成骑手
        List<User> result = userIds.stream().map(userId -> userList.stream().filter(x -> x.getId().equals(userId)).findFirst().orElse(null))
                .limit(10).collect(Collectors.toList());
        return result;
    }


//    /**
//     * 基于订单特征的协同推荐算法
//     * @return
//     */
//    public List<Order> recommend() {
//        // 找到所有的地址
//        List<Address> addressList =  addressMapper.selectAll(null);
//        // 已完成订单订单
//        List<Order> orderList = orderMapper.selectFinishedAll();
//        // 所有评论
//        List<Comment> commentList = commentMapper.selectAll(null);
//        // 所有用户
//        List<User> userList = userMapper.selectAll(null);
//        // 所有的骑手信息
//        List<Rider> riderList = riderMapper.selectAll(null);
//
//        // 开始计算每个骑手和每个订单之间的相关指数 笛卡尔积 index
//        List<RelateDTO> list = new ArrayList<>();
//        for (Order order : orderList) {
//            int orderId = order.getId();
//            int addressId = order.getAddressId();
//            int index = calculateIndex(addressId); // 根据实际的计算逻辑计算相关指数
//            list.add(new RelateDTO(orderId, addressId, index));
//        }
//        // 数据准备结束后，将这些数据传递给推荐算法
//        Account currentUser = TokenUtils.getCurrentUser();
//        Integer riderId = currentUser.getId();
//        // 返回 推荐的订单id
//        // 在前端判断过必须只有骑手登录时才会执行该方法，因此currentUser.getId获取到的是骑手id
//        // 获取推荐的订单ID列表
//        int orderSum = 10; // 假设推荐10个订单
//        List<Integer> recommendedOrderIds = itemCF.recommend(list, riderId, orderSum);
////        List<Integer> orderIds = UserCF.recommend(currentUser.getId(), list);
//        // 把订单id转换成订单
//        List<Order> result = recommendedOrderIds.stream().map(orderId -> orderList.stream().filter(x -> x.getId().equals(orderId)).findFirst().orElse(null))
//                .limit(10).collect(Collectors.toList());
//        return result;
//    }
//
//    private int calculateIndex(int addressId) {
//        // 已完成订单订单
//        List<Order> orderList = orderMapper.selectFinishedAll();
//        // 构建骑手的历史订单地址映射
//        Map<Integer, List<Integer>> riderOrderAddressMap = orderList.stream()
//                .collect(Collectors.groupingBy(Order::getAcceptId,
//                        Collectors.mapping(Order::getAddressId, Collectors.toList())));
//        int total = 0;
//        for (List<Integer> addressList : riderOrderAddressMap.values()) {
//            for (Integer addressID : addressList) {
//                if (addressID.equals(addressId)) {
//                    total++;
//                }
//            }
//        }
//        return total;
//    }


//    private List<Rider> getRandomRider(int num) {
//        List<Rider> riderList1 = new ArrayList<>(num);
//        List<Rider> riderLists = riderMapper.selectAll(null);
//        for (int i = 0; i < num; i++) {
//            int index = new Random().nextInt(riderLists.size());
//            riderList1.add(riderLists.get(index));
//        }
//        return riderList1;
//    }


}
