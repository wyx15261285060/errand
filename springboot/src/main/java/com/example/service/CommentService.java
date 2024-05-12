package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/26 16:37
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.Constants;
import com.example.common.enums.OrderStatus;
import com.example.entity.Account;
import com.example.entity.Comment;
import com.example.entity.Order;
import com.example.mapper.CommentMapper;
import com.example.utils.RedisUtils;
import com.example.utils.TokenUtils;
import com.github.pagehelper.Constant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private OrderService orderService;

    /**
     * 新增
     */
    @Transactional
    public void add(Comment comment) {
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);
        Integer orderId = comment.getOrderId();
        Order order = orderService.selectById(orderId);
        order.setStatus(OrderStatus.DONE.getValue());
        orderService.updateById(order);
        // 新增后设置缓存内容
        this.setCache(comment);
    }

    //更新用户的缓存评论
    public void setCache(Comment comment){
        List<Comment> list = commentMapper.selectByUserId(comment.getUserId());
        List<Comment> acceptList = commentMapper.selectByAcceptId(comment.getAcceptId());
        RedisUtils.setCacheObject(Constants.REDIS_CONSTANT_KEY + comment.getUserId(), list); //设置缓存,通过用户id来存储一组数据
        RedisUtils.setCacheObject(Constants.REDIS_CONSTANT_KEY + comment.getAcceptId(),acceptList);// 设置骑手缓存
    }



    /**
     * 删除
     */
    public void deleteById(Integer id) {
        // 在删除之后设置评价缓存，删除前查询评价信息
        Comment comment = commentMapper.selectById(id);
        commentMapper.deleteById(id);
        this.setCache(comment);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }


    public List<Comment> selectComment(Integer userId) {
//        Account currentUser = TokenUtils.getCurrentUser();
        // 先查询缓存
        List<Comment> cacheObject = RedisUtils.getCacheObject(Constants.REDIS_CONSTANT_KEY + userId);
        // 从缓存中查找的数据库为空时
        if (CollUtil.isEmpty(cacheObject)){
            // 从数据库中查所有评论信息
            cacheObject = commentMapper.selectComment(userId);
            // 缓存中没有的话，就重新设置换缓存
            for (Comment comment : cacheObject) {
                this.setCache(comment);
            }
        }
        return cacheObject;
    }
}
