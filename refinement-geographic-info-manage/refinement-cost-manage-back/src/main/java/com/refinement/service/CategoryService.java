package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.vo.CategoryListVO;
import com.refinement.entity.ProjectClassify;

import java.util.List;

public interface CategoryService extends IService<ProjectClassify> {

    /**
     * 项目分类列表
     * @return List
     */
    List<CategoryListVO> getCategoryList();
}
