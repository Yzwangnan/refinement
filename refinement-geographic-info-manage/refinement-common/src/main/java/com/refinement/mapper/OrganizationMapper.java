package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.Organization;
import com.refinement.entity.OrganizationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface OrganizationMapper extends BaseMapper<Organization> {

    List<OrganizationEntity> getOrganizationList();

    OrganizationEntity getOrganizationById(Long id);

    int insertOrganization(OrganizationEntity organization);

    int updateOrganization(OrganizationEntity organization);

    int editOrganization(OrganizationEntity organization);

    int editOrganizationDeptFlag(OrganizationEntity organization);

    OrganizationEntity getOrganizationByName(String name,Long id);

    List<OrganizationEntity> getOrganizationListByParentId(@Param("parentId") Long parentId);

}
