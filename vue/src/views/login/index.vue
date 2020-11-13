<template>
  <div class="login-container">
    <div style="width: 100%;height: 100px;"></div>
    <div class="login-box">
      <el-form autoComplete="on" :model="loginForm" ref="loginForm" class="card-box login-form"  >
        <div class="title">登录</div>
        <el-form-item prop="mobile">
          <el-row>
            <el-col><el-input v-model="loginForm.mobile" placeholder="请输入手机号码"></el-input></el-col>
          </el-row>
        </el-form-item>
        <el-form-item prop="password">
          <el-row>
            <el-col><el-input show-password placeholder="请输入密码"  v-model="loginForm.password" ></el-input></el-col>
          </el-row>
        </el-form-item>
        <el-form-item label-width="0px">
          <el-button  style="width:100%;background-color: #3870F0;color:#FFFFFF;" :loading="loading" @click.native.prevent="handleLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>

  export default {
    name: 'login',
    data() {
      return {
        sendImages:"",
        loginForm: {
          mobile: '',
          password: '',
          captcha: ''
        },
        loading: false
      }
    },
    mounted () {
      this.init()
    },
    methods: {
      init(){

      },
      handleLogin() {
        this.$refs.loginForm.validate(valid => {
          if (valid) {
            this.loading = true
            this.$store.dispatch('Login', this.loginForm).then(data => {
              this.loading = false
              this.$router.push({path: '/'})
            }).catch(() => {
              this.loading = false
            })
          } else {
            return false
          }
        })
      },
    },
  }
</script>
<style rel="stylesheet/scss" lang="scss" >
  @import "../../styles/mixin.scss";
  $a: #7E84A8;
  .login-container {
    background:rgba(255,255,255,1);
    @include relative;
    height: 100%;
    .login-logo{
      position: relative;
      top: 30px;
      left: 30px;
      width: 50px;
    }
    .login-box{
      width: 53%;
      -webkit-box-shadow: 4px 4px 20px 0px rgba(7, 39, 111, 0.15);
      box-shadow: 4px 4px 20px 0px rgba(7, 39, 111, 0.15);
      border-radius: 0px 10px 10px 0px;
      margin: 0 auto;
      height: 65%;
      input {
        outline:none;
        background:transparent;
        border:none;
        outline:medium;
      }
      .el-row{
        border-bottom:1px solid #EAECF3;
      }

      .login-image-center{
        width: 100%;
        height: 40px;
        /* 设置flex布局 */
        display: flex;
        /* 垂直居中 */
        align-items: center;
        /* 水平居中 */
        justify-content: center;
        text-align:center;
      }

    }
    .title {
      font-size: 22px;
      font-family: PingFang-SC-Bold;
      font-weight: bold;
      color: #3870f0;
      text-align: center;
      margin-bottom: 16%;
    }
    .login-form {
      width: 100%;
      height: 100%;
      float: right;
      padding: 40px;
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      border-radius: 5px;
    }
    .thirdparty-button {
      position: absolute;
      right: 35px;
      bottom: 28px;
    }
    a{
      color: $a;
      cursor:pointer;
      font-size: 12px;
    }
  }
</style>
