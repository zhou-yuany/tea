<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item
          label="账单类型"
        >
          <el-select
            clearable
            filterable
            v-model="withdrawalQuery.type"
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
                    v-model="withdrawalQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
                    v-model="withdrawalQuery.end"
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
      </el-form-item>
      
    </el-form>

    <el-dialog
      width="40%"
      :visible.sync="dialogWithdrawal"
      title="提现审核"
      @close="closeCoupon('withdrawalInfo')"
      center
    >
      <el-form
        :model="withdrawalInfo"
        ref="withdrawalInfo"
        label-width="60px"
      >
        <el-form-item
          label="提现金额"
          style="width: 78%"
          label-width="140px"
          prop="limits"
        >
        <span>{{withdrawalInfo.price}}</span>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="unWithdrawal()">驳回</el-button>
        <el-button type="primary" @click="save()"
          >提现</el-button
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
      <el-table-column label="提现状态" width="width" align="center">
        <template slot-scope="scope">
          {{
            scope.row.type == 1
              ? "提现中"
              : scope.row.type == 2 ? "提现到账" : '提现驳回'
          }}
        </template>
      </el-table-column>
      <el-table-column
        label="金额"
        width="width"
        align="center"
      >
      <template slot-scope="scope">
          ￥{{scope.row.price}}
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
      <el-table-column label="操作" width="200" align="center">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.type == 1"
                type="primary"
                size="mini"
                icon="el-icon-edit"
                class="editbtn white"
                @click="handler(scope.row)"
                >审核</el-button
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
// 引入调用admins.js文件
import withdrawalApi from "@/api/admins/withdraw";
// 引入moment.js
import moment from "moment";
export default {

  //
  data() {

    return {
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      totalPrice: 0,
      balance: 0,
      id: null,
      dialogWithdrawal: false,
      withdrawalQuery: {},
      listLoading: false,
      orderCode: '',
      codeShow: false,
      withdrawalInfo: {},
      timer: null,
      typeList: [
        {
          value: 1,
          label: "提现中",
        },
        {
          value: 2,
          label: "提现到账",
        },
        {
          value: 3,
          label: "提现驳回",
        },
      ],

    };
  },
  created() {
    this.getList();
    // createSocket2();
    // this.getsocketData = (e) => {
    //   // 创建接收消息函数
    //   let data = e && e.detail.data;
    //   if (data) {
    //     if (data != 'ping' && JSON.parse(data).message == '您有新订单啦') {
    //       this.codeShow = false;
    //       this.getList();
    //     }
    //   }
    // };
    // // 注册监听事件
    // window.addEventListener("onmessageWS2", this.getsocketData);
  },
  methods: {
    closeCoupon() {
      this.withdrawalInfo = {};
      this.dialogWithdrawal = false;
    },
    unWithdrawal(){
      withdrawalApi
        .unWithdrawalShop(this.withdrawalInfo.id)
        .then((response) => {
          this.withdrawalInfo = {};
      this.dialogWithdrawal = false;
          this.getList();
        });
    },
    save(){
      withdrawalApi
        .toWithdrawal(this.withdrawalInfo.id)
        .then((response) => {
          this.withdrawalInfo = {};
      this.dialogWithdrawal = false;
          this.getList();
        });
 
  
    },
    handler(data) {
      this.withdrawalInfo = data;
      this.dialogWithdrawal = true
    },
    // 清空方法
    clearData() {
      this.withdrawalQuery = {};
      this.getList();
    },

    getList(page = 1) {
      this.page = page;
      withdrawalApi
        .getList(this.page, this.limit, this.withdrawalQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;

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
.saomac{
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  margin: auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 99;
  cursor: pointer;
}
.saomac img{
  width: 200px;
  height: 200px;
  position: absolute;
  top: 50%;
  left: 50%;
  margin-top: -100px;
  margin-left: -100px;
}
</style>
