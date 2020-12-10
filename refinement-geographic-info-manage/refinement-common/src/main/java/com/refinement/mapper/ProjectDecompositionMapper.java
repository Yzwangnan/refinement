package com.refinement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.refinement.data.DecompositionExportDO;
import com.refinement.data.DecompositionLevelExportDO;
import com.refinement.entity.ProjectDecomposition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gen
 * @since 2020-10-19
 */
public interface ProjectDecompositionMapper extends BaseMapper<ProjectDecomposition> {

    /**
     * 查询一级分类统计
     * @param modelId
     * @return
     */
    List<DecompositionExportDO> oneLevelExport(@Param("projectId") String projectId,
                                               @Param("modelId") Long modelId);

    /**
     * 查询
     * @param projectId
     * @param level
     * @return
     */
    List<DecompositionLevelExportDO> listByProjectIdAndLevel(@Param("projectId") String projectId, @Param("level") Integer level
            , @Param("oneLevelName") String oneLevelName, @Param("twoLevelName") String twoLevelName);
}
