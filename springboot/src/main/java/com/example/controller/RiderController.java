package com.example.controller;/*
 *@author yviang wi
 *@date  2024/5/18 21:30
 *@version 1.0
 *@email：3060491854@qq.com
 */

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.entity.Order;
import com.example.entity.Rider;
import com.example.entity.User;
import com.example.service.NoticeService;
import com.example.service.RiderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping("/rider")
public class RiderController {
    @Resource
    private RiderService riderService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Rider Rider) {
        riderService.add(Rider);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        riderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        riderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Rider Rider) {
        riderService.updateById(Rider);
        return Result.success();
    }
    @GetMapping("/recommend")
    public Result recommend(){
       List<User> list =  riderService.recommendUser();
       return Result.success(list);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Rider Rider = riderService.selectById(id);
        return Result.success(Rider);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Rider Rider) {
        List<Rider> list = riderService.selectAll(Rider);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Rider Rider,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Rider> page = riderService.selectPage(Rider, pageNum, pageSize);
        return Result.success(page);
    }
}
