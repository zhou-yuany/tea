<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="商户">
        <el-select
          clearable
          filterable
          v-model="batchUseQuery.shopId"
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
           <el-form-item label="配料名称">
        <el-input v-model="batchUseQuery.name" placeholder="配料名称"></el-input>
      </el-form-item>
      <el-form-item label="开始时间">
         <el-date-picker
                    v-model="batchUseQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
                    v-model="batchUseQuery.end"
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

    <div>
      <span style="color:red">总消耗：{{totalUse}}ml</span>
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
      <el-table-column prop="batchName" label="配料名称" align="center">
      </el-table-column>
      <el-table-column prop="useCount" label="消耗(ml)" align="center">
      </el-table-column>
      <el-table-column prop="shopName" label="商家" align="center">
      </el-table-column>
      <el-table-column prop="createTime" label="时间" align="center">
      </el-table-column>
      <el-table-column prop="orderNum" label="订单号" align="center">
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
import batchUseApi from "@/api/admins/batchUse";

export default {

  data() {
    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      batchUseQuery: {},
      totalUse: 0,
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

    };
  },
  created() {
    this.getList();
    this.getShopList();
  },
  methods: {
    getShopList() {
      batchUseApi
        .getAllShops()
        .then((response) => {
          this.shopList = response.data.list;
        
        });
    },
    getList(page = 1) {
      if(this.batchUseQuery.begin && this.batchUseQuery.end){
        var timestart = new Date(this.batchUseQuery.begin).getTime();
        var timeend = new Date(this.batchUseQuery.end).getTime();
        if(timestart > timeend){
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.page = page;
      batchUseApi
        .getList(this.page, this.limit, this.batchUseQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
          this.totalUse = response.data.totalUse;
        });
    },


    resetData() {
      this.batchUseQuery = {};
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