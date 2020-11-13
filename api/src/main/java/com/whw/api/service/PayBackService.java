package com.whw.api.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface PayBackService {

    Map<String,String> xcxPayment(JSONObject jsonObject) throws Exception;

    String xcxNotify(String notityXml, String ip) throws Exception;
}
