package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.vo.DepartmentVO;
import com.refinement.entity.Department;

import java.util.List;

public interface DepartmentService extends IService<Department> {

    /**
     * 部门列表
     * @return List
     */
    List<DepartmentVO> getDepartmentList();

    /**
     * 查询部门
     * @param deptid
     * @return
     */
    Department getByDepartId(String deptid);
}
