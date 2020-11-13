//package com.whw.api.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.whw.api.config.ResultJson.GirlException;
//import com.whw.api.config.ResultJson.ResultUtil;
//import com.whw.api.service.PayBackService;
//import com.whw.api.util.wx.WxUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.math.BigDecimal;
//import java.util.Map;
//
//@RestController
//@Slf4j
//@RequestMapping("/payBack")
//public class PayBackController extends BaseController {
//
//    @Autowired
//    private PayBackService payBackService;
//
//    @Autowired
//    private WxUtil wxUtil;
//
//
//    /**
//     * <p>统一下单入口</p>
//     *
//     * @param request
//     * @throws Exception
//     */
//    @RequestMapping(value="toPay", method= RequestMethod.POST)
//    public Object toPay(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws Exception {
//        //判断
//        Integer userId = jsonObject.getInteger("userId");
//        if(userId == null) throw new GirlException("用户不存在");
//        Integer courseId = jsonObject.getInteger("courseId");
//        if(courseId == null) throw new GirlException("课程不存在");
//        String enrollName = jsonObject.getString("enrollName");
//        if(StringUtils.isEmpty(enrollName)) throw new GirlException("报名名称不能为空");
//        String enrollPhone = jsonObject.getString("enrollPhone");
//        if(StringUtils.isEmpty(enrollPhone)) throw new GirlException("家长电话不能为空");
//        String time = jsonObject.getString("time");
//        if(StringUtils.isEmpty(time)) throw new GirlException("预约时间不能为空");
//        Integer activeId = jsonObject.getInteger("activeId");
//        if(activeId == null) throw new GirlException("活动不存在");
//        //获取客户端的ip地址
//        jsonObject.put("_ip", WxUtil.getIpAddr(request));
//        Map<String,String> map = payBackService.xcxPayment(jsonObject);
//        return ResultUtil.success(map);
//    }
//
//    /**
//     * <p>回调Api</p>
//     *
//     * @param request
//     * @param response
//     * @throws Exception
//     */
//    @RequestMapping(value="xcxNotify")
//    public void xcxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        log.info("========================进入回调xcxNotify=========================");
//        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
//        String line = null;
//        StringBuilder sb = new StringBuilder();
//        while((line = br.readLine()) != null){
//            sb.append(line);
//        }
//        br.close();
//        //sb为微信返回的xml
//        String notityXml = sb.toString();
//        log.info("=========接收到的报文==========");
//        log.info(notityXml);
//        String ip = WxUtil.getIpAddr(request);
//        String resXml = payBackService.xcxNotify(notityXml, ip);
//        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//        out.write(resXml.getBytes());
//        out.flush();
//        out.close();
//    }
//
//
//    @RequestMapping(value="test")
//    public void test(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        log.info("========================test=========================");
//        String orderNo = WxUtil.setTradeNo();
//        String openid="oT5Ds4k4JLHo4A3IMbnjedLy48Y0";
//        String desc="邀请返现";
//        BigDecimal money= new BigDecimal("0.01");
//        String _ip=WxUtil.getIpAddr(request);
//        wxUtil.payUserMoney(orderNo, openid, desc, money, _ip);
//    }
//
//}