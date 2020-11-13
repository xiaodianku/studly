<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item><span class="title-contain">云盘扩容升级</span></el-form-item>
        <el-form-item>
          <el-row>
            <el-col :span="12" style="border: 1px solid #000000;padding: 20px;">
              <el-row><span class="title-contain">{{cloudName}}</span></el-row>
              <el-row v-for="item in timeData" :key="item.id" >
                <el-row>{{item.value}}：</el-row>
                <el-row v-if="item.relativeKey == 'cloud_size'">
                  <el-radio-group v-model="cloudInfo.cloudSizeId" @change="getAmount">
                    <div v-for="obj in item.relativeList"  :key="obj.id" style="margin-right:10px;float: left;">
                      <el-radio-button :label="obj.id" >{{obj.value}}</el-radio-button>
                    </div>
                  </el-radio-group>
                </el-row>
                <el-row v-if="item.relativeKey == 'cloud_time'">
                  <el-radio-group v-model="cloudInfo.cloudTimeId" @change="getAmount">
                    <div v-for="obj in item.relativeList"  :key="obj.id" style="margin-right:10px;float: left;">
                      <el-radio-button :label="obj.id" >{{obj.value}}</el-radio-button>
                    </div>
                  </el-radio-group>
                </el-row>
                <el-row v-if="item.relativeKey == 'cloud_service'">
                  <el-radio-group v-model="cloudInfo.cloudServiceId" @change="getAmount">
                    <div v-for="obj in item.relativeList"  :key="obj.id" style="margin-right:10px;float: left;">
                      <el-radio-button :label="obj.id" >{{obj.value}}</el-radio-button>
                    </div>
                  </el-radio-group>
                </el-row>
              </el-row>
            </el-col>
            <el-col :span="4">&nbsp;</el-col>
            <el-col :span="6" style="border: 1px solid #000000;padding: 20px;">
              <el-row style="border-bottom: 1px solid #000000;">当前所选配置</el-row>
              <el-row>云盘名称：{{cloudName}}</el-row>
              <el-row>存储空间：{{cloudInfo.cloudSize}}</el-row>
              <el-row>时长：{{cloudInfo.cloudTime}}</el-row>
              <el-row>服务：{{cloudInfo.cloudService}}</el-row>
              <el-row>配置费用：</el-row>
              <el-row>
                <span style="color: #F56C6C;font-size: 26px;">￥{{cloudInfo.nowPrice}}</span>
                &nbsp;&nbsp;
                <span style="text-decoration:line-through;font-size: 16px;">￥{{cloudInfo.oldPrice}}</span>
              </el-row>
              <el-row>
                <el-button type="danger" style="width: 100%;" @click="openQrcode">立即购买</el-button>
              </el-row>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <el-dialog title="代理商微信二维码" :visible.sync="dialogTableVisible">
       <el-row style="text-align: center;margin-bottom: 10px;"><img :src="qrcode" style="width: 40%;"></el-row>
       <el-row style="text-align: center;">微信扫码，添加客服微信进行购买</el-row>
    </el-dialog>
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        cloudName: "",
        timeData: [],
        cloudInfo: {
          cloudName: 0,
          cloudSize: "",
          cloudSizeId: 0,
          cloudService: "",
          cloudServiceId: 0,
          cloudTime: "",
          cloudTimeId: 0,
          nowPrice: "0",
          oldPrice: "0",
        },
        qrcode: "http://img4.imgtn.bdimg.com/it/u=1077302736,3145843276&fm=26&gp=0.jpg",
        dialogTableVisible: false,
      }
    },
    created() {
      this.$nextTick(function(){
        this.getByKeyList();
      })
    },
    beforeMount:function(){

    },
    methods: {
      getByKeyList(){
        api({
          url: "/index/getByKeyList",
          method: "post"
        }).then(data => {
          console.log("getByKeyList 列表" + data)
          this.timeData = data.list;
          this.cloudName = data.cloudName;
          var _vue= this;
          this.timeData.forEach(function (item, index) {
            if(item.relativeKey == 'cloud_size' && item.relativeList.length > 0){
              _vue.cloudInfo.cloudSizeId = item.relativeList[0].id;
            }
            if(item.relativeKey == 'cloud_time' && item.relativeList.length > 0){
              _vue.cloudInfo.cloudTimeId = item.relativeList[0].id;
            }
            if(item.relativeKey == 'cloud_service' && item.relativeList.length > 0){
              _vue.cloudInfo.cloudServiceId = item.relativeList[0].id;
            }
          });
          this.getAmount();
        })
      },
      getAmount(){
        if(this.cloudInfo.cloudTimeId != 0 && this.cloudInfo.cloudServiceId != 0
          && this.cloudInfo.cloudSizeId != 0) {
          api({
            url: "/index/getCloudData",
            method: "post",
            data: this.cloudInfo
          }).then(data => {
            console.log(" " + data)
            this.cloudInfo.cloudSize = data.adminCloud.cloudSize;
            this.cloudInfo.cloudService = data.adminCloud.cloudService;
            this.cloudInfo.cloudTime = data.adminCloud.cloudTime;
            this.cloudInfo.nowPrice = data.adminCloud.nowPrice;
            this.cloudInfo.oldPrice = data.adminCloud.oldPrice;
            this.qrcode = data.qrCode;
          })
        }
      },
      openQrcode(){
        this.dialogTableVisible = true;
      },
    }
  }
</script>
<style scoped>
  #zfChat{
    width: 100%;
    height: 297px;
  }
</style>
