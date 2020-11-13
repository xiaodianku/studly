import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import {getToken, removeToken} from '@/utils/auth' // 验权
const whiteList = ['/login', '/404'] //白名单,不需要登录的路由

router.beforeEach((to, from, next) => {
  NProgress.start()
  console.log(to.path)
  console.log("【" + store.getters.userId + "]")
  console.log("【" + getToken() + "]")
  if (getToken()) {
    console.log("1")
    //储存用户信息
    store.dispatch('GetInfo').then(() => {})
    if(to.path === '/login'){
      console.log("3")
      next({path: '/'})
    }else{
      next();
    }
  } else if (whiteList.indexOf(to.path) !== -1) {
    //如果前往的路径是白名单内的,就可以直接前往
    next()
  } else {
    //如果路径不是白名单内的,而且又没有登录,就跳转登录页面
    removeToken();
    store.commit('RESET_USER')
    next('/login')
    NProgress.done() // 结束Progress
  }
})
router.afterEach(() => {
  NProgress.done() // 结束Progress
})
