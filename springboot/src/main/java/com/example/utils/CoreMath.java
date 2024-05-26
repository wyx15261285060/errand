package com.example.utils;

/*
 * @author yviang wi
 * @date 2024/5/18 22:27
 * @version 1.0
 * @email：3060491854@qq.com
 */

import cn.hutool.core.collection.CollectionUtil;
import com.example.VO.DTO.RelateDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * 核心数学工具类，提供计算相关系数的功能
 */
public class CoreMath {
    /**
     * 计算并返回与指定骑手相关系数的排序映射
     *
     * @param key  指定骑手的键
     * @param map  包含骑手及其相关项的映射
     * @param type 类型标志，用于确定相关项的匹配方式
     * @return 与指定骑手相关系数的排序映射
     */
    public static Map<Integer, Double> computeNeighbor(Integer key, Map<Integer, List<RelateDTO>> map, int type) {
        // 创建一个树图映射，以确保键的有序性
        Map<Integer, Double> distMap = new TreeMap<>();
        // 获取当前骑手的相关项列表
        List<RelateDTO> riderItems = map.get(key);
        // 检查骑手项列表是否非空
        if (CollectionUtil.isNotEmpty(riderItems)) {
            // 遍历映射中的每一个用户，计算相关系数
            map.forEach((k, v) -> {
                // 排除当前用户自身
                if (!k.equals(key)) {
                    // 计算两个骑手相关项列表的相关性系数
                    try {
                        double coefficient = relateDist(v, riderItems, type);
                        // 计算相关距离（绝对值）
                        double distance = Math.abs(coefficient);
                        // 将用户键和相关距离添加到映射中
                        distMap.put(k, distance);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            });
        }
        return distMap;
    }

    /**
     * 计算两个序列之间的相关系数
     *
     * @param xList x循环变量骑手相关序列
     * @param yList y当前骑手相关序列
     * @param type  类型标志，用于确定序列的匹配方式
     * @return 两个序列之间的相关系数
     */
    private static double relateDist(List<RelateDTO> xList, List<RelateDTO> yList, int type) {
        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();
        // 根据类型标志，匹配并收集序列中的项
        xList.forEach(x -> yList.forEach(y -> {
            if (type == 0) {
                if (x.getUserId().equals(y.getUserId())) {
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            } else {
                if (x.getRiderId().equals(y.getRiderId())) {
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            }
        }));
        // 如果 xs 和 ys 都为空，抛出异常
        if (xs.isEmpty() && ys.isEmpty()) {
            throw new IllegalArgumentException("当前骑手和遍历种骑手无相关行为");
        }

        // 计算并返回两个序列的相关系数
        return getRelate(xs, ys);
    }

    /**
     * 计算皮尔森相关系数
     *
     * @param xs x集合
     * @param ys y集合
     * @return 皮尔森相关系数
     */
    public static double getRelate(List<Integer> xs, List<Integer> ys) {

        int n = xs.size();
        if (n < 2) {
            return 0D;
        }
        // 检查 xs 和 ys 是否所有元素都相等
        if (xs.equals(ys)) {
            return 1.0;
        }
        // 计算x和y的总和
        double Ex = xs.stream().mapToDouble(x -> x).sum();
        double Ey = ys.stream().mapToDouble(y -> y).sum();
        // 计算x和y的平方和
        double Ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double Ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        // 计算x和y的乘积之和
        double Exy = IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();
        // 计算相关系数的分子和分母
        double numerator = Exy - Ex * Ey / n;
        double denominator = Math.sqrt((Ex2 - Math.pow(Ex, 2) / n) * (Ey2 - Math.pow(Ey, 2) / n));
        // 若分母为0，则返回0；否则，计算并返回相关系数
        if (denominator == 0) {
            return 0D;
        }
        return numerator / denominator;
    }


}
