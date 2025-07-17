<template>
  <div class="home_box">
    <!-- 顶部 -->
    <div class="home_headtop">
      <div class="home_top_l">
        <img src="../../static/shopico.png" alt="">
        <div class="home_shopxx">
          <div>{{shopName}}</div>
          <div class="home_bianhao gray">用户编号： {{number}}</div>
        </div>
      </div>
      <div class="home_top_r">
        <img src="../../static/header.png" alt="">
        <span>{{cashier}}</span>
      </div>
    </div>
    <!-- 菜单 -->
    <div class="home_funlist">
      <div v-if="divideAccounts == 2" class="white" @click="goFood()"><img src="../../static/index_ico1.png" alt=""></div>
      <div class="white" @click="goOrder()"><img src="../../static/index_ico2.png" alt=""></div>
      <div class="white" @click="goCallnum()"><img src="../../static/index_ico3.png" alt=""></div>
      <div class="white"><img src="../../static/index_ico4.png" alt=""></div>
      <div class="white" @click="goForm()"><img src="../../static/index_ico5.png" alt=""></div>
      <div class="white" @click="goPrint()"><img src="../../static/index_ico6.png" alt=""></div>
      <div class="white" @click="goInventory()"><img src="../../static/index_ico7.png" alt=""></div>
      <div class="white"><img src="../../static/index_ico8.png" alt=""></div>
      <div class="white"><img src="../../static/index_ico9.png" alt=""></div>
      <div class="white" @click="goFormula()"><img src="../../static/index_ico10.png" alt=""></div>
    </div>
  </div>
</template>

<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
import homeApi from "@/api/home/index";
export default defineComponent({
  data() {
    return {
      shopName: '',
      number: '',
      cashier: ''
    };
  },
  created() {
    this.divideAccounts = localStorage.getItem("divideAccounts");
    this.cashier = localStorage.getItem('username');
    this.getShopInfo()
  },
  methods: {
    // 店家详情
    getShopInfo(){
      var adminId = localStorage.getItem("adminId");
      homeApi
        .getShopInfo(adminId)
        .then((res) => {
          if (res.data.code == 20000) {
            let info = res.data.data.info;
            localStorage.setItem("shopId", info.id);
            this.shopName = info.name;
            this.number = info.number;
          }
        });
    },

    // 饮品点单
    goFood:function(){
      this.$router.push({
        path: "/food/"
      });
    },

    // 订单
    goOrder:function(){
      this.$router.push({
        path: "/order/"
      });
    },

    // 叫号取餐
    goCallnum:function(){
      this.$router.push({
        path: "/callnum/"
      });
    },

    // 报表
    goForm:function(){
      this.$router.push({
        path: "/form/"
      });
    },

    // 打印
    goPrint:function(){
      this.$router.push({
        path: "/print/"
      });
    },

    // 库存
    goInventory:function(){
      this.$router.push({
        path: "/inventory/"
      });
    },

    // 配方
    goFormula:function(){
      this.$router.push({
        path: "/formula/"
      });
    }

  },
  mounted() {
    
  },
});
</script>
  
<style scoped>
/* 顶部 */
.home_headtop{
  height: 236px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.home_top_l{
  display: flex;
  align-items: center;
  margin-left: 80px;
}
.home_top_l img{
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
  margin-right: 19px;
}
.home_shopxx div{
  font-size: 28px;
  font-family: PingFangSCBold-Bold;
  line-height: 39px;
}
.home_shopxx div.home_bianhao{
  font-size: 24px;
  margin-top: 11px;
  font-family: PingFangSCMedium-Medium;
}
.home_top_r{
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-right: 88px;
}
.home_top_r img{
  width: 80px;
  height: 80px;
  display: block;
  border-radius: 50%;
  margin-right: 31px;
}
.home_top_r span{
  font-size: 24px;
  font-family: PingFangSCBold-Bold;
}
/* 菜单 */
.home_funlist{
  width: 100%;
  margin: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
}
.home_funlist div{
  width: 320px;
  height: 280px;
  margin: 0 20px 50px;
  cursor: pointer;
}
.home_funlist div img{
  width: 100%;
  height: 100%;
  display: block;
}
</style>