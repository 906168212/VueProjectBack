package com.example.Algorithm;

import com.example.Entity.dto.ArticleInfo;

import java.util.Date;

public class RankingAlgorithm {

    // 权重分配
    private static final double WEIGHT_CLR = 0.4;
    private static final double WEIGHT_IR = 0.6;

    // 指标归一化
    private static double normalize(double value, double min, double max) {
        return (value - min) * 100 / (max - min);
    }

    //评赞比指标
    private static double commentLikeRatio(int like,int comment){
        return (double) comment / like;
    }

    //互动指标
    private static double interactionRatio(int like,int comment,int collect,int visitor){
        return (double) (like + comment + collect) / visitor;
    }

    // 时间衰减函数
    private static double timeDecay(long pubDate) {
        // 这里可以根据实际情况调整衰减因子
        // 例如，假设一个月内的文章影响力不变，超过一个月则按月份衰减
        // 这里简化为时间间隔的线性衰减
        long timeInterval = new Date().getTime() - pubDate;
        return Math.exp(-timeInterval / (30 * 24 * 60 * 60 * 1000)); // 一个月的毫秒数
    }


//    public static double calculateRankingScore(ArticleInfo articleInfo){
//
//        double normalizedTime = timeDecay(articleInfo.getPubDate());
//
//        // 加权求和
//        double rawScore  =  WEIGHT_LIKE * normalizedLike +
//                WEIGHT_COLLECT * normalizedCollect +
//                WEIGHT_VIEW * normalizedView +
//                WEIGHT_COMMENT * normalizedComment;
//
//        // 应用时间衰减
//        double decayedScore = rawScore * normalizedTime;
//        return decayedScore;
//    }
}
