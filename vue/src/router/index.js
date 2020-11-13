import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '',
    component: Layout,
    redirect: '/dashboard',
    name: 'home',
    menu: 'home',
    meta: {title: '首页', icon: 'home'},
    children: [
      {
        path: '',
        component: _import('dashboard/parent'),
        meta: {title: '首页', icon: 'home'},
        menu: 'pyq_source',
        children: [

        ]
      },
    ]
  },
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [

  {
    path: '/marketing',
    component: Layout,
    name: 'marketing',
    redirect: '/marketing',
    menu: 'marketing',
    meta: {title: '课程管理', icon: 'marketing'},
    children: [
      {
        path: 'article',
        name: 'article',
        component: _import('article/list'),
        meta: {title: '文章管理', icon: 'article'},
        menu: 'coupon'
      },

      {
        path: 'user_list',
        name: 'user_list',
        component: _import('marketing/user_list'),
        meta: {title: '会员列表', icon: 'user_list'},
        menu: 'user_list'
      },
    ]
  },

  {
    path: '/system',
    component: Layout,
    name: 'system',
    meta: {title: '设置', icon: 'system'},
    menu: 'system',
    children: [

      {
        path: '',
        name: 'user',
        component: _import('system/user'),
        meta: {title: '管理员', icon: 'user'},
        menu: 'user'
      }
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]
