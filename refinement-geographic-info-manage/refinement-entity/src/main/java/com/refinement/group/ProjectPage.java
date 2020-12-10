package com.refinement.group;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectPage implements Serializable {

    /**
     * 项目id
     */
    private String projectid;

    /**
     * 项目状态
     */
    private Long state;

    /**
     * 项目名称
     */
    private String projectname;

    /**
     * 事业部id
     */
    private String deptid;

    /**
     * 开始时间
     */
    private String startdate;

    /**
     * 结束时间
     */
    private String enddate;

    /**
     * 分页参数
     */
    private Integer page;

    /**
     * 一级分类id
     */
    private Long classifyId;

    /**
     * 二级分类id
     */
    private Long categoryId;

}
