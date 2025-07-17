const context = require("context.js")
const util = require("util.js")
const urlPrefix = context.data.urlPrefix
const cache = {}
const cacheTime = {}

/**
 * 统一调用接口
 */
function request(url,data,method, hideLoading) {
  if(url === '/car/type/select'){
    let data = cache[url]
    let dataTime = cacheTime[url]
    if(data && dataTime != util.now()){
      return new Promise((resolve, reject) => {
        resolve(data)
      });
    }
  }
  return new Promise((resolve, reject) => {
      if(!hideLoading){
        wx.showLoading({ title: '加载中...', duration: 6000})
      }
      let header = {'Content-Type': 'application/json;charset=UTF-8'}
      if(context.getUserId()){
        header['user-id'] = context.getUserId()
      }else{
        header['user-id'] = -1
      }
      wx.request({
          url: urlPrefix + url,
          data: data || {},
          header,
          method: method || 'GET',
          dataType: 'json',
          responseType: 'text',
          success: res => {
              if(!hideLoading){
                wx.hideLoading()
              }
              let statusCode = res.statusCode
              if(statusCode >=200 && statusCode < 400){
                  if(url === '/car/type/select'){
                    cache[url] = res.data
                    cacheTime[url] = util.now()
                  }
                  resolve(res.data)
              }else{
                if(res.data && res.data.message){
                  wx.showToast({
                      title: res.data.message,
                      icon: 'none',
                      duration: 3000
                  })
                }
                reject(res)
              }
          },
          fail: res => {
              if(!hideLoading){
                wx.hideLoading()
              }
              wx.showToast({
                  title: '网络异常，请检查网络状态',
                  icon: 'none',
                  duration: 3000
              })
              reject(res)
          },
      })
  })
}
exports.urlPrefix = urlPrefix
exports.get = function (url, data, hideLoading) {
  return request( url , data , "GET" , hideLoading)
}
exports.post = function (url, data, hideLoading) {
  return request( url , data , "POST" , hideLoading)
}
exports.put = function (url, data, hideLoading) {
  return request( url , data , "PUT" , hideLoading)
}