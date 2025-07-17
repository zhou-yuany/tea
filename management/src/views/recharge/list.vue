<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="账单类型">
        <el-select
          clearable
          filterable
          v-model="accountQuery.type"
          placeholder="请选择"
        >
          <el-option
            v-for="typePro in typeList"
            :key="typePro.value"
            :label="typePro.label"
            :value="typePro.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="accountQuery.begin"
          type="datetime"
          placeholder="请选择日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
          :append-to-body="false"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="accountQuery.end"
          type="datetime"
          placeholder="请选择日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
          :append-to-body="false"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >查询</el-button
        >
        <el-button type="default" @click="clearData()">清空</el-button>
        <el-button
          type="warning"
          :disabled="list.length == 0"
          @click="downloadTo"
          >导出</el-button
        >
      </el-form-item>
    </el-form>

    <div>
      <el-button
        v-if="isAivideAccounts == 1"
        type="success"
        @click="withdrawal()"
        style="float: right; margin-bottom: 10px"
        >提现</el-button
      >
      <el-button
       v-if="isAivideAccounts == 1"
        type="success"
        @click="getCode()"
        style="float: right; margin-bottom: 10px;margin-left: 10px;"
        >充值</el-button
      >
      <span style="color: red">总收入：￥{{ totalPrice }}</span>
      <span v-if="isAivideAccounts == 1" style="color: red">余额：￥{{ balance }}</span>
    </div>
    <!-- 扫码支付层 -->
    <div class="saomac" v-show="codeShow" @click="goGuanbi()">
      <img :src="orderCode" alt="" />
    </div>

    <el-dialog
      width="40%"
      :visible.sync="dialogWithdrawal"
      title="提现"
      @close="closeCoupon('withdrawalInfo')"
      center
    >
      <el-form
        :model="withdrawalInfo"
        :rules="rules"
        ref="withdrawalInfo"
        label-width="60px"
      >
        <el-form-item
          label="提现金额"
          style="width: 78%"
          label-width="140px"
          prop="price"
        >
          <el-input
            type="number"
            v-model.trim="withdrawalInfo.price"
            placeholder=" 提现金额"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeCoupon('withdrawalInfo')">取 消</el-button>
        <el-button type="primary" @click="save('withdrawalInfo')"
          >确 定</el-button
        >
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
      <el-table-column
        prop="serialNum"
        label="订单编号"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column label="账单类型" width="width" align="center">
        <template slot-scope="scope">
          {{ scope.row.type == 1 ? "入账" : "出账" }}
        </template>
      </el-table-column>
      <el-table-column label="金额" width="width" align="center">
        <template slot-scope="scope">
          ￥{{ scope.row.shopDistributionPrice }}
        </template>
      </el-table-column>
      <el-table-column label="平台" align="center">
        <template slot-scope="scope">
          {{
            scope.row.payPlatform == 0
              ? "茶饮小程序"
              : scope.row.payPlatform == 1
              ? "饿了么"
              : scope.row.payPlatform == 2
              ? "美团"
              : scope.row.payPlatform == 3
              ? "线下"
              : "充值"
          }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="width"
        align="center"
        :formatter="dateFormat"
      >
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
// 引入调用admins.js文件
import rechargeApi from "@/api/admins/recharge";
import { createSocket2 } from "../websocket/websocket.js";
// 引入moment.js
import moment from "moment";
export default {
  //
  data() {
    //定义变量和初始值
    const validateLimits = (rule, value, callback) => {
      const reg =
        /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
      if (reg.test(value)) {
        callback();
      } else {
        callback(new Error("请输入正确金额格式,可保留两位小数"));
      }
    };
    return {
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      totalPrice: 0,
      balance: 0,
      id: null,
      isAivideAccounts: null,
      dialogWithdrawal: false,
      accountQuery: {
        type: "",
        begin: "",
        end: "",
      },
      listLoading: false,
      orderCode: "",
      codeShow: false,
      withdrawalInfo: {},
      timer: null,
      typeList: [
        {
          value: 1,
          label: "入账",
        },
        {
          value: 2,
          label: "出账",
        },
      ],
      rules: {
        price: [
          {
            required: true,
            message: "请输入提现金额",
            trigger: "blur",
          },
          { validator: validateLimits, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.isAivideAccounts = JSON.parse(localStorage.getItem("adminInfo")).divideAccounts;
    this.getList();
    createSocket2();
    this.getsocketData = (e) => {
      // 创建接收消息函数
      let data = e && e.detail.data;
      if (data) {
        if (data != "ping" && JSON.parse(data).message == "您有新订单啦") {
          this.codeShow = false;
          this.getList();
        }
      }
    };
    // 注册监听事件
    window.addEventListener("onmessageWS2", this.getsocketData);
  },
  methods: {
    downloadTo() {
      var that = this;
      this.$msgbox
        .confirm("此操作将导出数据，是否继续", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        .then(() => {
          let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
          var accountQuery = that.accountQuery;
          window.location.href = `/tea-management/ordersFlow/download/${shopId}?type=${accountQuery.type}&begin=${accountQuery.begin}&end=${accountQuery.end}`;
        })
        .catch(() => {});
    },
    closeCoupon(withdrawalInfo) {
      this.$refs[withdrawalInfo].clearValidate();
      this.withdrawalInfo = {};
      this.dialogWithdrawal = false;
    },
    save(withdrawalInfo) {
      this.$refs[withdrawalInfo].validate((valid) => {
        if (valid) {
          // 添加
          this.$refs[withdrawalInfo].clearValidate();
          let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
          rechargeApi
            .withdrawalShop(shopId, this.withdrawalInfo.price)
            .then((response) => {
              this.balance = response.data.balance;
              this.dialogWithdrawal = false;
            });
        } else {
          return false;
        }
      });
    },
    withdrawal() {
      this.dialogWithdrawal = true;
    },

    goGuanbi() {
      this.codeShow = false;
    },
    getCode() {
      let that = this;
      const projectTime = new Date(); // 当前中国标准时间
      const Year = projectTime.getFullYear(); // 获取当前年份 支持IE和火狐浏览器.
      const Month = projectTime.getMonth() + 1; // 获取中国区月份
      const Day = projectTime.getDate(); // 获取几号
      var CurrentDate = Year;
      if (Month >= 10) {
        // 判断月份和几号是否大于10或者小于10
        CurrentDate += Month;
      } else {
        CurrentDate += "0" + Month;
      }
      if (Day >= 10) {
        CurrentDate += Day;
      } else {
        CurrentDate += "0" + Day;
      }
      let did = CurrentDate + Math.floor(Math.random() * 10000);

      let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      rechargeApi.getQrCode(shopId, did).then((res) => {
 
        const href = window.URL.createObjectURL(res.data); //转成url格式
        that.orderCode = href; //赋值
        that.codeShow = true;
        // //   // 实现轮询
        //   that.timer = window.setInterval(() => {

        //     setTimeout(this.getShopOrdersBy(), 0);
        // }, 1000);
      });
    },
    // 清空方法
    clearData() {
      this.accountQuery = { type: "", begin: "", end: "" };
      this.getList();
    },

    getList(page = 1) {
      this.page = page;
      let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      rechargeApi
        .getRechargeRecordList(this.page, this.limit, shopId, this.accountQuery)
        .then((response) => {
          let list = response.data.rows;
          let addList = list.filter((item) => item.type == 1);
          let subList = list.filter((item) => item.type == 2);
          let addPrice = addList.reduce((sum, item) => sum + item.price, 0);
          let subPrice = subList.reduce((sum, item) => sum + item.price, 0);
          this.totalPrice =
            addPrice - subPrice > 0
              ? parseFloat(addPrice - subPrice).toFixed(2)
              : 0;
          this.list = response.data.rows;
          this.total = response.data.total;
          this.balance =
            response.data.balance > 0
              ? parseFloat(response.data.balance).toFixed(2)
              : 0;
        });
    },
    //表格中修改时间格式
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date == undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },
  },
};
</script>
<style scoped>
.tabbarh {
  width: 1400px;
  height: 50px;
  margin: auto;
  display: flex;
  justify-content: center;
  align-items: center;
}

.tabbarh button {
  width: 115px;
  height: 39px;
  font-size: 20px;
  border: none;
  border-radius: 6px;
  padding: 0;
  border-top: 0 !important;
  border-left: 0 !important;
  border-right: 0 !important;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
/* 扫码支付层 */
.saomac {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  margin: auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 99;
  cursor: pointer;
}
.saomac img {
  width: 200px;
  height: 200px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -100px;
  margin-left: -100px;
}
</style>
