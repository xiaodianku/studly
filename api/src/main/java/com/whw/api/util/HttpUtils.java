package com.whw.api.util;

/**
 * 短信平台
 * open.niubaifa.com
 * 帐号：nbqsmm
 * 帐号：nbqsmm
 */

import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HttpUtils  工具包
 *
 * @author kun.zhang@downjoy.com
 */
@Slf4j
public class HttpUtils {

    private static final int TIMEOUT_IN_MILLIONS = 5000;

    public interface CallBack {
        void onRequestComplete(String result);
    }

    /**
     * 异步的Get请求
     *
     * @param url
     * @param callBack
     */
    public static void HttpGetAsyn(final String url, final CallBack callBack) {

        new Thread() {//新建一个线程开始
            public void run() {
                try {
                    String result = HttpGet(url);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    log.error("{}-{}", e, e.getMessage());
                }
            }
        }.start();
    }


    /**
     * 异步的Post请求
     *
     * @param url
     * @param params
     * @param callBack
     */
    public static void HttpPostAsny(final String url, final String params, final CallBack callBack) {
        new Thread() {
            public void run() {
                try {
                    String result = HttpPost(url, params);
                    if (callBack != null) {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            ;
        }.start();
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url    发送请求的 URL
     * @param params 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String HttpPost(String url, String params) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            if (params != null && !params.trim().equals("")) {
                // 获取URLConnection对象对应的输出流
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(params);
                // flush输出流的缓冲
                out.flush();
            }
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Get请求，获得返回数据
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String HttpGet(String url) {
        URL surl = null;
        HttpsURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            surl = new URL(url);
            conn = (HttpsURLConnection) surl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");//保持连接
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);//设置从主机读取数据超时（单位：毫秒）
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);//设置连接主机超时（单位：毫秒）
            //
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                byteArrayOutputStream = new ByteArrayOutputStream();
                int len = -1;
                byte[] buf = new byte[128];
                while ((len = is.read(buf)) != -1) {
                    byteArrayOutputStream.write(buf, 0, len);
                }
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toString();
            } else {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (byteArrayOutputStream != null)
                    byteArrayOutputStream.close();
            } catch (IOException e) {
            }
            conn.disconnect();
        }
        return null;
    }

    /**
     * httpPost maps 请求
     * param url 地址
     * param params 参数
     * param encode 编码
     */
    public static String HttpPost(String url, Map<String, Object> params, String encode) {
        OutputStream out = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if(encode == null) {
                encode = "UTF-8";
            }
            conn.setRequestProperty("charset", encode);
            conn.setUseCaches(false);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            StringBuffer stringBuffer = new StringBuffer();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    try {
                        stringBuffer
                                .append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(String.valueOf(entry.getValue()), encode))
                                .append("&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);

                byte[] mydata = stringBuffer.toString().getBytes();
                // 获取URLConnection对象对应的输出流
                out = conn.getOutputStream();
                // 发送请求参数
                out.write(mydata);
                // flush输出流的缓冲
                out.flush();
                int responseCode = conn.getResponseCode();//获取服务器响应状态码
                if (responseCode == 200) {
                    // 获得输入流，从服务器端获得数据
                    InputStream inputStream = conn.getInputStream();
                    return (changeInputStream(inputStream, encode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static String changeInputStream(InputStream inputStream,
                                            String encode) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null) {
            try {
                while ((len = inputStream.read(data)) != -1) {
                    arrayOutputStream.write(data, 0, len);
                }
                result = new String(arrayOutputStream.toByteArray(), encode);//按照编码读取服务器数据
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 发送https请求
     *
     * @param path
     * @param method
     * @param body
     * @return
     */
    public static String httpsRequestToStringWx(String path, String method, String body) {
        if (path == null || method == null) {
            return null;
        }
        String response = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        HttpsURLConnection conn = null;
        try {
            // 创建SSLConrext对象，并使用我们指定的信任管理器初始化
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            TrustManager[] tm = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { }
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
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            response = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (IOException execption) {

            }
        }
        return response;
    }


    /**
     * 获取网络图片流
     *
     * @param url
     * @return
     */
    public InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }


}
