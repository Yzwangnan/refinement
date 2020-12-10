package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class OrganizationAddParam {

    @ApiModelProperty("组织id")
    private Long id;

    @ApiModelProperty("组织名称")
    @NotBlank(message = "组织名称不能为空")
    @Size(max = 20, message = "组织名称最大长度为20")
    private String name;

}
