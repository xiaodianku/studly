<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">优惠卷管理</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate">添加优惠卷</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中"   height="400" >
        <el-table-column align="center" label="优惠卷内容" prop="content"></el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <a v-if="scope.row.activeNumber == 0"  class="oprator-class" type="danger" icon="delete" @click="removeUser(scope.row.id)">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempUser"  label-position="left" label-width="100px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="优惠卷内容" required>
          <el-input type="text" v-model="tempUser.content"></el-input>
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
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑优惠卷',
          create: '添加优惠卷'
        },
        tempUser: {
          content: ''
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
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        api({
          url: "/market/couponList",
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
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      createUser() {
        if(this.checkdAll()){
          return;
        }
        api({
          url: "/market/addCoupon",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false

        })
      },
      removeUser(id) {
        let _vue = this;
        this.$confirm('确定删除此优惠卷?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          api({
            url: "/market/deleteCoupon",
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
        if(this.tempUser.content  == ""){
          this.$message({
            message: "请输入优惠卷内容",
            type: 'error',
          });
          return true;
        }
        return false;
      },
    }
  }
</script>
