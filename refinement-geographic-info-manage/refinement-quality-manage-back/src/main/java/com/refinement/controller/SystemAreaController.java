package com.refinement.controller;

import com.refinement.data.AllClassifyCategoryDO;
import com.refinement.data.ClassifyCategoryDO;
import com.refinement.http.ResultDTO;
import com.refinement.service.ClassifyCategoryService;
import com.refinement.service.SystemAreaService;
import com.refinement.vo.AllClassifyCategoryVO;
import com.refinement.vo.OneLevelClassifyVO;
import com.refinement.vo.SecondLevelClassifyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "区域列表")
public class SystemAreaController {

    @Resource
    private SystemAreaService systemAreaService;

    @Resource
    private ClassifyCategoryService classifyCategoryService;

    /**
     * 获取区域列表接口
     *
     * @param area_type 区域类型
     * @return ResultDTO
     */
    @PostMapping("/areaList")
    @ApiOperation(value = "areaList", notes = "取区域列表")
    public ResultDTO areaList(@RequestParam(required = false, defaultValue = "1") Long area_type) {
        if (area_type == null) {
            return new ResultDTO(201, "缺少参数");
        }
        return systemAreaService.areaList(area_type);
    }

    /**
     * 获取项目一级分类列表接口
     *
     * @return ResultDTO
     */
    @PostMapping("/classifyList")
    @ApiOperation(value = "classifyList", notes = "取项目一级分类列表")
    public ResultDTO classifyList() {
        List<ClassifyCategoryDO> categoryDOS = classifyCategoryService.classifyList();
        OneLevelClassifyVO oneLevelClassifyVO = new OneLevelClassifyVO();
        oneLevelClassifyVO.setClassifyList(categoryDOS);
        return new ResultDTO(oneLevelClassifyVO);
    }

    /**
     * 根据一级项目分类id获取二级项目分类列表接口
     *
     * @param classifyId 一级项目分类id
     * @return ResultDTO
     */
    @PostMapping("/categoryList")
    @ApiOperation(value = "categoryList", notes = "根据一级取二级分类列表")
    public ResultDTO categoryList(Long classifyId) {
        if (ObjectUtils.isEmpty(classifyId)) {
            return new ResultDTO(201, "参数 classifyId 为空");
        }
        List<ClassifyCategoryDO> categoryDOS = classifyCategoryService.categoryList(classifyId);
        SecondLevelClassifyVO secondLevelClassifyVO = new SecondLevelClassifyVO();
        secondLevelClassifyVO.setCategoryList(categoryDOS);
        return new ResultDTO(secondLevelClassifyVO);
    }

    /**
     * 获取所有项目分类接口
     *
     * @return ResultDTO
     */
    @PostMapping("/allCategory")
    @ApiOperation(value = "allCategory", notes = "取所有的项目分类")
    public ResultDTO allCategory() {
        List<AllClassifyCategoryDO> classifyList = classifyCategoryService.allCategory();
        AllClassifyCategoryVO allClassifyCategoryVO = new AllClassifyCategoryVO();
        allClassifyCategoryVO.setClassifyList(classifyList);
        return new ResultDTO(allClassifyCategoryVO);
    }
}
