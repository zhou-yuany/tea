import request from '@/utils/request'

export default {

  // 登陆
  login(user) {
    return request({
      url: `/admins/login1`,
      method: 'post',
      data: user
    })
  },

  // 店铺详情
  getShopInfo(adminId) {
    return request({
      url: `/shop/getShopInfo1/${adminId}`,
      method: 'get',
    })
  },

  // 下架商品
  clearGoods(goodsObj) {
    return request({
      url: `/shopToGoods/clearGoods`,
      method: 'post',
      data: goodsObj
    })
  },
  // 上架商品
  groundingGoods(id) {
    return request({
      url: `/shopToGoods/groundingGoods/${id}`,
      method: 'post',
    })
  },

  // 查询当前店铺全部商品
  goodsAllByShopId(shopId) {
    return request({
      url: `/goods/goodsAll/${shopId}`,
      method: 'get',
    })
  },
  // 根据选择商品查询规格
  goodsParams(goodsId) {
    return request({
      url: `/goods/getParamsByGoodsId/${goodsId}`,
      method: 'get',
    })
  },

  // 新增订单
  saveOrders(orderNo, type, adminId, params) {
    return request({
      url: `/orders/insertOrders/${orderNo}/${type}/${adminId}`,
      method: 'post',
      data: params
    })
  },

  // 支付成功改变订单状态
  updateOrders(id) {
    return request({
      url: `/orders/updateOrders/${id}`,
      method: 'post',
    })
  },
  // 点击完成订单按钮改变订单制作状态
  finishOrders(id) {
    return request({
      url: `/orders/finishOrders/${id}`,
      method: 'post',
    })
  },

  // 订单商品开始制作
  makeOrders(id) {
    return request({
      url: `/orders/makeOrders/${id}`,
      method: 'post',
    })
  },

  // 订单列表 分页 分类
  ordersList(page, limit, shopId, type) {
    return request({
      url: `/orders/getOrders/${page}/${limit}/${shopId}/${type}`,
      method: 'post',
    })
  },

  // 根据订单id查询订单详情
  ordersInfo(id,adminId) {
    return request({
      url: `/orders/getOrdersInfo/${id}/${adminId}`,
      method: 'get',
    })
  },

  // 当前店铺库存列表 分页
  batchList(page, limit, shopId) {
    return request({
      url: `/batchUse/getInventories/${page}/${limit}/${shopId}`,
      method: 'post',
    })
  },

  // 根据店铺id获取当前商品成本卡 分页
  goodsCartList(page, limit, shopId) {
    return request({
      url: `/goodsToBatch/getCostCards/${page}/${limit}/${shopId}`,
      method: 'post',
    })
  },

  // 根据店铺id获取当前商品成本卡配方详情
  cartInfo(id, sizeId) {
    return request({
      url: `/goodsToBatch/getCostCardInfo/${id}/${sizeId}`,
      method: 'get',
    })
  },

   // 根据店铺id获取当前出入库记录 分页
   inventoryList(page, limit, shopId, type) {
    return request({
      url: `/inventoryRecord/getInventoryRecords/${page}/${limit}/${shopId}/${type}`,
      method: 'post',
    })
  },

  // 入库
  insertInventory(batchForm) {
    return request({
      url: `/inventoryRecord/insertInventory`,
      method: 'post',
      data: batchForm
    })
  },

  // 出库
  outInventory(batchNumberId, count, inventoryContent) {
    return request({
      url: `/inventoryRecord/outInventory/${batchNumberId}/${count}`,
      method: 'post',
      params: {remarks: inventoryContent}
    })
  },

  // 当前店铺配料的所有品牌
  getBatchBrands(shopId) {
    return request({
      url: `/inventoryRecord/getBatchBrands/${shopId}`,
      method: 'get',
    })
  },

  // 当前店铺配料的所有配料名称
  getBatchNames(shopId) {
    return request({
      url: `/inventoryRecord/getBatchNames/${shopId}`,
      method: 'get',
    })
  },

  // 当前店铺所选配料的所有配料规格
  getBatchNumbers(shopId) {
    return request({
      url: `/inventoryRecord/getBatchNumbers/${shopId}`,
      method: 'get',
    })
  },

  // 根据店铺id获取叫号取餐列表 分页
  callOrders(page, limit, shopId) {
    return request({
      url: `/orders/callOrders/${page}/${limit}/${shopId}`,
      method: 'post',
    })
  },

  // 根据订单Id展示订单详情
  getOrdersAndInfo(id) {
    return request({
      url: `/orders/getOrdersAndInfo/${id}`,
      method: 'get',
    })
  },

  // 呼叫取餐改变呼叫状态
  callOrdersBy(id) {
    return request({
      url: `/orders/callOrdersBy/${id}`,
      method: 'post',
    })
  },
  // 通过输入取餐号呼叫取餐改变呼叫状态
  callOrdersByCode(code) {
    return request({
      url: `/orders/callOrdersByCode`,
      method: 'post',
      params: {pickupCode: code}
    })
  },

  // 点击已取餐改变取餐状态
  orderTake(id) {
    return request({
      url: `/orders/orderTake/${id}`,
      method: 'post',
    })
  },

   // 获取支付二维码
   payOrders(orderId) {
    return request({
      url: `/cashierPay/pay/${orderId}`,
      method: 'get',
      responseType: 'blob'
    })
  },

  // 根据id获取订单支付详情
  getOrdersInfoBy(id) {
    return request({
      url: `/orders/getOrdersInfoBy/${id}`,
      method: 'get',
    })
  },


  // 规格参数配方 分页
  getParamBatchList(page, limit, shopId, keyword) {
    return request({
      url: `/paramBatch/getParamBatchList/${page}/${limit}/${shopId}`,
      method: 'post',
      params: {keyword: keyword}
    })
  },

  // 规格参数配方详情
  getParamBatchInfo(id) {
    return request({
      url: `/paramBatch/getParamBatchInfo/${id}`,
      method: 'get',
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
      url: `/batchUse/getBatchList1/${shopId}`,
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
      url: `/goodsToBatch/getGoodsBatchingList1/${page}/${limit}/${shopId}`,
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




}

