package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.refinement.data.SystemAreaDO;
import com.refinement.data.vo.SystemAreaVO;
import com.refinement.mapper.SysAreaMapper;
import com.refinement.service.SystemAreaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemAreaServiceImpl implements SystemAreaService {

    @Resource
    private SysAreaMapper sysAreaMapper;

    @Override
    public List<SystemAreaVO> areaList(Long areaType) {
        List<SystemAreaDO> areaDOS = sysAreaMapper.areaList(areaType);
        return areaDOS.stream().map(c -> {
            SystemAreaVO vo = new SystemAreaVO();
            BeanUtil.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
