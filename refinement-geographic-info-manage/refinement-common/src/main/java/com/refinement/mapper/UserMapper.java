package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.entity.User;

/**
 * <p>
 *  UserMapper 接口
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
public interface UserMapper extends BaseMapper<User> {

    void insertAndReturnId(User user);
}
