package com.refinement.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.refinement.constant.MessageConstant;
import com.refinement.domain.SecurityUser;
import com.refinement.entity.Role;
import com.refinement.entity.User;
import com.refinement.entity.UserRole;
import com.refinement.http.UserDTO;
import com.refinement.mapper.RoleMapper;
import com.refinement.mapper.UserMapper;
import com.refinement.mapper.UserRoleMapper;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("username", username)
                .last("LIMIT 1"));
        if (user == null) {
            throw new UsernameNotFoundException(MessageConstant.USERNAME_PASSWORD_ERROR);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserDTO dto = new UserDTO();
        BeanUtil.copyProperties(user, dto);
        //查询所有角色
        List<String> roles = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .eq("user_id", user.getId())).stream().map(c -> {
            Role role = roleMapper.selectById(c.getRoleId());
            if (role != null) {
                return role.getRoleCode();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //该项目中此处设置用户可用，没有做动态处理
        dto.setStatus(1);
        dto.setRoles(roles);
        SecurityUser securityUser = new SecurityUser(dto);
        if (!securityUser.isEnabled()) {
            throw new DisabledException(MessageConstant.ACCOUNT_DISABLED);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.ACCOUNT_LOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.ACCOUNT_EXPIRED);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.CREDENTIALS_EXPIRED);
        }
        return securityUser;
    }
}
