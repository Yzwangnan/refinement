package com.refinement.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/******************************************* 
 *
 * @Package com.cccuu.project.myUtils
 * @Author duan
 * @Date 2018/3/29 9:12
 * @Version V1.0
 *******************************************/
public class ExcelUtil {

    @Data
    @Accessors(chain = true)
    public static class DataColumn {

        /**
         * 值
         */
        private Object value;

        /**
         * 合并单元格数
         */
        private Integer columCount;

        public DataColumn() {
        }

        public DataColumn(Object value, int columCount) {
            this.value = value;
            this.columCount = columCount;
        }

        public DataColumn(Object value) {
            this.value = value;
        }
    }

    /**
     * 导出excel
     *
     * @param title    导出表的标题
     * @param rowsName 导出表的列名
     * @param dataList 需要导出的数据
     * @param fileName 生成excel文件的文件名
     * @param response
     */
    public void exportExcel(String title, String[] rowsName, List<Object[]> dataList, String fileName, HttpServletResponse response) throws Exception {
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=" + fileName);
        response.setContentType("application/msexcel");
        this.export(title, rowsName, dataList, fileName, output);
        this.close(output);

    }

    public HSSFWorkbook exportHSSFWorkbook(String title, String[] rowName, List<Object[]> dataList){
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
            HSSFRow rowm = sheet.createRow(0);  // 产生表格标题行
            HSSFCell cellTiltle = rowm.createCell(0);   //创建表格标题列
            // sheet样式定义;    getColumnTopStyle();    getStyle()均为自定义方法 --在下面,可扩展
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook); // 获取单元格样式对象
            //合并表格标题行，合并列数为列名的长度,第一个0为起始行号，第二个1为终止行号，第三个0为起始列好，第四个参数为终止列号
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            cellTiltle.setCellStyle(columnTopStyle);    //设置标题行样式
            cellTiltle.setCellValue(title);     //设置标题行值
            int columnNum = rowName.length;     // 定义所需列数
            HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                Object[] obj = dataList.get(i);   // 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);   // 创建所需的行数
                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = null;   // 设置单元格的数据类型
                    if (j == 0) {
                        cell = row.createCell(j, CellType.NUMERIC);
                        cell.setCellValue(obj[j].toString());
                    } else {
                        cell = row.createCell(j, CellType.STRING);
                        if (obj[j] != null) {
                            cell.setCellValue(obj[j].toString()); // 设置单元格的值
                        }
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }
            }

            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == CellType.STRING) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复杂的excel导出
     * @param title
     * @param headList
     * @param dataList
     * @param fileName
     * @param response
     * @throws Exception
     */
    public void exportComplexExcel(String title, List<String> headList, List<List<DataColumn>> dataList
            , String fileName, HttpServletResponse response) throws Exception {
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition",
                "attachment; filename=" + fileName);
        response.setContentType("application/msexcel");
        this.exportComplex(title, headList, dataList, fileName, output);
        this.close(output);

    }

    /**
     * 复杂的excel导出
     * @param title
     * @param headList
     * @param dataList
     * @param fileName
     * @param out
     */
    private void exportComplex(String title, List<String> headList, List<List<DataColumn>> dataList, String fileName, OutputStream out) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
            HSSFRow rowm = sheet.createRow(0);  // 产生表格标题行
            HSSFCell cellTiltle = rowm.createCell(0);   //创建表格标题列
            // sheet样式定义;    getColumnTopStyle();    getStyle()均为自定义方法 --在下面,可扩展
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook); // 获取单元格样式对象
            //合并表格标题行，合并列数为列名的长度,第一个0为起始行号，第二个1为终止行号，第三个0为起始列好，第四个参数为终止列号
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (headList.size() - 1)));
            cellTiltle.setCellStyle(columnTopStyle);    //设置标题行样式
            cellTiltle.setCellValue(title);     //设置标题行值
            int columnNum = headList.size();     // 定义所需列数
            HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(headList.get(n));
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                List<DataColumn> obj = dataList.get(i);   // 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);   // 创建所需的行数
                for (int j = 0; j < obj.size(); j++) {
                    HSSFCell cell = null;   // 设置单元格的数据类型
                    cell = row.createCell(j, CellType.STRING);
                    if (obj.get(j) != null) {
                        DataColumn data = obj.get(j);
                        cell.setCellValue(data.getValue().toString()); // 设置单元格的值
                        //设置合并单元格
                        // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
                        // 行和列都是从0开始计数，且起始结束都会合并
                        // 这里是合并excel中日期的两行为一行
                        if (data.getColumCount() != null) {
                            CellRangeAddress region = new CellRangeAddress(i+3, (i+3+data.getColumCount())-1, j, j);
//                            CellRangeAddress region = new CellRangeAddress(3, 4, 0, 0);
                            sheet.addMergedRegion(region);
                        }
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }
            }

            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
     * 导出数据
     */
    private void export(String title, String[] rowName, List<Object[]> dataList, String fileName, OutputStream out) throws Exception {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(); // 创建工作簿对象
            HSSFSheet sheet = workbook.createSheet(title); // 创建工作表
            HSSFRow rowm = sheet.createRow(0);  // 产生表格标题行
            HSSFCell cellTiltle = rowm.createCell(0);   //创建表格标题列
            // sheet样式定义;    getColumnTopStyle();    getStyle()均为自定义方法 --在下面,可扩展
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
            HSSFCellStyle style = this.getStyle(workbook); // 获取单元格样式对象
            //合并表格标题行，合并列数为列名的长度,第一个0为起始行号，第二个1为终止行号，第三个0为起始列好，第四个参数为终止列号
            sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, (rowName.length - 1)));
            cellTiltle.setCellStyle(columnTopStyle);    //设置标题行样式
            cellTiltle.setCellValue(title);     //设置标题行值
            int columnNum = rowName.length;     // 定义所需列数
            HSSFRow rowRowName = sheet.createRow(2); // 在索引2的位置创建行(最顶端的行开始的第二行)
            // 将列头设置到sheet的单元格中
            for (int n = 0; n < columnNum; n++) {
                HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
                cellRowName.setCellType(CellType.STRING); // 设置列头单元格的数据类型
                HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
                cellRowName.setCellValue(text); // 设置列头单元格的值
                cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
            }

            // 将查询出的数据设置到sheet对应的单元格中
            for (int i = 0; i < dataList.size(); i++) {
                Object[] obj = dataList.get(i);   // 遍历每个对象
                HSSFRow row = sheet.createRow(i + 3);   // 创建所需的行数
                for (int j = 0; j < obj.length; j++) {
                    HSSFCell cell = null;   // 设置单元格的数据类型
                    if (j == 0) {
                        cell = row.createCell(j, CellType.NUMERIC);
                        cell.setCellValue(obj[j].toString());
                    } else {
                        cell = row.createCell(j, CellType.STRING);
                        if (obj[j] != null) {
                            cell.setCellValue(obj[j].toString()); // 设置单元格的值
                        }
                    }
                    cell.setCellStyle(style); // 设置单元格样式
                }
            }

            // 让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < columnNum; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    // 当前行未被使用过
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (currentRow.getCell(colNum) != null) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == CellType.STRING) {
                            int length = currentCell.getStringCellValue()
                                    .getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }
            }
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 列头单元格样式
     */
    private HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
//        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    private HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置左边框;
        style.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置右边框;
        style.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 设置顶边框;
        style.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HorizontalAlignment.CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {

        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        HSSFSheet sheet = workbook.createSheet("sheet");

//        HSSFRow row0 = sheet.createRow(0);
//        HSSFCell cell_00 = row0.createCell(0);
//        cell_00.setCellStyle(style);
//        cell_00.setCellValue("日期");
//        HSSFCell cell_01 = row0.createCell(1);
//        cell_01.setCellStyle(style);
//        cell_01.setCellValue("午别");
//
//        HSSFRow row1 = sheet.createRow(1);
//        HSSFCell cell_10 = row1.createCell(0);
//        cell_10.setCellStyle(style);
//        cell_10.setCellValue("20180412");
//        HSSFCell cell_11 = row1.createCell(1);
//        cell_11.setCellStyle(style);
//        cell_11.setCellValue("上午");
//
//        HSSFRow row2 = sheet.createRow(2);
//        HSSFCell cell_21 = row2.createCell(1);
//        cell_21.setCellStyle(style);
//        cell_21.setCellValue("下午");
//
//        // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
//        // 行和列都是从0开始计数，且起始结束都会合并
//        // 这里是合并excel中日期的两行为一行
//        CellRangeAddress region = new CellRangeAddress(1, 5, 1,1);
        CellRangeAddress region = new CellRangeAddress(1, 5, 2,2);
//        sheet.addMergedRegion(region);

        HSSFRow row1 = sheet.createRow(0);
        HSSFCell cell_11 = row1.createCell(0);
        cell_11.setCellStyle(style);
        cell_11.setCellValue("承包费用");

//        HSSFRow row21 = sheet.createRow(0);
        HSSFCell cell_21 = row1.createCell(1);
        cell_21.setCellStyle(style);
        cell_21.setCellValue("外协");

//        HSSFRow row22 = sheet.createRow(1);
        HSSFCell cell_22 = row1.createCell(2);
        cell_22.setCellStyle(style);
        cell_22.setCellValue("工资");



        File file = new File(""+System.currentTimeMillis()+".xls");
        FileOutputStream fout = new FileOutputStream(file);
        workbook.write(fout);
        fout.close();
    }
}