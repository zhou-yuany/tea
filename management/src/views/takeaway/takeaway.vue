<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="联系电话">
        <el-input v-model="takeawayQuery.phone" placeholder="联系电话"></el-input>
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
      @click="openAgentsDialog()"
      style="float: right; margin-bottom: 10px"
      >新增</el-button
    >

    <!-- 添加和修改表单 -->
    <el-dialog
      :visible.sync="dialogAgent"
      :title="takeawayInfo.id ? '修改' : '新增'"
      @close="closeShop('takeawayInfo')"
      center
    >
      <el-form
        :model="takeawayInfo"
        :rules="rules"
        ref="takeawayInfo"
        label-width="120px"
      >
        <div style="display: flex;">
        <el-form-item
            label="姓名"
            style="width: 78%"
            label-width="100px"
            prop="name"
          >
            <el-input v-model="takeawayInfo.name" placeholder=" 姓名" />
          </el-form-item>
          <el-form-item
            label="联系电话"
            style="width: 78%"
            label-width="100px"
            prop="phone"
          >
            <el-input v-model="takeawayInfo.phone" placeholder=" 联系电话" />
          </el-form-item>
        </div>
        <div style="display: flex;">
        <el-form-item
          label="所在城市"
          prop="district"
          style="width: 78%"
          label-width="100px"
        >
          <el-cascader
            v-model="takeawayInfo.district"
            placeholder="请选择"
            :options="districtOptions"
            clearable
            filterable
            style="width: 100%"
            @change="onChange"
          ></el-cascader>
        </el-form-item>

        <el-form-item
          label="详细地址"
          style="width: 78%"
          label-width="100px"
          prop="address"
        >
          <el-input v-model="takeawayInfo.address" placeholder=" 详细地址" />
        </el-form-item>
        </div>
       
        <div style="display: flex;">
          <el-form-item
          label="是否分账"
          prop="divideAccounts"
          style="width: 78%"
          label-width="100px"
        >
          <el-select
            clearable
            filterable
            v-model="takeawayInfo.divideAccounts"
            placeholder="请选择"
          >
            <el-option
              v-for="typePro in divideList"
              :key="typePro.value"
              :label="typePro.label"
              :value="typePro.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="takeawayInfo.divideAccounts == 1"
          label="分账比例"
          style="width: 78%"
          label-width="100px"
          prop="proportion"
        >
          <el-input
            type="number"
            v-model="takeawayInfo.proportion"
            @input="changeInput"
            placeholder=" 分账比例"
          >
          <template slot="append" >
            <span>%</span>
          </template>
          </el-input>
        </el-form-item>
        </div>
      
        <el-form-item
                label="是否禁用" style="justify-content: flex-start;margin-left: 10px;"
              >
                <el-switch
                  v-model="takeawayInfo.isUse"
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="可使用"
                  inactive-text="已禁用"
                  :active-value="1"
                  :inactive-value="2"
                >
                </el-switch>
              </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeShop('takeawayInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('takeawayInfo')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <!-- 详情 -->
    <el-dialog
      width="50%"
      :visible.sync="dialogInfo"
      title="详情"
      @close="dialogInfo = false"
      center
    >
      <el-form :model="takeawayInfo" label-width="120px">
        <div style="display: flex;">
          <el-form-item label="姓名：" style="width: 78%" prop="phone">
            <span>{{ takeawayInfo.name }}</span>
          </el-form-item>
          <el-form-item label="联系电话：" style="width: 78%" prop="phone">
            <span>{{ takeawayInfo.phone }}</span>
          </el-form-item>
        </div>
        
        <el-form-item label="是否可用：" style="width: 78%" prop="name">
            <span>{{ takeawayInfo.isUse == 1 ? '可用' : '已禁用' }}</span>
          </el-form-item>

        <div style="display: flex;">
          <el-form-item label="所在城市：" prop="city" style="width: 78%">
            <span
              >{{ takeawayInfo.province }}{{ takeawayInfo.city }}{{ takeawayInfo.area }}</span
            >
          </el-form-item>

          
        </div>
        <el-form-item label="详细地址：" style="width: 78%" prop="address">
            <span>{{ takeawayInfo.address }}</span>
          </el-form-item>

        <div style="display: flex;">
      <el-form-item
            label="是否分账"
            prop="divideAccounts"
            style="width: 78%"
            label-width="100px"
          >
            <span>{{ takeawayInfo.divideAccounts == 1 ? "是" : "否" }}</span>
          </el-form-item>
          <el-form-item
            v-if="takeawayInfo.divideAccounts == 1"
            label="分账比例"
            style="width: 78%"
            label-width="100px"
            prop="totalCount"
          >
            <span>{{takeawayInfo.proportion}}%</span>
          </el-form-item>
           </div>
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
      <el-table-column prop="name" label="姓名" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" align="center">
      </el-table-column>
      <el-table-column label="是否可用" align="center">
        <template slot-scope="scope">
          {{scope.row.isUse == 1 ? '可用' : '已禁用'}}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openInfo(scope.row)"
            >查看详情</el-button
          >
          <el-button type="primary" size="mini" @click="openEditShop(scope.row)"
            >编辑</el-button
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
import takeawayApi from "@/api/admins/takeaway";
import VDistpicker from "v-distpicker"; //引入省市区三级联动插件
import { getCascaderOptions } from "@/district/provinceCity";
export default {
  components: {
    VDistpicker, // 注册省市区三级联动组件
  },
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
    //     } else if (value !== this.takeawayInfo.password) {
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
      if (value != '' && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的密码"));
      }
    };
    var validateSurePassword = (rule, value, callback) => {

        if (this.takeawayInfo.confirmPsw != this.takeawayInfo.password) {
          callback(new Error("确认密码和密码不一致"));
        } else {
          callback();
        }
      
    };

    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的姓名"));
      }
    };
    const validateAddress = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的详细地址"));
      }
    };
    const validateLat = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的纬度"));
      }
    };
    const validateLng = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的经度"));
      }
    };
    const validateShopId = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的店铺id"));
      }
    };
    const validateRedirectUri = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的回调地址"));
      }
    };

    const validateUsername = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的登陆账号"));
      }
    };
    const validateequipmentId = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的授权设备型号"));
      }
    };

    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      takeawayQuery: {},
      brandList: [],
      takeawayInfo: {
        isUse: 1,
      },
      //所在地区组件
      districtOptions: [],
      shopList: [],
      dialogInfo: false,
      listLoading: false,
      dialogAgent: false,
      divideList: [
        {
          value: 1,
          label: "是",
        },
        {
          value: 2,
          label: "否",
        },
      ],
      rules: {
        name:[
          { required: true, trigger: "blur", message: "请输入姓名" },
          { validator: validateName, trigger: "blur" },
        ],
        phone: [
          { required: true, trigger: "blur", message: "请输入联系电话" },
          { validator: validatePhone, trigger: "blur" },
        ],
        proportion: [
          { required: true, trigger: "blur", message: "请输入分账比例" },
        ],
        district: [
          { required: true, message: "请选择所在城市", trigger: "blur" },
        ],
        address: [
          { required: true, message: "请输入详细地址", trigger: "blur" },
          { validator: validateAddress, trigger: "blur" },
        ],
        divideAccounts: [
          { required: true, message: "请选择是否分账", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.districtOptions = getCascaderOptions();
  },
  methods: {
    changeInput(e) {
      let value = e.replace(/^(0+)|[^\d]+/g, "");

      //如果输入框数值大于360，取余计算
      if (value > 30) {
        this.takeawayInfo.proportion = 30;
      } else {
        this.takeawayInfo.proportion = value;
      }
      //如果用户将input输入框内容清除且没有输入其他值，则输入框内容为0
      if (value === "") {
        this.takeawayInfo.proportion = 1;
      }
    },


    // onChangeProvince(data) {
    //   this.takeawayInfo.province = data.value;
    // },
    // onChangeCity(data) {
    //   this.takeawayInfo.city = data.value;
    // },
    // onChangeArea(data) {
    //   this.takeawayInfo.area = data.value;
    // },
    onChange(data) {
      this.takeawayInfo.province = data[0];
      this.takeawayInfo.city = data[1];
      this.takeawayInfo.area = data[2];
    },


    getList(page = 1) {
      this.page = page;
      takeawayApi
        .getList(this.page, this.limit, this.takeawayQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },
    // getShopList() {
    //   takeawayApi.getShopList().then((response) => {
    //     this.shopList = response.data.list;
    //   });
    // },

    resetData() {
      this.takeawayQuery = {};
      this.getList();
    },
    openAgentsDialog() {
      this.dialogAgent = true;
      this.takeawayInfo = {
        isUse: 1,
      }
    },
    // 添加
    addAgent() {
      takeawayApi
        .insertData(this.takeawayInfo)
        .then((response) => {
          if (response.message == "该骑手已添加") {
            this.$message.error(response.message);
          } else {
            this.dialogAgent = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getList();
          }
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },

    openEditShop(data) {
      this.dialogAgent = true;
      takeawayApi.getInfo(data.id).then((response) => {
        this.takeawayInfo = response.data.info;
        this.takeawayInfo.district = [
          response.data.info.province,
          response.data.info.city,
          response.data.info.area,
        ];
        // this.takeawayInfo.district[0] = response.data.info.province;
        // this.takeawayInfo.district[1] = response.data.info.city;
        // this.takeawayInfo.district[2] = response.data.info.area;
 
      });
    },
    // 修改信息
    updateAgent() {
      takeawayApi
        .updateData(this.takeawayInfo.id, this.takeawayInfo)
        .then((response) => {
          if (response.message == "该骑手已添加") {
            this.$message.error(response.message);
          } else {
            this.dialogAgent = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getList();
          }
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },


    // 查看管理员弹窗数据回显
    openInfo(data) {
      this.dialogInfo = true;
      takeawayApi.getInfo(data.id).then((response) => {
        this.takeawayInfo = response.data.info;


      });
    },

    closeShop(takeawayInfo) {
      this.$refs[takeawayInfo].clearValidate();
      this.takeawayInfo = {
        isUse: 1,
      };
      this.dialogAgent = false;
    },
    // 保存并下一步
    saveOrUpdate(takeawayInfo) {

      // 判断添加还是修改
      if (!this.takeawayInfo.id) {
        this.$refs[takeawayInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[takeawayInfo].clearValidate();
            this.addAgent();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[takeawayInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateAgent();
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
.el-upload{
  text-align: left;
}
</style>