<template>
  <div class="app-container">
    <div class="filter-container clearfix">
      <el-form>
        <el-form-item>
          <span class="title-contain">活动订单表</span>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" height="400">
        <el-table-column align="center" label="订单编号" prop="orderNo"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="订单金额" prop="amount"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="订单状态" prop="status"  min-width="30%">
          <template slot-scope="scope">{{scope.row.status == 0?'未完成':'已完成'}}</template>
        </el-table-column>
        <el-table-column align="center" label="购买用户" prop="nickname"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="购买课程" prop="courseName"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="家长电话" prop="enrollPhone"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="宝宝姓名" prop="enrollName"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="预约时间" prop="time"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="优惠券编码" prop="couponCode"  min-width="30%"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" min-width="40%" :formatter="yyyymmddhhiiss"></el-table-column>
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
  </div>
</template>
<script>
  import {default as api} from '../../utils/api'

  export default {
    data() {
      return {
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画

        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          status: 1,
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },

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
          url: "/market/orderList",
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
