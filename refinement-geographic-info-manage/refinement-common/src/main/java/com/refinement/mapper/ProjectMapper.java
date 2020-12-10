package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.refinement.entity.Project;
import com.refinement.group.ProjectDept;
import com.refinement.group.ProjectPage;
import com.refinement.group.QueryConditionForTotalPro;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  ProjectMapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<ProjectDept> getProjectList(@Param("project") ProjectPage project);

    //返回项目剩余工作量
    Double selectRemainWork(String projectid);

    //更新项目状态
    int updateState(@Param("projectid") String projectid, @Param("state") int state);

    List<String> listProjectIds();

    int removeProject(String projectid);

    // 获取指定状态和部门的项目
    List<Project> getTotalProjectByStateAndDept(
            @Param("queryConditionForTotalPro") QueryConditionForTotalPro queryConditionForTotalPro);


    IPage<Project> listPage(Page<Object> objectPage,
                            @Param("type") Integer type,
                            @Param("name") String name,
                            @Param("categoryId") Long categoryId,
                            @Param("startTime") String startTime,
                            @Param("modelId") Long modelId,
                            @Param("deptId") String deptId);

    Integer selectCountByModel(@Param("type") Integer type, @Param("modelId") Long modelId);

    /**
     *  列表
     * @param type 项目类型 0-新项目 1-进行中项目 2-历史项目
     * @return
     */
    List<Project> listByStatus(@Param("type") Integer type, @Param("startDate") Date startDate
            , @Param("endDate") Date endDate, @Param("deptidList") List<String> deptidList
            , @Param("projectIdList") List<String> projectIdList
            , @Param("modelId") Long modelId);

}
