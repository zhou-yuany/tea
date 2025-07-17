<template>
  <div class="box">
    <div class="kuang">
      <div class="leftp"><img src="../../static/loginico.png" alt=""></div>
      <div class="rightp white_bg">
        <div class="title">DIY系统</div>
        <div class="formk white_bg">
          <div>
            <span class="dark_gray">用户名</span>
            <input v-model="username" maxlength="11" />
          </div>
          <div>
            <span class="dark_gray passwords">密码</span>
            <input type="password" v-model="password" />
          </div>
        </div>
        <div class="btnk">
          <button type="button" class="white" @click="goHome()">登录</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import homeApi from "@/api/home/index";
import { defineComponent } from "vue";

export default defineComponent({
  data() {
    return {
      isShow: false,
      username: "",
      password: "",
    };
  },
  created() {
  },
  //beforeDestroy为实例销毁之前调用
  beforeDestroy() {
    if (this.refreshData) {
      // console.log('销毁倒计时')
      clearInterval(this.refreshData);
      this.refreshData = null;
    }
    if (this.refreshTime) {
      // console.log('销毁倒计时')
      clearInterval(this.refreshTime);
      this.refreshTime = null;
    }
  },
  methods: {
    goHome() {
      if(this.username == ""){
        this.$toast({
          message: "请输入账号",
          position: "top",
        });
        return;
      }
      if(this.password == ""){
        this.$toast({
          message: "请输入密码",
          position: "top",
        });
        return;
      }
      localStorage.setItem("username", 'username')
      this.$router.push({ path: "/Home" });
      
    },
    goAgree() {
      this.isShow = true;
    },
    goClose() {
      this.isShow = false;
    },
  },
});
</script>

<style scoped>
.box{
  width: 100%;
  height: 100%;
  background-image: url('../../static/login_bg.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: fixed;
  position: fixed;
  top: 0;
  z-index: 0;
}
.kuang{
  width: 1100px;
  height: 680px;
  position: fixed;
  top: 50%;
  left: 50%;
  margin-top: -340px;
  margin-left: -550px;
  z-index: 1;
  display: flex;
}
.leftp{
  width: 50%;
  height: 680px;
}
.leftp img{
  width: 100%;
  height: 100%;
  display: block;
}
.rightp{
  width: 50%;
  height: 680px;
}
.title{
  width: 100%;
  font-size: 38px;
  font-weight: 400;
  color: #333333;
  text-align: center;
  margin-top: 71px;
  margin-bottom: 90px;
}
.formk {
  width: 450px;
  height: auto;
  margin: auto;
}
.formk div {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  margin-bottom: 71px;
  border-bottom: 1px solid #E5E5E5;
}
.formk div span {
  font-size: 19px;
  font-weight: 400;
}
.formk div input {
  width: 87%;
  padding-left: 18px;
  font-size: 19px;
  font-weight: 400;
}
.formk div span.passwords {
  margin-right: 20px;
}
.btnk {
  width: 100%;
  text-align: center;
  margin-top: 79px;
}
.btnk button {
  width: 450px;
  height: 69px;
  background: linear-gradient(0deg, #F49E37, #F2BF7E);
  border-radius: 5px;
  font-size: 25px;
  font-weight: 300;
  text-align: center;
  line-height: 69px;
}
</style>