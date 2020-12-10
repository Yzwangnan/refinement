package com.refinement.controller;

import com.refinement.data.DepartmentDO;
import com.refinement.http.ResultDTO;
import com.refinement.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wn
 * @since 2020-04-22
 */
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
@Api(tags = "事业部")
@Validated
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 事业部列表接口
     *
     * @param page 页
     * @param size 大小
     * @return ResultDTO
     */
    @PostMapping("/list")
    @ApiOperation(value = "list", notes = "事业部列表")
    public ResultDTO list(@RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        return departmentService.listDepts(page, size);
    }

    /**
     * 添加事业部接口
     *
     * @param deptname 事业部名称
     * @param password 密码
     * @return ResultDTO
     */
    @PostMapping("/addDept")
    @ApiOperation(value = "addDept", notes = "新建事业部")
    public ResultDTO addDept(String deptname, String password) {
        //参数校验 password
        if (StringUtils.isEmpty(password)) {
            return new ResultDTO(201, "密码不能为空");
        }
        //参数校验 deptname
        if (StringUtils.isEmpty(deptname)) {
            return new ResultDTO(201, "事业部名称不能为空");
        }
        //参数deptname不能过长
        if (deptname.length() > 45) {
            return new ResultDTO(201, "事业部名称过长");
        }
        return departmentService.addDept(deptname, password);
    }

    /**
     * 不带分页获取事业部列表接口
     *
     * @return ResultDTO
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "findAll", notes = "无分页下查询所有部门")
    public ResultDTO findAll() {
        List<DepartmentDO> departmentDOList = departmentService.selectAllDeptList();
        if (departmentDOList == null || departmentDOList.size() == 0) {
            return new ResultDTO(200, "无部门信息");
        }
        return new ResultDTO(departmentDOList);
    }

    /**
     * 创建事业部id接口
     *
     * @return ResultDTO
     */
    @GetMapping("/createDeptId")
    @ApiOperation(value = "createDeptId", notes = "创建事业部ID")
    public ResultDTO createDeptId() {
//        List<String> deptIds = departmentService.listDeptIds();
//        //返回的事业部ID
//        String deptid;
//
//        if (deptIds == null || deptIds.size() == 0) {
//            deptid = "ZJZY01";
//        } else {
//            //最新的事业部ID
//            String uptodateDeptId = deptIds.get(0);
//            String str = uptodateDeptId.substring(4);
//            int number = Integer.parseInt(str);
//
//            //个位数
//            if (number < 9) {
//                deptid = "ZJZY" + "0" + (number + 1);
//            } else {
//                deptid = "ZJZY" + (number + 1);
//            }
//        }
        return new ResultDTO();
    }
}

