package com.refinement.controller;

import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@Api(tags = "登录API")
public class LoginController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    @PostMapping("/login")
    public Wrapper<?> login(@ApiParam("用户名") @NotBlank(message = "username 不能为空") String username,
                            @ApiParam("密码") @NotBlank(message = "password 不能为空") String password) {
        return WrapMapper.ok(userService.login(username.trim(), password));
    }

    /**
     * 修改密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return Wrapper
     */
    @PostMapping("/changePassword")
    public Wrapper<?> changePassword(@ApiParam("用户id") @NotNull(message = "userId 不能为空") Long userId,
                                     @ApiParam("旧密码") @NotBlank(message = "oldPassword 不能为空") String oldPassword,
                                     @ApiParam("新密码") @NotBlank(message = "newPassword 不能为空") String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return WrapMapper.ok();
    }
}
