<template>
  <div class="box">
    <div class="btnk" @click="goProduct()">
        <div class="point-flicker"></div>
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
      
    };
  },
  created() {
    
    
  },
  methods: {
    goProduct(){
      this.$router.push({ path: "/Product" });
    }

  },
  mounted() {
    
  },
});
</script>
  
<style scoped>
.box{
  width: 100%;
  height: 100%;
  background-image: url('../../static/home_bg.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: fixed;
  position: fixed;
  top: 0;
  z-index: 0;
}
.btnk{
  width: 491px;
  height: 141px;
  margin: auto;
  position: fixed;
  left: 0;
  right: 0;
  bottom: 201px;
  z-index: 2;
  cursor: pointer;
}
.btnk img{
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 1;
}
/* 设置动画前颜色 */
.point-flicker:after {
  background-image: url('../../static/homebtn.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}
/* 设置动画后颜色 */
.point-flicker:before {
  background-image: url('../../static/homebtn.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}
/* 设置动画 */
.point-flicker:before,
.point-flicker:after {
  content: "";
  width: 491px;
  height: 141px;
  position: absolute;
  left: 50%;
  top: 50%;
  margin-left: -245.5px;
  margin-top: -70.5px;
  animation: warn 1.5s ease-out 0s infinite;
  z-index: -1;
}
@keyframes warn {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.5);
  }
  100% {
    transform: scale(1);
  }
}
</style>