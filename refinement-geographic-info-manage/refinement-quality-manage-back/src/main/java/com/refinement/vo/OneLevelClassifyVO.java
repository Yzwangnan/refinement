package com.refinement.vo;


import com.refinement.data.ClassifyCategoryDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OneLevelClassifyVO implements Serializable {

    private List<ClassifyCategoryDO> classifyList;

}
