package com.refinement.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private Integer type;

    private String deptid;

    private String projectid;

    private String projectname;

    private Integer showComplete;

    private Integer showAddNewProject;
}
