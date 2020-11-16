import {request} from '../../utils/request.js'
const app = getApp()
Page({
  data: {
    //判断小程序的API，回调，参数，组件等是否在当前版本可用。
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    logo: '/images/timg.jpg',
    phone:""
  },
  onLoad: function () {
    
  },
  bindGetUserInfo: function (e) {
    if(this.data.phone == "") {
      wx.showToast({
        title: '手机号不能为空！',
        icon: 'none',
        duration: 1500
      })
      return;
    }
    wx.login({
      success: res => {
        // 获取到用户的 code 之后：res.code
        console.log("用户的code:" + res.code);
        if (e.detail.userInfo) {
          //用户按了允许授权按钮
          // 获取到用户的信息了，打印到控制台上看下
          var userInfo = e.detail.userInfo;
          // console.log("userInfo=", userInfo);
          //新增用户信息
          request({
            url: '/wx/addUser',
            data: {
              nickName: userInfo.nickName,
              avatarUrl: userInfo.avatarUrl,
              code: res.code,
              mobile: this.data.phone,
              beUserId: app.globalData.beUserId
            }
          }).then(res => {
              console.log("新增用户信息结果返回:", res)
              app.globalData.userId = res.id
              app.globalData.nickname = res.nickname
              app.globalData.img = res.img
              this.goActiveDetail();
          })
        } else {
          //用户按了拒绝按钮
          wx.showModal({
            title: '警告',
            content: '您点击了拒绝授权，请授权之后再进入!!!',
            showCancel: false,
            confirmText: '返回授权',
            success: function (res) {
              // 用户没有授权成功，不需要改变 isHide 的值
              if (res.confirm) {
                // console.log('用户点击了“返回授权”');
              }else{
                 this.qxsq();
              }
            }
          });
        }
      }
    })
    
  },
  qxsq:function(e){
    // console.log("取消授权");
    wx.switchTab({
      url: '/pages/user/user'
    })
  },
  goActiveDetail:function(e){
    console.log("授权通过");
    wx.switchTab({
      url: '/pages/course/course'
    })
  },
  bindKeyInput: function (e) {
    this.setData({
      phone: e.detail.value
    })
  },
})
