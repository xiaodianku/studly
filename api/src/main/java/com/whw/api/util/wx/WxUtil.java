package com.whw.api.util.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whw.api.util.HttpUtils;
import com.whw.api.util.Qiniu.QiNiuUtil;
import com.whw.api.util.StringTools;
import com.whw.api.util.wx.wxpay.SignType;
import com.whw.api.util.wx.wxpay.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.*;

import static com.whw.api.util.wx.wxpay.WXPayConstants.USER_AGENT;

@Component
@Slf4j
public class WxUtil {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.sercetkey}")
    private String sercetkey;

    @Value("${wx.merappid}")
    private String merappid;

    @Value("${wx.merid}")
    private String merid;

    @Value("${wx.mername}")
    private String mername;

    @Value("${wx.merKey}")
    private String merKey;

    @Value("${wx.notifyurl}")
    private String notifyurl;

    public static final String WXPAYSDK_VERSION = "WXPaySDK/3.0.9";
    public static final String USER_AGENT = WXPAYSDK_VERSION +
            " (" + System.getProperty("os.arch") + " " + System.getProperty("os.name") + " " + System.getProperty("os.version") +
            ") Java/" + System.getProperty("java.version") + " HttpClient/" + HttpClient.class.getPackage().getImplementationVersion();
    private static final String PAU_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";
    private static final String TRANS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    private static final int connectTimeoutMs=6*1000;
    private static final int httpReadTimeoutMs=8*1000;

    //微信支付类型
    //NATIVE--原生支付
    //JSAPI--公众号支付-小程序支付
    //MWEB--H5支付
    //APP -- app支付
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
    public static final String TRADE_TYPE_MWEB = "MWEB";
    public static final String TRADE_TYPE_APP = "APP";

    //微信支付API
    public static final String WX_PAY_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 通过code值获取openId
     *
     * @param code
     * @return
     */
    public  String getOpenId(String code) {
        JSONObject jsonObject = code2sessionKey(code);
        return jsonObject.getString("openid");
    }

    /**
     * 发送请求用code换取sessionKey和相关信息
     *
     * @param code
     * @return
     */
    public JSONObject code2sessionKey(String code) {
        String stringToken = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appid, sercetkey, code);
        String response = HttpUtils.httpsRequestToStringWx(stringToken, "GET", null);
        System.out.println("认证结果：" + response);
        return JSON.parseObject(response);
    }

    /**
     * 获取小程序码
     * @return
     */
    public  String getQrcode(String uid) {
        String backUrl="";
        String url = String.format(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                appid, sercetkey);
        String response = HttpUtils.httpsRequestToStringWx(url, "GET", null);
        JSONObject jsonObject1 = JSONObject.parseObject(response);
        log.info("生成小程序码：" + jsonObject1.toJSONString());
        if(jsonObject1.containsKey("access_token")){
            String access_token = jsonObject1.getString("access_token");
            url = String.format(
                    "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s&scene=%s&page=pages/guodu/guodu",
                    access_token, uid);
            JSONObject requestJson = new JSONObject();
            requestJson.put("scene",uid);
            backUrl = httpsRequest(url, "POST", requestJson.toJSONString(), uid);
        }
        return backUrl;
    }



    /**
     * 上传qrcode
     * @param path
     * @param method
     * @param body
     * @param uid
     * @return
     */
    public static String httpsRequest(String path, String method, String body, String uid) {
        if (path == null || method == null) {
            return null;
        }
        InputStream inputStream = null;
        HttpsURLConnection conn = null;
        String backUrl = null;
        try {
            // 创建SSLConrext对象，并使用我们指定的信任管理器初始化
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }};
            sslContext.init(null, tm, new java.security.SecureRandom());

            // 从上面对象中得到SSLSocketFactory
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(path);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            // 设置请求方式（get|post）
            conn.setRequestMethod(method);

            // 有数据提交时
            if (null != body) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(body.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = conn.getInputStream();
            String imageName = uid+".png";
            backUrl = QiNiuUtil.upLoadFile(inputStream, imageName);
            log.info("生成小程序码路径：" + backUrl);
            return backUrl;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    //组装预下单的请求数据
    public  String getReqStr(String orderNo, String openid, String actionName, BigDecimal money, String _ip){
        Map<String,String> data = new HashMap<String,String>();
        data.put("appid", appid);
        data.put("mch_id",merid);
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        data.put("sign_type", "MD5");
        data.put("body", actionName);
        data.put("out_trade_no", orderNo);
//        data.put("device_info", "");
        data.put("fee_type", "CNY");
        data.put("total_fee", (int)(money.doubleValue()*100) + "");//1分钱
        data.put("spbill_create_ip", _ip);
        data.put("notify_url", notifyurl);
        data.put("trade_type", "JSAPI");
        data.put("product_id", "12");
        data.put("openid", openid);
        try {
            String sign = WXPayUtil.generateSignature(data, merKey, SignType.MD5);
            data.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sign error", e);
        }
        String reqBody = null;
        try {
            reqBody = WXPayUtil.mapToXml(data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("request pay error ", e);
        }
        return reqBody;
    }

    //保证唯一
    public static String setTradeNo(){
        String orderid = System.currentTimeMillis() + StringTools.get4Number() ;
        log.info("orderid = " + orderid);
        return orderid;
    }

    //组装返回客户端的请求数据
    public  Map<String,String>  conPayParam(String prepayid){
        log.info("根据当前的prepayid构造返回参数= " + prepayid);
        Map<String,String> map = new HashMap<String,String>();
        map.put("appId", appid);
        LocalDateTime time = LocalDateTime.now();
        map.put("timeStamp",  WXPayUtil.getCurrentTimestamp()+"");
        map.put("nonceStr", WXPayUtil.generateNonceStr() );
        map.put("package", "prepay_id=" + prepayid);
        map.put("signType", "MD5");
        String sign;
        try {
            sign = WXPayUtil.generateSignature(map, merKey, SignType.MD5);
            map.put("sign", sign);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(" prepayid sign error", e.getMessage());
        }
        return map;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public boolean isSignatureValid(String xmlStr) throws Exception {
        return WXPayUtil.isSignatureValid(xmlStr, merKey);
    }

    /**
     * IpUtils工具类方法
     * 获取真实的ip地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 统一下单接口
     * @param orderNo
     * @param openid
     * @param actionName
     * @param money
     * @param _ip
     * @return
     */
    public String toPayWxCxx(String orderNo, String openid, String actionName, BigDecimal money, String _ip){
        String xml = getReqStr(orderNo, openid, actionName, money, _ip);;
        log.info("请求支付的xml");
        log.info(xml);
        String response = "";
        try {
            response = requestOnce(xml, PAU_URL, false);
            log.info("支付返回凭证结果");
            log.info(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("支付异常", e);
        }
        return response;
    }

    /**
     * 企业到零钱
     * @param orderNo
     * @param openid
     * @param money
     * @param desc
     * @param _ip
     * @return
     */
    public String payUserMoney(String orderNo, String openid, String desc, BigDecimal money, String _ip){
        String response = "";
        try {
            String xml = getStrForPayToUser(orderNo, openid, money, desc, _ip);;
            log.info("企业到零钱支付的xml");
            log.info(xml);
            response = requestOnce(xml, TRANS_URL, true);
            log.info("企业到零钱支付返回凭证结果");
            log.info(response);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("企业到零钱支付异常", e);
        }
        return response;
    }


    /**
     * 企业到零钱封装
     * @param openid
     * @param amount
     * @param desc
     * @return
     */
    public String getStrForPayToUser(String orderNo, String openid, BigDecimal amount, String desc, String _ip)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mch_appid", merappid);
        map.put("mchid", merid);
//        map.put("mch_id", merid);
        map.put("nonce_str",WXPayUtil.generateNonceStr() );
        map.put("partner_trade_no", orderNo);
        map.put("openid", openid);
        map.put("check_name", "NO_CHECK");
        map.put("amount", (int)(amount.doubleValue()*100) + "");
        map.put("desc", desc);
        map.put("spbill_create_ip", _ip);
        try {
            String sign = WXPayUtil.generateSignature(map, merKey, SignType.MD5);
            map.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sign error", e);
        }
        String reqBody = null;
        try {
            reqBody = WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("request pay error ", e);
        }
        return reqBody;
    }



    /**
     * 请求，只请求一次，不做重试
     * @param data
     * @return
     * @throws Exception
     */
    private String requestOnce(String data, String url, boolean useCert) throws Exception {
        BasicHttpClientConnectionManager connManager;
        if (useCert) {
            // 证书
            char[] password = merid.toCharArray();
            // 加载本地的证书进行https加密传输
            InputStream certStream = this.getClass().getResourceAsStream("/zs/apiclient_cert.p12");
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);

            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);

            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());

            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
        }
        else {
            connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", SSLConnectionSocketFactory.getSocketFactory())
                            .build(),
                    null,
                    null,
                    null
            );
        }

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .build();

        HttpPost httpPost = new HttpPost(url);

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpReadTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
        httpPost.setConfig(requestConfig);

        StringEntity postEntity = new StringEntity(data, "UTF-8");
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", USER_AGENT + " " + merid);
        httpPost.setEntity(postEntity);

        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        return EntityUtils.toString(httpEntity, "UTF-8");

    }


}