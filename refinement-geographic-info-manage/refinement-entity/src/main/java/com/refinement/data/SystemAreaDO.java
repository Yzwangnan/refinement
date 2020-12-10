package com.refinement.data;

import lombok.Data;

@Data
public class SystemAreaDO {

    private Long id;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域短称
     */
    private String areaShortName;

    /**
     * 编号
     */
    private String areaCode;

}
