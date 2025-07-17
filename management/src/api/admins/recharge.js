import request from '@/utils/request'
export default{
    
    // 余额列表 分页 分类
    getRechargeRecordList(page, limit, shopId, accountQuery) {
    return request({
      url: `/ordersFlow/getListShop/${page}/${limit}/${shopId}`,
      method: 'post',
      data: accountQuery
    })
  },

  // 获取支付二维码
  getQrCode(shopId, did) {
    return request({
      url: `/cashierPay/pay`,
      method: 'get',
      params: {shopId : shopId, did: did},
      responseType: 'blob'
    })
  },
  
  // 根据id获取订单支付详情
  getInfoBy(id) {
    return request({
      url: `/rechargeRecord/getInfoBy/${id}`,
      method: 'get',
    })
  },
  withdrawalShop(shopId, price){
    return request({
      url: `/ordersFlow/toWithdrawal/${shopId}`,
      method: 'post',
      params: {price}
    })
  },



}