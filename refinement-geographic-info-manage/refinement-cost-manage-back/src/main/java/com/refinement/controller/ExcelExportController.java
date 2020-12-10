package com.refinement.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.refinement.data.enums.ProjectStatusEnum;
import com.refinement.data.param.HistoryParam;
import com.refinement.data.vo.ExcelExportVo;
import com.refinement.entity.Project;
import com.refinement.http.WrapMapper;
import com.refinement.http.Wrapper;
import com.refinement.service.ExcelExportService;
import com.refinement.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author htmic
 * @date 2020/10/28
 */
@Api(tags = "Excel导出")
@Validated
@RestController
@RequestMapping("/excel")
public class ExcelExportController {

    @Autowired
    private ExcelExportService excelExportService;


    @GetMapping("/project/new")
    @ApiOperation(value = "新建项目-项目导出")
    public void projectNew(HttpServletResponse response) {
        ExcelExportVo result = excelExportService.exportProjectIng(ProjectStatusEnum.NEWS.status(), null, null);
        List<String> headList = result.getHeadList();
        List<Object[]> valueList = result.getValueList();

        // 定义表的标题
        String title = "费用";
        //定义表的列名
        String[] rowsName = headList.toArray(new String[headList.size()]);

        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String fileName = new String("项目成本导出表.xlsx".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, valueList, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/project/ing")
    @ApiOperation(value = "进行中项目-项目导出")
    public void projecting(HttpServletResponse response, Long modelId) {
        ExcelExportVo result = excelExportService.exportProjectIng(ProjectStatusEnum.ING.status(), null, modelId);
        List<String> headList = result.getHeadList();
        List<Object[]> valueList = result.getValueList();

        // 定义表的标题
        String title = "费用";
        //定义表的列名
        String[] rowsName = headList.toArray(new String[headList.size()]);

        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String fileName = new String("项目成本导出表.xlsx".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, valueList, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/project/decomposition")
    @ApiOperation(value = "进行中项目-细化分解导出")
    public void projectDecomposition(HttpServletResponse response, String projectId) throws IOException {
        String fileName = new String("进行中项目-细化分解导出.xlsx".getBytes("UTF-8"), "iso-8859-1");
        HSSFWorkbook workbook = excelExportService.exportProjectDecomposition(projectId);
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=" + fileName);
        response.setContentType("application/msexcel");
        workbook.write(output);
    }

    @PostMapping("/projectList")
    @ApiOperation(value = "能导出的项目")
    public Wrapper<?> projectList(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate
            , @RequestParam("deptidList") String deptidList, Long modelId){
        Date startDateTime = null;
        if (StrUtil.isNotEmpty(startDate)) {
            startDateTime = DateUtil.parseDate(startDate + " 00:00:00");
        }
        Date endDateTime = null;
        if (StrUtil.isNotEmpty(endDate)) {
            endDateTime = DateUtil.parseDate(endDate + " 59:59:59");
        }
        //JSON解析
        List<String> parseList = JSON.parseArray(deptidList, String.class);
        List<Project> list = excelExportService.projectList(startDateTime, endDateTime, parseList, modelId);
        return WrapMapper.ok(list);
    }

    @PostMapping("/project/history")
    @ApiOperation(value = "历史项目-项目导出")
    public void historyProject(HttpServletResponse response, @Validated @RequestBody HistoryParam param) throws IOException {
//        String fileName = new String("进行中项目-细化分解导出.xlsx".getBytes("UTF-8"), "iso-8859-1");
        String fileName = new String("历史项目.zip".getBytes("UTF-8"), "iso-8859-1");
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=" + fileName);
        response.setContentType("application/msexcel");
        //JSON解析
        List<String> list = JSON.parseArray(param.getProjectIdList(), String.class);
        excelExportService.historyProject(response, list, param.getType(), param.getModelId());
    }

    @GetMapping("test1")
    @ApiOperation(value = "测试")
    public String exportJiesuanProject(HttpServletResponse response) {
        excelExportService.exportJiesuanProject("ZJZY20201001");

        return System.currentTimeMillis()+"";
    }

    @GetMapping("exportWordData2")
    @ApiOperation(value = "excel导出测试2")
    public void exportWordData2(HttpServletResponse response) {

        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String title = "测试标题";
            List<String> headList = new ArrayList<>();
            headList.add("日期");
            headList.add("午别");
            List<List<ExcelUtil.DataColumn>> dataList= new ArrayList<>();
            List<ExcelUtil.DataColumn> value = new ArrayList<>();
            value.add(new ExcelUtil.DataColumn("2020-10-28",2));
            value.add(new ExcelUtil.DataColumn("上午"));
            dataList.add(value);

            List<ExcelUtil.DataColumn> value2 = new ArrayList<>();
            value2.add(new ExcelUtil.DataColumn("2020-10-28"));
            value2.add(new ExcelUtil.DataColumn("下午"));
            dataList.add(value2);
            String fileName = new String("测试excel文档.xlsx".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名

            excelUtil.exportComplexExcel(title, headList, dataList, fileName, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("exportWordData")
    @ApiOperation(value = "excel导出测试")
    public void exportExcelData(HttpServletResponse response) {
        // 定义表的标题
        String title = "员工列表一览";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "姓名", "性别", "特长", "学历", "入职时间", "简历", "照片", "部门"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = new Object[9];
        objs[0] = "测试";
        objs[1] = 11;
        objs[2] = "111";
        objs[3] = "测试";
        objs[4] = "测试";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(2018 - 12 - 24);
        objs[5] = date;
        objs[6] = "测试";
        objs[7] = "测试";
        objs[8] = "测试";
        dataList.add(objs);
        dataList.add(objs);
        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String fileName = new String("测试excel文档.xlsx".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
