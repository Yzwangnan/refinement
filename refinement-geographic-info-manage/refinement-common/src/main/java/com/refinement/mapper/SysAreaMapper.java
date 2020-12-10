package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.data.SystemAreaDO;
import com.refinement.entity.SysArea;

import java.util.List;

/**
 * <p>
 * 行政区 Mapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-24
 */
public interface SysAreaMapper extends BaseMapper<SysArea> {

    // 取区域列表
    List<SystemAreaDO> areaList(Long areaType);

}
