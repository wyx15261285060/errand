package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),
    PARAM_ERROR("400", "参数异常"),
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("401", "token验证失败，请重新登录"),
    PARAM_LOST_ERROR("4001", "参数缺失"),
    SYSTEM_ERROR("500", "系统异常"),
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    ROLE_ERROR("5006", "角色错误"),
    DIFFERENT_PASSWORD("5007","两次密码不一致"),
    PERSONAL_ACCOUNT_LIMIT_ERROR("5008","账户余额不足，请充值"),

    CERTIFICATION_ERROR("5009","您已提交过申请"),
    PHONE_IS_EXIST("5010","手机号已存在，请更换手机号"),
    CODE_ERROR("5011","验证码错误"),
    CODE_NOT_EXIST("5012","验证码不可为空")
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
