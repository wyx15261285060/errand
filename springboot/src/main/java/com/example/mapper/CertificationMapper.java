package com.example.mapper;/*
 *@author yviang wi
 *@date  2024/4/20 1:03
 *@version 1.0
 *@email：3060491854@qq.com
 */
import com.example.entity.Certification;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 操作certification相关数据接口
 */
public interface CertificationMapper {

    /**
     * 新增
     */
    int insert(Certification certification);

    /**
     * 删除
     */
    int deleteById(Integer id);

    /**
     * 修改
     */
    int updateById(Certification certification);

    /**
     * 根据ID查询
     */
    Certification selectById(Integer id);

    /**
     * 查询所有
     */
    List<Certification> selectAll(Certification certification);

    Certification selectByUserId(Integer userId);
}
