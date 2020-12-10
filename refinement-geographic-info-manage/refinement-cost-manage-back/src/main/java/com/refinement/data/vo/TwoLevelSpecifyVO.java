package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TwoLevelSpecifyVO extends BaseSpecifyVO {

    @ApiModelProperty("二级id")
    private Long id;

    @ApiModelProperty("二级分项名称")
    private String twoLevelName;

    @ApiModelProperty("三级列表")
    private List<ThreeLevelSpecifyVO> modelList;
}
