package com.refinement.vo;

import com.refinement.data.AllClassifyCategoryDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AllClassifyCategoryVO implements Serializable {

    private List<AllClassifyCategoryDO> classifyList;

}
