package com.refinement.vo;

import com.refinement.data.DepartmentDO;
import com.refinement.http.PageResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DepartmentVO implements Serializable {

    private PageResult pageInfo;

    private List<DepartmentDO> deptList;

}
