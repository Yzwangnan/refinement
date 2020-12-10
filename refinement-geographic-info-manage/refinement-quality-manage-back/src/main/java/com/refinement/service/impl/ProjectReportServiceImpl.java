package com.refinement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.data.OneProReportDO;
import com.refinement.entity.ProjectReport;
import com.refinement.entity.ProjectSpecify;
import com.refinement.entity.SpecifyReport;
import com.refinement.mapper.ProjectMapper;
import com.refinement.mapper.ProjectReportMapper;
import com.refinement.mapper.ProjectSpecifyMapper;
import com.refinement.mapper.SpecifyReportMapper;
import com.refinement.service.ProjectReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Service
public class ProjectReportServiceImpl extends ServiceImpl<ProjectReportMapper, ProjectReport> implements ProjectReportService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectReportMapper projectReportMapper;

    @Resource
    private SpecifyReportMapper specifyReportMapper;

    @Resource
    private ProjectSpecifyMapper projectSpecifyMapper;

    @Override
    public OneProReportDO selectOne(String projectid) {

        ProjectReport projectReport =
                projectReportMapper.selectOne(new QueryWrapper<ProjectReport>().eq("projectid", projectid));

        if (projectReport == null) {
            return new OneProReportDO();
        }
        // 进行数据封装
        OneProReportDO odo = new OneProReportDO();
        odo.setId(projectReport.getId());
        odo.setProjectid(projectReport.getProjectid());
        odo.setReportnums(projectReport.getReportnums());
        odo.setDeptCheck(projectReport.getDeptCheck());
        odo.setDeptReason(projectReport.getDeptReason());
        odo.setPdCheck(projectReport.getPdCheck());
        odo.setPdReason(projectReport.getPdReason());
        odo.setCreateTime(projectReport.getCreateTime());
        odo.setUpdateTime(projectReport.getUpdateTime());
        return odo;
    }

    @Override
    public int dpCheck(String projectid, String reason, Integer state) {
        //查询项目当月项目进度报告
        ProjectReport pr = projectReportMapper.selectCurrentMonth(projectid);
        if (pr == null) {
            return -1;
        }
        pr.setUpdateTime(LocalDateTime.now());
        pr.setPdCheck(state);
        pr.setPdReason(reason);
        int ret = projectReportMapper.updateById(pr);
        // 审核通过确认项目进度
        if (ret > 0 && state == 1) {
            //查询本次报告信息列表  报告的明细
            List<SpecifyReport> specifyReportList = specifyReportMapper.selectList(new QueryWrapper<SpecifyReport>().eq("reportid", pr.getId()));
            if (specifyReportList != null) {
                for (SpecifyReport specifyReport: specifyReportList) {
                    //追加更新分项进度到 项目分项表
                    projectSpecifyMapper.updateProgress(specifyReport.getSpecifyid(), specifyReport.getCompleted(), specifyReport.getCompletedValue());
                }
            }
            //更新项目分项的上报次数
            List<ProjectSpecify> projectSpecifyList = projectSpecifyMapper.selectList(new QueryWrapper<ProjectSpecify>().eq("projectid", projectid));
            for (ProjectSpecify projectSpecify : projectSpecifyList) {
                projectSpecify.setReportnums(projectSpecify.getReportnums() + 1);
                projectSpecify.setUpdateTime(LocalDateTime.now());
                projectSpecifyMapper.updateById(projectSpecify);
            }

            //判断项目是否完成
            // 取得项目分项完成情况
//            Double remain = projectMapper.selectRemainWork(projectid);
//            if (remain != null && remain.doubleValue() < 0.0001) {
//                //项目已完成
//                int update = projectMapper.updateState(projectid, 2);
//                if (update <= 0) {
//                    return -1;
//                }
//            }
        }
        return ret;
    }

    @Override
    public int deptCheck(String projectid, String reason, Integer state) {
        //查询当月的上报信息
        ProjectReport pr = projectReportMapper.selectCurrentMonth(projectid);
        if (pr == null) {
            return -1;
        }
        pr.setUpdateTime(LocalDateTime.now());
        pr.setDeptCheck(state);
        pr.setDeptReason(reason);
        return projectReportMapper.updateById(pr);
    }

    @Override
    public ProjectReport selectCurrentMonth(String projectid) {
        return projectReportMapper.selectCurrentMonth(projectid);
    }

    @Override
    public ProjectReport selectAssignReport(String projectid, String reportTime) {
        return projectReportMapper.selectAssignReport(projectid, reportTime);
    }
}
