package com.refinement.controller;

import com.refinement.data.param.RoleAddParam;
import com.refinement.data.param.RoleUpdateParam;
import com.refinement.data.vo.RoleDetailVO;
import com.refinement.entity.TreeSelect;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "角色API")
@Validated
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 角色列表
     *
     * @return Wrapper
     */
    @PostMapping("/list")
    @ApiOperation("角色列表")
    public Wrapper<?> list(@ApiParam("角色名称") @RequestParam(required = false) String name) {
        List<TreeSelect> treeSelects = roleService.buildDeptTreeSelect(name);
        return WrapMapper.ok(treeSelects);
    }

    /**
     * 角色详情
     * @param roleId 参数
     * @return Wrapper
     */
    @PostMapping("/detail")
    @ApiOperation("角色详情")
    public Wrapper<?> edit(@ApiParam("角色id") @RequestParam(required = false) Long roleId) {
        RoleDetailVO roleDetailVO = roleService.detail(roleId);
        return WrapMapper.ok(roleDetailVO);
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return Wrapper
     */
    @PostMapping("/delete")
    @ApiOperation("删除角色")
    public Wrapper<?> delete(@ApiParam("角色id") @RequestParam Long roleId) {
        roleService.delete(roleId);
        return WrapMapper.ok();
    }

    /**
     * 新增角色
     * @param roleParam 参数
     * @return Wrapper
     */
    @PostMapping("/add")
    @ApiOperation("新增角色")
    public Wrapper<?> add(@ApiParam("角色参数") @Validated @RequestBody RoleAddParam roleParam) {
        roleService.add(roleParam);
        return WrapMapper.ok();
    }

    /**
     * 修改角色
     * @param roleUpdateParam 参数
     * @return Wrapper
     */
    @PostMapping("/edit")
    @ApiOperation("修改角色")
    public Wrapper<?> edit(@ApiParam("角色参数") @Validated @RequestBody RoleUpdateParam roleUpdateParam) {
        roleService.edit(roleUpdateParam);
        return WrapMapper.ok();
    }

    /**
     * 移动角色
     * @param roleId 参数
     * @param targetId 参数
     * @return Wrapper
     */
    @PostMapping("/update")
    @ApiOperation("移动角色")
    public Wrapper<?> update(@ApiParam("组织id") @RequestParam Long roleId,@ApiParam("组织目标id") @RequestParam Long targetId) {
        roleService.update(roleId,targetId);
        return WrapMapper.ok();
    }

}
