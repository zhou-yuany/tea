<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="商户名称">
        <el-input v-model="shopQuery.name" placeholder="商户名称"></el-input>
      </el-form-item>

      <el-form-item label="联系电话">
        <el-input v-model="shopQuery.phone" placeholder="联系电话"></el-input>
      </el-form-item>

      <!-- <el-form-item label="所属品牌">
        <el-input v-model="shopQuery.brandId" placeholder="所属品牌"></el-input>
      </el-form-item> -->

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
      >新增商户</el-button
    >

    <!-- 添加和修改表单 -->
    <el-dialog
      :visible.sync="dialogShop"
      :title="shopInfo.id ? '修改' : '新增'"
      @close="closeShop('shopInfo')"
      center
    >
      <el-form
        :model="shopInfo"
        :rules="rules"
        ref="shopInfo"
        label-width="120px"
      >
        <div style="display: flex">
          <el-form-item
            label="商户名称"
            style="width: 78%"
            label-width="100px"
            prop="name"
          >
            <el-input v-model="shopInfo.name" placeholder=" 商户名称" />
          </el-form-item>

          <el-form-item
            label="联系电话"
            style="width: 78%"
            label-width="100px"
            prop="phone"
          >
            <el-input v-model="shopInfo.phone" placeholder=" 联系电话" />
          </el-form-item>
        </div>
        <!-- <div style="display: flex">
          <el-form-item
            label="所在城市"
            prop="district"
            style="width: 78%"
            label-width="100px"
          >
            <el-cascader
              v-model="shopInfo.district"
              placeholder="请选择"
              :options="districtOptions"
              clearable
              filterable
              style="width: 100%"
              @change="onChange"
            ></el-cascader>
          </el-form-item>

          <el-form-item
            label="详细地址"
            style="width: 78%"
            label-width="100px"
            prop="address"
          >
            <el-input v-model="shopInfo.address" placeholder=" 详细地址" />
          </el-form-item>
        </div> -->
        <!-- <div style="display: flex"> -->
        <!-- <el-form-item
            label="纬度"
            style="width: 78%"
            label-width="100px"
            prop="lat"
          >
            <el-input v-model="shopInfo.lat" placeholder=" 纬度" />
          </el-form-item>
          <el-form-item
            label="经度"
            style="width: 78%"
            label-width="100px"
            prop="lng"
          >
            <el-input v-model="shopInfo.lng" placeholder=" 经度" />
          </el-form-item> -->
        <el-form-item
          label="详细地址"
          label-width="100px"
          prop="address1"
          clearable
        >
          <div style="width: 100%">
            <div class="xqk">
              <span>{{ shopInfo.address }}</span>
            </div>
            <el-input
              placeholder="请选择地址"
              v-model="searchWord"
              @change="openMap()"
            >
              <el-button
                slot="append"
                icon="el-icon-location"
                @click="openMap()"
              ></el-button>
            </el-input>
            <TMap ref="ms" @on-select="selectAddress" />
          </div>
        </el-form-item>
        <!-- </div> -->
        <div style="display: flex">
          <el-form-item
            label="是否分账"
            prop="divideAccounts"
            style="width: 78%"
            label-width="100px"
          >
            <el-select
              clearable
              filterable
              v-model="shopInfo.divideAccounts"
              placeholder="请选择"
            >
              <el-option
                v-for="typePro in divideList"
                :key="typePro.value"
                :label="typePro.label"
                :value="typePro.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="shopInfo.divideAccounts == 1"
            label="分账比例（百分制）"
            style="width: 78%"
            label-width="140px"
            prop="totalCount"
          >
            <el-input
              type="number"
              v-model="shopInfo.proportion"
              @input="changeInput"
              placeholder=" 分账比例"
            />
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item
            label="所属品牌"
            prop="brandId"
            style="width: 78%"
            label-width="100px"
          >
            <el-select
              clearable
              filterable
              v-model="shopInfo.brandId"
              placeholder="请选择"
            >
              <el-option
                v-for="brandPro in brandList"
                :key="brandPro.id"
                :label="brandPro.name"
                :value="brandPro.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            label="所属平台"
            prop="platformType"
            style="width: 78%"
            label-width="100px"
          >
            <el-select
              clearable
              filterable
              multiple
              v-model="shopInfo.platformType"
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
        </div>
        <div style="display: flex">
          <el-form-item
            label="经营商品"
            style="width: 100%"
            label-width="100px"
            prop="goodRangeList"
          >
            <el-select
              v-model="shopInfo.goodRangeList"
              multiple
              placeholder="请选择"
              style="width: 100%"
            >
              <el-option
                v-for="item in goods"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item
            label="所属行业"
            style="width: 78%"
            label-width="100px"
            prop="sector"
          >
            <el-input v-model="shopInfo.sector" placeholder=" 所属行业" />
          </el-form-item>
          <el-form-item
            label="月营业额（元）"
            prop="turnoverM"
            style="justify-content: flex-start"
          >
            <el-input
              min="0"
              type="number"
              v-model.trim="shopInfo.turnoverM"
              placeholder=" 月营业额（元）"
            />
          </el-form-item>
        </div>
        <el-form-item label="商户logo" prop="logo">
          <el-upload
            accept=".png,.jpg,.jpeg"
            class="avatar-uploader"
            :action="'/tea-management/shop/upload'"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img
              v-if="shopInfo.logo"
              :src="'/tea-management/image/' + shopInfo.logo"
              class="avatar"
              style="width: 30%; height: 30%"
            />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div
              class="el-upload__tip"
              slot="tip"
              style="font-weight: bold; color: red"
            >
              上传图片只能是 JPG、JPEG、PNG 格式! 且图片大小不能超过 2MB!
            </div>
          </el-upload>
        </el-form-item>

        <el-form-item label="合同" label-width="100px" prop="agreement">
          <div class="up-picture-box">
            <el-upload
              :action="'/tea-management/shop/uploadAgreement'"
              list-type="picture-card"
              :on-preview="handlePictureCardPreview"
              :on-success="handleAgreementSuccess"
              :on-remove="handleAgreementRemove"
              :file-list="agreementList"
            >
              <div class="el-upload__tip" slot="tip" style="font-weight: bold">
                JPG或PNG格式！照片大小不超过2M
              </div>
            </el-upload>
          </div>
        </el-form-item>
        <el-form-item
            label="模板颜色"
            prop="templateId"
            style="width: 78%"
            label-width="100px"
          >
            <el-select
              clearable
              filterable
              v-model="shopInfo.templateId"
              placeholder="请选择"
            >
              <el-option
                v-for="typePro in templateList"
                :key="typePro.templateId"
                :label="'模板：' + typePro.name + '，颜色：' +  colorNameList.filter(item => item.value == typePro.colorName)[0].label"
                :value="typePro.templateId"
              />
            </el-select>
          </el-form-item>
        <el-form-item
            label="使用设备"
            prop="isUseDevice"
            style="width: 78%"
            label-width="100px"
          >
            <el-select
              clearable
              filterable
              v-model="shopInfo.isUseDevice"
              placeholder="请选择"
            >
              <el-option
                v-for="typePro in divideList"
                :key="typePro.value"
                :label="typePro.label"
                :value="typePro.value"
              />
            </el-select>
          </el-form-item>
        <el-form-item
          v-if="shopInfo.isUseDevice == 1"
          label="授权设备型号"
          style="width: 100%"
          label-width="120px"
          prop="equipmentIdList"
          ><el-select
            v-model="shopInfo.equipmentIdList"
            multiple
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in equipmentList"
              :key="item.id"
              :label="item.equipmentNo"
              :value="item.equipmentNo"
            >
            </el-option>
          </el-select>
          <!-- <el-input
            type="text"
            v-model="shopInfo.equipmentId"
            placeholder=" 授权设备型号 "
          >
            <template slot="append"
              >多个设备型号以英文逗号隔开! 例如：A00000001,A00000002
            </template> -->
          <!-- </el-input> -->
        </el-form-item>
        <div style="display: flex">
          <el-form-item
            label="登录帐号"
            style="width: 78%"
            label-width="100px"
            prop="username"
          >
            <el-input v-model="shopInfo.username" placeholder=" 登录帐号" />
          </el-form-item>

          <el-form-item
            v-if="!showPassword"
            label="密码"
            style="width: 78%"
            label-width="100px"
            prop="password"
          >
            <el-button type="text" @click="resetting" size="medium"
              >重置</el-button
            >
          </el-form-item>

          <el-form-item
            v-if="showPassword"
            label="密码"
            style="width: 78%"
            label-width="100px"
            type="password"
            prop="password"
          >
            <el-input v-model="shopInfo.password" placeholder=" 密码" />
          </el-form-item>

          <el-form-item
            v-if="showPassword"
            label="确认密码"
            style="width: 78%"
            label-width="100px"
            type="password"
            prop="confirmPsw"
          >
            <el-input
              v-model="shopInfo.confirmPsw"
              @input="changeConfirmPsw"
              placeholder=" 确认密码"
            />
          </el-form-item>
        </div>
        <el-form-item
          label="是否禁用"
          style="justify-content: flex-start; margin-left: 10px"
        >
          <el-switch
            v-model="shopInfo.isUse"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="可使用"
            inactive-text="已禁用"
            :active-value="1"
            :inactive-value="2"
          >
          </el-switch>
        </el-form-item>
        <el-form-item
          v-if="shopInfo.platformType && shopInfo.platformType.includes(1)"
          label="饿了么店铺id"
          style="width: 78%"
          label-width="100px"
          prop="eleShopId"
        >
          <el-input v-model="shopInfo.eleShopId" placeholder=" 店铺id" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeShop('shopInfo')">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate('shopInfo')"
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <!-- 详情 -->
    <el-dialog
      width="50%"
      :visible.sync="dialogInfo"
      title="详情"
      @close="dialogInfo = false"
      center
    >
      <el-form :model="shopInfo" label-width="120px">
        <div style="display: flex">
          <el-form-item label="商户名称：" style="width: 78%" prop="name">
            <span>{{ shopInfo.name }}</span>
          </el-form-item>

          <el-form-item label="联系电话：" style="width: 78%" prop="phone">
            <span>{{ shopInfo.phone }}</span>
          </el-form-item>
        </div>
        <el-form-item label="商户logo：" style="width: 78%" prop="logo">
          <el-image
            v-if="shopInfo.logo"
            style="width: 30%; height: 30%"
            :src="'/tea-management/image/' + shopInfo.logo"
            fit="cover"
          ></el-image>
        </el-form-item>
        <div style="display: flex">
        <el-form-item label="是否可用：" style="width: 78%" prop="name">
          <span>{{ shopInfo.isUse == 1 ? "可用" : "已禁用" }}</span>
        </el-form-item>
         <el-form-item label="模板颜色：" style="width: 78%" prop="name">
          {{shopInfo.colorName }}
        </el-form-item>
        </div>
        <el-form-item
          v-if="shopInfo.isUseDevice == 1"
          label="授权设备型号："
          style="width: 78%"
          prop="equipmentId"
        >
          <span>{{ shopInfo.equipmentId }}</span>
        </el-form-item>
        <div style="display: flex">
          <el-form-item label="所在城市：" prop="city" style="width: 78%">
            <span
              >{{ shopInfo.province }}{{ shopInfo.city
              }}{{ shopInfo.area }}</span
            >
          </el-form-item>

          <el-form-item label="详细地址：" style="width: 78%" prop="address">
            <span>{{ shopInfo.address }}</span>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item label="所属行业：" style="width: 78%" prop="sector">
            <span>{{ shopInfo.sector }}</span>
          </el-form-item>
          <el-form-item label="月营业额：" style="width: 78%" prop="turnoverM">
            <span>￥{{ shopInfo.turnoverM }}</span>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item label="纬度：" style="width: 78%" prop="lat">
            <span>{{ shopInfo.lat }}</span>
          </el-form-item>
          <el-form-item label="经度：" style="width: 78%" prop="lng">
            <span>{{ shopInfo.lng }}</span>
          </el-form-item>
        </div>
        <div style="display: flex">
          <el-form-item label="所属品牌：" prop="brandId" style="width: 78%">
            <span>{{ shopInfo.brandName }}</span>
          </el-form-item>
          <el-form-item
            label="是否分账"
            prop="divideAccounts"
            style="width: 78%"
            label-width="100px"
          >
            <span>{{ shopInfo.divideAccounts == 1 ? "是" : "否" }}</span>
          </el-form-item>
          <el-form-item
            v-if="shopInfo.divideAccounts == 1"
            label="分账比例"
            style="width: 78%"
            label-width="140px"
            prop="totalCount"
          >
            <span>{{ shopInfo.proportion }}%</span>
          </el-form-item>
        </div>
        <el-form-item label="经营商品：" prop="platformName" style="width: 78%">
          <span>{{ shopInfo.platformName }}</span>
        </el-form-item>
        <el-form-item label="所属平台：" prop="goodsName" style="width: 78%">
          <span>{{ shopInfo.goodsName }}</span>
        </el-form-item>
        <el-form-item label="登录帐号：" style="width: 78%" prop="username">
          <span>{{ shopInfo.username }}</span>
        </el-form-item>
        <el-form-item label="合同：" style="width: 78%" prop="agreement">
          <viewer :images="agreementArr">
            <!-- photo 一定要一个数组，否则报错 -->
            <img
              style="width: 30%; height: 30%"
              v-for="(img, index) in agreementArr"
              :src="'/tea-management/image/' + img"
              :key="index"
              :onerror="errorImg"
            />
          </viewer>
          <!-- <img v-image-previewer style="width: 30%; height: 30%"
            v-for="(img,index) in agreementArr"
            :key="index"
            :src="'/tea-management/image/' + img" /> -->
        </el-form-item>
        <el-form-item
          v-if="shopInfo.type != undefined && shopInfo.type.includes('1')"
          label="饿了么店铺id："
          style="width: 78%"
          prop="shopId"
        >
          <span>{{ shopInfo.eleShopId }}</span>
        </el-form-item>
        <el-form-item
          v-if="shopInfo.equipmentType == 2"
          label="机器二维码："
          style="width: 78%"
        >
          <span
            v-for="(item, index) in shopInfo.equipmentCodeList"
            :key="index"
          >
            <img
              style="width: 150px; height: 150px"
              :src="'/tea-management/image/' + item"
            />
            <el-button type="text" @click="downLoadCode(item)"
              >下载二维码</el-button
            >
          </span>
        </el-form-item>
        <!-- <el-form-item
          label="饿了么回调地址："
          style="width: 100%"
          prop="redirectUri"
          label-width="130px"
        >
          <span>{{ shopInfo.redirectUri }}</span>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogInfo = false">取 消</el-button>
        <el-button type="primary" @click="dialogInfo = false">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 配料价格 -->
    <el-dialog
      width="50%"
      :visible.sync="dialogPrice"
      title="配料价格"
      @close="closePrice('batchRules')"
      center
    >
      <el-form ref="batchRules" :model="goodBatch">
        <el-table
          border
          :data="goodBatch.batchList"
          class="neitable"
          :header-cell-style="{ background: '#C2C2C2', color: '#3D3D3D' }"
        >
          <el-table-column label="商品配料">
            <template slot-scope="scope">
              <span>{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="签约价格">
            <template slot-scope="scope">
              <el-form-item
                :prop="'batchList.' + scope.$index + '.price'"
                :rules="batchRules.price"
              >
                <el-input
                  type="number"
                  v-model="scope.row.price"
                  placeholder="请输入"
                ></el-input>
              </el-form-item>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closePrice('batchRules')">取 消</el-button>
        <el-button type="primary" @click="savePrice('batchRules')"
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
      <el-table-column prop="phone" label="联系电话" align="center">
      </el-table-column>
      <el-table-column label="是否可用" align="center">
        <template slot-scope="scope">
          {{ scope.row.isUse == 1 ? "可用" : "已禁用" }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openInfo(scope.row)"
            >查看详情</el-button
          >
          <el-button type="primary" size="mini" @click="openEditShop(scope.row)"
            >编辑</el-button
          >
          <el-button
            type="primary"
            size="mini"
            @click="openEditBatch(scope.row)"
            >配料价格</el-button
          >
          <!-- <el-button type="success" size="mini" @click="downLoadCode(scope.row)"
            >下载小程序二维码</el-button
          > -->
          <!-- <a :href="'/tea-management/image/' + scope.row.codeUrl" @click="downLoadCode(scope.row)">下载小程序二维码</a> -->
          <!-- <el-button
            type="danger"
            size="mini"
            @click="removeDataById(scope.row)"
            >删除</el-button
          > -->
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
import shopApi from "@/api/admins/shop";
import { getCascaderOptions } from "@/district/provinceCity";
import TMap from "../map/map";
import { jsonp } from "vue-jsonp";
export default {
  components: {
    TMap,
  },
  data() {
    const validatePhone = (rule, value, callback) => {
      const regex = /^1[3-9]\d{9}$/;
      if (!regex.test(value)) {
        callback(new Error("请输入正确的手机号"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value != "" && typeof value === "string" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的密码"));
      }
    };
    var validateSurePassword = (rule, value, callback) => {
      if (this.shopInfo.confirmPsw != this.shopInfo.password) {
        callback(new Error("确认密码和密码不一致"));
      } else {
        callback();
      }
    };

    const validateName = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的商户名称"));
      }
    };
    const validateAddress = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的详细地址"));
      }
    };
    const validateLat = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的纬度"));
      }
    };
    const validateLng = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的经度"));
      }
    };
    const validateShopId = (rule, value, callback) => {
      if (value != "" && value.toString().trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的饿了么店铺id"));
      }
    };
    const validateRedirectUri = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的回调地址"));
      }
    };

    const validateUsername = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的登陆账号"));
      }
    };
    const validateequipmentId = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的授权设备型号"));
      }
    };
    const validateSector = (rule, value, callback) => {
      if (value != "" && value.trim() != "") {
        callback();
      } else {
        callback(new Error("请输入有效的所属行业"));
      }
    };

    return {
      list: null, // 查询后接口返回集合
      page: 1, // 当前页
      limit: 10, // 每页显示记录数
      total: 0, // 总记录数
      shopQuery: {},
      errorImg: "../../static/errorImg.png",
      brandList: [],
      agreementList: [],
      agreementArr: [],
      equipmentList: [],
      shopInfo: {
        goodRangeList: [],
        platformType: [],
        confirmPsw: "",
      },
      goodBatch: {
        batchList: [{ name: "", price: "" }],
      },
      searchWord: "",
      BMap: null,
      // 地图显示的中心坐标
      location: {
        lng: 121.58308,
        lat: 38.88034,
      },
      // 缩放，15基本上就可以看附近的一些街道了
      zoom: 15,
      //所在地区组件
      districtOptions: [],
      dialogPrice: false,
      dialogInfo: false,
      showPassword: true,
      listLoading: false,
      dialogShop: false,
      goods: [],
      batchList: [],
      templateList: [],
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
      typeList: [
        {
          value: 0,
          label: "自助店",
        },
        {
          value: 1,
          label: "饿了么外卖店",
        },
        {
          value: 2,
          label: "美团外卖店",
        },
      ],
      rules: {
        name: [
          { required: true, message: "请输入商户名称", trigger: "blur" },
          { validator: validateName, trigger: "blur" },
        ],
        phone: [
          { required: true, trigger: "blur", message: "请输入联系电话" },
          { validator: validatePhone, trigger: "blur" },
        ],
        district: [
          { required: true, message: "请选择所在城市", trigger: "blur" },
        ],
        address: [
          { required: true, message: "请输入详细地址", trigger: "blur" },
          { validator: validateAddress, trigger: "blur" },
        ],
        lat: [
          { required: true, message: "请输入纬度", trigger: "blur" },
          { validator: validateLat, trigger: "blur" },
        ],
        lng: [
          { required: true, message: "请输入经度", trigger: "blur" },
          { validator: validateLng, trigger: "blur" },
        ],
        goodRangeList: [
          { required: true, message: "请选择经营商品", trigger: "blur" },
        ],
        templateId: [
          { required: true, message: "请选择模板颜色", trigger: "blur" },
        ],
        isUseDevice: [
          { required: true, message: "请选择是否使用设备", trigger: "blur" },
        ],
        equipmentIdList: [
          { required: true, message: "请选择授权设备型号", trigger: "blur" },
        ],
        logo: [{ required: true, message: "请上传商户logo", trigger: "blur" }],
        agreement: [
          { required: false, message: "请上传合同", trigger: "blur" },
        ],
        brandId: [
          { required: true, message: "请选择所属品牌", trigger: "blur" },
        ],
        divideAccounts: [
          { required: true, message: "请选择是否分账", trigger: "blur" },
        ],
        platformType: [
          { required: true, message: "请选择所属平台", trigger: "blur" },
        ],
        eleShopId: [
          { required: true, message: "请输入商户id", trigger: "blur" },
          { validator: validateShopId, trigger: "blur" },
        ],
        redirectUri: [
          { required: true, message: "请输入回调地址", trigger: "blur" },
          { validator: validateRedirectUri, trigger: "blur" },
        ],
        username: [
          { required: true, message: "请输入登录帐号", trigger: "blur" },
          { validator: validateUsername, trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePassword, trigger: "blur" },
        ],
        confirmPsw: [
          { required: true, message: "请输入确认密码", trigger: "blur" },
          { validator: validateSurePassword, trigger: "blur" },
        ],
        turnoverM: [
          { required: true, message: "请输入月营业额", trigger: "input" },
          {
            pattern:
              /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
            message: "请输入正确金额格式,可保留两位小数",
          },
        ],
        sector: [
          { required: true, message: "请输入所属行业", trigger: "blur" },
          { validator: validateSector, trigger: "blur" },
        ],
      },
      batchRules: {
        price: [{ required: true, message: "请输入价格", trigger: "blur" }],
      },
    };
  },

  created() {
    this.getList();
    this.getBrands();
    this.getGoodsList();
    // this.getEquipmentList();
    this.districtOptions = getCascaderOptions();
  },
  methods: {
    savePrice(batchRules) {
      this.$refs[batchRules].validate((valid) => {
        if (valid) {
          shopApi.addPrice(this.goodBatch).then((res) => {
            if (res.code == 20000) {
              this.$message({
                type: "success",
                message: "添加成功",
              });
              this.dialogPrice = false;

              this.goodBatch = {
                batchList: [{ name: "", price: "" }],
              };

              this.$refs[batchRules].clearValidate();
            }
          });
        } else {
          return false;
        }
      });
    },
    // 打开地图弹窗
    openMap() {
      jsonp("https://apis.map.qq.com/ws/geocoder/v1/?address=", {
        output: "jsonp",
        address: this.searchWord,
        key: "65SBZ-63RW3-RBB3J-OMWGH-2XTZO-63BY7",
      })
        .then((res) => {
    
          if (res.status == 0) {
            // 通过地址得到经纬度
            // locations.value = `${res.result.location.lat},${res.result.location.lng}`
            // let center = new qq.maps.LatLng(res.result.location.lat, res.result.location.lng)
            // map.panTo(center)  // 重新设置地图中心点
            // initMap(res.result.location.lat, res.result.location.lng)

            this.$refs.ms.setLocationByLatLng(
              res.result.location.lat,
              res.result.location.lng
            );
          } else {
            this.$messages(res.message);
          }
        })
        .catch((err) => {
          // search_btn.value = false
          console.log("地图错误：", err);
        });
      // 根据省市区设置初始值
      // this.$refs.ms.setLocationByAddress(this.mainForm.address);
      // 根据经纬度设置初始值

      // this.$refs.ms.setLocationByLatLng(this.mainForm.lat, this.mainForm.lng);
    },

    // 地址选择后的回调函数
    selectAddress(address, lat, lng, province, city, area) {
      this.shopInfo.lat = lat;
      this.shopInfo.lng = lng;
      this.shopInfo.province = province;
      this.shopInfo.city = city;
      this.shopInfo.area = area;
      this.shopInfo.address = address;
      this.searchWord = address;
    },
    // 上传协议成功调用方法
    handleAgreementSuccess(res, file) {
      this.shopInfo.agreement =
        this.shopInfo.agreement == undefined
          ? res.data.url
          : this.shopInfo.agreement + "," + res.data.url;
      // this.agreementList =
      //   this.shopInfo.agreement == undefined
      //     ? []
      //     : this.shopInfo.agreement.split(",");
    },
    handleAgreementRemove(res) {
      let rmUrl = res.response
        ? res.response.data.url
        : res.url.replace("/tea-management/image/", "");
      this.shopInfo.agreement = this.shopInfo.agreement
        .replace(rmUrl, "")
        .replace(",,", ",");
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
      // return (isJPG || isBMP || isGIF || isPNG) && isLt2M;
      return (isJPG || isPNG) && isLt2M;
    },

    downLoadCode(data) {
      // window.location.href = '/tea-management/image/'+data.codeUrl;

      const link = document.createElement("a");
      let url = "/tea-management/image/" + data;
      // 这里是将url转成blob地址
      fetch(url)
        .then((res) => res.blob())
        .then((blob) => {
          link.href = URL.createObjectURL(blob);

          link.style.display = "none";
          link.download = "机器二维码";
          link.click();
        });
    },

    changeConfirmPsw(e) {
      this.shopInfo.confirmPsw = e;
      this.$forceUpdate();
    },
    changeInput(e) {
      let value = e.replace(/^(0+)|[^\d]+/g, "");
      //如果输入框数值大于360，取余计算
      if (value > 30) {
        this.shopInfo.proportion = 30;
      } else {
        this.shopInfo.proportion = value;
      }
      //如果用户将input输入框内容清除且没有输入其他值，则输入框内容为0
      if (value === "") {
        this.shopInfo.proportion = 1;
      }
    },
    handleCoverSuccess(res, file) {
      // this.carrierInfo.codeDrive=res.data.url
      this.$set(this.shopInfo, "logo", res.data.url);
    },
    beforeCoverUpload(file) {
      //添加文件类型的限制
      let types = ["image/jpeg", "image/jpg", "image/png"];
      const isImage = types.includes(file.type);
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
        return false;
      }
      if (!isImage) {
        this.$message.error("上传图片只能是 JPG、JPEG、PNG 格式!");
        return false;
      }
    },

    // onChangeProvince(data) {
    //   this.shopInfo.province = data.value;
    // },
    // onChangeCity(data) {
    //   this.shopInfo.city = data.value;
    // },
    // onChangeArea(data) {
    //   this.shopInfo.area = data.value;
    // },
    onChange(data) {
      this.shopInfo.province = data[0];
      this.shopInfo.city = data[1];
      this.shopInfo.area = data[2];
    },
    resetting() {
      this.shopInfo.password = "";
      this.shopInfo.confirmPsw = "";
      this.showPassword = true;
    },

    getList(page = 1) {
      this.page = page;
      shopApi
        .getList(this.page, this.limit, this.shopQuery)
        .then((response) => {
          this.list = response.data.rows;
          this.total = response.data.total;
        });
    },

    getBrands() {
      shopApi.getBrandList().then((response) => {
        this.brandList = response.data.list;
      });
    },
    getGoodsList() {
      shopApi.getGoodsList().then((response) => {
        this.goods = response.data.list;
      });
    },
    getEquipmentList() {
      shopApi.getEquipmentList().then((response) => {
        this.equipmentList = response.data.list;
      });
    },
    getTemplateList() {
      shopApi.getTemplateList().then((response) => {
        this.templateList = response.data.list;
      });
    },

    resetData() {
      this.shopQuery = {};
      this.getList();
    },
    openShopDialog() {
      this.getEquipmentList();
      this.getTemplateList();
      this.dialogShop = true;
      this.shopInfo = {
        goodRangeList: [],
      };
    },
    closePrice(batchRules) {
      this.$refs[batchRules].clearValidate();
      this.dialogPrice = false;
      this.goodBatch = { batchList: [{ name: "", price: "" }] };
    },
    // 添加
    addShop() {
      shopApi
        .insertData(this.shopInfo)
        .then((response) => {
          if (response.message == "该商铺已添加") {
            this.$message.error(response.message);
          } else if (response.message == "该手机号已被使用") {
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

    openEditShop(data) {
      this.dialogShop = true;
      this.showPassword = false;
      this.searchWord = "";
      this.getEquipmentList();
      this.getTemplateList();
      shopApi.getInfo(data.id).then((response) => {
        this.shopInfo = response.data.info;
        this.shopInfo.district = [
          response.data.info.province,
          response.data.info.city,
          response.data.info.area,
        ];
        // this.shopInfo.district[0] = response.data.info.province;
        // this.shopInfo.district[1] = response.data.info.city;
        // this.shopInfo.district[2] = response.data.info.area;
        if (this.shopInfo.goodRangeList == null) {
          this.shopInfo.goodRangeList = [];
        }
        if (this.shopInfo.platformType == null) {
          this.shopInfo.platformType = [];
        }

        if (response.data.info.agreement != null) {
          let agreementList = response.data.info.agreement.split(",");
          let newList = [];
          agreementList.forEach((item, index) => {
            let obj = {};
            obj.url = "/tea-management/image/" + item;
            obj.name = index;
            newList.push(obj);
          });
          this.agreementList = newList;
        }
        
        
      });
    },
    openEditBatch(data) {
      this.dialogPrice = true;
      shopApi.getShopBatch(data.id).then((response) => {
        this.goodBatch.batchList = response.data.list;
      });
    },
    // 修改信息
    updateShop() {
      shopApi
        .updateData(this.shopInfo.id, this.shopInfo)
        .then((response) => {
          if (response.message == "该商铺已添加") {
            this.$message.error(response.message);
          } else {
            this.dialogShop = false;
            this.$message({
              type: "success",
              message: "修改成功",
            });
            this.getList();
          }
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
        shopApi.deleteData(data.id).then((response) => {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          // 回到列表页面
          this.getList();
        });
      });
    },

    // 查看
    openInfo(data) {
      this.dialogInfo = true;
      shopApi.getInfo(data.id).then((response) => {
        this.shopInfo = response.data.info;
        if (this.shopInfo.goodRangeList == null) {
          this.shopInfo.goodRangeList = [];
        }
        if (this.shopInfo.platformType == null) {
          this.shopInfo.platformType = [];
        }
        if (this.shopInfo.equipmentIdList == null) {
          this.shopInfo.equipmentIdList = [];
        }
        if (response.data.info.agreement != null) {
          this.agreementArr = response.data.info.agreement.split(",");
        }
        this.shopInfo.colorName = this.colorNameList.filter(item => item.value == this.shopInfo.color)[0].label;

      });
    },

    closeShop(shopInfo) {
      this.$refs[shopInfo].clearValidate();
      this.shopInfo = {
        goodRangeList: [],
      };
      this.dialogShop = false;
      this.showPassword = true;
      this.agreementList = [];
    },
    // 保存并下一步
    saveOrUpdate(shopInfo) {
      // console.log(this.shopInfo.platformType.includes(1))
      // 判断添加还是修改
      if (!this.shopInfo.id) {
        this.$refs[shopInfo].validate((valid) => {
          if (valid) {
            if (
              this.shopInfo.address == "" ||
              this.shopInfo.address == null ||
              this.shopInfo.address == undefined
            ) {
              this.$message({
                type: "warnning",
                message: "请添加商户地址!",
              });
              return false;
            }
            // 添加
            this.$refs[shopInfo].clearValidate();
            this.addShop();
          } else {
            return false;
          }
        });
      } else {
        this.$refs[shopInfo].validate((valid) => {
          if (valid) {
            if (
              this.shopInfo.address == "" ||
              this.shopInfo.address == null ||
              this.shopInfo.address == undefined
            ) {
              this.$message({
                type: "warnning",
                message: "请添加商户地址!",
              });
              return false;
            }
            // 修改
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