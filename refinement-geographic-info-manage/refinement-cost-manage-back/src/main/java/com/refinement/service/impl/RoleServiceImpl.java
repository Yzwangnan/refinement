package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.config.MyThreadLocal;
import com.refinement.constant.RedisConstant;
import com.refinement.data.enums.ExportEnum;
import com.refinement.data.param.RoleAddParam;
import com.refinement.data.param.RoleUpdateParam;
import com.refinement.data.vo.*;
import com.refinement.entity.*;
import com.refinement.http.DefaultResponseCode;
import com.refinement.mapper.*;
import com.refinement.service.RoleService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleFunctionMapper roleFunctionMapper;

    @Resource
    private FunctionMapper functionMapper;

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;


    @Override
    public List<RoleVO> getRoleList(Integer type) {
        QueryWrapper<Role> query = new QueryWrapper<>();
        query.notIn("parent_id", 0);
        if (type == 1) {
            query.like("role_name", "记录员");
        } else if (type == 2) {
            query.like("role_name", "审核员");
        }
        return roleMapper.selectList(query).stream().map(c -> {
            RoleVO vo = new RoleVO();
            BeanUtil.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TreeSelect> buildDeptTreeSelect(String name) {
        List<RoleEntity> roleList = roleMapper.getRoleList();
        if(name != null && !"".equals(name)) {
            if(roleList != null && roleList.size() > 0) {
                roleList.forEach(c->{
                    if(c.getRoleName() != null && c.getRoleName().contains(name)) {
                        c.setIsMatch(1);
                    }
                });
            }
        }
        List<RoleEntity> roleTrees = buildDeptTree(roleList);
        return roleTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    @Override
    public RoleDetailVO detail(Long roleId) {
        RoleDetailVO roleDetailVO = new RoleDetailVO();
        RoleEntity role = roleMapper.getRoleById(roleId);
        List<FunctionEntity> functionList = functionMapper.getFunctionList();
        if(role != null) {
            roleDetailVO.setId(role.getId());
            roleDetailVO.setRoleName(role.getRoleName());
            roleDetailVO.setIntroduction(role.getIntroduction());
            QueryWrapper<RoleFunction> query = new QueryWrapper<>();
            query.eq("role_id", role.getId());
            List<RoleFunction> roleFunctions = roleFunctionMapper.selectList(query);
            List<Long> roleIdList = new ArrayList<>();
            if(roleFunctions != null && roleFunctions.size() > 0) {
                roleFunctions.forEach(c->{
                    roleIdList.add(c.getFunctionId());
                });
            }
            if(roleIdList.size() > 0) {
                if(CollUtil.isNotEmpty(functionList)) {
                    functionList.forEach(c -> {
                        if (roleIdList.contains(c.getId())) {
                            c.setIsCheck(1);
                        }else {
                            c.setIsCheck(0);
                        }
                    });
                }
            }
        }
        List<RoleFunctionOneVO> functionOneList = new ArrayList<>();
        if(functionList != null && functionList.size() > 0) {
            functionList.forEach(c->{
                if(c.getParentId() == 0) {
                    RoleFunctionOneVO roleFunctionOneVO = new RoleFunctionOneVO();
                    roleFunctionOneVO.setId(c.getId());
                    roleFunctionOneVO.setFunctionName(c.getFunctionName());
                    roleFunctionOneVO.setIsCheck(c.getIsCheck());
                    List<RoleFunctionTwoVO> functionTwoList = new ArrayList<>();
                    functionList.forEach(d->{
                        if(d.getParentId().longValue() == c.getId().longValue()) {
                            RoleFunctionTwoVO roleFunctionTwoVO = new RoleFunctionTwoVO();
                            roleFunctionTwoVO.setId(d.getId());
                            roleFunctionTwoVO.setFunctionName(d.getFunctionName());
                            roleFunctionTwoVO.setIsCheck(d.getIsCheck());
                            List<RoleFunctionThreeVO> functionThreeList = new ArrayList<>();
                            functionList.forEach(e->{
                                if (e.getParentId().longValue() == d.getId().longValue()) {
                                    RoleFunctionThreeVO roleFunctionThreeVO = new RoleFunctionThreeVO();
                                    BeanUtil.copyProperties(e, roleFunctionThreeVO);
                                    functionThreeList.add(roleFunctionThreeVO);
                                }
                            });
                            roleFunctionTwoVO.setFunctionList(functionThreeList);
                            functionTwoList.add(roleFunctionTwoVO);
                        }
                    });
                    roleFunctionOneVO.setFunctionList(functionTwoList);
                    functionOneList.add(roleFunctionOneVO);
                }
            });
        }
        roleDetailVO.setFunctionList(functionOneList);
        return roleDetailVO;
    }

    @Override
    public void delete(Long roleId) {
        //获取当前登录用户id
        Long currentUserId = MyThreadLocal.getUserId();
        QueryWrapper<UserRole> query = new QueryWrapper<>();
        query.eq("user_id", currentUserId);
        List<UserRole> userRoleList = userRoleMapper.selectList(query);
        if (userRoleList != null && userRoleList.size() > 0) {
            List<Long> roleIdList = new ArrayList<>();
            userRoleList.forEach(c->{
                roleIdList.add(c.getRoleId());
            });

            if(roleIdList.size() > 0) {
                check(roleIdList,roleId);
            }
        }
        Role role = roleMapper.selectById(roleId);
        if(role != null) {
            if("root".equals(role.getRoleName())) {
                throw new BusinessException(DefaultResponseCode.ROOT_ROLE_CANNOT_DELETE);
            }
            deleteAll(role);
//            roleMapper.deleteById(role);
            //同步redis权限数据
            initRedisData();
        } else {
            throw new BusinessException(DefaultResponseCode.NO_ROLE);
        }

    }

    public void deleteAll(Role role){
        if(role != null) {
            roleMapper.deleteById(role);
            while (true){
                QueryWrapper<Role> query = new QueryWrapper<>();
                query.eq("parent_id", role.getId());
                List<Role> roleList = roleMapper.selectList(query);
                if(roleList != null && roleList.size() > 0) {
                    roleList.forEach(c->{
                        deleteAll(c);
                    });
                }
                break;
            }
        }
    }

    @Transactional
    @Override
    public void add(RoleAddParam roleParam) {
        QueryWrapper<Role> roleQuery = new QueryWrapper<>();
        roleQuery.eq("role_name", roleParam.getRoleName());
        roleQuery.eq("parent_id", roleParam.getParentId());
        Role r = roleMapper.selectOne(roleQuery);
        if(r != null) {
            throw new BusinessException(DefaultResponseCode.ROLE_IS_EXIST);
        }
        Role role = new Role();
        BeanUtil.copyProperties(roleParam, role);
        role.setRoleCode("auth_"+IdUtil.simpleUUID());
        roleMapper.insert(role);
        QueryWrapper<Role> query = new QueryWrapper<>();
        query.eq("role_name", role.getRoleName());
        query.eq("parent_id", role.getParentId());
        Role info = roleMapper.selectOne(query);
        if(info != null) {
            List<Long> functionIds = roleParam.getFunctionIds();
            List<RoleFunction> functionList = functionIds.stream().map(c -> {
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setFunctionId(c);
                roleFunction.setRoleId(info.getId());
                return roleFunction;
            }).collect(Collectors.toList());
            if (functionList.size() > 0) {
                roleFunctionMapper.saveBatch(functionList);
            }
            //同步redis权限数据
            initRedisData();
        } else {
            throw new BusinessException(DefaultResponseCode.SAVE_FAIL);
        }

    }

    @Transactional
    @Override
    public void edit(RoleUpdateParam roleUpdateParam) {
        Role role = new Role();
        role.setId(roleUpdateParam.getRoleId());
        Role info = roleMapper.selectById(role);
        if(info != null) {
            if("root".equals(role.getRoleName())) {
                throw new BusinessException(DefaultResponseCode.ROOT_ROLE_CANNOT_UPDATE);
            }
            //获取当前登录用户id
            Long currentUserId = MyThreadLocal.getUserId();
            QueryWrapper<UserRole> userQuery = new QueryWrapper<>();
            userQuery.eq("user_id", currentUserId);
            List<UserRole> userRoleList = userRoleMapper.selectList(userQuery);
            if (userRoleList != null && userRoleList.size() > 0) {
                List<Long> roleIdList = new ArrayList<>();
                userRoleList.forEach(c->{
                    roleIdList.add(c.getRoleId());
                });

                if(roleIdList.size() > 0) {
                    if(roleIdList.contains(roleUpdateParam.getRoleId())) {
                        throw new BusinessException(DefaultResponseCode.CANT_UPDATE_MYSELF_ROLE);
                    }
                }
            }
            info.setRoleName(roleUpdateParam.getRoleName());
            info.setIntroduction(roleUpdateParam.getIntroduction());
            // 更新角色表
            roleMapper.updateById(info);

            // 删除角色原有绑定的功能
            QueryWrapper<RoleFunction> query = new QueryWrapper<>();
            query.eq("role_id", role.getId());
            roleFunctionMapper.delete(query);

            // 绑定新的功能
            List<Long> functionIds = roleUpdateParam.getFunctionIds();
            List<RoleFunction> functionList = functionIds.stream().map(c -> {
                RoleFunction roleFunction = new RoleFunction();
                roleFunction.setFunctionId(c);
                roleFunction.setRoleId(info.getId());
                return roleFunction;
            }).collect(Collectors.toList());
            if (functionList.size() > 0) {
                roleFunctionMapper.saveBatch(functionList);
            }
            // 同步redis权限数据
            new Thread(this::initRedisData).start();
        } else {
            throw new BusinessException(DefaultResponseCode.NO_ROLE);
        }
    }

    @Override
    public void update(Long roleId, Long targetId) {
        Role role = new Role();
        role.setId(roleId);
        Role info = roleMapper.selectById(role);
        if(info != null) {
            info.setParentId(targetId);
            info.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(info);
            //同步redis权限数据
            initRedisData();
        } else {
            throw new BusinessException(DefaultResponseCode.NO_ROLE);
        }
    }

    @Override
    public boolean judgeExportRole(Integer type, Long userId) {
        //获取判断的url
        String url;
        if (type == 0) {
            url = ExportEnum.NEW_PROJECT_EXPORT.getValue();
        } else if (type == 1) {
            url = ExportEnum.PROGRESS_PROJECT_EXPORT.getValue();
        } else if (type == 2) {
            url = ExportEnum.PROGRESS_PROJECT_SPECIFY_EXPORT.getValue();
        } else if (type == 3) {
            url = ExportEnum.HISTORY_PROJECT_EXPORT.getValue();
        } else {
            return false;
        }
        //用户角色关联列表
        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .eq("user_id", userId));
        //判断条件
        for (UserRole userRole : userRoleList) {
            Long roleId = userRole.getRoleId();
            //功能列表
            List<RoleFunction> roleFunctionList = roleFunctionMapper.selectList(new QueryWrapper<RoleFunction>()
                    .eq("role_id", roleId));
            for (RoleFunction roleFunction : roleFunctionList) {
                Long functionId = roleFunction.getFunctionId();
                //功能信息
                Function function = functionMapper.selectById(functionId);
                if (function != null) {
                    //资源id列表
                    List<String> resourceIdList = StrUtil.splitTrim(function.getResourceIds(), ",");
                    //资源url列表
                    List<String> urlList = new ArrayList<>();
                    resourceIdList.forEach(resourceId -> {
                        com.refinement.entity.Resource resource = resourceMapper.selectById(resourceId);
                        if (resource != null) {
                            urlList.add(resource.getUri());
                        }
                    });
                    if (urlList.contains(url)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 验证要删除的是否是自己所属的角色以及父角色
     * @param roleIdList
     * @param roleId
     */
    public void check(List<Long> roleIdList,Long roleId){

        roleIdList.forEach(c->{
            Role role = roleMapper.selectById(c);
            if(role != null) {
                while (true){
                    if(role.getId().longValue() == roleId.longValue()) {
                        throw new BusinessException(DefaultResponseCode.CANT_DELETE_MYSELF_ROLE);
                    }
                    Long parentId = role.getParentId();
                    if(parentId != null && parentId != 0) {
                        List<Long> newRoleIdList = new ArrayList<>();
                        newRoleIdList.add(parentId);
                        check(newRoleIdList,roleId);
                    }
                    break;
                }
            }
        });


    }

    /**
     * 构建前端所需要树结构
     *
     * @param roleList 角色列表
     * @return 树结构列表
     */
    public List<RoleEntity> buildDeptTree(List<RoleEntity> roleList)
    {
        List<RoleEntity> returnList = new ArrayList<RoleEntity>();
        List<Long> tempList = new ArrayList<Long>();
        for (RoleEntity dept : roleList)
        {
            tempList.add(dept.getId());
        }
        for (Iterator<RoleEntity> iterator = roleList.iterator(); iterator.hasNext();)
        {
            RoleEntity roleEntity = (RoleEntity) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(roleEntity.getParentId()))
            {
                recursionFn(roleList, roleEntity);
                returnList.add(roleEntity);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = roleList;
        }
        return returnList;
    }

    /**
     * 递归列表
     * @param list
     * @param t
     */
    private void recursionFn(List<RoleEntity> list, RoleEntity t)
    {
        // 得到子节点列表
        List<RoleEntity> childList = getChildList(list, t);
        t.setChildren(childList);
        for (RoleEntity tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                // 判断是否有子节点
                Iterator<RoleEntity> it = childList.iterator();
                while (it.hasNext())
                {
                    RoleEntity n = (RoleEntity) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     * @param list
     * @param t
     * @return
     */
    private List<RoleEntity> getChildList(List<RoleEntity> list, RoleEntity t)
    {
        List<RoleEntity> tlist = new ArrayList<RoleEntity>();
        Iterator<RoleEntity> it = list.iterator();
        while (it.hasNext())
        {
            RoleEntity n = (RoleEntity) it.next();
            if (n.getParentId() != null && n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     * @param list
     * @param t
     * @return
     */
    private boolean hasChild(List<RoleEntity> list, RoleEntity t)
    {
        return getChildList(list, t).size() > 0;
    }

    //同步redis权限
    private void initRedisData() {
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
