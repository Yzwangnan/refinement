package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.config.MyThreadLocal;
import com.refinement.constant.ProjectSpecifyTitleConstant;
import com.refinement.data.ClassifyCategoryDO;
import com.refinement.data.param.*;
import com.refinement.data.vo.*;
import com.refinement.entity.*;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.PageResult;
import com.refinement.mapper.*;
import com.refinement.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private DecompositionMapper decompositionMapper;

    @Resource
    private ProjectDecompositionMapper projectDecompositionMapper;

    @Resource
    private ProjectMonthlyReportMapper projectMonthlyReportMapper;

    @Resource
    private ClassifyCategoryMapper classifyCategoryMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private ProjectCompleteMapper projectCompleteMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private ModelMapper modelMapper;

    @Override
    public ProjectListPageVO list(Integer type, String name, Long categoryId, String startTime, Long modelId, Integer page, Integer size) {
        //返回VO
        ProjectListPageVO listPageVO = new ProjectListPageVO();
        //标题列表
        List<String> titleList = new ArrayList<>();
        //当前登录用户信息
        Long userId = MyThreadLocal.getUserId();
        User user = userMapper.selectById(userId);
        //事业部id
        String deptId = null;
        if (user != null && user.getType() != null && user.getType() == 2) {
            deptId = user.getDeptid();
        }
        //分页数据
        IPage<Project> iPage = projectMapper.listPage(new Page<>(page, size), type, name, categoryId, startTime, modelId, deptId);
        //项目列表
        List<Project> projectList = iPage.getRecords();
        //序号
        AtomicInteger order = new AtomicInteger(1);
        //一级分项id数组
        List<Long> idList = new ArrayList<>();
        //项目列表VO
        List<ProjectVO> voList = projectList.stream().map(c -> {
            ProjectVO vo = new ProjectVO();
            BeanUtil.copyProperties(c, vo);
            //项目状态
            if (type == 0) {
                //新项目
                vo.setState("0");
            } else if (type == 1) {
                //进行中项目
                vo.setState("1");
            } else if (type == 2) {
                //历史项目
                vo.setState("2");
            }
            //序号
            vo.setOrder(order.get());
            order.getAndIncrement();
            //事业部
            Organization organization = organizationMapper.selectById(c.getDeptid());
            if (organization != null) {
                vo.setDeptName(organization.getOrganizationName());
            }
            //累计使用
            AtomicReference<BigDecimal> totalUse = new AtomicReference<>(BigDecimal.ZERO);
            if (type == 1 || type == 2) {
                //不为空才进行操作
                if (CollUtil.isEmpty(idList)) {
                    //查询项目的一级成本分项列表
                    List<Decomposition> decompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                            .eq("model_id", c.getModelId())
                            .eq("level", 1));
                    decompositionList.forEach(decomposition -> {
                        idList.add(decomposition.getId());
                        //新增一级分类标题
                        titleList.add(decomposition.getOneLevelName());
                        titleList.add("已使用");
                        titleList.add("比例");
                    });
                }
                //分项列表VO
                List<String> decompositionVOList = new ArrayList<>();
                idList.forEach(id -> {
                    //项目成本分解项列表
                    List<ProjectDecomposition> projectDecompositionList = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                            .eq("project_id", c.getProjectid()));
                    //查询项目对应成本分项
                    ProjectDecomposition projectDecomposition = projectDecompositionMapper.selectOne(new QueryWrapper<ProjectDecomposition>()
                            .eq("project_id", c.getProjectid())
                            .eq("decomposition_id", id)
                            .last("LIMIT 1"));
                    if (projectDecomposition != null) {
                        decompositionVOList.add(new BigDecimal(projectDecomposition.getBudgetAmount())
                                .divide(new BigDecimal("10000"), 6, RoundingMode.HALF_UP).toString() + "万");
                        //所有需要计算已使用细化项的id数组
                        List<Long> projectDecompositionIdList
                                = judgeReturnList(projectDecompositionList, projectDecomposition.getId());
                        if (CollUtil.isEmpty(projectDecompositionIdList)) {
                            //表明该分类下无子分类
                            projectDecompositionIdList.add(projectDecomposition.getId());
                        }
                        //已使用
                        BigDecimal isUse = isUse(projectDecompositionIdList);
                        totalUse.accumulateAndGet(isUse, BigDecimal::add);
                        decompositionVOList.add(isUse.divide(new BigDecimal("10000"), 6, RoundingMode.HALF_UP).toString() + "万");
                        try {
                            //使用占比
                            BigDecimal scale = isUse.multiply(new BigDecimal("100"))
                                    .divide(new BigDecimal(projectDecomposition.getBudgetAmount()), 2, RoundingMode.HALF_UP);
                            decompositionVOList.add(scale.toString() + "%");
                        } catch (Exception e) {
                            decompositionVOList.add(BigDecimal.ZERO.toString());
                        }
                    }
                });
                if (c.getBudgetAmount() == null) {
                    vo.setIsUse(BigDecimal.ZERO);
                } else {
                    vo.setIsUse(totalUse.get().divide(new BigDecimal("10000"), 6, RoundingMode.HALF_UP));
                }
                //预算使用比例
                try {
                    vo.setScale(totalUse.get().divide(c.getBudgetAmount().multiply(new BigDecimal("100")), 2, RoundingMode.HALF_UP));
                } catch (Exception e) {
                    vo.setScale(null);
                }
                //月报情况
                //1->待审核：上报/修改待审核
                //2->驳回：上报未通过
                //3->已报：上报已通过
                //4->未报：未查询到当月月报记录或超过上报时间但未上报
                //5->超时：超过结束上报日期号后还未通过审核
                List<ProjectMonthlyReport> monthlyReportList = projectMonthlyReportMapper.selectList(new QueryWrapper<ProjectMonthlyReport>()
                        .eq("project_id", c.getProjectid())
                        .eq("month", LocalDate.now().getMonthValue()));
                //查询模板信息
                Model model = modelMapper.selectById(c.getModelId());
                if (model == null) {
                    throw new BusinessException(DefaultResponseCode.MODEL_IS_NOT_EXIST);
                }
                if (CollUtil.isEmpty(monthlyReportList)) {
                    //未报
                    vo.setMonthlyReport(4);
                } else {
                    List<Integer> collectLit
                            = monthlyReportList.stream().map(ProjectMonthlyReport::getState).filter(Objects::nonNull).collect(Collectors.toList());
                    if (collectLit.contains(0)) {
                        //待审核
                        vo.setMonthlyReport(1);
                    } else if (collectLit.contains(2)) {
                        //驳回
                        vo.setMonthlyReport(2);
                        //超时未通过
//                        if (LocalDate.now().getDayOfMonth() > model.getEndTime()) {
//                            vo.setMonthlyReport(5);
//                        }
                    } else {
                        //审核通过
                        vo.setMonthlyReport(3);
                    }
                }
                vo.setDecompositionList(decompositionVOList);
            }
            if (type == 2) {
                //实际利润率
                try {
                    vo.setProfit(totalUse.get().divide(c.getContractvalue().multiply(new BigDecimal("100")), 2, RoundingMode.HALF_UP));
                } catch (Exception e) {
                    vo.setProfit(null);
                }
            }
            return vo;
        }).collect(Collectors.toList());
        listPageVO.setTitleList(titleList);
        listPageVO.setProjectList(voList);
        //分页参数
        PageResult pageInfo = new PageResult(){{
            setCurrent(page);
            setSize(projectList.size());
            setTotal(iPage.getTotal());
        }};
        listPageVO.setPageInfo(pageInfo);
        return listPageVO;
    }

    @Transactional
    @Override
    public void add(ProjectAddParam param) {
        //查询项目名称是否存在
        Project selectOne = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectname", param.getProjectname())
                .last("LIMIT 1"));
        if (selectOne != null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_NAME_IS_EXIST);
        }
        //新增一个项目部用户
        User user = new User();
        BeanUtil.copyProperties(param, user);
        user.setUsername(param.getProjectid());
        user.setType(3);
        userMapper.insertAndReturnId(user);
        //赋予项目角色
        Role role = roleMapper.selectOne(new QueryWrapper<Role>()
                .eq("parent_id", 0)
                .eq("role_name", "项目部")
                .last("LIMIT 1"));
        if (role != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        } else {
            log.info("用户id 为 {} 的用户赋予项目部角色失败，无法在角色表中查询到名称为 项目部 的角色数据记录", param.getProjectid());
        }
        Project project = new Project();
        BeanUtil.copyProperties(param, project);
        project.setUserid(user.getId());
        project.setSystemType(2);
        //查询二级分类
        ClassifyCategoryDO category = classifyCategoryMapper.selectCategoryById(param.getCategoryId());
        if (category != null) {
            //查询一级分类
            ClassifyCategoryDO classify = classifyCategoryMapper.selectClassifyById(category.getClassifyId());
            if (classify != null) {
                project.setClassifyId(classify.getId());
                project.setCategory(classify.getName() + "/" + category.getName());
            }
        }
        projectMapper.insert(project);
    }

    @Override
    public String createProjectId() {
        //获取数据库项目ID列表
        List<String> projectIds = projectMapper.listProjectIds();
        //返回的项目ID
        String projectId;
        //当前年份
        String year = LocalDate.now().getYear() + "";
        //当前月份
        String month = LocalDate.now().getMonthValue() + "";
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if (CollUtil.isEmpty(projectIds)) {
            //返回的项目ID
            projectId = "ZJZY" + year + month + "01";
        } else {
            //最新的项目ID
            String newProjectId = projectIds.get(0);
            String str = newProjectId.substring(10);
            int number = Integer.parseInt(str);
            //个位数
            if (number < 9) {
                projectId = "ZJZY" + year + month + "0" + (number + 1);
            } else {
                projectId = "ZJZY" + year + month + (number + 1);
            }
        }
        return projectId;
    }

    @Transactional(rollbackFor = {Exception.class, BusinessException.class})
    @Override
    public void decomposition(ProjectDecompositionParam param) {
        //查询项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", param.getProjectid())
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //存在细化分解信息
        if (project.getModelId() != null) {
            projectDecompositionMapper.delete(new QueryWrapper<ProjectDecomposition>()
                    .eq("project_id", project.getProjectid()));
        }
        //查询模板分项列表
        Long modelId = param.getModelId();
        List<Decomposition> decompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", modelId));
        if (CollUtil.isEmpty(decompositionList)) {
            throw new BusinessException(DefaultResponseCode.MODEL_IS_NOT_EXIST);
        }
        //细化分解数据
        List<ProjectSpecifyParam> specifyList = param.getSpecify();
        //key-id value-预算金额
        Map<Long, String> collectMap = new HashMap<>();
        specifyList.forEach(c -> collectMap.put(c.getId(), c.getCost()));
        //一级预算总和
        BigDecimal oneLevelTotal = BigDecimal.ZERO;
        //记录三级分项按模板比例分配的集合
//        List<Long> defaultIdList = new ArrayList<>();
        //记录等的项索引，返回前端标红
        int idx = 0;
        //返回给前端的字符串
        StringBuilder message = new StringBuilder();
        //key-分项id value-索引
        Map<Long, Integer> collect = new HashMap<>();
        for (Decomposition decomposition : decompositionList) {
            //级别
            Integer level = decomposition.getLevel();
            if (level == 1) {
                collect.put(decomposition.getId(), idx);
                idx++;
                //一级预算金额
                String oneLevelCost = MapUtil.getStr(collectMap, decomposition.getId());
                //单项一级预算
                BigDecimal oneLevelItem = BigDecimal.ZERO;
                //记录所有下级预算
                BigDecimal oneLevelAmount = BigDecimal.ZERO;
                //查询该一级下的所有二级分项
                List<Decomposition> twoLevelList = getDecompositionList(modelId, 2, decomposition.getId());
                if (CollUtil.isNotEmpty(twoLevelList)) {
                    //判断一级下的二级花费是否未填
//                    boolean flag = true;
                    //判断是否存在三级
//                    boolean res = false;
                    for (Decomposition twoLevel : twoLevelList) {
                        //二级id
                        Long twoLevelId = twoLevel.getId();
                        collect.put(twoLevelId, idx);
                        idx++;
                        //二级花费
                        String twoLevelCost = MapUtil.getStr(collectMap, twoLevelId);
                        if (StrUtil.isNotBlank(twoLevelCost)) {
                            oneLevelAmount = oneLevelAmount.add(new BigDecimal(twoLevelCost));
                        }
//                        if (StrUtil.isBlank(twoLevelCost)) {
//                            //默认预算比例
//                            String budgetScale = twoLevel.getBudgetScale();
//                            if (StrUtil.isNotBlank(oneLevelCost)) {
//                                if (StrUtil.isNotBlank(budgetScale)) {
//                                    //说明使用默认比例
//                                    collectMap.put(twoLevelId, new BigDecimal(oneLevelCost).multiply(new BigDecimal(budgetScale)
//                                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)).toString());
//                                } else {
//                                    //均分
//                                    collectMap.put(twoLevelId, new BigDecimal(oneLevelCost)
//                                            .divide(new BigDecimal(twoLevelList.size()), 2, RoundingMode.HALF_UP).toString());
//                                }
//                            }
//                            //将id添加到标记集合
//                            defaultIdList.add(twoLevelId);
//                            flag = false;
//                        } else {
//                            oneLevelAmount = oneLevelAmount.add(new BigDecimal(twoLevelCost));
//                        }
                        //查询该二级下的所有三级分项
                        List<Decomposition> threeLevelList = getDecompositionList(modelId, 3, twoLevelId);
//                        if (CollUtil.isNotEmpty(threeLevelList)) {
//                            res = true;
//                        }
                        //记录三级预算总计
                        BigDecimal threeLevelTotal = BigDecimal.ZERO;
                        for (Decomposition threeLevel : threeLevelList) {
                            //存在三级分项并且未填二级分项预算
                            if (StrUtil.isBlank(twoLevelCost)) {
                                throw new BusinessException(DefaultResponseCode.TWO_LEVEL_COST_CAN_NOT_EMPTY);
                            }
                            //三级id
                            Long threeLevelId = threeLevel.getId();
                            collect.put(threeLevelId, idx);
                            idx++;
                            //三级花费
                            String threeLevelCost = MapUtil.getStr(collectMap, threeLevelId);
                            if (StrUtil.isNotBlank(threeLevelCost)) {
                                threeLevelTotal = threeLevelTotal.add(new BigDecimal(threeLevelCost));
                            }
//                            if (StrUtil.isBlank(threeLevelCost)) {
//                                //默认预算比例
//                                String budgetScale = threeLevel.getBudgetScale();
//                                if (StrUtil.isNotBlank(budgetScale)) {
//                                    //说明使用默认比例
//                                    collectMap.put(threeLevelId, new BigDecimal(twoLevelCost).multiply(new BigDecimal(budgetScale)
//                                            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)).toString());
//                                } else {
//                                    //均分
//                                    collectMap.put(threeLevelId, new BigDecimal(twoLevelCost)
//                                            .divide(new BigDecimal(threeLevelList.size()), 2, RoundingMode.HALF_UP).toString());
//                                }
//                                //将id添加到标记集合
//                                defaultIdList.add(threeLevelId);
//                            } else {
//                                threeLevelTotal = threeLevelTotal.add(new BigDecimal(threeLevelCost));
//                            }
                        }
                        //填了三级预算但三级预算总和与二级不相等
                        if (threeLevelTotal.compareTo(BigDecimal.ZERO) != 0
                                && threeLevelTotal.compareTo(new BigDecimal(twoLevelCost)) != 0) {
                            message.append(MapUtil.getStr(collect, twoLevelId)).append(",");
//                            throw new BusinessException(DefaultResponseCode.THREE_LEVEL_BUDGET_SHOULD_EQUAL_TOTAL);
                        }
                    }
                    //一级二级都未填
//                    if (StrUtil.isBlank(oneLevelCost) && !flag) {
//                        throw new BusinessException(DefaultResponseCode.ONE_LEVEL_COST_CAN_NOT_EMPTY);
//                    }
                    //存在三级二级未填
//                    if (res && !flag) {
//                        throw new BusinessException(DefaultResponseCode.TWO_LEVEL_COST_CAN_NOT_EMPTY);
//                    }
                    //一级预算赋值
                    if (StrUtil.isNotBlank(oneLevelCost)) {
                        oneLevelItem = oneLevelItem.add(new BigDecimal(oneLevelCost));
                    } else {
                        oneLevelItem = oneLevelAmount;
                    }
                    //存在二级填写并二级总预算不等于一级填写的预算
                    if (oneLevelAmount.compareTo(BigDecimal.ZERO) != 0 && oneLevelItem.compareTo(oneLevelAmount) != 0) {
                        message.append(MapUtil.getStr(collect, decomposition.getId())).append(",");
//                        throw new BusinessException(DefaultResponseCode.BUDGET_SHOULD_EQUAL);
                    }
                } else {
                    //未存在下级分类必填
                    if (StrUtil.isBlank(oneLevelCost)) {
                        throw new BusinessException(DefaultResponseCode.ONE_LEVEL_COST_CAN_NOT_EMPTY);
                    }
                    oneLevelItem = new BigDecimal(oneLevelCost);
                }
                //一级和二级分类预算都为空
                if (StrUtil.isBlank(oneLevelCost) && oneLevelAmount.compareTo(BigDecimal.ZERO) == 0) {
                    throw new BusinessException(DefaultResponseCode.ONE_LEVEL_COST_CAN_NOT_EMPTY);
                }
                collectMap.put(decomposition.getId(), oneLevelItem.toString());
                oneLevelTotal = oneLevelTotal.add(oneLevelItem);
            }
        }
        //项目合同总额
        BigDecimal contractValue = project.getContractvalue();
        if ((oneLevelTotal.setScale(2, RoundingMode.HALF_UP)
                .compareTo(contractValue.multiply(new BigDecimal("10000")))) >= 0) {
            throw new BusinessException(DefaultResponseCode.ONE_LEVEL_BUDGET_SHOULD_NOT_MORE_THAN_CONTRACT_VALUE);
        }
        if (StrUtil.isNotBlank(message.toString())) {
            DefaultResponseCode.SHOW_POINT.setMessage(message.toString().substring(0, message.toString().lastIndexOf(",")));
            throw new BusinessException(DefaultResponseCode.SHOW_POINT);
        }
        //情况都满足处理均分除不尽情况
//        for (Map.Entry<Long, String> entry : collectMap.entrySet()) {
//            //预算
//            BigDecimal amount = new BigDecimal(entry.getValue());
//            //模板分项
//            Decomposition decomposition = decompositionMapper.selectById(entry.getKey());
//            //级别
//            Integer level = decomposition.getLevel();
//            if (level == 1) {
//                //一级 查询该一级下所有二级
//                List<Decomposition> twoLevelList = getDecompositionList(modelId, 2, decomposition.getId());
//                //记录二级总和
//                BigDecimal twoLevelTotal = BigDecimal.ZERO;
//                for (Decomposition item : twoLevelList) {
//                    twoLevelTotal = twoLevelTotal.add(new BigDecimal(MapUtil.getStr(collectMap, item.getId())));
//                }
//                if (CollUtil.isNotEmpty(twoLevelList) && twoLevelTotal.compareTo(amount) != 0) {
//                    //此处说明均分未除尽 将剩余部门给予其中一项
//                    Decomposition twoLevelDecomposition = twoLevelList.get(twoLevelList.size() - 1);
//                    collectMap.put(twoLevelDecomposition.getId(), amount
//                            .subtract(twoLevelTotal)
//                            .add(new BigDecimal(MapUtil.getStr(collectMap, twoLevelDecomposition.getId()))).toString());
//                }
//            } else if (level == 2) {
//                //二级 查询该二级下所有三级
//                List<Decomposition> threeLevelList = getDecompositionList(modelId, 3, decomposition.getId());
//                //记录三级总和
//                BigDecimal threeLevelTotal = BigDecimal.ZERO;
//                for (Decomposition item : threeLevelList) {
//                    threeLevelTotal = threeLevelTotal.add(new BigDecimal(MapUtil.getStr(collectMap, item.getId())));
//                }
//                if (CollUtil.isNotEmpty(threeLevelList) && threeLevelTotal.compareTo(amount) != 0) {
//                    //此处说明均分未除尽 将剩余部门给予其中一项
//                    Decomposition threeLevelDecomposition = threeLevelList.get(threeLevelList.size() - 1);
//                    collectMap.put(threeLevelDecomposition.getId(), amount
//                            .subtract(threeLevelTotal)
//                            .add(new BigDecimal(MapUtil.getStr(collectMap, threeLevelDecomposition.getId()))).toString());
//                }
//            }
//        }
        //修改项目模板
        project.setModelId(modelId);
        //预算金额
        project.setBudgetAmount(oneLevelTotal.divide(new BigDecimal("10000"), 6, RoundingMode.HALF_UP));
        projectMapper.updateById(project);
        for (Map.Entry<Long, String> entry : collectMap.entrySet()) {
            ProjectDecomposition projectDecomposition = new ProjectDecomposition();
            projectDecomposition.setProjectId(project.getProjectid());
            projectDecomposition.setDecompositionId(entry.getKey());
            projectDecomposition.setBudgetAmount(entry.getValue());
            //按照默认比例分配的项
//            if (defaultIdList.contains(entry.getKey())) {
//                projectDecomposition.setDefaultFlag(1);
//            }
            projectDecompositionMapper.insert(projectDecomposition);
        }
        //设置对应模板状态已使用
        Model model = modelMapper.selectById(modelId);
        if (model != null && model.getStatus() == 2) {
            model.setStatus(1);
            modelMapper.updateById(model);
        }
    }

    /**
     * 获取模板细分项列表
     * @param modelId 模板id
     * @param level 级别
     * @param parentId 上级id
     * @return List
     */
    public List<Decomposition> getDecompositionList(Long modelId, Integer level, Long parentId) {
        return decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", modelId)
                .eq("level", level)
                .eq("parent_id", parentId));
    }

    @Override
    public void confirm(String projectid) {
        //查询项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //查询项目的细化分解记录
        List<ProjectDecomposition> projectDecompositionList
                = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid));
        if (CollUtil.isEmpty(projectDecompositionList)) {
            throw new BusinessException(DefaultResponseCode.THIS_PROJECT_NOT_DECOMPOSITION);
        }
        //新增一条项目确认记录
        ProjectComplete complete = new ProjectComplete();
        complete.setProjectId(projectid);
        //成本系统类型
        complete.setSystemType(2);
        //新建确认项目状态为进行中
        complete.setState(1);
        projectCompleteMapper.insert(complete);
    }

    @Override
    public MonthlyReportVO getMonthlyReportTitleList(String projectid) {
        MonthlyReportVO reportVO = new MonthlyReportVO();
        //默认隐藏
        reportVO.setShowReport(0);
        //返回VO
        List<MonthlyReportTitleVO> voList = new ArrayList<>();
        //项目月报列表
        List<ProjectMonthlyReport> monthlyReportList
                = projectMonthlyReportMapper.selectList(new QueryWrapper<ProjectMonthlyReport>()
                .eq("project_id", projectid));
        //根据时间分组收集map
        Map<Integer, List<ProjectMonthlyReport>> collectMap
                = monthlyReportList.stream().collect(Collectors.groupingBy(ProjectMonthlyReport::getMonth));
        //项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project != null) {
            //当前月份
            int monthValue = LocalDate.now().getMonthValue();
            if (CollUtil.isEmpty(collectMap) || CollUtil.isEmpty(collectMap.get(monthValue))) {
                //当月不存在上报记录 查询模板的细化分项列表
                List<Decomposition> decompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("model_id", project.getModelId()));
                List<Long> collect
                        = decompositionList.stream().map(Decomposition::getRecorderRoleId).filter(Objects::nonNull).collect(Collectors.toList());
                //当前用户的角色列表
                List<Long> roleIds = roleIds();
                roleIds.forEach(c -> {
                    if (collect.contains(c)) {
                        //拥有记录员角色的用户
                        reportVO.setShowReport(1);
                    }
                });
                if (isRoot()) {
                    //root用户
                    reportVO.setShowReport(1);
                }
            }
        }
        for (Map.Entry<Integer, List<ProjectMonthlyReport>> entry : collectMap.entrySet()) {
            MonthlyReportTitleVO vo = new MonthlyReportTitleVO();
            vo.setMonth(entry.getKey());
            //默认0
            vo.setNeedVerify(0);
            //月份对应上报列表
            List<ProjectMonthlyReport> reportList = entry.getValue();
            for (ProjectMonthlyReport report : reportList) {
                //判断当前用户是否需要审评/修改
                if (isVerify(report.getProjectDecompositionId(), report.getState())) {
                    vo.setNeedVerify(1);
                }
            }
            voList.add(vo);
        }
        reportVO.setMonthTitleList(voList);
        return reportVO;
    }

    @Override
    public ProjectSpecifyVO specifyList(String projectid, Integer type, Integer month) {
        //返回VO
        ProjectSpecifyVO specifyVO = new ProjectSpecifyVO();
        //默认隐藏确认审评按钮
        specifyVO.setShowVerify(0);
        //默认隐藏确认修改
        specifyVO.setShowConfirm(0);
        //项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //细化分解项列表
        List<OneLevelSpecifyVO> oneLevelSpecifyList = new ArrayList<>();
        //查询所有一级分项
        List<Decomposition> oneLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", project.getModelId())
                .eq("level", 1));
        //查询项目细化项列表
        List<ProjectDecomposition> projectDecompositionList
                = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid));
        //配合前端赋予默认id
        AtomicLong id = new AtomicLong(1);
        oneLevelList.forEach(oneLevel -> {
            OneLevelSpecifyVO oneLevelSpecifyVO = new OneLevelSpecifyVO();
            oneLevelSpecifyVO.setOneLevelName(oneLevel.getOneLevelName());
            //拷贝值
            copy(projectDecompositionList, specifyVO, oneLevelSpecifyVO, projectid, oneLevel.getId(), month);
            //月报id
            if (REPORT_ID != null) {
                oneLevelSpecifyVO.setId(REPORT_ID);
            } else {
                oneLevelSpecifyVO.setId(id.get());
                id.getAndAdd(1);
            }
            //二级列表
            List<TwoLevelSpecifyVO> twoLevelSpecifyList = new ArrayList<>();
            //查询对应二级分项
            List<Decomposition> twoLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                    .eq("model_id", project.getModelId())
                    .eq("parent_id", oneLevel.getId())
                    .eq("level", 2));
            twoLevelList.forEach(twoLevel -> {
                TwoLevelSpecifyVO twoLevelSpecifyVO = new TwoLevelSpecifyVO();
                twoLevelSpecifyVO.setTwoLevelName(twoLevel.getTwoLevelName());
                //拷贝值
                copy(projectDecompositionList, specifyVO, twoLevelSpecifyVO, projectid, twoLevel.getId(), month);
                //月报id
                if (REPORT_ID != null) {
                    twoLevelSpecifyVO.setId(REPORT_ID);
                } else {
                    twoLevelSpecifyVO.setId(id.get());
                    id.getAndAdd(1);
                }
                //三级列表
                List<ThreeLevelSpecifyVO> threeLevelSpecifyList = new ArrayList<>();
                //查询对应三级分项
                List<Decomposition> threeLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("model_id", project.getModelId())
                        .eq("parent_id", twoLevel.getId())
                        .eq("level", 3));
                threeLevelList.forEach(threeLevel -> {
                    ThreeLevelSpecifyVO threeLevelSpecifyVO = new ThreeLevelSpecifyVO();
                    threeLevelSpecifyVO.setThreeLevelName(threeLevel.getThreeLevelName());
                    //拷贝值
                    copy(projectDecompositionList, specifyVO, threeLevelSpecifyVO, projectid, threeLevel.getId(), month);
                    //月报id
                    if (REPORT_ID != null) {
                        threeLevelSpecifyVO.setId(REPORT_ID);
                    } else {
                        threeLevelSpecifyVO.setId(id.get());
                        id.getAndAdd(1);
                    }
                    threeLevelSpecifyList.add(threeLevelSpecifyVO);
                });
                twoLevelSpecifyVO.setModelList(threeLevelSpecifyList);
                twoLevelSpecifyList.add(twoLevelSpecifyVO);
            });
            oneLevelSpecifyVO.setModelList(twoLevelSpecifyList);
            oneLevelSpecifyList.add(oneLevelSpecifyVO);
        });
        //选用列表标题
        List<String> selectList;
        if (month == null) {
            //汇总
            selectList = ProjectSpecifyTitleConstant.totalTitleList;
        } else {
            if (type == 1) {
                //进行中项目
                selectList = ProjectSpecifyTitleConstant.monthTitleListOfProgress;
            } else {
                //历史项目
                selectList = ProjectSpecifyTitleConstant.monthTitleListOfHistory;
            }
        }
        specifyVO.setSpecifyList(oneLevelSpecifyList);
        //列表标题
        List<TitleVO> titleList = new ArrayList<>();
        selectList.forEach(c -> {
            TitleVO titleVO = new TitleVO();
            List<String> splitTrim = StrUtil.splitTrim(c, "-");
            titleVO.setTitle(splitTrim.get(0));
            titleVO.setParam(splitTrim.get(1));
            titleList.add(titleVO);
        });
        specifyVO.setTitleList(titleList);
        return specifyVO;
    }

    @Override
    public void verify(ProjectVerifyParam param) {
        if (CollUtil.isNotEmpty(param.getDataArray())) {
            for (VerifyParam verify : param.getDataArray()) {
                //未选择的不做修改
                if (verify.getResult() != 0) {
                    //查询对应月报记录
                    ProjectMonthlyReport monthlyReport = projectMonthlyReportMapper.selectById(verify.getMonthlyReportId());
                    if (monthlyReport != null) {
                        monthlyReport.setState(verify.getResult());
                        projectMonthlyReportMapper.updateById(monthlyReport);
                    }
                }
            }
        }
    }

    @Override
    public List<OneLevelSpecifyVO> getReportModel(String projectid) {
        List<OneLevelSpecifyVO> oneLevelSpecifyList = new ArrayList<>();
        //项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //当前月份
        int monthValue = LocalDate.now().getMonthValue();
        //当月上报列表
        List<ProjectMonthlyReport> monthlyReports = projectMonthlyReportMapper.selectList(new QueryWrapper<ProjectMonthlyReport>()
                .eq("project_id", projectid)
                .eq("month", monthValue));
        //项目细化项id关联月报记录id map
        Map<Long, Long> collectMap = monthlyReports
                .stream().collect(Collectors.toMap(ProjectMonthlyReport::getProjectDecompositionId, ProjectMonthlyReport::getId));
        //查询项目细化项列表
        List<ProjectDecomposition> projectDecompositionList
                = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid));
        //查询所有一级分项
        List<Decomposition> oneLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", project.getModelId())
                .eq("level", 1));
        oneLevelList.forEach(oneLevel -> {
            OneLevelSpecifyVO oneLevelSpecifyVO = new OneLevelSpecifyVO();
            oneLevelSpecifyVO.setOneLevelName(oneLevel.getOneLevelName());
            //默认不需要上报
            oneLevelSpecifyVO.setNeedReport(0);
            //copy
            copyModel(projectDecompositionList, oneLevelSpecifyVO, projectid, collectMap,
                    oneLevel.getId(), oneLevel.getRecorderRoleId());
            //模板记录id
            oneLevelSpecifyVO.setId(MODEL_ID);
            //二级列表
            List<TwoLevelSpecifyVO> twoLevelSpecifyList = new ArrayList<>();
            //查询对应二级分项
            List<Decomposition> twoLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                    .eq("model_id", project.getModelId())
                    .eq("parent_id", oneLevel.getId())
                    .eq("level", 2));
            twoLevelList.forEach(twoLevel -> {
                TwoLevelSpecifyVO twoLevelSpecifyVO = new TwoLevelSpecifyVO();
                twoLevelSpecifyVO.setTwoLevelName(twoLevel.getTwoLevelName());
                //copy
                copyModel(projectDecompositionList, twoLevelSpecifyVO, projectid, collectMap,
                        twoLevel.getId(), twoLevel.getRecorderRoleId());
                //模板记录id
                twoLevelSpecifyVO.setId(MODEL_ID);
                //三级列表
                List<ThreeLevelSpecifyVO> threeLevelSpecifyList = new ArrayList<>();
                //查询对应三级分项
                List<Decomposition> threeLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("model_id", project.getModelId())
                        .eq("parent_id", twoLevel.getId())
                        .eq("level", 3));
                threeLevelList.forEach(threeLevel -> {
                    ThreeLevelSpecifyVO threeLevelSpecifyVO = new ThreeLevelSpecifyVO();
                    threeLevelSpecifyVO.setThreeLevelName(threeLevel.getThreeLevelName());
                    //copy
                    copyModel(projectDecompositionList, threeLevelSpecifyVO, projectid, collectMap,
                            threeLevel.getId(), threeLevel.getRecorderRoleId());
                    //模板记录id
                    threeLevelSpecifyVO.setId(MODEL_ID);
                    threeLevelSpecifyList.add(threeLevelSpecifyVO);
                });
                twoLevelSpecifyVO.setModelList(threeLevelSpecifyList);
                twoLevelSpecifyList.add(twoLevelSpecifyVO);
            });
            oneLevelSpecifyVO.setModelList(twoLevelSpecifyList);
            oneLevelSpecifyList.add(oneLevelSpecifyVO);
        });
        return oneLevelSpecifyList;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void addMonthlyReport(ProjectMonthlyReportParam param) {
        //项目id
        String projectid = param.getProjectid();
        //项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //当前时间
        LocalDate now = LocalDate.now();
//        //项目开工日期
//        LocalDate startDate = project.getStartdate();
//        //项目工期
//        String period = project.getPeriod();
//        //项目结束日期
//        LocalDate endDate = startDate.plusMonths(Integer.parseInt(period));
//        if (now.compareTo(endDate) > 0) {
//            throw new BusinessException(DefaultResponseCode.PROJECT_IS_TIME_OUT);
//        }
        //项目模板
        Model model = modelMapper.selectById(project.getModelId());
        if (model == null) {
            throw new BusinessException(DefaultResponseCode.MODEL_IS_NOT_EXIST);
        }
        //未到上报日期
//        if (now.getDayOfMonth() < model.getStartTime()) {
//            throw new BusinessException(DefaultResponseCode.REPORT_TIME_NOT_ARRIVED);
//        }
        //超过上报日期
//        if (now.getDayOfMonth() > model.getEndTime()) {
//            throw new BusinessException(DefaultResponseCode.REPORT_TIME_OUT);
//        }
        //上报的数据集合 key-id value-amount
        Map<Long, MonthlyReportParam> collectMap = param.getDataArray().stream()
                .filter(item -> item.getNeedReport().equals(1))
                .collect(Collectors.toMap(MonthlyReportParam::getId, Function.identity()));
        //项目成本分解项列表
        List<ProjectDecomposition> projectDecompositionList = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid));
        //本月上报记录列表
        List<ProjectMonthlyReport> monthlyReportList = projectMonthlyReportMapper.selectList(new QueryWrapper<ProjectMonthlyReport>()
                .eq("project_id", projectid)
                .eq("month", now.getMonthValue()));
        if (CollUtil.isEmpty(monthlyReportList)) {
            //本月无上报记录
            projectDecompositionList.forEach(c -> {
                ProjectMonthlyReport report = new ProjectMonthlyReport();
                report.setProjectId(projectid);
                report.setProjectDecompositionId(c.getId());
                report.setMonth(now.getMonthValue());
                //上报数据
                MonthlyReportParam reportParam = collectMap.get(c.getId());
                if (reportParam != null) {
                    //该项下级分项id列表
                    List<Long> idList = judgeReturnList(projectDecompositionList, c.getId());
                    if (CollUtil.isEmpty(idList)) {
                        //表明此项下无子级分项
                        idList.add(c.getId());
                    }
                    reportJudge(collectMap, report, idList, reportParam);
                    report.setState(0);
                    report.setUserId(MyThreadLocal.getUserId());
                } else {
                    //表明此项不属于该角色上报
                    report.setState(3);
                }
                projectMonthlyReportMapper.insert(report);
            });
        } else {
            //本月存在上报记录
            monthlyReportList.forEach(report -> {
                //该项下级分项id列表
                List<Long> idList = judgeReturnList(projectDecompositionList, report.getProjectDecompositionId());
                if (CollUtil.isEmpty(idList)) {
                    //表明此项下无子级分项
                    idList.add(report.getProjectDecompositionId());
                }
                //上报数据
                MonthlyReportParam reportParam = collectMap.get(report.getId());
                if (reportParam != null) {
                    reportJudge(collectMap, report, idList, reportParam);
                    report.setState(0);
                    report.setUserId(MyThreadLocal.getUserId());
                    projectMonthlyReportMapper.updateById(report);
                }
            });
        }
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void updateMonthlyReport(ProjectMonthlyReportParam param) {
        //项目id
        String projectid = param.getProjectid();
        //项目信息
        Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                .eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        List<MonthlyReportParam> dataArray = param.getDataArray();
        dataArray.forEach(c -> {
            //上报费用
            BigDecimal amount = c.getAmount();
            if (amount == null && c.getNeedReport().equals(1)) {
                throw new BusinessException(DefaultResponseCode.REPORT_AMOUNT_MISS);
            }
            //余额
            BigDecimal over = new BigDecimal(c.getOver());
            if (amount != null && c.getNeedReport().equals(1) && amount.compareTo(over) > 0) {
                //上报金额比余额大
                throw new BusinessException(DefaultResponseCode.REPORT_AMOUNT_MORE);
            }
        });
        //项目细分项id-key，本月花费cost-value map
        Map<Long, MonthlyReportParam> collectMap = new HashMap<>();
        dataArray.forEach(c -> {
            ProjectMonthlyReport report = projectMonthlyReportMapper.selectById(c.getId());
            if (report != null) {
                collectMap.put(report.getProjectDecompositionId(), c);
            }
        });
        //查询项目细化项列表
        List<ProjectDecomposition> projectDecompositionList
                = projectDecompositionMapper.selectList(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid));
        projectDecompositionList.forEach(projectDecomposition -> {
            MonthlyReportParam reportParam = collectMap.get(projectDecomposition.getId());
            //该分项上报金额
            BigDecimal amount = reportParam.getAmount();
            //该分项是否需要上报
            Integer report = reportParam.getNeedReport();
            //该项下级分项id列表
            List<Long> idList = judgeReturnList(projectDecompositionList, projectDecomposition.getId());
            if (CollUtil.isEmpty(idList)) {
                //表明此项下无子级分项
                idList.add(projectDecomposition.getId());
            }
            //该分项预算总计
            BigDecimal total = BigDecimal.ZERO;
            for (Long id : idList) {
                MonthlyReportParam addItem = collectMap.get(id);
                if (addItem.getAmount() != null) {
                    total = total.add(addItem.getAmount());
                }
            }
            if (amount != null && report.equals(1) && total.compareTo(amount) != 0 ) {
                throw new BusinessException(DefaultResponseCode.SUP_ITEM_SHOULD_EQUAL_SUB_ITEM);
            }
        });
        //通过所有校验
        dataArray.forEach(c -> {
            Integer needReport = c.getNeedReport();
            if (needReport == 1) {
                //需要修改项
                ProjectMonthlyReport report = projectMonthlyReportMapper.selectById(c.getId());
                if (report != null) {
                    report.setState(0);
                    report.setAmount(c.getAmount());
                    if (report.getUserId() == null) {
                        //说明此项是未上报项
                        report.setUserId(MyThreadLocal.getUserId());
                    }
                    projectMonthlyReportMapper.updateById(report);
                }
            }
        });
    }

    @Override
    public void complete(String projectid) {
        Project project = projectMapper.selectOne(new QueryWrapper<Project>().
                eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        //查询项目完成状态记录
        ProjectComplete complete = projectCompleteMapper.selectOne(new QueryWrapper<ProjectComplete>()
                .eq("project_id", projectid)
                .eq("system_type", 2)
                .last("LIMIT 1"));
        //修改状态为已完成
        complete.setState(2);
        projectCompleteMapper.updateById(complete);
    }

    @Override
    public ProjectDetailVO detail(String projectid) {
        Project project = projectMapper.selectOne(new QueryWrapper<Project>().
                eq("projectid", projectid)
                .last("LIMIT 1"));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        }
        ProjectDetailVO detailVO = new ProjectDetailVO();
        BeanUtil.copyProperties(project, detailVO);
        //所属事业部
        Organization organization = organizationMapper.selectById(project.getDeptid());
        if (organization != null) {
            detailVO.setDeptname(organization.getOrganizationName());
        }
        detailVO.setUserId(project.getUserid());
        //判断项目状态
        ProjectComplete complete = projectCompleteMapper.selectOne(new QueryWrapper<ProjectComplete>()
                .eq("project_id", projectid)
                .eq("system_type", 2)
                .last("LIMIT 1"));
        if (complete == null) {
            //新项目
            detailVO.setState(0);
        } else if (complete.getState() == 1) {
            //进行中项目
            detailVO.setState(1);
        } else {
            //历史项目
            detailVO.setState(2);
        }
        return detailVO;
    }

    //模板记录id
    public static Long MODEL_ID;

    //拷贝模板
    private void copyModel(List<ProjectDecomposition> projectDecompositionList, BaseSpecifyVO vo,
                           String projectid, Map<Long, Long> collectMap, Long decompositionId, Long recorderId) {
        //查询项目细化分解项
        ProjectDecomposition projectDecomposition = projectDecompositionMapper.selectOne(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid)
                .eq("decomposition_id", decompositionId)
                .last("LIMIT 1"));
        if (projectDecomposition != null) {
            //默认不需要上报
            vo.setNeedReport(0);
            if (CollUtil.isEmpty(collectMap)) {
                //本月该项目未被上报过 生成月报记录
                MODEL_ID = projectDecomposition.getId();
            } else {
                //本月该项目被上报过
                MODEL_ID = collectMap.get(projectDecomposition.getId());
            }
            vo.setBudgetAmount(projectDecomposition.getBudgetAmount());
            List<Long> idList = judgeReturnList(projectDecompositionList, projectDecomposition.getId());
            if (CollUtil.isEmpty(idList)) {
                //表明该分类下无子分类
                idList.add(projectDecomposition.getId());
            }
            //已使用
            BigDecimal isUse = isUse(idList);
            vo.setOver(new BigDecimal(projectDecomposition.getBudgetAmount()).subtract(isUse));
            vo.setIsUse(isUse);
            //root用户
            if (isRoot()) {
                vo.setNeedReport(1);
            }
            //当前用户的角色列表
            List<Long> roleIds = roleIds();
            if (recorderId != null && roleIds.contains(recorderId)) {
                vo.setNeedReport(1);
            }
        }
    }

    //记录月报id
    public static Long REPORT_ID;

    //拷贝成本细化项值
    private void copy(List<ProjectDecomposition> projectDecompositionList,
                      ProjectSpecifyVO vo, BaseSpecifyVO specifyVO, String projectid,
                      Long id, Integer month) {
        //查询细化分项
        ProjectDecomposition projectDecomposition = projectDecompositionMapper.selectOne(new QueryWrapper<ProjectDecomposition>()
                .eq("project_id", projectid)
                .eq("decomposition_id", id)
                .last("LIMIT 1"));
        if (projectDecomposition != null) {
            specifyVO.setBudgetAmount(projectDecomposition.getBudgetAmount());
            //需要计算已使用费用的分项id列表
            List<Long> idList = judgeReturnList(projectDecompositionList, projectDecomposition.getId());
            if (CollUtil.isEmpty(idList)) {
                //表明该分类下无子分类
                idList.add(projectDecomposition.getId());
            }
            //计算已使用
            BigDecimal isUse = isUse(idList);
            //余额
            specifyVO.setOver(new BigDecimal(projectDecomposition.getBudgetAmount()).subtract(isUse));
            //已使用
            specifyVO.setIsUse(isUse);
            //查询条件
            QueryWrapper<ProjectMonthlyReport> query = new QueryWrapper<>();
            query.eq("project_decomposition_id", projectDecomposition.getId());
            query.orderByDesc("month");
            query.last("LIMIT 1");
            if (month != null) {
                query.eq("month", month);
            }
            //上报人
            ProjectMonthlyReport report = projectMonthlyReportMapper.selectOne(query);
            if (report == null) {
                REPORT_ID = null;
            } else {
                REPORT_ID = report.getId();
                //上报人
                User user = userMapper.selectById(report.getUserId());
                if (user != null) {
                    specifyVO.setReportPer(user.getUsername());
                    specifyVO.setReportTime(report.getCreateTime());
                }
                specifyVO.setState(report.getState() == 3 ? 0 : report.getState());
                if (month != null) {
                    //月花费
                    specifyVO.setMonthCost(report.getAmount());
                    //默认不需要操作
                    specifyVO.setNeedReport(0);
                    specifyVO.setNeedVerify(0);
                    //判断当前用户是否需要审评/修改
                    boolean verify = isVerify(report.getProjectDecompositionId(), report.getState());
                    if (verify) {
                        if (duties) {
                            //记录员
                            specifyVO.setNeedReport(1);
                            vo.setShowConfirm(1);
                        } else {
                            //审评员
                            specifyVO.setNeedVerify(1);
                            vo.setShowVerify(1);
                        }
                    }
                }
            }
        }
    }

    //月报上报判断
    private void reportJudge(Map<Long, MonthlyReportParam> collectMap, ProjectMonthlyReport report, List<Long> idList, MonthlyReportParam reportParam) {
        //上报金额
        BigDecimal amount = reportParam.getAmount();
        if (amount == null) {
            throw new BusinessException(DefaultResponseCode.REPORT_AMOUNT_MISS);
        }
        //余额
        BigDecimal over = new BigDecimal(reportParam.getOver());
        if (amount.compareTo(over) > 0) {
            //上报金额比余额大
            throw new BusinessException(DefaultResponseCode.REPORT_AMOUNT_MORE);
        }
        //该分项预算总计
        BigDecimal total = BigDecimal.ZERO;
        for (Long id : idList) {
            MonthlyReportParam monthlyReportParam = collectMap.get(id);
            if (monthlyReportParam != null && monthlyReportParam.getAmount() != null) {
                total = total.add(monthlyReportParam.getAmount());
            }
        }
        if (total.compareTo(amount) != 0) {
            throw new BusinessException(DefaultResponseCode.SUP_ITEM_SHOULD_EQUAL_SUB_ITEM);
        }
        report.setAmount(amount.stripTrailingZeros());
    }

    //计算已使用
    private BigDecimal isUse(List<Long> idList) {
        //已使用
        BigDecimal isUse = BigDecimal.ZERO;
        //查询已使用
        for (Long id : idList) {
            List<ProjectMonthlyReport> monthlyReportList =
                    projectMonthlyReportMapper.selectList(new QueryWrapper<ProjectMonthlyReport>()
                            .eq("state", 1) //审核通过
                            .eq("project_decomposition_id", id));
            if (CollUtil.isNotEmpty(monthlyReportList)) {
                for (ProjectMonthlyReport report : monthlyReportList) {
                    isUse = isUse.add(report.getAmount());
                }
            }
        }
        return isUse;
    }

    //判断当前登录用户是否是root角色
    private boolean isRoot() {
        Long userId = MyThreadLocal.getUserId();
        List<String> roleNameList = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                .eq("user_id", userId)).stream().map(c -> {
            Role role = roleMapper.selectById(c.getRoleId());
            if (role != null) {
                return role.getRoleName();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        return roleNameList.contains("root");
    }

    //身份标识
    public static boolean duties;

    //判断当前用户是否需要审评或修改
    private boolean isVerify(Long projectDecompositionId, Integer state) {
        boolean flag = false;
        //用户id
        Long userId = MyThreadLocal.getUserId();
        //项目成本关联项id
        ProjectDecomposition projectDecomposition
                = projectDecompositionMapper.selectById(projectDecompositionId);
        if (projectDecomposition != null) {
            //成本分解项
            Decomposition decomposition
                    = decompositionMapper.selectById(projectDecomposition.getDecompositionId());
            if (decomposition != null) {
                //记录员id
                Long recorderRoleId = decomposition.getRecorderRoleId();
                //查询记录员角色对应用户列表
                List<Long> recorderUserIds = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                        .eq("role_id", recorderRoleId))
                        .stream().map(UserRole::getUserId).collect(Collectors.toList());
                //审核员id
                Long auditorRoleId = decomposition.getAuditorRoleId();
                //查询审核员角色对应用户列表
                List<Long> auditorUserIds = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                        .eq("role_id", auditorRoleId))
                        .stream().map(UserRole::getUserId).collect(Collectors.toList());
                //当前用户为对应的记录员或未上报且对应上报项审评未通过需修改，root角色
                if ((state == 2 || state == 3) && ((CollUtil.isNotEmpty(recorderUserIds) && recorderUserIds.contains(userId)) || isRoot())) {
                    flag = true;
                    //记录员
                    duties = true;
                }
                //当前用户为对应的审核员且对应上报项未审评，root角色
                if (state == 0 && (CollUtil.isNotEmpty(auditorUserIds) && (auditorUserIds.contains(userId)) || isRoot())) {
                    flag = true;
                    //审核员
                    duties = false;
                }
            }
        }
        return flag;
    }

    //查询当前用户的角色列表
    private List<Long> roleIds() {
        Long userId = MyThreadLocal.getUserId();
        return userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId))
                .stream().map(UserRole::getRoleId).collect(Collectors.toList());
    }

    //一级二级或三级标记
    public static Integer LEVEL;

    //根据项目细化项是一级、二级或者三级返回数据，一级返回对应没有三级分项的二级以及有三级分项的所有三级细化项id集合，二级返回对应所有三级的细化项id集合
    private List<Long> judgeReturnList(List<ProjectDecomposition> projectDecompositionList, Long id) {
        List<Long> idList = new ArrayList<>();
        //key-模板细化项id value-项目细化项id
        Map<Long, Long> collectMap = projectDecompositionList
                .stream().collect(Collectors.toMap(ProjectDecomposition::getDecompositionId, ProjectDecomposition::getId));
        ProjectDecomposition projectDecomposition = projectDecompositionMapper.selectById(id);
        if (projectDecomposition != null) {
            Decomposition decomposition = decompositionMapper.selectById(projectDecomposition.getDecompositionId());
            if (decomposition != null) {
                //查询条件
                QueryWrapper<Decomposition> query = new QueryWrapper<>();
                query.eq("model_id", decomposition.getModelId());
                //级别
                Integer level = decomposition.getLevel();
                if (level == 1) {
                    LEVEL = 1;
                    //一级 查询对应所有二级列表
                    query.eq("level", 2)
                            .eq("parent_id", decomposition.getId());
                    List<Decomposition> decompositionList = decompositionMapper.selectList(query);
                    decompositionList.forEach(c -> {
                        //查询三级
                        List<Decomposition> threeLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                                .eq("level", 3)
                                .eq("parent_id", c.getId()));
                        if (CollUtil.isNotEmpty(threeLevelList)) {
                            for (Decomposition threeLevel : threeLevelList) {
                                idList.add(MapUtil.getLong(collectMap, threeLevel.getId()));
                            }
                        } else {
                            idList.add(MapUtil.getLong(collectMap, c.getId()));
                        }
                    });
                } else if (level == 2) {
                    LEVEL = 2;
                    //二级
                    query.eq("level", 3)
                            .eq("parent_id", decomposition.getId());
                    List<Decomposition> decompositionList = decompositionMapper.selectList(query);
                    decompositionList.forEach(c -> {
                        idList.add(MapUtil.getLong(collectMap, c.getId()));
                    });
                } else {
                    LEVEL = 3;
                }
            }
        }
        return idList;
    }

    @Override
    public Project getByProjectId(String projectId) {
        QueryWrapper<Project> qw = new QueryWrapper<>();
        qw.eq("projectid", projectId);
        return this.getOne(qw, false);
    }
}
