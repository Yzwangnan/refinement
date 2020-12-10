package com.refinement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.data.param.ModelAddParam;
import com.refinement.data.param.OneLevelDecompositionParam;
import com.refinement.data.param.ThreeLevelDecompositionParam;
import com.refinement.data.param.TwoLevelDecompositionParam;
import com.refinement.data.vo.*;
import com.refinement.entity.*;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.PageResult;
import com.refinement.mapper.*;
import com.refinement.service.ModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Resource
    private ModelMapper modelMapper;

    @Resource
    private DecompositionMapper decompositionMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectDecompositionMapper projectDecompositionMapper;

    @Resource
    private ProjectCompleteMapper projectCompleteMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public ModelPageVO getModelList(Integer type, Integer page, Integer size) {
        ModelPageVO pageVO = new ModelPageVO();
        IPage<Model> iPage = new Page<>(page, size);
        IPage<Model> pageList = modelMapper.selectPage(iPage, null);
        //模板列表
        List<Model> models = pageList.getRecords();
        List<ModelVO> modelList = models.stream().map(c -> {
            ModelVO vo = new ModelVO();
            BeanUtil.copyProperties(c, vo);
            return vo;
        }).collect(Collectors.toList());
        pageVO.setModelList(modelList);
        //分页信息
        PageResult pageInfo = new PageResult();
        pageInfo.setCurrent(page);
        pageInfo.setSize(modelList.size());
        pageInfo.setTotal(pageList.getTotal());
        pageVO.setPageInfo(pageInfo);
        return pageVO;
    }

    @Override
    public ModelProjectDetailVO detail(Long id, String projectid) {
        ModelProjectDetailVO detailVO = new ModelProjectDetailVO();
        //查询项目
        if (StrUtil.isNotBlank(projectid)) {
            Project project = projectMapper.selectOne(new QueryWrapper<Project>()
                    .eq("projectid", projectid)
                    .last("LIMIT 1"));
            if (project != null && project.getModelId() != null) {
                detailVO.setModelId(project.getModelId());
            }
        }
        List<OneLevelDecompositionVO> oneLevelDecompositionList = new ArrayList<>();
        //查询所有一级分项
        List<Decomposition> oneLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", id)
                .eq("level", 1));
        oneLevelList.forEach(oneLevel -> {
            OneLevelDecompositionVO oneLevelDecompositionVO = new OneLevelDecompositionVO();
            BeanUtil.copyProperties(oneLevel, oneLevelDecompositionVO);
            oneLevelDecompositionVO.setRecorderName(getRoleName(oneLevel.getRecorderRoleId()));
            oneLevelDecompositionVO.setAuditorName(getRoleName(oneLevel.getAuditorRoleId()));
            //获取对应细化分项
            ProjectDecomposition oneLevelDecomposition = getProjectDecomposition(projectid, oneLevel.getId());
            if (oneLevelDecomposition != null) {
                //预算金额
                oneLevelDecompositionVO.setBudgetAmount(oneLevelDecomposition.getBudgetAmount());
                //默认按模板细分标记
//                oneLevelDecompositionVO.setDefaultFlag(oneLevelDecomposition.getDefaultFlag());
            }
            //查询对应二级分项
            List<Decomposition> twoLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                    .eq("model_id", id)
                    .eq("parent_id", oneLevel.getId())
                    .eq("level", 2));
            //二级模板列表
            List<TwoLevelDecompositionVO> twoLevelDecompositionList = new ArrayList<>();
            twoLevelList.forEach(twoLevel -> {
                TwoLevelDecompositionVO twoLevelDecompositionVO = new TwoLevelDecompositionVO();
                BeanUtil.copyProperties(twoLevel, twoLevelDecompositionVO);
                twoLevelDecompositionVO.setRecorderName(getRoleName(twoLevel.getRecorderRoleId()));
                twoLevelDecompositionVO.setAuditorName(getRoleName(twoLevel.getAuditorRoleId()));
                //获取对应细化分项
                ProjectDecomposition twoLevelDecomposition = getProjectDecomposition(projectid, twoLevel.getId());
                if (twoLevelDecomposition != null) {
                    //预算金额
                    twoLevelDecompositionVO.setBudgetAmount(twoLevelDecomposition.getBudgetAmount());
                    //默认按模板细分标记
//                    twoLevelDecompositionVO.setDefaultFlag(twoLevelDecomposition.getDefaultFlag());
                }
                //查询对应三级分项
                List<Decomposition> threeLevelList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("model_id", id)
                        .eq("parent_id", twoLevel.getId())
                        .eq("level", 3));
                //三级模板列表
                List<ThreeLevelDecompositionVO> threeLevelDecompositionList = new ArrayList<>();
                threeLevelList.forEach(threeLevel -> {
                    ThreeLevelDecompositionVO threeLevelDecompositionVO = new ThreeLevelDecompositionVO();
                    BeanUtil.copyProperties(threeLevel, threeLevelDecompositionVO);
                    threeLevelDecompositionVO.setRecorderName(getRoleName(threeLevel.getRecorderRoleId()));
                    threeLevelDecompositionVO.setAuditorName(getRoleName(threeLevel.getAuditorRoleId()));
                    //获取对应细化分项
                    ProjectDecomposition threeLevelDecomposition = getProjectDecomposition(projectid, threeLevel.getId());
                    if (threeLevelDecomposition != null) {
                        //预算金额
                        threeLevelDecompositionVO.setBudgetAmount(threeLevelDecomposition.getBudgetAmount());
                        //默认按模板细分标记
//                        threeLevelDecompositionVO.setDefaultFlag(threeLevelDecomposition.getDefaultFlag());
                    }
                    threeLevelDecompositionList.add(threeLevelDecompositionVO);
                });
                twoLevelDecompositionVO.setModelList(threeLevelDecompositionList);
                twoLevelDecompositionList.add(twoLevelDecompositionVO);
            });
            oneLevelDecompositionVO.setModelList(twoLevelDecompositionList);
            oneLevelDecompositionList.add(oneLevelDecompositionVO);
        });
        detailVO.setModelList(oneLevelDecompositionList);
        return detailVO;
    }

    /**
     * 根据角色id获取角色名称
     * @param roleId 角色id
     * @return String
     */
    private String getRoleName(Long roleId) {
        Role role = roleMapper.selectById(roleId);
        if (role == null) {
            return null;
        }
        return role.getRoleName();
    }

    //获取细化分项
    private ProjectDecomposition getProjectDecomposition(String projectid, Long id) {
        if (StrUtil.isNotBlank(projectid)) {
            return projectDecompositionMapper.selectOne(new QueryWrapper<ProjectDecomposition>()
                    .eq("project_id", projectid)
                    .eq("decomposition_id", id)
                    .last("LIMIT 1"));
        }
        return null;
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void add(ModelAddParam param) {
        Model model = new Model();
        BeanUtil.copyProperties(param, model);
        modelMapper.insertAndReturnId(model);
        //模板id
        Long modelId = model.getId();
        //成本分项列表
        List<OneLevelDecompositionParam> dataArray = param.getDataArray();
        dataArray.forEach(oneLevel -> {
            if (StrUtil.isBlank(oneLevel.getOneLevelName())) {
                throw new BusinessException(DefaultResponseCode.ONE_LEVEL_NAME_CAN_NOT_EMPTY);
            }
            //一级分项
            Decomposition oneLevelDecomposition = new Decomposition();
            BeanUtil.copyProperties(oneLevel, oneLevelDecomposition);
            oneLevelDecomposition.setLevel(1);
            oneLevelDecomposition.setModelId(modelId);
            oneLevelDecomposition.setParentId(0L);
            decompositionMapper.insertAndReturnId(oneLevelDecomposition);
            //记录二级总预算比例
            AtomicReference<BigDecimal> twoLevelTotalScale = new AtomicReference<>(BigDecimal.ZERO);
            //记录除最后一项的累计
            BigDecimal twoTotalScale = BigDecimal.ZERO;
            for (int i = 0; i < oneLevel.getArray().size(); i++) {
                TwoLevelDecompositionParam twoLevel = oneLevel.getArray().get(i);
                if (StrUtil.isBlank(twoLevel.getTwoLevelName())) {
                    throw new BusinessException(DefaultResponseCode.TWO_LEVEL_NAME_CAN_NOT_EMPTY);
                }
                if (twoLevel.getRecorderRoleId() == null) {
                    throw new BusinessException(DefaultResponseCode.RECORDER_ROLE_ID_MISS);
                }
                if (twoLevel.getAuditorRoleId() == null) {
                    throw new BusinessException(DefaultResponseCode.AUDITOR_ROLE_ID_MISS);
                }
                if (twoLevel.getBudgetScale() != null) {
                    twoLevelTotalScale.accumulateAndGet(twoLevel.getBudgetScale(), BigDecimal::add);
                } else {
                    //按默认比例均分
                    if (i == oneLevel.getArray().size() - 1) {
                        //最后一项分摊剩余比例
                        twoLevel.setBudgetScale(new BigDecimal("100").subtract(twoTotalScale));
                    } else {
                        //均分比例
                        BigDecimal split = new BigDecimal("100")
                                .divide(new BigDecimal(oneLevel.getArray().size()), 2, RoundingMode.HALF_UP);
                        twoLevel.setBudgetScale(split);
                        twoTotalScale = twoTotalScale.add(split);
                    }
                }
                //二级分项
                Decomposition twoLevelDecomposition = new Decomposition();
                BeanUtil.copyProperties(twoLevel, twoLevelDecomposition);
                twoLevelDecomposition.setOneLevelName(oneLevelDecomposition.getOneLevelName());
                twoLevelDecomposition.setLevel(2);
                twoLevelDecomposition.setModelId(modelId);
                twoLevelDecomposition.setParentId(oneLevelDecomposition.getId());
                decompositionMapper.insertAndReturnId(twoLevelDecomposition);
                //记录三级总预算比例
                AtomicReference<BigDecimal> threeLevelTotalScale = new AtomicReference<>(BigDecimal.ZERO);
                //记录除最后一项的累计
                BigDecimal threeTotalScale = BigDecimal.ZERO;
                for (int j = 0; j < twoLevel.getArray().size(); j++) {
                    ThreeLevelDecompositionParam threeLevel = twoLevel.getArray().get(j);
                    if (StrUtil.isBlank(threeLevel.getThreeLevelName())) {
                        throw new BusinessException(DefaultResponseCode.THREE_LEVEL_NAME_CAN_NOT_EMPTY);
                    }
                    if (threeLevel.getBudgetScale() != null) {
                        threeLevelTotalScale.accumulateAndGet(threeLevel.getBudgetScale(), BigDecimal::add);
                    } else {
                        //按默认比例均分
                        if (j == twoLevel.getArray().size() - 1) {
                            //最后一项分摊剩余比例
                            threeLevel.setBudgetScale(new BigDecimal("100").subtract(threeTotalScale));
                        } else {
                            //均分比例
                            BigDecimal split = new BigDecimal("100")
                                    .divide(new BigDecimal(twoLevel.getArray().size()), 2, RoundingMode.HALF_UP)
                                    .stripTrailingZeros();
                            threeLevel.setBudgetScale(split);
                            threeTotalScale = threeTotalScale.add(split);
                        }
                    }
                    //三级分项
                    Decomposition threeLevelDecomposition = new Decomposition();
                    BeanUtil.copyProperties(threeLevel, threeLevelDecomposition);
                    threeLevelDecomposition.setOneLevelName(twoLevelDecomposition.getOneLevelName());
                    threeLevelDecomposition.setTwoLevelName(twoLevelDecomposition.getTwoLevelName());
                    threeLevelDecomposition.setLevel(3);
                    threeLevelDecomposition.setModelId(modelId);
                    threeLevelDecomposition.setParentId(twoLevelDecomposition.getId());
                    decompositionMapper.insert(threeLevelDecomposition);
                }
                if (threeLevelTotalScale.get().compareTo(BigDecimal.ZERO) != 0
                        && threeLevelTotalScale.get().compareTo(new BigDecimal("100")) != 0) {
                    throw new BusinessException(DefaultResponseCode.BUDGET_SCALE_NOT_EQUAL_HUNDRED);
                }
            }
            if (twoLevelTotalScale.get().compareTo(BigDecimal.ZERO) != 0
                    && twoLevelTotalScale.get().compareTo(new BigDecimal("100")) != 0) {
                throw new BusinessException(DefaultResponseCode.BUDGET_SCALE_NOT_EQUAL_HUNDRED);
            }
        });
    }

    @Transactional
    @Override
    public void delete(Long modelId) {
        //查询正在使用此模板的项目
        List<Project> projectList = projectMapper.selectList(new QueryWrapper<Project>()
                .eq("model_id", modelId));
        if (CollUtil.isNotEmpty(projectList)) {
            if (projectList.size() == 1) {
                Project project = projectList.get(0);
                //查询项目状态
                ProjectComplete complete = projectCompleteMapper.selectOne(new QueryWrapper<ProjectComplete>()
                        .eq("project_id", project.getProjectid())
                        .eq("system_type", 2)
                        .last("LIMIT 1"));
                if (complete == null) {
                    throw new BusinessException(DefaultResponseCode.MODEL_IS_USE_OF_NEW_PROJECT);
                }
            } else {
                throw new BusinessException(DefaultResponseCode.MODEL_IS_USE);
            }
        }
        modelMapper.deleteById(modelId);
        //删除对应成本分项
        decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                .eq("model_id", modelId))
                .forEach(c -> decompositionMapper.deleteById(c.getId()));
    }

    @Override
    public List<ModelVO> getModelList(Integer type) {
        //查询所有
        return modelMapper.selectList(null).stream().map(c -> {
            ModelVO vo = new ModelVO();
            BeanUtil.copyProperties(c, vo);
            if (type != null) {
                //查询模板下的项目数量
                vo.setNumber(projectMapper.selectCountByModel(type, c.getId()));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<?> computeDecomposition(BigDecimal budgetAmount, Long id) {
        Decomposition decomposition = decompositionMapper.selectById(id);
        if (decomposition != null) {
            // 级别
            Integer level = decomposition.getLevel();
            if (level == 1) {
                List<TwoLevelComputeVO> twoLevelComputeList = new ArrayList<>();
                // 查询一级下的所有二级
                List<Decomposition> twoLevelDecompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("level", 2)
                        .eq("parent_id", decomposition.getId()));
                twoLevelDecompositionList.forEach(twoLevel -> {
                    TwoLevelComputeVO twoLevelComputeVO = new TwoLevelComputeVO();
                    BeanUtil.copyProperties(twoLevel, twoLevelComputeVO);
                    // 每个二级项预算
                    AtomicReference<BigDecimal> amount = new AtomicReference<>(BigDecimal.ZERO);
                    if (StrUtil.isNotBlank(twoLevel.getBudgetScale())) {
                        // 比例预算
                        BigDecimal scaleAmount = budgetAmount.multiply(new BigDecimal(twoLevel.getBudgetScale())
                                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))
                                .stripTrailingZeros();
                        twoLevelComputeVO.setBudgetAmount(scaleAmount.toPlainString());
                        amount.accumulateAndGet(scaleAmount, BigDecimal::add);
                    }
                    // 查询二级下的所有三级
                    List<Decomposition> threeLevelDecompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                            .eq("level", 3)
                            .eq("parent_id", twoLevel.getId()));
                    List<ThreeLevelComputeVO> threeLevelComputeList = new ArrayList<>();
                    threeLevelDecompositionList.forEach(threeLevel -> {
                        ThreeLevelComputeVO threeLevelComputeVO = new ThreeLevelComputeVO();
                        BeanUtil.copyProperties(threeLevel, threeLevelComputeVO);
                        if (StrUtil.isNotBlank(threeLevel.getBudgetScale())) {
                            threeLevelComputeVO.setBudgetAmount(amount.get().multiply(new BigDecimal(threeLevel.getBudgetScale())
                                    .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))
                                    .stripTrailingZeros()
                                    .toPlainString());
                        }
                        threeLevelComputeList.add(threeLevelComputeVO);
                    });
                    twoLevelComputeVO.setComputeList(threeLevelComputeList);
                    twoLevelComputeList.add(twoLevelComputeVO);
                });
                return twoLevelComputeList;
            } else if (level == 2) {
                List<ThreeLevelComputeVO> threeLevelComputeList = new ArrayList<>();
                // 查询二级下的所有三级
                List<Decomposition> threeLevelDecompositionList = decompositionMapper.selectList(new QueryWrapper<Decomposition>()
                        .eq("level", 3)
                        .eq("parent_id", decomposition.getId()));
                threeLevelDecompositionList.forEach(threeLevel -> {
                    ThreeLevelComputeVO threeLevelComputeVO = new ThreeLevelComputeVO();
                    BeanUtil.copyProperties(threeLevel, threeLevelComputeVO);
                    if (StrUtil.isNotBlank(threeLevel.getBudgetScale())) {
                        threeLevelComputeVO.setBudgetAmount(budgetAmount.multiply(new BigDecimal(threeLevel.getBudgetScale())
                                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP))
                                .stripTrailingZeros()
                                .toPlainString());
                    }
                    threeLevelComputeList.add(threeLevelComputeVO);
                });
                return threeLevelComputeList;
            }
        }
        return new ArrayList<>();
    }
}
