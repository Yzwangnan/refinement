package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.vo.DepartmentVO;
import com.refinement.data.vo.OrganizationVO;
import com.refinement.entity.Organization;
import com.refinement.entity.TreeSelect;

import java.util.List;

public interface OrganizationService extends IService<Organization> {

    /**
     * 构建前端所需要下拉树结构
     *
     * @param
     * @return 下拉树结构组织列表
     */
    List<TreeSelect> buildDeptTreeSelect(String name);

    /**
     * 新增组织
     * @param id
     * @param name
     */
    void add(Long id,String name,Integer deptFlag);

    /**
     * 修改组织
     * @param organizationId
     * @param targetId
     */
    void update(Long organizationId, Long targetId);

    void edit(Long organizationId, String name,Integer deptFlag);

    /**
     * 删除组织
     * @param organizationId 组织id
     */
    void delete(Long organizationId);

    /**
     * 组织列表
     * @return List
     */
    List<OrganizationVO> getOrganizationList();

    /**
     * 部门列表
     * @return List
     */
    List<DepartmentVO> getDepartmentList();

    List<Organization> getOrganizationListLikeName(String name);
}
