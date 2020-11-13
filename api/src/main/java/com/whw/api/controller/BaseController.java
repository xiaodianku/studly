package com.whw.api.controller;

import com.whw.api.config.FebsConstant;
import com.whw.api.config.ResultJson.GirlException;
import com.whw.api.config.ResultJson.ResultEnum;
import com.whw.api.model.SystemUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;

@RestController
@Slf4j
public class BaseController {

    public static SystemUser getUser() {
        SystemUser systemUser = (SystemUser) SecurityUtils.getSubject().getSession().getAttribute(FebsConstant.SESSION_USER_INFO);
        if (systemUser == null) {
            throw new GirlException(ResultEnum.NOT_USER);
        }
        return systemUser;
    }

}