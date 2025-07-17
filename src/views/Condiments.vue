<template>
  <div class="box">
    <div class="pro_head">
      <img src="../../static/back.png" alt="" class="backico" @click="goBack()">
      <div class="pro_title green">选择小料</div>
    </div>
    <div class="middlek">
      <div class="leftpart white_bg active">
        <img src="../../static/goodsa.png" alt="">
        <button type="button" class="white">做奶茶</button>
      </div>
      <div class="rightpart">
        <div class="yixuan orange">已选：大杯，绿茶，全脂牛奶</div>
        <div class="subtitle dark_gray">小料（最少一种，最多三种）</div>
        <div class="flex">
          <ul class="list" ref="scrollContainer">
            <li v-for="(item,key , index) in condiments" :key="index" :class="{'action' : active_obj.indexOf(key)>-1}" @click="selected(key)">{{ item }}</li>
          </ul>
          <div class="updown">
            <img src="../../static/up.png" alt="" @click="goUp()">
            <img src="../../static/down.png" alt="" @click="goDown()">
          </div>
        </div>
      </div>
    </div>
    <div class="nextbtn"><img src="../../static/buy.png" alt="" @click="goBuy()"></div>
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
      condiments:['珍珠','椰果','布丁','椰果1','布丁','椰果','布丁2','椰果','布丁','椰果3','布丁','椰果','布丁4','椰果','布丁','椰果5','布丁','椰果','布丁6'],
      active_obj: [],
      timer: null
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

    // 小料选择
    selected(key){
      let arrIndex = this.active_obj.indexOf(key);
      if(arrIndex>-1){
        this.active_obj.splice(arrIndex,1);
      }else{
        this.active_obj.push(key);
      }
    },

    // 立即购买
    goBuy(){
      this.$router.push({ path: "/Pay" });
    },

    // 上滑
    goUp(){
      let scrolled =this.$refs.scrollContainer.scrollTop;
      let num = 0
      clearInterval(this.timer)
      this.timer = null
      this.timer = setInterval(() => {
        if (!scrolled || num >= 520) {
          clearInterval(this.timer)
          this.timer = null
          return
        }
        this.$refs.scrollContainer.scrollTop = scrolled -= 30
        num += 30
      }, 20)
    }, 

    // 下滑
    goDown(){
      let scrolled =this.$refs.scrollContainer.scrollTop;
      let scrollHeight = this.$refs.scrollContainer.scrollHeight
      let clientHeight = this.$refs.scrollContainer.clientHeight
      let num = 0
      clearInterval(this.timer)
      this.timer = setInterval(() => {
        if (scrolled + clientHeight >= scrollHeight || num >= 520) {
          clearInterval(this.timer)
          return
        }
        this.$refs.scrollContainer.scrollTop = scrolled += 30
        num += 30
      }, 20)
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
  padding-top: 41px;
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
  width: 55%;
  margin-left: 223px;
}
.yixuan{
  font-size: 38px;
  font-weight: 400;
  margin-bottom: 28px;
}
.subtitle{
  font-weight: 400;
  font-size: 38px;
}
.flex{
  display: flex;
  align-items: center;
}
.updown{
  height: 283px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.updown img{
  width: 64px;
  height: 64px;
  display: block;
  cursor: pointer;
}
.list{
  display: flex;
  flex-wrap: wrap;
  margin-top: 45px;
  width: 85%;
  height: 520px;
  overflow: hidden;
  overflow-y: scroll;
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
  margin-bottom: 50px;
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