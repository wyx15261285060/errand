package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/20 1:02
 *@version 1.0
 *@email：3060491854@qq.com
 */
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Certification;
import com.example.entity.Rider;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.CertificationMapper;
import com.example.mapper.RiderMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class CertificationService {

    @Resource
    private CertificationMapper certificationMapper;

    @Resource
    private RiderMapper riderMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(Certification certification) {
        Certification dbCert = certificationMapper.selectByUserId(certification.getUserId());
        if (dbCert != null){
            throw new CustomException(ResultCodeEnum.CERTIFICATION_ERROR);
        }
        certificationMapper.insert(certification);
    }
    /**
     * 查询当前用户的认证信息
     */

    public Certification selectUserCert(){
        Account currentUser = TokenUtils.getCurrentUser();
        Certification dbCert = certificationMapper.selectByUserId(currentUser.getId());
        return dbCert;
    }
    public Certification selectByUserId(Integer userId){
        Certification dbCert = certificationMapper.selectByUserId(userId);
        return dbCert;
    }
    /**
     * 删除
     */
    public void deleteById(Integer id) {
        certificationMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            certificationMapper.deleteById(id);
        }
    }

    /**
     * 修改
     * 审核骑手认证
     */
    public void updateById(Certification certification) {
//        userMapper.updateById()
        // 更新后将用户角色修改为RIDER
        User user = userMapper.selectById(certification.getUserId());
        user.setRole("RIDER");
        userMapper.updateById(user);
        // 根据用户id来查询骑手认证信息
        Certification cert = certificationMapper.selectByUserId(certification.getUserId());
        // 根据用户id查询到对应用户信息
        User certUser = userMapper.selectById(certification.getUserId());
        Rider rider = new Rider();
        // 当数据库中的状态为通过 前端为拒绝时 需要将骑手表中该骑手信息修改   吊销骑手
        if (cert.getStatus().equals("通过") && certification.getStatus().equals("拒绝")){
            Rider deleteRider = riderMapper.selectById(certification.getUserId());
            // 骑手表存在该骑手,则需要将该骑手删除 并将其身份设置为USER
            if (ObjectUtil.isNotEmpty(deleteRider)){
               riderMapper.deleteById(certification.getUserId());
               user.setRole("USER");
               userMapper.updateById(user);
            }
        }
        // 前端传入参数中认证状态为通过并且数据库里面的状态为待审核时，将该骑手信息添加到Rider表中
        // 只有在数据库为 拒绝或者待审核时 并且前端传入的是通过，才可以添加骑手信息
        if ((cert.getStatus().equals("待审核")||cert.getStatus().equals("拒绝")) && certification.getStatus().equals("通过")){
            rider.setId(certification.getUserId());
            rider.setUsername(certification.getName());
            rider.setPassword(certUser.getPassword());
            rider.setSex(certUser.getSex());
            rider.setName(certUser.getName());
            rider.setAvatar(certification.getAvatar());
            rider.setEmail(certUser.getEmail());
            rider.setPhone(certification.getPhone());
            rider.setRole("RIDER");
            rider.setAccount(certUser.getAccount());
            // 将骑手放入骑手表
            riderMapper.insert(rider);
        }
        // 状态由拒绝变为待审核
        if (cert.getStatus().equals("拒绝") && certification.getStatus().equals("待审核")){
            User initUser = userMapper.selectById(certification.getUserId());
            initUser.setRole("USER");
            userMapper.updateById(initUser);
        }
        // 更新认证状态
        certificationMapper.updateById(certification);

    }

    /**
     * 根据ID查询
     */
    public Certification selectById(Integer id) {
        return certificationMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Certification> selectAll(Certification certification) {
        return certificationMapper.selectAll(certification);
    }

    /**
     * 分页查询
     */
    public PageInfo<Certification> selectPage(Certification certification, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Certification> list = certificationMapper.selectAll(certification);
        return PageInfo.of(list);
    }

}