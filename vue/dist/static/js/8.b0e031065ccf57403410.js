webpackJsonp([8],{mtmd:function(e,t,s){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=s("4YfN"),a=s.n(i),r=s("9rMa"),l=s("VsUZ"),o={data:function(){return{list:[],listLoading:!1,dialogStatus:"create",dialogFormVisible:!1,textMap:{update:"编辑管理员",create:"添加管理员"},tempUser:{mobile:"",username:"",password:"",againPassword:"",nickname:"",deleteStatus:0,userId:""},totalCount:0,listQuery:{pageNum:1,pageRow:100}}},created:function(){this.getList()},computed:a()({},Object(r.b)(["userId","roleId"])),methods:{getList:function(){var e=this;this.listLoading=!0,Object(l.a)({url:"/system/userList",method:"get",params:this.listQuery}).then(function(t){e.listLoading=!1,e.list=t.list})},showCreate:function(){this.tempUser.roleId="2",this.tempUser.username="",this.tempUser.mobile="",this.tempUser.password="",this.tempUser.nickname="",this.tempUser.userId="",this.dialogStatus="create",this.dialogFormVisible=!0},showUpdate:function(e){var t=this.list[e];this.tempUser.mobile=t.mobile,this.tempUser.username=t.username,this.tempUser.nickname=t.nickname,this.tempUser.userId=t.id,this.tempUser.deleteStatus=t.deleteStatus,this.tempUser.password="",this.dialogStatus="update",this.dialogFormVisible=!0},createUser:function(){var e=this;this.checkdAll()||Object(l.a)({url:"/system/addUser",method:"post",data:this.tempUser}).then(function(){e.getList(),e.dialogFormVisible=!1})},updateUser:function(){var e=this;if(!this.checkdAll()){var t=this;Object(l.a)({url:"/system/updateUser",method:"post",data:this.tempUser}).then(function(){var s="修改成功";e.dialogFormVisible=!1,e.userId===e.tempUser.userId&&(s="修改成功,部分信息重新登录后生效"),e.$message({message:s,type:"success",duration:1e3,onClose:function(){t.getList()}})})}},removeUser:function(e){var t=this,s=this;this.$confirm("确定删除此用户?","提示",{confirmButtonText:"确定",showCancelButton:!1,type:"warning"}).then(function(){var i=s.list[e];t.tempUser.userId=i.id,t.tempUser.deleteStatus=1,Object(l.a)({url:"/system/deleteUser",method:"post",data:t.tempUser}).then(function(){s.getList()}).catch(function(){s.$message.error("删除失败")})})},checkdAll:function(){return""==this.tempUser.nickname?(this.$message({message:"请输入管理员名称",type:"error"}),!0):""==this.tempUser.mobile?(this.$message({message:"请输入手机号码",type:"error"}),!0):11!=this.tempUser.mobile.length?(this.$message({message:"请输入11位手机号码",type:"error"}),!0):""==this.tempUser.password?(this.$message({message:"密码不能为空",type:"error"}),!0):""==this.tempUser.againPassword?(this.$message({message:"确认密码不能为空",type:"error"}),!0):this.tempUser.password!=this.tempUser.againPassword&&(this.$message({message:"两次输入密码不一致",type:"error"}),!0)}}},n={render:function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"app-container"},[s("div",{staticClass:"filter-container"},[s("el-form",[s("el-form-item",[s("span",{staticClass:"title-contain"},[e._v("管理员")])])],1),e._v(" "),s("el-form",[1==e.userId?s("el-form-item",[s("el-button",{attrs:{type:"primary",icon:"plus"},on:{click:e.showCreate}},[e._v("添加管理员\n        ")])],1):e._e()],1)],1),e._v(" "),s("div",{staticClass:"contain-table"},[s("el-table",{directives:[{name:"loading",rawName:"v-loading.body",value:e.listLoading,expression:"listLoading",modifiers:{body:!0}}],attrs:{data:e.list,"element-loading-text":"拼命加载中",height:"400"}},[s("el-table-column",{attrs:{align:"center",label:"管理员",prop:"nickname","min-width":"10%"}}),e._v(" "),s("el-table-column",{attrs:{align:"center",label:"登录账号",prop:"mobile","min-width":"10%"}}),e._v(" "),s("el-table-column",{attrs:{align:"center",label:"创建时间",prop:"createTime","min-width":"20%",formatter:e.yyyymmddhhiiss}}),e._v(" "),s("el-table-column",{attrs:{align:"center",label:"操作","min-width":"20%"},scopedSlots:e._u([{key:"default",fn:function(t){return 1==e.userId?[s("a",{staticClass:"oprator-class",attrs:{type:"primary",icon:"edit"},on:{click:function(s){e.showUpdate(t.$index)}}},[e._v("编辑")]),e._v(" "),s("a",{staticClass:"oprator-class",attrs:{type:"danger",icon:"delete"},on:{click:function(s){e.removeUser(t.$index)}}},[e._v("删除")])]:void 0}}])})],1)],1),e._v(" "),s("el-dialog",{attrs:{title:e.textMap[e.dialogStatus],visible:e.dialogFormVisible,customClass:"customWidth"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[s("el-form",{staticClass:"small-space",staticStyle:{width:"300px","margin-left":"50px"},attrs:{model:e.tempUser,"label-position":"left","label-width":"100px"}},[s("el-form-item",{attrs:{label:"管理员姓名",required:""}},[s("el-input",{attrs:{type:"text",maxlength:"6"},model:{value:e.tempUser.nickname,callback:function(t){e.$set(e.tempUser,"nickname",t)},expression:"tempUser.nickname"}})],1),e._v(" "),s("el-form-item",{attrs:{label:"手机号码",required:""}},[s("el-input",{attrs:{type:"text",maxlength:"11",disabled:"create"!=e.dialogStatus},model:{value:e.tempUser.mobile,callback:function(t){e.$set(e.tempUser,"mobile",t)},expression:"tempUser.mobile"}}),e._v(" "),s("span",{staticClass:"tip-title"},[e._v("请输入11位手机号码")])],1),e._v(" "),[s("el-form-item",{attrs:{label:"密码",required:""}},[s("el-input",{attrs:{"show-password":""},model:{value:e.tempUser.password,callback:function(t){e.$set(e.tempUser,"password",t)},expression:"tempUser.password"}}),e._v(" "),s("span",{staticClass:"tip-title"},[e._v("6至21位数字、字母、符号")])],1),e._v(" "),s("el-form-item",{attrs:{label:"确认密码",required:""}},[s("el-input",{attrs:{"show-password":""},model:{value:e.tempUser.againPassword,callback:function(t){e.$set(e.tempUser,"againPassword",t)},expression:"tempUser.againPassword"}}),e._v(" "),s("span",{staticClass:"tip-title"},[e._v("6至21位数字、字母、符号")])],1)]],2),e._v(" "),s("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[s("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取 消")]),e._v(" "),"create"==e.dialogStatus?s("el-button",{attrs:{type:"primary"},on:{click:e.createUser}},[e._v("确认")]):s("el-button",{attrs:{type:"primary"},on:{click:e.updateUser}},[e._v("修 改")])],1)],1)],1)},staticRenderFns:[]},m=s("/Xao")(o,n,!1,null,null,null);t.default=m.exports}});