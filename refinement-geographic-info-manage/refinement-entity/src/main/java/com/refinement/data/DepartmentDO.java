package com.refinement.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentDO implements Serializable {

    /**
     * 事业部id
     */
    private String deptid;

    /**
     * 事业部名称
     */
    private String deptname;

}
