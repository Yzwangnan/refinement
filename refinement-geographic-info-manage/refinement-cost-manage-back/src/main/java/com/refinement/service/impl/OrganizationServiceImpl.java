package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.config.MyThreadLocal;
import com.refinement.data.vo.DepartmentVO;
import com.refinement.data.vo.OrganizationVO;
import com.refinement.entity.Organization;
import com.refinement.entity.OrganizationEntity;
import com.refinement.entity.TreeSelect;
import com.refinement.entity.User;
import com.refinement.http.DefaultResponseCode;
import com.refinement.mapper.OrganizationMapper;
import com.refinement.mapper.UserMapper;
import com.refinement.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Override
    public List<TreeSelect> buildDeptTreeSelect(String name) {
        List<OrganizationEntity> organizationList = organizationMapper.getOrganizationList();
        if(name != null && !"".equals(name)) {
            if(organizationList != null && organizationList.size() > 0) {
                organizationList.forEach(c->{
                    if(c.getOrganizationName() != null && c.getOrganizationName().contains(name)) {
                        c.setIsMatch(1);
                    }
                });
            }
        }
        List<OrganizationEntity> organizationTrees = buildDeptTree(organizationList);
        return organizationTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param organizations 部门列表
     * @return 树结构列表
     */
    public List<OrganizationEntity> buildDeptTree(List<OrganizationEntity> organizations)
    {
        List<OrganizationEntity> returnList = new ArrayList<OrganizationEntity>();
        List<Long> tempList = new ArrayList<Long>();
        for (OrganizationEntity dept : organizations)
        {
            tempList.add(dept.getId());
        }
        for (Iterator<OrganizationEntity> iterator = organizations.iterator(); iterator.hasNext();)
        {
            OrganizationEntity organization = (OrganizationEntity) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(organization.getParentId()))
            {
                recursionFn(organizations, organization);
                returnList.add(organization);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = organizations;
        }
        return returnList;
    }

    /**
     * 递归列表
     * @param list
     * @param t
     */
    private void recursionFn(List<OrganizationEntity> list, OrganizationEntity t)
    {
        // 得到子节点列表
        List<OrganizationEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (OrganizationEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<OrganizationEntity> it = childList.iterator();
                while (it.hasNext())
                {
                    OrganizationEntity n = (OrganizationEntity) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     * @param list
     * @param t
     * @return
     */
    private List<OrganizationEntity> getChildList(List<OrganizationEntity> list, OrganizationEntity t)
    {
        List<OrganizationEntity> tlist = new ArrayList<OrganizationEntity>();
        Iterator<OrganizationEntity> it = list.iterator();
        while (it.hasNext())
        {
            OrganizationEntity n = (OrganizationEntity) it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     * @param list
     * @param t
     * @return
     */
    private boolean hasChild(List<OrganizationEntity> list, OrganizationEntity t)
    {
        return getChildList(list, t).size() > 0 ? true : false;
    }


    @Override
    public void add(Long id, String name,Integer deptFlag) {
        OrganizationEntity organize = organizationMapper.getOrganizationByName(name, id);
        if(organize != null) {
            throw new BusinessException(DefaultResponseCode.ORGAN_NAME_IS_EXIST);
        }
        OrganizationEntity organization = new OrganizationEntity();
        if(id != null && id != 0) {
            OrganizationEntity info = organizationMapper.getOrganizationById(id);
            if(info != null) {
                Integer level = info.getLevel();
                organization.setLevel(level+1);
            } else {
                throw new BusinessException(DefaultResponseCode.PARENT_ORGAN_NOT_EXIST);
            }
        } else {
            organization.setLevel(1);
        }
        organization.setDeptFlag(deptFlag);
        organization.setParentId(id);
        organization.setOrganizationName(name);
        organizationMapper.insertOrganization(organization);
    }

    @Override
    public void update(Long organizationId, Long targetId) {

        OrganizationEntity resource = organizationMapper.getOrganizationById(organizationId);
        if(resource == null) {
            throw new BusinessException(DefaultResponseCode.ORGAN_NOT_EXIST);
        } else {
            if(targetId != 0) {
                // 获取目标父部门
                OrganizationEntity target = organizationMapper.getOrganizationById(targetId);
                if(target == null) {
                    throw new BusinessException(DefaultResponseCode.ORGAN_NOT_EXIST);
                }
                resource.setLevel(target.getLevel() + 1);
                resource.setParentId(targetId);
                organizationMapper.updateOrganization(resource);
                //判断要移动的父部门是否是事业部,如果是，所有部门变更为事业部
                if(target.getDeptFlag() != 0) {
                    resource.setDeptFlag(1);
                    updateDeptFlag(resource);
                }
            } else {
                resource.setLevel(1);
                resource.setParentId(targetId);
                organizationMapper.updateOrganization(resource);
            }
        }

    }

    @Override
    public void edit(Long organizationId, String name,Integer deptFlag) {
        OrganizationEntity resource = organizationMapper.getOrganizationById(organizationId);
        if(resource == null) {
            throw new BusinessException(DefaultResponseCode.ORGAN_NOT_EXIST);
        } else {
            if(name != null && !"".equals(name)) {
                OrganizationEntity organize = organizationMapper.getOrganizationByName(name, resource.getParentId());
                if(organize != null && organize.getId().longValue() != organizationId.longValue()) {
                    throw new BusinessException(DefaultResponseCode.ORGAN_NAME_IS_EXIST);
                }
                resource.setOrganizationName(name);
                organizationMapper.editOrganization(resource);
            }
            if(deptFlag != null) {
                resource.setDeptFlag(deptFlag);
                updateAll(resource);
            }
            /*int i = organizationMapper.editOrganization(resource);
            if(i == 0) {
                throw new BusinessException(DefaultResponseCode.SAVE_FAIL);
            }*/

        }
    }

    @Override
    public void delete(Long organizationId) {
        //获取当前登录用户id
        Long currentUserId = MyThreadLocal.getUserId();
        User user = userMapper.selectById(currentUserId);
        if (user != null) {
            String deptid = user.getDeptid();
            if(deptid != null && !"".equals(deptid)) {
                check(Long.parseLong(deptid),organizationId);
            }
        }
        OrganizationEntity organization = organizationMapper.getOrganizationById(organizationId);
        if(organization != null) {
            deleteAll(organization);
//            organizationMapper.deleteById(organization);
        } else {
            throw new BusinessException(DefaultResponseCode.DEL_ORGAN_NOT_EXIST);
        }

    }

    @Override
    public List<OrganizationVO> getOrganizationList() {
        return organizationMapper.selectList(null).stream().map(c -> {
            OrganizationVO vo = new OrganizationVO();
            BeanUtil.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentVO> getDepartmentList() {
        return organizationMapper.selectList(new QueryWrapper<Organization>()
                .eq("dept_flag", 1)).stream().map(c -> {
            DepartmentVO vo = new DepartmentVO();
            vo.setDeptid(c.getId().toString());
            vo.setDeptname(c.getOrganizationName());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Organization> getOrganizationListLikeName(String name) {
        QueryWrapper<Organization> query = new QueryWrapper<>();
        query.like("organization_name",name);
        if(name == null || "".equals(name)) {
            return organizationMapper.selectList(null);
        }
        return organizationMapper.selectList(query);



    }

    /**
     * 验证要删除的是否是自己组织
     * @param organizationId
     * @param deptId
     */
    public void check(Long organizationId,Long deptId){

        OrganizationEntity organization = organizationMapper.getOrganizationById(organizationId);
        if(organization != null) {
            while (true){
                if(organization.getId().longValue() == deptId.longValue()) {
                    throw new BusinessException(DefaultResponseCode.CANT_DELETE_MYSELF_ORGAN);
                }
                Long parentId = organization.getParentId();
                if(parentId != null && parentId.longValue() != 0) {
                    check(parentId,deptId);
                }
                break;
            }
        }
    }

    public void deleteAll(OrganizationEntity organization){

        if(organization != null) {
            organizationMapper.deleteById(organization);
            while (true){
                List<OrganizationEntity> organizationList = organizationMapper.getOrganizationListByParentId(organization.getId());
                if(organizationList != null && organizationList.size() > 0) {
                    organizationList.forEach(c->{
                        deleteAll(c);
                    });
                }
                break;
            }
        }
    }

    public void updateAll(OrganizationEntity organization){

        if(organization != null) {
            organizationMapper.editOrganization(organization);
            while (true){
                List<OrganizationEntity> organizationList = organizationMapper.getOrganizationListByParentId(organization.getId());
                if(organizationList != null && organizationList.size() > 0) {
                    organizationList.forEach(c->{
                        c.setDeptFlag(organization.getDeptFlag());
                        updateAll(c);
                    });
                }
                break;
            }
        }
    }

    public void updateDeptFlag(OrganizationEntity organization){

        if(organization != null) {
            organizationMapper.editOrganizationDeptFlag(organization);
            while (true){
                List<OrganizationEntity> organizationList = organizationMapper.getOrganizationListByParentId(organization.getId());
                if(organizationList != null && organizationList.size() > 0) {
                    organizationList.forEach(c->{
                        c.setDeptFlag(organization.getDeptFlag());
                        updateDeptFlag(c);
                    });
                }
                break;
            }
        }
    }
}
