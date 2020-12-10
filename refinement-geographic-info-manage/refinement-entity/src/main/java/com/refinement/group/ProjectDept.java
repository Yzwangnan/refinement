package com.refinement.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDept implements Serializable {

    private Long id;

    private String projectid;

    private String projectname;

    private BigDecimal contractvalue;

    private String deptname;

    @JsonFormat(pattern = "yyyy.MM.dd", timezone = "GMT+8")
    private Date startdate;

    private String period;

    private Long userid;

    //区域
    private String region;

    private int state;

    private String category;

    // 每月上报日期
    private Integer reportday;

    // 本月上报状态
    private Integer reportstate;

    // 事业部审核 0-未审核、1-通过、2-未通过
    @JsonIgnore
    private Integer deptCheck;

    // 生产运营部审核 0-未审核、1-通过、2-未通过
    @JsonIgnore
    private Integer pdCheck;

    private String deptid;

    //项目完成产值
    private BigDecimal totalCompletedValue;

    private List<String> proSpecifyList;

    //时间进度
    private BigDecimal timeSchedule;
}
