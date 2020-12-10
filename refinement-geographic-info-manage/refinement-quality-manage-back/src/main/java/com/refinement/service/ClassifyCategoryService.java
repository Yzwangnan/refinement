package com.refinement.service;


import com.refinement.data.AllClassifyCategoryDO;
import com.refinement.data.ClassifyCategoryDO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-05-20
 */
public interface ClassifyCategoryService {

    //查询类型列表
    List<ClassifyCategoryDO> classifyList();

    //取所有的项目分类
    List<AllClassifyCategoryDO> allCategory();

    //根据一级项目分类id获取二级项目分类列表
    List<ClassifyCategoryDO> categoryList(Long classifyId);

    //获取指定一级项目分类
    ClassifyCategoryDO selectClassifyById(Long classifyId);

    //获取指定二级项目分类
    ClassifyCategoryDO selectCategoryById(Long categoryId);

}

