package com.refinement.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 * 
 * @author ruoyi
 */
public class TreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 是否匹配 **/
    private Integer isMatch;

    /**
     * 父级组织id
     */
    private Long parentId;

    /**
     * 1-一级 2-二级..... n-n级
     */
    private Integer level;

    private Integer deptFlag;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect()
    {

    }

    public TreeSelect(OrganizationEntity organization)
    {
        this.id = organization.getId();
        this.label = organization.getOrganizationName();
        this.parentId = organization.getParentId();
        this.level = organization.getLevel();
        this.deptFlag = organization.getDeptFlag();
        this.isMatch = organization.getIsMatch() == null ? 0:organization.getIsMatch();
        this.children = organization.getChildren().stream().map(com.refinement.entity.TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(RoleEntity roleEntity)
    {
        this.id = roleEntity.getId();
        this.label = roleEntity.getRoleName();
        this.parentId = roleEntity.getParentId();
//        this.level = roleEntity.getLevel();
        this.isMatch = roleEntity.getIsMatch() == null ? 0:roleEntity.getIsMatch();
        this.children = roleEntity.getChildren().stream().map(com.refinement.entity.TreeSelect::new).collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelect> children)
    {
        this.children = children;
    }

    public Integer getIsMatch() { return isMatch; }

    public void setIsMatch(Integer isMatch) { this.isMatch = isMatch; }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDeptFlag() {
        return deptFlag;
    }

    public void setDeptFlag(Integer deptFlag) {
        this.deptFlag = deptFlag;
    }
}
