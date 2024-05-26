package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/3 17:11
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.RecordEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Certification;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 用户业务服务
 */

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private CertificationService certificationService;
    /**
     * 新增用户
     */
    public void add(User user) {
        // 用户的唯一性校验
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 批量删除用户信息
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(User user) {
        userMapper.updateById(user);
    }

    /**
     * 根据ID查询
     */
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    /**
     * 分页查询
     */
    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        // 查询到当前用户的身份信息
        Certification certification = certificationService.selectByUserId(dbUser.getId());
        dbUser.setRider(ObjectUtil.isNotNull(certification) && certification.getStatus().equals("通过"));
        return dbUser;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        User user = new User();

        //密码MD5加密处理
        //盐值+password+盐值 盐值(一个随机的字符串)
        //String oldPassword=account.getPassword();
        //获取盐值,随机生成一个盐值
        //String salt= UUID.randomUUID().toString().toUpperCase();
        //account.setSalt(salt);
        //        将密码和盐值作为一个整体进行加密,提高了数据的安全
        //String md5Password=getMD5Password(oldPassword,salt);
        //account.setPassword(md5Password);
        // Bean工具类的拷贝对象属性值，浅拷贝
        BeanUtils.copyProperties(account, user);
        this.add(user);
    }

    //定义一个md5算法的加密处理
    private String getMD5Password(String password,String salt) {
        // md5加密算法调用 转换成字节
        //进行加密三次
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        //返回加密后的密码
        return password;
    }


    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }


    public void charge(Double money) {
        Account currentUser = TokenUtils.getCurrentUser();
        currentUser.setAccount(currentUser.getAccount().add(BigDecimal.valueOf(money)));
        this.updateById((User) currentUser);
        // 充值明细记录
        RecordService.addRecord("充值" ,BigDecimal.valueOf(money), RecordEnum.CHARGE.getValue(),currentUser.getId());
    }

    public Boolean selectByPhone(String phone) {
        boolean flag = false;
        List<User> userList = userMapper.selectByPhone(phone);
        if (userList.size() == 0){
            flag = true;
        }
        return flag;
    }
}
