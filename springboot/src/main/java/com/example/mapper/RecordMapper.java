package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/26 14:51
 *@version 1.0
 *@email：3060491854@qq.com
 */


import com.example.entity.Record;

import java.util.List;

public interface RecordMapper {
    /**
     * 新增
     */
    int insert(Record record);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Record record);

    /**
     * 根据ID查询
     */
    Record selectById(Integer id);

    /**
     * 查询所有
     */
    List<Record> selectAll(Record record);




}
