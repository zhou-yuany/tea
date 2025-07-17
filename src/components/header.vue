<template>
  <div class="headerk white_bg" ref="getheaderkheight">
    <div class="header_l white lightblue_bg" @click="goHome()">
      茶饮机器系统
    </div>
    <div class="timek">{{ currentDateTime }}</div>
    <div v-if="isWarnning" class="flicker" @click="showFeed()">
      <div>警</div>
    </div>
    <div class="header_r">
      <audio ref="audioBatch" :src="mp3Batch"></audio>
      <div class="wifik"><img src="../../static/wifi.png" alt="" /></div>
      <div class="piczu">
        <img src="../../static/notice.png" alt="" />
        <img src="../../static/user.png" alt="" />
        <!-- <img src="../../static/log-out.png" alt="" @click="logout()" /> -->
      </div>
    </div>
    <!-- 警示弹窗 -->
    <div class="grayc" v-if="isFeed">
      <div class="white_bg feedk">
        <div class="feed_title">
          <p>补料单</p>
          <img src="/static/x.png" alt="" @click="goQx()" />
        </div>
        <div class="feed_jiequ">
          <block v-for="(item, index) in paramlist" :key="index">
            <div class="feed_pfname">{{ item }}</div>
          </block>
        </div>
        <div class="feed_closebtn">
          <button type="button" class="white_bg" @click="finishHandle()">
            补料完成
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
      
<script>
import { defineComponent } from "vue";

import {
  createSocket,
  createSocket2,
  sendWSPush,
  closeWs,
  closeWs2,
} from "../views/websocket/websocket.js";
export default defineComponent({
  name: "HeaderLogin",
  props: {},
  data() {
    return {
      // currentDateTime: this.getCurrentDateTime(),
      currentDateTime: null,
      time1: null,
      isFeed: false,
      isWarnning: false,
      paramlist: [],
      mp3Batch: require('../../static/warnning.mp3'),
      feedList: [
        {
          name: "糖",
        },
        {
          name: "茉莉茶",
        },
      ],
    };
  },
  created() {
    createSocket();
    createSocket2();
    this.getsocketData = (e) => {
      // 创建接收消息函数
      let data = e && e.detail.data;
      if (data) {

        if (data != "ping" && JSON.parse(data).message.includes("不足")) {
          let str = JSON.parse(data).message.replace("不足","");
          this.paramlist = str.split(",");
          this.isWarnning = true;
          // 发出配料不足警告
          // this.playVoice(JSON.parse(data).message + "，请及时添加");
     
        }
        if (data != "ping" && JSON.parse(data).message == "禁用") {
          localStorage.setItem("balance", -1);
        }
        // if (data != "ping" && JSON.parse(data).message == "您有新订单啦") {
        //   this.playVoice("您有新的订单，请及时处理");
        // }
      }
    };
    // 注册监听事件
    window.addEventListener("onmessageWS", this.getsocketData);
    // 注册监听事件
    window.addEventListener("onmessageWS2", this.getsocketData);

    this.time1 = setInterval(() => {
      const now = new Date();
      const year = now.getFullYear();
      const month = this.padNumber(now.getMonth() + 1); // 月份是从0开始的
      const day = this.padNumber(now.getDate());
      const hours = this.padNumber(now.getHours());
      const minutes = this.padNumber(now.getMinutes());
      const seconds = this.padNumber(now.getSeconds());
      this.currentDateTime = `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
    }, 1000);
  },
  methods: {
    playBatch(){
  const audioBatch = this.$refs.audioBatch;
    audioBatch.play();
},
    // 语音播报
    playVoice(message) {
      const synth = window.speechSynthesis;
      const speech = new SpeechSynthesisUtterance();
      speech.text = message; // 文字内容
      speech.lang = "zh-CN"; // 使用的语言:中文
      speech.volume = 50; // 声音音量：1
      speech.rate = 1; // 语速：1
      speech.pitch = 1; // 音高：1
      synth.speak(speech); // 播放
    },
    showFeed() {
      this.isFeed = true;
    },
    goQx() {
      this.isFeed = false;
    },
    finishHandle() {
      this.isFeed = false;
      this.isWarnning = false;

    },

    getCurrentDateTime() {
      const now = new Date();
      const year = now.getFullYear();
      const month = this.padNumber(now.getMonth() + 1); // 月份是从0开始的
      const day = this.padNumber(now.getDate());
      const hours = this.padNumber(now.getHours());
      const minutes = this.padNumber(now.getMinutes());
      const seconds = this.padNumber(now.getSeconds());
      return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
    },
    padNumber(num) {
      return num < 10 ? "0" + num : num;
    },

    logout() {
      localStorage.clear();
      if (this.time1) {
        clearInterval(this.time1);
      }
      this.$router.push({ path: "/Login" });
    },

    goHome() {
      this.$router.push({ path: "/Index" });
    },
  },
  mounted() {
    let touheight = this.$refs.getheaderkheight.offsetHeight;
    localStorage.setItem("touheight", touheight);
  },
});
</script>
    
<style scoped>
/* 竖屏时的样式 */
@media screen and (orientation: portrait) {
.headerk {
  width: 100%;
  height: 1rem;
  margin: auto;
  box-shadow: 0 1px 0 0 rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header_l {
  width: 2.4rem;
  height: 1rem;
  font-size: 0.28rem;
  line-height: 1rem;
  text-align: center;
}
.timek {
  font-size: 0.24rem;
  margin-left: 0.36rem;
  margin-right: auto;
}
/* 闪烁警示 */
.flicker {
  width: 0.6rem;
  height: 0.6rem;
  border-radius: 50%;
  background-color: rgba(234, 0, 0, 0.5);
  animation: custom-blink 3s infinite;
  margin-right: 0.3rem;
  position: relative;
}
.flicker div {
  width: 0.4rem;
  height: 0.4rem;
  background-color: #ea0000;
  border-radius: 50%;
  color: #ffffff;
  text-align: center;
  font-size: 0.2rem;
  line-height: 0.4rem;
  position: absolute;
  top: 0.1rem;
  left: 0.1rem;
}
@keyframes custom-blink {
  0% {
    opacity: 1;
  }
  15% {
    opacity: 0.8;
  }
  25% {
    opacity: 0.5;
  }
  50% {
    opacity: 0.2;
  }
  75% {
    opacity: 0.5;
  }
  85% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}
.header_r {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-right: 0.17rem;
}
.wifik {
  width: 0.85rem;
  height: 0.4rem;
}
.wifik img {
  width: 100%;
  height: 100%;
  display: block;
}
.piczu {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.piczu img {
  width: 0.24rem;
  height: 0.24rem;
  display: block;
  margin-left: 0.26rem;
}
/* 警示弹窗 */
.feedk {
  width: 6.32rem;
  height: 5.4rem;
  border-radius: 0.16rem;
  position: fixed;
  top: 50%;
  left: 50%;
  margin-top: -3.29rem;
  margin-left: -3.16rem;
  z-index: 100;
}
.feed_title {
  width: 100%;
  height: 0.76rem;
  margin: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.2rem;
}
.feed_title p {
  font-size: 0.26rem;
  padding-left: 0.2rem;
}
.feed_title img {
  width: 0.28rem;
  height: 0.28rem;
  display: block;
  margin-right: 0.2rem;
}
.feed_jiequ {
  width: 100%;
  height: 3rem;
  overflow: hidden;
  overflow-y: scroll;
}
.feed_pfname {
  padding-left: 1.05rem;
  font-size: 0.24rem;
  margin-bottom: 0.1rem;
}
.feed_closebtn {
  width: 100%;
  height: 0.44rem;
  margin: auto;
  text-align: center;
  margin-top: 0.48rem;
}
.feed_closebtn button {
  width: 1rem;
  height: 0.44rem;
  border-radius: 0.04rem;
  border: 1px solid #666666;
  font-size: 0.2rem;
}
}
/* 横屏时的样式 */
@media screen and (orientation: landscape) {
  .headerk {
  width: 100%;
  height: .7rem;
  margin: auto;
  box-shadow: 0 1px 0 0 rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.header_l {
  width: 2.2rem;
  height: .7rem;
  font-size: 0.24rem;
  line-height: .7rem;
  text-align: center;
}
.timek {
  font-size: 0.20rem;
  margin-left: 0.36rem;
  margin-right: auto;
}
/* 闪烁警示 */
.flicker {
  width: 0.6rem;
  height: 0.6rem;
  border-radius: 50%;
  background-color: rgba(234, 0, 0, 0.5);
  animation: custom-blink 3s infinite;
  margin-right: 0.3rem;
  position: relative;
}
.flicker div {
  width: 0.4rem;
  height: 0.4rem;
  background-color: #ea0000;
  border-radius: 50%;
  color: #ffffff;
  text-align: center;
  font-size: 0.2rem;
  line-height: 0.4rem;
  position: absolute;
  top: 0.1rem;
  left: 0.1rem;
}
@keyframes custom-blink {
  0% {
    opacity: 1;
  }
  15% {
    opacity: 0.8;
  }
  25% {
    opacity: 0.5;
  }
  50% {
    opacity: 0.2;
  }
  75% {
    opacity: 0.5;
  }
  85% {
    opacity: 0.8;
  }
  100% {
    opacity: 1;
  }
}
.header_r {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-right: 0.17rem;
}
.wifik {
  width: 0.85rem;
  height: 0.4rem;
}
.wifik img {
  width: 100%;
  height: 100%;
  display: block;
}
.piczu {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.piczu img {
  width: 0.24rem;
  height: 0.24rem;
  display: block;
  margin-left: 0.26rem;
}
/* 警示弹窗 */
.feedk {
  width: 6.32rem;
  height: 5.4rem;
  border-radius: 0.16rem;
  position: fixed;
  top: 50%;
  left: 50%;
  margin-top: -3.29rem;
  margin-left: -3.16rem;
  z-index: 100;
}
.feed_title {
  width: 100%;
  height: 0.76rem;
  margin: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.2rem;
}
.feed_title p {
  font-size: 0.26rem;
  padding-left: 0.2rem;
}
.feed_title img {
  width: 0.28rem;
  height: 0.28rem;
  display: block;
  margin-right: 0.2rem;
}
.feed_jiequ {
  width: 100%;
  height: 3rem;
  overflow: hidden;
  overflow-y: scroll;
}
.feed_pfname {
  padding-left: 1.05rem;
  font-size: 0.24rem;
  margin-bottom: 0.1rem;
}
.feed_closebtn {
  width: 100%;
  height: 0.44rem;
  margin: auto;
  text-align: center;
  margin-top: 0.48rem;
}
.feed_closebtn button {
  width: 1rem;
  height: 0.44rem;
  border-radius: 0.04rem;
  border: 1px solid #666666;
  font-size: 0.2rem;
}
}
</style>