// pages/carmodel/carmodel.js
// 获取应用实例
const app = getApp()
const api = require('../../utils/api')
const context = require("../../utils/context")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    urlPrefix: app.globalData.urlPrefix,
    list: [],
    allList: [],
    search: '',
    modelFlag: false,
    brandName: ''
  },
  // pinpai
  bindBrand: function (e){
    this.setData({
      brandName: e.detail.value
    })
  
},
// 取消弹窗
cancelBrand(){
  this.setData({
    modelFlag: false
  })
},
// 添加品牌
confirmBrand(){
  let that = this;
  let name = this.data.brandName;
  console.log(name)
  //空格或者全部是空格
  var regu = "^[ ]+$" 
  var re = new RegExp(regu)
  if(!name || re.test(name)){
    wx.showToast({
      title: '请输入有效的品牌',
      icon: 'none',
      duration: 2000
    })
    return
  }else{
      api.post("/car/addBrand", {name}).then((result) => {
        if(result){
          let size = -1;
          let detail = -1;
          let brand = result;
          const eventChannel = this.getOpenerEventChannel()
          eventChannel.emit('setData', {brand, size, detail});
          wx.navigateBack({
            delta: 1,
          })
        }else{
          wx.showToast({
            title: '品牌已存在，请从列表中选择',
            icon: 'none',
            duration: 3000,
            success: function () {
              setTimeout(function () {
                that.setData({
                  modelFlag: false
                })
                
              }, 3000) //延迟时间
            }
          })
        }
      })
    }

},
  search: function(e){
    let search = e.detail.value;
    this.setData({search});
    this.filterList();
  },
  clearSearch: function(){
    this.setData({search: ''});
    this.filterList();
  },
  filterList: function(){
    let search = this.data.search;
    let allList = JSON.parse(JSON.stringify(this.data.allList));
    let newObj = {};
    let item = [];
    let obj = {};
    obj.name = "没有可选品牌？请点击输入!"
    item.push(obj)
    newObj.item = item
    newObj.letter = "#"
    allList.unshift(newObj);
    
    this.setData({list: allList.map(d=>{
      if(search){
        let items = d.item.filter(i=>i.name.indexOf(search) >= 0);
        if(items.length > 0){
          d.item = items;
          return d;
        }
        return undefined;
      }
      return d;
    }).filter(d=>d)})
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var this_ = this;
    api.get("/car/model/select/map").then(res=>{
      this_.setData({allList: res})
      this_.filterList();
    })
    // wx.request({
    //   url: 'http://api.besttool.cn/?c=Car&a=brandlist',
    //   method: 'post',
    //   header: {
    //     'content-type': 'application/x-www-form-urlencoded'
    //   },
    //   data: {
    //     appid: 1,
    //     secret: 'd90824a5a8224fd7bb4fdffd331c62aa'
    //   },
    //   success(res) {
    //     console.log(res);
    //     that.setData({ list: res.data.brandlist });
    //   }
    // })
  },

  chooseLetter(e) {
    this.setData({
      curLetter: null
    });
    var letter = e.currentTarget.dataset.letter;
    console.log(letter);

    // 查找对应的id
    var id = "#letter" + letter;
    const query = wx.createSelectorQuery()  //创建节点查询器 query
    query.select(id).boundingClientRect() //这段代码的意思是选择Id = id的节点，获取节点位置信息的查询请求
    query.selectViewport().scrollOffset()  ////这段代码的意思是获取页面滑动位置的查询请求
    query.exec(function (res) {
      // res[0].top       // id节点的上边界坐标
      // res[1].scrollTop // 显示区域的竖直滚动位置
      wx.pageScrollTo({
        scrollTop: res[0].top + res[1].scrollTop,
        duration: 300
      })
    })
  },

  //跳转车系
  goCarseries:function(e){
    let id = e.currentTarget.dataset.id
    if(id == undefined){
      this.setData({
        modelFlag: true
      })
    }else{
      const eventChannel = this.getOpenerEventChannel()
    wx.navigateTo({
      url: '/pages/carseries/carseries?modelId='+id,
      events: {
        setData: function(data){
          eventChannel.emit('setData', data);
        }
      }
    })
    }
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})