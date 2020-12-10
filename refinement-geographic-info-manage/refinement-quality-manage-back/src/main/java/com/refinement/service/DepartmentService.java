package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.DepartmentDO;
import com.refinement.entity.Department;
import com.refinement.http.ResultDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-22
 */
public interface DepartmentService extends IService<Department> {

    // 查询事业部列表
    ResultDTO listDepts(Integer page, Integer size);

    // 新建事业部
    ResultDTO addDept(String deptname, String password);

    // 总记录条数
    Long selectTotal();

    // 所有项目部名称
    List<DepartmentDO> selectAllDeptList();

    List<String> listDeptIds();

}
