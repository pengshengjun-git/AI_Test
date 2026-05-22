import axios from 'axios'
import router from '@/router'
import { showError } from './message'

const request = axios.create({
  baseURL: '',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

request.interceptors.request.use(
  (config: any) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    config.headers['Content-Type'] = 'application/json'
    return config
  },
  (error: any) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

request.interceptors.response.use(
  (response: any) => {
    if (!response || !response.data) {
      showError('响应数据为空')
      return Promise.reject(new Error('响应数据为空'))
    }
    
    const res = response.data
    
    if (res.code === 200 || res.code === 0) {
      return res
    } else if (res.code === 401) {
      showError(res.message || '登录已过期，请重新登录')
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
      return Promise.reject(new Error(res.message || '未授权'))
    } else if (res.code === 403) {
      showError(res.message || '没有权限访问此资源')
      return Promise.reject(new Error(res.message || '没有权限访问此资源'))
    } else {
      showError(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error: any) => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        showError('登录已过期，请重新登录')
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      } else if (status === 403) {
        showError('没有权限访问此资源')
      } else if (status === 404) {
        showError('请求的资源不存在')
      } else if (status >= 500) {
        showError('服务器内部错误，请稍后重试')
      } else {
        const message = error.response.data?.message || error.message || '请求失败'
        showError(message)
      }
    } else if (error.request) {
      showError('请求超时或网络异常')
    } else {
      showError('请求配置错误')
    }
    return Promise.reject(error)
  }
)

const typedRequest = request as {
  <T = any>(config: any): Promise<T>
  get<T = any>(url: string, config?: any): Promise<T>
  post<T = any>(url: string, data?: any, config?: any): Promise<T>
  put<T = any>(url: string, data?: any, config?: any): Promise<T>
  delete<T = any>(url: string, config?: any): Promise<T>
}

export default typedRequest