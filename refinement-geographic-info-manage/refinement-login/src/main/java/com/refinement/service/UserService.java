package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.entity.User;
import com.refinement.vo.UserVO;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return UserVO
     */
    UserVO login(String username, String password);

    /**
     * 修改密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}
