package com.refinement.data.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ProjectVO {

    @ApiModelProperty("序号")
    private Integer order;

    @ApiModelProperty("项目id")
    private String projectid;

    @ApiModelProperty("项目名称")
    private String projectname;

    @ApiModelProperty("合同产值")
    private BigDecimal contractvalue;

    @ApiModelProperty("所属事业部")
    private String deptName;

    @ApiModelProperty("开工日期")
    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private LocalDate startdate;

    @ApiModelProperty("工期")
    private Integer period;

    @ApiModelProperty("项目类型")
    private String category;

    @ApiModelProperty("区域（省）")
    private String region;

    @ApiModelProperty("实施状态 0-新项目 1-进行中项目 2-历史项目")
    private String state;

    @ApiModelProperty("预算金额")
    private BigDecimal budgetAmount;

    @ApiModelProperty("预算金额已使用")
    private BigDecimal isUse;

    @ApiModelProperty("预算金额使用比例")
    private BigDecimal scale;

    @ApiModelProperty("分项列表，根据标题变化而变化")
    private List<String> decompositionList;

    @ApiModelProperty("月报情况" +
            "1->待审核" +
            "2->驳回" +
            "3->已报" +
            "4->未报" +
            "5->超时")
    private Integer monthlyReport;

    @ApiModelProperty("实际利润")
    private BigDecimal profit;

    @ApiModelProperty("模板id")
    private Long modelId;
}
