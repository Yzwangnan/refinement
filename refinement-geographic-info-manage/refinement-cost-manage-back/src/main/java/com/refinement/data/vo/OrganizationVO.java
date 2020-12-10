package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrganizationVO {

    @ApiModelProperty("组织id")
    private Long id;

    @ApiModelProperty("组织名称")
    private String organizationName;
}
