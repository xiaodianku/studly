package com.whw.api.controller;

import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.dao.TbCategoryDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController extends BaseController {


    @Autowired
    private TbCategoryDao tbCategoryDao;

    /**
     * 分类列表
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/getList")
    public Object getList() throws IOException {
        return ResultUtil.success(tbCategoryDao.findByOrderBySortDesc());
    }

}