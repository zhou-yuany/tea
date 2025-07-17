<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <!-- <el-form-item label="限制条件">
        <el-input v-model="couponQuery.name" placeholder="限制条件"></el-input>
      </el-form-item> -->
      <el-form-item label="平台类型">
        <el-select v-model="couponQuery.platType" placeholder="请选择">
          <el-option
            v-for="item in platList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="发放类型">
        <el-select v-model="couponQuery.type" placeholder="请选择">
          <el-option
            v-for="item in typeList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="发放时间">
        <el-date-picker
          v-model="couponQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          format="yyyy-MM-dd HH:mm"
          value-format="yyyy-MM-dd HH:mm"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="couponQuery.end"
          type="datetime"
          placeholder="选择结束时间"
          format="yyyy-MM-dd HH:mm"
          value-format="yyyy-MM-dd HH:mm"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="getList()" icon="el-icon-search"
          >搜索</el-button
        >
        <el-button type="default" @click="resetData()" icon="el-icon-my-clear"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <el-button
      type="success"
      @click="openCouponDialog()"
      style="float: right; margin-bottom: 10px"
      >添加</el-button
    >

    <!-- 添加和修改 -->
    <el-dialog
      width="40%"
      :visible.sync="dialogCoupon"
      :title="couponInfo.id > 0 ? '修改' : '添加'"
      @close="closeCoupon('couponInfo')"
      center
    >
      <el-form
        :model="couponInfo"
        :rules="rules"
        ref="couponInfo"
        label-width="60px"
      >
      <el-form-item
          label="平台类型"
          style="width: 78%"
          label-width="140px"
          prop="type"
        >
          <el-select v-model="couponInfo.platType" placeholder="请选择" @change="changePlat">
            <el-option
              v-for="item in platList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="couponInfo.platType == 2"
          label="店铺名称"
          style="width: 78%"
          label-width="140px"
          prop="shopId"
        >
          <el-select v-model="couponInfo.shopId" placeholder="请选择">
            <el-option
              v-for="item in shopList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="优惠券类型"
          style="width: 78%"
          label-width="140px"
          prop="type"
        >
          <el-select v-model="couponInfo.type" placeholder="请选择">
            <el-option
              v-for="item in typeList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item
          label="优惠券名称"
          style="width: 78%"
          label-width="140px"
          prop="name"
        >
          <el-input v-model="couponInfo.name" placeholder=" 优惠券名称" />
        </el-form-item>
        <el-form-item
          label="优惠券面值"
          style="width: 78%"
          label-width="140px"
          prop="parValue"
        >
          <el-input
            type="number"
            v-model="couponInfo.parValue"
            onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
            placeholder=" 优惠券面值"
          />
        </el-form-item>
        <el-form-item
          label="活动开始时间"
          prop="startTime"
          style="width: 78%"
          label-width="140px"
        >
          <el-date-picker
            v-model="couponInfo.startTime"
            type="datetime"
            placeholder="选择结束时间"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
          ></el-date-picker>
        </el-form-item>
        <el-form-item
          label="活动结束时间"
          prop="endTime"
          style="width: 78%"
          label-width="140px"
        >
          <el-date-picker
            v-model="couponInfo.endTime"
            type="datetime"
            placeholder="选择结束时间"
            format="yyyy-MM-dd HH:mm"
            value-format="yyyy-MM-dd HH:mm"
          ></el-date-picker>
        </el-form-item>
        <!-- <el-form-item
        v-if="couponInfo.type == 1"
          label="金额限制（满减）"
          style="width: 78%"
          label-width="140px"
          prop="limits"
        >
          <el-input
            type="number"
            v-model.trim="couponInfo.limits"
            placeholder=" 金额限制（满减）"
          />
        </el-form-item> -->
        <el-form-item
                v-if="couponInfo.type != 2"
                label="数量"
                style="width: 78%"
                label-width="140px"
                prop="useCount"
              >
                <el-input
                  min="0"
                  type="number"
                  v-model="couponInfo.count"
                  onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
                  placeholder=" 数量"
                />
              </el-form-item>
        <el-form-item
          label="有效时长（天）"
          style="width: 78%"
          label-width="140px"
          prop="days"
        >
          <el-input
            v-model="couponInfo.days"
            onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')"
            placeholder=" 有效时长（天）"
          />
        </el-form-item>
        <el-form-item
          label="有效时长（月）"
          style="width: 78%"
          label-width="140px"
          prop="months"
        >
          <el-input
            v-model="couponInfo.months"
            onkeyup="this.value= (this.value == 0 && this.value.length > 0) ? 0 : this.value.replace(/^(0+)|[^\d]+/g,'')"
            placeholder=" 有效时长（月）"
          />
        </el-form-item>
        <div style="color: red;margin-left: 8.8rem;">有效时长=月数+天数</div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeCoupon('couponInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('couponInfo')"
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
      <el-table-column prop="name" label="优惠券名称" align="center">
      </el-table-column>
      <el-table-column label="平台类型" align="center">
        <template slot-scope="scope">
          {{ scope.row.platType == 1 ? "通用" : "指定店铺" }}
        </template>
      </el-table-column>
      <el-table-column label="发放类型" align="center">
        <template slot-scope="scope">
          {{ scope.row.type == 1 ? "满减" : "新用户" }}
        </template>
      </el-table-column>
      <el-table-column label="发放时间" align="center">
        <template slot-scope="scope">
          {{ scope.row.startTime }}至{{ scope.row.endTime }}
        </template>
      </el-table-column>
      <el-table-column prop="parValue" label="优惠券面值" align="center">
      </el-table-column>
      <el-table-column prop="count" label="数量" align="center">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="openEditCoupon(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row)"
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
import couponApi from "@/api/admins/coupon";

export default {
  data() {
    const validateName = (rule, value, callback) => {
      if (value.trim()) {
        callback();
      } else {
        callback(new Error("请输入有效的优惠券名称"));
      }
    };

     const validateLimits = (rule, value, callback) => {
      const reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^[0-9]\.[0-9]([0-9])?$)/;
      if (reg.test(value)) {
        callback();
      } else {
        callback(new Error("请输入正确金额格式,可保留两位小数"));
      }
    };
    const validateStartTime = (rule, value, callback) => {
      if (
        Date.parse(new Date(this.couponInfo.endTime)) <=
        Date.parse(new Date(value))
      ) {
        callback(new Error("活动开始时间不能大于等于结束时间"));
        return false;
      } else {
        callback();
      }
    };
    const validateTime = (rule, value, callback) => {
      if (
        Date.parse(new Date(this.couponInfo.startTime)) >=
        Date.parse(new Date(value))
      ) {
        callback(new Error("活动结束时间不能小于等于开始时间"));
        return false;
      } else {
        callback();
      }
    };

    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      couponQuery: {
        name: "",
        type: "",
        begin: "",
        end: "",
      },
      couponInfo: {
        id: "",
        shopId: "",
        name: "",
        type: "",
        parValue: null,
        limits: null,
        days: "",
        months: "",
        startTime: "",
        endTime: "",
      },
      typeList: [
        {
          value: 1,
          label: "满减",
        },
        {
          value: 2,
          label: "新用户",
        },
      ],
      platList: [
        {
          value: 1,
          label: "通用",
        },
        {
          value: 2,
          label: "指定店铺",
        },
      ],
      shopList: [],
      listLoading: false,
      dialogCoupon: false,
      rules: {
        shopId: [
          { required: true, message: "请选择店铺名称", trigger: "blur" },
        ],
        type: [
          { required: true, message: "请选择优惠券类型", trigger: "blur" },
        ],
        name: [
          { required: true, message: "请输入优惠券名称", trigger: "blur" },
          { validator: validateName, trigger: "blur" },
        ],
        parValue: [
          { required: true, message: "请输入优惠券面值", trigger: "blur" },
        ],
        limits: [
          {
            required: true,
            message: "请输入金额限制（满减）",
            trigger: "blur",
          },
          { validator: validateLimits, trigger: "blur" },
        ],
        days: [
          { required: true, message: "请输入有效时长（天）", trigger: "blur" },
        ],
        months: [
          { required: true, message: "请输入有效时长（月）", trigger: "blur" },
        ],
        startTime: [
          { required: true, message: "请选择活动时限", trigger: "blur" },
          { validator: validateStartTime, trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "请选择有活动时限", trigger: "blur" },
          { validator: validateTime, trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getShops();
  },
  methods: {
    changePlat(e){
      if(e == 1){
        this.typeList = [{
          value: 1,
          label: "满减",
        },
        {
          value: 2,
          label: "新用户",
        },
        {
          value: 3,
          label: "完善信息",
        }]
      }else{
        this.typeList = [{
          value: 1,
          label: "满减",
        },
        {
          value: 2,
          label: "新用户",
        }]
      }
    },
    getShops() {
      couponApi.getShops().then((response) => {
        this.shopList = response.data.shopList;
      });
    },

    getList(page = 1) {
      if(this.couponQuery.begin && this.couponQuery.end){
        var timestart = new Date(this.couponQuery.begin).getTime();
        var timeend = new Date(this.couponQuery.end).getTime();
        if(timestart > timeend){
          this.$message({
            type: "warning",
            message: "开始时间不能大于结束时间！",
          });
          return false;
        }
      }
      this.page = page;
      couponApi
        .getList(this.page, this.limit, this.couponQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },

    resetData() {
      this.couponQuery = {};
      this.getList();
    },
    openCouponDialog() {
      this.dialogCoupon = true;
    },
    // 添加
    addCoupon() {
      couponApi
        .addCoupon(this.couponInfo)
        .then((response) => {
  
          this.dialogCoupon = false;
          this.$message({
            type: "success",
            message: "添加成功",
          });
          this.getList();
        })
        .catch((response) => {
          this.$message({
            type: "error",
            message: "添加失败",
          });
        });
    },

    openEditCoupon(data) {
      this.dialogCoupon = true;
      couponApi.getCouponInfo(data.id).then((response) => {
        this.couponInfo = response.data.info;
      });
    },
    // 修改信息
    updateCoupon() {
      couponApi
        .updateCoupon(this.couponInfo)
        .then((response) => {
          this.dialogCoupon = false;
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
        couponApi.deleteCopupon(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getList();
        });
      });
    },

    // 查看管理员弹窗数据回显
    opendetails(data) {
      couponApi.getAdminsInfo(data.id).then((response) => {
        this.couponInfo = response.data.adminsInfo;
      });
    },

    closeCoupon(couponInfo) {
      this.$refs[couponInfo].clearValidate();
      this.couponInfo = {
        id: "",
        shopId: "",
        name: "",
        type: "",
        parValue: null,
        limits: null,
        days: "",
        months: "",
        startTime: "",
        endTime: "",
      };
      this.dialogCoupon = false;
    },
    // 保存并下一步
    saveOrUpdate(couponInfo) {
      // 判断添加还是修改
      if (this.couponInfo.id == "") {
        this.$refs[couponInfo].validate((valid) => {
          if (valid) {
            // 添加
            this.$refs[couponInfo].clearValidate();
            this.addCoupon();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[couponInfo].validate((valid) => {
          if (valid) {
            // 修改
            this.updateCoupon();
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
.el-icon-my-clear {
  background: url("../../assets/clearData.png") no-repeat;
  font-size: 16px;
  background-size: cover;
}
.el-icon-my-clear:before {
  content: "替";
  font-size: 16px;
  visibility: hidden;
}
</style>