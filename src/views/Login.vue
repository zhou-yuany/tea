<template>
  <div class="box">
    <!-- 背景图 -->
    <div class="circulara blue_bg"></div>
    <div class="circularb blue_bg"></div>
    <!-- 标题 -->
    <div class="subtitle">茶饮机器系统</div>
    <div class="formk white_bg">
      <div class="bt">用户登录</div>
      <div class="tell" style="border-bottom: 0">
        <input
          v-model="username"
          placeholder="请输入账户名"
          placeholder-class="placeholder_color"
        />
      </div>
      <div class="tell">
        <input
          type="password"
          v-model="password"
          placeholder="请输入密码"
          placeholder-class="placeholder_color"
        />
      </div>
      <div class="darkgray checkboxk">
        <div>
          <el-checkbox
            @change="selectType"
            v-model="selectValue"
            :checked="selectValue"
          ></el-checkbox
          ><span>记住密码</span>
        </div>
      </div>
      <div class="btnk">
        <button :loading="loading" type="button" class="white darkblue_bg" @click="goHome()">
          授权登录
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import homeApi from "@/api/home/index";
import { defineComponent } from "vue";
import { setCookie, getCookie, delCookie } from "../utils/index";
import store from '../store'

export default defineComponent({
  data() {
    return {
      isShow: false,
      username: "",
      password: "",
      selectValue: false,
      loading: false
    };
  },
  created() {
    if (getCookie("user") != "" && getCookie("pwd") != "") {
      this.username = getCookie("user");
      this.password = getCookie("pwd");
      this.selectValue = true;
    }
  },
  methods: {
    selectType(e) {
      // if(e && this.username != '' && this.password != ''){
      //   this.goHome()
      // }
      this.selectValue = e;
      if (e) {
        setCookie("user", this.username, 7);
        setCookie("pwd", this.password, 7);
      } else {
        delCookie("user");
        delCookie("pwd");
      }
    },

    goHome() {
      //   const reg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      // if (!reg.test(this.phone)) {
      //   this.$toast({
      //     message: "手机号码格式错误",
      //     position: "top",
      //   });
      //   return;
      // }
      if (this.username == "") {
        this.$toast({
          message: "请输入账号",
          position: "top",
        });
        return;
      }
      if (this.password == "") {
        this.$toast({
          message: "请输入密码",
          position: "top",
        });
        return;
      }

      let loginForm = {
        username: this.username,
        password: this.password,
      };
      homeApi.login(loginForm).then((res) => {
        this.loading = true
        if (res.code == 20000) {
   
          localStorage.setItem("divideAccounts", res.data.divideAccounts);
          localStorage.setItem("username", res.data.info.username);
          localStorage.setItem("adminId", res.data.info.id);
          localStorage.setItem("token", res.data.token);
          localStorage.setItem("shopId", res.data.shopId);
          localStorage.setItem("shopInfoId", res.data.shopInfoId);
          localStorage.setItem("balance", res.data.balance);
        
          this.loading = false;
          this.$router.push({ path: '/Home' })
          
        } else {
          this.$toast({
            message: res.message,
            position: "top",
          });
        }
      });
      // this.loading = true
      //     this.$store.dispatch('Login', loginForm).then(() => {
      //       this.loading = false;
      //       this.$router.push({ path: '/Home' })
      //     }).catch(() => {
      //       this.loading = false
      //     })
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
@import url("../assets/login.css");
</style>