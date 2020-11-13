<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">活动时间管理</span>
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
            <el-button type="primary" icon="plus" @click="showCreate">添加活动时间</el-button>
          </el-col>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" height="400">
        <el-table-column align="center" label="时间内容" prop="content" min-width="10%"></el-table-column>
        <el-table-column align="center" label="排序" prop="sort" min-width="10%"></el-table-column>
        <el-table-column align="center" label="关联活动" prop="activeName" min-width="10%"></el-table-column>
        <el-table-column align="center" label="操作" min-width="20%">
          <template slot-scope="scope">
            <a class="oprator-class" type="danger" icon="delete" @click="removeUser(scope.row.id)">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempUser" label-position="left" label-width="100px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="时间内容" required>
          <el-input type="text" v-model="tempUser.content"></el-input>
        </el-form-item>
        <el-form-item label="排序" required>
          <el-input type="text" v-model="tempUser.sort"></el-input>
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
        <el-button type="primary" @click="createUser">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        activeList: [],//表格的数据
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑活动时间',
          create: '添加活动时间'
        },
        tempUser: {
          content: '',
          sort: 0,
          activeId: ""
        },
        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          pageNum: 1,//页码
          pageRow: 100,//每页条数
          activeId: "",
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
          url: "/market/activeTimeList",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.content = "";
        this.tempUser.sort = 0;
        this.tempUser.activeId = this.activeList[0].id;
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      createUser() {
        if (this.checkdAll()) {
          return;
        }
        api({
          url: "/market/addActiveTime",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false

        })
      },
      removeUser(id) {
        api({
          url: "/market/deleteActiveTime",
          method: "post",
          data: {
            id: id
          }
        }).then(() => {
          this.$message.success("删除成功")
          this.getList()
        }).catch(() => {
          _vue.$message.error("删除失败")
        })
      },
      checkdAll() {
        if (this.tempUser.content === "") {
          this.$message({
            message: "请输入公告内容",
            type: 'error',
          });
          return true;
        }
        if (this.tempUser.sort === "") {
          this.$message({
            message: "请输入排序",
            type: 'error',
          });
          return true;
        }
        if (this.tempUser.activeId === "") {
          this.$message({
            message: "活动不能为空",
            type: 'error',
          });
          return true;
        }
        return false;
      },
    }
  }
</script>
