package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.data.vo.DepartmentVO;
import com.refinement.entity.Department;
import com.refinement.mapper.DepartmentMapper;
import com.refinement.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentVO> getDepartmentList() {
        return departmentMapper.selectList(null).stream().map(c -> {
            DepartmentVO vo = new DepartmentVO();
            BeanUtil.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Department getByDepartId(String deptid) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("deptid", deptid);
        return this.getOne(qw, false);
    }
}
