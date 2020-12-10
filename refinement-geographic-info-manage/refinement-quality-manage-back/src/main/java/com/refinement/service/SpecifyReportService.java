package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.entity.SpecifyReport;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-05-13
 */
public interface SpecifyReportService extends IService<SpecifyReport> {

    //根据起始和结束日期查询时间范围内完成工作量和时间范围完成产值
    Map<String, BigDecimal> selectCompletedByScopeTime(Long specifyid, String startTime, String endTime);

}
