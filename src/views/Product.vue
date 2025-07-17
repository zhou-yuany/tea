<template>
    <div class="box">
      <div class="pro_head">
        <img src="../../static/back.png" alt="" class="backico" @click="goBack()">
        <div class="pro_title green">选择商品</div>
      </div>
      <ul class="pro_ul">
        <li class="white_bg" :class="{active : num == 0}" @click="num = 0">
            <img src="../../static/goodsa.png" alt="">
            <button type="button" class="white">做奶茶</button>
        </li>
        <li class="white_bg" :class="{active : num == 1}" @click="num = 1">
            <img src="../../static/goodsb.png" alt="">
            <button type="button" class="white">做果茶</button>
        </li>
      </ul>
      <div class="nextbtn"><img src="../../static/next.png" alt="" @click="goNext()"></div>
      <img src="../../static/cancel.png" alt="" class="cancelico" @click="goIndex()">
    </div>
</template>
  
<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
import homeApi from "@/api/home/index";
export default defineComponent({
    data() {
        return {
            num: 0
        };
    },

    created() {
      
    },

    methods: {
        goBack:function(){
            this.$router.go(-1);
        },

        goIndex:function(){
            this.$router.push({ path: "/Home" });
        },

        goNext:function(){
            this.$router.push({ path: "/Primers" });
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
.pro_ul{
    width: 100%;
    margin: auto;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 107px;
    height: 520px;
}
.pro_ul li{
    width: 423px;
    height: 500px;
    box-shadow: 0px 2px 9px 0px rgba(23,103,45,0.29);
    border-radius: 100px;
    margin: 0 97.5px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
}
.pro_ul li.active{
    border: 5px solid #F19E36;
    box-shadow: 0px 0px 29px 10px rgba(241,158,54,0.29);
}
.pro_ul li img{
    width: 249px;
    height: 279px;
    display: block;
    margin-bottom: 32px;
}
.pro_ul li button{
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
.nextbtn{
    width: 100%;
    position: fixed;
    bottom: 48px;
}
.nextbtn img{
    width: 492px;
    height: 143px;
    display: block;
    margin: auto;
    cursor: pointer;
}
</style>