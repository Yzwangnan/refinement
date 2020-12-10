package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.param.RoleAddParam;
import com.refinement.data.param.RoleUpdateParam;
import com.refinement.data.vo.RoleDetailVO;
import com.refinement.data.vo.RoleVO;
import com.refinement.entity.Role;
import com.refinement.entity.TreeSelect;

import java.util.List;

public interface RoleService extends IService<Role> {

    /**
     * 获取记录员/审核员列表
     * @param type 类型 1->记录员 2->审核员
     * @return List
     */
    List<RoleVO> getRoleList(Integer type);

    List<TreeSelect> buildDeptTreeSelect(String name);

    RoleDetailVO detail(Long roleId);

    void delete(Long roleId);

    void add(RoleAddParam roleParam);

    void edit(RoleUpdateParam roleUpdateParam);

    void update(Long roleId, Long targetId);

    boolean judgeExportRole(Integer type, Long userId);
}
