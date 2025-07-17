<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="模板名称">
        <el-input
          v-model="templateQuery.name"
          placeholder="模板名称"
        ></el-input>
      </el-form-item>

      <el-form-item label="模板颜色">
        <el-select v-model="templateQuery.colorName" placeholder="请选择">
          <el-option
            v-for="item in colorNameList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >查询</el-button
        >
        <el-button type="default" @click="resetData()">清空</el-button>
      </el-form-item>
    </el-form>
    <el-button
      type="success"
      @click="openShopDialog()"
      style="float: right; margin-bottom: 10px"
      >新增模板</el-button
    >

    <!-- 图片预览 -->
 <el-dialog title="图片预览" :visible.sync="previewVisible" width="50%">
    <img :src="previewPath" alt="" style="width:100%;height:100%" />
 </el-dialog>


    <!-- 添加和修改表单 -->
    <el-dialog
      :visible.sync="dialogShop"
      :title="templateInfo.id ? '修改' : '新增'"
      @close="closeShop('templateInfo')"
      center
    >
      <el-form
        :model="templateInfo"
        :rules="rules"
        ref="templateInfo"
        label-width="120px"
      >
        <div style="display: flex">
          <el-form-item
            label="模板名称"
            style="width: 78%"
            label-width="100px"
            prop="name"
          >
            <el-input v-model="templateInfo.name" placeholder=" 模板名称" />
          </el-form-item>

          <el-form-item
            label="模板颜色"
            style="width: 78%"
            label-width="100px"
            prop="colorName"
          >
            <el-select
              clearable
              filterable
              v-model="templateInfo.colorName"
              placeholder="请选择"
            >
              <el-option
                v-for="typePro in colorNameList"
                :key="typePro.value"
                :label="typePro.label"
                :value="typePro.value"
              />
            </el-select>
          </el-form-item>

          
          
        </div>
        <el-form-item
            label="样式"
            style="width: 78%"
            label-width="100px"
            prop="color"
          >
            <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 6 }" v-model="templateInfo.color" placeholder=" 样式" />
          </el-form-item>
        <div style="color: red;margin: 0 auto 20px;width: 77%;">注意：每个颜色用英文";"进行分隔。--secondColor辅助色--themeColor主色--unThemeColor次辅助色--bgColor-main主渐变色--bgColor-second辅助渐变色</div>
        <div style="display: flex">
            <el-form-item label="展示图集" label-width="100px" prop="pics">
              <div class="up-picture-box">
                <el-upload
                  :action="'/tea-management/template/uploadPics'"
                  list-type="picture-card"
                  :before-upload="handlePictureCardPreview"
                  :on-preview="handlePreview"
                  :on-success="handleTemplateSuccess"
                  :on-remove="handleTemplateRemove"
                  :file-list="picList"
                >
                  <div
                    class="el-upload__tip"
                    slot="tip"
                    style="font-weight: bold"
                  >
                    JPG或PNG格式！照片大小不超过2M
                  </div>
                </el-upload>
              </div>
            </el-form-item>
          </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeShop('templateInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('templateInfo')"
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
      <el-table-column prop="name" label="商户名称" align="center">
      </el-table-column>
      <el-table-column prop="colorName" label="模板颜色" align="center">
        <template slot-scope="scope">
          <el-tag v-for="(item, index) in colorNameList" :key="index" v-show="scope.row.colorName.includes(item.value)">{{item.label}}</el-tag>
          <!-- {{colorNameList.filter(item => item.value == scope.row.colorName).label }} -->
          </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openInfo(scope.row)"
            >编辑</el-button
          >
          <!-- <el-button type="success" size="mini" @click="openColor(scope.row)"
            >配色</el-button
          > -->
          <el-button type="success" size="mini" @click="openPic(scope.row)"
            >配图</el-button
          >
          <el-button type="danger"
            size="mini"
             @click="removeDataById()"
            >删除</el-button
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
import templateApi from "@/api/admins/template";
export default {
  data() {
    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的模板名称"));
      }
    };
    const validateColor = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的十六进制颜色"));
      }
    };
    

    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      templateQuery: {},
      errorImg: "../../static/errorImg.png",
      picList: [],
      templateInfo: {},
      previewPath: '',
      previewVisible: false,
      listLoading: false,
      dialogShop: false,
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
      rules: {
        name: [
          { required: true, message: "请输入模板名称", trigger: "blur" },
          { validator: validateName, trigger: "blur" },
        ],
        colorName: [
          { required: true, message: "请选择模板颜色", trigger: "blur" },
        ],
        color: [
          { required: true, message: "请输入十六进制颜色", trigger: "blur" },
          { validator: validateColor, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handlePreview(file){
      this.previewPath = file.url
      this.previewVisible = true
    },

    // 删除
    removeDataById(data) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        templateApi.deleteData(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getList();
        });
      });
    },
    // 上传协议成功调用方法
    handleTemplateSuccess(res, file) {
      this.templateInfo.pics =
        this.templateInfo.pics == undefined
          ? res.data.url
          : this.templateInfo.pics + "," + res.data.url;
    },
    handleTemplateRemove(res, fileList) {
      if (fileList.length > 0) {
        this.templateInfo.pics = fileList
          .map((item) =>
            item.response
              ? item.response.data.url
              : item.url.replace("/tea-management/image/", "")
          )
          .join(",");
      } else {
        this.templateInfo.pics = null;
      }
    },

    // 上传照片之前调用的方法
    handlePictureCardPreview(file) {
      const isJPG = file.type === "image/jpeg";
      // const isGIF = file.type === 'image/gif';
      const isPNG = file.type === "image/png";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      return (isJPG || isPNG) && isLt2M;
    },

    getList(page = 1) {
      this.page = page;
      templateApi
        .getList(this.page, this.limit, this.templateQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },

    resetData() {
      this.templateQuery = {};
      this.getList();
    },
    openShopDialog() {
      this.dialogShop = true;
      this.templateInfo = {};
      this.picList = [];
    },
    // 添加
    addShop() {
      templateApi
        .insertData(this.templateInfo)
        .then((response) => {
          if (response.message == "该模板颜色已添加") {
            this.$message.error(response.message);
          } else {
            this.dialogShop = false;
            this.$message({
              type: "success",
              message: "新增成功",
            });

            this.getList();
          }
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "新增失败",
          });
        });
    },

    openInfo(data) {
      this.dialogShop = true;
      templateApi.getInfo(data.id).then((response) => {
        this.templateInfo = response.data.info;
        if (response.data.info.pics != null) {
          let picList =
            response.data.info.pics.split(",");
          let newList = [];
          picList.forEach((item, index) => {
            let obj = {};
            obj.url = "/tea-management/image/" + item;
            obj.name = index;
            newList.push(obj);
          });
          this.picList = newList;
        }
      });
    },
    openColor(data){
      this.$router.push({
        path: "/template/colorInfo/" + data.id,
        query: {
          page: this.page,
        },
      });
    },
    openPic(data){
      this.$router.push({
        path: "/template/pictureInfo/" + data.id,
        query: {
          page: this.page,
        },
      });
    },

    // 修改信息
    updateShop() {
      templateApi
        .updateData(this.templateInfo)
        .then((response) => {
          this.dialogShop = false;
          this.$message({
            type: "success",
            message: "修改成功",
          });
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
    removeDataById(data) {
      this.$confirm("此操作将永久删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        templateApi.deleteData(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getList();
        });
      });
    },

    closeShop(templateInfo) {
      this.$refs[templateInfo].clearValidate();
      this.dialogShop = false;
    },

    // 保存并下一步
    saveOrUpdate(templateInfo) {
      // 判断添加还是修改
      if (!this.templateInfo.id) {
        this.$refs[templateInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[templateInfo].clearValidate();
            this.addShop();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[templateInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.$refs[templateInfo].clearValidate();
            this.updateShop();
            
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