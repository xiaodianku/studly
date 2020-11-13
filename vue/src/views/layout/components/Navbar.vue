<template>
  <el-menu class="navbar" mode="horizontal">
<!--    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened"></hamburger>-->
<!--    <breadcrumb></breadcrumb>-->
        <div class="avatar-container" @click="logout" @mouseenter="showActive(1)" @mouseleave="showActive(0)">
          {{nickname}}
          <img v-if="active === 0" :src="loginout">
          <img v-if="active === 1" :src="activeloginout">
        </div>
<!--    <el-dropdown class="avatar-container" trigger="click">-->
<!--      <div class="avatar-wrapper">-->
<!--        <img class="user-avatar" :src="headimage">-->
<!--        <i class="el-icon-caret-bottom"></i>-->
<!--      </div>-->
<!--      <el-dropdown-menu class="user-dropdown" slot="dropdown">-->
<!--        <router-link class="inlineBlock" to="/">-->
<!--          <el-dropdown-item>-->
<!--            Home-->
<!--          </el-dropdown-item>-->
<!--        </router-link>-->
<!--        <el-dropdown-item divided>-->
<!--          <span @click="logout" style="display:block;">LogOut</span>-->
<!--        </el-dropdown-item>-->
<!--      </el-dropdown-menu>-->
<!--    </el-dropdown>-->
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
// import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import headimage from '@/img/headimage.png'
import loginout from '@/img/loginout.png'
import activeloginout from '@/img/activeloginout.png'

export default {
  data() {
    return {
      headimage,
      loginout,
      activeloginout,
      active: 0
    }
  },
  components: {
    // Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'nickname',
    ])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$confirm('确定要退出当前账号吗?', '退出登录', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        // type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.reload() // 为了重新实例化vue-router对象 避免bug
        })
      })
    },
    showActive(index) {
      this.active = index;
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 10%;
  border-radius: 0px !important;
  z-index: 1000;
  width: 100%;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    width: 20%;
    height: 100%;
    float: right;
    color:#737373;
    /* 设置flex布局 */
    display: flex;
    /* 垂直居中 */
    align-items: center;
    /* 水平居中 */
    justify-content: center;
    text-align:center;
    letter-spacing: 2px;
    img{
      width: 20px;
      height: 20px;
      display: inline-block;
      margin-left: 20px;
    }
  }
  .avatar-container:hover{
    color:#3873F5;
    cursor: pointer;
  }


}
</style>

