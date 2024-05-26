package com.example.utils;
import cn.hutool.core.collection.CollectionUtil;
import com.example.VO.DTO.RelateDTO;
import com.example.utils.CoreMath;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCF {
    /**
     * 方法描述: 推荐骑手id列表
     *
     * @param riderId 当前骑手
     * @param list   用户骑手评分数据
     * @return {@link List<Integer>}
     */
    public static List<Integer> recommend(Integer riderId, List<RelateDTO> list) {
        // 按骑手分组
        Map<Integer, List<RelateDTO>> riderMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getRiderId));
        // 获取其他骑手与当前骑手的关系值
        Map<Integer, Double> riderDisMap = CoreMath.computeNeighbor(riderId, riderMap, 0);
        if (CollectionUtil.isEmpty(riderDisMap)) {
            return Collections.emptyList();
        }
        // 获取关系最近的骑手
        double maxValue = Collections.max(riderDisMap.values());
        Set<Integer> riderIds = riderDisMap.entrySet().stream().filter(e -> e.getValue() == maxValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        // 取关系最近的骑手
        Integer nearestRiderId = riderIds.stream().findAny().orElse(null);
        if (nearestRiderId == null) {
            return Collections.emptyList();
        }
        // 最近邻骑手配送过的用户列表
        List<Integer> neighborItems = riderMap.get(nearestRiderId).stream().map(RelateDTO::getUserId).collect(Collectors.toList());
        // 当前骑手配送过的用户列表
        List<Integer> userItems = riderMap.get(riderId).stream().map(RelateDTO::getUserId).collect(Collectors.toList());
        // 找到最近邻接受过服务，但是该用户没接受过服务的商品
        neighborItems.removeAll(userItems);
        return neighborItems;
    }
}
