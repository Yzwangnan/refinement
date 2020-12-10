package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TwoLevelComputeVO {

    @ApiModelProperty("细化项id")
    private Long id;

    @ApiModelProperty("二级分项名称")
    private String twoLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;

    @ApiModelProperty("三级列表")
    private List<ThreeLevelComputeVO> computeList;
}
