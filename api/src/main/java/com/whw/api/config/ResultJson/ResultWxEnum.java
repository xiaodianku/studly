package com.whw.api.config.ResultJson;

/**
 * 微信错误集合
 */
public enum ResultWxEnum {
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    ERROR_85004(85004, "微信号已经绑定"),
    ERROR_85001(85001, "微信号不存在或微信号设置为不可搜索"),
    ERROR_85002(85002, "小程序绑定的体验者数量达到上限"),
    ERROR_85003(85003, "微信号绑定的小程序体验者达到上限"),
    ;

    private Integer code;

    private String msg;

    ResultWxEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static String getMsgByCode(int code){
        String str = "操作失败，请稍后再试";
        for (ResultWxEnum resultWxEnum : ResultWxEnum.values()) {
            if(code == resultWxEnum.code){
                str = resultWxEnum.msg;
                break;
            }
        }
        return str;
    }
}

