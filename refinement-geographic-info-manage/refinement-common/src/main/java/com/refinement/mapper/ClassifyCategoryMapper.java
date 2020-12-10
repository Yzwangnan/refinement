package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.data.AllClassifyCategoryDO;
import com.refinement.data.ClassifyCategoryDO;
import com.refinement.entity.ClassifyCategory;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-05-20
 */
public interface ClassifyCategoryMapper extends BaseMapper<ClassifyCategory> {

    //查询项目分类列表
    List<ClassifyCategoryDO> classifyList();

    //取所有的项目分类
    List<AllClassifyCategoryDO> allCategory();

    List<ClassifyCategoryDO> categoryList(Long classifyId);

    ClassifyCategoryDO selectClassifyById(Long classifyId);

    ClassifyCategoryDO selectCategoryById(Long categoryId);
}
