<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="联系电话">
        <el-input v-model="agentsQuery.phone" placeholder="联系电话"></el-input>
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
      :title="agentsInfo.id ? '修改' : '新增'"
      @close="closeShop('agentsInfo')"
      center
    >
      <el-form
        :model="agentsInfo"
        :rules="rules"
        ref="agentsInfo"
        label-width="120px"
      >
        <div style="display: flex;">
        
          <el-form-item
            label="联系电话"
            style="width: 78%"
            label-width="100px"
            prop="phone"
          >
            <el-input v-model="agentsInfo.phone" placeholder=" 联系电话" />
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
            v-model="agentsInfo.district"
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
          <el-input v-model="agentsInfo.address" placeholder=" 详细地址" />
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
            v-model="agentsInfo.divideAccounts"
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
          v-if="agentsInfo.divideAccounts == 1"
          label="分账比例"
          style="width: 78%"
          label-width="100px"
          prop="proportion"
        >
          <el-input
            type="number"
            v-model="agentsInfo.proportion"
            @input="changeInput"
            placeholder=" 分账比例"
          >
          <template slot="append" >
            <span>%</span>
          </template>
          </el-input>
        </el-form-item>
        </div>
        <div style="display: flex;">
          <el-form-item
            label="代理商户"
            style="width: 100%"
            label-width="100px"
            prop="shopRangeList"
          >
            <el-select v-model="agentsInfo.shopRangeList" multiple placeholder="请选择" style="width: 100%;">
              <el-option
                v-for="item in shopList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
          
        </div>
        <el-form-item
                label="是否禁用" style="justify-content: flex-start;margin-left: 10px;"
              >
                <el-switch
                  v-model="agentsInfo.isUse"
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
        <el-button @click="closeShop('agentsInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('agentsInfo')"
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
      <el-form :model="agentsInfo" label-width="120px">
        <div style="display: flex;">
          <el-form-item label="联系电话：" style="width: 78%" prop="phone">
            <span>{{ agentsInfo.phone }}</span>
          </el-form-item>
        </div>
        
        <el-form-item label="是否可用：" style="width: 78%" prop="name">
            <span>{{ agentsInfo.isUse == 1 ? '可用' : '已禁用' }}</span>
          </el-form-item>

        <div style="display: flex;">
          <el-form-item label="所在城市：" prop="city" style="width: 78%">
            <span
              >{{ agentsInfo.province }}{{ agentsInfo.city }}{{ agentsInfo.area }}</span
            >
          </el-form-item>

          
        </div>
        <el-form-item label="详细地址：" style="width: 78%" prop="address">
            <span>{{ agentsInfo.address }}</span>
          </el-form-item>
        <div style="display: flex;">
          <el-form-item label="代理商户：" prop="shopName" style="width: 78%">
          <span>{{ agentsInfo.shopName }}</span>
        </el-form-item>
          
        </div>
        <div style="display: flex;">
      <el-form-item
            label="是否分账"
            prop="divideAccounts"
            style="width: 78%"
            label-width="100px"
          >
            <span>{{ agentsInfo.divideAccounts == 1 ? "是" : "否" }}</span>
          </el-form-item>
          <el-form-item
            v-if="agentsInfo.divideAccounts == 1"
            label="分账比例"
            style="width: 78%"
            label-width="100px"
            prop="totalCount"
          >
            <span>{{agentsInfo.proportion}}%</span>
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
      <el-table-column prop="shopName" label="商户名称" align="center">
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
import agentApi from "@/api/admins/agent";
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
    //     } else if (value !== this.agentsInfo.password) {
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

        if (this.agentsInfo.confirmPsw != this.agentsInfo.password) {
          callback(new Error("确认密码和密码不一致"));
        } else {
          callback();
        }
      
    };

    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != '') {
        callback();
      } else {
        callback(new Error("请输入有效的商户名称"));
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
      agentsQuery: {},
      brandList: [],
      agentsInfo: {
        isUse: 1,
        shopRangeList: [],
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
        shopRangeList: [
          { required: true, message: "请选择代理商户", trigger: "blur" },
        ],
        equipmentId: [
          { required: true, message: "请输入授权设备型号", trigger: "blur" },
          { validator: validateequipmentId, trigger: "blur" },
        ],
        divideAccounts: [
          { required: true, message: "请选择是否分账", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getShopList();
    this.districtOptions = getCascaderOptions();
  },
  methods: {
    changeInput(e) {
      let value = e.replace(/^(0+)|[^\d]+/g, "");

      //如果输入框数值大于360，取余计算
      if (value > 30) {
        this.agentsInfo.proportion = 30;
      } else {
        this.agentsInfo.proportion = value;
      }
      //如果用户将input输入框内容清除且没有输入其他值，则输入框内容为0
      if (value === "") {
        this.agentsInfo.proportion = 1;
      }
    },


    // onChangeProvince(data) {
    //   this.agentsInfo.province = data.value;
    // },
    // onChangeCity(data) {
    //   this.agentsInfo.city = data.value;
    // },
    // onChangeArea(data) {
    //   this.agentsInfo.area = data.value;
    // },
    onChange(data) {
      this.agentsInfo.province = data[0];
      this.agentsInfo.city = data[1];
      this.agentsInfo.area = data[2];
    },


    getList(page = 1) {
      this.page = page;
      agentApi
        .getList(this.page, this.limit, this.agentsQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },
    getShopList() {
      agentApi.getShopList().then((response) => {
        this.shopList = response.data.list;
      });
    },

    resetData() {
      this.agentsQuery = {};
      this.getList();
    },
    openAgentsDialog() {
      this.dialogAgent = true;
      this.agentsInfo = {
        isUse: 1,
        shopRangeList: [],
      };
    },
    // 添加
    addAgent() {
      agentApi
        .insertData(this.agentsInfo)
        .then((response) => {
          if (response.message == "该代理已添加") {
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
      agentApi.getInfo(data.id).then((response) => {
        this.agentsInfo = response.data.info;
        this.agentsInfo.district = [
          response.data.info.province,
          response.data.info.city,
          response.data.info.area,
        ];
        // this.agentsInfo.district[0] = response.data.info.province;
        // this.agentsInfo.district[1] = response.data.info.city;
        // this.agentsInfo.district[2] = response.data.info.area;
        if (this.agentsInfo.shopRangeList == null) {
          this.agentsInfo.shopRangeList = [];
        }
      });
    },
    // 修改信息
    updateAgent() {
      agentApi
        .updateData(this.agentsInfo.id, this.agentsInfo)
        .then((response) => {
          if (response.message == "该代理已添加") {
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
      agentApi.getInfo(data.id).then((response) => {
        this.agentsInfo = response.data.info;
        if (this.agentsInfo.shopRangeList == null) {
          this.agentsInfo.shopRangeList = [];
        }

      });
    },

    closeShop(agentsInfo) {
      this.$refs[agentsInfo].clearValidate();
      this.agentsInfo = {
        isUse: 1,
        shopRangeList: [],
      };
      this.dialogAgent = false;
    },
    // 保存并下一步
    saveOrUpdate(agentsInfo) {

      // 判断添加还是修改
      if (!this.agentsInfo.id) {
        this.$refs[agentsInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[agentsInfo].clearValidate();
            this.addAgent();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[agentsInfo].validate((valid) => {
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