package com.refinement.controller;

import com.refinement.data.param.ProjectAddParam;
import com.refinement.data.param.ProjectDecompositionParam;
import com.refinement.data.param.ProjectMonthlyReportParam;
import com.refinement.data.param.ProjectVerifyParam;
import com.refinement.data.vo.*;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "项目API")
@Validated
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    /**
     * 项目列表
     * @param type 项目类型 0-新项目 1-进行中项目 2-历史项目
     * @param name 项目名称
     * @param categoryId 二级分类id
     * @param startTime 开始时间
     * @param modelId 模板id
     * @param page 页
     * @param size 大小
     * @return Wrapper
     */
    @PostMapping("/list")
    @ApiOperation("项目列表")
    public Wrapper<ProjectListPageVO> list(
            @ApiParam("项目类型 0-新项目 1-进行中项目 2-历史项目") @RequestParam @NotNull(message = "type 不能为空") Integer type,
            @ApiParam("项目名称") @RequestParam(required = false) String name,
            @ApiParam("二级分类id") @RequestParam(required = false) Long categoryId,
            @ApiParam("开始时间") @RequestParam(required = false) String startTime,
            @ApiParam("模板id") @RequestParam(required = false) Long modelId,
            @ApiParam("页") @RequestParam @NotNull(message = "page 不能为空") Integer page,
            @ApiParam("大小") @RequestParam(required = false, defaultValue = "10") Integer size) {
        return WrapMapper.ok(projectService.list(type, name, categoryId, startTime, modelId, page, size));
    }

    /**
     * 新建项目
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/add")
    @ApiOperation("新建项目")
    public Wrapper<?> add(@Validated @RequestBody ProjectAddParam param) {
        projectService.add(param);
        return WrapMapper.ok();
    }

    /**
     * 细化分解
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/decomposition")
    @ApiOperation("细化分解")
    public Wrapper<?> decomposition(@Validated @RequestBody ProjectDecompositionParam param) {
        projectService.decomposition(param);
        return WrapMapper.ok();
    }

    /**
     * 新建确认
     * @param projectid 项目id
     * @return Wrapper
     */
    @PostMapping("/confirm")
    @ApiOperation("新建确认")
    public Wrapper<?> confirm(@ApiParam("项目id") @RequestParam @NotNull(message = "projectid 不能为空") String projectid) {
        projectService.confirm(projectid);
        return WrapMapper.ok();
    }

    /**
     * 月报标题列表
     * @param projectid 项目id
     * @return Wrapper
     */
    @PostMapping("/getMonthlyReportTitleList")
    @ApiOperation("月报标题列表")
    public Wrapper<MonthlyReportVO> getMonthlyReportTitleList(
            @ApiParam("项目id") @RequestParam @NotNull(message = "projectid 不能为空") String projectid) {
        return WrapMapper.ok(projectService.getMonthlyReportTitleList(projectid));
    }

    /**
     * 月报评审
     * @param param 审评数据
     * @return Wrapper
     */
    @PostMapping("/verify")
    @ApiOperation("月报评审")
    public Wrapper<?> verify(@Validated @RequestBody ProjectVerifyParam param) {
        projectService.verify(param);
        return WrapMapper.ok();
    }

    /**
     * 成本细化分解列表
     * @param projectid 项目id
     * @param type 项目类型 1-进行中项目 2-历史项目
     * @param month 月份
     * @return Wrapper
     */
    @PostMapping("/specifyList")
    @ApiOperation("成本细化分解列表")
    public Wrapper<ProjectSpecifyVO> specifyList(
            @ApiParam("项目id") @RequestParam @NotBlank(message = "projectid 不能为空") String projectid,
            @ApiParam("项目类型 1-进行中项目 2-历史项目") @RequestParam @NotNull(message = "type 不能为空") Integer type,
            @ApiParam("月份") @RequestParam(required = false) Integer month) {
        return WrapMapper.ok(projectService.specifyList(projectid, type, month));
    }

    /**
     * 新增月报获取模板
     * @param projectid 项目id
     * @return Wrapper
     */
    @PostMapping("/getReportModel")
    @ApiOperation("新增月报获取模板")
    public Wrapper<List<OneLevelSpecifyVO>> getReportModel(
            @ApiParam("项目id") @RequestParam @NotBlank(message = "projectid 不能为空") String projectid) {
        return WrapMapper.ok(projectService.getReportModel(projectid));
    }


    /**
     * 月报新增
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/addMonthlyReport")
    @ApiOperation("月报新增")
    public Wrapper<?> addMonthlyReport(@Validated @RequestBody ProjectMonthlyReportParam param) {
        projectService.addMonthlyReport(param);
        return WrapMapper.ok();
    }

    /**
     * 月报修改
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/updateMonthlyReport")
    @ApiOperation("月报修改")
    public Wrapper<?> updateMonthlyReport(@Validated @RequestBody ProjectMonthlyReportParam param) {
        projectService.updateMonthlyReport(param);
        return WrapMapper.ok();
    }

    /**
     * 项目完成
     * @param projectid 项目id
     * @return Wrapper
     */
    @PostMapping("/complete")
    @ApiOperation("项目完成")
    public Wrapper<?> complete(@ApiParam("项目id")  @NotBlank(message = "projectid 不能为空") String projectid) {
        projectService.complete(projectid);
        return WrapMapper.ok();
    }

    /**
     * 项目详情
     * @param projectid 项目id
     * @return Wrapper
     */
    @PostMapping("/detail")
    @ApiOperation("项目详情")
    public Wrapper<ProjectDetailVO> detail(@ApiParam("项目id") @RequestParam @NotBlank(message = "projectid 不能为空") String projectid) {
        return WrapMapper.ok(projectService.detail(projectid));
    }
}
