<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="商户">
        <el-select
          clearable
          filterable
          v-model="orderQuery.shopId"
          placeholder="请选择"
        >
          <el-option
            v-for="typePro in shopList"
            :key="typePro.id"
            :label="typePro.name"
            :value="typePro.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="订单编号">
        <el-input
          v-model="orderQuery.orderNum"
          placeholder="订单编号"
        ></el-input>
      </el-form-item>
      <el-form-item label="订单来源">
        <el-select
          clearable
          filterable
          v-model="orderQuery.platformType"
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
                    v-model="orderQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
                    v-model="orderQuery.end"
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
    <div style="padding-bottom: 20px;">
      <span style="color:red;margin-right: 20px;">总金额：￥{{price}}</span>
      <span style="color:red">总数量：{{sum}} 单</span>
    </div>

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
        prop="orderNum"
        label="订单编号"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        label="订单来源"
        width="width"
        align="center"
      >
      <template slot-scope="scope">
         {{scope.row.platformType == 1 ? '小程序' : scope.row.platformType == 2 ? '饿了么' : scope.row.platformType == 3 ? '美团' : '竖屏'}}
        </template>
      </el-table-column>
    
      
      <el-table-column
        prop="totalPrice"
        label="总支付金额"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="totalCount"
        label="总数量"
        width="width"
        align="center"
      >
      </el-table-column>
      <!-- <el-table-column label="支付类型" width="width" align="center">
        <template slot-scope="scope">
          {{
            scope.row.payType == 1
              ? "线上"
              : scope.row.payType == 2
              ? "扫码"
              : "现金"
          }}
        </template>
      </el-table-column> -->
      <el-table-column label="订单类型" width="width" align="center">
        <template slot-scope="scope">
          {{
            scope.row.equipmentType == 1
              ? "外卖单"
              : '自助单'
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

      <el-table-column label="操作" width="width" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="openInfo(scope.row.id)"
            >查看详情</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 订单表单 -->
    <el-dialog :visible.sync="dialogInfo" title="订单详情">
      <el-form :model="ordersInfo" label-width="60px">
        <div
          style="
            color: #409eff;
            font-size: 20px;
            height: 40px;
            display: flex;
            align-items: center;
          "
        >
          订单信息
          <div
            style="
              margin-left: 10px;
              width: 80%;
              height: 2px;
              background: #409eff;
            "
          ></div>
        </div>
        <el-form-item label="订单号：" style="width: 45%" label-width="120px">
          <span>{{ ordersInfo.orderNum }}</span>
        </el-form-item>
        <el-form-item label="下单时间：" style="width: 45%" label-width="120px">
          <span>{{ ordersInfo.createTime }}</span>
        </el-form-item>
        <el-form-item label="支付合计：" style="width: 45%" label-width="120px">
          <span>￥{{ ordersInfo.totalPrice }}</span>
        </el-form-item>
        <el-form-item
        label="订单来源"
        style="width: 45%" label-width="120px"
      >
      <span>{{ordersInfo.platformType == 1 ? '茶饮小程序' : ordersInfo.platformType == 2 ? '饿了么' : ordersInfo.platformType == 3 ? '美团' : '竖屏'}}</span>
         
      </el-form-item>
        <!-- <el-form-item label="来源：" style="width: 45%" label-width="120px">
          <span>{{ ordersInfo.payType == 1 ? "小程序" : "收银POS" }}</span>
        </el-form-item> -->
        <el-form-item label="订单类型" style="width: 45%" label-width="120px">
          <span>{{ ordersInfo.equipmentType == 1 ? "外卖单" : "自助单" }}</span>
        </el-form-item>
        <!-- <el-form-item label="开单人：" style="width: 45%" label-width="120px">
          <span>{{
            ordersInfo.payType == 1 ? "茶饮小程序" : ordersInfo.adminName
          }}</span>
        </el-form-item> -->
        <el-form-item label="客户备注：" style="width: 45%" label-width="120px">
          <span>{{ ordersInfo.notes }}</span>
        </el-form-item>
        <div
          style="
            color: #409eff;
            font-size: 20px;
            height: 40px;
            display: flex;
            align-items: center;
          "
        >
          饮品信息
          <div
            style="
              margin-left: 10px;
              width: 80%;
              height: 2px;
              background: #409eff;
            "
          ></div>
        </div>

          <el-table :data="ordersInfo.params" style="width: 100%">
            <el-table-column label="名称"
              ><template slot-scope="props">
                <span
                  >{{ props.row.name
                  }}{{
                    props.row.specifications.indexOf("杯") > -1
                      ? "（" + props.row.specifications.substring(0, 2) + "）"
                      : ""
                  }}</span
                >
              </template>
            </el-table-column>
            <el-table-column label="做法"
              ><template slot-scope="props">
                <span>{{
                  props.row.specifications.indexOf("杯") > -1
                    ? props.row.specifications.substring(3)
                    : props.row.specifications
                }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数量"
              ><template slot-scope="props">
                <span>x{{ props.row.number }}</span>
              </template>
            </el-table-column>
            <el-table-column label="小计"
              ><template slot-scope="props">
                <span
                  >￥{{ props.row.price }}</span
                >
              </template>
            </el-table-column>
          </el-table>
    
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogInfo = false">取 消</el-button>
        <el-button type="primary" @click="dialogInfo = false">确 定</el-button>
      </div>
    </el-dialog>
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
import orderApi from "@/api/admins/orderInfo";
// 引入moment.js
import moment from "moment";
import VDistpicker from "v-distpicker"; //引入省市区三级联动插件
export default {
  components: {
    VDistpicker, // 注册省市区三级联动组件
  },
  //
  data() {
    //定义变量和初始值
    return {
      list: [],
      page: 1,
      limit: 10,
      total: 0,
      orderQuery: {},
      listLoading: false,
      price: 0,
      sum: 0,
      ordersInfo: {},
      shopList: [],
      typeList: [
        {
          value: 1,
          label: "茶饮小程序",
        },
        {
          value: 2,
          label: "饿了么",
        },
        {
          value: 3,
          label: "美团",
        },
        {
          value: 4,
          label: "竖屏",
        },
      ],
      payStatus: [
        {
          value: 1,
          label: "待支付",
        },
        {
          value: 2,
          label: "已支付",
        },
      ],
      dialogInfo: false,
      dialogSubCate: false,
      id: null,
    };
  },
  created() {
    this.getList();
    this.getShopList();
  },
  methods: {
    // 清空方法
    clearData() {
      this.orderQuery = {};
      this.getList();
    },
    getShopList() {
      orderApi
        .getAllShops()
        .then((response) => {
          this.shopList = response.data.list;
        
        });
    },

    getList(page = 1) {
      this.page = page;
      orderApi
        .ordersList(this.page, this.limit, this.orderQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
          this.price = response.data.price;
          this.sum = response.data.sum;
        });
    },

    // 详情
    openInfo(id) {
      this.dialogInfo = true;
      orderApi.ordersInfo(id).then((response) => {
        this.ordersInfo = response.data.info;
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
</style>
