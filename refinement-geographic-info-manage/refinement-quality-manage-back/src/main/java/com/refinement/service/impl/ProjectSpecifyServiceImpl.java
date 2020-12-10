package com.refinement.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.entity.Project;
import com.refinement.entity.ProjectSpecify;
import com.refinement.entity.SpecifyReport;
import com.refinement.group.SpecifyRe;
import com.refinement.mapper.ProjectMapper;
import com.refinement.mapper.ProjectSpecifyMapper;
import com.refinement.mapper.SpecifyReportMapper;
import com.refinement.service.ProjectSpecifyService;
import com.refinement.util.CommonUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Service
public class ProjectSpecifyServiceImpl extends ServiceImpl<ProjectSpecifyMapper, ProjectSpecify> implements ProjectSpecifyService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectSpecifyMapper projectSpecifyMapper;

    @Resource
    private SpecifyReportMapper specifyReportMapper;

    @Override
    public List<SpecifyRe> getSpecifyList(String projectid) {
        //项目细分列表
        List<SpecifyRe> specifyList = projectSpecifyMapper.getSpecifyList(projectid);
        if (specifyList == null || specifyList.size() == 0) {
            return new ArrayList<>();
        }
        // 查询细分进度本月工作完成量
        for (SpecifyRe specifyRe : specifyList) {
            // 项目细分id
            Long id = specifyRe.getId();
            // 查询本月对应的进度列表
            List<SpecifyReport> specifyReports
                    = specifyReportMapper.selectCurrentMonthList(id);
            // 本月完成的工作量
            BigDecimal monthCompleted = BigDecimal.ZERO;
            BigDecimal monthCompletedValue = BigDecimal.ZERO;
            if (specifyReports != null) {
                for (SpecifyReport specifyReport : specifyReports) {
                    if (specifyReport.getCompleted() != null) {
                        monthCompleted = monthCompleted.add(specifyReport.getCompleted());
                    }
                    if (specifyReport.getCompletedValue() != null) {
                        monthCompletedValue = monthCompletedValue.add(specifyReport.getCompletedValue());
                    }
                    if (specifyReport.getCreateTime() != null) {
                        specifyRe.setReportTime(CommonUtils.localDateTime2Date(specifyReport.getCreateTime()));
                    }
                }
            }
            // 总工作量
            specifyRe.setTotalWork(specifyRe.getQuantity().multiply(new BigDecimal(specifyRe.getPrice())).stripTrailingZeros());
            specifyRe.setMonthCompleted(monthCompleted);
            specifyRe.setMonthCompletedValue(monthCompletedValue);
            //取最新已经审批的工作量
            //prevCompleted	Bigdecimal	上次完成工作量
            //reportTime	Date	上报时间  取最新一次上报的时间
            SpecifyReport checkedReprot = specifyReportMapper.selectLastChecked(id);
            if (checkedReprot != null) {
                specifyRe.setPrevCompleted(checkedReprot.getCompleted());
                if (specifyRe.getReportTime() == null) {
                    specifyRe.setReportTime(CommonUtils.localDateTime2Date(checkedReprot.getCreateTime()));
                }
            }
            String price = specifyRe.getPrice();
            if (!"".equals(price) && !specifyRe.getQuantity().equals(BigDecimal.ZERO)) {
                BigDecimal scale
                        = BigDecimal.ZERO.multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //单项总价格
                BigDecimal sumPrice
                        = new BigDecimal(price).multiply(new BigDecimal(String.valueOf(specifyRe.getQuantity())));
                specifyRe.setSumPrice(sumPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
                if (sumPrice.compareTo(new BigDecimal("0.00")) != 0) {
                    //单项完成进度
                    BigDecimal report
                            = specifyRe.getTotalCompletedValue().divide(sumPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP);
                    specifyRe.setReport(report);
                } else {
                    specifyRe.setReport(new BigDecimal("0"));
                }
            } else {
                specifyRe.setSumPrice(new BigDecimal("0"));
                specifyRe.setReport(new BigDecimal("0"));
            }
        }
        return specifyList;
    }

    @Override
    public int updateSpecify(String projectid, List<ProjectSpecify> projectSpecifyList) {
        //分项的累计价格
        BigDecimal sumPrice = BigDecimal.ZERO;
        //分项系数
        BigDecimal coefficient = BigDecimal.ZERO;
        for (ProjectSpecify specifyObj : projectSpecifyList) {
            sumPrice = sumPrice.add(new BigDecimal(String.valueOf(specifyObj.getQuantity())).multiply(new BigDecimal(specifyObj.getPrice())));
            coefficient = coefficient.add(specifyObj.getCoefficient());
            specifyObj.setProjectid(projectid);
        }
        //分项系数总和大于1不给上报
        if (coefficient.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")).compareTo(new BigDecimal("0.00")) > 0) {
            return -1;
        }
        //保留四位小数
        sumPrice = sumPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
        //项目
        List<Project> projectList = projectMapper.selectList(new QueryWrapper<Project>().eq("projectid", projectid));
        if (!CollectionUtils.isEmpty(projectList)) {
            Project project = projectList.get(0);
            //合同总价值
            BigDecimal contractvalue = project.getContractvalue().multiply(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP);
            if (sumPrice.compareTo(contractvalue) != 0) {
                return -1;
            }
        }
        // 先删除掉之前的数据
        projectSpecifyMapper.delete(new QueryWrapper<ProjectSpecify>().eq("projectid", projectid));
        // 获取到更新后的数据，更新到数据库
        return projectSpecifyMapper.proSpecifyBatchSave(projectSpecifyList);
    }

    @Override
    public List<SpecifyRe> getAssignSpecifyList(String projectid, String reportTime) {
        //项目细分列表
        List<SpecifyRe> specifyList = projectSpecifyMapper.getSpecifyList(projectid);
        if (specifyList == null || specifyList.size() == 0) {
            return new ArrayList<>();
        }
        // 查询细分进度本月工作完成量
        for (SpecifyRe specifyRe : specifyList) {
            // 项目细分id
            Long id = specifyRe.getId();
            // 查询指定月份月对应的进度列表
            List<SpecifyReport> specifyReports
                    = specifyReportMapper.selectAssignSpecifyReportList(id, reportTime);
            // 本月完成的工作量
            BigDecimal monthCompleted = BigDecimal.ZERO;
            BigDecimal monthCompletedValue = BigDecimal.ZERO;
            if (specifyReports != null) {
                for (SpecifyReport specifyReport : specifyReports) {
                    if (specifyReport.getCompleted() != null) {
                        monthCompleted = monthCompleted.add(specifyReport.getCompleted());
                    }
                    if (specifyReport.getCompletedValue() != null) {
                        monthCompletedValue = monthCompletedValue.add(specifyReport.getCompletedValue());
                    }
                    if (specifyReport.getCreateTime() != null) {
                        specifyRe.setReportTime(CommonUtils.localDateTime2Date(specifyReport.getCreateTime()));
                    }
                }
            }
            // 总工作量
            specifyRe.setTotalWork(specifyRe.getQuantity().multiply(new BigDecimal(specifyRe.getPrice())).stripTrailingZeros());
            specifyRe.setMonthCompleted(monthCompleted);
            specifyRe.setMonthCompletedValue(monthCompletedValue);
            //指定月份使总计完成值等于指定的完成值
//            specifyRe.setTotalCompleted(monthCompleted);
//            specifyRe.setTotalCompletedValue(monthCompletedValue);
            //取最新已经审批的工作量
            //prevCompleted	Bigdecimal	上次完成工作量
            //reportTime	Date	上报时间  取最新一次上报的时间
            SpecifyReport checkedReprot = specifyReportMapper.selectLastChecked(id);
            if (checkedReprot != null) {
                specifyRe.setPrevCompleted(checkedReprot.getCompleted());
                if (specifyRe.getReportTime() == null) {
                    specifyRe.setReportTime(CommonUtils.localDateTime2Date(checkedReprot.getCreateTime()));
                }
            }
            //单项总价格
            BigDecimal sumPrice
                    = new BigDecimal(specifyRe.getPrice()).multiply(new BigDecimal(String.valueOf(specifyRe.getQuantity())));
            specifyRe.setSumPrice(sumPrice.setScale(2, BigDecimal.ROUND_HALF_UP));
            //单项完成进度
            BigDecimal report
                    = specifyRe.getTotalCompletedValue().divide(sumPrice, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(0, BigDecimal.ROUND_HALF_UP);
            specifyRe.setReport(report);
        }
        return specifyList;
    }

    @Override
    public boolean isSpecify(String projectid) {
        //分项的累计价格
        BigDecimal sumPrice = BigDecimal.ZERO;
        //分项系数
        BigDecimal coefficient = BigDecimal.ZERO;
        List<ProjectSpecify> projectSpecifyList
                = projectSpecifyMapper.selectList(new QueryWrapper<ProjectSpecify>().eq("projectid", projectid));
        for (ProjectSpecify specifyObj : projectSpecifyList) {
            if ("".equals(specifyObj.getPrice()) || specifyObj.getQuantity().equals(new BigDecimal("0.00"))) {
                return false;
            }
            sumPrice = sumPrice.add(new BigDecimal(String.valueOf(specifyObj.getQuantity())).multiply(new BigDecimal(specifyObj.getPrice())));
            coefficient = coefficient.add(specifyObj.getCoefficient());
            specifyObj.setProjectid(projectid);
        }
        // 去除分项系数大于1，分项系数不做判断
//        if (coefficient.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1.00")).compareTo(new BigDecimal("0.00")) > 0) {
//            return false;
//        }
        //保留四位小数
        sumPrice = sumPrice.setScale(6, BigDecimal.ROUND_HALF_UP);
        //项目
        List<Project> projectList = projectMapper.selectList(new QueryWrapper<Project>().eq("projectid", projectid));
        if (!CollectionUtils.isEmpty(projectList)) {
            Project project = projectList.get(0);
            //合同总价值
            BigDecimal contractvalue = project.getContractvalue().multiply(new BigDecimal("10000")).setScale(6, BigDecimal.ROUND_HALF_UP);
            return sumPrice.compareTo(contractvalue) == 0;
        }
        return true;
    }

    @Override
    public void savePreSpecify(String projectid, List<ProjectSpecify> specList) {
        for (ProjectSpecify projectSpecify : specList) {
            //分项内容
            String subitem = projectSpecify.getSubitem();
            if (subitem == null || subitem.trim().equals("")) {
                projectSpecify.setSubitem("");
            }
            //数量
            BigDecimal quantity = projectSpecify.getQuantity();
            if (quantity == null) {
                projectSpecify.setQuantity(new BigDecimal("0.00"));
            }
            //单价
            String price = projectSpecify.getPrice();
            if (price == null || price.trim().equals("")) {
                projectSpecify.setPrice("");
            }
            projectSpecify.setProjectid(projectid);
        }

        try {
            // 先删除掉之前的数据
            projectSpecifyMapper.delete(new QueryWrapper<ProjectSpecify>().eq("projectid", projectid));
            // 获取到更新后的数据，更新到数据库
            if (CollUtil.isNotEmpty(specList)) {
                projectSpecifyMapper.proSpecifyBatchSave(specList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
