package com.refinement.http;

public enum DefaultResponseCode implements ResponseCode {

    /**
     * NOT_LOGGED_IN
     */
    NOT_LOGGED_IN(1001, "用户未登录"),

    /**
     * INSUFFICIENT_AUTHORITY
     */
    INSUFFICIENT_AUTHORITY(1002, "权限不足"),

    REQUEST_TIMEOUT(1003, "请求超时"),

    SUCCESS(200, "请求成功"),

    LOGIN_SUCCESS(200, "登录成功"),

    LOGIN_FAIL(201, "登录失败"),

    TOKEN_TIME_OUT(300, "==== token 过期 ====="),

    TOKEN_PARSE_EXCEPTION(300, "==== token 解析异常 ====="),

    USER_NOT_EXIST(201, "用户不存在"),

    PASSWORD_NOT_CORRECT(201, "密码错误"),

    PASSWORD_CAN_NOT_CONSISTENT(201, "新密码不能与旧密码一致"),

    NOT_FOUND(404, "数据不存在"),

    ERROR(500, "网络异常"),

    FAIL(500, "请求失败"),

    ILLEGAL_ARGUMENT(1000, "参数异常"),

    ILLEGAL_ARGUMENT_CUSTOM(1000, "{}"),

    DELETE_SUCCESS(200, "删除成功"),

    SAVE_SUCCESS(200, "保存成功"),

    OPERATE_SUCCESS(200, "操作成功"),

    DELETE_FAIL(1004, "删除失败"),

    SAVE_FAIL(1005, "保存失败"),

    UPDATE_SUCCESS(200, "修改成功"),

    INCORRECT_CREDENTIAL(201, "此帐户凭据不正确或已过期"),

    ACCOUNT_IS_DISABLED(201, "账号不可用"),

    USER_OF_ROOT_CAN_NOT_DELETE(201, "root角色用户无法删除"),

    CANT_DELETE_MYSELF(201, "不能删除自己"),

    RECORDER_ROLE_ID_MISS(201, "记录员角色未选择"),

    AUDITOR_ROLE_ID_MISS(201, "审核员角色未选择"),

    BUDGET_SCALE_MISS(201, "三级分类默认预算比例不能空"),

    BUDGET_SCALE_NOT_EQUAL_HUNDRED(201, "默认预算比例总和应为100"),

    TWO_LEVEL_NAME_MISS(201, "二级分类不能空"),

    START_TIME_CAN_NOT_MORE_THAN_END_TIME(201, "每月开始时间不能大于每月结束时间"),

    ORGAN_NOT_EXIST(201, "组织不存在"),

    PARENT_ORGAN_NOT_EXIST(201, "父组织不存在"),

    DEL_ORGAN_NOT_EXIST(201, "要删除组织不存在"),

    ORGAN_NAME_IS_EXIST(201, "该组织名称已存在"),

    CANT_DELETE_MYSELF_ORGAN(201, "不能删除自己组织"),

    ORGAN_IS_EXIST(201, "组织已存在"),

    DIVIDE_ZERO_EXCEPTION(201, "零或空值不能作为除数"),

    ROOT_ROLE_CANNOT_DELETE(1002,"无法删除root角色"),

    ROOT_ROLE_CANNOT_UPDATE(1002,"root角色不可修改"),

    CANT_DELETE_MYSELF_ROLE(1002, "不能删除自己角色"),

    CANT_UPDATE_MYSELF_ROLE(1002, "不能修改自己角色"),

    NO_ROLE(1002,"无此角色"),

    ROLE_IS_EXIST(201, "角色已存在"),

    PROJECT_NAME_IS_EXIST(201, "项目名称已存在"),

    PROJECT_IS_NOT_EXIST(201, "项目不存在"),

    MODEL_IS_NOT_EXIST(201, "模板不存在"),

    ONE_LEVEL_COST_CAN_NOT_EMPTY(201, "一级分类预算不能为空"),

    TWO_LEVEL_COST_CAN_NOT_EMPTY(201, "二级分类预算不能为空"),

    ONE_LEVEL_BUDGET_SHOULD_NOT_MORE_THAN_CONTRACT_VALUE(201, "一级预算总和不能超过合同总额"),

    THREE_LEVEL_BUDGET_SHOULD_EQUAL_TOTAL(201, "三级预算总和共与其二级预算应相等"),

    BUDGET_SHOULD_EQUAL(201, "一级预算应与二级预算总和应相等"),

    BUDGET_SHOULD_NOT_MORE(201, "二级预算总和应相等不能超过一级预算"),

    THERE_IS_NOT_TWO_LEVEL(201, "请添加三级分类对应的二级分类项"),

    ONE_LEVEL_COST_NOT_EXIST(201, "一级分类项不存在"),

    TWO_LEVEL_COST_NOT_EXIST(201, "二级分类项不存在"),

    THIS_PROJECT_NOT_DECOMPOSITION(201, "请先完成细化分解"),

    PROJECT_IS_TIME_OUT(201, "项目工期到期，无法上报"),

    REPORT_TIME_OUT(201, "本月已超过项目上报日期，无法上报"),

    REPORT_AMOUNT_MORE(201, "上报金额不能超过余额"),

    REPORT_AMOUNT_MISS(201, "本月上报费用不能为空"),

    SUP_ITEM_SHOULD_EQUAL_SUB_ITEM(201, "分项费用应等于其子项费用总和"),

    USER_NAME_IS_EXIST(201, "用户名已存在"),

    ONE_LEVEL_NAME_CAN_NOT_EMPTY(201, "一级分类名称不能为空"),

    TWO_LEVEL_NAME_CAN_NOT_EMPTY(201, "二级分类名称不能为空"),

    THREE_LEVEL_NAME_CAN_NOT_EMPTY(201, "三级分类名称不能为空"),

    ORGANIZATION_MISS(201, "请先添加生产运营部组织"),

    ORGANIZATION_NAME_NOT_SAME(201, "部门名称重复，请更换"),

    BUDGET_AMOUNT_NOT_EMPTY(201, "预算金额不能为空"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    PROJECT_BUDGET_AMOUNT_IS_EMPTY(201, "该项目的预算金额为空"),

    MODEL_IS_USE(201, "该模板存在正在使用中的项目，暂无法删除"),

    MODEL_IS_USE_OF_NEW_PROJECT(201, "该模板已在新项目中使用"),

    REPORT_TIME_NOT_ARRIVED(201, "月报上报日期未到"),

    SHOW_POINT(202, "");

    int code;

    String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    DefaultResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
