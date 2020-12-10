package com.refinement.data.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RoleAddParam {

    @ApiModelProperty("角色名称")
    @NotBlank(message = "roleName 不能为空")
    @Size(max = 100, message = "roleName 最大长度为100")
    private String roleName;

    @ApiModelProperty("角色简介")
    @Size(max = 100, message = "introduction 最大长度为100")
    private String introduction;

    @NotNull(message = "parentId 不能为空")
    private Long parentId;

    @ApiModelProperty("功能id数组")
    private List<Long> functionIds;
}
