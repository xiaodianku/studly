<template>
  <div class="app-container">
    <div class="filter-container clearfix">
      <el-form>
        <el-form-item>
          <span class="title-contain">用户优惠卷列表</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus"  @click="useYhj">使用优惠卷</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit  highlight-current-row style="width: 100%">
        <el-table-column align="center" label="用户昵称" prop="nickname"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="优惠卷编码" prop="code"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="优惠卷内容" prop="content"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="是否使用"  min-width="30%">
          <template slot-scope="scope">
            {{scope.row.status =='0'?'未使用':'已使用'}}
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
        :page-sizes="[5, 10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>


    <el-dialog title="使用优惠卷" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space" :model="tempObj"  label-position="left" label-width="100px" style='width: 300px; margin-left:50px;'>
        <el-form-item label="优惠卷编码" >
          <el-input type="text" v-model="tempObj.code" placeholder="请输入">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="createData">确认使用</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        list: [
          {
            code:"",
            content:"",
            status:"",
            nickname:"",
            createTime: ""
          }
        ],//表格的数据
        listLoading: false,//数据加载等待动画

        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          pageNum: 1,//页码
          pageRow: 5,//每页条数
        },
        dialogFormVisible: false,
        tempObj:{
          code: ""
        }
      }
    },
    created() {
      this.getList();
    },
    computed: {

    },
    methods: {
      getList() {
        this.listLoading = true;
        api({
          url: "/market/couponUserList",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
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
      useYhj(){
          this.dialogFormVisible = true;
      },
      createData(){
        let _vue = this;
        this.$confirm('确定使用此优惠卷?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          api({
            url: "/market/useCoupon",
            method: "post",
            data: this.tempObj
          }).then((res) => {
            _vue.$message.success(res)
            _vue.getList()
          })
        })
      }
    }
  }
</script>
<style scoped>
  .tbuser-title{
    font-size: 18px;
    font-family: MicrosoftYaHei-Bold;
    font-weight: bold;
    color: rgba(51,51,51,1);
    /* line-height: 30px; */
    border-left: 4px solid #3873F5;
    padding-left: 13px;
    height: 27px;
    line-height: 18px;
  }
  .total-text{
    font-size:14px;
    font-family:MicrosoftYaHei;
    font-weight:400;
    color:rgba(89,89,89,1);
    width: 100%;
    display: inline-block;
    line-height: 10px;
  }
  .zssj{
    display: block;float: left;width: 30%;
  }

  .grjj{
    font-size: 12px;
    margin-bottom: 3%;
    word-break: break-all; /*允许在单词内换行*/
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box; /*自适应盒子*/
    -webkit-line-clamp: 2; /*此处为1行,如果是两行就改成2*/
    -webkit-box-orient: vertical;
  }
</style>
