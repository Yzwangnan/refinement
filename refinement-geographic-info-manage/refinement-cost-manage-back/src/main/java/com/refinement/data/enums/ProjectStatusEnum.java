package com.refinement.data.enums;


/**
 * 项目类型 0-新项目 1-进行中项目 2-历史项目
 */
public enum ProjectStatusEnum {

    NEWS(0, "新项目"),
    ING(1, "进行中项目"),
    HISTORY(2, "历史项目"),
    ;

    private final Integer status;
    private final String desc;

    ProjectStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }


    public Integer status() {
        return status;
    }

    public Object getKey() {
        return status;
    }

    public String getValue() {
        return desc;
    }
}
