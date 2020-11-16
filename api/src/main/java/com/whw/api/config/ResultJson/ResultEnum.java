package com.whw.api.config.ResultJson;

public enum ResultEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    PRIMARY_SCHOOL(500, "系统错误"),
    MUST_DATA(501, "缺少必填参数"),
    NOT_USER(502, "登录过期，请重新登录"),
    LOGIN_USER_NOT(508, "登录过期，请重新登录"),
    SMS_PASS(503, "验证码已过期，请重新获取"),
    SMS_ERROR(504, "验证码错误"),
    EMPTY_MOBILE(505, "手机号码不能为空"),
    HAVING_MOBILE(506, "当前手机号码已存在，请更换手机号尝试"),
    NOT_LOGIN_AUTH(509, "当前手机号码，无登录权限"),
    NOT_FIND_USER(507, "用户名账号/密码错误！"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

