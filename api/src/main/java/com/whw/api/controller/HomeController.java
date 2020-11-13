package com.whw.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.whw.api.config.ResultJson.GirlException;
import com.whw.api.config.ResultJson.ResultEnum;
import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.dao.SystemUserDao;
import com.whw.api.model.SystemUser;
import com.whw.api.util.Qiniu.QiNiuUtil;
import com.whw.api.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    private SystemUserDao systemUserDao;


    @RequestMapping("/login")//,
    public Object login(@RequestBody JSONObject jsonObject) throws IOException {
        //实际图形验证码
        if (StringUtils.isEmpty(jsonObject.getString("mobile"))
                || StringUtils.isEmpty(jsonObject.getString("password"))) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(jsonObject.getString("mobile"), jsonObject.getString("password"));
        try {
            currentUser.login(usernamePasswordToken);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            log.error("手机号码/密码错误", e);
            throw new GirlException(e.getMessage());
        }
        String token = StringTools.getUUID();
        return ResultUtil.success(token);
    }


    @RequestMapping("/logout")
    public Object logout() {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
        } catch (Exception e) {
        }
        return ResultUtil.success("");
    }

    @RequestMapping("/isUser")
    public Object isUser(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        SystemUser systemUser = systemUserDao.findByMobile(mobile);
        if (systemUser == null) {
            throw new GirlException(ResultEnum.NOT_USER);
        }
        return ResultUtil.success(null);
    }


    @RequestMapping("/updateUserPassword")
    public Object updateUserPassword(@RequestBody JSONObject jsonObject) {
        if (StringUtils.isEmpty(jsonObject.getString("mobile"))
                || StringUtils.isEmpty(jsonObject.getString("password"))
                || StringUtils.isEmpty(jsonObject.getString("smsCode"))) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        SystemUser systemUser = systemUserDao.findByMobile(jsonObject.getString("mobile"));
        if (systemUser == null) {
            throw new GirlException(ResultEnum.NOT_USER);
        }
        systemUser.setPassword(jsonObject.getString("password"));
        systemUserDao.saveAndFlush(systemUser);
        return ResultUtil.success(true);
    }

    /**
     * 上传图片和照片
     *
     * @return
     */
    @RequestMapping("/uploadImage")
    public Object uploadPicture(HttpServletRequest request, HttpServletResponse response) {
        String backUrl = "";
        try {
            //获取从前台传过来得图片
            //判断 request 是否有文件上传,即多部分请求
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multiFileMap = multiRequest.getMultiFileMap();
            List<MultipartFile> fileSet = new LinkedList<>();
            for (Map.Entry<String, List<MultipartFile>> temp : multiFileMap.entrySet()) {
                fileSet = temp.getValue();
            }
            if (fileSet.size() > 0) {
                for (int i = 0; i < fileSet.size(); i++) {
                    MultipartFile temp = fileSet.get(i);
                    String oldFileName = temp.getOriginalFilename();
                    //换成 七牛云，返回路径
                    backUrl = QiNiuUtil.upLoadFile(temp, oldFileName);
                    if (null == backUrl) {
                        throw new GirlException("文件上传失败");
                    }
                }
            }
            return ResultUtil.success(backUrl);
        } catch (Exception e) {
            log.error("上传图片失败：", e);
            throw new GirlException(e.getMessage());
        }
    }


}