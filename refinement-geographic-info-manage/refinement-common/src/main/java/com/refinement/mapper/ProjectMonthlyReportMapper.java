package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.ProjectMonthlyReport;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface ProjectMonthlyReportMapper extends BaseMapper<ProjectMonthlyReport> {

    /**
     * 查询汇总
     * @param projectDecompositionId
     * @return
     */
    BigDecimal statisticsAllAmount(@Param("projectDecompositionId") Long projectDecompositionId);

    /**
     * 查询项目的有哪些月份报表
     * @param projectId
     * @return
     */
    List<String> mothsByProjectId(@Param("projectId") String projectId);

    /**
     * 查询项目的月份报表
     * @param projectDecompositionId
     * @param moth
     * @return
     */
    BigDecimal statisticsMothAmount(@Param("projectDecompositionId") Long projectDecompositionId, @Param("moth") String moth);

    void insertAndReturnId(ProjectMonthlyReport report);
}
