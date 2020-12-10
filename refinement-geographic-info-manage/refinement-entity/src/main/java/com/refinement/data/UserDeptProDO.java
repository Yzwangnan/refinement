package com.refinement.data;


import com.refinement.entity.Department;
import com.refinement.entity.Project;
import com.refinement.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDeptProDO implements Serializable {

    private User user;

    private Department department;

    private Project project;

}
