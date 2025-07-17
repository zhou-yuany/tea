const locationKey = '3QJBZ-RN2W3-C7L3D-Y7OVJ-EZITF-DDFZP'
// const urlPrefix = 'http://192.168.0.111:9988/transport'
// const urlPrefix = 'http://119.3.209.36:8088/transport'
// const urlPrefix = 'https://yungeche.xinjie-tec.cn/transport'
// const urlPrefix = 'http://192.168.0.111:9988/transport'
// const urlPrefix = 'https://yungeche.yinghai-tec.com/transport'
// 正式版
// const urlPrefix = 'https://yungeche.ddpai.top/transport'
// 测试版
const urlPrefix = 'https://yunche.ddpai.top/transport'

const applicationName = '运个车小程序'
//let userId = undefined
let userId = 4  // 临时变量

//let userObj = {type: 2}
let userObj = {}

exports.data = {
  urlPrefix
  , locationKey
  , applicationName
}
exports.setUserId = function(id){
  userId = id
}
exports.getUserId = function(){
  return userId
}
exports.setUser = function (user){
  userObj = user
}
exports.getUser = function (){
  return userObj
}