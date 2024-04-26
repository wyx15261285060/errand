package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/20 0:09
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Address;

import java.util.List;

public interface AddressMapper {

    /**
     * 新增
     */
    int insert(Address address);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Address address);

    /**
     * 根据ID查询
     */
    Address selectById(Integer id);

    /**
     * 查询所有
     */
    List<Address> selectAll(Address address);

}
