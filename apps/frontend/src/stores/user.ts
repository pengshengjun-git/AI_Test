import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface UserInfo {
  id?: number
  userId?: number
  username?: string
  email?: string
  phone?: string
  realName?: string
  departmentId?: number
  status?: number
  roles: string[]
  permissions: string[]
  createTime?: string
  updateTime?: string
}

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const token = ref<string>('')

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const setToken = (newToken: string) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const getToken = () => {
    if (!token.value) {
      token.value = localStorage.getItem('token') || ''
    }
    return token.value
  }

  const getUserInfo = () => {
    if (!userInfo.value) {
      const stored = localStorage.getItem('userInfo')
      if (stored) {
        try {
          userInfo.value = JSON.parse(stored)
        } catch (e) {
          console.error('解析用户信息失败:', e)
        }
      }
    }
    return userInfo.value
  }

  const logout = () => {
    userInfo.value = null
    token.value = ''
    localStorage.removeItem('userInfo')
    localStorage.removeItem('token')
  }

  const hasRole = (role: string): boolean => {
    if (!userInfo.value) return false
    return userInfo.value.roles?.includes(role) || false
  }

  const hasPermission = (permission: string): boolean => {
    if (!userInfo.value) return false
    const perms = userInfo.value.permissions || []
    if (perms.includes('*')) return true
    if (perms.includes('system:*')) return true
    return perms.some(p => {
      if (p === permission) return true
      if (p.endsWith('*')) {
        const prefix = p.slice(0, -1)
        return permission.startsWith(prefix)
      }
      return false
    })
  }

  const isAdmin = (): boolean => {
    if (!userInfo.value) return false
    return userInfo.value.roles?.includes('ADMIN') || hasPermission('system:*') || false
  }

  return {
    userInfo,
    token,
    setUserInfo,
    setToken,
    getToken,
    getUserInfo,
    logout,
    hasRole,
    hasPermission,
    isAdmin
  }
})
