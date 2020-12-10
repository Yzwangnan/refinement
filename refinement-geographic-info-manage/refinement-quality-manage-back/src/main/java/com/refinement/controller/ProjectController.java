package com.refinement.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.refinement.data.ClassifyCategoryDO;
import com.refinement.data.ReportRecordDO;
import com.refinement.entity.Project;
import com.refinement.entity.ProjectReport;
import com.refinement.entity.ProjectSpecify;
import com.refinement.group.*;
import com.refinement.http.PageResult;
import com.refinement.http.ResultDTO;
import com.refinement.service.*;
import com.refinement.util.CommonUtils;
import com.refinement.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Api(tags = "项目")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectReportService projectReportService;

    @Resource
    private ProjectSpecifyService projectSpecifyService;

    @Resource
    private SpecifyReportService specifyReportService;

    @Resource
    private ClassifyCategoryService categoryService;

    /**
     * 获取项目列表接口
     *
     * @param project 查询条件
     * @param size 页面大小
     * @return ResultDTO
     */
    @PostMapping("/list")
    @ApiOperation(value = "list", notes = "分页项目列表")
    public ResultDTO list(@RequestBody ProjectPage project,
                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        //参数校验 project
        if (project == null || project.getState() == null) {
            return new ResultDTO(201, "缺少参数");
        }
        if (project.getPage() == null || project.getPage() <= 0) {
            //非法情况默认一页
            project.setPage(1);
        }
        // 开启分页 获取项目分页数据
        Page<ProjectDept> pages = PageHelper.startPage(project.getPage(), size);
        // 获取所属部门项目总数及合同额总数 ，如果部门deptid为空的话，那么将查询所有的项目
        ResultDTO resultDTO = projectService.getProjectList(project);
        // 获取上一步骤的查询结果数据
        Map<String, Object> map = (Map<String, Object>) resultDTO.getResult();
        // 请求成功，进行数据封装
        // 传输对象
        ProjectListVO pvo = new ProjectListVO();
        // 查询系统项目的总数
        pvo.setProjectNum(Long.parseLong(map.get("totalProject").toString()));
        // 分页参数
        PageResult pageInfo = CommonUtils.pageInfo(pages, project.getPage());
        pvo.setPageInfo(pageInfo);
        pvo.setProjectList(pages.getResult());
        // 获取分页列表，并且计算时间进度
        List<ProjectDept> projectList = pvo.getProjectList();
        if (!CollectionUtils.isEmpty(projectList)) {
            for (ProjectDept dept : projectList) {
                //现在时间
                Date now = new Date();
                LocalDate localNowDate = CommonUtils.dateToLocalDate(now);
                //项目开工日期
                Date startdate = dept.getStartdate();
                LocalDate localStartDate = CommonUtils.dateToLocalDate(startdate);
                //项目结束时间
                Date endDate = CommonUtils.localDateToDate(localStartDate.plusMonths(Long.parseLong(dept.getPeriod())));
                LocalDate localEndDate = CommonUtils.dateToLocalDate(endDate);
                if (localNowDate.compareTo(localEndDate) >= 0) {
                    //今天是项目结束时间直接返回100%
                    dept.setTimeSchedule(new BigDecimal("100"));
                    continue;
                }
                if (localNowDate.compareTo(localStartDate) < 0) {
                    //今天还没到开工日期
                    dept.setTimeSchedule(new BigDecimal("0"));
                    continue;
                }
                //现在距开工日期的天数
                int nowCount = CommonUtils.differDay(startdate, now);
                //项目结束时间距开工日期的天数
                int endCount = CommonUtils.differDay(startdate, endDate);

                BigDecimal timeSchedule = new BigDecimal(String.valueOf(nowCount))
                        .divide(new BigDecimal(String.valueOf(endCount)), 2, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
                dept.setTimeSchedule(timeSchedule);
            }
        }
        // 设置合同额
        if (!CollectionUtils.isEmpty(map)) {
            //合同总额
            pvo.setTotalContractValue(new BigDecimal(map.get("totalContractValue").toString()));
            //当月完成产值
            pvo.setMonthValue(new BigDecimal(map.get("monthValue").toString()));
            //累计完成产值
            pvo.setTotalValue(new BigDecimal(map.get("totalValue").toString()));
            //剩余合同额
            pvo.setSurplusContractValue(new BigDecimal(map.get("surplusContractValue").toString()));
        }
        return new ResultDTO(pvo);
    }

    /**
     * 新建项目接口
     *
     * @param region 地区
     * @param password 密码
     * @param reportday 上报日期
     * @param projectid 项目id
     * @param projectname 项目名
     * @param contractvalue 合同产值
     * @param deptid 部门id
     * @param startdate 开工日期
     * @param period 项目工期
     * @param classifyId 一级项目分类id
     * @param categoryId 二级项目分类id
     * @return ResultDTO
     */
    @PostMapping("/addProject")
    @ApiOperation(value = "addProject", notes = "新建项目")
    public ResultDTO addProject (String region, String password, String reportday, String projectid,
                                 String projectname, String contractvalue, String deptid, String startdate,
                                 String period, Long classifyId, Long categoryId){
        //参数校验 region：区域
        if (StringUtils.isEmpty(region)) {
            return new ResultDTO(201, "区域（省）不能为空");
        }
        //参数校验 password：密码
        if (StringUtils.isEmpty(password)) {
            return new ResultDTO(201, "缺少参数");
        }
        //参数校验 reportday：每月上报时间
        if (StringUtils.isEmpty(reportday)) {
            return new ResultDTO(201, "上报时间不能为空");
        }
        //参数校验 projeceid：项目id
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "缺少参数");
        }
        //参数校验 projectname：项目名称
        if (StringUtils.isEmpty(projectname)) {
            return new ResultDTO(201, "项目名称不能为空");
        }
        //参数校验 contractvalue：合同总产值（万）
        if (StringUtils.isEmpty(contractvalue)) {
            return new ResultDTO(201, "合同产值不能为空");
        } else {
            // 判断contractvalue小数点后的位数
            if (contractvalue.substring(contractvalue.indexOf(".") + 1).length() > 6) {
                return new ResultDTO(201, "合同产值最多保留六位小数");
            }
        }
        //参数校验 deptid：所属部门
        if (StringUtils.isEmpty(deptid)) {
            return new ResultDTO(201, "所属事业部不能为空");
        }
        //参数校验 startdate：项目开工时间
        if (StringUtils.isEmpty(startdate)) {
            return new ResultDTO(201, "开工时间不能为空");
        }
        //参数校验 period：项目工期
        if (StringUtils.isEmpty(period)) {
            return new ResultDTO(201, "项目工期不能为空");
        }
        //参数 budgetAmount：预算金额
//        if (budgetAmount == null) {
//            return new ResultDTO(201, "预算金额不能为空");
//        }
        List<Project> projectList
                = projectService.list(new QueryWrapper<Project>().eq("projectid", projectid));

        if (projectList != null && projectList.size() > 0) {
            return new ResultDTO(201, "项目已创建");
        }
        //入参封装
        Project project = new Project();
        project.setReportday(Integer.parseInt(reportday));
        project.setProjectid(projectid);
        project.setProjectname(projectname);
        project.setContractvalue(new BigDecimal(contractvalue));
        project.setDeptid(deptid);
        project.setStartdate(LocalDate.parse(startdate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        project.setPeriod(period);
        project.setRegion(region);
//        project.setBudgetAmount(budgetAmount);
        String category = "";
        //查询一级分类名
        if (classifyId != null) {
            ClassifyCategoryDO classify = categoryService.selectClassifyById(classifyId);
            category = classify.getName() + "/";
        }
        //查询二级分类名
        if (classifyId != null) {
            ClassifyCategoryDO categoryDO = categoryService.selectCategoryById(categoryId);
            category = category + categoryDO.getName();
        }
        project.setCategory(category);
        project.setClassifyId(classifyId);
        project.setCategoryId(categoryId);
        return projectService.addProject(project, password);
    }

    /**
     * 项目新建确认接口
     *
     * @param projectid 项目id
     * @return ResultDTO
     */
    @PostMapping("/confirmNew")
    @ApiOperation(value = "confirmNew", notes = "项目的新建确认")
    public ResultDTO confirmNew (String projectid){
        //参数校验 projectid
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "缺少参数");
        }
        boolean flag = projectSpecifyService.isSpecify(projectid);
        if (!flag) {
            return new ResultDTO(201, "请先配置细化分解");
        }
        return projectService.confirmNew(projectid);
    }


    /**
     * 项目细化分解表接口
     *
     * @param projectid 项目id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return ResultDTO
     */
    @PostMapping("/specifyList")
    @ApiOperation(value = "specifyList", notes = "项目细化分解表")
    public ResultDTO specifyList (String projectid, String startTime, String endTime){
        //参数校验 projectid
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "项目id不能为空");
        }
        //返回前台数据
        ProjectSpeReportVO pvo = new ProjectSpeReportVO();
        //项目细化表
        List<SpecifyRe> specifyList = projectSpecifyService.getSpecifyList(projectid);
        //根据起始和结束日期查询时间范围内完成工作量和时间范围完成产值
        if (!StringUtils.isEmpty(startTime) || !StringUtils.isEmpty(endTime)) {
            //判断字符串是否为指定格式
            if (!StringUtils.isEmpty(startTime) && CommonUtils.checkDate(startTime)) {
                return new ResultDTO(201, "日期格式不正确");
            }
            if (!StringUtils.isEmpty(endTime) && CommonUtils.checkDate(endTime)) {
                return new ResultDTO(201, "日期格式不正确");
            }
            for (SpecifyRe specifyRe : specifyList) {
                Long specifyid = specifyRe.getId();
                Map<String, BigDecimal> completeMap = specifyReportService.selectCompletedByScopeTime(specifyid, startTime, endTime);
                if (completeMap == null) {
                    specifyRe.setCalcCompleted(BigDecimal.ZERO);
                } else {
                    BigDecimal calcCompleted = completeMap.get("calcCompleted");
                    specifyRe.setCalcCompleted(calcCompleted);
                }
                if (completeMap == null) {
                    specifyRe.setCalcCompletedValue(BigDecimal.ZERO);
                } else {
                    BigDecimal calcCompletedValue = completeMap.get("calcCompletedValue");
                    specifyRe.setCalcCompletedValue(calcCompletedValue);
                }
            }
        } else {
            for (SpecifyRe specifyRe : specifyList) {
                specifyRe.setCalcCompleted(specifyRe.getTotalCompleted());
                specifyRe.setCalcCompletedValue(specifyRe.getTotalCompletedValue());
            }
        }
        pvo.setSpecifyList(specifyList);
        //取项目本月汇报记录
        ProjectReport reportInfo = projectReportService.selectCurrentMonth(projectid);
        if (reportInfo != null) {
            pvo.setReportInfo(reportInfo);
        }
        return new ResultDTO(pvo);
    }

    /**
     * 更新项目细化分解接口
     *
     * @param proSpecify 分解项
     * @return ResultDTO
     */
    @PostMapping("/updateSpecify")
    @ApiOperation(value = "updateSpecify", notes = "更新项目细化分解")
    public ResultDTO updateSpecify (@RequestBody ProSpecify proSpecify){
        if (proSpecify == null) {
            return new ResultDTO(201, "缺少细化分接项");
        }
        String projectid = proSpecify.getProjectid();
        String specify = proSpecify.getSpecify();
        //参数校验
        if (StringUtils.isEmpty(projectid) || StringUtils.isEmpty(specify)) {
            return new ResultDTO(201, "缺少参数");
        }
        //Json Parse specify
        List<ProjectSpecify> specList = JSON.parseArray(specify, ProjectSpecify.class);
        for (ProjectSpecify proSpec : specList) {
            //分项内容
            String subitem = proSpec.getSubitem();
            if (StringUtils.isEmpty(subitem)) {
                return new ResultDTO(201, "分项内容不能为空");
            }
            //数量
            BigDecimal quantity = proSpec.getQuantity();
            if (quantity == null || quantity.equals(new BigDecimal("0.00"))) {
                return new ResultDTO(201, "数量不能为空");
            }

            //单价
            String price = proSpec.getPrice();
            if (StringUtils.isEmpty(price)) {
                return new ResultDTO(201, "单价不能为空");
            }
        }
        int ret = projectSpecifyService.updateSpecify(projectid, specList);
        // 操作校验
        if (ret > 0) {
            return new ResultDTO(200, "细化分解成功");
        } else if (ret == -1) {
            return new ResultDTO(201, "各分项的金额不等于总金额,请重新输入!");
        } else {
            return new ResultDTO(201, "细化分解失败");
        }
    }

    /**
     * 项目细化分解上报接口
     *
     * @param progressVo 上报项
     * @return ResultDTO
     */
    @PostMapping("/progress")
    @ApiOperation(value = "progress", notes = "项目细化分解上报")
    public ResultDTO progress (@RequestBody ProgressVO progressVo){
        if (progressVo == null) {
            return new ResultDTO(201, "缺少细化分解上报");
        }
        //参数校验 projectid
        if (StringUtils.isEmpty(progressVo.getProjectid())) {
            return new ResultDTO(201, "项目id不能空");
        }
        //参数校验 progress
        if (StringUtils.isEmpty(progressVo.getProgress())) {
            return new ResultDTO(201, "项目进度不能为空");
        }
        return projectService.progress(progressVo.getProjectid(), progressVo.getProgress());
    }

    /**
     * 生产运营部审核接口
     *
     * @param projectid 项目id
     * @param reason 审核意见
     * @param state 审核状态：通过-1 打回-2
     * @return ResultDTO
     */
    @PostMapping("/dpCheck")
    @ApiOperation(value = "dpCheck", notes = "生产运营部审核-项目月进度上报信息审核")
    public ResultDTO dpCheck (String projectid, String reason, Integer state){
        if (StringUtils.isEmpty(projectid) || state == null) {
            return new ResultDTO(201, "项目id不能为空");
        }
        if (state == 2 && StringUtils.isNotEmpty(reason)) {
            reason = reason.trim();
            if (StringUtils.isEmpty(reason)) {
                return new ResultDTO(201, "打回意见不能为空");
            }
        }
        int ret = projectReportService.dpCheck(projectid, reason, state);
        if (ret > 0) {
            if (state == 1) {
                return new ResultDTO(200, "成功保存进度");
            } else {
                return new ResultDTO(200, "已打回至项目部");
            }
        }
        return new ResultDTO(201, "项目进度上报失败！请重新上报");
    }

    /**
     * 事业部审核接口
     *
     * @param projectid 项目id
     * @param reason 审核意见
     * @param state 审核状态：通过-1 打回-2
     * @return ResultDTO
     */
    @PostMapping("/deptCheck")
    @ApiOperation(value = "deptCheck", notes = "事业部审核-项目月进度上报信息审核")
    public ResultDTO deptCheck (String projectid, String reason, Integer state){
        if (StringUtils.isEmpty(projectid) || state == null) {
            return new ResultDTO(201, "项目id不能为空");
        }
        if (state == 2 && StringUtils.isNotEmpty(reason)) {
            reason = reason.trim();
            if (StringUtils.isEmpty(reason)) {
                return new ResultDTO(201, "打回意见不能为空");
            }
        }
        int ret = projectReportService.deptCheck(projectid, reason, state);
        if (ret > 0 && state == 1) {
            return new ResultDTO(200, "成功保存进度");
        }
        if (ret > 0 && state == 2) {
            return new ResultDTO(200, "已打回至项目部");
        }
        return new ResultDTO(201, "项目进度上报失败！请重新上报");
    }

    /**
     * 不带分页的获取项目列表接口
     *
     * @param project 查询条件
     * @return ResultDTO
     */
    @PostMapping("/findAll")
    @ApiOperation(value = "findAll", notes = "项目列表")
    public ResultDTO list (@RequestBody ProjectPage project){
        //参数校验 project
        if (project == null || project.getState() == null) {
            return new ResultDTO(201, "缺少参数");
        }
        ResultDTO resultDTO = projectService.getProjectList(project);
        if (resultDTO != null && resultDTO.getResult() == null) {
            return resultDTO;
        }
        //请求成功，进行数据封装
        // 传输对象
        ProjectListVO pvo = new ProjectListVO();
        // 查询系统项目的总数
        Long projectNum = projectService.selectTotal();
        pvo.setProjectNum(projectNum);
        Map<String, Object> map = (Map<String, Object>) resultDTO.getResult();
        pvo.setProjectList((List<ProjectDept>) map.get("projectList"));
        return new ResultDTO(200, "导出", pvo);
    }

    /**
     * 创建项目id接口
     *
     * @return ResultDTO
     */
    @GetMapping("/createProjectId")
    @ApiOperation(value = "createProjectId", notes = "创建项目ID")
    public ResultDTO createProjectId () {
        //获取数据库项目ID列表
        List<String> projectIds = projectService.listProjectIds();
        //返回的项目ID
        String projectid = null;
        //当前年份
        String year = LocalDate.now().getYear() + "";
        //当前月份
        String month = LocalDate.now().getMonthValue() + "";
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if (projectIds == null || projectIds.size() == 0) {
            //返回的项目ID
            projectid = "ZJZY" + year + month + "01";
        } else {
            //最新的项目ID
            String uptodateProjectId = projectIds.get(0);
            String str = uptodateProjectId.substring(10);
            int number = Integer.parseInt(str);
            //个位数
            if (number < 9) {
                projectid = "ZJZY" + year + month + "0" + (number + 1);
            } else {
                projectid = "ZJZY" + year + month + (number + 1);
            }
        }
        return new ResultDTO(projectid);
    }

    /**
     * 项目确认完成接口
     *
     * @param projectid 项目id
     * @return ResultDTO
     */
    @PostMapping("/projectCmplete")
    @ApiOperation(value = "projectCmplete", notes = "进行中项目确认完成")
    public ResultDTO projectCmplete (String projectid){
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "缺少参数");
        }
        projectService.updateProjectStatus(projectid, 2);
        return new ResultDTO();
    }

    /**
     * 项目状态修改接口
     *
     * @param projectid 项目id
     * @param state 项目状态
     * @return ResultDTO
     */
    @PostMapping("/updateProjectStatus")
    @ApiOperation(value = "updateProjectStatus", notes = "项目状态修改接口")
    public ResultDTO updateProjectStatus (String projectid, Integer state){
        if (StringUtils.isEmpty(projectid) || state == null) {
            return new ResultDTO(201, "缺少参数");
        }
        projectService.updateProjectStatus(projectid, state);
        return new ResultDTO();
    }

    /**
     * 删除项目接口
     *
     * @param projectid 项目id
     * @return ResultDTO
     */
    @PostMapping("/delProject")
    @ApiOperation(value = "delProject", notes = "删除项目")
    public ResultDTO removeProject (String projectid){
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "项目id不能为空");
        }
        int result = projectService.removeProject(projectid);
        if (result == 0) {
            return new ResultDTO(201, "删除失败");
        }
        return new ResultDTO(200, "删除成功");
    }

    /**
     * 项目汇报记录接口
     *
     * @param projectid 项目id
     * @return ResultDTO
     */
    @PostMapping("/reportRecord")
    @ApiOperation(value = "reportRecord", notes = "项目汇报记录")
    public ResultDTO reportRecord (String projectid){
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "参数 projectid 不能为空");
        }
        List<ReportRecordDO> reportRecordDOS = projectService.reportRecord(projectid);
        ReportRecordVO reportRecordVO = new ReportRecordVO();
        reportRecordVO.setTimeList(reportRecordDOS);
        return new ResultDTO(reportRecordVO);
    }

    /**
     * 指定日期取得项目细化分解项列表接口
     *
     * @param projectid 项目id
     * @param reportTime 上报时间
     * @return ResultDTO
     */
    @PostMapping("/reportInfo")
    @ApiOperation(value = "reportInfo", notes = "指定日期取得项目细化分解项列表")
    public ResultDTO reportInfo (String projectid, String reportTime){
        if (StringUtils.isEmpty(projectid)) {
            return new ResultDTO(201, "参数 projectid 不能为空");
        }
        if (!StringUtils.isEmpty(reportTime) && CommonUtils.checkDate(reportTime)) {
            return new ResultDTO(201, "参数 reportTime 格式不对");
        }
        //返回前台数据
        ProjectSpeReportVO pvo = new ProjectSpeReportVO();
        if (StringUtils.isEmpty(reportTime)) {
            //取项目本月汇报记录
            ProjectReport reportInfo = projectReportService.selectCurrentMonth(projectid);
            pvo.setReportInfo(reportInfo);
            //项目细化表
            List<SpecifyRe> specifyList = projectSpecifyService.getSpecifyList(projectid);
            for (SpecifyRe specifyRe : specifyList) {
                if (specifyRe.getQuantity() == BigDecimal.ZERO) {
                    specifyRe.setQuantity(null);
                }
                specifyRe.setCalcCompleted(specifyRe.getTotalCompleted());
                specifyRe.setCalcCompletedValue(specifyRe.getTotalCompletedValue());
            }
            pvo.setSpecifyList(specifyList);
        } else {
            //指定月份的汇报记录
            ProjectReport reportInfo = projectReportService.selectAssignReport(projectid, reportTime);
            pvo.setReportInfo(reportInfo);
            //指定月份的完成工作量和产值
            List<SpecifyRe> specifyList = projectSpecifyService.getAssignSpecifyList(projectid, reportTime);
            pvo.setSpecifyList(specifyList);
        }
        return new ResultDTO(pvo);
    }

    /**
     * 计算工作产值和完成进度接口
     *
     * @param report 之前完成进度
     * @param sumNum 分项总量
     * @param num 完成工作量
     * @param price 分项单价
     * @return ResultDTO
     */
    @PostMapping("/workOutput")
    @ApiOperation(value = "workOutput", notes = "计算工作产值和完成进度")
    public ResultDTO workOutput (String report, String sumNum, String num, String price){
        if (StringUtils.isEmpty(report) || StringUtils.isEmpty(sumNum) || StringUtils.isEmpty(num) || StringUtils.isEmpty(price)) {
            return new ResultDTO(201, "参数缺失");
        }
        //完成工作量
        BigDecimal quantity = new BigDecimal(num);
        //分项单价
        BigDecimal unitPrice = new BigDecimal(price);
        //之前完成的进度
        BigDecimal completed = new BigDecimal(report);
        //新的完成进度
        completed = completed.add(quantity.divide(new BigDecimal(sumNum), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")));
        WorkOutputVO outputVO = new WorkOutputVO();
        outputVO.setCompletedValue(quantity.multiply(unitPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
        outputVO.setReport(completed);
        return new ResultDTO(outputVO);
    }

    /**
     * 临时退出保存细化分解的内容
     *
     * @param proSpecify 分解项
     * @return ResultDTO
     */
    @PostMapping("/savePreSpecify")
    @ApiOperation(value = "savePreSpecify", notes = "临时退出保存细化分解的内容")
    public ResultDTO savePreSpecify (@RequestBody ProSpecify proSpecify){
        //项目id
        String projectid = proSpecify.getProjectid();
        //判断项目是否已经细化分解完成
//        boolean flag = projectSpecifyService.isSpecify(projectid);
//        if (flag) {
//            return new ResultDTO();
//        }
        String specify = proSpecify.getSpecify();
        //Json Parse specify
        List<ProjectSpecify> specList = JSON.parseArray(specify, ProjectSpecify.class);
        projectSpecifyService.savePreSpecify(projectid, specList);
        return new ResultDTO();
    }
}