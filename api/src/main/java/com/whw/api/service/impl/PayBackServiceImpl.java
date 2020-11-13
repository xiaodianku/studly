//package com.whw.api.service.impl;
//
//import com.alibaba.fastjson.JSONObject;
//import com.whw.api.config.ResultJson.GirlException;
//import com.whw.api.dao.*;
//import com.whw.api.model.*;
//import com.whw.api.service.PayBackService;
//import com.whw.api.util.HttpUtils;
//import com.whw.api.util.MapAndXml;
//import com.whw.api.util.StringTools;
//import com.whw.api.util.wx.WxUtil;
//import com.whw.api.util.wx.wxpay.WXPayConstants;
//import com.whw.api.util.wx.wxpay.WXPayUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * 回调结果
// */
//@Service
//@Slf4j
//public class PayBackServiceImpl implements PayBackService {
//
//    @Autowired
//    private TbActiveOrderDao tbActiveOrderDao;
//
//    @Autowired
//    private TbActiveDao activeDao;
//
//    @Autowired
//    private TbUserDao tbUserDao;
//
//    @Autowired
//    private TbWaterDao tbWaterDao;
//
//    @Autowired
//    private TbCouponDao tbCouponDao;
//
//    @Autowired
//    private TbCouponUserDao tbCouponUserDao;
//
//    @Autowired
//    private WxUtil wxUtil;
//
//    @Override
//    public Map<String, String> xcxPayment(JSONObject jsonObject) throws Exception {
//        Map<String, String> backStr = new HashMap<>();
//        TbUser tbUser = tbUserDao.getOne(jsonObject.getIntValue("userId"));
//        if (tbUser == null) throw new GirlException("用户不存在");
//        TbActive active = activeDao.getOne(jsonObject.getIntValue("activeId"));
//        if (active == null) throw new GirlException("活动不存在");
//
//        //基础数据
//        String openid = tbUser.getOpenid();
//        log.info("【小程序支付】 统一下单开始, openid=" + openid);
//        String orderNo = WxUtil.setTradeNo();
//        log.info("【小程序支付】 统一下单开始, 订单编号=" + orderNo);
//        BigDecimal payAmount = active.getAmount();
//        log.info("【小程序支付】 统一下单开始, 金额=" + payAmount.doubleValue());
//        //生成支付金额，开发环境处理支付金额数到0.01、0.02、0.03元
//        //添加或更新支付记录(参数跟进自己业务需求添加)
//        TbActiveOrder tbActiveOrder = tbActiveOrderDao.findByOrderNo(orderNo);
//        if (tbActiveOrder == null) {
//            String reponse = wxUtil.toPayWxCxx(orderNo, openid, active.getTitle(), payAmount, jsonObject.getString("_ip"));
//            Map<String, String> resMap = MapAndXml.xml2ToMap(reponse);
//            if (WXPayConstants.SUCCESS.equals(resMap.get("return_code"))
//                    && WXPayConstants.SUCCESS.equals(resMap.get("result_code"))) {
//                backStr = wxUtil.conPayParam(resMap.get("prepay_id"));
//                log.info("【小程序支付】统一下单成功，返回参数:" + reponse);
//            } else {
//                log.info("【小程序支付】统一下单失败，失败原因:" + reponse);
//                throw new GirlException(resMap.get("return_msg"));
//            }
//            //生成数据
//            tbActiveOrder = new TbActiveOrder();
//            tbActiveOrder.setActiveId(active.getId());
//            tbActiveOrder.setCourseId(jsonObject.getInteger("courseId"));
//            tbActiveOrder.setAmount(payAmount);
//            tbActiveOrder.setEnrollName(jsonObject.getString("enrollName"));
//            tbActiveOrder.setEnrollPhone(jsonObject.getString("enrollPhone"));
//            tbActiveOrder.setUserId(jsonObject.getInteger("userId"));
//            tbActiveOrder.setBeUserId(jsonObject.getInteger("beUserId"));
//            tbActiveOrder.setOrderNo(orderNo);
//            tbActiveOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            tbActiveOrder.setStatus(0);
//            tbActiveOrder.setCouponId(active.getCouponId());
//            tbActiveOrder.setTime(jsonObject.getString("time"));
//            tbActiveOrderDao.save(tbActiveOrder);
//        } else {
//            if (tbActiveOrder.getStatus() == 0) {
//                log.info("【小程序支付】 此订单正在支付！订单编号=" + orderNo);
//                throw new GirlException("此订单正在支付");
//
//            } else if (tbActiveOrder.getStatus() == 1) {
//                log.info("【小程序支付】 此订单已支付！订单编号=" + orderNo);
//                throw new GirlException("此订单已支付");
//            } else {
//                log.info("【小程序支付】 订单不存在，请重新发起支付！订单编号=" + orderNo);
//                throw new GirlException("订单不存在，请重新发起支付！");
//
//            }
//        }
//        return backStr;
//    }
//
//    @Override
//    public String xcxNotify(String notityXml, String ip) throws Exception {
//        String resXml = "";
//        try {
//            Map<String, String> map = WXPayUtil.xmlToMap(notityXml);
//            String returnCode = map.get("return_code");
//            if ("SUCCESS".equals(returnCode)) {
//                boolean flag = wxUtil.isSignatureValid(notityXml);
//                log.info("签名验证结果：" + flag);
//                if (flag) {
//                    /**此处添加自己的业务逻辑代码start**/
//                    this.checkOrder(map.get("out_trade_no"), ip);
//                    /**此处添加自己的业务逻辑代码end**/
//                    //通知微信服务器已经支付成功
//                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//                } else {
//                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//                            + "<return_msg><![CDATA[签名验证失败]]></return_msg>" + "</xml> ";
//                }
//            } else {
//                resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//                        + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//            }
//            log.info("微信支付回调数据结束" + resXml);
//        } catch (Exception e) {
//            log.error("回调返回错误", e);
//        }
//        return resXml;
//    }
//
//    /**
//     * 订单处理
//     *
//     * @param orderNo
//     */
//    public void checkOrder(String orderNo, String ip) {
//        log.info("订单编号:" + orderNo + "============开始处理");
//        synchronized (orderNo) {
//            log.info("==========订单编号:" + orderNo + "进入锁========");
//            TbActiveOrder tbActiveOrder = tbActiveOrderDao.findByOrderNo(orderNo);
//            if (tbActiveOrder == null) {
//                log.info("订单编号：" + orderNo + " 不存在");
//                return;
//            }
//            if (tbActiveOrder.getStatus() == 1) {
//                log.info("订单编号：" + orderNo + " 已支付");
//                return;
//            }
//            Integer userId = tbActiveOrder.getUserId();
//            //新增流水
//            TbWater water = new TbWater();
//            water.setAmount(tbActiveOrder.getAmount());
//            water.setType("活动支付");
//            water.setTag(1);
//            water.setUserId(userId);
//            water.setOrderNo(orderNo);
//            water.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            tbWaterDao.save(water);
//            //新增红包
//            TbCouponUser tbCouponUser = new TbCouponUser();
//            tbCouponUser.setStatus(0);
//            tbCouponUser.setCouponId(tbActiveOrder.getCouponId());//
//            tbCouponUser.setCode(StringTools.get4Number() + "-" + StringTools.get4Number());
//            tbCouponUser.setUserId(tbActiveOrder.getUserId());
//            tbCouponUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
//            tbCouponUserDao.save(tbCouponUser);
//            //判断是否有被邀请人
//            Integer beUserId = tbActiveOrder.getBeUserId();
//            if (beUserId != null) {
//                TbUser tbUser = tbUserDao.getOne(beUserId);
//                //获取活动详情
//                TbActive tbActive = activeDao.getOne(tbActiveOrder.getActiveId());
//                BigDecimal backAmount = tbActive.getBackAmount();
//                if(backAmount != null && backAmount.doubleValue() > 0) {
//                    //统计总共返利合计
//                    Double lessMoney = tbWaterDao.sumMoneyByUserId(beUserId);
//                    if (lessMoney > 0) {
//                        String orderNoNew = orderNo + (int)(lessMoney.doubleValue()*100);
//                        //开始转账，回调接口里记录
//                        water = new TbWater();
//                        water.setAmount(backAmount);
//                        water.setType("活动返利");
//                        water.setTag(-1);
//                        water.setUserId(beUserId);
//                        water.setOrderNo(orderNoNew);
//                        water.setCreateTime(new Timestamp(System.currentTimeMillis()));
//                        tbWaterDao.save(water);
//                        String openid = tbUser.getOpenid();
//                        String desc = "邀请返现";
//                        wxUtil.payUserMoney(orderNoNew, openid, desc, backAmount, ip);
//                    }
//                }
//            }
//            tbActiveOrder.setCouponCode(tbCouponUser.getCode());
//            tbActiveOrder.setStatus(1);
//            tbActiveOrderDao.saveAndFlush(tbActiveOrder);
//            log.info("===================订单编号:" + orderNo + "结束=============");
//        }
//    }
//
//}
