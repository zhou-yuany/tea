<template>
    <div class="box">
        <div class="pro_head">
            <img src="../../static/back.png" alt="" class="backico" @click="goBack()">
            <div class="pro_title green">选择底料</div>
        </div>
        <div class="middlek">
            <div class="leftpart white_bg active">
                <img src="../../static/goodsa.png" alt="">
                <button type="button" class="white">做奶茶</button>
            </div>
            <div class="rightpart">
                <div class="subtitle dark_gray">杯型</div>
                <ul class="list">
                    <li class="action">大杯</li>
                </ul>
                <div class="subtitle dark_gray">茶底</div>
                <ul class="list">
                    <li :class="{action : num == 0}" @click="num = 0">绿茶</li>
                    <li :class="{action : num == 1}" @click="num = 1">红茶</li>
                </ul>
                <div class="subtitle dark_gray">奶底</div>
                <ul class="list">
                    <li :class="{action : nums == 0}" @click="nums = 0">全脂牛奶</li>
                    <li :class="{action : nums == 1}" @click="nums = 1">低脂牛奶</li>
                </ul>
            </div>
        </div>
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
            num: 0,
            nums: 0
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
            this.$router.push({ path: "/Condiments" });
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
    padding-top: 47px;
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
.rightpart{
    margin-left: 223px;
}
.subtitle{
    font-weight: 400;
    font-size: 38px;
}
.list{
    padding-top: 47px;
    padding-bottom: 46px;
    display: flex;
}
.list li{
    width: 240px;
    height: 80px;
    background: rgba(170, 218, 158, 0.5);
    box-shadow: 0px 5px 7px 0px inset rgba(0,0,0,0.29);
    border-radius: 40px;
    line-height: 80px;
    color: #666666;
    font-weight: bold;
    font-size: 32px;
    text-align: center;
    margin-right: 50px;
    cursor: pointer;
}
.list li.action{
    background: linear-gradient(0deg, #F39D36, #F3C07F);
    border: 1px solid #F39D36;
    box-shadow: 0px 3px 9px 0px rgba(149,94,29,0.45);
    color: #FFFFFF;
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