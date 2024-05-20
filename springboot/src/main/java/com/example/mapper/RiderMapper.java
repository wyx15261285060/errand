package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/5/18 21:31
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Rider;

import java.util.List;

public interface RiderMapper {

    /**
     * 新增
     */
    int insert(Rider rider);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Rider rider);

    /**
     * 根据ID查询
     */
    Rider selectById(Integer id);

    /**
     * 查询所有
     */
    List<Rider> selectAll(Rider rider);
}
