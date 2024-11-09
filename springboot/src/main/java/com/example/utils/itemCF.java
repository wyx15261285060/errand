//package com.example.utils;/*
// *@author yviang wi
// *@date  2024/5/24 21:59
// *@version 1.0
// *@email：3060491854@qq.com
// */
//
//import com.example.VO.DTO.RelateDTO;
//import com.example.VO.DTO.RelateVO;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class itemCF {
//    /**
//     * 方法描述: 推荐订单id列表
//     *
//     * @return {@link List <Integer>}
//     */
//    public static List<Integer> recommend(List<RelateVO> orderData, int riderId, int orderSum) {
//        // 相似度
//        Map<Integer, Double> similarityScores = new HashMap<>();
//        // 计算目标订单与其他订单的相似度
//        for (Map.Entry<Integer, Map<Integer, Integer>> entry : orderData.entrySet()) {
//            int orderId = entry.getKey();
//            if (orderId != riderId) {
//                double similarity = pearsonCorrelation(orderData.get(riderId), entry.getValue());
//                similarityScores.put(orderId, similarity);
//            }
//        }
//    // 计算皮尔森相关性系数
//    public static double pearsonCorrelation(Map<Integer, Integer> order1, Map<Integer, Integer> order2) {
//        // 计算订单的平均指数
//        double avgOrder1 = order1.values().stream().mapToInt(Integer::intValue).average().orElse(0);
//        double avgOrder2 = order2.values().stream().mapToInt(Integer::intValue).average().orElse(0);
//        // 计算协方差
//        double covariance = 0;
//        for (Integer key : order1.keySet()) {
//            if (order2.containsKey(key)) {
//                covariance += (order1.get(key) - avgOrder1) * (order2.get(key) - avgOrder2);
//            }
//        }
//        // 计算标准差
//        double stdDevOrder1 = Math.sqrt(order1.values().stream().mapToDouble(val -> Math.pow(val - avgOrder1, 2)).sum());
//        double stdDevOrder2 = Math.sqrt(order2.values().stream().mapToDouble(val -> Math.pow(val - avgOrder2, 2)).sum());
//
//        // 计算皮尔森相关性系数
//        if (stdDevOrder1 == 0 || stdDevOrder2 == 0) {
//            return 0; // 避免除零错误
//        } else {
//            return covariance / (stdDevOrder1 * stdDevOrder2);
//        }
//    }
//}
