// pages/user/user.js
import { request } from '../../utils/request.js'
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: "",
    nickname:"张三",
    image:"../../images/timg.jpg",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({
      title: "我的"
    })
  },
  initLoad: function () {
    var _this = this;
    console.log("个人中心动态加载" + app.globalData.initLoadFlag);
    if (!app.globalData.initLoadFlag) {
      setTimeout(function () {
        _this.initLoad();
      }, 500);
    } else {
      _this.setData({
        nickname: app.globalData.nickname,
        image: app.globalData.img,
        userId: app.globalData.userId
      })
    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    // console.log("进入 onReady");

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.initLoad();
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {},
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {},
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  },

  /**
   * 用户点击右上角分享
   */
  //转发
  onShareAppMessage: function (res) {
    return {
      title: app.globalData.merchantXcx.name,
      path: '/pages/index/index',
      imageUrl: app.globalData.merchantXcx.logo,
      success: function (res) {
        //console.log('成功', res)
      }
    }
  },
  goRecord: function (e) {
    if(app.globalData.userId == "") {
      this.toLogin();
    }else{
      wx.navigateTo({
        url: '/pages/records/records'
      })
    }
  },
  toLogin: function(){
    wx.navigateTo({
      url: '/pages/login/login'
    })
  }
})
