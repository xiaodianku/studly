package com.whw.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.dao.TbUserDao;
import com.whw.api.model.TbUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController extends BaseController {


    @Autowired
    private TbUserDao tbUserDao;

    /**
     * 会员列表
     *
     * @param pageNum
     * @param pageRow
     * @return
     * @throws IOException
     */
    @RequestMapping("/userList")
    public Object userList(Integer pageNum, Integer pageRow, String nickname) throws IOException {
        JSONObject jsonObject = new JSONObject();
        int offset = (pageNum - 1) * pageRow;
        int rows = pageRow;
        List<TbUser> systemUsers = tbUserDao.getList(offset, rows);
        long totalCount = tbUserDao.count();
        jsonObject.put("list", systemUsers);
        jsonObject.put("totalCount", totalCount);
        return ResultUtil.success(jsonObject);
    }


}