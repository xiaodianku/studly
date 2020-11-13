<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">课程管理</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-col :span="1">
            活动：
          </el-col>
          <el-col :span="6">
            <el-select v-model="listQuery.activeId" style="width: 90%">
              <el-option
                v-for="item in activeList"
                :key="item.id"
                :label="item.title"
                :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" icon="plus" @click="getList">查询</el-button>
          </el-col>
          <el-col :span="6">
            <el-button type="primary" icon="plus" @click="showCreate">添加课程</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中"   height="400" >
        <el-table-column align="center" label="课程名称" prop="name"  min-width="10%"></el-table-column>
        <el-table-column align="center" label="操作"  min-width="20%">
          <template slot-scope="scope">
            <a  class="oprator-class" type="danger" icon="delete" @click="removeUser(scope.row.id)">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempUser"  label-position="left" label-width="100px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="课程名称" required>
          <el-input type="text" v-model="tempUser.name"></el-input>
        </el-form-item>
        <el-form-item label="关联活动" required>
          <el-select v-model="tempUser.activeId" placeholder="请选择">
            <el-option
              v-for="item in activeList"
              :key="item.id"
              :label="item.title"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button  type="primary" @click="createUser">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        activeList:[],
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑课程',
          create: '添加课程'
        },
        tempUser: {
          name: '',
          activeId: ''
        },
        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          pageNum: 1,//页码
          pageRow: 100,//每页条数
          activeId: "",//每页条数
        },
      }
    },
    created() {
      this.getActiveList();
    },
    methods: {
      getActiveList(){
        //查询列表
        this.listLoading = true;
        api({
          url: "/market/activeList",
          method: "get",
          params: {}
        }).then(data => {
          this.activeList = data.list;
          if(this.activeList.length > 0){
            this.listQuery.activeId = this.activeList[0].id;
            this.getList();
          }
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/market/courseList",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.name = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      createUser() {
        if(this.checkdAll()){
          return;
        }
        api({
          url: "/market/addCourse",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false

        })
      },
      removeUser(id) {
        let _vue = this;
        this.$confirm('确定删除此课程?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          api({
            url: "/market/deleteCourse",
            method: "post",
            data: {
              id: id
            }
          }).then(() => {
            _vue.$message.success("删除成功")
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
      checkdAll(){
        if(this.tempUser.name  == ""){
          this.$message({
            message: "请输入课程名称",
            type: 'error',
          });
          return true;
        }
        return false;
      },
    }
  }
</script>
