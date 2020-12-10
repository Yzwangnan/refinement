package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MonthlyReportTitleVO {

    @ApiModelProperty("月份")
    private Integer month;

    @ApiModelProperty("是否需要审评（审评员）/是否需要修改（记录员） 0-否 1-是")
    private Integer needVerify;
}
