package com.example.service;/*
 *@author yviang wi
 *@date  2024/5/18 21:32
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.collection.CollectionUtil;
import com.example.VO.DTO.RelateDTO;
import com.example.entity.*;
import com.example.mapper.CommentMapper;
import com.example.mapper.OrderMapper;
import com.example.mapper.RiderMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
     *
     * @return
     */
    public List<Rider> recommend() {
        // 用户哪些行为可以和骑手产生关系  （'订单'，‘评论’）
        // 所有订单
        List<Order> orderList = orderMapper.selectFinishedAll();
        // 所有评论
        List<Comment> commentList = commentMapper.selectAll(null);
        // 所有用户
        List<User> userList = userMapper.selectAll(null);
        // 所有的骑手信息
        List<Rider> riderList = riderMapper.selectAll(null);

        // 开始计算每个骑手和每个用户之间的相关指数 笛卡尔积 index
        List<RelateDTO> list = new ArrayList<>();
        for (Rider rider : riderList) {
            //骑手id
            Integer riderId = rider.getId();
            for (User user : userList) {
                //用户id
                Integer userId = user.getId();
                // 相关性指数 默认为1
                int index = 1;
                // 判断订单中该用户和该骑手是否与遍历得到的骑手用户一致
                Optional<Order> optionalOrder = orderList.stream().filter(x -> x.getAcceptId().equals(riderId) && x.getUserId().equals(userId)).findFirst();
                // 如果该用户曾和该骑手有过交易关系 index权重+1
                if (optionalOrder.isPresent()) {
                    index += 1;
                }
                // 判断评论中该用户和该骑手是否有关联
                Optional<Comment> commentOptional = commentList.stream().filter(x -> x.getAcceptId().equals(riderId) && x.getUserId().equals(userId)).findFirst();
                if (commentOptional.isPresent()) {
                    index += 2;
                }
                if (index > 1){
                    RelateDTO relateDTO = new RelateDTO(userId, riderId, index);
                    list.add(relateDTO);
                }
            }
        }
        // 数据准备结束后，将这些数据传递给推荐算法
        Account currentUser = TokenUtils.getCurrentUser();
        // 返回 推荐的骑手id
        List<Integer> riderIds = UserCF.recommend(currentUser.getId(), list);
        // 把骑手id转换成骑手
        List<Rider> result = riderIds.stream().map(riderId -> riderList.stream().filter(x -> x.getId().equals(riderId)).findFirst().orElse(null))
                .limit(10).collect(Collectors.toList());
        //判断推荐骑手是否为空
        if (CollectionUtil.isEmpty(result)) {
            // 随机给它推荐10个
            return getRandomRider(10);
        }
        if (result.size() < 10) {
            int num = 10 - result.size();
            List<Rider> list2 = getRandomRider(num);
            result.addAll(list2);
        }
        return result;
    }

    private List<Rider> getRandomRider(int num) {
        List<Rider> riderList1 = new ArrayList<>(num);
        List<Rider> riderLists = riderMapper.selectAll(null);
        for (int i = 0; i < num; i++) {
            int index = new Random().nextInt(riderLists.size());
            riderList1.add(riderLists.get(index));
        }
        return riderList1;
    }


}
