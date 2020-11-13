// pages/courseDetail/courseDetail.js
import {request} from '../../utils/request.js'
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        isPlayVideo: false,
        articleId: "",
        articleDetail:{},
        leaveMessageTotal:0,
        leaveMessageList:[]
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      console.log("============详情" + options.articleId)
      this.setData({
        articleId: options.articleId
      });
      if(app.globalData.userId != "") {
        this.saveBrowsingHistory();
      }
      this.getArticleDetail();
      this.getLeaveMessageList();
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },
    videoPlay: function(){
        if(this.data.isPlayVideo){
          this.setData({
            isPlayVideo: false
          })
          this.playMusic();
        }else{
          this.setData({
            isPlayVideo: true
          })
          back.stop();
        }
      },
      //图片点击事件
  previewImg: function (event) {
    var src = event.currentTarget.dataset.image;//获取data-src
    var imgList = [src];//获取data-list
    //图片预览
    wx.previewImage({
      current: src, // 当前显示图片的http链接
      urls: imgList // 需要预览的图片http链接列表
    })
  },
  goLeaveMessage: function () {
    if(app.globalData.userId == "") {
      wx.redirectTo({
        url: '/pages/login/login'
      })
    }else{
      wx.navigateTo({
        url: '/pages/leaveMessage/leaveMessage?articleId=' + this.data.articleId
      })
    }
  },
  saveBrowsingHistory: function(){
    request({
      url: '/wx/saveBrowsingHistory',
      data: {
        articleId: this.data.articleId,
        userId: app.globalData.userId
      }
    }).then(res => {
        console.log("saveBrowsingHistory:", res)
    })
  },
  getArticleDetail: function(){
    request({
      url: '/wx/getArticleDetail',
      data: {
        articleId: this.data.articleId
      }
    }).then(res => {
        console.log("getArticleDetail:", res)
        this.setData({
          articleDetail: res
        })
    })
  },
  getLeaveMessageList: function(){
    request({
      url: '/wx/getLeaveMessageList',
      data: {
        articleId: this.data.articleId
      }
    }).then(res => {
        console.log("getLeaveMessageList:", res)
        this.setData({
          leaveMessageTotal: res.length,
          leaveMessageList: res
        })
    })
  }

})