package com.refinement;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.refinement.config.BusinessException;
import com.refinement.entity.*;
import com.refinement.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 运行ExecuteSQLMain run方法后执行对数据表数据的更改
 * @author wn
 * @date 2020/11/06
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AfterExecuteSQLMain {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectCompleteMapper projectCompleteMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    /**
     * 角色名称 role_name
     */
    private static final String ROLE_ROOT = "root";
    private static final String ROLE_SUPERVISOR = "生产主管";
    private static final String ROLE_DEPARTMENT = "事业部";
    private static final String ROLE_PROJECT = "项目部";

    /**
     * 组织名称 organization_name
     */
    private static final String ORGANIZATION_SUPERVISOR = "生产运营部";

    /**
     * 注意！！！
     * 执行方法，由于  @Transactional 注解在 JUNIT 单元测试中的 rollback 值默认为 true，
     * 所以无论有无异常都会回滚，那么可以先执行一遍 run 方法，看有无异常抛出，如果没有异常顺利执行完了可以将 @Transactional 注解去掉
     * 再执行一次 run 方法
     */
    @Test
    @Transactional
    public void run() {
        //======================== department表操作 start ========================
        // 事业部列表
        List<Department> departmentList = departmentMapper.selectList(null);
        // 查询生产运营部
        Organization supervisor = organizationMapper.selectOne(new QueryWrapper<Organization>()
                .eq("organization_name", ORGANIZATION_SUPERVISOR)
                .last("LIMIT 1"));
        // 将所有事业部数据同步到部门表中生产运营部下
        departmentList.forEach(department -> {
            // 查询在组织表中该名称事业部数据是否存在
            Organization selectOne = organizationMapper.selectOne(new QueryWrapper<Organization>()
                    .eq("organization_name", department.getDeptname()));
            if (selectOne == null) {
                if (supervisor == null) {
                    throw new BusinessException(ORGANIZATION_SUPERVISOR + " 部门数据不存在");
                }
                Organization organization = new Organization();
                organization.setParentId(supervisor.getId());
                organization.setDeptFlag(1);
                organization.setLevel(supervisor.getLevel() + 1);
                organization.setOrganizationName(department.getDeptname());
                organizationMapper.insert(organization);
            }
        });
        //======================== department表操作 end ========================

        //======================== user表操作 start ========================
        // 用户列表
        List<User> userList = userMapper.selectList(null);
        userList.forEach(user -> {
            // 用户类型
            Integer type = user.getType();
            // 用户角色
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            if (type == 0) {
                // root
                Role role = roleMapper.selectOne(new QueryWrapper<Role>()
                        .eq("role_name", ROLE_ROOT)
                        .last("LIMIT 1"));
                if (role == null) {
                    throw new BusinessException(ROLE_ROOT + " 角色记录不存在");
                }
                userRole.setRoleId(role.getId());
            } else if (type == 1) {
                // 生产主管
                Role role = roleMapper.selectOne(new QueryWrapper<Role>()
                        .eq("role_name", ROLE_SUPERVISOR)
                        .last("LIMIT 1"));
                if (role == null) {
                    throw new BusinessException(ROLE_SUPERVISOR + " 角色记录不存在");
                }
                userRole.setRoleId(role.getId());
            } else if (type == 2) {
                // 事业部
                Role role = roleMapper.selectOne(new QueryWrapper<Role>()
                        .eq("role_name", ROLE_DEPARTMENT)
                        .last("LIMIT 1"));
                if (role == null) {
                    throw new BusinessException(ROLE_DEPARTMENT + " 角色记录不存在");
                }
                userRole.setRoleId(role.getId());
            } else if (type == 3) {
                // 项目部
                Role role = roleMapper.selectOne(new QueryWrapper<Role>()
                        .eq("role_name", ROLE_PROJECT)
                        .last("LIMIT 1"));
                if (role == null) {
                    throw new BusinessException(ROLE_PROJECT + " 角色记录不存在");
                }
                userRole.setRoleId(role.getId());
            }
            userRoleMapper.insert(userRole);
            // 事业部id
            String deptId = user.getDeptid();
            if (StrUtil.isNotEmpty(deptId)) {
                // 获取组织id
                Long id = getOrganizationId(deptId);
                if (id == null) {
                    throw new BusinessException("事业部id为 " + deptId + " 的事业部对应数据不存在");
                }
                // 修改事业部id
                user.setDeptid(id.toString());
                userMapper.updateById(user);
            }
        });
        //======================== user表操作 end ========================

        //======================== project表操作 start ========================
        // 项目列表
        List<Project> projectList = projectMapper.selectList(null);
        projectList.forEach(project -> {
            // 项目状态
            Integer state = project.getState();
           if (state == 1 || state == 3 || state == 4 || state == 5) {
                // 进行中项目 或 待验收 或 停工 或 已验收
                // 生成项目完成状态记录-进行中
               insertProjectComplete(project, 1);
           } else if (state == 2) {
                // 历史项目 已完成
               // 生成项目完成状态记录-已完成
               insertProjectComplete(project, 2);
           }
            // 事业部id
            String deptId = project.getDeptid();
            if (StrUtil.isNotEmpty(deptId)) {
                // 获取组织id
                Long id = getOrganizationId(deptId);
                if (id == null) {
                    throw new BusinessException("事业部id为 " + deptId + " 的事业部对应数据不存在");
                }
                // 修改事业部id
                project.setDeptid(id.toString());
                projectMapper.updateById(project);
            }
        });
        //======================== project表操作 end ========================
    }

    private void insertProjectComplete(Project project, int i) {
        ProjectComplete complete = new ProjectComplete();
        complete.setProjectId(project.getProjectid());
        // 形象进度系统类型
        complete.setSystemType(1);
        // 状态-进行中
        complete.setState(i);
        projectCompleteMapper.insert(complete);
    }

    private Long getOrganizationId(String deptId) {
        if (StrUtil.isNotEmpty(deptId)) {
            Department department = departmentMapper.selectOne(new QueryWrapper<Department>()
                    .eq("deptid", deptId)
                    .last("LIMIT 1"));
            if (department == null) {
                return null;
            }
            // 查询新增到组织中数据
            Organization organization = organizationMapper.selectOne(new QueryWrapper<Organization>()
                    .eq("organization_name", department.getDeptname())
                    .last("LIMIT 1"));
            if (organization == null) {
                return null;
            }
            return organization.getId();
        }
        return null;
    }
}
