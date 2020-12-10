package com.refinement.vo;

import com.refinement.entity.ProjectReport;
import com.refinement.group.SpecifyRe;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectSpeReportVO implements Serializable {

    private ProjectReport reportInfo;

    private List<SpecifyRe> specifyList;

}
