// const prevUrl = "http://localhost:8081";

const prevUrl = "http://www.whwen.cn:8085";


export default function request(options) {
  let requestURL = options.url;
  if (!/^http/.test(options.url)) {
    requestURL = prevUrl + requestURL;
  }
  console.log("请求地址：" + requestURL);
  var contenttype = 'application/x-www-form-urlencoded;charset=utf-8';
  if(options.method == "post"){
    contenttype = 'application/json;charset=utf-8';
  }

  return new Promise((resolve, reject) => {
    wx.request({
      //后台请求
      url: requestURL,
      method: options.method,
      data: options.data,
      header: { 'content-type': contenttype},
      success: function (response) {
        const res = response.data
        if(res.code != null) {
          if (res.code == 0) {
            resolve(res.data === undefined ? {} : res.data)
          } else {
            wx.showToast({
              title: res.msg,
              icon: 'none'
            })
            reject(res.msg)
          }
        }
      },
      fail: function (err) {
        wx.showToast({
          title: err.errMsg,
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

const _responseFail = {
  status: -1,
  info: '服务器繁忙，请稍后重试'
}

module.exports = { request: request, prevUrl: prevUrl}
