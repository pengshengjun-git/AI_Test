import request from '@/utils/request'

/**
 * 登录
 */
export const login = (data: { username: string; password: string }) => {
  return request({
    url: '/api/v1/auth/login',
    method: 'post',
    data
  })
}

/**
 * 注册
 */
export const register = (data: { username: string; password: string; email: string }) => {
  return request({
    url: '/api/v1/auth/register',
    method: 'post',
    data
  })
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = () => {
  return request({
    url: '/api/v1/users/me',
    method: 'get'
  })
}

/**
 * 登出
 */
export const logout = () => {
  return request({
    url: '/api/v1/auth/logout',
    method: 'post'
  })
}