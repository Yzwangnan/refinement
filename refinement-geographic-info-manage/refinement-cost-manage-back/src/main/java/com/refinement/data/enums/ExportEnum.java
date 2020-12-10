package com.refinement.data.enums;

/**
 * 导出 0-新项目导出 1-进行中项目导出 2-进行中项目细化导出 3-历史项目导出
 */
public enum ExportEnum {

    NEW_PROJECT_EXPORT(0, "/cost/excel/project/new"),

    PROGRESS_PROJECT_EXPORT(1, "/cost/excel/project/ing"),

    PROGRESS_PROJECT_SPECIFY_EXPORT(2, "/cost/excel/project/decomposition"),

    HISTORY_PROJECT_EXPORT(3, "/cost/excel/project/history");


    private final Integer status;
    private final String url;

    ExportEnum(Integer status, String url) {
        this.status = status;
        this.url = url;
    }

    public Integer status() {
        return status;
    }

    public Object getKey() {
        return status;
    }

    public String getValue() {
        return url;
    }
}
