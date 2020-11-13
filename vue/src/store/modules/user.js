import {getInfo, login, logout} from '@/api/login'
import {getToken, removeToken, setToken} from '@/utils/auth'
import {default as api} from '../../utils/api'
import store from '../../store'
import router from '../../router'

const user = {
  state: {
    nickname: "",
    userId: "",
    merchantId: "",
    name: "",
    logo: "",
    avatar: 'https://www.gravatar.com/avatar/6560ed55e62396e40b34aac1e5041028',
    role: '',
    roles: [],
    menus: [],
    permissions: [],
  },
  mutations: {
    SET_USER: (state, userInfo) => {
      state.nickname = userInfo.user.nickname;
      state.name = userInfo.user.name;
      state.merchantId = userInfo.user.merchantId;
      state.logo = userInfo.user.logo;
      state.userId = userInfo.user.id;
      // state.role = userInfo.roles[0].roleName;
      state.roles = userInfo.roles;
      state.permissions = userInfo.permissions;
    },
    RESET_USER: (state) => {
      state.nickname = "";
      state.userId = "";
      state.merchantId = "";
      state.role = '';
      state.roles = [];
      state.permissions = [];
    }
  },
  actions: {
    // 登录
    Login({commit, state}, loginForm) {
      return new Promise((resolve, reject) => {
        api({
          url: "home/login",
          method: "post",
          data: loginForm
        }).then(data => {
          //cookie中保存前端登录状态
          // console.log("cookie中保存前端登录状态" + data);
          setToken();
          resolve(data);
          store.dispatch('GetInfo').then(() => {})
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户信息
    GetInfo({commit, state}) {
      return new Promise((resolve, reject) => {
        api({
          url: '/system/userinfo',
          method: 'post'
        }).then(data => {
          console.log("userinfo::", data)
          //储存用户信息
          commit('SET_USER', data);
          //cookie保存登录状态,仅靠vuex保存的话,页面刷新就会丢失登录状态
          setToken();
          //生成路由
          store.dispatch('GenerateRoutes', data).then(() => {
            //生成该用户的新路由json操作完毕之后,调用vue-router的动态新增路由方法,将新路由添加
            router.addRoutes(store.getters.addRouters)
          })
          resolve(data)
        }).catch(error => {
          console.log("获取用户信息失败::", error)
          reject(error)
        })
      })
    },
    // 登出
    LogOut({commit}) {
      return new Promise((resolve) => {
        api({
          url: "home/logout",
          method: "post"
        }).then(data => {
          commit('RESET_USER')
          removeToken()
          resolve(data);
        }).catch(() => {
          commit('RESET_USER')
          removeToken()
        })
      })
    },
    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('RESET_USER')
        removeToken()
        resolve()
      })
    }
  }
}
export default user
