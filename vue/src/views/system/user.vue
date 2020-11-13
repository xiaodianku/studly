<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">管理员</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item v-if="userId == 1">
          <el-button type="primary" icon="plus" @click="showCreate">添加管理员
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中"  height="400">
       <el-table-column align="center" label="管理员" prop="nickname"  min-width="10%"></el-table-column>
        <el-table-column align="center" label="登录账号" prop="mobile"  min-width="10%"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" min-width="20%" :formatter="yyyymmddhhiiss"></el-table-column>
        <el-table-column align="center" label="操作"  min-width="20%">
          <template slot-scope="scope" v-if="userId == 1">
            <a  class="oprator-class" type="primary" icon="edit" @click="showUpdate(scope.$index)" >编辑</a>
            <a  class="oprator-class" type="danger" icon="delete" @click="removeUser(scope.$index)">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempUser"  label-position="left" label-width="100px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="管理员姓名" required>
          <el-input type="text" v-model="tempUser.nickname" maxlength="6">
          </el-input>
        </el-form-item>
        <el-form-item   label="手机号码" required>
          <el-input type="text" v-model="tempUser.mobile" maxlength="11" :disabled="dialogStatus!='create'"></el-input>
          <span class="tip-title">请输入11位手机号码</span>
        </el-form-item>
        <template>
          <el-form-item   label="密码"  required >
            <el-input show-password v-model="tempUser.password"  ></el-input>
            <span class="tip-title">6至21位数字、字母、符号</span>
          </el-form-item>
          <el-form-item   label="确认密码"  required >
            <el-input show-password   v-model="tempUser.againPassword"> </el-input>
            <span class="tip-title">6至21位数字、字母、符号</span>
          </el-form-item>
        </template>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createUser">确认</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
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
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑管理员',
          create: '添加管理员'
        },
        tempUser: {
          mobile: '',
          username: '',
          password: '',
          againPassword: '',
          nickname: '',
          deleteStatus: 0,
          userId: ''
        },
        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          pageNum: 1,//页码
          pageRow: 100,//每页条数
        },
      }
    },
    created() {
      this.getList();
    },
    computed: {
      ...mapGetters([
        'userId','roleId'
      ])
    },
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/system/userList",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
        })
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.roleId = "2";
        this.tempUser.username = "";
        this.tempUser.mobile = "";
        this.tempUser.password = "";
        this.tempUser.nickname = "";
        this.tempUser.userId = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.tempUser.mobile = user.mobile;
        this.tempUser.username = user.username;
        this.tempUser.nickname = user.nickname;
        this.tempUser.userId = user.id;
        this.tempUser.deleteStatus = user.deleteStatus;
        this.tempUser.password = '';
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createUser() {
        if(this.checkdAll()){
          return;
        }
        api({
          url: "/system/addUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false

        })
      },
      updateUser() {
        if(this.checkdAll()){
          return;
        }
        //修改用户信息
        let _vue = this;
        api({
          url: "/system/updateUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.userId === this.tempUser.userId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
          this.tempUser.userId = user.id;
          this.tempUser.deleteStatus = 1;
          // console.log("deleteuser::"  + user);
          api({
            url: "/system/deleteUser",
            method: "post",
            data: this.tempUser
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
      checkdAll(){
        if(this.tempUser.nickname  == ""){
          this.$message({
            message: "请输入管理员名称",
            type: 'error',
          });
          return true;
        }

        if(this.tempUser.mobile  == ""){
          this.$message({
            message: "请输入手机号码",
            type: 'error',
          });
          return true;
        }
        if(this.tempUser.mobile.length  != 11){
          this.$message({
            message: "请输入11位手机号码",
            type: 'error',
          });
          return true;
        }
        if(this.tempUser.password  == ""){
          this.$message({
            message: "密码不能为空",
            type: 'error',
          });
          return true;
        }
        if(this.tempUser.againPassword  == ""){
          this.$message({
            message: "确认密码不能为空",
            type: 'error',
          });
          return true;
        }
        if(this.tempUser.password  != this.tempUser.againPassword) {
          this.$message({
            message: "两次输入密码不一致",
            type: 'error',
          });
          return true;
        }
        return false;
      },
    }
  }
</script>
