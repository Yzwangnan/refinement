package com.refinement.data.param;

import com.refinement.annotion.PhoneNumber;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserUpdateParam {

    @ApiModelProperty("用户id")
    @NotNull(message = "userId 不能为空")
    private Long userId;

    @ApiModelProperty("手机号码")
    @NotBlank(message = "phone 不能为空")
    @PhoneNumber
    @Size(max = 20, message = "phone 最大长度为20")
    private String phone;

    @ApiModelProperty("备注")
    @NotBlank(message = "remark 不能为空")
    @Size(max = 50, message = "remark 最大长度为50")
    private String remark;

    @ApiModelProperty("部门id")
    @NotBlank(message = "deptid 不能为空")
    private String deptid;

    @ApiModelProperty("角色id数组")
    @NotNull(message = "roleIds 不能为空")
    private List<Long> roleIds;
}
