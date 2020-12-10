package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.Department;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-22
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<String> listDeptIds();

}
