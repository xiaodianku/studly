package com.whw.api.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Author: Manitozhang
 * @Data: 2019/1/9 16:51
 * @Email: manitozhang@foxmail.com
 * <p>
 * 文件工具类
 */
public class FileUtil {

    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }


    public static void uploadFileInputStream(InputStream inStream, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        byte[] file = input2byte(inStream);
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    /**
     * 上传文件
     *
     * @param filecontent
     * @param path
     * @param fileName
     * @return
     */
    public static boolean uploadFile(MultipartFile filecontent, String path, String fileName) {
        OutputStream os = null;
        InputStream inputStream = null;
        try {
            inputStream = filecontent.getInputStream();

            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static double backFileSize(Long len, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        return fileSize;
    }

    /**
     * 返回文件的文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        try {
            return fileName.split("\\.")[fileName.split("\\.").length - 1];
        } catch (Exception e) {
            return null;
        }
    }

    /**文件转换为字节数组
     * @param file
     * @return
     */
    public static byte[] fileToByteArray(File file) {
        byte[] imagebs = null;
        try {
            //读取输入的文件====文件输入流
            FileInputStream fis = new FileInputStream(file);
            //字节数组输出流  在内存中创建一个字节数组缓冲区，所有输出流的数据都会保存在字符数组缓冲区中
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            //将文件读入到字节数组中
            while ((len = fis.read(buffer)) != -1) {
                // 将指定字节数组中从偏移量 off 开始的 len 个字节写入此字节数组输出流。
                baos.write(buffer, 0, len);
            }
            imagebs = baos.toByteArray();//当前输出流的拷贝  拷贝到指定的字节数组中

            fis.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagebs;

    }


}