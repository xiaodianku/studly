//app.js
import { request, prevUrl, prevSocketUrl } from 'utils/request.js'
App({
  globalData: {
    userId: "",
    nickname:"",
    img:"",
    beUserId: "",
    initLoadFlag: false,
  },
  onLaunch: function () {
    this.loginWx();
  },
  updateWx: function(){
    const updateManager = wx.getUpdateManager()
    
    updateManager.onCheckForUpdate(function (res) {
        // 请求完新版本信息的回调
        // console.log("9999" + res.hasUpdate)
    })

    updateManager.onUpdateReady(function () {
        // 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
        updateManager.applyUpdate()
    })

    updateManager.onUpdateFailed(function () {
      // 新版本下载失败
    })
  },
  loginWx: function () { // 登录
    var _this = this;
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId 判断是否认证过
        request({
          url: "/wx/getUser?code=" + res.code
        }).then(res => {
            // console.log("获取个人信息", res);
            if (res != null && res != "") {
              _this.globalData.userId = res.id
              _this.globalData.nickname = res.nickname
             _this.globalData.img = res.img
           }
          _this.globalData.initLoadFlag = true;
       }, err => {
            console.log("获取个人信息", err)
            _this.globalData.initLoadFlag = true;
       })
      }
    })
  },
  isLogin: function(){
    if (this.globalData.userId == '') {
      wx.navigateTo({
        url: '/pages/login/login'
      })
    }
  },
  onShow: function (options) {
    console.log("===============appjs=============")
    this.updateWx();
  }
})
