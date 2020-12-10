package com.refinement.service;

import com.refinement.data.vo.SystemAreaVO;

import java.util.List;

public interface SystemAreaService {

    /**
     * 获取区域列表接口
     * @param areaType 区域类型
     * @return List
     */
    List<SystemAreaVO> areaList(Long areaType);
}
