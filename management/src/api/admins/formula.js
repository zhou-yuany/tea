import request from '@/utils/request'

export default {

 // 查询所有当前店铺商品 包含机器型号
 allGoods(shopId) {
    return request({
      url: `/goodsToBatch/goodsAll/${shopId}`,
      method: 'get',
    })
  },

  // 查询所有当前店铺配料
  batchUse(shopId) {
    return request({
      url: `/batchUse/getBatchList/${shopId}`,
      method: 'get',
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

  // 查询 商品配料
  getGoodsBatchInfo(goodsId, shopId) {
    return request({
      url: `/goodsToBatch/getGoodsBatchInfo/${goodsId}/${shopId}`,
      method: 'get',
    })
  },

  // 产品配方配方 分页
  getGoodBatchList(page, limit, shopId, keyword) {
    return request({
      url: `/goodsToBatch/getGoodsBatchingList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: {keyword: keyword}
    })
  },

  // 删除 产品配方
  removeGoodBatch(goodsId, shopId) {
    return request({
      url: `/goodsToBatch/remove/${goodsId}/${shopId}`,
      method: 'delete',
    })
  },
  // 添加 规格参数配方
  addParamBatch(paramBatch) {
    return request({
      url: `/paramBatch/addParamBatch`,
      method: 'post',
      data: paramBatch
    })
  },

  // 修改 规格参数配方
  updateParamBatch(paramBatch) {
    return request({
      url: `/paramBatch/updateParamBatch`,
      method: 'post',
      data: paramBatch
    })
  },

   // 删除 规格参数配方
   removeParamBatch(id) {
    return request({
      url: `/paramBatch/${id}`,
      method: 'delete',
    })
  },

  // 查询全部糖度\温度规格参数
  getHeatAndSugarParams() {
    return request({
      url: `/paramBatch/getHeatAndSugarParams`,
      method: 'get',
    })
  },

  // 查询全部杯型
  getCupShaped(shopId) {
    return request({
      url: `/param/getCupShaped/${shopId}`,
      method: 'get',
    })
  },
}