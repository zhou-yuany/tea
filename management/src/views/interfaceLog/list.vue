<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="开始时间">
         <el-date-picker
                    v-model="interfaceLogQuery.begin"
                    type="datetime"
                    placeholder="请选择日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    default-time="00:00:00"
                    :append-to-body="false"
                  ></el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
                    v-model="interfaceLogQuery.end"
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
      <el-table-column prop="title" label="标题" align="center">
      </el-table-column>
      <el-table-column prop="content" label="操作内容" align="center">
      </el-table-column>
      <el-table-column prop="shopName" label="商家名称" align="center">
      </el-table-column>
      <el-table-column label="服务器状态" align="center">
        <template slot-scope="scope">
          {{scope.row.typeStatus == 1 ? '异常' : '正常'}}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" align="center">
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
import interfaceLogApi from "@/api/admins/interfaceLog";

export default {

  data() {
    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      interfaceLogQuery: {},
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
  },
  methods: {
    getList(page = 1) {
      if(this.interfaceLogQuery.begin && this.interfaceLogQuery.end){
        var timestart = new Date(this.interfaceLogQuery.begin).getTime();
        var timeend = new Date(this.interfaceLogQuery.end).getTime();
        if(timestart > timeend){
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.page = page;
      var shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
      interfaceLogApi
        .getList(this.page, this.limit, shopId, this.interfaceLogQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;

        });
    },


    resetData() {
      this.interfaceLogQuery = {};
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