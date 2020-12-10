package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.entity.Project;
import com.refinement.entity.Role;
import com.refinement.entity.User;
import com.refinement.entity.UserRole;
import com.refinement.http.DefaultResponseCode;
import com.refinement.mapper.ProjectMapper;
import com.refinement.mapper.RoleMapper;
import com.refinement.mapper.UserMapper;
import com.refinement.mapper.UserRoleMapper;
import com.refinement.service.UserService;
import com.refinement.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public UserVO login(String username, String password) {
        //查询Mapper
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username", username);
        query.last("LIMIT 1");
        //查询
        User user = userMapper.selectOne(query);
        if (user == null) {
            throw new BusinessException(DefaultResponseCode.USER_NOT_EXIST);
        }
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(DefaultResponseCode.PASSWORD_NOT_CORRECT);
        }
        //VO
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", user.getProjectid()));
        if (project != null) {
            vo.setProjectname(project.getProjectname());
        }
        //用户角色关联
        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .eq("user_id", vo.getId()));
        userRoleList.forEach(userRole -> {
            //角色
            Role role = roleMapper.selectById(userRole.getRoleId());
            if (role != null && ("root".contains(role.getRoleName()) || "生产主管".equals(role.getRoleName()))) {
                //root及生产主管角色显示项目完成按钮
                vo.setShowComplete(1);
                //root及生产主管角色显示新建项目按钮
                vo.setShowAddNewProject(1);
            } else {
                vo.setShowComplete(0);
                vo.setShowAddNewProject(0);
            }
        });
        return vo;
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        //用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(DefaultResponseCode.USER_NOT_EXIST);
        }
        if (!StrUtil.equals(oldPassword, user.getPassword())) {
            throw new BusinessException(DefaultResponseCode.PASSWORD_NOT_CORRECT);
        }
        if (StrUtil.equals(newPassword, oldPassword)) {
            throw new BusinessException(DefaultResponseCode.PASSWORD_CAN_NOT_CONSISTENT);
        }
        user.setPassword(newPassword);
        userMapper.updateById(user);
    }
}
