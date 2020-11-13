package com.whw.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.dao.TbArticleDao;
import com.whw.api.dao.TbCategoryDao;
import com.whw.api.dao.TbUserDao;
import com.whw.api.model.TbArticle;
import com.whw.api.model.TbCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Autowired
    private TbArticleDao tbArticleDao;

    @Autowired
    private TbCategoryDao tbCategoryDao;


    /**
     * 文章列表
     */
    @RequestMapping("/getList")
    public Object getList(Integer pageNum, Integer pageRow) throws IOException {
        JSONObject jsonObject = new JSONObject();
        int offset = (pageNum - 1) * pageRow;
        int rows = pageRow;
        List<TbArticle> list = tbArticleDao.getList(offset, rows);
        for (TbArticle article : list) {
            TbCategory category = tbCategoryDao.getOne(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        long totalCount = tbArticleDao.count();
        jsonObject.put("list", list);
        jsonObject.put("totalCount", totalCount);
        return ResultUtil.success(jsonObject);
    }

    /**
     * 修改/新增文章
     */
    @RequestMapping("/saveOrUpdateArticle")
    public Object saveOrUpdateArticle(@RequestBody TbArticle tbArticle) throws IOException {
        if(tbArticle.getId() == 0) {
            tbArticle.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            tbArticle.setGmtModify(new Timestamp(System.currentTimeMillis()));
        }
        tbArticleDao.saveAndFlush(tbArticle);
        return ResultUtil.success(true);
    }


}