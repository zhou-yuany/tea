import request from '@/utils/request'

export default {
    // 查询所有品牌
 getAllBrand() {
  return request({
    url: `/brand/allList`,
    method: 'get',
  })
},

  // 查询所有总分类
 getAllCates() {
  return request({
    url: `/categorizeAll/allList`,
    method: 'get',
  })
},

// 查询所有总商品
allGoods() {
  return request({
    url: `/goods/allList`,
    method: 'get',
  })
},


// 查询所有总分类列表 分页
getCateAllList(page, limit, keyword) {
  return request({
    url: `/categorizeAll/getCateAllList/${page}/${limit}`,
    method: 'post',
    params: {keyword: keyword}
  })
},

// 查询所有规格分类
getAllParamCates() {
  return request({
    url: `/paramCate/allList`,
    method: 'get',
  })
},

// 查询 总分类详情
getCateAllInfo(id) {
  return request({
    url: `/categorizeAll/getCateAllInfo/${id}`,
    method: 'get',
  })
},
// 添加总分类
insertData0(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/categorizeAll/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},

// 修改总分类
updateData0(id,obj){
  return request({
      url: `/categorizeAll/updateData/${id}`,
      method: 'post',
      data: obj
  })
},

// 删除总分类
deleteCateAll(id){
  return request({
      url: `/categorizeAll/${id}`,
      method: 'delete'
  })
},

// 查询所有配料列表 分页
getBatchList(page, limit, keyword) {
  return request({
    url: `/batching/getBatchList/${page}/${limit}`,
    method: 'post',
    params: {keyword: keyword}
  })
},
// 查询 配料详情
getBatchInfo(id) {
  return request({
    url: `/batching/getBatchInfo/${id}`,
    method: 'get',
  })
},
// 添加配料
insertData1(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/batching/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},

// 修改配料
updateData1(id,obj){
  return request({
      url: `/batching/updateData/${id}`,
      method: 'post',
      data: obj
  })
},

// 删除配料
deleteBatchInfo(id){
  return request({
      url: `/batching/${id}`,
      method: 'delete'
  })
},
// 查询商品列表 分页
getGoodsList(page, limit, keyword) {
  return request({
    url: `/goods/getGoodsList/${page}/${limit}`,
    method: 'post',
    params: {keyword: keyword}
  })
},
// 查询 商品详情
getGoodInfo(id) {
  return request({
    url: `/goods/getGoodInfo/${id}`,
    method: 'get',
  })
},
// 添加商品
insertData4(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/goods/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},

// 修改商品
updateData4(id,obj){
  return request({
      url: `/goods/updateData/${id}`,
      method: 'post',
      data: obj
  })
},

// 删除商品
deleteGoods(id){
  return request({
      url: `/goods/${id}`,
      method: 'delete'
  })
},
 // 查询所有当前店铺商品 包含机器型号
//  allGoods(shopId) {
//     return request({
//       url: `/goodsToBatch/goodsAll/${shopId}`,
//       method: 'get',
//     })
//   },

  // 查询所有配料
  getAllBatching() {
    return request({
      url: `/batching/getAllBatchs`,
      method: 'get',
    })
  },

  // 添加 商品配料
  addCostCards(goodsAllBatchVo) {
    return request({
      url: `/goodsAllBatch/addGoodsBatching`,
      method: 'post',
      data: goodsAllBatchVo
    })
  },

  // 修改 商品配料
  updateCostCards(goodsAllBatchVo) {
    return request({
      url: `/goodsAllBatch/updateGoodsBatching`,
      method: 'post',
      data: goodsAllBatchVo
    })
  },

  // 查询 商品配料
  getGoodsBatchInfo(goodsId,sizeId, sugarId, temperatureId) {
    return request({
      url: `/goodsAllBatch/getGoodsBatchInfo/${goodsId}/${sizeId}/${sugarId}/${temperatureId}`,
      method: 'get',
    })
  },

  // 产品配方 分页
  getGoodBatchList(page, limit, keyword) {
    return request({
      url: `/goodsAllBatch/getGoodsAllBatchingList/${page}/${limit}`,
      method: 'post',
      params: {keyword: keyword}
    })
  },

  // 删除 产品配方
  removeGoodBatch(goodsId, sizeId, sugarId, temperatureId) {
    return request({
      url: `/goodsAllBatch/remove/${goodsId}/${sizeId}/${sugarId}/${temperatureId}`,
      method: 'delete',
    })
  },
  // 添加 规格参数配方
  addParamBatch(paramAllBatch) {
    return request({
      url: `/paramAllBatch/addParamAllBatch`,
      method: 'post',
      data: paramAllBatch
    })
  },

  // 修改 规格参数配方
  updateParamBatch(paramAllBatch) {
    return request({
      url: `/paramAllBatch/updateParamAllBatch`,
      method: 'post',
      data: paramAllBatch
    })
  },

   // 删除 规格参数配方
   removeParamBatch(id) {
    return request({
      url: `/paramAllBatch/${id}`,
      method: 'delete',
    })
  },

  // 查询 规格参数详情
  getParamAllBatchInfo(id) {
  return request({
    url: `/paramAllBatch/getParamAllBatchInfo/${id}`,
    method: 'get',
  })
},

  // 规格参数 分页
  getParamBatchList(page, limit, keyword) {
    return request({
      url: `/paramAllBatch/getParamAllBatchList/${page}/${limit}`,
      method: 'post',
      params: {keyword: keyword}
    })
  },


  // 查询全部糖度\温度规格参数
  getHeatAndSugarParams() {
    return request({
      url: `/paramAllBatch/getHeatAndSugarParams`,
      method: 'get',
    })
  },

  // 查询全部杯型
  getCupShaped() {
    return request({
      url: `/param/getCupShaped`,
      method: 'get',
    })
  },

  // 查询所有规格分类列表 分页
  getParamCateList(page, limit, keyword) {
  return request({
    url: `/paramCate/getParamCateList/${page}/${limit}`,
    method: 'post',
    params: {keyword: keyword}
  })
},
// 查询 规格分类详情
getParamCateInfo(id) {
  return request({
    url: `/paramCate/getParamCateInfo/${id}`,
    method: 'get',
  })
},
// 添加规格分类
insertData5(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/paramCate/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},

// 修改规格分类
updateData5(id,obj){
  return request({
      url: `/paramCate/updateData/${id}`,
      method: 'post',
      data: obj
  })
},

// 删除规格分类
deleteParamCate(id){
  return request({
      url: `/paramCate/${id}`,
      method: 'delete'
  })
},

 // 查询所有规格参数列表 分页
 getParamList(page, limit, keyword) {
  return request({
    url: `/param/getParamList/${page}/${limit}`,
    method: 'post',
    params: {keyword: keyword}
  })
},
// 查询 规格参数详情
getParamInfo(id) {
  return request({
    url: `/param/getParamInfo/${id}`,
    method: 'get',
  })
},
// 添加规格参数
insertData6(obj){
  return request({
      // url: '/carStore/course/pageTeacherCondition/'+current+'/'+limit,
      url: `/param/insertData`,  // 飘号
      method: 'post',
      data:obj
  })
},

// 修改规格参数
updateData6(id,obj){
  return request({
      url: `/param/updateData/${id}`,
      method: 'post',
      data: obj
  })
},

// 删除规格参数
deleteParam(id){
  return request({
      url: `/param/${id}`,
      method: 'delete'
  })
},
}