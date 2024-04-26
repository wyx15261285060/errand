package com.example.controller;/*
 *@author yviang wi
 *@date  2024/4/26 14:55
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.common.Result;
import com.example.entity.Order;
import com.example.entity.Record;
import com.example.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 收支明细
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Resource
    private RecordService recordService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Record record){
        recordService.add(record);
        return Result.success();
    }
    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        recordService.deleteById(id);
        return Result.success();
    }
    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        recordService.deleteBatch(ids);
        return Result.success();
    }
    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Record record) {
        recordService.updateById(record);
        return Result.success();
    }
    /**
     * 查询单个
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(recordService.selectById(id));
    }
    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Record record) {
        List<Record> list = recordService.selectAll(record);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Record record, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10")Integer pageSize) {
        PageInfo<Record> page = recordService.selectPage(record, pageNum, pageSize);
        return Result.success(page);
    }

}
