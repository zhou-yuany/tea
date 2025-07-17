
let Socket = null
let setIntervalWesocketPush = null
let Socket2 = null
let setIntervalWesocketPush2 = null
let flag = window.location.href.indexOf('https') >= 0 ;
// let socketUrl = flag ? 'wss://teaws.yinghai-tec.com:8585?client=' : 'ws://47.94.56.51:8585?client=';
// let socketUrl = 'wss://teaws.yinghai-tec.com:8585?client=';
//     let newPath = '';
// let socketUrl = 'wss://tea.yinghai-tec.com:9220/platSocket/';

// const socketUrl = 'ws://localhost:9220/tea-server/platSocket/'  // socket连接地址
/**
 * 建立websocket连接
 * @param {string} url ws地址
 */
 export const createSocket = (url) => {
  // console.log('建立websocket连接')
  // Socket && Socket.close()
  Socket && Socket.close()
  if (Socket == null) {
    let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;;// 这里我是为了获取登录的用户信息
    if(shopId){
      // 第二个socket
      Socket = new WebSocket('wss://tea.yinghai-tec.com/tea-server/webSocket/'+shopId)
      Socket.onopen = onopenWS
      Socket.onmessage = onmessageWS
      Socket.onerror = onerrorWS
      Socket.onclose = oncloseWS
    }else {
      this.$message.error("websocket连接失败，未获取到账户信息")
    }
  } else {
    // console.log('websocket已连接')
  }
}
export const createSocket2 = (url) => {
  // console.log('建立websocket2连接')
  // Socket && Socket.close()
  Socket2 && Socket2.close()
  if (Socket2 == null) {
    let shopId = JSON.parse(localStorage.getItem("adminInfo")).id;;// 这里我是为了获取登录的用户信息
    if(shopId){
      // 第二个socket
      Socket2 = new WebSocket('wss://tea.yinghai-tec.com/tea-management/webSocket/'+shopId)
      Socket2.onopen = onopenWS2
      Socket2.onmessage = onmessageWS2
      Socket2.onerror = onerrorWS2
      Socket2.onclose = oncloseWS2
    }else {
      this.$message.error("websocket2连接失败，未获取到账户信息")
    }
  } else {
    // console.log('websocket2已连接')
  }
}

/**打开WS之后发送心跳 */
const onopenWS2 = () => {
  sendPing2()
}
const onopenWS = () => {
  sendPing()
}
 

/**连接失败重连 */
const onerrorWS2 = () => {
  Socket2.close()
  clearInterval(setIntervalWesocketPush2)
  if (Socket2.readyState !== 3) {
    Socket2 = null
    createSocket2()
  }
}
const onerrorWS = () => {
  Socket.close()
  clearInterval(setIntervalWesocketPush)
  if (Socket.readyState !== 3) {
    Socket = null
    createSocket()
  }
}
 

/**WS数据接收统一处理 */
const onmessageWS2 = e => {
  window.dispatchEvent(new CustomEvent('onmessageWS2', {
    detail: {
      data: e.data
    }
  }))
}
const onmessageWS = e => {
  window.dispatchEvent(new CustomEvent('onmessageWS', {
    detail: {
      data: e.data
    }
  }))
}
 

/**
 * 发送数据但连接未建立时进行处理等待重发
 * @param {any} message 需要发送的数据
 */
 const connecting2 = message => {
  setTimeout(() => {
    if (Socket2.readyState === 0) {
      connecting2(message)
    } else {
      // Socket.send(JSON.stringify(message))
      Socket2.send(message)
    }
  }, 30000)
}
const connecting = message => {
  setTimeout(() => {
    if (Socket.readyState === 0) {
      connecting(message)
    } else {
      // Socket.send(JSON.stringify(message))
      Socket.send(message)
    }
  }, 30000)
}
/**
 * 发送数据
 * @param {any} message 需要发送的数据
 */
 export const sendWSPush2 = message => {
  if (Socket2 !== null && Socket2.readyState === 3) {
    Socket2.close()
    createSocket2()
  } else if (Socket2.readyState === 1) {
    // Socket.send(JSON.stringify(message))
    Socket2.send(message)
  } else if (Socket2.readyState === 0) {
    connecting2(message)
  }
}
export const sendWSPush = message => {
  if (Socket !== null && Socket.readyState === 3) {
    Socket.close()
    createSocket()
  } else if (Socket.readyState === 1) {
    // Socket.send(JSON.stringify(message))
    Socket.send(message)
  } else if (Socket.readyState === 0) {
    connecting(message)
  }
}
 

/**断开重连 */
const oncloseWS2 = () => {
  // Socket = null
  clearInterval(setIntervalWesocketPush2)
  // console.log('websocket2已断开....正在尝试重连')
  if (Socket2.readyState !== 2) {
    Socket2 = null
    createSocket2()
  }
}
const oncloseWS = () => {
  // Socket = null
  clearInterval(setIntervalWesocketPush2)
  // console.log('websocket已断开....正在尝试重连')
  if (Socket.readyState !== 2) {
    Socket = null
    createSocket()
  }
}
 

export const closeWs2 =() =>{
  Socket2.close()
}
 
export const closeWs =() =>{
  Socket.close()
}

/**发送心跳
 * @param {number} time 心跳间隔毫秒 默认5000
 * @param {string} ping 心跳名称 默认字符串ping
 */
 export const sendPing2 = (time = 30000, ping = 'ping') => {
  clearInterval(setIntervalWesocketPush2)
  // Socket.send(JSON.stringify({"event":"heart"}))
  Socket2.send(ping)
  setIntervalWesocketPush2 = setInterval(() => {
    // Socket.send(JSON.stringify({"event":"heart"}))
    Socket2.send(ping)
  }, time)
}
export const sendPing = (time = 30000, ping = 'ping') => {
  clearInterval(setIntervalWesocketPush)
  // Socket.send(JSON.stringify({"event":"heart"}))
  Socket.send(ping)
  setIntervalWesocketPush = setInterval(() => {
    // Socket.send(JSON.stringify({"event":"heart"}))
    Socket.send(ping)
  }, time)
}