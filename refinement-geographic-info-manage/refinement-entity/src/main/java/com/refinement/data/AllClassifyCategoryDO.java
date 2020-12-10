package com.refinement.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AllClassifyCategoryDO implements Serializable {

    private Long id;

    private String name;

    private List<ClassifyCategoryDO> categoryList;

}
