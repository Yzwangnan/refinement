package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectSpecifyVO {

    @ApiModelProperty("显示确认审评按钮 0-隐藏 1-显示")
    private Integer showVerify;

    @ApiModelProperty("显示确认修改按钮 0-隐藏 1-显示")
    private Integer showConfirm;

    @ApiModelProperty("标题列表")
    private List<TitleVO> titleList;

    @ApiModelProperty("细化分解项列表")
    private List<OneLevelSpecifyVO> specifyList;
}
