//package com.whw.api.util.wx.wxpay;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//
//import javax.xml.XMLConstants;
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.*;
//
//public class PayUtil {
//
//    /**
//     * 签名字符串
//     * @param text 需要签名的字符串
//     * @param key 密钥
//     * @param input_charset 编码格式
//     * @return 签名结果
//     */
//    public static String sign(String text, String key, String input_charset) {
//        text = text + "&key=" + key;
//        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
//    }
//    /**
//     * 签名字符串
//     *  @param text 需要签名的字符串
//     * @param sign 签名结果
//     * @param key 密钥
//     * @param input_charset 编码格式
//     * @return 签名结果
//     */
//    public static boolean verify(String text, String sign, String key, String input_charset) {
//        text = text + key;
//        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
//        if (mysign.equals(sign)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//    /**
//     * @param content
//     * @param charset
//     * @return
//     * @throws UnsupportedEncodingException
//     */
//    public static byte[] getContentBytes(String content, String charset) {
//        if (charset == null || "".equals(charset)) {
//            return content.getBytes();
//        }
//        try {
//            return content.getBytes(charset);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
//        }
//    }
//
//    private static boolean isValidChar(char ch) {
//        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
//            return true;
//        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
//            return true;// 简体中文汉字编码
//        return false;
//    }
//    /**
//     * 除去数组中的空值和签名参数
//     * @param sArray 签名参数组
//     * @return 去掉空值与签名参数后的新签名参数组
//     */
//    public static Map<String, String> paraFilter(Map<String, String> sArray) {
//        Map<String, String> result = new HashMap<String, String>();
//        if (sArray == null || sArray.size() <= 0) {
//            return result;
//        }
//        for (String key : sArray.keySet()) {
//            String value = sArray.get(key);
//            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
//                    || key.equalsIgnoreCase("sign_type")) {
//                continue;
//            }
//            result.put(key, value);
//        }
//        return result;
//    }
//
//    /**
//     *
//     * @param requestUrl 请求地址
//     * @param requestMethod 请求方法
//     * @param outputStr 参数
//     */
//    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
//        // 创建SSLContext
//        StringBuffer buffer = null;
//        try{
//            URL url = new URL(requestUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod(requestMethod);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.connect();
//            //往服务器端写内容
//            if(null !=outputStr){
//                OutputStream os=conn.getOutputStream();
//                os.write(outputStr.getBytes("utf-8"));
//                os.close();
//            }
//            // 读取服务器端返回的内容
//            InputStream is = conn.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            buffer = new StringBuffer();
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                buffer.append(line);
//            }
//            br.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return buffer.toString();
//    }
//
//
//    public static String urlEncodeUTF8(String source){
//        String result=source;
//        try {
//            result=java.net.URLEncoder.encode(source, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    public static InputStream String2Inputstream(String str) {
//        return new ByteArrayInputStream(str.getBytes());
//    }
//
//
//
//}
