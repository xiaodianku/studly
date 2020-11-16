package com.whw.api.controller;

import com.whw.api.config.ResultJson.GirlException;
import com.whw.api.config.ResultJson.ResultEnum;
import com.whw.api.config.ResultJson.ResultUtil;
import com.whw.api.config.emnu.ArticleStatus;
import com.whw.api.config.emnu.MapStaticEnum;
import com.whw.api.dao.*;
import com.whw.api.model.*;
import com.whw.api.util.DateUtil;
import com.whw.api.util.StringTools;
import com.whw.api.util.wx.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/wx")
public class WxController extends BaseController {


    @Autowired
    private WxUtil wxUtil;
    @Autowired
    private TbUserDao tbUserDao;

    @Autowired
    private TbMapDao tbMapDao;

    /**
     * 获取用户信息
     * @param code
     * @return
     */
    @RequestMapping("/getUser")
    public Object getUserForCode(String code) {
        String openId = wxUtil.getOpenId(code);
        if(StringTools.isNullOrEmpty(openId)) throw new GirlException(ResultEnum.UNKONW_ERROR);
        TbUser user = tbUserDao.findByOpenid(openId);
        return ResultUtil.success(user);
    }

    /**
     * 新增用户
     * @param code
     * @param nickName
     * @param avatarUrl
     * @param beUserId
     * @return
     */
    @RequestMapping("/addUser")
    public Object addUser(String code, String nickName, String avatarUrl, Integer beUserId, String mobile) {
        String openId = wxUtil.getOpenId(code);
        if(StringTools.isNullOrEmpty(openId)) throw new GirlException("openid is null");
        System.out.println("*" + MapStaticEnum.LOGIN_PHONE.getName());
        List<TbMap> byKNameAndVName = tbMapDao.findByKNameAndVName(MapStaticEnum.LOGIN_PHONE.getName(), mobile);
        if(byKNameAndVName.size() == 0) {
            throw new GirlException(ResultEnum.NOT_LOGIN_AUTH);
        }
        //是否在登录的范畴内
        TbUser mobileUser = tbUserDao.findByMobile(mobile);
        if(mobileUser != null) {
            throw new GirlException(ResultEnum.HAVING_MOBILE);
        }
        TbUser user = tbUserDao.findByOpenid(openId);
        if (user == null) {
            user = new TbUser();
            user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            user.setGmtCreate(new Timestamp(System.currentTimeMillis()));
            user.setGmtModify(new Timestamp(System.currentTimeMillis()));
            user.setImg(avatarUrl);
            user.setOpenid(openId);
            user.setMobile(mobile);
            user.setNickname(nickName);
            user.setBeUser(beUserId);
            user = tbUserDao.save(user);
        }else{
            user.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
            user = tbUserDao.saveAndFlush(user);
        }
        return ResultUtil.success(user);
    }


    @Autowired
    private TbArticleDao tbArticleDao;

    /**
     * 获取文章列表
     * @return
     */
    @RequestMapping("/getArticleList")
    public Object getArticleList(Integer categoryId) {
        List<TbArticle> list = null;
        if(categoryId == null){
            list = tbArticleDao.findByStatusOrderByGmtCreateDesc(ArticleStatus.OPEN.getValue());
        }else{
            list = tbArticleDao.findByCategoryIdAndStatusOrderByGmtCreateDesc(categoryId, ArticleStatus.OPEN.getValue());
        }
        for (TbArticle article : list) {
            TbCategory category = tbCategoryDao.getOne(article.getCategoryId());
            article.setCategoryName(category.getName());
        }
        return ResultUtil.success(list);
    }

    /**
     * 获取文章详情
     * @return
     */
    @RequestMapping("/getArticleDetail")
    public Object getArticleDetail(Integer articleId) {
        TbArticle article = tbArticleDao.getOne(articleId);
        TbCategory category = tbCategoryDao.getOne(article.getCategoryId());
        article.setCategoryName(category.getName());

        List<String> imageList = new ArrayList<>();
        String[] images = article.getImages().split(",");
        for (int i = 0; i < images.length; i++) {
            if("".equals(images[i])){
                continue;
            }
            imageList.add(images[i]);
        }
        article.setImageList(imageList);

        List<String> videoList = new ArrayList<>();
        String[] videos = article.getVideos().split(",");
        for (int i = 0; i < videos.length; i++) {
            if("".equals(videos[i])){
                continue;
            }
            videoList.add(videos[i]);
        }
        article.setVideoList(videoList);

        return ResultUtil.success(article);
    }


    @Autowired
    private TbLeaveMessageDao tbLeaveMessageDao;

    /**
     * 获取留言列表
     * @return
     */
    @RequestMapping("/getLeaveMessageList")
    public Object getLeaveMessageList(Integer articleId) {
        List<TbLeaveMessage> list = tbLeaveMessageDao.findByArticleIdOrderByGmtCreateDesc(articleId);
        for (TbLeaveMessage tbLeaveMessage : list) {
            TbUser tbUser = tbUserDao.getOne(tbLeaveMessage.getUserId());
            tbLeaveMessage.setNickname(tbUser.getNickname());
            tbLeaveMessage.setImg(tbUser.getImg());
            tbLeaveMessage.setLeaveMessageTime(DateUtil.dateToStrYYYYMMDDHHMMSS(tbLeaveMessage.getGmtCreate()));
        }
        return ResultUtil.success(list);
    }

    /**
     * 新增留言
     * @return
     */
    @RequestMapping("/saveLeaveMessage")
    public Object saveLeaveMessage(Integer articleId, Integer userId, String content) {
        TbLeaveMessage tbLeaveMessage = new TbLeaveMessage();
        tbLeaveMessage.setArticleId(articleId);
        tbLeaveMessage.setContent(content);
        tbLeaveMessage.setUserId(userId);
        tbLeaveMessage.setGmtCreate(new Timestamp(System.currentTimeMillis()));
        tbLeaveMessage.setGmtModify(new Timestamp(System.currentTimeMillis()));
        tbLeaveMessageDao.save(tbLeaveMessage);
        return ResultUtil.success(true);
    }



    @Autowired
    private TbBrowsingHistoryDao tbBrowsingHistoryDao;

    /**
     * 获取浏览记录列表
     * @return
     */
    @RequestMapping("/getBrowsingHistory")
    public Object getBrowsingHistory(Integer userId) {
        List<TbBrowsingHistory>  list =tbBrowsingHistoryDao.findByUserIdOrderByBrowsingTimeDesc(userId);
        for (TbBrowsingHistory tbBrowsingHistory : list) {
            TbArticle article = tbArticleDao.getOne(tbBrowsingHistory.getArticleId());
            tbBrowsingHistory.setArticleTitle(article.getTitle());
            TbCategory category = tbCategoryDao.getOne(article.getCategoryId());
            tbBrowsingHistory.setCategoryName(category.getName());
            tbBrowsingHistory.setBrowsingTimeStr(DateUtil.dateToStrYYYYMMDDHHMMSS(tbBrowsingHistory.getBrowsingTime()));
        }
        return ResultUtil.success(list);
    }

    /**
     * 新增浏览记录
     * @return
     */
    @RequestMapping("/saveBrowsingHistory")
    public Object saveBrowsingHistory(Integer articleId, Integer userId) {
        TbBrowsingHistory tbBrowsingHistory = new TbBrowsingHistory();
        tbBrowsingHistory.setArticleId(articleId);
        tbBrowsingHistory.setUserId(userId);
        tbBrowsingHistory.setBrowsingTime(new Timestamp(System.currentTimeMillis()));
        tbBrowsingHistoryDao.save(tbBrowsingHistory);
        return ResultUtil.success(true);
    }


    @Autowired
    private TbCategoryDao tbCategoryDao;

    /**
     * 分类列表
     *
     */
    @RequestMapping("/getCategoryList")
    public Object getCategoryList() {
        return ResultUtil.success(tbCategoryDao.findByOrderBySortDesc());
    }

}