package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/26 14:49
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.entity.Record;
import com.example.mapper.RecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收支明细业务处理
 **/
@Service
public class RecordService {
   
    

        @Resource
        private RecordMapper recordMapper;


        /**
         * 新增
         */
        public void add(Record record) {
            recordMapper.insert(record);
        }

        /**
         * 删除
         */
        public void deleteById(Integer id) {
            recordMapper.deleteById(id);
        }

        /**
         * 批量删除
         */
        public void deleteBatch(List<Integer> ids) {
            for (Integer id : ids) {
                recordMapper.deleteById(id);
            }
        }

        /**
         * 修改
         */
        public void updateById(Record record) {
            recordMapper.updateById(record);
        }

        /**
         * 根据ID查询
         */
        public Record selectById(Integer id) {
            return recordMapper.selectById(id);
        }

        /**
         * 查询所有
         */
        public List<Record> selectAll(Record record) {
            return recordMapper.selectAll(record);
        }

        /**
         * 分页查询
         */
        public PageInfo<Record> selectPage(Record record, Integer pageNum, Integer pageSize) {
            PageHelper.startPage(pageNum, pageSize);
            List<Record> list = recordMapper.selectAll(record);
            return PageInfo.of(list);
        }
    }
