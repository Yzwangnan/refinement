package com.refinement.data;

import com.refinement.entity.Project;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectListDO implements Serializable {

    private Project project;

    /**
     * 总项目数
     */
    private Long projectNum;

}
