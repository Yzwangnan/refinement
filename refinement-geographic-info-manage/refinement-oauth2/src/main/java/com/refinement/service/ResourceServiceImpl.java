package com.refinement.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.refinement.constant.RedisConstant;
import com.refinement.entity.Function;
import com.refinement.entity.Role;
import com.refinement.entity.RoleFunction;
import com.refinement.mapper.FunctionMapper;
import com.refinement.mapper.ResourceMapper;
import com.refinement.mapper.RoleFunctionMapper;
import com.refinement.mapper.RoleMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 资源与角色匹配关系管理业务类
 * Created by macro on 2020/6/19.
 */
@Service
public class ResourceServiceImpl {

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private FunctionMapper functionMapper;

    @Resource
    private RoleFunctionMapper roleFunctionMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void initData() {
        Map<String, List<String>> resourceRolesMap = new TreeMap<>();
        List<com.refinement.entity.Resource> resourceList = resourceMapper.selectList(null);
        if (CollUtil.isNotEmpty(resourceList)) {
            for (com.refinement.entity.Resource resource : resourceList) {
                //查询对应功能
                Function function = functionMapper.selectOne(new QueryWrapper<Function>()
                        .like("resource_ids", "," + resource.getId() + ",")
                        .last("LIMIT 1"));
                if (function != null) {
                    //查询功能的所有角色列表
                    List<RoleFunction> roleFunctionList = roleFunctionMapper.selectList(new QueryWrapper<RoleFunction>()
                            .eq("function_id", function.getId()));
                    List<String> roles = roleFunctionList.stream().map(roleFunction -> {
                        //角色id
                        Long roleId = roleFunction.getRoleId();
                        //角色信息
                        Role role = roleMapper.selectById(roleId);
                        if (role != null) {
                            return role.getRoleCode();
                        }
                        return null;
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                    resourceRolesMap.put(resource.getUri(), roles);
                }
            }
        }
        redisTemplate.opsForHash().putAll(RedisConstant.RESOURCE_ROLES_MAP, resourceRolesMap);
    }
}
