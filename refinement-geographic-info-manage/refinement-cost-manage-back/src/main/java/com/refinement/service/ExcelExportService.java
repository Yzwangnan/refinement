package com.refinement.service;

import com.refinement.data.vo.ExcelExportVo;
import com.refinement.entity.Project;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * excel导出
 * @author htmic
 * @date 2020/10/28
 */
public interface ExcelExportService {

    /**
     * 进行中项目-项目导出
     * @return
     */
    ExcelExportVo exportProjectIng(Integer status, List<String> projectIdList, Long modelId);

    /**
     * 进行中项目-细化分解导出
     * @param projectId
     */
    HSSFWorkbook exportProjectDecomposition(String projectId);

    /**
     * 查询项目
     * @param startDateTime
     * @param endDateTime
     * @param deptidList
     * @param modelId
     * @return
     */
    List<Project> projectList(Date startDateTime, Date endDateTime, List<String> deptidList, Long modelId);

    /**
     * 历史项目-项目导出
     * @param response
     * @param projectIdList
     * @param type 表格形式： 1->项目成本导出；2->项目细化分解导出表里->项目结算报表
     * @param modelId
     */
    void historyProject(HttpServletResponse response, List<String> projectIdList, Integer type, Long modelId);

    /**
     * 项目结算报表
     * @param projectid
     * @return
     */
    XSSFWorkbook exportJiesuanProject(String projectid);
}
