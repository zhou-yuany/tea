<template>
  <div class="box">
    <div class="toubu_title">茶饮收银系统</div>
    <div class="kuang white_bg">
      <div class="subtitle">用户登录</div>
      <div class="formk">
        <div>
          <input v-model="username" maxlength="11" placeholder="请输入账号" placeholder-class="placeholder_color" />
        </div>
        <div>
          <input type="password" v-model="password" placeholder="请输入密码" placeholder-class="placeholder_color" />
        </div>
      </div>
      <div class="zd">
        <el-checkbox @change="selectType" v-model="selectValue" :checked="selectValue"></el-checkbox><span>记住密码</span>
      </div>
      <div class="btnk">
        <button type="button" class="white" @click="goHome()">登陆</button>
      </div>
    </div>
  </div>
</template>

<script>
import homeApi from "@/api/home/index";
import { defineComponent } from "vue";
import { setCookie, getCookie, delCookie } from "../utils/index";
export default defineComponent({
  name: 'Login',
  data() {
    return {
      isShow: false,
      username: "",
      password: "",
      selectValue: false
    };
  },
  created() {
    if (getCookie("user") != "" && getCookie("pwd") != "") {
        this.username = getCookie("user");
        this.password = getCookie("pwd");
        this.selectValue = true;
      }
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
    selectType(e){
        // if(e && this.username != '' && this.password != ''){
        //   this.goHome()
        // }
        this.selectValue = e;
        if(e){
          setCookie("user", this.username, 7);
          setCookie("pwd", this.password, 7);
        }else{
          delCookie("user");
          delCookie("pwd");
        }
    },

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
      let loginForm = {
         username: this.username, 
         password: this.password
      }
        homeApi
        .login(loginForm)
        .then((res) => {
          if (res.data.code == 20000) {
            localStorage.setItem("divideAccounts", res.data.data.divideAccounts);
            localStorage.setItem("username", res.data.data.info.username);
            localStorage.setItem("adminId", res.data.data.info.id);
            localStorage.setItem("token", res.data.data.token);
              this.$router.push({ path: "/Home" });
          }else{
            this.$toast({
              message: res.data.message,
              position: "top",
            });
          }
        });
      
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
  height: 100vh;
  margin: auto;
  background-image: url('../../static/login_bg.png');
  background-position: center;
  background-repeat: no-repeat;
  background-size: 100% 100%;
}
.toubu_title{
  width: 100%;
  text-align: center;
  font-size: 80px;
  color: #3D3D3D;
  margin-bottom: 78px;
  font-family: PingFangSCHeavy-Heavy!important;
  padding-top: 147px;
}
.kuang{
  width: 440px;
  height: 450px;
  margin: auto;
  border-radius: 14px;
  box-shadow: 0px 5px 10px 0px rgba(25, 74, 142, 0.11);
  padding-top: 107px;
}
.subtitle{
  font-size: 16px;
  text-align: center;
  margin-bottom: 35px;
}
.formk {
  width: 100%;
  height: auto;
  margin: auto;
}
.formk div {
  width: 290px;
  height: 40px;
  margin: auto;
  border: 0.5px solid #C0D2E2;
}
.formk div input {
  width: 100%;
  height: 38px;
  padding-left: 17px;
  font-size: 14px;
  font-weight: 400;
}
.zd{
  width: 290px;
  height: 53px;
  margin: auto;
  display: flex;
  align-items: center;
}
.zd span{
  font-size: 14px;
  margin-left: 7px;
  color: #A7ACBB;
}
.btnk {
  width: 100%;
  text-align: center;
}
.btnk button {
  width: 290px;
  height: 50px;
  border-radius: 2px;
  font-size: 16px;
  font-weight: 500;
  text-align: center;
  line-height: 50px;
  background-color: #3B90FB;
}
</style>