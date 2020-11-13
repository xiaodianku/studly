import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'element-ui/lib/index.js';
import 'normalize.css/normalize.css'// A modern alternative to CSS resets
import locale from 'element-ui/lib/locale/lang/zh-CN'
import 'echarts/map/js/china.js';

import App from './App'
import router from './router'
import store from './store'
import '@/icons' // icon
import '@/permission' // 权限
import {default as api} from './utils/api'
import {hasPermissionNotButtom} from "./utils/hasPermission";
import {yyyymmddhhiiss} from "./utils/index.js";
import {yyyymmdd2} from "./utils/index.js";
// main.js or index.js
import InfiniteLoading from 'vue-infinite-loading';
// import Waterfall from 'vue-waterfall/lib/waterfall'
// import WaterfallSlot from 'vue-waterfall/lib/waterfall-slot'
import waterfall from 'vue-waterfall2'
Vue.use(waterfall)
Vue.use(InfiniteLoading, { /* options */ });
// main.js里引入并使用
Vue.use(ElementUI, {locale})
Vue.prototype.api = api
//全局的常量
// Vue.component("Waterfall", Waterfall);
// Vue.component("waterfall", waterfall.default);
// Vue.component("WaterfallSlot", WaterfallSlot);
Vue.prototype.hasPerm = hasPermissionNotButtom
Vue.prototype.yyyymmddhhiiss = yyyymmddhhiiss
Vue.prototype.yyyymmdd2 = yyyymmdd2
//生产环境时自动设置为 false 以阻止 vue 在启动时生成生产提示。
Vue.config.productionTip = (process.env.NODE_ENV != 'production')
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: {
    App,
  }
})
