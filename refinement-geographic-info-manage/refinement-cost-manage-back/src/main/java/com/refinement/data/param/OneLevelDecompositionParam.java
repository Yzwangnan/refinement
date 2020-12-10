package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OneLevelDecompositionParam {

    @ApiModelProperty("分类名")
    private String oneLevelName;

    @ApiModelProperty("记录员id")
    private Long recorderRoleId;

    @ApiModelProperty("审评员id")
    private Long auditorRoleId;

    @ApiModelProperty("二级分项列表")
    private List<TwoLevelDecompositionParam> array;
}
