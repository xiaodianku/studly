// pages/leaveMessage/leaveMessage.js
import {request} from '../../utils/request.js'
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        lengthMessage: 1000,
        content:"",
        articleId:""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
      console.log("留言：" + options.articleId)
      this.setData({
        articleId: options.articleId
      })
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
    bind: function (e) {
        var _this = this;
        // 获取输入框的内容
        var value = e.detail.value;
        // 获取输入框内容的长度
        var len = parseInt(value.length);
        _this.setData({
          content: value,
          lengthMessage: len
        });
      },
      sendMessage: function(){
        var _this= this;
        request({
          url: '/wx/saveLeaveMessage',
          data: {
            articleId: this.data.articleId,
            userId: app.globalData.userId,
            content: this.data.content
          }
        }).then(res => {
            console.log("sendMessage:", res)
            var pages = getCurrentPages(); //当前页面
            var beforePage = pages[pages.length - 2]; //前一页
            wx.navigateBack({
                success: function () {
                  beforePage.onLoad({articleId: _this.data.articleId}); // 执行前一个页面的onLoad方法
                }
              });
        })
      },
      
})