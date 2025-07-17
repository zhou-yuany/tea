<template>
  <div class="app-container">
    <!-- 顶部 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="设备类型">
        <el-select clearable filterable v-model="type" placeholder="请选择">
          <el-option
            v-for="typePro in typeList"
            :key="typePro.id"
            :label="typePro.label"
            :value="typePro.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="getEquipmentList()"
          icon="el-icon-search"
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

        <el-table border :data="equipmentList" >
          <el-table-column
            prop="date"
            label="序号"
            :resizable="false"
            type="index"
            width="80"
            align="center"
          ></el-table-column>

          <el-table-column prop="equipmentNo" label="设备编号" align="center">
          </el-table-column>
          <el-table-column label="设备类型" align="center">
            <template slot-scope="scope">
          {{scope.row.type == 1 ? '手动' : '自动'}}
        </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :page-size="limit"
          :total="total12"
          style="padding: 20px 0 30px; text-align: center"
          layout="total, prev, pager, next, jumper"
          @current-change="getEquipmentList"
        >
        </el-pagination>
        <!-- 新建设备 -->
        <el-dialog
          width="60%"
          :visible.sync="isShows12"
          title="添加设备"
          @close="onCloseds12('equipmentInfo')"
          center
        >
          <el-form
            :model="equipmentInfo"
            ref="equipmentInfo"
            :rules="equipmentInfoRules"
            class="formula_form zengao"
          >
            <el-form-item label="设备编号" prop="equipmentNo">
              <el-input
                type="text"
                class="selectk"
                v-model="equipmentInfo.equipmentNo"
              ></el-input>
            </el-form-item>
            <el-form-item label="设备类型" prop="type">
              <el-select
                clearable
                filterable
                v-model="equipmentInfo.type"
                placeholder="请选择"
              >
                <el-option
                  v-for="typePro in typeList"
                  :key="typePro.id"
                  :label="typePro.label"
                  :value="typePro.id"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="onCloseds12('equipmentInfo')">取 消</el-button>
            <el-button type="primary" @click="saveParam12('equipmentInfo')"
              >确 定</el-button
            >
          </div>
        </el-dialog>
      </div>
</template>
  
<script>
import homeApi from "@/api/admins/equipment";

export default {
  name: "Index",
  data() {
    const validateSpace13 = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的设备别名"));
      }
    };
    return {
      page: 1,
      limit: 10,
      total12: 0,
      type: null,
      equipmentInfo: {},
      equipmentList: [],
      typeList: [
        {
          id: 1,
          label: "手动",
        },
        {
          id: 2,
          label: "自动",
        },
      ],
      isShows12: false,
      equipmentInfoRules: {
        equipmentNo: [
          { required: true, message: "请输入设备编号", trigger: "blur" },
          { validator: validateSpace13, trigger: "blur" },
        ],
        type: [{ required: true, message: "请选择设备类型", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getEquipmentList();
  },
  methods: {
    onCloseds12(equipmentInfo) {
      this.$refs[equipmentInfo].clearValidate();
      this.isShows12 = false;
    },
    openDialog(){
      this.equipmentInfo = {};
      this.isShows12 = true;
    },
    resetData() {
      this.type = null;
      this.getEquipmentList();
    },

    // 设备管理列表
    getEquipmentList(page = 1) {
      this.page = page;
      homeApi.getEquipmentList(this.page, this.limit, this.type).then((res) => {
        if (res.code == 20000) {
          this.equipmentList = res.data.rows;
          this.total12 = res.data.total;
        }
      });
    },

    insertEquipment() {
      homeApi
        .insertData(this.equipmentInfo)
        .then((response) => {
          this.isShows12 = false;
          this.$message({
            type: "success",
            message: "添加成功",
          });
          this.getEquipmentList();
        })
    },

    saveParam12(equipmentInfo) {
      this.$refs[equipmentInfo].validate((valid) => {
        if (valid) {
          // 修改
          this.insertEquipment();
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