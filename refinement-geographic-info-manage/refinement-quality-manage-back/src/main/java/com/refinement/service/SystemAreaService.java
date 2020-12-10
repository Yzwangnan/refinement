package com.refinement.service;


import com.refinement.http.ResultDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-22
 */
public interface SystemAreaService {

    // 获取区域列表
    ResultDTO areaList(Long area_type);

}
