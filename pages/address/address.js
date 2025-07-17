// pages/address/address.js
// 获取应用实例
const app = getApp()
const {
    config,mapKey
} = require('../../utils/config')
var QQMapWX = require('../../libs/qqmap-wx-jssdk.js');
// 实例化API核心类
var qqmapsdk = new QQMapWX({
    key: mapKey// 必填，填自己在腾讯位置服务申请的key
});
Page({

    /**
     * 页面的初始数据
     */
    data: {
        addressList: [],
        speed: 0,
        accuracy: 16,
        city: '',
        keyword: '',
        markers: [],
        covers: [],
        latitude: 38.849563,
        longitude: 121.515856
    },
    inputKeyword(e) {
        this.setData({
            keyword: e.detail.value.trim()
        });

    },
    searchMap() {
        var _this = this
        var allMarkers = []
        //通过wx.request发起HTTPS接口请求
        wx.request({
            //地图WebserviceAPI地点搜索接口请求路径及参数（具体使用方法请参考开发文档）
            url: 'https://apis.map.qq.com/ws/place/v1/search',
            method: 'GET',
            data: {
                page_index: 1,
                page_size: 20,
                boundary: 'region('+this.data.city+',0)',
                keyword: this.data.keyword,
                key: mapKey
            },
            header: {
                'Content-Type': 'application/json'
            },
            success(res) {
                var result = res.data
                var pois = result.data
                for (var i = 0; i < pois.length; i++) {
                    var title = pois[i].title
                    var lat = pois[i].location.lat
                    var lng = pois[i].location.lng

                    // let marker = {
                    //     id: i,
                    //     latitude: lat,
                    //     longitude: lng,
                    //     callout: {
                    //         // 点击marker展示title
                    //         content: title
                    //     }
                    // }
                    // allMarkers.push(marker)
                    // marker = null
                }
                // _this.setData({
                //     latitude: allMarkers[0].latitude,
                //     longitude: allMarkers[0].longitude,
                //     markers: allMarkers
                // })
                _this.getShopList(lat, lng);
            }
        })
    },
getShopList(latitude, longitude){
    let that = this;
    wx.request({
        url: `${config}/shop/getShopList`,
        method: 'GET',
        data: {},
        header: {
            'Content-Type': 'application/json'
        },
        success(res) {
            let list = res.data.data.list;
            let markers = [];
            list.forEach((item, index) => {
                let lat = item.lat;
                let lng = item.lng;
                item.distance = that.getDistances(lat, lng, latitude, longitude);
                let obj = {};
                obj.id = index;
                obj.latitude = item.lat;
                obj.longitude = item.lng;
                obj.iconPath = item.equipmentType == 1 ? '/static/mapico1.png' : '/static/mapico2.png';
                obj.width = 23;
                obj.height = 28;
                markers.push(obj);
            });
            let newArr = list.sort((a, b) => a.distance - b.distance);
            that.setData({
                addressList: newArr,
                latitude,
                longitude,
                markers
            })
        }
    });
},

    getShopInfo() {
        let that = this;
        wx.getLocation({
            isHighAccuracy: true, // 开启地图精准定位
            type: 'gcj02',
            success(res) {
                const latitude = res.latitude;
                const longitude = res.longitude;
                qqmapsdk.reverseGeocoder({
                    location: {
                        latitude: res.latitude,
                        longitude: res.longitude
                    },
                    success: function (res) {
                        that.setData({
                            city: res.result.ad_info.city
                        })
                    },
                    fail: function (error) {
                        console.error(error);
                    },
                    complete: function (res) {

                    }
                })
                wx.request({
                    url: `${config}/shop/getShopList`,
                    method: 'GET',
                    data: {},
                    header: {
                        'Content-Type': 'application/json'
                    },
                    success(res) {
                        let list = res.data.data.list;
                        let markers = [];
                        list.forEach((item, index) => {
                            let lat = item.lat;
                            let lng = item.lng;
                            item.distance = that.getDistances(lat, lng, latitude, longitude);
                            let obj = {};
                            obj.id = index;
                            obj.latitude = item.lat;
                            obj.longitude = item.lng;
                            obj.iconPath = item.equipmentType == 1 ? '/static/mapico1.png' : '/static/mapico2.png';
                            obj.width = 23;
                            obj.height = 28;
                            markers.push(obj);
                        });
                        let newArr = list.sort((a, b) => a.distance - b.distance);
                        that.setData({
                            addressList: newArr,
                            latitude,
                            longitude,
                            markers
                        })
                    }
                });

            },
        })


    },
    getDistances(lat1, lng1, lat2, lng2) {
        let EARTH_RADIUS = 6378.137; // 地球半径
        let radLat1 = lat1 * Math.PI / 180.0; //lat1 * Math.PI / 180.0=>弧度计算
        let radLat2 = lat2 * Math.PI / 180.0;
        let a = radLat1 - radLat2;
        let b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
        let s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000; // 输出为公里
        return s < 1 ? Number(s * 1000).toFixed(2)+'m' : s.toFixed(2)+'km'
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        var that = this;
        if (options.latitude) {
            this.setData({
                latitude: options.latitude,
                longitude: options.longitude,
                city: options.city
            })
            this.getShopList(options.latitude, options.longitude)
        } else {
            this.getShopInfo();
        }
    },

    //城市选择
    chooseCity: function () {
        wx.navigateTo({
            url: '../city/city',
        })
    },

    // 导航
    goNavigation: function (e) {
        let data = e.currentTarget.dataset;
        wx.getLocation({//获取当前经纬度
            type: 'gcj02', //返回可以用于wx.openLocation的经纬度，官方提示bug: iOS 6.3.30 type 参数不生效，只会返回 wgs84 类型的坐标信息
            success: function (res) {
            wx.openLocation({//?使用微信内置地图查看位置。
            latitude: Number(data.lat),//要去地点的纬度
            longitude: Number(data.lng),///要去地点的经度-地址
            scale: 18,
            name: data.name,//
            address: data.address,
            success() {
                console.log('地图打开成功');
              },
              fail(err) {
                console.error('地图打开失败', err);
              }
            })
            }
            })
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