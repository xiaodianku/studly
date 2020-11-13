<template>
  <div class="scroll-container" ref="scrollContainer" @wheel.prevent="handleScroll" >
    <div class="scroll-wrapper" ref="scrollWrapper" :style="{top: top + 'px'}">
      <div class="title_top">
        <img v-if="logo == null" class="user-avatar" :src="logo2">
        <img v-else class="user-avatar" :src="logo">
        &nbsp; &nbsp; {{name}}
      </div>
      <slot></slot>
    </div>
  </div>
</template>

<script>

  import { mapGetters } from 'vuex'
  import logo2 from '../../img/logo.png'
  const delta = 15

  export default {
    name: 'scrollBar',
    data() {
      return {
        top: 0,
        logo2
      }
    },
    computed: {
      ...mapGetters([
        'logo',
        'name',
      ])
    },
    methods: {
      handleScroll(e) {
        const eventDelta = e.wheelDelta || -e.deltaY * 3
        const $container = this.$refs.scrollContainer
        const $containerHeight = $container.offsetHeight
        const $wrapper = this.$refs.scrollWrapper
        const $wrapperHeight = $wrapper.offsetHeight
        if (eventDelta > 0) {
          this.top = Math.min(0, this.top + eventDelta)
        } else {
          if ($containerHeight - delta < $wrapperHeight) {
            if (this.top < -($wrapperHeight - $containerHeight + delta)) {
              this.top = this.top
            } else {
              this.top = Math.max(this.top + eventDelta, $containerHeight - $wrapperHeight - delta)
            }
          } else {
            this.top = 0
          }
        }
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import '../../styles/variables.scss';

  .scroll-container {
    position: relative;
    width: 100%;
    height: 100%;
    .scroll-wrapper {
      position: absolute;
      width: 100%!important;
      height: 10%!important;
    }
    .title_top{
      width: 100%!important;
      height: 100%;
      color: #FFFFFF;
      font-size:14px;
      font-weight:400;
      color:rgba(255,255,255,1);
      background-color: #042341;
      margin-bottom: 18px;
      /* 设置flex布局 */
      display: flex;
      /* 垂直居中 */
      align-items: center;
      /* 水平居中 */
      justify-content: center;
      text-align:center;
      .user-avatar {
        width: 100px;
        height: 60px;
      }
    }
  }
</style>
