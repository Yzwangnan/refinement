package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OneLevelDecompositionVO extends BaseDecompositionVO {

    @ApiModelProperty("一级分项id")
    private Long id;

    @ApiModelProperty("一级分项名称")
    private String oneLevelName;

    @ApiModelProperty("预算金额")
    private String budgetAmount;

    @ApiModelProperty("二级列表")
    private List<TwoLevelDecompositionVO> modelList;
}
