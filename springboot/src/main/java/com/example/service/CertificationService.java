package com.example.service;/*
 *@author yviang wi
 *@date  2024/4/20 1:02
 *@version 1.0
 *@email：3060491854@qq.com
 */
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Certification;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.CertificationMapper;
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
     */
    public void updateById(Certification certification) {
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