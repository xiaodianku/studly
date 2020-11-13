package com.whw.api.config;

/**
 * 常量
 *
 * @author MrBird
 */
public class FebsConstant {

    // 排序规则：降序
    public static final String ORDER_DESC = "desc";
    // 排序规则：升序
    public static final String ORDER_ASC = "asc";

    // 前端页面路径前缀
    public static final String VIEW_PREFIX = "febs/views/";

    // 验证码 Session Key
    public static final String CODE_PREFIX = "febs_captcha_";

    // 图形验证码
    public static final String IMAGE_CATCHA = "captcha";

    // 短信验证码
    public static final String SMS_CATCHA = "sms_captcha";

    // 允许下载的文件类型，根据需求自己添加（小写）
    public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

    /**
     * session中存放用户信息的key值
     */
    public static final String SESSION_USER_INFO = "userInfo";
    public static final String SESSION_USER_ID= "userId";
    public static final String SESSION_USER_PERMISSION = "userPermission";

    public static final String REDIRECT_URL_BY_SQ="https://sc.lifebenefit.cn/system/receive/callBackSq";

}
