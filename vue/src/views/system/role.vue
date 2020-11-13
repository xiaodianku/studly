<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <span class="title-contain">权限角色</span>
        </el-form-item>
      </el-form>
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" v-if="hasPerm('role:add')" @click="showCreate">添加角色
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="contain-table">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit  highlight-current-row style="width: 100%"
                :header-cell-style="{background:'#F5F6FC',color: '#1A1A1A'}">
        <el-table-column align="center" label="角色名称" prop="roleName"  min-width="20%"></el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime"  min-width="20%" :formatter="yyyymmddhhiiss"></el-table-column>
<!--        <el-table-column align="center" label="用户"  min-width="20%">-->
<!--          <template slot-scope="scope">-->
<!--            <div v-for="user in scope.row.users" style="text-align: left;margin-top:2px;">-->
<!--              <el-tag type="danger">{{user.nickname}}</el-tag>-->
<!--            </div>-->
<!--          </template>-->
<!--        </el-table-column>-->
<!--        <el-table-column align="center" label="菜单&权限"  min-width="40%">-->
<!--          <template slot-scope="scope">-->
<!--            <el-tag v-if="scope.row.id == 1" type="success">全部</el-tag>-->
<!--            <div v-else>-->
<!--              <div v-for="menu in scope.row.menus" style="text-align: left">-->
<!--                <el-tag type="success">{{menu.permissionName}}</el-tag><br/>-->
<!--                <div v-for="perm in menu.nextList" style="text-align: left;margin-top:2px;">-->
<!--                   <el-tag type="warning">{{perm.permissionName}}</el-tag>-->
<!--                   <el-tag v-for="permbuttom in perm.nextList" :key="permbuttom.id" v-text="permbuttom.permissionName"-->
<!--                  style="margin-right: 3px;"-->
<!--                  type="primary"></el-tag>-->
<!--                </div>-->
<!--              </div>-->
<!--            </div>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column align="center" label="操作"  min-width="30%" >
          <template slot-scope="scope" v-if="scope.row.id != 1">
            <div v-if="hasPerm('role:update') ||hasPerm('role:delete') ">
              <a  class="oprator-class" @click="showUpdate(scope.$index)" v-if="hasPerm('role:update')">编辑
              </a>
              <a  class="oprator-class"  v-if=" scope.row.users && scope.row.users.length===0 && hasPerm('role:delete')"  @click="removeRole(scope.$index)">
                删除
              </a>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" customClass="customWidth">
      <el-form class="small-space"   :model="tempRole" label-position="left" label-width="100px" style='width: 600px; margin-left:50px;'>
        <el-form-item label="角色名称"  required>
          <el-input type="text" v-model="tempRole.roleName" style="width: 250px;" maxlength="6">
          </el-input>
        </el-form-item>
        <el-form-item label="权限设置" required>
            <el-tree
              :data="allPermission"
              show-checkbox
              default-expand-all
              node-key="id"
              ref="tree"
              highlight-current
              :props="defaultProps">
            </el-tree>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createRole" :loading="loading">创 建</el-button>
        <el-button type="primary" v-else @click="updateRole" :loading="loading">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        loading: false,
        list: [],//表格的数据
        allPermission: [],
        listLoading: false,//数据加载等待动画
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑权限角色',
          create: '添加权限角色'
        },
        tempRole: {
          roleName: '',
          roleId: '',
          permissions: [],
        },
        defaultProps: {
          children: 'nextList',
          label: 'permissionName'
        },
      }
    },
    created() {
      this.getList();
      this.getAllPermisson();
    },

    methods: {
      getAllPermisson() {
        //查询所有权限
        this.api({
          url: "/system/listAllPermission",
          method: "get"
        }).then(data => {
          // console.log("查询所有权限" + data)
          this.allPermission = data;
        })
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.api({
          url: "/system/getAllRolesList",
          method: "get"
        }).then(data => {
          // console.log("角色查询列表" + data)
          this.listLoading = false;
          this.list = data;
        })
      },
      getIndex($index) {
        //表格序号
        return $index + 1
      },
      showCreate() {
        //显示新增对话框
        // console.log("新增")
        this.tempRole.roleName = '';
        this.tempRole.roleId = '';
        this.tempRole.permissions = [];
        this.checkMenu();
        // console.log(this.tempRole.permissions);
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        // console.log("修改")
        let role = this.list[$index];
        this.tempRole.roleName = role.roleName;
        this.tempRole.roleId = role.id;
        this.tempRole.permissions = [];
        for (let i = 0; i < role.menus.length; i++) {
          var twoList = role.menus[i].nextList;
          if (twoList != null) {
            for (let i = 0; i < twoList.length; i++) {
              this.tempRole.permissions.push(twoList[i].id);
              // var threeList = twoList[i].nextList;
              // if (threeList != null) {
              //   for (let i = 0; i < threeList.length; i++) {
              //     this.tempRole.permissions.push(threeList[i].id);
              //   }
              // }
            }
          }
        }
        this.checkMenu();
        // console.log(this.tempRole.permissions)
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createRole() {
        this.loading = true;
        if(this.checkdAll()){
          return;
        }
        this.tempRole.permissions = this.$refs.tree.getCheckedKeys();
        //添加新角色
        this.api({
          url: "/system/addRole",
          method: "post",
          data: this.tempRole
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
          this.$message({
            message: '新增成功',
            type: 'success'
          })
        }).finally(() => {
          // console.log('无论成功与否都会执行');
          this.loading = false
        });
      },
      updateRole() {
        this.loading = true;
        if(this.checkdAll()){
          return;
        }
        this.tempRole.permissions = this.$refs.tree.getCheckedKeys();

        // console.log(this.tempRole.permissions);
        //修改角色
        this.api({
          url: "/system/updateRole",
          method: "post",
          data: this.tempRole
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
          // loading.close();
          this.$message({
            message: '修改成功',
            type: 'success'
          });
        }).finally(() => {
          // console.log('无论成功与否都会执行');
          this.loading = false
        });
      },
      removeRole($index) {
        let _vue = this;
        this.$confirm('确定删除此角色?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          this.tempRole.roleId = _vue.list[$index].id;
          _vue.api({
            url: "/system/deleteRole",
            method: "post",
            data: this.tempRole
          }).then(() => {
            _vue.getList()
          }).catch(e => {
          })
        })
      },

      checkMenu(){
        this.$nextTick(() => {
          this.$refs.tree.setCheckedKeys(this.tempRole.permissions);
        })
      },
      checkdAll(){
        // console.log("判断")
        if(this.tempRole.roleName  == ""){
          this.$message({  message: "请输入角色名称", type: 'error', });
          return true;
        }
        return false;
      },
    }
  }
</script>
<style scoped>
  .el-form{
    width: 70% !important;
  }
</style>
