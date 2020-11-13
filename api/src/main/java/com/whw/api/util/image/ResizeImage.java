package com.whw.api.util.image;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Map;

public class ResizeImage {

    /**
     * @param im            原始图像
     * @param resizeTimes   需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小
     * @return              返回处理后的图像
     */
    public static BufferedImage resizeImage(BufferedImage im, float resizeTimes) {
        /*原始图像的宽度和高度*/
        int width = im.getWidth();
        int height = im.getHeight();

        /*调整后的图片的宽度和高度*/
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);

        /*新生成结果图片*/
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }

    /**
     * @param im            原始图像
     * @param resizeTimes   倍数,比如0.5就是缩小一半,0.98等等double类型
     * @return              返回处理后的图像
     */
    public static BufferedImage zoomImage(BufferedImage im, Float resizeTimes) {
        /*原始图像的宽度和高度*/
        int width = im.getWidth();
        int height = im.getHeight();
        if(resizeTimes == null) resizeTimes = 0.1f;
        /*调整后的图片的宽度和高度*/
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);

        /*新生成结果图片*/
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }

    /**
     * @param path  要转化的图像的文件夹,就是存放图像的文件夹路径
     * @param type  图片的后缀名组成的数组
     * @return
     */
    public static List<BufferedImage> getImageList(String path, String[] type) throws IOException{
        Map<String,Boolean> map = new HashMap<String, Boolean>();
        for(String s : type) {
            map.put(s,true);
        }
        List<BufferedImage> result = new ArrayList<BufferedImage>();
        File[] fileList = new File(path).listFiles();
        for (File f : fileList) {
            if(f.length() == 0)
                continue;
            if(map.get(getExtension(f.getName())) == null)
                continue;
            result.add(ImageIO.read(f));
        }
        return result;
    }

    /**
     * 把图片写到磁盘上
     * @param im
     * @param path     eg: C://home// 图片写入的文件夹地址
     * @param fileName DCM1987.jpg  写入图片的名字
     * @return
     */
    public static boolean writeToDisk(BufferedImage im, String path, String fileName) {
        File f = new File(path + fileName);
        String fileType = getExtension(fileName);
        if (fileType == null)
            return false;
        try {
            ImageIO.write(im, fileType, f);
            im.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    public static boolean writeHighQuality(BufferedImage im, String fileFullPath, Float quality) {
        try {
            /*输出到文件流*/
            FileOutputStream newimage = new FileOutputStream(fileFullPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
            /* 压缩质量 */
            if(quality == null) quality = 1f;
            jep.setQuality(0.5f, true);
            encoder.encode(im, jep);
            /*近JPEG编码*/
            newimage.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回文件的文件后缀名
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

    /**
     * 生成缩略图
     * @param file
     * @param realPath
     * @param fileName
     */
    public static void upLoadSLImage(MultipartFile file,String realPath, String fileName, Float resizeTimes, Float quality) {
        File targetFile = new File(realPath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        BufferedImage bufferedImage = InputImage(file);
        writeHighQuality(zoomImage(bufferedImage, resizeTimes), realPath+fileName, quality);
    }


    public static BufferedImage InputImage(MultipartFile file) {
        BufferedImage srcImage = null;
        try {
            FileInputStream in = (FileInputStream) file.getInputStream();
            srcImage = ImageIO.read(in);
        } catch (IOException e) {
            System.out.println("读取图片文件出错！" + e.getMessage());
        }
        return srcImage;
    }

    public static void listImageTOBufferImage(Float resizeTimes, Float quality) throws IOException {
        String inputFoler = "d:\\wx" ;
        /*这儿填写你存放要缩小图片的文件夹全地址*/
        String outputFolder = "d:\\wx\\";
        /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/
        List<BufferedImage> imageList = getImageList(inputFoler,new String[] {"jpg","png"});
        for (int i = 0; i < imageList.size(); i++) {
            BufferedImage image = imageList.get(i);
            writeHighQuality(zoomImage(image, resizeTimes), outputFolder+ i+".png", quality);
        }
    }

    /**
     * 网络图片地址
     * @param urlStr
     * @param filePath
     */
    public static void urlTOBufferImage(String urlStr, String filePath, String fileName, Float resizeTimes, Float quality) {
        URL url = null;
        try {
            File targetFile = new File(filePath);
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            url = new URL(urlStr);
            BufferedImage image = ImageIO.read(url);
            writeHighQuality(zoomImage(image, resizeTimes),  filePath + fileName, quality);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        listImageTOBufferImage( 0.8f, 1f);
    }

}
