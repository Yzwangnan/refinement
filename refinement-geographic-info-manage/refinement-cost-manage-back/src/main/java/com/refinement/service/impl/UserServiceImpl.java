package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.config.MyThreadLocal;
import com.refinement.data.param.UserAddParam;
import com.refinement.data.param.UserUpdateParam;
import com.refinement.data.vo.RoleVO;
import com.refinement.data.vo.UserPageVO;
import com.refinement.data.vo.UserVO;
import com.refinement.entity.*;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.PageResult;
import com.refinement.mapper.*;
import com.refinement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public UserPageVO findPageList(Integer page, Integer size) {
        //返回对象VO
        UserPageVO pageVO = new UserPageVO();
        IPage<User> pageList = new Page<>(page, size);
        //查询条件
        QueryWrapper<User> query = new QueryWrapper<>();
//        query.eq("system_type", 2);
        userMapper.selectPage(pageList, query);
        //用户列表
        List<User> userList = pageList.getRecords();
        List<UserVO> voList = userList.stream().map(c -> {
            UserVO vo = new UserVO();
            BeanUtil.copyProperties(c, vo);
            vo.setUserId(c.getId());
            //部门信息
            Organization organization = organizationMapper.selectById(c.getDeptid());
            //组织id
            if (organization != null) {
                vo.setOrganizationId(organization.getId());
                vo.setOrganization(organization.getOrganizationName());
            }
            //查询角色;
            List<RoleVO> roleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                    .eq("user_id", c.getId())).stream().map(o -> {
                RoleVO roleVO = new RoleVO();
                Role role = roleMapper.selectById(o.getRoleId());
                if (role != null) {
                    BeanUtil.copyProperties(role, roleVO);
                    //表明是哪个项目的项目部角色
                    Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                            .eq("projectid", c.getProjectid()));
                    if (project != null) {
                        roleVO.setRoleName(project.getProjectname() + role.getRoleName());
                    }
                    return roleVO;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            vo.setRoleList(roleList);
            return vo;
        }).collect(Collectors.toList());
        pageVO.setUserList(voList);
        //分页对象
        PageResult pageInfo = new PageResult();
        pageInfo.setCurrent(page);
        pageInfo.setSize(userList.size());
        pageInfo.setTotal(pageList.getTotal());
        pageVO.setPageInfo(pageInfo);
        return pageVO;
    }

    @Override
    public void add(UserAddParam param) {
        //根据用户名查询用户
        User selectUser = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", param.getUsername())
                .last("LIMIT 1"));
        if (selectUser != null) {
            throw new BusinessException(DefaultResponseCode.USER_NAME_IS_EXIST);
        }
        //新增
        User user = new User();
        //查询组织信息
        Organization organization = organizationMapper.selectById(param.getDeptid());
        if (organization != null) {
            if (organization.getOrganizationName().equals("生产运营部")) {
                //生产运营部
                user.setType(1);
            } else if (organization.getDeptFlag() == 1) {
                //事业部
                user.setType(2);
            }
        }
        BeanUtil.copyProperties(param, user);
        //默认密码
        user.setPassword(SecureUtil.md5("123456"));
        userMapper.insertAndReturnId(user);
        //用户角色关联
        for (Long roleId : param.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void update(UserUpdateParam param) {
        Long userId = param.getUserId();
        //修改
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(DefaultResponseCode.USER_NOT_EXIST);
        }
        BeanUtil.copyProperties(param, user, true);
        userMapper.updateById(user);
        //角色处理
        userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId));
        //用户角色关联
        for (Long roleId : param.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void delete(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(DefaultResponseCode.USER_NOT_EXIST);
        }
        //获取当前登录用户id
        Long currentUserId = MyThreadLocal.getUserId();
        if (userId.equals(currentUserId)) {
            throw new BusinessException(DefaultResponseCode.CANT_DELETE_MYSELF);
        }
        //查询用户角色
       userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .eq("user_id", userId)).forEach(c -> {
           Role role = roleMapper.selectById(c.getRoleId());
           if (role != null && "auth_root".equals(role.getRoleCode())) {
               //root角色用户无法删除
               throw new BusinessException(DefaultResponseCode.USER_OF_ROOT_CAN_NOT_DELETE);
           }
       });
        userMapper.deleteById(user);
    }
}
