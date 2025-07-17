// pages/productxq/productxq.js
// 获取应用实例
const app = getApp()
const theme = require("../../utils/themeSystem")
const {
    config
} = require('../../utils/config')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        theme:wx.getStorageSync('systemTheme'),
        config: config,
        statusBarHeight: app.globalData.statusBarHeight,
        cateId: '',
        goodsId: '',
        shopId: 0,
        info: {},
        curSelect: '',
        addPrice: 0,
        curParam: false,
        normsList: [{
            name: '中杯'
        }, {
            name: '大杯'
        }],
        currentTaba: [],
        currentTabb: [],
        currentTabc: 0,
        minusStatus: true, // 购物车数量
        goodsCount: 1,
        totalPrice: 0
    },

    goBack: function () {
        wx.navigateBack({
            delta: -1,
        })
    },

    //选择规格
    clickOn: function (e) {
        let index = e.currentTarget.dataset.currenta;
        let paramId = e.currentTarget.dataset.paramid;
        let paramCate = e.currentTarget.dataset.cate;
        let param = e.currentTarget.dataset.param;

        let currentTabb = this.data.currentTabb;
        if (['currentTabb[' + index + ']'] == e.currentTarget.dataset.currenta) {
            return;
        }
        let curSelect = this.data.curSelect;
        let arr = curSelect.split('，');
        arr[index] = param;
        let newArr = arr.join('，');

        let totalPrice = 0;
        let sizeId = 0;
        let sugarId = 0;
        let temperatureId = 0;

        if (currentTabb.length > 0) {
            currentTabb.forEach(p => {
                if (p.paramCate == '规格') {
                    sizeId = p.paramId;
                }
                if (p.paramCate == '糖度') {
                    sugarId = p.paramId;
                }
                if (p.paramCate == '温度') {
                    temperatureId = p.paramId;
                }
            })
        }
        if (paramCate == "规格") {
            sizeId = paramId
        }
        if (paramCate == "糖度") {
            sugarId = paramId
        }
        if (paramCate == "温度") {
            temperatureId = paramId
        }
        let newParams = this.data.paramGoodsList.filter(param => param.sizeId == sizeId && param.sugarId == sugarId && param.temperatureId == temperatureId);
        totalPrice += newParams[0].price;
        this.setData({
            ['currentTabb[' + index + '].index']: e.currentTarget.dataset.currentb,
            ['currentTabb[' + index + '].paramCate']: paramCate,
            ['currentTabb[' + index + '].param']: param,
            ['currentTabb[' + index + '].paramId']: paramId,
            curSelect: newArr,
            totalPrice: totalPrice
        });
        // this.setData({
        //   addPrice: param.indexOf('大杯') > -1 || this.data.curSelect.indexOf('大杯') > -1 ? true : false
        // })

    },

    //选择型号
    clickTwo: function (e) {
        if (this.data.currentTabb == e.currentTarget.dataset.currentb) {
            return;
        }
        this.setData({
            currentTabb: e.currentTarget.dataset.currentb
        })
    },

    //数量增加
    addNum() {
        var goodsCount = this.data.goodsCount;
        goodsCount++;
        this.setData({
            goodsCount: goodsCount,
            minusStatus: false
        })
    },


    //数量减少
    minusNum: function () {
        var goodsCount = this.data.goodsCount;
        if (goodsCount > 1) {
            goodsCount--;
        }
        var minusStatus = goodsCount <= 1 ? true : false;
        this.setData({
            goodsCount: goodsCount,
            minusStatus: minusStatus
        })
    },

    inputGoodsCount(e) {
        this.setData({
            goodsCount: e.detail.value
        })
    },
    confirmGoodsCount() {
        this.setData({
            goodsCount: this.data.goodsCount < 1 ? 1 : this.data.goodsCount
        })
    },

    //加入购物车
    addShop: function () {
        let obj = {};
        let currentTabb = this.data.currentTabb;
        let totalPrice = this.data.totalPrice;
        let paramIds = currentTabb.map(item => {
            return item.paramId
        })
        // let price = this.data.info.price;
        let goodsCount = this.data.goodsCount;
        let curSelect = this.data.curSelect;
        obj.price = totalPrice;
        obj.goodsCount = goodsCount;
        obj.curSelect = curSelect;
        // obj.param = currentTabb;
        obj.goodsId = this.data.goodsId;
        obj.url = this.data.info.url;
        obj.checked = true;
        obj.name = this.data.info.name;
        obj.introduce = this.data.info.introduce;
        obj.paramIds = paramIds;
        let pages = getCurrentPages();
        let prevPage = pages[pages.length - 2];
        prevPage.setData({
            temporary: obj,
            fromSelect: true
        });
        wx.navigateBack({
            url: '../food/food',
        })
    },

    // 商品详情及规格
    getGoodsAndParam(shopId, cateId, goodsId) {
        let that = this;
        wx.request({
            url: `${config}/shopToGoods/getGoodsInfoAndParam`,
            method: 'GET',
            data: {
                shopId: shopId,
                cateId: cateId,
                goodsId: goodsId
            },
            header: {
                'Content-Type': 'application/json'
            },
            success(res) {
                if (res.data.code == 20000) {
                    that.setData({
                        info: res.data.data.info,
                        paramGoodsList: res.data.data.list
                    });
                    let paramCateList = res.data.data.info.paramCateList;
                    let totalPrice = 0;
                    let sizeId = 0;
                    let sugarId = 0;
                    let temperatureId = 0;
                    if (paramCateList.length > 0) {
                        for (var i = 0; i < paramCateList.length; i++) {
                            let str = '';
                            if (paramCateList[i].paramList[0].isRecommend == 1) {
                                str = that.data.curSelect + paramCateList[i].paramList[0].name + '（推荐）' + '，'
                            } else {
                                str = that.data.curSelect + paramCateList[i].paramList[0].name + '，'
                            }
                            if (paramCateList[i].name == '规格') {
                                sizeId = paramCateList[i].paramList[0].id;
                            }
                            if (paramCateList[i].name == '糖度') {
                                sugarId = paramCateList[i].paramList[0].id;
                            }
                            if (paramCateList[i].name == '温度') {
                                temperatureId = paramCateList[i].paramList[0].id;
                            }

                            that.setData({
                                ['currentTabb[' + i + '].index']: parseInt(0),
                                ['currentTabb[' + i + '].paramCate']: paramCateList[i].name,
                                ['currentTabb[' + i + '].param']: paramCateList[i].paramList[0].name,
                                ['currentTabb[' + i + '].paramId']: paramCateList[i].paramList[0].id,
                                curSelect: str,
                            })

                        }
                        let newParams = res.data.data.list.filter(param => param.sizeId == sizeId && param.sugarId == sugarId && param.temperatureId == temperatureId);
                        totalPrice += newParams[0].price;
                        that.setData({
                            totalPrice: totalPrice,
                            curSelect: that.data.curSelect.slice(0, that.data.curSelect.length - 1)
                        })

                    }
                } else {
                    wx.showToast({
                        title: '商品已下架',
                        icon: 'none',
                        duration: 1500,
                        success(data) {
                            setTimeout(function () {
                                wx.navigateBack({
                                    delta: -1,
                                })
                            }, 1000)

                        },
                    })


                }

            }
        });
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        if (options.shopId, options.cateId && options.goodsId) {
            this.setData({
                cateId: options.cateId,
                goodsId: options.goodsId,
                shopId: options.shopId,
            })
            if (options.curSelect) {
                this.setData({
                    curParam: true,
                })
                this.getGoodsAndParam(options.shopId, options.cateId, options.goodsId)
            } else {
                this.setData({
                    curParam: false,
                })
                this.getGoodsAndParam(options.shopId, options.cateId, options.goodsId)
            }

        }


    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {
      let that = this;
      if (wx.getStorageSync('selectColor')) {
        theme.setTheme(wx.getStorageSync('selectColor'));
        setTimeout(function () {
          that.setData({
            theme: wx.getStorageSync('systemTheme')
          })
        }, 300)
      }
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})