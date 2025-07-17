<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="手机号">
        <el-input v-model="adminQuery.phone" placeholder="手机号"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >查询</el-button
        >
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>
    <el-button
      type="success"
      @click="openShopDialog()"
      style="float: right; margin-bottom: 10px"
      >新增帐号</el-button
    >

    <!-- 添加和修改表单 -->
    <el-dialog
      width="30%"
      :visible.sync="dialogAdmin"
      :title="adminInfo.id ? '修改' : '新增'"
      @close="closeShop('adminInfo')"
      center
    >
      <el-form
        :model="adminInfo"
        :rules="rules"
        ref="adminInfo"
        label-width="120px"
      >
        <el-form-item
          label="经营店铺"
          style="width: 78%"
          label-width="100px"
          prop="shopList"
        >
<el-select v-model="adminInfo.shopId" placeholder="请选择" style="width: 100%;margin-bottom: 10px;">
            <el-option
              v-for="item in shops"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <!-- <el-select v-model="adminInfo.shopId" placeholder="请选择">
            <el-option
              v-for="item in shops"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select> -->
        </el-form-item>

        <el-form-item
          label="手机号"
          style="width: 78%"
          label-width="100px"
          prop="phone"
        >
          <el-input v-model="adminInfo.phone" placeholder=" 手机号" />
        </el-form-item>

        <el-form-item
          label="登录帐号"
          style="width: 78%"
          label-width="100px"
          prop="username"
        >
          <el-input v-model="adminInfo.username" placeholder=" 登录帐号" />
        </el-form-item>

        <el-form-item
          v-if="!showPassword"
          label="密码"
          style="width: 78%"
          label-width="100px"
          prop="password"
        >
          <el-button type="text" @click="resetting" size="medium"
            >重置</el-button
          >
        </el-form-item>

        <el-form-item
          v-if="showPassword"
          label="密码"
          style="width: 78%"
          label-width="100px"
          type="password"
          prop="password"
        >
          <el-input v-model="adminInfo.password" placeholder=" 密码" />
        </el-form-item>

        <el-form-item
          v-if="showPassword"
          label="确认密码"
          style="width: 78%"
          label-width="100px"
          type="password"
          prop="confirmPsw"
        >
          <el-input v-model="adminInfo.confirmPsw" placeholder=" 确认密码" />
        </el-form-item>
        
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeShop('adminInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('adminInfo')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <!-- 详情 -->
    <el-dialog
      width="30%"
      :visible.sync="dialogInfo"
      title="详情"
      @close="dialogInfo = false"
      center
    >
      <el-form :model="adminInfo" label-width="120px">
        <el-form-item
          label="登录帐号："
          style="width: 78%"
          prop="username"
        >
          <span>{{ adminInfo.username }}</span>
        </el-form-item>
        <el-form-item
          label="经营商户："
          style="width: 78%"
          prop="shopName"
        >
          <span>{{ adminInfo.shopName }}</span>
        </el-form-item>

        <el-form-item
          label="联系电话："
          style="width: 78%"
          prop="phone"
        >
          <span>{{ adminInfo.phone }}</span>
        </el-form-item>
        
        

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogInfo = false">取 消</el-button>
        <el-button type="primary" @click="dialogInfo = false">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="序号" width="100" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="username" label="登录帐号" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" align="center">
      </el-table-column>
      <el-table-column prop="shopName" label="经营商户" align="center">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openInfo(scope.row)"
            >查看详情</el-button
          >
          <el-button type="primary" size="mini" @click="openEditAdmin(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="mini"
            @click="removeDataById(scope.row)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="getList"
    >
    </el-pagination>
  </div>
</template>
<script>
import adminApi from "@/api/admins/account";
export default {

  data() {
    // const validatePassword = (rule, value, callback) => {
    //   if (value.length < 6) {
    //     callback(new Error("密码不能小于6位"));
    //   } else {
    //     callback();
    //   }
    // };

    // var validateSurePassword = (rule, value, callback) => {
    //   if (value !== "") {
    //     if (value.length < 6) {
    //       callback(new Error("密码不能小于6位"));
    //       return false;
    //     } else if (value !== this.adminInfo.password) {
    //       callback(new Error("两次输入的新密码不一致"));
    //       return false;
    //     } else {
    //       callback();
    //     }
    //   }
    // };
    const validatePhone = (rule, value, callback) => {
      const regex = /^1[3-9]\d{9}$/;
      if (!regex.test(value)) {
        callback(new Error("请输入正确的手机号"));
      } else {
        callback();
      }
    };
     const validatePassword = (rule, value, callback) => {
      if (value !== "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的密码"));
      }
    };
    var validateSurePassword = (rule, value, callback) => {
      if (value !== "") {
        if (value !== this.adminInfo.password) {
          callback(new Error("两次输入的新密码不一致"));
          return false;
        } else {
          callback();
        }
      }
    };


    const validateUsername = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的账号"));
      }
    };


    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      adminQuery: {},
      brandList: [],
      adminInfo: {
        shopList: []
      },
      //所在地区组件
      districtOptions: [],
      dialogInfo: false,
      showPassword: true,
      listLoading: false,
      dialogAdmin: false,
      shops: [],
      typeList: [
        {
          value: 0,
          label: "茶饮小程序",
        },
        {
          value: 1,
          label: "饿了么",
        },
        {
          value: 2,
          label: "美团",
        },
      ],
      rules: {
        phone: [
          { required: true, trigger: "blur" , message: "请输入联系电话"},
          { validator: validatePhone, trigger: "blur" },
        ],
        // shopList: [
        //   { required: true, message: "请选择经营商户", trigger: "blur" },
        // ],
        shopId: [
          { required: true, message: "请选择经营商户", trigger: "blur" },
        ],
        username: [
          { required: true, message: "请输入帐号", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePassword, trigger: "blur" },
        ],
        confirmPsw: [
          { required: true, message: "请输入确认密码", trigger: "blur" },
          { validator: validateSurePassword, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getshopsList();
  },
  methods: {
    resetting() {
      this.adminInfo.password = "";
      this.adminInfo.confirmPsw = "";
      this.showPassword = true;
    },

    getList(page = 1) {
      this.page = page;
      adminApi
        .getList(this.page, this.limit, this.adminQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },
    getshopsList() {
      adminApi.getshopsList().then((response) => {
        this.shops = response.data.list;
      });
    },

    resetData() {
      this.adminQuery = {};
      this.getList();
    },
    openShopDialog() {
      this.dialogAdmin = true;
      this.adminInfo = {
        shopList: []
      };
    },
    // 添加
    addAdmin() {
  
      adminApi
        .insertData(this.adminInfo)
        .then((response) => {
            this.dialogAdmin = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getList();
          
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },

    openEditAdmin(data) {
      this.dialogAdmin = true;
      this.showPassword = false;
      adminApi.getInfo(data.id).then((response) => {
        this.adminInfo = response.data.info;
        if(this.adminInfo.shopList == null){
          this.adminInfo.shopList = [];
        }
      });
    },
    // 修改信息
    updateAdmin() {
      adminApi
        .updateData(this.adminInfo.id, this.adminInfo)
        .then((response) => {
            this.dialogAdmin = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getList();
          
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },
    // 删除
    removeDataById(data) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        adminApi.deleteData(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getList();
        });
      });
    },

    // 查看管理员弹窗数据回显
    openInfo(data) {
      this.dialogInfo = true;
      adminApi.getInfo(data.id).then((response) => {
        this.adminInfo = response.data.info;
        if(this.adminInfo.shopList == null){
          this.adminInfo.shopList = [];
        }
      });
    },

    closeShop(adminInfo) {
      this.$refs[adminInfo].clearValidate();
      this.adminInfo = {
        shopList: []
      };
      this.dialogAdmin = false;
      this.showPassword = true;
    },
    // 保存并下一步
    saveOrUpdate(adminInfo) {
      // 判断添加还是修改
      if (!this.adminInfo.id) {
        this.$refs[adminInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[adminInfo].clearValidate();
            this.addAdmin();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[adminInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateAdmin();
          } else {
            return false;
          }
        });
      }
    },
  },
};
</script>
<style>
.el-icon-my-clear:before {
  content: "替";
  font-size: 16px;
  visibility: hidden;
}
</style>