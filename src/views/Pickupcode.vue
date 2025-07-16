<template>
  <div class="pickup_box">
    <div class="lightblue_bg white ffan" @click="goBack()">返回</div>
    <div class="pickupk white_bg">
      <input
        type="text"
        placeholder="请输入订单号后六位"
        maxlength="6"
        v-model="orderNo"
        placeholder-class="placeholder_color"
      />
      <button type="button" class="lightblue_bg white" @click="onSure(1)">
        确定
      </button>
    </div>
    <ul class="numk">
      <li
        v-for="(item, index) in items"
        :key="index"
        @click="handleClick(item)"
        :class="item == 0 ? 'zero' : ''"
      >
        {{ item }}
      </li>
      <!-- <li class="zero">0</li> -->
      <li class="clearkk" @click="clearNo"><img src="../../static/clear.png" alt="" /></li>
    </ul>
  </div>
</template>
  
<script>
import Api from "@/api/api";
import { defineComponent } from "vue";
import homeApi from "@/api/home/index";
export default defineComponent({
  data() {
    return {
      items: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0],
      orderNo: "",
    };
  },
  created() {},
  methods: {
    getOrdersInfoBy(index) {
      let adminId = localStorage.getItem("adminId");
      homeApi.ordersInfoBy(this.orderNo, adminId).then((res) => {
        if (res.code == 20000) {

          this.$router.push({
            path: "/Detail",
            query: { current: index, orderNo: this.orderNo },
          });
        }
      });
    },
    clearNo() {
      this.orderNo = this.orderNo.slice(0, -1);
    },
    handleClick(data) {
      if ((this.orderNo + data).length <= 6) {
        this.orderNo = this.orderNo + data;
      }
    },

    goBack() {
      this.$router.push({
        path: "/Index",
      });
    },
    onSure(index) {
      if (this.orderNo.length != 6) {
        this.$message({
          type: "warnning",
          message: "请输入订单号后六位",
        });
      } else {
        this.getOrdersInfoBy(index);
      }
    },
  },
  mounted() {},
});
</script>
    
<style scoped>
@import url("../assets/pickup.css");
</style>