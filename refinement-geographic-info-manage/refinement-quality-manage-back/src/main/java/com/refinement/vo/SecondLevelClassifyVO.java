package com.refinement.vo;

import com.refinement.data.ClassifyCategoryDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SecondLevelClassifyVO implements Serializable {

    private List<ClassifyCategoryDO> categoryList;

}
