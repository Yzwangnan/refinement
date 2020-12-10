package com.refinement.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.refinement.data.DecompositionExportDO;
import com.refinement.data.DecompositionLevelExportDO;
import com.refinement.data.enums.ProjectStatusEnum;
import com.refinement.data.vo.ExcelExportProjectVo;
import com.refinement.data.vo.ExcelExportVo;
import com.refinement.entity.Organization;
import com.refinement.entity.Project;
import com.refinement.entity.ProjectIndirectCost;
import com.refinement.mapper.OrganizationMapper;
import com.refinement.mapper.ProjectDecompositionMapper;
import com.refinement.mapper.ProjectMapper;
import com.refinement.mapper.ProjectMonthlyReportMapper;
import com.refinement.service.ExcelExportService;
import com.refinement.service.ProjectIndirectCostService;
import com.refinement.service.ProjectService;
import com.refinement.utils.ExcelUtil;
import com.refinement.utils.ZipUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbls;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * excel导出
 *
 * @author htmic
 * @date 2020/10/28
 */
@Service
public class ExcelExportServiceImpl implements ExcelExportService {

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectDecompositionMapper projectDecompositionMapper;

    @Resource
    private OrganizationMapper organizationMapper;

    @Resource
    private ProjectMonthlyReportMapper projectMonthlyReportMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ProjectIndirectCostService projectIndirectCostService;


    @Override
    public ExcelExportVo exportProjectIng(Integer status, List<String> projectIdList, Long modelId) {
        ExcelExportVo result = new ExcelExportVo();

        //查询所有进行中的项目
        QueryWrapper qw = new QueryWrapper();
        if (status != null) {
            qw.eq("state", status);
        }
        if (projectIdList != null) {
            qw.in("projectid", projectIdList);
        }
        List<Project> list = projectMapper.listByStatus(status, null, null, null, projectIdList, modelId);
        List<String> headList = new ArrayList<>();
        List<Object[]> valueList = new ArrayList<>();
        result.setHeadList(headList);
        result.setValueList(valueList);

        if (CollectionUtil.isNotEmpty(list)) {
            headList.add("序号");
            headList.add("事业部名称");
            headList.add("项目名称");
            headList.add("合同额（万元）");
            headList.add("预算额（万元）");

            for (int i = 0; i < list.size(); i++) {
                Project item = list.get(i);
                ExcelExportProjectVo vo = new ExcelExportProjectVo();

                List<Object> objs = new ArrayList<>();

                //序号
                objs.add((i + 1) + "");
                //事业部名称
                Organization organization = organizationMapper.selectById(item.getDeptid());
                if (organization != null) {
                    objs.add(organization.getOrganizationName());
                } else {
                    objs.add("");
                }
                //项目名称
                objs.add(item.getProjectname());
                //合同额
                objs.add(item.getContractvalue() != null ? item.getContractvalue() : "");
                //预算额
                objs.add(item.getBudgetAmount() != null ? item.getBudgetAmount()  : "");

//                QueryWrapper itQw = new QueryWrapper();
//                itQw.eq("model_id", item.getModelId());
                if (status != null && (status == 1 || status == 2)) {
                    //进行中项目查询模板一级分类
                    List<DecompositionExportDO> decompositionList = projectDecompositionMapper.oneLevelExport(item.getProjectid(), modelId);
                    for (DecompositionExportDO decomposition : decompositionList) {
                        if (i == 0) {
                            headList.add(decomposition.getOneLevelName());
                        }
                        objs.add(decomposition.getTotalBudgetAmount());
                    }
                }
                valueList.add(objs.toArray());
            }

            //加入总结
            List<Object> zongjie = new ArrayList<>();
            zongjie.add("总结");
            zongjie.add("");
            zongjie.add("");
            for (int i = 3; i < headList.size(); i++) {
                int finalI = i;
                zongjie.add(valueList.stream().map(item -> {
                    if (item == null) {
                        return BigDecimal.ZERO;
                    }
                    BigDecimal rs = new BigDecimal(item[finalI] != null && item[finalI] != "" ? item[finalI] + "" : "0");
                    return rs;
                }).reduce(BigDecimal.ZERO, BigDecimal::add));
            }
            valueList.add(zongjie.toArray());
            result.setHeadList(headList);
            result.setValueList(valueList);
        }
        return result;
    }

    @Override
    public HSSFWorkbook exportProjectDecomposition(String projectId) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        HSSFSheet sheet = workbook.createSheet("sheet");

        Project project = projectService.getByProjectId(projectId);

        List<DecompositionLevelExportDO> allList = projectDecompositionMapper.listByProjectIdAndLevel(projectId
                , null, null, null);

        // 所有一级列表
        List<DecompositionLevelExportDO> oneLevelList
                = allList.stream().filter(item -> item.getLevel() == 1).collect(Collectors.toList());

        // 所有二级列表
        List<DecompositionLevelExportDO> twoLevelList
                = allList.stream().filter(item -> item.getLevel() == 2).collect(Collectors.toList());

        // 所有三级列表
        List<DecompositionLevelExportDO> threeLevelList
                = allList.stream().filter(item -> item.getLevel() == 3).collect(Collectors.toList());

        //处理一级合并单元格问题
        List<String> oneRegionList = new ArrayList<>();
        Map<String, Map<String, Object>> flagList = new HashMap<>();
        oneLevelList.forEach(item -> {
            if (!oneRegionList.contains(item.getOneLevelName())) {
                oneRegionList.add(item.getOneLevelName());
                // 需要合并的长度
                int count;
                // 查询对应三级
                List<DecompositionLevelExportDO> threeLevelCollectList =
                        threeLevelList.stream().filter(fil -> fil.getOneLevelName().equals(item.getOneLevelName()))
                                .collect(Collectors.toList());
                if (CollUtil.isEmpty(threeLevelCollectList)) {
                    // 查询对应二级
                    List<DecompositionLevelExportDO> twoLevelCollectList =
                            twoLevelList.stream().filter(fil -> fil.getOneLevelName().equals(item.getOneLevelName()))
                                    .collect(Collectors.toList());
                    count = twoLevelCollectList.size();
                } else {
                    count = threeLevelCollectList.size();
                }
                Map<String, Object> map = new HashMap<>();
                map.put("name", item.getOneLevelName());
                map.put("count", count);
                map.put("flag", false);
                flagList.put(item.getOneLevelName(), map);
            }
        });

        //处理二级合并单元格问题
        List<String> twoRegionList = new ArrayList<>();
        Map<String, Map<String, Object>> flagTwoList = new HashMap<>();
        twoLevelList.forEach(item -> {
            if (!twoRegionList.contains(item.getOneLevelName() + "-" + item.getTwoLevelName())) {
                twoRegionList.add(item.getOneLevelName() + "-" + item.getTwoLevelName());
                // 查询对应三级
                List<DecompositionLevelExportDO> threeLevelCollectList =
                        threeLevelList.stream().filter(fil -> fil.getOneLevelName().equals(item.getOneLevelName()) && fil.getTwoLevelName().equals(item.getTwoLevelName()))
                                .collect(Collectors.toList());
                Map<String, Object> map = new HashMap<>();
                map.put("name", item.getOneLevelName() + "-" + item.getTwoLevelName());
                map.put("count", threeLevelCollectList.size());
                map.put("flag", false);
                flagTwoList.put(item.getOneLevelName() + "-" + item.getTwoLevelName(), map);
            }
        });

        //头所占的行数
        int headRowCount = 2;
        //创建头数据
        for (int i = 0; i < headRowCount; i++) {
            sheet.createRow(i);
        }
        //标题
        HSSFRow rowHead0 = sheet.getRow(0);
        HSSFCell cellhead_0 = rowHead0.createCell(0);
        cellhead_0.setCellStyle(style);
        cellhead_0.setCellValue(project.getProjectname());
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));

        List<String> headStrList = new ArrayList<>();
        headStrList.add("一级分类");
        headStrList.add("二级分类");
        headStrList.add("三级分类");
        headStrList.add("数据归口来源");
        headStrList.add("汇总");
        HSSFRow rowhead_1 = sheet.getRow(1);
        for (int i = 0; i < headStrList.size(); i++) {
            HSSFCell cell = rowhead_1.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headStrList.get(i));
        }

        //查询项目的有哪些月份报表
        List<String> mothList = projectMonthlyReportMapper.mothsByProjectId(projectId);

        //记录位置
        int record = 0;
        //需要合并的起始位置
        int merge = headRowCount;
        //画excel
        for (DecompositionLevelExportDO all : oneLevelList) {
            // 记录没有三级数量
            int number = 0;
            // 查询对应三级
            List<DecompositionLevelExportDO> threeLevelCollectList =
                    threeLevelList.stream().filter(fil -> fil.getOneLevelName().equals(all.getOneLevelName()))
                            .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(threeLevelCollectList)) {
                for (DecompositionLevelExportDO exportDO : threeLevelCollectList) {
                    HSSFRow row1 = sheet.createRow(headRowCount + record);
                    HSSFCell cell = row1.createCell(exportDO.getLevel() - 1);
                    cell.setCellStyle(style);

                    HSSFCell cell_1 = row1.createCell(0);
                    cell_1.setCellStyle(style);
                    cell_1.setCellValue(exportDO.getOneLevelName());

                    HSSFCell cell_2 = row1.createCell(1);
                    cell_2.setCellStyle(style);
                    cell_2.setCellValue(exportDO.getTwoLevelName());

                    HSSFCell cell_3 = row1.createCell(2);
                    cell_3.setCellStyle(style);
                    cell_3.setCellValue(exportDO.getThreeLevelName());

                    //合并二级的单元格
                    Map<String, Object> twoRegion = flagTwoList.get(exportDO.getOneLevelName() + "-" + exportDO.getTwoLevelName());
                    if (twoRegion != null && !(Boolean) twoRegion.get("flag")) {
                        Integer count = (Integer) twoRegion.get("count");
                        if (count > 1) {
                            sheet.addMergedRegion(new CellRangeAddress(headRowCount + record, headRowCount + record + count - 1, 1, 1));
                        }
                        twoRegion.put("flag", true);
                        flagTwoList.put(exportDO.getOneLevelName() + "-" + exportDO.getTwoLevelName(), twoRegion);
                    }
                    createCell(style, sheet, headRowCount, mothList, record, exportDO);
                    number++;
                    record++;
                }
            }
            // 查询对应二级
            List<DecompositionLevelExportDO> twoLevelCollectList =
                    twoLevelList.stream().filter(fil -> fil.getOneLevelName().equals(all.getOneLevelName()))
                            .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(twoLevelCollectList)) {
                for (DecompositionLevelExportDO exportDO : twoLevelCollectList) {
                    // 查询对应三级
                    List<DecompositionLevelExportDO> collectList =
                            threeLevelList.stream().filter(fil -> fil.getOneLevelName().equals(exportDO.getOneLevelName()) && fil.getTwoLevelName().equals(exportDO.getTwoLevelName()))
                                    .collect(Collectors.toList());
                    if (CollUtil.isEmpty(collectList)) {
                        HSSFRow row1 = sheet.createRow(headRowCount + record);
                        HSSFCell cell_1 = row1.createCell(0);
                        cell_1.setCellStyle(style);
                        cell_1.setCellValue(exportDO.getOneLevelName());

                        HSSFCell cell_2 = row1.createCell(1);
                        cell_2.setCellStyle(style);
                        cell_2.setCellValue(exportDO.getTwoLevelName());

                        createCell(style, sheet, headRowCount, mothList, record, exportDO);
                        record++;
                        number++;
                    }
                }
            } else {
                HSSFRow row = sheet.createRow(headRowCount + record);
                HSSFCell cell_11 = row.createCell(all.getLevel() - 1);
                cell_11.setCellStyle(style);

                cell_11.setCellValue(all.getOneLevelName());
                createCell(style, sheet, headRowCount, mothList, record, all);
                record++;
            }
           // 合并一级单元格
           if (number > 1) {
               sheet.addMergedRegion(new CellRangeAddress(merge, merge + number - 1, 0, 0));
               merge = merge + number;
           } else {
               // 说明此一级分项无子级
               merge = merge + 1;
           }
        }
        return workbook;
    }

    private void createCell(HSSFCellStyle style, HSSFSheet sheet, int headRowCount, List<String> mothList, int record, DecompositionLevelExportDO exportDO) {
        //设置数据归口来源(上传人)
        HSSFRow row = sheet.getRow(headRowCount + record);
        HSSFCell c_3 = row.createCell(3);
        c_3.setCellStyle(style);
        c_3.setCellValue(exportDO.getUsername());

        //设置汇总
        HSSFRow rowhz = sheet.getRow(headRowCount + record);
        HSSFCell cellhz = rowhz.createCell(4);
        cellhz.setCellStyle(style);
        cellhz.setCellValue(exportDO.getUsername());
        BigDecimal allAmount = projectMonthlyReportMapper.statisticsAllAmount(exportDO.getProjectDecompositionId());
        cellhz.setCellValue(allAmount == null ? "0" : String.valueOf(allAmount));

        //设置月份
        HSSFRow rowyf = sheet.getRow(headRowCount + record);
        for (int j = 0; j < mothList.size(); j++) {
            //创建头
            HSSFRow mothRow = sheet.getRow(headRowCount - 1);
            HSSFCell cellMoth = mothRow.createCell(5 + j);
            cellMoth.setCellStyle(style);
            cellMoth.setCellValue(mothList.get(j) + "月");

            String moth = mothList.get(j);
            BigDecimal mothAmount = projectMonthlyReportMapper.statisticsMothAmount(exportDO.getProjectDecompositionId(), moth);
            HSSFCell cellyf = rowyf.createCell(5 + j);
            cellyf.setCellStyle(style);
            cellyf.setCellValue(mothAmount == null ? "0" : String.valueOf(mothAmount));
        }
    }

    @Override
    public List<Project> projectList(Date startDateTime, Date endDateTime, List<String> deptidList, Long modelId) {
        List<Project> list = projectMapper.listByStatus(ProjectStatusEnum.HISTORY.status(), startDateTime, endDateTime, deptidList, null, modelId);
        return list;
    }

    @Override
    public void historyProject(HttpServletResponse response, List<String> projectIdList, Integer type, Long modelId) {

        try {
            List<File> fileList = new ArrayList<>();

            //项目成本导出表
            if (type == 1) {
                ExcelExportVo result = this.exportProjectIng(ProjectStatusEnum.HISTORY.status(), projectIdList, modelId);
                List<String> headList = new ArrayList<>();
                headList.add("序号");
                headList.add("事业部名称");
                headList.add("项目名称");
                headList.add("合同额（万元）");
                headList.add(" 预算额（万元）");
                List<Object[]> valueList = result.getValueList();
                // 定义表的标题
                String title = "费用";
                //定义表的列名
                String[] rowsName = headList.toArray(new String[headList.size()]);
                // 创建ExportExcel对象
                ExcelUtil excelUtil = new ExcelUtil();
                HSSFWorkbook workbook = excelUtil.exportHSSFWorkbook(title, rowsName, valueList);
                File file = File.createTempFile("项目成本导出表", ".xlsx");
                workbook.write(new FileOutputStream(file));
                fileList.add(file);
            }
            //项目细化分解导出表
            if (type == 2) {
                for (String projectid : projectIdList) {
                    HSSFWorkbook workbook = this.exportProjectDecomposition(projectid);
                    File file = File.createTempFile("项目细化分解导出表-[" + projectid + "]", ".xlsx");
                    workbook.write(new FileOutputStream(file));
                    fileList.add(file);
                }
            }
            //项目结算报表
            if (type == 3) {
                for (String projectid : projectIdList) {
                    XSSFWorkbook workbook = this.exportJiesuanProject(projectid);
                    File file = File.createTempFile("项目结算报表-[" + projectid + "]", ".xlsx");
                    workbook.write(new FileOutputStream(file));
                    fileList.add(file);
                }
            }
            ZipUtils.toZip(fileList, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 项目结算报表
     *
     * @param projectid
     * @return
     */
    @Override
    public XSSFWorkbook exportJiesuanProject(String projectid) {

        Project project = projectService.getByProjectId(projectid);

        List<DecompositionLevelExportDO> allList = projectDecompositionMapper.listByProjectIdAndLevel(projectid
                , null, null, null);
        List<String> allOneNameList = allList.stream().map(DecompositionLevelExportDO::getOneLevelName).collect(Collectors.toList());
        List<String> allOneTwoNameList = allList.stream().map(item -> item.getOneLevelName() + "-" + item.getTwoLevelName()).collect(Collectors.toList());

        //过滤一级数据
        List<DecompositionLevelExportDO> list = allList.stream().filter(item -> item.getLevel() != 1).collect(Collectors.toList());
        //过滤二级数据
        list = list.stream().filter(item -> {
            boolean flag = !allOneTwoNameList.contains(item.getOneLevelName() + "-" + item.getTwoLevelName()) || StrUtil.isNotEmpty(item.getThreeLevelName());
            return flag;
        }).collect(Collectors.toList());

        //查询所有的细化分解数据
        List<ProjectIndirectCost> projectIndirectList = projectIndirectCostService.list();
        List<String> indirectCostList = projectIndirectList.stream().map(ProjectIndirectCost::getName).collect(Collectors.toList());

        list.forEach(item -> {
            if (StrUtil.isNotEmpty(item.getOneLevelName())) {
                item.setShowName(item.getOneLevelName());
            }
            if (StrUtil.isNotEmpty(item.getTwoLevelName())) {
                item.setShowName(item.getTwoLevelName());
            }
            if (StrUtil.isNotEmpty(item.getThreeLevelName())) {
                item.setShowName(item.getThreeLevelName());
            }
        });

        //间接成本
        List<DecompositionLevelExportDO> indirectDecompositionList = list.stream().filter(item -> indirectCostList.contains(item.getShowName())).collect(Collectors.toList());
        //直接成本
        List<DecompositionLevelExportDO> directlyDecompositionList = list.stream().filter(item -> !indirectCostList.contains(item.getShowName())).collect(Collectors.toList());


//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFCellStyle style = workbook.createCellStyle();
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        HSSFSheet sheet = workbook.createSheet("sheet");

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFSheet sheet = workbook.createSheet("sheet");

        //头
        XSSFRow headRow = sheet.createRow(0);
//        for (int i = 0; i < 6; i++) {
//            headRow.createCell(i);
//        }
//        HSSFCell headCell = headRow.getCell(0);
        XSSFCell headCell = headRow.createCell(0);
        headCell.setCellStyle(style);
        headCell.setCellValue(project.getProjectname() + "结算报告");
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 5));

        //画直接成本
        XSSFRow zjHeadRow = sheet.createRow(3);
        XSSFCell zjHeadCell = zjHeadRow.createCell(0);
        zjHeadCell.setCellStyle(style);
        zjHeadCell.setCellValue("直接成本");
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));

        XSSFCell zjHeadCell_2 = zjHeadRow.createCell(2);
        zjHeadCell_2.setCellStyle(style);
        zjHeadCell_2.setCellValue("金额（万元）");

        for (int i = 0; i < directlyDecompositionList.size(); i++) {
            DecompositionLevelExportDO zj = directlyDecompositionList.get(i);
            XSSFRow zjRow = sheet.createRow(3 + 1 + i);
            XSSFCell zjCell_1 = zjRow.createCell(0);
            zjCell_1.setCellStyle(style);
            zjCell_1.setCellValue(zj.getShowName());
            sheet.addMergedRegion(new CellRangeAddress(3 + 1 + i, 3 + 1 + i, 0, 1));

            XSSFCell zjCell_2 = zjRow.createCell(2);
            zjCell_2.setCellStyle(style);
            zjCell_2.setCellValue(zj.covertBudgetAmountWangUnit() + "");
        }


        //画间接成本
        XSSFRow jjHeadRow = sheet.getRow(3);
        XSSFCell jjHeadCell = jjHeadRow.createCell(3);
        jjHeadCell.setCellStyle(style);
        jjHeadCell.setCellValue("间接成本");
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));

        XSSFCell jjHeadCell_2 = jjHeadRow.createCell(5);
        jjHeadCell_2.setCellStyle(style);
        jjHeadCell_2.setCellValue("金额（万元）");

        for (int i = 0; i < indirectDecompositionList.size(); i++) {
            DecompositionLevelExportDO jj = indirectDecompositionList.get(i);
            XSSFRow jjRow = sheet.getRow(3 + 1 + i);
            if (jjRow == null) {
                jjRow = sheet.createRow(3 + 1 + i);
            }
            XSSFCell jjCell_1 = jjRow.createCell(3);
            jjCell_1.setCellStyle(style);
            jjCell_1.setCellValue(jj.getShowName());
            sheet.addMergedRegion(new CellRangeAddress(3 + 1 + i, 3 + 1 + i, 3, 4));

            XSSFCell jjCell_2 = jjRow.createCell(4);
            jjCell_2.setCellStyle(style);
            jjCell_2.setCellValue(jj.covertBudgetAmountWangUnit() + "");
        }

        //添加合计
        int hjRowCount = directlyDecompositionList.size() > indirectDecompositionList.size() ? directlyDecompositionList.size() : indirectDecompositionList.size();
        hjRowCount = hjRowCount + 4;
        XSSFRow hjRow = sheet.createRow(hjRowCount);
        XSSFCell hj_zj_Cell = hjRow.createCell(0);
        hj_zj_Cell.setCellStyle(style);
        hj_zj_Cell.setCellValue("合计");
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount, hjRowCount, 0, 1));

        XSSFCell hj_zj_Cell_2 = hjRow.createCell(2);
        hj_zj_Cell_2.setCellStyle(style);
        BigDecimal hj_zj_total = directlyDecompositionList.stream().map(item -> new BigDecimal(item.covertBudgetAmountWangUnit())).reduce(BigDecimal.ZERO, BigDecimal::add);
        hj_zj_Cell_2.setCellValue(hj_zj_total + "");

        XSSFCell hj_jj_Cell = hjRow.createCell(3);
        hj_jj_Cell.setCellStyle(style);
        hj_jj_Cell.setCellValue("合计");
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount, hjRowCount, 3, 4));

        XSSFCell hj_jj_Cell_2 = hjRow.createCell(5);
        hj_jj_Cell_2.setCellStyle(style);
        BigDecimal hj_jj_total = indirectDecompositionList.stream().map(item -> new BigDecimal(item.covertBudgetAmountWangUnit())).reduce(BigDecimal.ZERO, BigDecimal::add);
        hj_jj_Cell_2.setCellValue(hj_jj_total + "");


        //创建空白行
        XSSFRow emptyRow = sheet.createRow(hjRowCount + 1);
        emptyRow.createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1, hjRowCount + 1, 0, 5));


        //其他统计
        XSSFRow other_1 = sheet.createRow(hjRowCount + 1 + 1);
        other_1.createCell(0).setCellValue("合同总额（万元）");
        other_1.createCell(1).setCellValue(project.getContractvalue() + "");
        other_1.createCell(2).setCellValue("内部承包总额（万元）");
        other_1.createCell(3).setCellFormula("B" + (hjRowCount + 1 + 1 + 1) + "*0.6382");
        other_1.createCell(4).setCellValue("可分配效益奖金（万元）");
        other_1.createCell(5).setCellFormula("D" + (hjRowCount + 1 + 1 + 1) + "-C" + (hjRowCount + 1) + "-F" + (hjRowCount + 1));


        XSSFRow other_2 = sheet.createRow(hjRowCount + 1 + 2);
        other_2.createCell(0).setCellValue("总成本（万元）");
        other_2.createCell(1).setCellFormula("C" + (hjRowCount + 1) + "+F" + (hjRowCount + 1) + "+F" + (5 + 1));
        other_2.createCell(2).setCellValue("合同占比");
        XSSFCell htzbCell = other_2.createCell(3);
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        htzbCell.setCellStyle(cellStyle);
        htzbCell.setCellFormula("B" + (hjRowCount + 1 + 1 + 1) + "/B" + (hjRowCount + 1 + 2 + 1));
        other_2.createCell(4).setCellValue("结算时间");
        Date date = Date.from(project.getUpdateTime().atZone(ZoneOffset.ofHours(8)).toInstant());
        other_2.createCell(5).setCellValue(DateUtil.formatDate(date));

        //创建空白行
        sheet.createRow(hjRowCount + 1 + 1 + 1 + 1).createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1 + 1 + 1 + 1, hjRowCount + 1 + 1 + 1 + 1, 0, 5));

        //领导签字
        XSSFCell ldCell = sheet.createRow(hjRowCount + 1 + 1 + 1 + 1 + 1).createCell(0);
        ldCell.setCellValue("公司分管领导：                生产运营部负责人：                 事业部负责人：               项目经理：                 结算负责人：");
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1 + 1 + 1 + 1 + 1, hjRowCount + 1 + 1 + 1 + 1 + 1, 0, 5));

        //创建空白行
        sheet.createRow(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1).createCell(0);
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1, hjRowCount + 1 + 1 + 1 + 1 + 1 + 1, 0, 5));

        //比例
        sheet.createRow(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1).createCell(0).setCellValue("各项成本占总成本比例：");
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1, hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1
                , 0, 1));
        sheet.getRow(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1).createCell(3).setCellValue("各项成本占合同比例：");
        sheet.addMergedRegion(new CellRangeAddress(hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1, hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1
                , 3, 4));


        //设置宽度
        sheet.setColumnWidth(0, 30 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 30 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);


        //画饼图-直接成本
        if (CollectionUtil.isNotEmpty(directlyDecompositionList)) {
            this.drawChart(sheet, hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 3, 0, directlyDecompositionList);
        }
        //画饼图-间接成本
        if (CollectionUtil.isNotEmpty(indirectDecompositionList)) {
            this.drawChart(sheet, hjRowCount + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 3, 3, indirectDecompositionList);
        }

        //测试保存文件
//        try {
//            File file = new File("E:\\zj-" + System.currentTimeMillis() + ".xls");
//            FileOutputStream fout = new FileOutputStream(file);
//            workbook.write(fout);
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return workbook;
    }

    /**
     * 画饼图
     *
     * @param sheet
     */
    private void drawChart(XSSFSheet sheet, int rowCount,int cellCount, List<DecompositionLevelExportDO> dataList) {
        try {
            //创建一个画布
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            //前四个默认0，[0,4]：从0列4行开始;[7,20]:宽度7个单元格，20向下扩展到20行
            //默认宽度(14-8)*12
            /**
             * * @param dx1第一个单元格内的x坐标。
             * * @param dy1第一个单元格内的y坐标。
             * * @param dx2第二单元格内的x坐标。
             * * @param dy2是第二个单元格内的y坐标。
             * 第一个单元格的列(基于0)。
             * * @param第一个单元格的行(基于0)。
             * * @param col2第二个单元格的列(基于0)。
             * * @param row2第二个单元格的行(基于0)。
             */
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, cellCount, rowCount, cellCount+3, rowCount + 32);
            //创建一个chart对象
            XSSFChart chart = drawing.createChart(anchor);
            //标题
            chart.setTitleText("");
            chart.setAutoTitleDeleted(true);
            //标题是否覆盖图表
            chart.setTitleOverlay(false);

            //图例位置
//            XDDFChartLegend legend = chart.getOrAddLegend();
//            legend.setPosition(LegendPosition.TOP_RIGHT);

            //CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
            
            //分类
//            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromArray(
//                    new String[]{"测试1", "测试2", "测试3"});
            List<String> xData = dataList.stream().map(DecompositionLevelExportDO::getShowName).collect(Collectors.toList());
            String[] arrX = new String[xData.size()];
            xData.toArray(arrX);
            XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromArray(arrX);

            //数据
            List<BigDecimal> yData = dataList.stream().map(DecompositionLevelExportDO::getBudgetAmount).collect(Collectors.toList());
            BigDecimal[] arrY = new BigDecimal[xData.size()];
            yData.toArray(arrY);
//            XDDFNumericalDataSource<BigDecimal> values = XDDFDataSourcesFactory.fromArray(
//                    new BigDecimal[]{new BigDecimal("123"), new BigDecimal("234"), new BigDecimal("456")});
            XDDFNumericalDataSource<BigDecimal> values = XDDFDataSourcesFactory.fromArray(arrY);

            XDDFChartData data = chart.createData(ChartTypes.PIE, null, null);
            //设置为可变颜色
            data.setVaryColors(true);
            //图表加载数据
            data.addSeries(countries, values);


//            XDDFChartAxis category = new XDDFCategoryAxis();
//            XDDFValueAxis values;
//            XDDFChartData data = chart.createData(ChartTypes.PIE, category, values);


            //绘制
            chart.plot(data);
            CTDLbls dLbls = chart.getCTChart().getPlotArea().getPieChartArray(0).getSerArray(0).addNewDLbls();
            dLbls.addNewShowVal().setVal(false);
            dLbls.addNewShowLegendKey().setVal(false);
            dLbls.addNewShowCatName().setVal(true);//类别名称
            dLbls.addNewShowSerName().setVal(false);
            dLbls.addNewShowPercent().setVal(true);//百分比
            dLbls.addNewShowLeaderLines().setVal(true);//引导线
            dLbls.setSeparator("\n");//分隔符为分行符
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
