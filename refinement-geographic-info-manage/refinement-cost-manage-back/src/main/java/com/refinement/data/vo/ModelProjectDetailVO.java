package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModelProjectDetailVO {

    @ApiModelProperty("模板id")
    private Long modelId;

    @ApiModelProperty("一级列表")
    private List<OneLevelDecompositionVO> modelList;
}
