package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyReportModelVO {

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("一级分类")
    private String oneLevelName;

    @ApiModelProperty("二级分类")
    private String twoLevelName;

    @ApiModelProperty("三级分类")
    private String threeLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;

    @ApiModelProperty("已使用")
    private BigDecimal isUse;

    @ApiModelProperty("余额")
    private BigDecimal over;

    @ApiModelProperty("是否需要上报 0-否 1-是 不需要上报的项禁用输入框")
    private Integer needReport;
}
