package com.refinement.controller;

import com.refinement.data.param.ModelAddParam;
import com.refinement.data.vo.ModelPageVO;
import com.refinement.data.vo.ModelProjectDetailVO;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.ModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Api(tags = "模板API")
@Validated
@RestController
@RequestMapping("/model")
public class ModelController {

    @Resource
    private ModelService modelService;

    /**
     * 模板列表
     * @param type 项目类型 1-进行中项目 2-历史项目
     * @param page 页
     * @param size 大小
     * @return Wrapper
     */
    @GetMapping("/list")
    @ApiOperation("模板列表")
    public Wrapper<ModelPageVO> list(
            @ApiParam("项目类型 1-进行中项目 2-历史项目") @RequestParam(required = false) Integer type,
            @ApiParam("页") @RequestParam @NotNull(message = "page 不能为空") Integer page,
            @ApiParam("大小") @RequestParam(required = false, defaultValue = "10") Integer size) {
        return WrapMapper.ok(modelService.getModelList(type, page, size));
    }

    /**
     * 模板详情
     * @param id 模板id
     * @return Wrapper
     */
    @PostMapping("/detail")
    @ApiOperation("模板详情")
    public Wrapper<ModelProjectDetailVO> detail(
            @ApiParam("模板id") @RequestParam @NotNull(message = "id 不能为空") Long id,
            @ApiParam("项目id") @RequestParam(required = false) String projectid) {
        return WrapMapper.ok(modelService.detail(id, projectid));
    }

    /**
     * 新增模板
     * @param param 模板参数
     * @return Wrapper
     */
    @PostMapping("/add")
    @ApiOperation("新增模板")
    public Wrapper<?> add(@Validated @RequestBody ModelAddParam param) {
        modelService.add(param);
        return WrapMapper.ok();
    }

    /**
     * 删除模板
     * @param modelId 模板id
     * @return Wrapper
     */
    @PostMapping("/delete")
    @ApiOperation("删除模板")
    public Wrapper<?> delete(@ApiParam("模板id") @RequestParam @NotNull(message = "模板id不能为空") Long modelId) {
        modelService.delete(modelId);
        return WrapMapper.ok();
    }
}
