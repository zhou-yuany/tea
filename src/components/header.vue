<template>
  <div class="box">
    <div class="headerk" ref="getheaderkheight">
      <div class="header_l white">茶饮收银系统</div>
      <div class="header_r">
        <div class="wifik white"><img src="../../static/rss.png" alt="">正常</div>
        <span style="margin-right: 0;">营业日</span>
        <span>{{time}}</span>
        <img src="../../static/notice.png" alt="">
        <img src="../../static/print.png" alt="" @click="goPrint()">
        <img src="../../static/headico1.png" alt="">
        <img src="../../static/out.png" alt="" @click="logout()" style="margin-right: 0;">
      </div>
    </div>
  </div>
</template>
  
<script>
import { defineComponent } from "vue";
import dayjs from "dayjs";
export default defineComponent({
  name: "HeaderLogin",
  data() {
    return {
      time: '',
      defaultHeadImg: require("../../static/header.png"),
      default_cattle: 'this.src="' + require("../assets/zhan_ad.png") + '"', //轮播图占位图
      default_cattle_wen:
        'this.src="' + require("../assets/zhan_home.png") + '"', //文章占位图
    };
  },
  created() {},
  methods: {
    logout(){
      localStorage.removeItem('username');
      this.$router.push({
        path: "/Login"
      });
    },
    dateTime() {
      let day = dayjs().day();
      switch (day) {
        case 1:
          day = "一";
          break;
        case 2:
          day = "二";
          break;
        case 3:
          day = "三";
          break;
        case 4:
          day = "四";
          break;
        case 5:
          day = "五";
          break;
        case 6:
          day = "六";
          break;
        case 0:
          day = "日";
          break;
      }
      this.time =
        dayjs(new Date()).format("YYYY-MM-DD") +
        " " +
        "星期" +
        day +
        " " +
        dayjs(new Date()).format("HH:mm:ss");
    },

    // 打印
    goPrint:function(){
      this.$router.push({
        path: "/print/"
      });
    },
  },
  mounted() {
    const _this = this;
    this.time = setInterval(() => {
      _this.dateTime();
    }, 1000);

    let touheight = this.$refs.getheaderkheight.offsetHeight;
    localStorage.setItem('touheight',touheight)
  },
});
</script>

<style scoped>
.box{
  background-color: #ffffff;
}
.headerk {
  width: 100%;
  height: 100px;
  margin: auto;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header_l{
  width: 300px;
  height: 100%;
  font-size: 32px;
  font-weight: bold;
  text-align: center;
  line-height: 100px;
  background-color: #0256FF;
}
.header_r{
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-right: 34px;
}
.wifik{
  width: 85px;
  height: 40px;
  border-radius: 8px;
  background: #4ECB70;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  font-family: PingFangSCBold-Bold;
}
.wifik img{
  width: 20px!important;
  height: 20px!important;
  display: block;
  cursor: none!important;
  margin-right: 2.7px!important;
}
.header_r span{
  font-size: 24px;
  margin-left: 38px;
  margin-right: 50px;
}
.header_r img{
  width: 24px;
  height: 24px;
  display: block;
  cursor: pointer;
  margin-right: 25px;
}
</style>