package com.refinement.controller;

import com.refinement.data.vo.*;
import com.refinement.entity.Organization;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "公共API")
@Validated
@RestController
@RequestMapping("/common")
public class CommonController {

    @Resource
    private RoleService roleService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    private ProjectService projectService;

    @Resource
    private ModelService modelService;

    @Resource
    private SystemAreaService systemAreaService;

    /**
     * 获取记录员/审核员列表
     * @param type 类型 1->记录员 2->审核员
     * @return Wrapper
     */
    @GetMapping("/getRoleList")
    @ApiOperation("获取记录员/审核员列表")
    public Wrapper<List<RoleVO>> getRoleList(
            @ApiParam("类型 1->记录员 2->审核员") @RequestParam @NotNull(message = "type 不能为空") Integer type) {
        return WrapMapper.ok(roleService.getRoleList(type));
    }

    /**
     * 项目分类列表
     * @return Wrapper
     */
    @GetMapping("/getCategoryList")
    @ApiOperation("项目分类列表")
    public Wrapper<List<CategoryListVO>> getCategoryList() {
        return WrapMapper.ok(categoryService.getCategoryList());
    }

    /**
     * 部门列表
     * @return Wrapper
     */
    @GetMapping("/getDepartmentList")
    @ApiOperation("部门列表")
    public Wrapper<List<DepartmentVO>> getDepartmentList() {
        return WrapMapper.ok(organizationService.getDepartmentList());
    }

    /**
     * 组织列表
     * @return Wrapper
     */
    @GetMapping("/getOrganizationList")
    @ApiOperation("组织列表")
    public Wrapper<List<OrganizationVO>> getOrganizationList() {
        return WrapMapper.ok(organizationService.getOrganizationList());
    }

    /**
     * 创建项目id
     * @return Wrapper
     */
    @GetMapping("/createProjectId")
    @ApiOperation("创建项目id")
    public Wrapper<?> createProjectId() {
        return WrapMapper.ok(projectService.createProjectId());
    }

    /**
     * 所有模板列表
     * @param type 项目类型 1-进行中项目 2-历史项目
     * @return Wrapper
     */
    @GetMapping("/getModelList")
    @ApiOperation("所有模板列表")
    public Wrapper<List<ModelVO>> getModelList(@ApiParam("项目类型 1-进行中项目 2-历史项目") @RequestParam(required = false) Integer type) {
        return WrapMapper.ok(modelService.getModelList(type));
    }

    /**
     * 组织列表
     * @param name 组织名称
     * @return
     */
    @PostMapping("/getOrganizationListLikeName")
    @ApiOperation("组织列表")
    public Wrapper<?> getOrganizationListLikeName(@ApiParam("组织名称") @RequestParam(required = false) String name) {
        List<Organization> list = organizationService.getOrganizationListLikeName(name);
        return WrapMapper.ok(list);
    }

    /**
     * 获取区域列表
     * @param areaType 区域类型
     * @return Wrapper
     */
    @PostMapping("/areaList")
    @ApiOperation("获取区域列表")
    public Wrapper<List<SystemAreaVO>> areaList(@ApiParam("区域类型 1-一级") @RequestParam(required = false, defaultValue = "1") Long areaType) {
        return WrapMapper.ok(systemAreaService.areaList(areaType));
    }

    /**
     * 校验有无导出权限
     *
     * @param type 校验类型 0-新项目导出 1-进行中导出 2-进行中细化导出 3-历史项目导出
     * @return Wrapper
     */
    @GetMapping("/judgeExportRole")
    @ApiOperation("校验有无导出权限")
    public Wrapper<?> judgeExportRole(@ApiParam("校验类型 0-新项目导出 1-进行中导出 2-进行中细化导出 3-历史项目导出") @NotNull(message = "type 不能为空")
                                      @RequestParam Integer type, @ApiParam("用户 id") Long userId) {
        return WrapMapper.wrap(roleService.judgeExportRole(type, userId) ? DefaultResponseCode.SUCCESS :
                DefaultResponseCode.INSUFFICIENT_AUTHORITY);
    }

    /**
     * 项目细分按模板比例细分
     *
     * @param budgetAmount 预算金额
     * @param id 计算项id
     * @return Wrapper
     */
    @GetMapping("/computeDecomposition")
    @ApiOperation("项目细分按模板比例细分")
    public Wrapper<?> computeDecomposition(
            @ApiParam("预算金额") @NotNull(message = "budgetAmount 不能为空") @RequestParam BigDecimal budgetAmount,
            @ApiParam("计算项id") @NotNull(message = "id 不能为空") @RequestParam Long id) {
        return WrapMapper.ok(modelService.computeDecomposition(budgetAmount, id));
    }
}
