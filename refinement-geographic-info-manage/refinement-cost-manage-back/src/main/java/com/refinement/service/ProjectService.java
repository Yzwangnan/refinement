package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.param.ProjectAddParam;
import com.refinement.data.param.ProjectDecompositionParam;
import com.refinement.data.param.ProjectMonthlyReportParam;
import com.refinement.data.param.ProjectVerifyParam;
import com.refinement.data.vo.*;
import com.refinement.entity.Project;

import java.util.List;

public interface ProjectService extends IService<Project> {

    /**
     * 项目列表
     * @param type 项目类型 0-新项目 1-进行中项目 2-历史项目
     * @param name 项目名称
     * @param categoryId 二级分类id
     * @param startTime 开始时间
     * @param modelId 模板id
     * @param page 页
     * @param size 大小
     * @return ProjectListPageVO
     */
    ProjectListPageVO list(Integer type, String name, Long categoryId, String startTime, Long modelId, Integer page, Integer size);

    /**
     * 新建项目
     * @param param 参数
     */
    void add(ProjectAddParam param);

    /**
     * 创建项目id
     * @return String
     */
    String createProjectId();

    /**
     * 查询项目
     * @param projectId 项目id
     * @return Project
     */
    Project getByProjectId(String projectId);

    /**
     * 细化分解
     * @param param 参数
     */
    void decomposition(ProjectDecompositionParam param);

    /**
     * 新建确认
     * @param projectid 项目id
     */
    void confirm(String projectid);

    /**
     * 月报标题列表
     * @param projectid 项目id
     * @return MonthlyReportVO
     */
    MonthlyReportVO getMonthlyReportTitleList(String projectid);

    /**
     * 成本细化分解列表
     *
     * @param projectid 项目id
     * @param type 项目类型 1-进行中 2-历史
     * @param month 月份
     * @return ProjectSpecifyVO
     */
    ProjectSpecifyVO specifyList(String projectid, Integer type, Integer month);

    /**
     * 月报评审
     * @param param 审评数据
     */
    void verify(ProjectVerifyParam param);

    /**
     * 新增月报获取模板
     * @param projectid 项目id
     * @return List
     */
    List<OneLevelSpecifyVO> getReportModel(String projectid);

    /**
     * 月报新增
     * @param param 参数
     */
    void addMonthlyReport(ProjectMonthlyReportParam param);

    /**
     * 月报修改
     * @param param 参数
     */
    void updateMonthlyReport(ProjectMonthlyReportParam param);

    /**
     * 项目完成
     * @param projectid
     */
    void complete(String projectid);

    /**
     * 项目详情
     * @param projectid 项目id
     * @return Wrapper
     */
    ProjectDetailVO detail(String projectid);
}
