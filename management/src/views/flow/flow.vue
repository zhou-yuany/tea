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
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>

    <span style="color:red;margin-bottom: 20px;">总收入：￥{{banlance}}</span>

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
      <el-table-column prop="serialNum" label="订单编号" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="账单类型" align="center">
        <template slot-scope="scope">
          {{scope.row.type == 1 ? '入账' : '出账'}}
        </template>
      </el-table-column>
      <el-table-column prop="platDistributionPrice" label="分销金额" align="center">
      </el-table-column>
      <el-table-column label="平台" align="center">
        <template slot-scope="scope">
          {{scope.row.payPlatform == 0 ? '茶饮小程序' : scope.row.payPlatform == 1 ? '饿了么' : scope.row.payPlatform == 2 ? '美团' : scope.row.payPlatform == 3 ? '线下' :'充值'}}
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
import accountApi from "@/api/admins/flow";

export default {

  data() {
    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      accountQuery: {},
      brandList: [],
      agentsInfo: {
        shopRangeList: [],
      },
      //所在地区组件
      districtOptions: [],
      shopList: [],
      dialogInfo: false,
      listLoading: false,
      dialogAgent: false,
      banlance: 0,
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

    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList(page = 1) {
      if(this.accountQuery.begin && this.accountQuery.end){
        var timestart = new Date(this.accountQuery.begin).getTime();
        var timeend = new Date(this.accountQuery.end).getTime();
        if(timestart > timeend){
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.page = page;
      accountApi
        .getList(this.page, this.limit, this.accountQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
          this.banlance = response.data.totalPrice;

        });
    },


    resetData() {
      this.accountQuery = {};
      this.getList();
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