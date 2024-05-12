package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/26 16:38
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CommentMapper {

    /**
     * 新增
     */
    int insert(Comment comment);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Comment comment);

    /**
     * 根据ID查询
     */
    Comment selectById(Integer id);

    /**
     * 查询所有
     */
    List<Comment> selectAll(Comment comment);


    // userid 表示当前登录的userid
    List<Comment> selectComment(Integer userId);

    List<Comment> selectByUserId(Integer userId);

    @Select("select * from wyx_comment where accept_id = #{acceptId}")
    List<Comment> selectByAcceptId(Integer acceptId);
}
