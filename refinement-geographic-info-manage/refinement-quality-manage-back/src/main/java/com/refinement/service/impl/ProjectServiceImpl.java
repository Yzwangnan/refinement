package com.refinement.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.refinement.config.BusinessException;
import com.refinement.data.ReportRecordDO;
import com.refinement.entity.*;
import com.refinement.group.ProjectDept;
import com.refinement.group.ProjectPage;
import com.refinement.group.QueryConditionForTotalPro;
import com.refinement.http.DefaultResponseCode;
import com.refinement.http.ResultDTO;
import com.refinement.mapper.*;
import com.refinement.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wn
 * @since 2020-04-21
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ProjectReportMapper projectReportMapper;

    @Resource
    private SpecifyReportMapper specifyReportMapper;

    @Resource
    private ProjectSpecifyMapper projectSpecifyMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private ProjectCompleteMapper projectCompleteMapper;

    @Override
    public ResultDTO getProjectList(ProjectPage project) {
        // 去除前端可能传过来的空格
        String projectname = project.getProjectname();
        if (projectname != null) {
            // 去掉首位空格
            project.setProjectname(projectname.trim());
        }
        //合同总额
        BigDecimal totalContractValue = BigDecimal.ZERO;
        //当月完成产值
        BigDecimal monthValue = BigDecimal.ZERO;
        //累计完成产值
        BigDecimal totalValue = BigDecimal.ZERO;
        //剩余合同额
        BigDecimal surplusContractValue;
        int totalProject = 0;
        // 查询符合条件的项目列表 ----> ProjectMapper.xml  进行了分页处理
        List<ProjectDept> list = projectMapper.getProjectList(project);
        //取本月上报状态（0-未上报 1-审核通过 2-审核未通过 3-已上报未审核 4-超出上报日期）
        if (list != null && list.size() > 0) {
            // 判断每个项目进度的上报状态
            for (ProjectDept pro : list) {
                //查询事业部信息
                Organization organization = organizationMapper.selectById(pro.getDeptid());
                if (organization != null) {
                    pro.setDeptname(organization.getOrganizationName());
                }
                //根据项目id取当月上报信息
                ProjectReport report = projectReportMapper.selectCurrentMonth(pro.getProjectid());
                // 判断当前项目当前月份所处状态  0 - 未审核  ；1 - 通过 ； 2 - 未通过
                if (report == null) {
                    pro.setReportstate(0);
                    //4-超出上报日期
                    int thisDay = LocalDate.now().getDayOfMonth();
                    if (thisDay > pro.getReportday()) {
                        pro.setReportstate(4);
                    }
                } else if (report.getPdCheck() == 1) { //生产运营部审核 通过
                    pro.setReportstate(1);
                } else if (report.getDeptCheck() == 2 || report.getPdCheck() == 2) {
                    pro.setReportstate(2);
                } else {
                    pro.setReportstate(3);
                }
                // 查询当前项目下的细分
                List<ProjectSpecify> projectSpecifyList
                        = projectSpecifyMapper.selectList(new QueryWrapper<ProjectSpecify>().eq("projectid", pro.getProjectid()));

                int count = 1;

                // 项目细分集合
                List<String> proSpecifyList = new ArrayList<>();
                // 项目完成产值
                BigDecimal totalCompletedValue = BigDecimal.ZERO;
                for (ProjectSpecify specify : projectSpecifyList) {
                    StringBuffer sb = new StringBuffer();
                    String proSpecify = "";
                    totalCompletedValue = totalCompletedValue.add(specify.getTotalCompletedValue());
                    //项目细化分解项数据
                    // 分项名称
                    String subitem = specify.getSubitem();
                    sb.append(count + " 分项内容：" + subitem);
                    // 上报时间
                    if (report != null) {
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        sb.append(" 上报时间：" + dtf.format(report.getCreateTime()));
                    } else {
                        sb.append(" 上报时间：本月无上报记录");
                    }
                    // 合同单价
                    sb.append(" 合同单价：" + specify.getPrice() + "元");
                    // 累计完成工作量
                    sb.append(" 累计完成工作量：" + specify.getTotalCompleted());
                    // 累计完成工作产值
                    sb.append(" 累计完成工作产值：" + specify.getTotalCompletedValue() + "元");
                    // 完成进度
                    if (specify.getTotalCompletedValue().compareTo(BigDecimal.ZERO) != 0) {
                        //分项需完成的产值
                        BigDecimal multiply = new BigDecimal(specify.getPrice()).multiply(new BigDecimal(String.valueOf(specify.getQuantity())));
                        BigDecimal completed
                                = specify.getTotalCompletedValue().divide(multiply, 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        sb.append(" 完成进度：" + completed.multiply(new BigDecimal("100")) + "%");
                    } else {
                        sb.append(" 完成进度：" + "0%");
                    }
                    proSpecify += sb.toString();
                    proSpecifyList.add(proSpecify);
                    count++;
                    //计算当月完成产值
//                    Long specifyId = specify.getId();
//                    List<SpecifyReport> reportList = specifyReportMapper.selectCurrentMonthList(specifyId);
//                    if (!CollectionUtils.isEmpty(reportList)) {
//                        for (SpecifyReport specifyReport : reportList) {
//                            monthValue = monthValue.add(specifyReport.getCompletedValue());
//                        }
//                    }
                }
                pro.setProSpecifyList(proSpecifyList);
                pro.setTotalCompletedValue(totalCompletedValue);
                //合同总额
//                totalContractValue = totalContractValue.add(pro.getContractvalue());
                //累计完成产值
//                totalValue = totalValue.add(pro.getTotalCompletedValue());
            }
        }
        // 计算合同总额 、当月完成产值、累计完成产值、剩余合同额
        // 获取当前事业部 新项目 、进行中、已完成所有的项目
        QueryConditionForTotalPro queryConditionForTotalPro = new QueryConditionForTotalPro();
        queryConditionForTotalPro.setDeptId(project.getDeptid());
        queryConditionForTotalPro.setState(project.getState());
        List<Project> deptTotalProjectList = projectMapper.getTotalProjectByStateAndDept(queryConditionForTotalPro);
        if (deptTotalProjectList != null && deptTotalProjectList.size() > 0) {
            for (Project targetProjectAppropriate : deptTotalProjectList) {
                totalProject = totalProject + 1;
                // 1.计算合同总额
                totalContractValue = totalContractValue.add(targetProjectAppropriate.getContractvalue());
                // 根据项目id取当月上报信息
                ProjectReport report = projectReportMapper.selectCurrentMonth(targetProjectAppropriate.getProjectid());
                // 2.如果当月有上报产值，则计算当月完成产值
                if (report != null) {
                    // 如果当前项目本月有进行上报产值 ，则进行以下的计算
                    List<ProjectSpecify> projectSpecifyList   = projectSpecifyMapper.selectList(new QueryWrapper<ProjectSpecify>().eq("projectid", report.getProjectid()));
                    for (ProjectSpecify proSpecify : projectSpecifyList) {
                        // 3. 根据获取到的项目细分项列表来获取当前月份所完成产值
                        Long specifyId = proSpecify.getId();
                        List<SpecifyReport> reportList = specifyReportMapper.selectCurrentMonthList(specifyId);
                        if (!CollectionUtils.isEmpty(reportList)) {
                            for (SpecifyReport specifyReport : reportList) {
                                monthValue = monthValue.add(specifyReport.getCompletedValue());
                            }
                        }
                        // 4. 根据获取到的项目细分项列表，统计累计完成产值数量
                        totalValue = totalValue.add(proSpecify.getTotalCompletedValue());
                    }
                } else {
                    // 如果当前项目本月没有进行产值上报，则进行以下的计算
                    List<ProjectSpecify> projectSpecifies = projectSpecifyMapper.selectList(new QueryWrapper<ProjectSpecify>().eq("projectid",targetProjectAppropriate.getProjectid()));
                    for (ProjectSpecify projectSpecify : projectSpecifies) {
                        // 根据获取到的项目细分列表，
                        totalValue = totalValue.add(projectSpecify.getTotalCompletedValue());
                    }
                }
            }
        }
        monthValue = monthValue.divide(new BigDecimal("10000"), 6, BigDecimal.ROUND_HALF_UP);
        totalValue = totalValue.divide(new BigDecimal("10000"), 6, BigDecimal.ROUND_HALF_UP);
        // 计算剩余合同额
        surplusContractValue = totalContractValue.subtract(totalValue);
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> map = new HashMap<>(4);
        map.put("totalContractValue", totalContractValue);
        map.put("monthValue", monthValue);
        map.put("totalValue", totalValue);
        map.put("surplusContractValue", surplusContractValue);
        map.put("projectList", list);
        map.put("totalProject", totalProject);
        resultDTO.setResult(map);
        return resultDTO;
    }

    @Override
    public ResultDTO addProject(Project project, String password) {
        //查询数据库是否存在该项目的用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", project.getProjectid()));
        //用来记录用户id
        Long userid = null;
        if (user == null) {
            //创建项目账号和密码
            User newUser = new User();
            newUser.setUsername(project.getProjectid());
            newUser.setPassword(password);
            //项目用户为3
            newUser.setType(3);
            newUser.setDeptid(project.getDeptid());
            newUser.setProjectid(project.getProjectid());
            userMapper.insert(newUser);
            //查询新增后的用户获取userid
            User subjectUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", project.getProjectid()));
            userid = subjectUser.getId();
        }
        if (userid == null && user != null) {
            userid = user.getId();
        }
        //新建项目关联用户id
        project.setUserid(userid);
        //新建项目状态为0
        project.setState(0);
        //添加新项目
        int insert = projectMapper.insert(project);
        //判断是否添加成功
        if (insert > 0) {
            return new ResultDTO(200, "请求成功");
        } else {
            return new ResultDTO(201, "请求失败");
        }
    }

    @Override
    public ResultDTO confirmNew(String projectid) {
        Project project = projectMapper.selectOne(new QueryWrapper<Project>().eq("projectid", projectid));
        if (project == null) {
            throw new BusinessException(DefaultResponseCode.PROJECT_IS_NOT_EXIST);
        } else {
            project.setState(1);
            int i = projectMapper.updateById(project);
            if (i > 0) {
                //生成项目完成状态记录
                ProjectComplete complete = new ProjectComplete();
                complete.setProjectId(projectid);
                //系统类型 形象进度
                complete.setSystemType(1);
                //新建确认后状态为进行中
                complete.setState(1);
                projectCompleteMapper.insert(complete);
                return new ResultDTO();
            } else {
                return new ResultDTO(201, "请求失败");
            }
        }
    }

    @Override
    public ResultDTO progress(String projectid , String progress) {
        //当月的第一天
        LocalDateTime first = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0);
        //当月的最后一天
        LocalDateTime last = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59);
        //根据项目id查出项目进度报告表id
        //查询当月的项目进度列表
        List<ProjectReport> projectReports = projectReportMapper.selectList(new QueryWrapper<ProjectReport>().eq("projectid", projectid)
                .between("create_time", first, last));
        //最新的上报记录
        ProjectReport report = new ProjectReport();
        if (projectReports != null && projectReports.size() > 0) {
            report = projectReports.get(0);
            //1 本月上报 未审核的场合
            //  提示错误，不能上报
            //3 通过的场合
            // 不能上报
            //2 未通过的场合
            // 更新上次上报的信息
            report.setReportnums(report.getReportnums() + 1); //不用
            report.setUpdateTime(LocalDateTime.now());
            //改变审核的状态和审核原因
            report.setDeptCheck(0);
            report.setDeptReason("");
            report.setPdCheck(0);
            report.setPdReason("");
            report.setUpdateTime(LocalDateTime.now());
            //修改项目进度报告
            projectReportMapper.updateById(report);
            //删除上报细分数据
            specifyReportMapper.delete(new QueryWrapper<SpecifyReport>().eq("reportid", report.getId()));
        } else {
            //添加项目进度报告表
            report.setProjectid(projectid);
            report.setReportnums(1);
            //新增项目进度报告
            projectReportMapper.insert(report);
        }
        //添加细分项报告
        JSONArray array = JSONArray.parseArray(progress);
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            long specifyid = Long.parseLong(String.valueOf(object.get("specifyid")));
            //完成工作量
            BigDecimal completed = new BigDecimal(String.valueOf(object.get("completed")));
            //上报完成产品
            BigDecimal completedValue = new BigDecimal(String.valueOf(object.get("completedValue")));
            //添加项目细分项目报告
            SpecifyReport specifyReport = new SpecifyReport();
            specifyReport.setReportid(report.getId());
            specifyReport.setProjectid(projectid);
            specifyReport.setSpecifyid(specifyid);
            specifyReport.setCompleted(completed);
            specifyReport.setCompletedValue(completedValue);
            specifyReportMapper.insert(specifyReport);
        }
        return new ResultDTO(200, "项目进度上报成功");
    }

    @Override
    public Long selectTotal() {
        Integer count = projectMapper.selectCount(null);
        return Long.parseLong(String.valueOf(count));
    }

    @Override
    public List<String> listProjectIds() {
        return projectMapper.listProjectIds();
    }

    @Override
    public void updateProjectStatus(String projectid, Integer state) {
        projectMapper.updateState(projectid, state);
        if (state == 2) {
            //表明操作为项目完成
            // 修改项目对应记录为已完成
            ProjectComplete complete = projectCompleteMapper.selectOne(new QueryWrapper<ProjectComplete>()
                    .eq("project_id", projectid)
                    .eq("system_type", 1)
                    .last("LIMIT 1"));
            //逻辑需要 不进行判空校验
            complete.setState(2);
            projectCompleteMapper.updateById(complete);
        }
    }

    @Override
    public int removeProject(String projectid) {
        //删除关联用户
        userMapper.delete(new QueryWrapper<User>().eq("projectid", projectid));
        //删除项目 逻辑删除
        return projectMapper.delete(new QueryWrapper<Project>().eq("projectid", projectid));
    }

    @Override
    public List<ReportRecordDO> reportRecord(String projectid) {
        List<ProjectReport> reportList =
                projectReportMapper.selectList(new QueryWrapper<ProjectReport>().eq("projectid", projectid).orderByAsc("create_time"));
        if (reportList == null || reportList.size() == 0) {
            return new ArrayList<>();
        }
        List<ReportRecordDO> reportRecordDOS = new ArrayList<>();
        for (ProjectReport projectReport : reportList) {
            LocalDateTime createTime = projectReport.getCreateTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyy-MM");
            String reportTime = dtf.format(createTime);
            ReportRecordDO reportRecordDO = new ReportRecordDO();
            reportRecordDO.setReportTime(reportTime);
            reportRecordDOS.add(reportRecordDO);
        }
        return reportRecordDOS;
    }
}
