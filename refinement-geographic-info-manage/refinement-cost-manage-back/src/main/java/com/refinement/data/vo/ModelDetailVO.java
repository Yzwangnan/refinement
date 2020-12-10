package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelDetailVO {

    @ApiModelProperty("分解项id")
	private Long id;

    @ApiModelProperty("一级分项")
	private String oneLevelName;

    @ApiModelProperty("二级分项")
	private String twoLevelName;

    @ApiModelProperty("三级分项")
	private String threeLevelName;

    @ApiModelProperty("默认预算比例")
	private String budgetScale;

    @ApiModelProperty("记录员角色")
	private String recorderRole;

    @ApiModelProperty("审核员角色")
	private String auditorRole;

    @ApiModelProperty("模板级别")
    private Integer level;

    @ApiModelProperty("预算金额")
    private String budgetAmount;
}
