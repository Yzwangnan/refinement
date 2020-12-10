package com.refinement.controller;

import com.refinement.data.UserDeptProDO;
import com.refinement.entity.Department;
import com.refinement.entity.Project;
import com.refinement.entity.User;
import com.refinement.http.ResultDTO;
import com.refinement.http.ResultEnum;
import com.refinement.service.UserService;
import com.refinement.vo.UserDeptProVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(tags = "用户")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return ResultDTO
     */
    @PostMapping("/login")
    @ApiOperation(value = "login", notes = "登录")
    public ResultDTO login(String username, String password) {
        //参数验证 username
        if (StringUtils.isEmpty(username)) {
            return new ResultDTO(201, "用户名不能为空");
        }
        //参数验证 password
        if (StringUtils.isEmpty(password)) {
            return new ResultDTO(201, "密码不能为空");
        }
        ResultDTO resultDTO = userService.selectUserAndDeptAndPro(username.trim(), password);
        //请求成功封装数据
        if (resultDTO != null && ObjectUtils.isNotEmpty(resultDTO.getResult())) {
            UserDeptProDO userDeptProDO = (UserDeptProDO) resultDTO.getResult();
            //项目
            Project project = userDeptProDO.getProject();
            //判断项目的进行状态，新项目或者已完成项目阻止登录
            if (project != null && (project.getState() == 0 || project.getState() == 2)) {
                return new ResultDTO(ResultEnum.WRONG_LOGIN_OR_PASSWORD);
            }
            //用户
            User user = userDeptProDO.getUser();
            //部门
            Department department = userDeptProDO.getDepartment();
            UserDeptProVO userDeptProVO = new UserDeptProVO();
            //校验部门
            if (department != null) {
                userDeptProVO.setDeptid(department.getDeptid());
                userDeptProVO.setDeptname(department.getDeptname());
            }
            //校验项目
            if (project != null) {
                userDeptProVO.setProjectid(project.getProjectid());
                userDeptProVO.setProjectname(project.getProjectname());
            }
            //登录成功
//            session.setAttribute("username", username.trim());
            userDeptProVO.setType(user.getType());
            userDeptProVO.setUserid(user.getId());
            userDeptProVO.setUsername(user.getUsername());
            return new ResultDTO(userDeptProVO);
        } else {
            return resultDTO;
        }
    }

    /**
     * 更改密码接口
     *
     * @param userid 用户 id
     * @param passwordOld 更改前密码
     * @param passwordNew 更改后密码
     * @return ResultDTO
     */
    @PostMapping("/changePassword")
    @ApiOperation(value = "changePassword", notes = "更改密码")
    public ResultDTO changePassword(Long userid, String passwordOld, String passwordNew) {
        // userid
        if (ObjectUtils.isEmpty(userid)) {
            return new ResultDTO(201, "缺少参数");
        }
        // passwordOld
        if (StringUtils.isEmpty(passwordOld)) {
            return new ResultDTO(201, "原密码不能为空");
        }
        // passwordNew
        if (StringUtils.isEmpty(passwordNew)) {
            return new ResultDTO(201, "新密码不能为空");
        }
        return userService.changePassword(userid, passwordOld, passwordNew);
    }

    /**
     * 重置密码接口
     *
     * @param username 用户名
     * @return ResultDTO
     */
    @PostMapping("/resetPassword")
    @ApiOperation(value = "restPassword", notes = "重置密码")
    public ResultDTO restPassword(String username) {
        //参数校验 username
        if (StringUtils.isEmpty(username)) {
            return new ResultDTO(201, "用户名不能为空");
        }
        return userService.restPassword(username);
    }
}

