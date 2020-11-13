package com.whw.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.whw.api.config.FebsConstant;
import com.whw.api.config.ResultJson.GirlException;
import com.whw.api.config.ResultJson.ResultEnum;
import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.dao.*;
import com.whw.api.model.*;
import com.whw.api.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/system")
public class SystemController extends BaseController {

    @Autowired
    private SystemUserDao systemUserDao;

    @RequestMapping("/userinfo")
    public Object login() throws IOException {
        SystemUser systemUser = getUser();
        JSONObject jsonObjectBack = new JSONObject();
        jsonObjectBack.put("user", systemUser);
        return ResultUtil.success(jsonObjectBack);
    }


    /**
     * 系统用户列表
     * @param pageNum
     * @param pageRow
     * @return
     * @throws IOException
     */
    @RequestMapping("/userList")
    public Object userList(Integer pageNum, Integer pageRow) throws IOException {
        SystemUser systemUser = getUser();
        JSONObject jsonObject = new JSONObject();
        int offset = (pageNum-1)*pageRow;
        int rows = pageRow;
        List<SystemUser> systemUsers= systemUserDao.getList(offset, rows);
        long totalCount = systemUserDao.count();
        jsonObject.put("list",systemUsers);
        jsonObject.put("totalCount",totalCount);
        return ResultUtil.success(jsonObject);
    }



    /**
     * 修改删除用户
     * @param jsonObject
     * @return
     */
    @RequestMapping("/updateUser")
    public Object updateUser(@RequestBody JSONObject jsonObject) {
        if (StringTools.isNullOrEmpty(jsonObject.get("userId"))) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        SystemUser systemUser = systemUserDao.getOne(jsonObject.getIntValue("userId"));
        if(StringTools.isNotNullOrEmpty(jsonObject.get("mobile"))){
            SystemUser systemUserCheck = systemUserDao.findByMobileAndIdNot(jsonObject.getString("mobile"), jsonObject.getIntValue("userId"));
            if(systemUserCheck != null){
                throw new GirlException(ResultEnum.HAVING_MOBILE);
            }
            systemUser.setMobile(jsonObject.getString("mobile"));
        }
        if(StringTools.isNotNullOrEmpty(jsonObject.get("nickname")) ){
            systemUser.setNickname(jsonObject.getString("nickname"));
        }
        if(StringTools.isNotNullOrEmpty(jsonObject.get("roleId")) ){
            systemUser.setRoleId(jsonObject.getIntValue("roleId"));
        }
        if(StringTools.isNotNullOrEmpty(jsonObject.get("password")) ){
            systemUser.setPassword(jsonObject.getString("password"));
        }
        if(StringTools.isNotNullOrEmpty(jsonObject.get("deleteStatus")) ){
            systemUser.setDeleteStatus(jsonObject.getIntValue("deleteStatus"));
        }
        systemUserDao.saveAndFlush(systemUser);
        return ResultUtil.success(true);
    }

   /**
     * 删除用户
     * @param jsonObject
     * @return
     */
    @RequestMapping("/deleteUser")
    public Object deleteUser(@RequestBody JSONObject jsonObject) {
        if (StringTools.isNullOrEmpty(jsonObject.get("userId"))) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        systemUserDao.deleteById(jsonObject.getIntValue("userId"));
        return ResultUtil.success(true);
    }

    /**
     * 新增用户
     * @param jsonObject
     * @return
     */
    @RequestMapping("/addUser")
    public Object addUser(@RequestBody JSONObject jsonObject) {
        if (StringTools.isNullOrEmpty(jsonObject.get("roleId"))
                ||  StringTools.isNullOrEmpty(jsonObject.get("nickname"))
                ||  StringTools.isNullOrEmpty(jsonObject.get("mobile"))
                ||  StringTools.isNullOrEmpty(jsonObject.get("password"))   ) {
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        SystemUser systemUser = systemUserDao.findByMobile(jsonObject.getString("mobile"));
        if(systemUser != null){
            throw new GirlException(ResultEnum.HAVING_MOBILE);
        }
        systemUser = new SystemUser();
        systemUser.setNickname(jsonObject.getString("nickname"));
        systemUser.setPassword(jsonObject.getString("password"));
        systemUser.setRoleId(jsonObject.getIntValue("roleId"));
        systemUser.setMobile(jsonObject.getString("mobile"));
        systemUser.setDeleteStatus(0);
        systemUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        systemUser.setCreateId(Integer.valueOf(String.valueOf(SecurityUtils.getSubject().getSession().getAttribute(FebsConstant.SESSION_USER_ID))));
        systemUserDao.save(systemUser);
        return ResultUtil.success(true);
    }

    /**
     * 检查当前电话是否被使用
     * @param jsonObject
     * @return
     */
    @RequestMapping("/checkedUserMobile")
    public Object checkedUserMobile(@RequestBody JSONObject jsonObject) {
        if (StringTools.isNullOrEmpty(jsonObject.get("mobile")) || StringTools.isNullOrEmpty(jsonObject.get("type"))) { //type: 1-新增电话，2-修改电话，3-修改密码
            throw new GirlException(ResultEnum.MUST_DATA);
        }
        if(jsonObject.getInteger("type") == 1){
            SystemUser systemUser = systemUserDao.findByMobile(jsonObject.getString("mobile"));
            if(systemUser != null){
                return ResultUtil.success(false);
            }
        }else if(jsonObject.getInteger("type") == 2){
            SystemUser systemUser = systemUserDao.findByMobileAndIdNot(jsonObject.getString("mobile"), jsonObject.getIntValue("userId"));
            if(systemUser != null){
                return ResultUtil.success(false);
            }
        }
        return ResultUtil.success(true);
    }

    /**
     * 获取登录人详情
     * @return
     */
    @RequestMapping("/getLoginInfo")
    public Object getLoginInfo() {
        SystemUser systemUser = getUser();
        return ResultUtil.success(systemUser);
    }

}