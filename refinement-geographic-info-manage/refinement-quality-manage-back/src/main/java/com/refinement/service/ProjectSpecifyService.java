package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.entity.ProjectSpecify;
import com.refinement.group.SpecifyRe;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface ProjectSpecifyService extends IService<ProjectSpecify> {

    // 项目细化进度列表
    List<SpecifyRe> getSpecifyList(String projectid);

    // 更新项目细化分解
    int updateSpecify(String projectid, List<ProjectSpecify> projectSpecifyList);

    // 指定月份的完成工作量和产值
    List<SpecifyRe> getAssignSpecifyList(String projectid, String reportTime);

    //判断项目是否达到细化分解标准
    boolean isSpecify(String projectid);

    //临时退出保存细化分解的内容
    void savePreSpecify(String projectid, List<ProjectSpecify> specList);
}
