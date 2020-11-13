// pages/records/records.js
import {request} from '../../utils/request.js'
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        recordList: []
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getRecordList()
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
    getRecordList: function(){
        request({
            url: '/wx/getBrowsingHistory',
            data: {
                userId: app.globalData.userId
            }
          }).then(res => {
              console.log("getRecordList:", res)
              this.setData({
                recordList: res
              })
          })
    },
    goDetail: function(e){
        console.log("goDetail")
        const id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: '/pages/courseDetail/courseDetail?articleId='+id
          })
    },
})