// pages/courses/course.js
import {request} from '../../utils/request.js'
const app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        open: false,
        // mark 是指原点x轴坐标
        mark: 0,
        // newmark 是指移动的最新点的x轴坐标 
        newmark: 0,
        istoright: true,
        articleList:[],
        categoryList:[],
        categoryId:""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getCategoryList();
        this.getArticleList();
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

    goDetail: function(e){
        console.log("goDetail")
        if(this.data.open) {
            return;
        }
        const id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: '/pages/courseDetail/courseDetail?articleId='+id
          })
    },
    // 点击左上角小图标事件
    tap_ch: function(e) {
        if (this.data.open) {
            this.setData({
                open: false
            });
        } else {
            this.setData({
                open: true
            });
        }
    },
    tap_drag:function(e){
        this.data.newmarkX = e.touches[0].pageX
        this.data.newmarkY = e.touches[0].pageY
    },
    tap_start: function(e) {
        console.log(e)
        // touchstart事件
        // 把手指触摸屏幕的那一个点的 x 轴坐标赋值给 mark 和 newmark
        this.data.markX = e.touches[0].pageX;
    },
    tap_end: function(e) {
        console.log(e)
        // touchend事件
        // touchmove事件
        console.log(this.data.newmarkX)
        console.log(this.data.markX)
        // 手指从左向右移动
        if (this.data.markX+20 < this.data.newmarkX) {
            this.setData({
                open: true
            });
        }
        // 手指从右向左移动
        if (this.data.markX > this.data.newmarkX+20) {
            this.setData({
                open: false
            });
        }
        //纵滑待续

    },
    closeTc: function(){
        console.log("closeTc")
        if(this.data.open) {
            this.setData({
                open: false
            });
        }
    },
    getArticleList: function(){
        request({
            url: '/wx/getArticleList',
            data: {
                categoryId: this.data.categoryId
            }
          }).then(res => {
              console.log("getArticleList:", res)
              this.setData({
                articleList: res
            });
          })
    },
    getCategoryList: function(){
        request({
            url: '/wx/getCategoryList',
            data: {}
          }).then(res => {
              console.log("getCategoryList:", res)
              this.setData({
                categoryList: res
            });
          })
    },
    changeCategory: function(e){
        console.log("changeCategory")
        var categoryId=e.currentTarget.dataset.categoryid;
        this.setData({
            categoryId: categoryId,
            open: false
        });
        this.getArticleList()
    }

})