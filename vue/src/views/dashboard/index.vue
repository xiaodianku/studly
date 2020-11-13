<template>
  <div class="app-container" style="background-color:#F5F5F5">


  </div>
</template>
<script>


  export default {
    data() {
      return {

      }
    },
    created() {
      this.$nextTick(function(){

      })
    },
    beforeMount:function(){},
    methods: {

    },
    filters: {
      formatDate: function(time) {
        if(time!=null&&time!="")
        {
          return yyyymmdd(time);
        }else{
          return "";
        }
      }
    }
  }
</script>
<style scoped>
  #addUser,#dzChat,#zfChat{
    width: 100%;
    height: 400px;
    margin: 0 auto;
  }
  #cloudChat{
    width: 100%;
    height: 250px;
  }
  .timeTab{
    line-height: 30px;
    border: 1px solid #000000;
    font-size: 12px;
  }
  .timeTab-paddting{
    padding-left: 10px;
    padding-right: 10px;
  }
  .timeTab-bottom{
    border-top: 1px solid #000000;
    background-color: #D9D9D9;
  }
  .tab-size{
    font-weight: 700;
  }
</style>
