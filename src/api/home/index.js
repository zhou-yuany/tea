import request from '@/utils/request'

export default {

  // 登陆
  login(user) {
    return request({
      url: `/admins/portraitLogin`,
      method: 'post',
      data: user
    })
  },
  // 查询商品（带分类）
  getCateAndGoods(adminId) {
    return request({
      url: `/shopToGoods/getCateAndGoods/${adminId}`,
      method: 'get',
    })
  },
  // 查询所有规格参数
  getStandards(adminId) {
    return request({
      url: `/param/getStandards/${adminId}`,
      method: 'get',
    })
  },

  // 点击商品直接制作
  startOrders(orderNo, type, adminId, goodsId, params) {
    return request({
      url: `/orders/startOrders/${orderNo}/${type}/${adminId}/${goodsId}`,
      method: 'post',
      data: params
    })
  },
  // 茶饮机器查询订单列表 带分类
  getOrderList(adminId) {
    return request({
      url: `/orders/getOrderList/${adminId}`,
      method: 'get',
      params: {}
    })
  },
  // 根据订单尾号后六位查询订单详情
  ordersInfoBy(orderNo, adminId) {
    return request({
      url: `/orders/getOrdersInfoBy/${orderNo}/${adminId}`,
      method: 'get',
    })
  },
  // 根据订单id查询订单详情
  ordersInfo(id, adminId) {
    return request({
      url: `/orders/getOrdersInfo/${id}/${adminId}`,
      method: 'get',
    })
  },
  // 订单商品按照规格参数开始制作
  makeParam(id, params) {
    return request({
      url: `/orders/makeParam/${id}`,
      method: 'post',
      data: params
    })
  },
  // 查询订单流水
  ordersFlowList(adminId, accountQuery) {
    return request({
      url: `/ordersFlow/getList/${adminId}`,
      method: 'post',
      data: accountQuery
    })
  },
// 根据流水查询做法
getMakeWay(ordersFlow) {
  return request({
    url: `/ordersFlow/getMakeWay`,
    method: 'post',
    data: ordersFlow
  })
},
getBatchList(adminId,keyword) {
  return request({
    url: `/batchUse/getShopBatchList/${adminId}`,
    method: 'get',
    params: {keyword: keyword}
  })
},
// 产品配方
getGoodsBatchings(adminId,goodsBatchQuery) {
  return request({
    url: `/goodsToBatch/getGoodsBatchings/${adminId}`,
    method: 'post',
    data: goodsBatchQuery
  })
},
// 查询 商品配料
getGoodsBatchInfo(goodsId,sizeId,sugarId,temperatureId,adminId) {
  return request({
    url: `/goodsToBatch/getGoodsBatchInfo/${goodsId}/${sizeId}/${sugarId}/${temperatureId}/${adminId}`,
    method: 'get',
  })
},

  // 查询全部杯型
  getCupSizes() {
    return request({
      url: `/param/getCupSizes`,
      method: 'get',
    })
  },
    // 调用设备 第一次 获取时间
    doOrder(outletObj) {
      return request({
        url: `/orders/doOrder`,
        method: 'post',
        data: outletObj
      })
    },

    // 调用设备 第二次
    doOrder1(outletObj) {
      return request({
        url: `/orders/doOrder1`,
        method: 'post',
        data: outletObj
      })
    },

      // 
      getUrl(url) {
    return request({
      url: `/orders/getUrl`,
      method: 'get',
      params:{url}
    })
  },

  // 添加商家配料
insertData8(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/batchUse/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},
// 修改商家配料
updateData8(id,obj){
  return request({
      url: `/batchUse/updateData/${id}`,
      method: 'post',
      data: obj
  })
},
// 删除商家商品
deleteBatch(id){
  return request({
      url: `/batchUse/${id}`,
      method: 'delete'
  })
},
// 查询 商家配料详情
getBatchInfo(id) {
  return request({
    url: `/batchUse/getBatchInfo/${id}`,
    method: 'get',
  })
},

 // 查询机器
 getMachineList(adminId) {
  return request({
    url: `/batchUse/getMachineList/${adminId}`,
    method: 'get',
  })
},

  // 清洗机器
  clearMachine(adminId, equipmentId) {
    return request({
      url: `/batchUse/clearMachine/${adminId}`,
      method: 'post',
      params: {equipmentId}
    })
  },
   // 排放空气
   dischargeAir(adminId, equipmentId) {
    return request({
      url: `/batchUse/dischargeAir/${adminId}`,
      method: 'post',
      params: {equipmentId}
    })
  },
// *
  // *********************用户详情

  // 多图上传
  uploadCase(file, openid) {
    return request({
      url: `/users/uploadCase/${openid}`,
      method: 'post',
      data: file,
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
  },

  // 查询物料消耗
  batchUseList(adminId, accountQuery) {
    return request({
      url: `/batchOrderUse/shopBatchUseList/${adminId}`,
      method: 'post',
      data: accountQuery
    })
  },
}

