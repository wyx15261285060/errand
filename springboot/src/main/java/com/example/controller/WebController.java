package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.CertificationService;
import com.example.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.omg.CORBA.Object;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 基础前端接口
 */
@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private CertificationService certificationService;

    @Resource
    RedisTemplate<String, String> redisTemplate;


    @GetMapping("/")
    public Result hello() {
        return Result.success("访问成功");
    }

    /**
     * 登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
//        Result result = new Result();
        // 判断如果角色、用户名、密码存在空值
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        // 从redis中将验证码取出来，与输入框中的对比
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            if (ObjectUtil.isEmpty(account.getCode())){
                return Result.error(ResultCodeEnum.CODE_NOT_EXIST);
            }
            String uuid = account.getUuid();
            String verifyCode = redisTemplate.opsForValue().get(uuid);
            if (!account.getCode().equals(verifyCode)) {
                return Result.error(ResultCodeEnum.CODE_ERROR);
            } else {
                account = adminService.login(account);
            }
        } else if ((RoleEnum.USER.name().equals(account.getRole())) || (RoleEnum.RIDER.name().equals(account.getRole()))) {
            account = userService.login(account);
        } else {
            return Result.error(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        return Result.success(account);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getRole()) || StrUtil.isBlank(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        Boolean flag = userService.selectByPhone(account.getPhone());
        if (!flag) {
            throw new CustomException(ResultCodeEnum.PHONE_IS_EXIST);
        }
        if (!(account.getNewPassword().equals(account.getPassword()))) {
            return Result.error(ResultCodeEnum.DIFFERENT_PASSWORD);
        }
        if (RoleEnum.USER.name().equals(account.getRole())) {
            userService.register(account);
        } else {
            return Result.error(ResultCodeEnum.ROLE_ERROR);
        }
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        //确保不为空
        if (StrUtil.isBlank(account.getUsername()) || StrUtil.isBlank(account.getPassword())
                || ObjectUtil.isEmpty(account.getNewPassword())) {
            return Result.error(ResultCodeEnum.PARAM_LOST_ERROR);
        }
        // 身份判断
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            adminService.updatePassword(account);
            // 用户和骑手修改密码
        } else if ((RoleEnum.USER.name().equals(account.getRole())) || (RoleEnum.RIDER.name().equals(account.getRole()))) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
