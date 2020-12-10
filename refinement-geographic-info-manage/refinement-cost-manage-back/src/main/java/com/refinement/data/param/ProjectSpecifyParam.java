package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectSpecifyParam {

    @ApiModelProperty("分项id")
    private Long id;

    @ApiModelProperty("分项预算")
    private String cost;
}
