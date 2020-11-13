package com.whw.api.util.Qiniu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.StringUtils;
import com.whw.api.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
public class QiNiuUtil {

    //密钥配置
    static Auth auth = null;

    //构造一个带指定Region对象的配置类
    static  Configuration cfg = new Configuration(Region.region2());
    //空間名
    static String bucketName = "oumeisi";

    static String url = "http://qjosdczrw.hn-bkt.clouddn.com/";

    static String accessKey = "Z5pK8Dxl9UYZzYSD5K0FuWt1RbBZW5o6G2tyLnfj";

    static String secretKey = "292afuyvIgQRm02vQNx-s0xDE-XpbkUMVbc9-fGY";
    //创建上传对象

    static {
//		初始化
        auth = Auth.create(accessKey, secretKey);
    }

    public static String getUpToken() {
        return auth.uploadToken(bucketName);
    }

    /**
     * 上传文件
     *
     * @param filecontent
     * @param oldFileName
     * @return
     * @throws IOException
     */
    public static String upLoadFile(MultipartFile filecontent, String oldFileName) throws IOException {
        String fileName = setImgName(oldFileName);
        byte[] bytes = filecontent.getBytes();
        boolean upload = upload(bytes, fileName);
        return upload ? url + fileName : null;
    }

    /**
     * 上传文件
     *
     * @param inStream
     * @param oldFileName
     * @return
     * @throws IOException
     */
    public static String upLoadFile(InputStream inStream, String oldFileName) throws IOException {
        String fileName = setImgName(oldFileName);
        byte[] bytes = FileUtil.input2byte(inStream);
        boolean upload = upload(bytes, fileName);
        return upload ? url + fileName : null;
    }


    /**
     * 上传
     *
     * @param bytes
     * @param imgName 图片命名
     * @throws IOException
     */
    public static boolean upload(byte[] bytes, String imgName) throws IOException {
        log.info("上传图片：" + imgName);
        try {
             UploadManager uploadManager = new UploadManager(cfg);
            // 调用put方法上传
            Response res = uploadManager.put(bytes, imgName, getUpToken());
            // 打印返回的信息
            if (null == res || !res.isOK()) {
                log.error("上传失败" + res);
                return false;
            }
        } catch (QiniuException e) {
            Response r = e.response;
            try {
                log.error("上传文件到七牛失败,reponse={}:", r.bodyString(), e);
            } catch (QiniuException e1) {
                log.error("上传文件到七牛失败:", e);
            }
            return false;
        }
        return true;
    }


    /**
     * desc: 处理图片名字.
     *
     * @param:
     * @author qinlihan 2017年5月16日
     */
    public static String setImgName(String name) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        String time = sdf.format(new Date());
        int random = (int) (Math.random() * 900) + 100;
        return time + "-" + random + "." + suffix;
    }

    /**
     * 删除图片
     *
     * @param picUrlStr
     * @return
     */
    public static boolean deleteImg(String picUrlStr) {
        log.info("删除图片：" + picUrlStr);
        if (StringUtils.isNullOrEmpty(picUrlStr)) {
            return true;
        }
        String picKey = picUrlStr.substring(picUrlStr.lastIndexOf("/") + 1);
        //实例化一个BucketManager对象


        BucketManager bucketManager = new BucketManager(auth, cfg);
        //要测试的空间和key，并且这个key在你空间中存在
        try {
            //调用delete方法移动文件
            bucketManager.delete(bucketName, picKey);
            return true;
        } catch (QiniuException e) {
            //捕获异常信息
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\1.jpg");
        String fileName = setImgName("D:\\1.jpg");
        byte[] bytes = FileUtil.fileToByteArray(file);
        boolean upload = upload(bytes, fileName);
        System.out.println(upload);
        System.out.println(url + fileName);
    }

}
