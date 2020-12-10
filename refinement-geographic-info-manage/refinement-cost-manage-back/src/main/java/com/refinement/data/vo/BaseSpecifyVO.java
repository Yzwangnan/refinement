package com.refinement.data.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BaseSpecifyVO {

    @ApiModelProperty("预算金额")
    private String budgetAmount;

    @ApiModelProperty("已使用")
    private BigDecimal isUse;

    @ApiModelProperty("上报人")
    private String reportPer;

    @ApiModelProperty("最后上报时间")
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDateTime reportTime;

    @ApiModelProperty("本月费用")
    private BigDecimal monthCost;

    @ApiModelProperty("余额")
    private BigDecimal over;

    @ApiModelProperty("审评状态 0-未审评 1-通过 2-不通过")
    private Integer state;

    @ApiModelProperty("是否需要修改 0-否 1-是 需要修改项标红显示")
    private Integer needReport;

    @ApiModelProperty("是否需要审评 0-否 1-是")
    private Integer needVerify;
}
