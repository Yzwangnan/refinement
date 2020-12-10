package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.data.vo.CategoryListVO;
import com.refinement.data.vo.CategoryVO;
import com.refinement.entity.ProjectClassify;
import com.refinement.mapper.ClassifyCategoryMapper;
import com.refinement.mapper.ProjectClassifyMapper;
import com.refinement.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<ProjectClassifyMapper, ProjectClassify> implements CategoryService {

    @Resource
    private ProjectClassifyMapper projectClassifyMapper;

    @Resource
    private ClassifyCategoryMapper classifyCategoryMapper;

    @Override
    public List<CategoryListVO> getCategoryList() {
        List<CategoryListVO> voList = new ArrayList<>();
        projectClassifyMapper.selectList(null).forEach(c -> {
            CategoryListVO vo = new CategoryListVO();
            BeanUtil.copyProperties(c, vo);
            List<CategoryVO> categoryList = classifyCategoryMapper
                    .categoryList(c.getId())
                    .stream().map(o -> {
                        CategoryVO categoryVO = new CategoryVO();
                        BeanUtil.copyProperties(o, categoryVO);
                        return categoryVO;
                    }).collect(Collectors.toList());
            vo.setCategoryList(categoryList);
            voList.add(vo);
        });
        return voList;
    }
}
