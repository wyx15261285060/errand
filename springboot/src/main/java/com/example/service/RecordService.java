package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/26 14:49
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Record;
import com.example.mapper.RecordMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 收支明细业务处理
 **/
@Service
public class RecordService implements InitializingBean {


    @Resource
    private RecordMapper recordMapper;


    private static RecordMapper staticRecordMapper;


    /**
     * 新增
     */
    public void add(Record record) {
        recordMapper.insert(record);
    }

    /**
     * 收支明细
     */
    public static void addRecord(String content, BigDecimal money, String type) {
        // 将支出充值收入的金额更新后放到数据库中
        Record record = new Record();
        Account currentUser = TokenUtils.getCurrentUser();
        record.setUserId(currentUser.getId());
        record.setTime(DateUtil.now());
        record.setContent(content);
        record.setMoney(money);
        record.setType(type);
        staticRecordMapper.insert(record);

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

    /**
     * 赋值静态变量
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        staticRecordMapper = recordMapper;
    }
}
