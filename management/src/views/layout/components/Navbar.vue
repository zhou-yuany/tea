<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger
      :toggle-click="toggleSideBar"
      :is-active="sidebar.opened"
      class="hamburger-container"
    />
    <breadcrumb />
    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <span v-if="showWarnning" style="color: red; margin-right: 10px"
          >余额不足五百，请充值
        </span>
        <span>店铺: {{ name }} </span>
        <i class="el-icon-caret-bottom" />
      </div>

      <el-dropdown-menu slot="dropdown" class="user-dropdown">
        <router-link class="inlineBlock" to="/">
          <el-dropdown-item> 首页 </el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span style="display: block" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import { createSocket2 } from "../../websocket/websocket.js";

export default {
  components: {
    Breadcrumb,
    Hamburger,
  },
  data() {
    return {
      showWarnning: false,
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "name", "roles", "balance", "divideAccounts"]),
  },
  created() {
    if (this.balance <= 0 && this.divideAccounts == 1) {
      this.showWarnning = true;
    } else {
      this.showWarnning = false;
    }

    createSocket2();
    this.getsocketData = (e) => {
      // 创建接收消息函数
      let data = e && e.detail.data;
      if (data) {
        if (data != "ping" && JSON.parse(data).message == "余额不足") {
          this.showWarnning = true;
        }
        if (data != "ping" && JSON.parse(data).message == "您有新订单啦") {
          this.showWarnning = false;
        }
      }
    };
    // 注册监听事件
    window.addEventListener("onmessageWS2", this.getsocketData);
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("ToggleSideBar");
    },
    logout() {
      this.$store.dispatch("LogOut").then(() => {
        localStorage.clear();
        location.reload(); // 为了重新实例化vue-router对象 避免bug
      });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      line-height: initial;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>

