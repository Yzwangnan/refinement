package com.refinement.data.vo;

import com.refinement.http.PageResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserPageVO {

    @ApiModelProperty("用户列表")
    private List<UserVO> userList;

    @ApiModelProperty("分页参数")
    private PageResult pageInfo;
}
