<template>
  <div class="app-container">
    <el-form :model="shopInfo" label-width="120px">
      <div class="title">基本信息</div>
      <div class="subtitle"><div></div>基本信息</div>
      <div style="display: flex;">
        <el-form-item
          label="商户名称："
          label-width="120px"
          prop="name" style="width: 25%"
        >
          <span>{{ shopInfo.name }}</span>
        </el-form-item>

        <el-form-item
          label="联系电话："
          label-width="120px"
          prop="phone" style="width: 25%"
        >
          <span>{{ shopInfo.phone }}</span>
        </el-form-item>
        <el-form-item
          label="所属行业："
          label-width="120px"
          style="width: 25%"
          prop="sector"
        >
          <span>{{shopInfo.sector}}</span>
        </el-form-item>
        <!-- <el-form-item label="小程序二维码：" prop="codeUrl" style="width: 78%;display: flex;flex-direction: column;justify-content: center;align-items: center;">
          <img
            style="width: 150px; height: 150px"
            :src="'/tea-management/image/' + shopInfo.codeUrl"
          />
          <el-button type="text" @click="downLoadCode(shopInfo.codeUrl)"
            >下载二维码</el-button
          >
        </el-form-item> -->
        
      </div>
      <div style="display: flex;">
        <el-form-item
          label="所在城市："
          prop="city"
          label-width="120px" style="width: 25%"
        >
          <span
            >{{ shopInfo.province }}{{ shopInfo.city }}{{ shopInfo.area }}</span
          >
        </el-form-item>

        <el-form-item
          label="详细地址："
          label-width="120px"
          prop="address" style="width: 25%"
        >
          <span>{{ shopInfo.address }}</span>
        </el-form-item>

        <el-form-item
          label="纬度："
          label-width="120px"
          prop="lat" style="width: 25%"
        >
          <span>{{ shopInfo.lat }}</span>
        </el-form-item>
        <el-form-item
          label="经度："
          label-width="120px"
          prop="lng" style="width: 25%"
        >
          <span>{{ shopInfo.lng }}</span>
        </el-form-item>
      </div>
      <div style="display: flex;">
        <el-form-item label="商户logo：" style="width: 25%" prop="logo">
          <img
            style="width: 150px; height: 150px"
            :src="'/tea-management/image/' + shopInfo.logo"
          />
        </el-form-item>
      </div>
      <div style="display: flex;">
        <el-form-item
          label="营业时间："
          style="width: 78%"
          label-width="120px"
          prop="phone"
        >
          <el-time-select
            placeholder="起始时间"
            v-model="shopInfo.onStartTime"
            :picker-options="{
              start: '05:30',
              step: '00:15',
              end: '19:30'
            }">
          </el-time-select>
          <el-time-select
            placeholder="结束时间"
            v-model="shopInfo.onEndTime"
            :picker-options="{
              start: '05:30',
              step: '00:15',
              end: '24:00',
              minTime: startTime
            }">
          </el-time-select>
          <el-button type="primary" @click="saveTime()" style="margin-left: 10px;">保存</el-button>
        </el-form-item>
      </div>
      <div class="subtitle"><div></div>营业信息</div>
      <div style="display: flex;">
        <el-form-item
          label="授权设备型号："
          style="width: 25%"
          prop="equipmentId"
        >
          <span>{{ shopInfo.equipmentId }}</span>
        </el-form-item>
        
        <el-form-item
          label="所属品牌："
          prop="brandId"
          style="width: 25%"
          label-width="120px"
        >
          <span>{{ shopInfo.brandName }}</span>
        </el-form-item>
        <el-form-item
          label="所属平台："
          prop="type"
          style="width: 25%"
          label-width="120px"
        >
          <span>{{
            shopInfo.goodsName
          }}</span>
        </el-form-item>
        <el-form-item
          label="登录帐号："
          style="width: 25%"
          label-width="120px"
          prop="username"
        >
          <span>{{ shopInfo.username }}</span>
        </el-form-item>
      </div>
      <div style="display: flex;">
        <el-form-item
          label="饿了么店铺id："
          style="width: 25%"
          label-width="120px"
          prop="eleShopId" v-if="shopInfo.platformType && shopInfo.platformType.includes(1)"
        >
          <span>{{ shopInfo.eleShopId }}</span>
        </el-form-item>
        <el-form-item
          label="月营业额："
          label-width="120px"
          style="width: 25%"
          prop="turnoverM"
        >
          <span>￥{{shopInfo.turnoverM}}</span>
        </el-form-item>
        <!-- <el-form-item
          label="回调地址："
          style="width: 78%"
          label-width="120px"
          prop="redirectUri"
        >
        <span>{{shopInfo.redirectUri}}</span>
        </el-form-item> -->
      </div>
      <div style="display: flex;">
        <el-form-item label="合同：" prop="logo">
          <viewer :images="agreementArr">
            <img
              style="width: 150px; height: 150px;margin-right: 10px;"
              v-for="(img, index) in agreementArr"
              :src="'/tea-management/image/' + img"
              :key="index"
              :onerror="errorImg"
            />
          </viewer>
        </el-form-item>
      </div>
      <div style="display: flex;" v-if="shopInfo.equipmentType == 2">
        <el-form-item label="机器二维码：">
          <span v-for="(item, index) in shopInfo.equipmentCodeList" :key="index">
             <img
            style="width: 150px; height: 150px"
            :src="'/tea-management/image/' + item"
          />
          <el-button type="text" @click="downLoadCode(item)"
            >下载二维码</el-button
          >
          </span>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import shopApi from "@/api/admins/shop";
export default {
  data() {
    return {
      shopInfo: {},
      agreementArr: []
    };
  },
  created() {
    let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;
    this.getInfo(shopId);
  },
  methods: {
    saveTime(){
      shopApi
        .updateTime(this.shopInfo.id, this.shopInfo)
        .then((response) => {
            this.$message({
              type: "success",
              message: "保存成功",
            });
      
        })
    },
    downLoadCode(data) {
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
    getInfo(id) {
      shopApi.getInfo(id).then((response) => {
        this.shopInfo = response.data.info;
        if (response.data.info.agreement != null) {
          this.agreementArr = response.data.info.agreement.split(",");
        }
      });
    },
  },
};
</script>

<style>
.title{
  font-size: 24px;
  padding-bottom: 20px;
}
.subtitle{
  width: 100%;
  height: 40px;
  background-color: #f4f4f4;
  font-size: 18px;
  display: flex;
  align-items: center;
  padding-left: 10px;
}
.subtitle div{
  width: 4px;
  height: 20px;
  background-color: #409EFF;
  margin-right: 8px;
}
</style>