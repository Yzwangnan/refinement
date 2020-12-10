package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThreeLevelComputeVO {

    @ApiModelProperty("细化项id")
    private Long id;

    @ApiModelProperty("三级分项名称")
    private String threeLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;
}
