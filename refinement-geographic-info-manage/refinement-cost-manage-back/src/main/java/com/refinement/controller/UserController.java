package com.refinement.controller;

import com.refinement.data.param.UserAddParam;
import com.refinement.data.param.UserUpdateParam;
import com.refinement.data.vo.UserPageVO;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Api(tags = "用户API")
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户列表
     * @param page 页
     * @param size 大小
     * @return Wrapper
     */
    @GetMapping("/list")
    @ApiOperation("用户列表")
    public Wrapper<UserPageVO> list(@ApiParam("页") @RequestParam @NotNull(message = "参数 page 为null") Integer page,
                                    @ApiParam("大小") @RequestParam(required = false, defaultValue = "10") Integer size) {
        return WrapMapper.ok(userService.findPageList(page, size));
    }

    /**
     * 新增用户
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/add")
    @ApiOperation("新增用户")
    public Wrapper<?> add(@Validated @RequestBody UserAddParam param) {
        userService.add(param);
        return WrapMapper.ok();
    }

    /**
     * 修改用户
     * @param param 参数
     * @return Wrapper
     */
    @PostMapping("/update")
    @ApiOperation("修改用户")
    public Wrapper<?> update(@Validated @RequestBody UserUpdateParam param) {
        userService.update(param);
        return WrapMapper.ok();
    }

    /**
     * 删除用户
     * @param userId 用户id
     * @return Wrapper
     */
    @PostMapping("/delete")
    @ApiOperation("删除用户")
    public Wrapper<?> delete(@ApiParam("用户id") @RequestParam @NotNull(message = "userId 不能为空") Long userId) {
        userService.delete(userId);
        return WrapMapper.ok();
    }
}
