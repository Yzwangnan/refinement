package com.refinement.data.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserVO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("组织id")
    private Long organizationId;

    @ApiModelProperty("所属组织")
    private String organization;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色选中列表")
    private List<RoleVO> roleList;
}
