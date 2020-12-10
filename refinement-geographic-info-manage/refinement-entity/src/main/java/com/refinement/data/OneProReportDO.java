package com.refinement.data;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OneProReportDO implements Serializable {

    private Long id;

    private String projectid;

    private Integer reportnums;

    private Integer deptCheck;

    private String deptReason;

    private Integer pdCheck;

    private String pdReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
