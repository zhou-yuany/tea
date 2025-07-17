<template>
  <div class="app-container">
    <!-- 顶部 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="品牌名称">
        <el-input v-model="brandQuery.name" placeholder="品牌名称"></el-input>
      </el-form-item>

      <el-form-item label="联系电话">
        <el-input v-model="brandQuery.phone" placeholder="联系电话"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getBrandList()" icon="el-icon-search"
          >搜索</el-button
        >
        <el-button type="default" @click="resetData()" icon="el-icon-my-clear"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-button
      type="success"
      @click="openDialog()"
      style="float: right; margin-bottom: 10px"
      >新增</el-button
    >
    <!-- 内容 -->

    <el-table border :data="brandList">
      <el-table-column
        prop="date"
        label="序号"
        :resizable="false"
        type="index"
        width="80"
        align="center"
      ></el-table-column>

      <el-table-column prop="name" label="品牌名称" align="center">
      </el-table-column>
      <el-table-column prop="manufacturer" label="厂家名称" align="center">
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" align="center">
      </el-table-column>
      <el-table-column prop="address" label="厂家地址" align="center">
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openEdit(scope.row)"
            >编辑</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :page-size="limit"
      :total="total"
      style="padding: 20px 0 30px; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="getBrandList"
    >
    </el-pagination>
    <!-- 修改/新建品牌 -->
    <el-dialog
      width="60%"
      :visible.sync="isShows"
      :title="brandInfo.id ? '修改品牌' : '添加品牌'"
      @close="onCloseds('brandInfo')"
      center
    >
      <el-form
        :model="brandInfo"
        ref="brandInfo"
        :rules="brandInfoRules"
        class="formula_form zengao"
      >
        <el-form-item label="品牌名称" prop="name">
          <el-input
            type="text"
            class="selectk"
            v-model="brandInfo.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="厂家名称" prop="manufacturer">
          <el-input
            type="text"
            class="selectk"
            v-model="brandInfo.manufacturer"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input class="selectk" v-model="brandInfo.phone"/>
        </el-form-item>
        <el-form-item label="厂家地址" prop="address">
          <el-input
            type="text"
            class="selectk"
            v-model="brandInfo.address"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="onCloseds('brandInfo')">取 消</el-button>
        <el-button type="primary" @click="saveParam('brandInfo')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
  
<script>
import homeApi from "@/api/admins/brand";

export default {
  name: "brand",
  data() {
    const validateSpace1 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的品牌名称"));
      }
    };
    const validateSpace2 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的厂家名称"));
      }
    };

    const validateSpace3 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的厂家地址"));
      }
    };
    const validatePhone = (rule, value, callback) => {
      const regex = /^1[3-9]\d{9}$/;
      if (!regex.test(value)) {
        callback(new Error("请输入正确的手机号"));
      } else {
        callback();
      }
    };
    return {
      page: 1,
      limit: 10,
      total: 0,
      brandQuery: {},
      brandInfo: {},
      brandList: [],
      isShows: false,
      brandInfoRules: {
        name: [
          { required: true, message: "请输入品牌名称", trigger: "blur" },
          { validator: validateSpace1, trigger: "blur" },
        ],
        manufacturer: [
          { required: true, message: "请输入厂家名称", trigger: "blur" },
          { validator: validateSpace2, trigger: "blur" },
        ],
        address: [
          { required: true, message: "请输入厂家地址", trigger: "blur" },
          { validator: validateSpace2, trigger: "blur" },
        ],
        phone: [
          { required: true, message: "请输入联系方式", trigger: "blur" },
          { validator: validatePhone, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getBrandList();
  },
  methods: {
    openEdit(data) {
      this.isShows = true;
      homeApi.getInfo(data.id).then((response) => {
        this.brandInfo = response.data.info;

      });
    },
    onCloseds(brandInfo) {
      this.$refs[brandInfo].clearValidate();
      this.isShows = false;
    },
    openDialog() {
      this.brandInfo = {};
      this.isShows = true;
    },
    resetData() {
      this.brandQuery = {};
      this.getBrandList();
    },

    // 设备管理列表
    getBrandList(page = 1) {
      this.page = page;
      homeApi.getBrandList(this.page, this.limit, this.brandQuery).then((res) => {
        if (res.code == 20000) {
          this.brandList = res.data.rows;
          this.total = res.data.total;
        }
      });
    },

    insertBrand() {
      homeApi.insertData(this.brandInfo).then((response) => {
        this.isShows = false;
        this.$message({
          type: "success",
          message: "添加成功",
        });
        this.getBrandList();
      });
    },

    updateData() {
      homeApi.updateData(this.brandInfo).then((response) => {
        this.isShows = false;
        this.$message({
          type: "success",
          message: "修改成功",
        });
        this.getBrandList();
      });
    },

    saveParam(brandInfo) {
      this.$refs[brandInfo].validate((valid) => {
        if (valid) {
          if(!this.brandInfo.id){
            this.insertBrand();
          }else{
            // 修改
            this.updateData();
          }
          
          
        } else {
          return false;
        }
      });
    },
  },
};
</script>
  
<style scoped>
/* 顶部 */
.formula_head {
  width: 95%;
  height: 50px;
  margin: auto;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ddd;
  margin-bottom: 20px;
}
.tab-tit {
  font-size: 0;
  cursor: pointer;
}
.tab-tit a {
  display: inline-block;
  width: 108px;
  height: 50px;
  line-height: 50px;
  font-size: 14px;
  text-align: center;
  color: #333;
  text-decoration: none;
  margin-right: 20px;
}
.tab-tit .cur {
  color: #0256ff;
  border-bottom: 2px solid rgb(64, 158, 255);
}
.formula_searchk {
  width: 95%;
  margin: auto;
}
/* 内容 */
.formula_one {
  width: 97%;
  margin: auto;
}
.addk {
  width: 96.5%;
  margin: auto;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 19px;
}
.addbtn {
  width: 100px;
  height: 44px;
  border-radius: 4px;
  background: #0256ff;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
  text-align: center;
}
/* 新建配方 */
.formula_form .el-form-item {
  display: flex;
  justify-content: center;
  align-items: center;
}
.zengao {
  padding-top: 20px;
}
.selectk {
  width: 400px;
}
.inputk {
  margin-left: 30px;
}
.neitable {
  width: 650px;
  margin-left: 61px;
}
.xinjian {
  margin-left: 61px;
  margin-top: 22px;
}
button:hover {
  color: #ffffff;
}
</style>