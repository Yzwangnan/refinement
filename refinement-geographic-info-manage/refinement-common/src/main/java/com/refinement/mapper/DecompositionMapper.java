package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.Decomposition;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface DecompositionMapper extends BaseMapper<Decomposition> {

    void insertAndReturnId(Decomposition oneLevelDecomposition);
}
