package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OneLevelSpecifyVO extends BaseSpecifyVO {

    @ApiModelProperty("一级id")
    private Long id;

    @ApiModelProperty("一级分项")
    private String oneLevelName;

    @ApiModelProperty("二级列表")
    private List<TwoLevelSpecifyVO> modelList;
}

