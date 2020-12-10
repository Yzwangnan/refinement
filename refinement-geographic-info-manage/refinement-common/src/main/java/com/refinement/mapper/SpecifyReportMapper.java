package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.SpecifyReport;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  SpecifyReportMapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface SpecifyReportMapper extends BaseMapper<SpecifyReport> {

    // 查询本月对应的进度列表
    List<SpecifyReport> selectCurrentMonthList(Long id);

    //取最新审批通过的上报记录
    SpecifyReport selectLastChecked(Long id);

    //根据起始和结束日期查询时间范围内完成工作量和时间范围完成产值
    Map<String, BigDecimal> selectCompletedByScopeTime(@Param("specifyid") Long specifyid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    //查询指定月份月对应的进度列表
    List<SpecifyReport> selectAssignSpecifyReportList(@Param("id") Long id, @Param("reportTime") String reportTime);
}
