package com.refinement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.data.DepartmentDO;
import com.refinement.entity.Department;
import com.refinement.entity.Organization;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.PageResult;
import com.refinement.http.ResultDTO;
import com.refinement.mapper.DepartmentMapper;
import com.refinement.mapper.OrganizationMapper;
import com.refinement.mapper.UserMapper;
import com.refinement.service.DepartmentService;
import com.refinement.vo.DepartmentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-22
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public ResultDTO listDepts(Integer page, Integer size) {
        // 数据封装
        DepartmentVO dvo = new DepartmentVO();
        // 封装需求的数据
        List<DepartmentDO> departmentDOList = new ArrayList<>();
        // 查询生产运营部
        Organization organization = organizationMapper.selectOne(new QueryWrapper<Organization>()
                .eq("organization_name", "生产运营部")
                .last("LIMIT 1"));
        // 总记录条数
        long total = 0L;
        if (organization != null) {
            // 分页数据
            IPage<Organization> iPage = new Page<>(page, size);
            organizationMapper.selectPage(iPage, new QueryWrapper<Organization>()
                    .eq("parent_id", organization.getId()));
            iPage.getRecords().forEach(c -> {
                DepartmentDO departmentDO = new DepartmentDO();
                departmentDO.setDeptid(c.getId().toString());
                departmentDO.setDeptname(c.getOrganizationName());
                departmentDOList.add(departmentDO);
            });
            total = iPage.getTotal();
        }
        // 分页信息
        PageResult pageInfo = new PageResult();
        pageInfo.setCurrent(page);
        pageInfo.setSize(departmentDOList.size());
        pageInfo.setTotal(total);
        dvo.setDeptList(departmentDOList);
        dvo.setPageInfo(pageInfo);
        return new ResultDTO(dvo);
    }

    @Override
    public ResultDTO addDept(String deptname, String password) {
        // 查询生产运营部
        Organization organization = organizationMapper.selectOne(new QueryWrapper<Organization>()
                .eq("organization_name", "生产运营部")
                .last("LIMIT 1"));
        if (organization == null) {
            throw new BusinessException(DefaultResponseCode.ORGANIZATION_MISS);
        }
        // 根据添加的名称查询
        Organization selectOne = organizationMapper.selectOne(new QueryWrapper<Organization>()
                .eq("parent_id", organization.getId())
                .eq("organization_name", deptname)
                .last("LIMIT 1"));
        if (selectOne != null) {
            throw new BusinessException(DefaultResponseCode.ORGANIZATION_NAME_NOT_SAME);
        }
        Organization subOrganization = new Organization();
        subOrganization.setLevel(organization.getLevel() + 1);
        subOrganization.setOrganizationName(deptname);
        subOrganization.setParentId(organization.getId());
        organizationMapper.insert(subOrganization);
        return new ResultDTO();
    }

    @Override
    public Long selectTotal() {
        Integer total = departmentMapper.selectCount(null);
        if (total == null) {
            return null;
        } else {
            return Long.parseLong(total.toString());
        }
    }

    @Override
    public List<DepartmentDO> selectAllDeptList() {
        return organizationMapper.selectList(new QueryWrapper<Organization>()
                .eq("dept_flag", 1)).stream().map(c -> {
            DepartmentDO departmentDO = new DepartmentDO();
            departmentDO.setDeptid(c.getId().toString());
            departmentDO.setDeptname(c.getOrganizationName());
            return departmentDO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> listDeptIds() {
        return departmentMapper.listDeptIds();
    }
}
