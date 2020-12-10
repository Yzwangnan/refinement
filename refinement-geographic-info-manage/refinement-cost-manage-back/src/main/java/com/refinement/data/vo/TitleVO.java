package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TitleVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("对应参数")
    private String param;
}
