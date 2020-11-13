package com.whw.api.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.whw.api.config.FebsConstant;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 生成验证码
 * @author jude
 *
 */
@Controller
public class DrawImageController {

    public static final int WIDTH = 120;
    public static final int HEIGHT = 90;


    /**
     * 生成图片
     * @param request
     * @param response
     */
    @RequestMapping("/drawImage")
    public void drawImage(HttpServletRequest request, HttpServletResponse response)throws Exception{
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        // 创建缓存
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB);
        // 获得画布
        Graphics g = bi.getGraphics();

        // 设置背影色
        setBackGround(g);
        // 设置边框
        setBorder(g);
        // 画干扰线
        drawRandomLine(g);
        // 画点
        drawRandomLineDian(g);
        // 写随机数
        String random = drawRandomNum((Graphics2D) g);

        // 将随机汉字存在session中
        SecurityUtils.getSubject().getSession().setAttribute(FebsConstant.IMAGE_CATCHA, random);
        // 将图形写给浏览器
        response.setContentType("image/jpeg");
        // 发头控制浏览器不要缓存
        response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        // 将图片写给浏览器

        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    /**
     * 设置背景色
     *
     * @param g
     */
    private void setBackGround(Graphics g) {
        // 设置颜色
        g.setColor(Color.WHITE);
        // 填充区域
        g.fillRect(0, 0, WIDTH, HEIGHT);

    }

    /**
     * 设置边框
     *
     * @param g
     */
    private void setBorder(Graphics g) {
        // 设置边框颜色
        g.setColor(Color.WHITE);
        // 边框区域
        g.drawRect(1, 1, WIDTH - 2, HEIGHT - 2);
    }

    /**
     * 画随机线条
     *
     * @param g
     */
    private void drawRandomLine(Graphics g) {
        // 设置颜色
        g.setColor(Color.GRAY);
        // 设置线条个数并画线
        for (int i = 0; i < 4; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            int x2 = new Random().nextInt(WIDTH);
            int y2 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

    }
    /**
     * 画随机点
     *
     * @param g
     */
    private void drawRandomLineDian(Graphics g) {
        // 设置颜色
        g.setColor(Color.lightGray);
        int number = WIDTH*HEIGHT/2;
        // 设置线条个数并画线
        for (int i = 0; i < number; i++) {
            int x1 = new Random().nextInt(WIDTH);
            int y1 = new Random().nextInt(HEIGHT);
            g.drawLine(x1, y1, x1, y1);
        }

    }

    private Color randomColor(int rate) {
        Random random = new Random();
          int red = random.nextInt(256) / rate;
            int green = random.nextInt(256) / rate;
               int blue = random.nextInt(256) / rate;
               return new Color(red, green, blue);
        }

    /**
     * 画随机汉字
     *
     * @param g
     * @return
     */
    private String drawRandomNum(Graphics2D g) {
        StringBuffer sb = new StringBuffer();
        // 设置字体
        g.setFont(new Font("宋体", Font.BOLD, 30));
        // 准备常用汉字集
        String base = "1234567890";
        int x = 5;
        // 控制字数
        for (int i = 0; i < 4; i++) {
            // 设置颜色
            g.setColor(randomColor(1));
            // 设置字体旋转角度
            int degree = new Random().nextInt() % 30;
            // 截取汉字
            String ch = base.charAt(new Random().nextInt(base.length())) + "";
            sb.append(ch);
            // 正向角度
            g.rotate(degree * Math.PI / 180, x, 50);
            g.drawString(ch, x, 50);
            // 反向角度
            g.rotate(-degree * Math.PI / 180, x, 50);
            x += 30;
        }
        return sb.toString();
    }

}
