import axios from 'axios'
import {Message, MessageBox} from 'element-ui'
import {getToken, removeToken} from '@/utils/auth'
import store from '../store'
// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 15000,                  // 请求超时时间2
  withCredentials :true,
 })
// request拦截器
service.interceptors.request.use(config => {

  return config
}, error => {
  // Do something with request error
  console.error(error) // for debug
  Promise.reject(error)
})
// respone拦截器
service.interceptors.response.use(
  response => {
    console.log("接口返回结果: ", response)
    var res = response.data;
    if (res.code !== 0) {
      if (res.code === 999 || res.code === 503) {
        Message({
          showClose: true,
          message: res.msg,
          type: 'error',
          duration: 500,
          onClose: () => {
            store.dispatch('FedLogOut').then(() => {
              location.reload()// 为了重新实例化vue-router对象 避免bug
            })
          }
        });
      }else if(res.code === 600 || res.code === 601){
        //页面弹窗
        return Promise.resolve(res)
      }else if(res.code === 508 || res.code === 502){
        store.dispatch('FedLogOut').then(() => {
          location.reload()// 为了重新实例化vue-router对象 避免bug
        })
      }else{
        Message({
          message: res.msg,
          type: 'error'
        });
      }
      return Promise.reject(res.msg)
    } else {
      return Promise.resolve(res.data)
    }
  },
  error => {
    // console.error('err' + error)// for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)
export default service

