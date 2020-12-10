package com.refinement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.entity.SpecifyReport;
import com.refinement.mapper.SpecifyReportMapper;
import com.refinement.service.SpecifyReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-05-13
 */

@Service
public class SpecifyReportServiceImpl extends ServiceImpl<SpecifyReportMapper, SpecifyReport> implements SpecifyReportService {

    @Resource
    private SpecifyReportMapper specifyReportMapper;

    @Override
    public Map<String, BigDecimal> selectCompletedByScopeTime(Long specifyid, String startTime, String endTime) {
        return specifyReportMapper.selectCompletedByScopeTime(specifyid, startTime, endTime);
    }
}
