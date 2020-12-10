package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.ProjectReport;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  ProjectReportMapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectReportMapper extends BaseMapper<ProjectReport> {

    //取当月汇报信息
    ProjectReport selectCurrentMonth(String projectid);

    //指定月份的汇报记录
    ProjectReport selectAssignReport(@Param("projectid") String projectid, @Param("reportTime") String reportTime);

}
