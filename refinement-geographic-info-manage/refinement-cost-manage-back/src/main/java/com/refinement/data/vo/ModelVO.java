package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelVO {

    @ApiModelProperty("模板id")
    private Long id;

    @ApiModelProperty("模板名称")
    private String modelName;

    @ApiModelProperty("模板状态 1->使用中 2->未使用")
    private Integer status;

    @ApiModelProperty("模板数量")
    private Integer number;
}
