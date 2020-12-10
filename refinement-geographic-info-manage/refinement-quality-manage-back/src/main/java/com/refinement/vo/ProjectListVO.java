package com.refinement.vo;

import com.refinement.group.ProjectDept;
import com.refinement.http.PageResult;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProjectListVO implements Serializable {

    // 总项目数
    private Long projectNum;

    private List<ProjectDept> projectList;

    private PageResult pageInfo;

    //所有合同的总值
    private BigDecimal totalContractValue;

    //当月完成产值
    private BigDecimal monthValue;

    //累计完成产值
    private BigDecimal totalValue;

    //剩余合同额
    private BigDecimal surplusContractValue;

}
