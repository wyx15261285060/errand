package com.example.utils;/*
 *@author yviang wi
 *@date  2024/5/18 22:24
 *@version 1.0
 *@email：3060491854@qq.com
 */

import cn.hutool.core.collection.CollectionUtil;
import com.example.VO.DTO.RelateDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCF {
    /**
     * 方法描述: 推荐商品id列表
     *
     * @param userId 当前用户
     * @param list   用户商品评分数据
     * @return {@link List<Integer>}
     */
    public static List<Integer> recommend(Integer userId, List<RelateDTO> list) {
        // 按用户分组
        Map<Integer, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUserId));
        // 获取其他用户与当前用户的关系值
        Map<Integer, Double> userDisMap = CoreMath.computeNeighbor(userId, userMap, 0);
        if (CollectionUtil.isEmpty(userDisMap)) {
            return Collections.emptyList();
        }
        // 获取关系最近的用户
        double maxValue = Collections.max(userDisMap.values());
        Set<Integer> userIds = userDisMap.entrySet().stream().filter(e -> e.getValue() == maxValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        // 取关系最近的用户
        Integer nearestUserId = userIds.stream().findAny().orElse(null);
        if (nearestUserId == null) {
            return Collections.emptyList();
        }
        // 最近邻用户接受服务的骑手列表
        List<Integer> neighborItems = userMap.get(nearestUserId).stream().map(RelateDTO::getRiderId).collect(Collectors.toList());
        // 指定用户接受过服务的骑手列表
        List<Integer> userItems = userMap.get(userId).stream().map(RelateDTO::getRiderId).collect(Collectors.toList());
        // 找到最近邻接受过服务，但是该用户没接受过服务的商品
        neighborItems.removeAll(userItems);
        return neighborItems;
    }
}
