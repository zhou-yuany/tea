<template>
  <div class="app-container">
      <div class="tabbarh">
        <el-button
        @click="changeBtn(1)"
        :style="{
          color: type == 1 ? '#ffffff' : '#434C42',
          background: type == 1 ? '#D2AD52' : 'transparent',
        }"
        >合伙人</el-button
      >
      <div class="line"></div>
      <el-button
        @click="changeBtn(2)"
        :style="{
          color: type == 2 ? '#ffffff' : '#434C42',
          background: type == 2 ? '#D2AD52' : 'transparent',
        }"
        >合伙人分账表</el-button
      >
      </div>
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="合伙人类型">
        <el-select
            clearable
            filterable
            v-model="partnerQuery.cateId"
            placeholder="请选择">
            <el-option
                v-for="typePro in cateList"
                :key="typePro.value"
                :label="typePro.label"
                :value="typePro.value"/>
    </el-select>
      </el-form-item>
      <el-form-item label="时间">
        <el-date-picker
          v-model="couponQuery.begin"
          type="datetime"
          placeholder="请选择"
          format="yyyy-MM-dd HH:mm"
          value-format="yyyy-MM-dd HH:mm"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >查询</el-button
        >
        <el-button type="default" @click="clearData()">清空</el-button>
      </el-form-item>
      <el-button
        type="success"
        @click="addData()"
        style="float: right; margin-bottom: 10px"
        >添加</el-button
      >
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
      <el-table-column
        prop="cateName"
        label="所属店铺"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="cateName"
        label="合伙人类型"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="subName"
        label="姓名"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="subName"
        label="电话"
        width="width"
        align="center"
      >
      </el-table-column>
      <el-table-column
        v-if="type == 1"
        prop="createTime"
        label="时间"
        width="width"
        align="center"
        :formatter="dateFormat"
      >
      </el-table-column>
      <el-table-column
        prop="subName"
        label="月份"
        width="width"
        align="center"
        v-if="type == 2"
      >
      </el-table-column>
      <el-table-column
        prop="subName"
        label="分账情况"
        width="width"
        align="center"
        v-if="type == 2"
      >
      </el-table-column>

      <el-table-column v-if="type == 1" label="操作" width="width" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="openEdit(scope.row)"
            >查看合同</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <!-- 合同表单 -->
    <el-dialog :visible.sync="dialogContract" title="合同详情">
      <el-form
        :model="contract"
        :rules="rules"
        ref="contract"
        label-width="60px"
      >
        <el-form-item
          label="一级分类名称"
          style="width: 45%"
          label-width="120px"
          prop="cateName"
        >
          <el-input v-model="contract.cateName" placeholder="一级分类名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogContract = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('contract')"
          >确 定</el-button
        >
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
import contractApi from "@/api/admins/contract";
import subCateApi from "@/api/admins/subCate";
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
      contractQuery: {}, 
      partnerQuery: {},
      listLoading: false,
      info: {},
      nodes: [],
      contract: {},
      cateList: [],
      dialogContract: false,
      dialogSubCate: false,
      subcate: {},
      id: null,
      project: {},
      type: 1,
      rules: {
        cateName: [
          { required: true, message: "请输入一级分类名称！", trigger: "blur" },
        ],
      },
      subcateRules: {
        cateId: [
          { required: true, message: "请选择一级分类！", trigger: "blur" },
        ],
        subName: [
          { required: true, message: "请输入二级分类名称！", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
     // 清空方法
  clearData() {
    
    // 表单输入项数据清空
    if(this.type == 1){
      this.contractQuery = {};
    }else{
      this.partnerQuery = {};
    }
    this.getList();
  },
    // 获取一级分类
    getCateList(){
      subCateApi
        .getcontract()
        .then((response) => {
          this.cateList = response.data.contract.map((item) => {
            return {
              id: item.id,
              value: item.id,
              label: item.cateName,
            };
          })
        });
    },
    changeBtn(value) {
      this.type = value;
      this.getList()
    },
    getList(page = 1) {
      this.page = page;
      if(this.type == 1){
        contractApi
        .getcontractList(this.page, this.limit, this.contractQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
      }else{
        subCateApi
        .getSubcateList(this.page, this.limit, this.partnerQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
          this.getCateList()
        });
      }
      
    },
    // 弹出添加页面
    addData() {
      if(this.type == 1){
        this.dialogContract = true;
      this.contract = {};
      this.$refs.contract.clearValidate();
      }else{
        this.dialogSubCate = true;
      this.subcate = {};
      this.$refs.subcate.clearValidate();
      }
      
    },
    // 保存
    saveOrUpdate(contract) {
      if (!this.contract.id) {
        this.$refs[contract].validate((valid) => {
          if (valid) {
            this.$refs[contract].clearValidate();

              this.addcontract();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[contract].validate((valid) => {
          if (valid) {
              this.updatecontract();
            
          } else {
            return false;
          }
        });
      }
    },
    // 添加
    addcontract() {
      contractApi
        .addcontract(this.contract)
        .then((response) => {
          this.$message({
            type: "success",
            message: "添加成功",
          });
          this.dialogContract = false;
          this.getList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "添加失败",
          });
        });
    },
    // 修改
    openEdit(data) {
      if(this.type == 1){
        //   弹窗
      this.dialogContract = true;
        contractApi.getInfo(data.id).then((response) => {
        this.contract = response.data.info;
      });
      }else{
        //   弹窗
      this.dialogSubCate = true;
        subCateApi.getInfo(data.id).then((response) => {
        this.subcate = response.data.info;
      });
      }
    },
    // 修改信息
    updatecontract() {
      contractApi
        .updatecontract(this.contract)
        .then((response) => {
          // 关闭弹窗
          this.dialogContract = false;
          // 提示
          this.$message({
            type: "success",
            message: "修改成功",
          });
          // 刷新页面
          this.getList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },
    // 保存二级分类
    submitSubcate(subcate) {
      // 判断添加还是修改
      if (!this.subcate.id) {
        this.$refs[subcate].validate((valid) => {
          if (valid) {
            this.$refs[subcate].clearValidate();
              this.addSubcate();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[subcate].validate((valid) => {
          if (valid) {
              this.updateSubcate();
            
          } else {
            return false;
          }
        });
      }
    },
     // 添加二级分类
    addSubcate() {
      subCateApi
        .addSubcate(this.subcate)
        .then((response) => {
          this.$message({
            type: "success",
            message: "添加成功",
          });
          this.dialogSubCate = false;
          this.getList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "添加失败",
          });
        });
    },
  
    // 修改二级分类
    updateSubcate() {
      subCateApi
        .updateSubcate(this.subcate)
        .then((response) => {
          // 关闭弹窗
          this.dialogSubCate = false;
          // 提示
          this.$message({
            type: "success",
            message: "修改成功",
          });
          // 刷新页面
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
    deleteData(index, row) {
      if(this.type == 1){
        contractApi.getSubs(row.id).then(res =>{
          if(res.data.info == 1){
            this.$alert('该分类下有二级分类，不可删除！如若删除，请先删除该分类下的二级分类', '提示', {
          confirmButtonText: '确定',
        });
          }else{
              this.$confirm(`确认删除${row.cateName}吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        contractApi.delete(row.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.getList();
        });
      });
          }
        })
      
      }else{
        this.$confirm(`确认删除${row.subName}吗?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        subCateApi.delete(row.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.getList();
        });
      });
      }
      
    },

  //表格中修改时间格式
  dateFormat: function (row, column) {
    var date = row[column.property];
    if (date == undefined) {
      return "";
    }
    return moment(date).format("YYYY-MM-DD HH:mm:ss");
  },

  }
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
