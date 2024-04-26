package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/3 17:23
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 数据处理
 */

public interface UserMapper {
    /**
     * 新增
     */
    int insert(User user);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(User user);

    /**
     * 根据ID查询
     */
    User selectById(Integer id);

    /**
     * 查询所有
     */
    List<User> selectAll(User user);

    User selectByUsername(String username);

}
