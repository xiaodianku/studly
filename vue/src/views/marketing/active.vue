<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">活动页</span>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" height="400">
        <el-table-column align="center" label="活动编号" prop="id" min-width="25%"></el-table-column>
        <el-table-column align="center" label="title图片" prop="imageUrl" min-width="20%">
          <template slot-scope="scope">
            <img :src="scope.row.titleImg" style="height: 40px;">
          </template>
        </el-table-column>
        <el-table-column align="center" label="标题" prop="title" min-width="25%"></el-table-column>
        <el-table-column align="center" label="规则" prop="remark" min-width="25%"></el-table-column>
        <el-table-column align="center" label="实际价格" prop="actionAmount" min-width="25%"></el-table-column>
        <el-table-column align="center" label="出售价格" prop="amount" min-width="25%"></el-table-column>
        <!--        <el-table-column align="center" label="已售人数" prop="soldNumber"  min-width="25%"></el-table-column>-->
        <!--        <el-table-column align="center" label="浏览人数" prop="lookNumber"  min-width="25%"></el-table-column>-->
        <!--        <el-table-column align="center" label="分享人数" prop="shareNumber"  min-width="25%"></el-table-column>-->
        <el-table-column align="center" label="详情图片" prop="imageUrl" min-width="20%">
          <template slot-scope="scope">
            <img :src="scope.row.detailImg" style="height: 40px;">
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" min-width="40%"
                         :formatter="yyyymmddhhiiss"></el-table-column>
        <el-table-column align="center" label="操作" min-width="20%">
          <template slot-scope="scope">
            <el-col :span="6">
              <el-button type="primary" icon="plus" @click="showUpdate(scope.row)">编辑</el-button>
            </el-col>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <el-dialog title="修改活动" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempObj" label-position="left" label-width="100px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item required label="title图片" required>
          <div class="rotary-image">
            <el-upload
              class="avatar-uploader"
              :action="uploadurl"
              accept="image/jpeg,image/gif,image/png"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="tempObj.titleImg" :src="tempObj.titleImg" class="avatar2">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="标题">
          <el-input type="text" v-model="tempObj.title" placeholder="请输入">
          </el-input>
        </el-form-item>
        <el-form-item label="规则">
          <el-input type="text" v-model="tempObj.remark" placeholder="请输入">
          </el-input>
        </el-form-item>
        <el-form-item label="实际价格">
          <el-input type="number" v-model="tempObj.actionAmount" placeholder="请输入">
          </el-input>
        </el-form-item>
        <el-form-item label="出售价格">
          <el-input type="number" v-model="tempObj.amount" placeholder="请输入">
          </el-input>
        </el-form-item>
        <el-form-item label="每单返利金额">
          <el-input type="number" v-model="tempObj.backAmount" placeholder="请输入">
          </el-input>
        </el-form-item>
                <el-form-item required label="详情图片" required>
                  <div class="rotary-image">
                    <el-upload
                      class="avatar-uploader"
                      :action="uploadurl"
                      accept="image/jpeg,image/gif,image/png"
                      :show-file-list="false"
                      :on-success="handleAvatarSuccessDetail"
                      :before-upload="beforeAvatarUpload">
                      <img v-if="tempObj.detailImg" :src="tempObj.detailImg" class="avatar2">
                      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                  </div>
                </el-form-item>
        <el-form-item label="活动详情类型">
          <el-radio-group v-model="tempObj.type">
            <el-radio :label="1">图片</el-radio>
            <el-radio :label="2">视频</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="" v-if="tempObj.type == 1">
          <div class="imagelist clearfix">
            <div v-for="(obj,index) in tempObj.resourceList1">
              <div
                style="position:relative;width: 148px;height: 148px; float: left; margin-right: 1%;  margin-bottom: 2%;border: 1px solid #d9d9d9 !important">
                <el-image :src="obj" :preview-src-list="tempObj.resourceList1"></el-image>
                <div class="guandiao" @click="handleRemoveResource(obj)">x</div>
              </div>
            </div>
            <el-upload
              class="avatar-uploader"
              list-type="picture-card"
              :action="uploadurl"
              :show-file-list="false"
              multiple
              :before-upload="beforeUpload"
              :on-remove="handleRemove"
              :on-success="handleResourceSuccess"
            >
              <i class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item label="" v-if="tempObj.type == 2">
          <div class="video-list">
            <el-upload
              class="avatar-uploader"
              :action="uploadurl"
              :show-file-list="false"
              :on-success="handleResourceSuccess"
              :before-upload="beforeUpload">
              <div style="position: relative;">
                <i v-if="tempObj.resourceList2.length == 0" class="el-icon-plus avatar-uploader-icon"></i>
                <div v-else v-for="(obj,index) in tempObj.resourceList2">
                  <video :src="obj" class="video-avatar" controls="controls" style="height: 180px">
                    您的浏览器不支持视频播放
                  </video>
                </div>
              </div>
            </el-upload>
            <span class="tip-title">最多上传一个视频</span>
          </div>
        </el-form-item>

        <el-form-item label="是否结束活动">
          <el-radio-group v-model="tempObj.status">
            <el-radio :label="0">进行中</el-radio>
            <el-radio :label="1">结束</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否开启活动">
          <el-radio-group v-model="tempObj.openStatus">
            <el-radio :label="0">关闭</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="关联优惠卷">
          <el-select v-model="tempObj.couponId" placeholder="请选择">
            <el-option
              v-for="item in couponListData"
              :key="item.id"
              :label="item.content"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateData">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        couponListData: [],//优惠卷列表
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '修改活动信息',
        },
        tempObj: {},
      }
    },
    created() {
      this.couponListAll();
      this.getList();
      this.uploadurl = process.env.BASE_URL + "/home/uploadImage";
    },
    methods: {
      couponListAll() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/market/couponListAll",
          method: "get",
        }).then(data => {
          this.couponListData = data;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/market/activeList",
          method: "get",
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      showUpdate(obj) {
        console.log("showupdate", obj)
        this.tempObj = obj;
        if(this.tempObj.type ==1){
          this.tempObj.resourceList1 = this.tempObj.resourceList;
          this.tempObj.resourceList2 = [];
        }
        if(this.tempObj.type ==2){
          this.tempObj.resourceList1 = [];
          this.tempObj.resourceList2 = this.tempObj.resourceList;
        }
        this.dialogStatus = "update";
        this.dialogFormVisible = true
      },
      updateData() {
        if (this.checkdAll()) {
          return;
        }
        var detailImgs = "";
        for (let i = 0; i < this.tempObj.imageList.length; i++) {
          detailImgs += this.tempObj.imageList[i] + ",";
        }
        var detailVideo = "";
        for (let i = 0; i < this.tempObj.videoList.length; i++) {
          detailVideo += this.tempObj.videoList[i] + ",";

        }
        this.tempObj.images = detailImgs;
        this.tempObj.videos = detailVideo;
        api({
          url: "/article/saveOrUpdateArticle",
          method: "post",
          data: this.tempObj
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
          this.$message({
            message: "修改成功",
            type: 'success',
          })
        })
      },
      checkdAll() {
        return false;
      },
      handleAvatarSuccess(res, file) {
        console.log(res)
        if (res.code == 0) {
          this.tempObj.titleImg = res.data;
        } else {
          this.$message.error(res.msg);
        }
      },
      handleAvatarSuccessDetail(res, file) {
        console.log(res)
        if (res.code == 0) {
          this.tempObj.detailImg = res.data;
        } else {
          this.$message.error(res.msg);
        }
      },
      beforeAvatarUpload(file) {
        // const isLt2M = file.size / 1024 / 1024 < 2;
        // if (!isLt2M) {
        //   this.$message.error('上传头像图片大小不能超过 2MB!');
        // }
        return true;
      },
      //上传图片.视频移除方法
      handleRemoveResource(url) {
        console.log("删除"+url)
        var resourceList = [];
        if(this.tempObj.type ==1){
          resourceList = this.tempObj.resourceList1;
        }
        if(this.tempObj.type ==2){
          resourceList = this.tempObj.resourceList2;
        }
        for (let i in resourceList) {
          if (resourceList[i] === url) {
            resourceList.splice(i, 1);
          }
        }
        if(this.tempObj.type ==1){
          this.tempObj.resourceList1 = resourceList ;
        }
        if(this.tempObj.type ==2){
          this.tempObj.resourceList2 = resourceList ;
        }
        this.$forceUpdate();
      },
      //上传前回调
      beforeUpload(file) {
        if (this.tempObj.type == 1) {
          // const isLt2M = file.size / 1024 / 1024 < 2;
          // if (!isLt2M) {
          //   this.$message.error('上传图片大小不能超过 2MB!');
          // }
          return true;
        } else {
          var fileSize = file.size / 1024 / 1024 < 50;
          if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb', 'video/mov'].indexOf(file.type) == -1) {
            this.$message.error("请上传正确的视频格式");
            return false;
          }
          if (!fileSize) {
            this.$message.error("视频大小不能超过50MB");
            return false;
          }
        }
      },
      handleRemove(file, fileList) {
        // console.log("移除照片");
        var urlStr = file.url

        var resourceList = [];
        if(this.tempObj.type ==1){
          resourceList = this.tempObj.resourceList1;
        }
        if(this.tempObj.type ==2){
          resourceList = this.tempObj.resourceList2;
        }
        for (let i in resourceList) {
          if (resourceList[i] === urlStr) {
            resourceList.splice(i, 1);
          }
        }
        if(this.tempObj.type ==1){
          this.tempObj.resourceList1 = resourceList ;
        }
        if(this.tempObj.type ==2){
          this.tempObj.resourceList2 = resourceList ;
        }
        this.$forceUpdate();
      },
      handleResourceSuccess(res, files, fileList) {
        var slUrl = files.response.data;
        console.log("上传成功", slUrl);
        if (this.tempObj.type == 2) {
          var resourceList  = [];
          resourceList.push(slUrl);
          this.tempObj.resourceList2= resourceList;
        } else {
          this.tempObj.resourceList1.push(slUrl);
        }
        console.log( this.tempObj.resourceList2);
        console.log( this.tempObj.resourceList1);
        this.$forceUpdate();

      },
    }
  }
</script>
