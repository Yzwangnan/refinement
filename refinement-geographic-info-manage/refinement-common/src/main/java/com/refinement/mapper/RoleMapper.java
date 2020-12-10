package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.Role;
import com.refinement.entity.RoleEntity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<RoleEntity> getRoleList();

    RoleEntity getRoleById(Long id);
}
