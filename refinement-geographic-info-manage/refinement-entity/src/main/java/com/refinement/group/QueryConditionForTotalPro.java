package com.refinement.group;

import lombok.Data;

/**
 * @Description 用来承载查询符合特定状态和部门的Project
 * @Author bobli
 * @Date 2020/8/29 10:07
 * @Version V1.0
 */
@Data
public class QueryConditionForTotalPro {
    /* 部门ID*/
    private String deptId;
    /* 项目状态*/
    private Long state;
}
