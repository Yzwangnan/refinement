package com.refinement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.data.SystemAreaDO;
import com.refinement.entity.SysArea;
import com.refinement.http.ResultDTO;
import com.refinement.mapper.SysAreaMapper;
import com.refinement.service.SystemAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Service
public class SystemAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements SystemAreaService {

    @Resource
    private SysAreaMapper sysAreaMapper;

    @Override
    public ResultDTO areaList(Long area_type) {
        // 参数校验 area_type
        if (area_type == null) {
            return new ResultDTO(201, "缺少参数");
        }
        List<SystemAreaDO> areaDOS = sysAreaMapper.areaList(area_type);
        if (areaDOS.size() == 0) {
            return new ResultDTO(201, "无数据", areaDOS);
        }
        return new ResultDTO(areaDOS);
    }
}
