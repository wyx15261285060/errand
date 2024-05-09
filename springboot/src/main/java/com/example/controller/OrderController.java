package com.example.controller;/*
 *@author yviang wi
 *@date  2024/4/14 12:15
 *@version 1.0
 *@email：3060491854@qq.com
 */
import com.example.common.Result;
import com.example.entity.Order;
import com.example.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 订单信息前端操作接口
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 小程序下单对应的接口
     */
    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return Result.success();
    }
    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Order order) {
        orderService.add(order);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        orderService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        orderService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Order Order) {
        orderService.updateById(Order);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Order Order = orderService.selectById(id);
        return Result.success(Order);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Order Order) {
        List<Order> list = orderService.selectAll(Order);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Order order,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Order> page = orderService.selectPage(order, pageNum, pageSize);
        return Result.success(page);
    }

}
