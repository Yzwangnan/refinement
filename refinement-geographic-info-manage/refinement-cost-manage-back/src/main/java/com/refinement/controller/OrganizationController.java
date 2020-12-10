package com.refinement.controller;

import com.refinement.entity.TreeSelect;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "组织API")
@Validated
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Resource
    private OrganizationService organizationService;

    /**
     * 组织列表
     *
     * @return Wrapper
     */
    @PostMapping("/list")
    @ApiOperation("组织列表")
    public Wrapper<?> list(@ApiParam("组织名称") @RequestParam(required = false) String name) {
        List<TreeSelect> treeSelects = organizationService.buildDeptTreeSelect(name);
        return WrapMapper.ok(treeSelects);
    }

    /**
     * 新增组织
     * @param id 参数
     * @return Wrapper
     */
    @PostMapping("/add")
    @ApiOperation("新增组织")
    public Wrapper<?> add(@ApiParam("组织id") @RequestParam(required = false) Long id,
                          @ApiParam("组织名称") @RequestParam String name,
                          @ApiParam("是否是事业部") @RequestParam(required = false,defaultValue = "0") Integer deptFlag) {
        organizationService.add(id,name,deptFlag);
        return WrapMapper.ok();
    }

    /**
     * 移动组织
     * @param organizationId 参数
     * @param targetId 参数
     * @return Wrapper
     */
    @PostMapping("/update")
    @ApiOperation("修改组织")
    public Wrapper<?> update(@ApiParam("组织id") @RequestParam Long organizationId,@ApiParam("组织目标id") @RequestParam Long targetId) {
        organizationService.update(organizationId,targetId);
        return WrapMapper.ok();
    }

    /**
     * 修改组织
     * @param organizationId 参数
     * @param name 参数
     * @return Wrapper
     */
    @PostMapping("/edit")
    @ApiOperation("修改组织名称")
    public Wrapper<?> edit(@ApiParam("组织id") @RequestParam Long organizationId,
                           @ApiParam("组织名称") @RequestParam(required = false)  String name,
                           @ApiParam("是否是事业部") @RequestParam(required = false) Integer deptFlag) {
        organizationService.edit(organizationId,name,deptFlag);
        return WrapMapper.ok();
    }

    /**
     * 删除组织
     * @param organizationId 组织id
     * @return Wrapper
     */
    @PostMapping("/delete")
    @ApiOperation("删除组织")
    public Wrapper<?> delete(@ApiParam("组织id") @RequestParam @NotNull(message = "organizationId 不能为空") Long organizationId) {
        organizationService.delete(organizationId);
        return WrapMapper.ok();
    }
}
