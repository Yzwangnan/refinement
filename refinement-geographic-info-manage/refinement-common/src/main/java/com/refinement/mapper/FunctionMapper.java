package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.Function;
import com.refinement.entity.FunctionEntity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface FunctionMapper extends BaseMapper<Function> {

    List<FunctionEntity> getFunctionList();

}
