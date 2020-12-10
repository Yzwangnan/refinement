package com.refinement.constant;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * 项目标题常量
 */
public class ProjectTitleConstant {

    /**
     * 新项目标题
     */
    public static final List<String> newProject = CollUtil.newArrayList(
            "序号", "项目ID", "项目名称", "合同总产值", "所属事业部", "开工时间", "工期", "项目类型", "区域（省）",
            "实施状态", "预算金额");

    /**
     * 进行中项目标题
     */
    public static final List<String> progressProject = CollUtil.newArrayList(
            "项目ID", "项目名称", "开工时间", "工期", "合同总产值", "预算金额", "已使用", "比例");

    /**
     * 历史项目标题
     */
    public static final List<String> historyProject = CollUtil.newArrayList(
            "项目ID", "项目名称", "开工时间", "工期", "合同总产值", "预算金额", "已使用", "比例");
}
