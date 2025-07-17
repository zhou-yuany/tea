import request from '@/utils/request'

export default {
  // 查询所有总分类
  getAllCates() {
    return request({
      url: `/categorizeAll/allList`,
      method: 'get',
    })
  },

  // 查询所有商家分类
  getCates(shopId) {
    return request({
      url: `/categorize/allList/${shopId}`,
      method: 'get',
    })
  },
  // 查询商家的全部商品
  getShopGoodsList(shopId) {
    return request({
      url: `/shopToGoods/getShopGoodsList/${shopId}`,
      method: 'get',
    })
  },

  // 查询授权商家的全部商品
  getAllGoods(shopId) {
    return request({
      url: `/shopToGoods/allList/${shopId}`,
      method: 'get',
    })
  },
  // 查询设备列表 分页
  getEquipmentShopList(page, limit, shopId, keyword) {
    return request({
      url: `/equipment/getEquipmentList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: { keyword: keyword }
    })
  },
   // 查询 设备详情
   getEquipmentInfo(id) {
    return request({
      url: `/equipment/getEquipmentInfo/${id}`,
      method: 'get',
    })
  },
  // 修改设备别名
  updateData12(id, obj) {
    return request({
      url: `/equipment/updateData/${id}`,
      method: 'post',
      data: obj
    })
  },

  // 查询所有总分类列表 分页
  getCateList(page, limit, shopId, keyword) {
    return request({
      url: `/categorize/getCateList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: { keyword: keyword }
    })
  },
  // 查询 分类详情
  getCateInfo(id) {
    return request({
      url: `/categorize/getCateInfo/${id}`,
      method: 'get',
    })
  },
  // 添加分类
  insertData0(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/categorize/insertData`,  // 飘号
      method: 'post',
      data: obj
    })
  },
  // 添加饿了么商品分类
  createCategory(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/ele/createCategory`,  // 飘号
      method: 'post',
      data: obj
    })
  },

  // 修改分类
  updateData0(id, obj) {
    return request({
      url: `/categorize/updateData/${id}`,
      method: 'post',
      data: obj
    })
  },
  // 修改饿了么商品分类
  updateCategory(obj) {
    return request({
      url: `/ele/updateCategory`,
      method: 'post',
      data: obj
    })
  },

  // 删除分类
  deleteCate(id) {
    return request({
      url: `/categorize/${id}`,
      method: 'delete'
    })
  },
  // 删除饿了么商品分类
  deleteEleCate(eleId, access_token, token_type, expires_in, refresh_token) {
    return request({
      url: `/ele/${eleId}`,
      method: 'delete',
      params: { access_token, token_type, expires_in, refresh_token }
    })
  },
  // 查询商家商品列表 分页
  getGoodsList(page, limit, shopId, keyword) {
    return request({
      url: `/shopToGoods/getGoodsList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: { keyword: keyword }
    })
  },

  // 查询 总商品详情
  getGoodsById(id) {
    return request({
      url: `/goods/getGoodInfo/${id}`,
      method: 'get',
    })
  },
  // 

  // 查询 商家商品详情
  getGoodInfo(id) {
    return request({
      url: `/shopToGoods/getGoodInfo/${id}`,
      method: 'get',
    })
  },
  // 添加商家商品
  insertData4(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/shopToGoods/insertData`,  // 飘号
      method: 'post',
      data: obj
    })
  },
  // 添加饿了么的商家商品
  createGoods(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/ele/createGoods`,  // 飘号
      method: 'post',
      data: obj
    })
  },
  // 修改商家商品
  updateData4(id, obj) {
    return request({
      url: `/shopToGoods/updateData/${id}`,
      method: 'post',
      data: obj
    })
  },
  // 修改饿了么的商家商品
  updateGoods(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/ele/updateGoods`,  // 飘号
      method: 'post',
      data: obj
    })
  },
  // 删除商家商品
  deleteGoods(id) {
    return request({
      url: `/shopToGoods/${id}`,
      method: 'delete'
    })
  },
  // 删除饿了么商品
  deleteEleGoods(id, access_token, token_type, expires_in, refresh_token) {
    return request({
      url: `/ele/deleteEleGoods/${id}`,
      method: 'delete',
      params: { access_token, token_type, expires_in, refresh_token }
    })
  },

  // 查询所有规格参数列表 分页
  getParamList(page, limit, shopId, keyword) {
    return request({
      url: `/shopToParam/getParamList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: { 'keyword': keyword }
    })
  },
  // 查询 规格参数详情
  getParamInfo(id) {
    return request({
      url: `/shopToParam/getParamInfo/${id}`,
      method: 'get',
    })
  },

  // 修改规格参数
  updateData6(id, obj) {
    return request({
      url: `/shopToParam/updateData/${id}`,
      method: 'post',
      data: obj
    })
  },

  // 添加 商品配料
  addCostCards(goodsToBatchVo) {
    return request({
      url: `/goodsToBatch/addGoodsBatching`,
      method: 'post',
      data: goodsToBatchVo
    })
  },

  // 修改 商品配料
  updateCostCards(goodsToBatchVo) {
    return request({
      url: `/goodsToBatch/updateGoodsBatching`,
      method: 'post',
      data: goodsToBatchVo
    })
  },
  // 查询 商家配料详情
  getAllGoodsBatchInfo(goodsId, shopId, sizeId, sugarId, temperatureId) {
    return request({
      url: `/goodsToBatch/getAllGoodsBatchInfo/${goodsId}/${shopId}/${sizeId}/${sugarId}/${temperatureId}`,
      method: 'get',
    })
  },
  // 查询 商品配料
  getGoodsBatchInfo(goodsId, sizeId, sugarId, temperatureId, shopId) {
    return request({
      url: `/goodsToBatch/getGoodsBatchInfo/${goodsId}/${sizeId}/${sugarId}/${temperatureId}/${shopId}`,
      method: 'get',
    })
  },

  // 产品配方配方 分页
  getGoodBatchList(page, limit, shopId, keyword) {
    return request({
      url: `/goodsToBatch/getGoodsBatchingList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: { keyword: keyword }
    })
  },

  // 删除 产品配方
  removeGoodBatch(goodsId, shopId, sizeId, sugarId, temperatureId) {
    return request({
      url: `/goodsToBatch/remove/${goodsId}/${shopId}/${sizeId}/${sugarId}/${temperatureId}`,
      method: 'delete',
    })
  },
  // 查询 所有杯型
  getShopParams(shopId) {
    return request({
      url: `/goodsToBatch/getShopParams/${shopId}`,
      method: 'get',
    })
  },

  // 查询商家配料列表 分页
  getBatchList(page, limit, shopId) {
    return request({
      url: `/batchUse/getBatchList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: {}
    })
  },


  // 查询 商家配料详情
  getBatchInfo(id) {
    return request({
      url: `/batchUse/getBatchInfo/${id}`,
      method: 'get',
    })
  },
  // 添加商家配料
  insertData8(obj) {
    return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/batchUse/insertData`,  // 飘号
      method: 'post',
      data: obj
    })
  },
  // 修改商家配料
  updateData8(id, obj) {
    return request({
      url: `/batchUse/updateData/${id}`,
      method: 'post',
      data: obj
    })
  },
  // 删除商家商品
  deleteBatch(id) {
    return request({
      url: `/batchUse/${id}`,
      method: 'delete'
    })
  },
  // 查询所有配料
  getAllBatching() {
    return request({
      url: `/batching/getAllBatchs`,
      method: 'get',
    })
  },

  // 获取支付二维码
  payShop(shopId) {
    return request({
      url: `/cashierPay/getCode/${shopId}`,
      method: 'get',
      responseType: 'blob'
    })
  },
}