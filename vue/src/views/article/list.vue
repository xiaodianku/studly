<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">文章管理</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate">添加文章</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" height="400">
        <el-table-column align="center" label="文章编号" prop="id" min-width="25%"></el-table-column>
        <el-table-column align="center" label="title图片" prop="imageUrl" min-width="20%">
          <template slot-scope="scope">
            <img :src="scope.row.titleImg" style="height: 40px;">
          </template>
        </el-table-column>
        <el-table-column align="center" label="标题" prop="title" min-width="25%"></el-table-column>
        <el-table-column align="center" label="课程名称" prop="courseName" min-width="25%"></el-table-column>
        <el-table-column align="center" label="类别" prop="categoryName" min-width="25%"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="gmtCreate" min-width="40%"
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

    <div class="page-bottom">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-size="listQuery.pageRow"
        :total="totalCount"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>


    <el-dialog :title="title" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempObj" label-position="left" label-width="100px"
               style='width: 71%; margin-left:50px;'>
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
        <el-form-item label="类别">
          <el-select v-model="tempObj.categoryId" placeholder="请选择">
            <el-option
              v-for="item in categoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="tempObj.content" placeholder="请输入" :rows="4">
          </el-input>
        </el-form-item>
        <el-form-item label="详情图">
          <div class="imagelist clearfix">
            <div v-for="(obj,index) in tempObj.imageList">
              <div
                style="position:relative;width: 148px; float: left; margin-right: 1%;  margin-bottom: 2%;border: 1px solid #d9d9d9 !important">
                <el-image :src="obj" :preview-src-list="tempObj.imageList"></el-image>
                <div class="guandiao" @click="handleRemoveResource(obj, 1)">x</div>
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
        <el-form-item label="音频">
          <div class="video-list videolist" style="position: relative;">
            <el-upload
              class="avatar-uploader"
              :action="uploadurl"
              :show-file-list="false"
              :on-success="handleResourceSuccessVideo"
              :on-remove="handleRemoveVideo"
              :before-upload="beforeUploadVideo">
              <div style="position: relative;" >
                <i v-if="tempObj.videoList != null && tempObj.videoList.length == 0" class="el-icon-plus avatar-uploader-icon"></i>
                <div v-else v-for="(obj,index) in tempObj.videoList">
                  <video :src="obj" class="video-avatar" controls="controls" style="height: 180px">
                    您的浏览器不支持视频播放
                  </video>
                </div>
              </div>
            </el-upload>
            <span class="tip-title">最多上传一个视频</span>
            <div v-if="tempObj.videoList != null && tempObj.videoList.length > 0" class="guandiao" @click="handleRemoveResource(tempObj.videoList[0], 2)">x</div>
          </div>
        </el-form-item>
        <el-form-item label="是否开启活动">
          <el-radio-group v-model="tempObj.status">
            <el-radio :label="0">关闭</el-radio>
            <el-radio :label="1">启用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateData">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        categoryList: [],//类型列表
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        title: "新增文章",
        tempObj: {},
        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },
      }
    },
    created() {
      this.getCategoryList();
      this.getList();
      this.uploadurl = process.env.BASE_URL + "/home/uploadImage";
    },
    methods: {
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      getCategoryList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/category/getList",
          method: "get",
        }).then(data => {
          this.categoryList = data;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/article/getList",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      showCreate() {
        console.log("showCreate")
        this.tempObj = {
          categoryId: this.categoryList[0].id,
          status:0,
          images:"",
          videos:""
        };
        this.tempObj.imageList = [];
        this.tempObj.videoList = [];
        this.dialogStatus = "add";
        this.title = "新增文章";
        this.dialogFormVisible = true
      },
      showUpdate(obj) {
        console.log("showUpdate", obj)
        this.tempObj = obj;
        var imageList = [];
        var videoList = [];
        if(this.tempObj.images.indexOf(",") > 0) {
          var list = this.tempObj.images.split(',');
          for (let i = 0; i < list.length; i++) {
            if(list[i] !== ""){
              imageList.push(list[i])
            }
          }
        }
        this.tempObj.imageList = imageList;

        if(this.tempObj.videos.indexOf(",") > 0) {
          var list = this.tempObj.videos.split(',');

          for (let i = 0; i < list.length; i++) {
            if(list[i] !== ""){
              videoList.push(list[i])
            }
          }
        }
        this.tempObj.videoList = videoList;

        this.dialogStatus = "update";
        this.title = "修改文章";
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
            message: "操作成功",
            type: 'success',
          })
        })
      },
      checkdAll() {
        return false;
      },
      handleAvatarSuccess(res, file) {
        console.log("handleAvatarSuccess")
        if (res.code === 0) {
          this.tempObj.titleImg = res.data;
          this.$forceUpdate();
        } else {
          this.$message.error(res.msg);
        }
      },
      beforeAvatarUpload(file) {
        return true;
      },
      //上传图片.视频移除方法
      handleRemoveResource(url, type) {
        console.log("删除"+url)
        var resourceList = [];
        if(type==1){
          resourceList = this.tempObj.imageList;
        }
        if(type ==2){
          resourceList = this.tempObj.videoList;
        }
        for (let i in resourceList) {
          if (resourceList[i] === url) {
            resourceList.splice(i, 1);
          }
        }
        if(type ==1){
          this.tempObj.imageList = resourceList ;
        }
        if(type ==2){
          this.tempObj.videoList = resourceList ;
        }
        this.$forceUpdate();
      },

      //上传前回调
      beforeUpload(file) {
        return true;
      },
      handleRemove(file, fileList) {
        var urlStr = file.url
        var resourceList  = this.tempObj.imageList;
        for (let i in resourceList) {
          if (resourceList[i] === urlStr) {
            resourceList.splice(i, 1);
          }
        }
        this.tempObj.imageList = resourceList ;
        this.$forceUpdate();
        return false;
      },
      handleResourceSuccess(res, files, fileList) {
        var slUrl = files.response.data;
        console.log("上传成功", slUrl);
        this.tempObj.imageList.push(slUrl);
        console.log(this.tempObj.imageList);
        this.$forceUpdate();

      },



      handleResourceSuccessVideo(res, files, fileList) {
        var slUrl = files.response.data;
        console.log("上传成功", slUrl);
        this.tempObj.videoList.push(slUrl);
        console.log(this.tempObj.videoList);
        this.$forceUpdate();

      },
      beforeUploadVideo(file) {
          var fileSize = file.size / 1024 / 1024 < 50;
          if (['video/mp4', 'video/ogg', 'video/flv', 'video/avi', 'video/wmv', 'video/rmvb', 'video/mov'].indexOf(file.type) == -1) {
            this.$message.error("请上传正确的视频格式");
            return false;
          }
          if (!fileSize) {
            this.$message.error("视频大小不能超过50MB");
            return false;
          }
      },
      handleRemoveVideo(file, fileList) {
        var urlStr = file.url
        var resourceList  = this.tempObj.videoList;
        for (let i in resourceList) {
          if (resourceList[i] === urlStr) {
            resourceList.splice(i, 1);
          }
        }
        this.tempObj.videoList = resourceList ;
        this.$forceUpdate();
        return false;
      },
    }
  }
</script>
