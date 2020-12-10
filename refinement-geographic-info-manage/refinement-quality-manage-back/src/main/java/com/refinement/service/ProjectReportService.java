package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.OneProReportDO;
import com.refinement.entity.ProjectReport;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectReportService extends IService<ProjectReport> {

    // 查询单个项目进度
    OneProReportDO selectOne(String projectid);

    // 生产运营部审核-项目月进度上报信息审核
    int dpCheck(String projectid, String reason, Integer state);

    // 事业部审核-项目月进度上报信息审核
    int deptCheck(String projectid, String reason, Integer state);

    //取本月上报记录
    ProjectReport selectCurrentMonth(String projectid);

    //指定月份的汇报记录
    ProjectReport selectAssignReport(String projectid, String reportTime);
}
