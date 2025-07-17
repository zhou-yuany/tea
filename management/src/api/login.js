import request from '@/utils/request'

export function login(username,password) {
  return request({
    url: '/shop/login',
    method: 'post',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/shop/info',
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

export function getMtAccess(code, state) {
  return request({
    url: '/mt/callback',
    method: 'POST',
    data: {"code": code, "state": state}
  })
}


export function logout() {
  return request({
    url: '/shop/logout',
    method: 'post'
  })
}
