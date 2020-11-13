package com.whw.api.util.Sms;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.whw.api.util.HttpUtils;
import com.whw.api.util.MD5Util;
import com.whw.api.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 牛白发短信api
 */
@Slf4j
public class NiuBaiFa {

    static String USER_NAME ="hzdywl";
    static String PASSWORD ="hzdywl";
    static String URL ="http://api.niubaifa.com/api/send";
    public static String MESSAGE ="【发圈小助手】验证码：%s，您正在修改登录密码，验证码10分钟内有效，请勿泄露。";

    public static Map<String, Object> sendMessageMap(){
        Map<String, Object> map = new HashMap<>();
        long time = System.currentTimeMillis();
        map.put("timestamp", time);
        map.put("key", MD5Util.md5(String.valueOf(time) + PASSWORD+USER_NAME));
        map.put("user_name", USER_NAME);
        return map;
    }

    /**
     *  发送单条短信
     * @param mobile
     * @param message
     * @return
     */
    public static boolean sendSms(String mobile,  String  message){
       try {
           Long timestamp = new Date().getTime();
           String key = StringTools.getMD5(timestamp.toString() + PASSWORD + USER_NAME);

           Map<String, Object> map = new JSONObject();
           map.put("mobile", mobile);
           map.put("message", message);
           map.put("key", key);
           map.put("user_name", USER_NAME);
           map.put("timestamp", timestamp);
           String respose = HttpUtils.HttpPost(URL, map, null);
           if(respose == null || "".equals(respose)) {
               log.error("验证码发送失败"+ respose);
               return false;
           }
           JSONObject jsonObject = JSONObject.parseObject(respose);
           if(!jsonObject.containsKey("code") || jsonObject.getInteger("code") != 0){
               log.error("验证码发送失败"+ respose);
               return false;
           }
       }catch (Exception e){
           e.printStackTrace();
           log.error("验证码发送失败", e);
           return false;
       }
        return true;
    }


    public static void main(String[] args) {
        String message = String.format(NiuBaiFa.MESSAGE, "1234");
        sendSms("15260282340", message);
    }
}
