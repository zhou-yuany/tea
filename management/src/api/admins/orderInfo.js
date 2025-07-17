import request from '@/utils/request'
export default{
    
    // 订单列表 分页 分类
  ordersList(page, limit, orderQuery) {
    return request({
      url: `/orders/getAllOrders/${page}/${limit}`,
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

  // 查询所有商户
  getAllShops() {
    return request({
      url: `/shop/getAllShops`,
      method: 'get',
    })
  },

}