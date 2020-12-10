package com.refinement.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassifyCategoryDO implements Serializable {

    private Long id;

    private String name;

    private Long classifyId;
}
