package com.refinement.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeptProVO implements Serializable {

    private Long userid;

    private String username;

    private Integer type;

    private String deptid;

    private String deptname;

    private String projectid;

    private String projectname;

}
