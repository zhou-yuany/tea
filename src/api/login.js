import request from '@/utils/request'

export function login(equipmentNo) {
  return request({
    url: '/admins/miniLogin',
    method: 'post',
    data: {
      equipmentNo
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/admins/info',
    method: 'get',
    params: { token }
  })
}

export function getShopInfo(shopId, access_token, token_type, expires_in, refresh_token) {
  return request({
    url: '/ele/getShopInfo',
    method: 'get',
    params: {shopId, access_token, token_type, expires_in, refresh_token}
  })
}

export function logout() {
  return request({
    url: '/shop/logout',
    method: 'post'
  })
}
