<template>
  <div id="app">
    <HeaderLogin v-if="$route.meta.alive" :name="name"></HeaderLogin>
    <router-view />
  </div>
</template>

<script>
import Api from "@/api/api";
import HeaderLogin from "./components/header.vue"
export default {
  name: "App",
  components: {
    HeaderLogin
  },
  data(){
    return{
      name: '',
      // 定时任务配置
      config: {
        time: '08:30:00', // 每天几点执行
        interval: 1, // 隔几天执行一次
        runNow: false, // 是否立即执行
        intervalTimer: '',
        timeOutTimer: ''
      }
    }
  },
  beforeDestroy () {
    // 清除任务定时器
    // clearInterval(this.config.intervalTimer)
    // 清除定时器timeout
    // clearTimeout(this.config.timeOutTimer)
  },
  created() {
    this.name = localStorage.getItem("username");
    // this.setTimedTask();
  },
  methods: {
    // 设置定时任务
    setTimedTask() {
      // 默认为false，true为立即触发，看你的需求，如果设置为true则立刻运行任务函数
      if (this.config.runNow) {
        this.initBusinessFn()
      }
      // 获取下次要执行的时间，如果执行时间已经过了今天，就让把执行时间设到明天的按时执行的时间
      const nowTime = new Date().getTime()
      const timePoint = this.config.time.split(':').map((i) => parseInt(i))
 
      let recent = new Date().setHours(...timePoint) // 获取执行时间的时间戳
 
      if (recent <= nowTime) {
        recent += 24 * 60 * 60 * 1000
      }
      // 要执行的时间减去现在的时间，就是程序要多少秒之后执行
      const doRunTime = recent - nowTime
      this.config.timeOutTimer = setTimeout(this.setTimer, doRunTime)
 },
 
 
   // 设置定时器
    setTimer () {
      // console.log('进入定时器了')
      // 这里是将在你设置的时间点执行你的业务函数
      this.getCount()
      // 每隔多少天再执行一次，这里设置的是24小时
      const intTime = this.config.interval * 24 * 60 * 60 * 1000
      this.config.intervalTimer = setInterval(this.getCount, intTime)
    },
 




  },
};
</script>

<style>
</style>
