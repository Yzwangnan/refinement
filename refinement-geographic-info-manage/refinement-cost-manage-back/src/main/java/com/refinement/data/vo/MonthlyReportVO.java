package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MonthlyReportVO {

    @ApiModelProperty("拥有月报功能记录员显示 “+” 号 0-隐藏 1-显示")
    private Integer showReport;

    @ApiModelProperty("月报标题列表")
    private List<MonthlyReportTitleVO> monthTitleList;
}
