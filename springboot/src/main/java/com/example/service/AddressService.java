package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/20 0:09
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Address;
import com.example.mapper.AddressMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressService {
    @Resource
    private AddressMapper addressMapper;

    /**
     * 新增
     */
    public void add(Address address) {
        addressMapper.insert(address);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        addressMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            addressMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Address address) {
        addressMapper.updateById(address);
    }

    /**
     * 根据ID查询
     */
    public Address selectById(Integer id) {
        return addressMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Address> selectAll(Address address) {
        return addressMapper.selectAll(address);
    }

    /**
     * 分页查询
     */
    public PageInfo<Address> selectPage(Address address, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Address> list = addressMapper.selectAll(address);
        return PageInfo.of(list);
    }
}
