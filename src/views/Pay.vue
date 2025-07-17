<template>
    <div class="box">
        <div class="pro_head">
            <img src="../../static/back.png" alt="" class="backico" @click="goBack()">
            <div class="pro_title green">扫码支付</div>
        </div>
        <div class="middlek">
            <div class="leftpart white_bg active">
                <img src="../../static/goodsa.png" alt="">
                <button type="button" class="white">做奶茶</button>
            </div>
            <div class="xian"></div>
            <div class="rightpart white_bg">
                <div class="lp">
                    <div class="hanga">已选：<div>DIY奶茶，大杯，绿茶，全脂牛奶，小料（珍珠，椰果，布丁）</div></div>
                    <div class="hangb">合计：￥<span>12.00</span></div>
                    <img src="../../static/wx.png" alt="" class="wx">
                    <div class="hangc">请使用微信扫描二维码支付</div>
                </div>
                <img src="../../static/code.png" alt="" class="codek" @click="showTc()">
            </div>
        </div>
        <img src="../../static/cancel.png" alt="" class="cancelico" @click="goIndex()">
        <!-- 支付结果弹窗 -->
        <div class="mask" v-show="isShow">
            <div class="tc white_bg">
                <img src="../../static/success.png" alt="">
                <div>已支付成功，开始制作</div>
            </div>
            <div class="tc white_bg">
                <img src="../../static/fail.png" alt="">
                <div>支付失败</div>
            </div>
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
            isShow: false
        };
    },
    created() {
      
      
    },
    methods: {
        goBack(){
            this.$router.go(-1);
        },

        goIndex(){
            this.$router.push({ path: "/Home" });
        },

        showTc(){
            this.isShow = true
            setTimeout(() => {
                this.isShow = false
            },2000)
        }
    },
    mounted() {
      
    }
});
</script>
    
<style scoped>
.pro_head{
  width: 100%;
  margin: auto;
  padding-top: 60px;
} 
.pro_title{
  width: 480px;
  height: 100px;
  margin: auto;
  background: rgba(170, 218, 158, 0.5);
  box-shadow: 0px 5px 7px 0px inset rgba(0,0,0,0.29);
  border-radius: 50px;
  font-size: 48px;
  font-weight: bold;
  line-height: 100px;
  text-align: center;
}
.middlek{
  width: 100%;
  margin: auto;
  display: flex;
  align-items: center;
  padding-top: 113px;
}
.leftpart{
  width: 423px;
  height: 500px;
  box-shadow: 0px 2px 9px 0px rgba(23,103,45,0.29);
  border-radius: 100px;
  margin-left: 139px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.active{
  border: 5px solid #F19E36;
  box-shadow: 0px 0px 29px 10px rgba(241,158,54,0.29);
}
.leftpart img{
  width: 249px;
  height: 279px;
  display: block;
  margin-bottom: 32px;
}
.leftpart button{
  width: 239px;
  height: 69px;
  background: linear-gradient(0deg, #F39D36, #F3C07F);
  box-shadow: 0px 3px 9px 0px rgba(149,94,29,0.45);
  border-radius: 32px;
  font-weight: bold;
  font-size: 40px;
  text-shadow: 0px 1px 1px rgba(149,94,29,0.45);
  text-align: center;
  line-height: 69px;
}
.xian{
    width: 2px;
    height: 414px;
    background: #FFFFFF;
    box-shadow: -1px 0px 1px 0px rgba(61,99,23,0.25);
    margin-left: 94px;
    margin-right: 98px;
}
.rightpart{
    width: 1000px;
    height: 500px;
    box-shadow: 0px 1px 6px 0px rgba(102,29,42,0.15);
    border-radius: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
}
.lp{
    width: 491px;
}
.hanga{
    width: 100%;
    text-align: left;
    font-size: 28px;
    display: flex;
}
.hanga div{
    width: 82%;
}
.hangb{
    width: 100%;
    text-align: left;
    font-size: 28px;
    margin-top: 27px;
    margin-bottom: 34px;
}
.hangb span{
    font-size: 40px;
    font-weight: bold;
}
.wx{
    width: 301px;
    height: 82px;
    display: block;
    margin-left: 37px;
    margin-bottom: 24px;
}
.hangc{
    width: 100%;
    text-align: left;
    font-size: 24px;
    margin-left: 44px;
}
.codek{
    width: 313px;
    height: 313px;
    display: block;
    margin-left: 30px;
}
/* 支付结果弹窗 */
.tc{
    width: 830px;
    height: 600px;
    border-radius: 56px;
    position: fixed;
    top: 50%;
    left: 50%;
    margin-top: -300px;
    margin-left: -415px;
    z-index: 100;
    display: flex;
    flex-direction: column;
    align-items: center;
}
.tc img{
   width: 369px;
   height: 355px;
   display: block; 
   margin-top: 23px;
   margin-bottom: 66px;
}
.tc div{
    width: 100%;
    text-align: center;
    font-size: 48px;
}
</style>