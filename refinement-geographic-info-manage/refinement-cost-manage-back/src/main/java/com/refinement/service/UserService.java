package com.refinement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.refinement.data.param.UserAddParam;
import com.refinement.data.param.UserUpdateParam;
import com.refinement.data.vo.UserPageVO;
import com.refinement.entity.User;

public interface UserService extends IService<User> {

    /**
     * 查询用户列表
     * @return UserPageVO
     */
    UserPageVO findPageList(Integer page, Integer size);

    /**
     * 新增用户
     * @param param 参数
     */
    void add(UserAddParam param);

    /**
     * 修改用户
     * @param param 参数
     */
    void update(UserUpdateParam param);

    /**
     * 删除用户
     * @param userId 用户id
     */
    void delete(Long userId);
}
