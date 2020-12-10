package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.RoleFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleFunctionMapper extends BaseMapper<RoleFunction> {

    void saveBatch(@Param("functionList") List<RoleFunction> functionList);
}
