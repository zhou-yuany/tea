<template>
  <div class="app-container">
    <el-button type="primary" @click="clickReturn()">返回</el-button>

    <el-form
      :model="colorInfo"
      :rules="rules"
      ref="colorInfo"
      label-width="120px"
    >
      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        基本信息
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>

      <div style="display: flex">
        <el-form-item
          label="模板名称："
          style="width: 78%"
          label-width="100px"
          prop="name"
        >
          <span>{{ colorInfo.name }}</span>
        </el-form-item>

        <el-form-item
          label="模板颜色："
          style="width: 78%"
          label-width="100px"
          prop="indexColor"
        >
          <el-select
            :disabled="true"
            v-model="colorInfo.colorName"
            placeholder="请选择"
          >
            <el-option
              v-for="typePro in colorNameList"
              :key="typePro.value"
              :label="typePro.label"
              :value="typePro.value"
            >
              <span style="display: flex; align-items: center">
                <span
                  :style="{ backgroundColor: typePro.value }"
                  style="
                    width: 15px;
                    height: 15px;
                    margin-right: 5px;
                    border-radius: 50%;
                  "
                ></span>
                {{ typePro.label }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
      </div>

      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        点餐
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>

      <div style="display: flex">
        <el-form-item
          label="分类文字颜色："
          style="width: 78%"
          label-width="100px"
          prop="activity"
        >
          <el-select
            clearable
            filterable
            v-model="colorInfo.activity"
            placeholder="请选择"
          >
            <el-option
              v-for="typePro in activityList"
              :key="typePro.value"
              :label="typePro.value"
              :value="typePro.value"
            >
              <span style="display: flex; align-items: center">
                <span
                  :style="{ backgroundColor: typePro.label }"
                  style="
                    width: 15px;
                    height: 15px;
                    margin-right: 5px;
                    border-radius: 50%;
                  "
                ></span>
                {{ typePro.value }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
      </div>

      <!-- 以下待完成
      <div
        style="
          color: #409eff;
          font-size: 20px;
          height: 40px;
          display: flex;
          align-items: center;
        "
      >
        地址
        <div
          style="
            margin-left: 10px;
            width: 80%;
            height: 2px;
            background: #409eff;
          "
        ></div>
      </div>
      <div style="display: flex">
        <el-form-item
          label="定位文字颜色："
          style="width: 78%"
          label-width="100px"
          prop="positioning"
        >
          <el-select
            clearable
            filterable
            v-model="colorInfo.positioning"
            placeholder="请选择"
          >
            <el-option
              v-for="typePro in positioningList"
              :key="typePro.value"
              :label="typePro.value"
              :value="typePro.value"
            >
              <span style="display: flex; align-items: center">
                <span
                  :style="{ backgroundColor: typePro.label }"
                  style="
                    width: 15px;
                    height: 15px;
                    margin-right: 5px;
                    border-radius: 50%;
                  "
                ></span>
                {{ typePro.value }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
      </div> -->
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeShop('colorInfo')">取 消</el-button>
      <el-button type="primary" @click="saveOrUpdate('colorInfo')"
        >确 定</el-button
      >
    </div>
  </div>
</template>
<script>
import templateApi from "@/api/admins/color";
export default {
  data() {
    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的模板名称"));
      }
    };

    return {
      list: null, // 查询后接口返回集合
      prePage: "",
      errorImg: "../../static/errorImg.png",
      picList: [],
      colorInfo: {},
      listLoading: false,
      dialogShop: false,
      templateId: "",
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
      colorNameList: [
        {
          value: "red",
          label: "红",
        },
        {
          value: "orange",
          label: "橙",
        },
        {
          value: "yellow",
          label: "黄",
        },
        {
          value: "green",
          label: "绿",
        },
        {
          value: "cyan",
          label: "青",
        },
        {
          value: "blue",
          label: "蓝",
        },
        {
          value: "purple",
          label: "紫",
        },
      ],
      activityList: [
        {
          value: "white_bg",
          label: "#ffffff",
        },
        {
          value: "blue_bg",
          label: "#1D8294",
        },
        {
          value: "green_bg",
          label: "#DBF2C4",
        },
        {
          value: "btn_bg",
          label: "#A9D99D",
        },
        {
          value: "red_bg",
          label: "#E03D3C",
        },
        {
          value: "gray_bg",
          label: "#D8D8D8",
        },
        {
          value: "orange_bg",
          label: "#F39D36",
        },
      ],
      positioningList: [
        {
          value: "gray",
          label: "#999999",
        },
        {
          value: "dark_gray",
          label: "#6A6767",
        },
        {
          value: "hui",
          label: "#9FA399",
        },
        {
          value: "dark_hui",
          label: "#7E7E7E",
        },
        {
          value: "shen_gray",
          label: "#666666",
        },
        {
          value: "huise",
          label: "#AAAAAA",
        },
        {
          value: "huisea",
          label: "#D8D8D8",
        },
        {
          value: "white",
          label: "#ffffff",
        },
        {
          value: "blue",
          label: "#0256FF",
        },
        {
          value: "red",
          label: "#E03D3C",
        },
        {
          value: "dark_red",
          label: "#EA0000",
        },
        {
          value: "hongse",
          label: "#FC2221",
        },
        {
          value: "orange",
          label: "#F39D36",
        },
        {
          value: "green",
          label: "#A9D99D",
        },
      ],
      rules: {
        activity: [
          { required: true, message: "请选择分类文字颜色", trigger: "blur" },
        ],
        positioning: [
          { required: true, message: "请选择定位文字颜色", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    if (this.$route.params.id && this.$route.params.id > 0) {
      this.templateId = this.$route.params.id;
      this.getInfo(this.$route.params.id);
    }
    if (this.$route.query.page) {
      this.prePage = this.$route.query.page;
    }
  },
  methods: {
    clickReturn() {
      this.$router.push({
        path: "/template/table",
        query: { page: this.prePage },
      });
    },

    // 添加
    addData() {
      templateApi
        .insertData(this.colorInfo)
        .then((response) => {
          this.$message({
            type: "success",
            message: "新增成功",
          });
          this.$router.push({
            path: "/template/table",
            query: { page: this.prePage },
          });
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },

    getInfo(templateId) {
      templateApi.getInfo(templateId).then((response) => {
        this.colorInfo = response.data.info;
      });
    },

    // 修改信息
    updateData() {
      templateApi
        .updateData(this.colorInfo)
        .then((response) => {
          this.$message({
            type: "success",
            message: "修改成功",
          });
          this.$router.push({
            path: "/template/table",
            query: { page: this.prePage },
          });
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "修改失败",
          });
        });
    },

    closeShop(colorInfo) {
      this.$refs[colorInfo].clearValidate();
      this.$router.push({
        path: "/template/table",
        query: { page: this.prePage },
      });
    },

    // 保存并下一步
    saveOrUpdate(colorInfo) {
      // 判断添加还是修改
      if (!this.colorInfo.id) {
        this.$refs[colorInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[colorInfo].clearValidate();
            this.addData();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[colorInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateData();
            this.$refs[colorInfo].clearValidate();
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
.el-upload {
  text-align: left;
}
</style>