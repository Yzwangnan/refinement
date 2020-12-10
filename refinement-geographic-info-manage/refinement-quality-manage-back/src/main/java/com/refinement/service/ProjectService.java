package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.ReportRecordDO;
import com.refinement.entity.Project;
import com.refinement.group.ProjectPage;
import com.refinement.http.ResultDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectService extends IService<Project> {

    // 项目列表
    ResultDTO getProjectList(ProjectPage project);

    // 新建项目
    ResultDTO addProject(Project project, String password);

    // 项目的新建确认
    ResultDTO confirmNew(String projectid);

    // 项目细化分解上报
    ResultDTO progress(String projectid , String progress);

    // 查询系统项目总数
    Long selectTotal();

    List<String> listProjectIds();

    // 更新项目状态
    void updateProjectStatus(String projectid, Integer state);

    // 删除项目
    int removeProject(String projectid);

    // 项目汇报记录
    List<ReportRecordDO> reportRecord(String projectid);
}
