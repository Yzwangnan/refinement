package com.refinement.constant;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * 项目细化分级表标题
 */
public class ProjectSpecifyTitleConstant {

    /**
     * 汇总标题
     */
    public static final List<String> totalTitleList = CollUtil.toList(
            "分类名称-name", "预算金额-budgetAmount",
            "已使用-isUse", "上报人-reportPer", "最后上报时间-reportTime"
    );

    /**
     * 月报上报
     */
    public static final List<String> reportTitleList = CollUtil.toList(
            "一级分类-oneLevelName", "二级分类-twoLevelName", "三级分类-threeLevelName", "预算金额-budgetAmount",
            "已使用-isUse", "本月费用-monthCost", "余额-over"
    );


    /**
     * 月份标题-进行中项目
     */
    public static final List<String> monthTitleListOfProgress = CollUtil.toList(
            "一级分类-oneLevelName", "二级分类-twoLevelName", "三级分类-threeLevelName", "预算金额-budgetAmount",
            "已使用-isUse", "上报人-reportPer", "更新时间-reportTime", "本月费用-monthCost", "余额-over", "确认-verify"
    );

    /**
     * 月份标题-历史项目
     */
    public static final List<String> monthTitleListOfHistory = CollUtil.toList(
            "分类名称-name", "预算金额-budgetAmount",
            "累计支出-isUse", "上报人-reportPer", "本月支出-monthCost", "余额-over"
    );
}
