import request from '@/utils/request'
export default{
    
    // 订单列表 分页 分类
  ordersList(page, limit, shopId, orderQuery) {
    return request({
      url: `/orders/getShopOrders/${page}/${limit}/${shopId}`,
      method: 'post',
      data: orderQuery
    })
  },

  // 根据订单id查询订单详情
  ordersInfo(id) {
    return request({
      url: `/orders/getOrdersInfoShop/${id}`,
      method: 'get',
    })
  },


}