package com.refinement.service.impl;

import com.refinement.data.AllClassifyCategoryDO;
import com.refinement.data.ClassifyCategoryDO;
import com.refinement.mapper.ClassifyCategoryMapper;
import com.refinement.service.ClassifyCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-05-20
 */
@Service
public class ClassifyCategoryServiceImpl implements ClassifyCategoryService {

    @Resource
    private ClassifyCategoryMapper classifyCategoryMapper;

    @Override
    public List<ClassifyCategoryDO> classifyList() {
        return classifyCategoryMapper.classifyList();
    }

    @Override
    public List<AllClassifyCategoryDO> allCategory() {
        return classifyCategoryMapper.allCategory();
    }

    @Override
    public List<ClassifyCategoryDO> categoryList(Long classifyId) {
        return classifyCategoryMapper.categoryList(classifyId);
    }

    @Override
    public ClassifyCategoryDO selectClassifyById(Long classifyId) {
        return classifyCategoryMapper.selectClassifyById(classifyId);
    }

    @Override
    public ClassifyCategoryDO selectCategoryById(Long categoryId) {
        return classifyCategoryMapper.selectCategoryById(categoryId);
    }
}
