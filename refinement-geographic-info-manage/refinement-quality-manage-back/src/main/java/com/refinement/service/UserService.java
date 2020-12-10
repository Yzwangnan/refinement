package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.entity.User;
import com.refinement.http.ResultDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface UserService extends IService<User> {

    //登录
    ResultDTO selectUserAndDeptAndPro(String username, String password);

    //更改密码
    ResultDTO changePassword(Long userid, String passwordOld, String passwordNew);

    //重置密码
    ResultDTO restPassword(String username);

}