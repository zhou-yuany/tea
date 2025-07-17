const {
  config
} = require('./config')
// const state={
//     // 绿色系主题
//     green:`--secondColor:#F39D36;--themeColor:#A9D99D;--unThemeColor:#ECF6E2;--bgColor-main:linear-gradient(121deg, #A8D89C 0%, #DBF2C4 100%);--bgColor-second: linear-gradient(97deg, #F2BF7E 0%, #F28E76 98%);`,
//     // 红色系主题
//     red:`--secondColor:#f9e5ee;--themeColor:#ff0000;--bgColor-main:linear-gradient(121deg, #ff0000 0%, #ffffff 100%);--bgColor-second: linear-gradient(97deg, #f9e5ee 0%, #f9e5ee 98%);`,
// }
function setTheme(theme){
  let that = this;
  wx.request({
    url: `${config}/template/getTemplateColor`,
    method: 'GET',
    data: {
      color: theme
    },
    header: {
      'Content-Type': 'application/json'
    },
    success(res) {
      let colorInfo = res.data.data.info;
      wx.setStorageSync('systemTheme',colorInfo.color)
      return '`'+colorInfo.color+'`'
    }
  });
    // if(theme==1){
    //   wx.setStorageSync('systemTheme',state.green)
    //   wx.setStorageSync('systemThemeCode','green')
    //   return state.green
    // }else if(theme==2){
    //   wx.setStorageSync('systemTheme',state.red)
    //   wx.setStorageSync('systemThemeCode','red')
    //   return state.red
    // }
}
module.exports = {
    setTheme
}