package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyReportParam {

    @ApiModelProperty("分项id")
    private Long id;

    @ApiModelProperty("本月费用")
    private BigDecimal amount;

    @ApiModelProperty("余额")
    private String over;

    @ApiModelProperty("是否需要上报/修改")
    private Integer needReport;
}
