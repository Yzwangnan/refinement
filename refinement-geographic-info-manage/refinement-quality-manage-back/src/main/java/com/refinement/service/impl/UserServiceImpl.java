package com.refinement.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.data.UserDeptProDO;
import com.refinement.entity.Department;
import com.refinement.entity.Project;
import com.refinement.entity.User;
import com.refinement.http.ResultDTO;
import com.refinement.http.ResultEnum;
import com.refinement.mapper.DepartmentMapper;
import com.refinement.mapper.ProjectMapper;
import com.refinement.mapper.UserMapper;
import com.refinement.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Override
    public ResultDTO selectUserAndDeptAndPro(String username, String password) {
        // 返回的对象
        UserDeptProDO userDeptProDO = new UserDeptProDO();
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        if (user == null) {
            // 用户未注册
            return new ResultDTO(ResultEnum.WRONG_LOGIN_OR_PASSWORD);
        } else {
            userDeptProDO.setUser(user);
        }
        if (!user.getUsername().equals(username)){
            return new ResultDTO(ResultEnum.WRONG_LOGIN_OR_PASSWORD);
        }
        if (!password.equals(user.getPassword())) {
            return new ResultDTO(ResultEnum.WRONG_LOGIN_OR_PASSWORD);
        }
        // 用户名和密码都比对成功
        // 查询用户对应部门  事业部用户和项目用户
        Department department = departmentMapper.selectOne(new QueryWrapper<Department>().eq("deptid", user.getDeptid()));
        // 查询用户对应的项目
        Project project = projectMapper.selectOne(new QueryWrapper<Project>().eq("projectid", user.getProjectid()));
        if (department != null) {
            userDeptProDO.setDepartment(department);
        }
        if (project != null) {
            userDeptProDO.setProject(project);
        }
        return new ResultDTO(userDeptProDO);
    }

    @Override
    public ResultDTO changePassword(Long userid, String passwordOld, String passwordNew) {
        // 校验 userid
        if (userid == null) {
            return new ResultDTO(ResultEnum.MISS_API_PARAM);
        }
        // 校验 passwordOld
        if (passwordOld == null) {
            return new ResultDTO(ResultEnum.MISS_API_PARAM);
        }
        // 校验 passwordNew
        if (passwordNew == null) {
            return new ResultDTO(ResultEnum.MISS_API_PARAM);
        }
        // 根据id查询用户
        User user = userMapper.selectById(userid);
        if (user == null) {
            return new ResultDTO(201, "不存在该用户");
        }
        // 比较旧密码是否相同
        if (!passwordOld.equals(user.getPassword())) {
            return new ResultDTO(201, "原密码输入错误，请重新输入");
        } else {
            user.setPassword(passwordNew);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
            return new ResultDTO();
        }
    }

    @Override
    public ResultDTO restPassword(String username) {
        //根据用户名查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            return new ResultDTO(201, "用户不存在");
        }
        //重置密码
        user.setPassword(SecureUtil.md5("123456"));
        //用户类型
        Integer type = user.getType();
        // 事业部
        if (type == 2) {
            user.setUsername(user.getDeptid());
        }
        // 项目部
        if (type == 3) {
            user.setUsername(user.getProjectid());
        }
        // 更新时间
        user.setUpdateTime(LocalDateTime.now());
        //更新用户信息
        userMapper.updateById(user);
        return new ResultDTO();
    }
}
