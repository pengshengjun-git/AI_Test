/**
 * 权限控制工具函数
 */
import { useUserStore } from '@/stores/user'

export interface UserInfo {
  id?: number
  userId?: number
  username?: string
  email?: string
  phone?: string
  realName?: string
  departmentId?: number
  status?: number
  role?: string
  roleName?: string
  userRole?: string
  roles: string[]
  permissions: string[]
  createTime?: string
  updateTime?: string
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = (): UserInfo => {
  try {
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)
      console.log('当前用户信息:', userInfo)
      return {
        ...userInfo,
        roles: userInfo.roles || [],
        permissions: userInfo.permissions || []
      }
    }
  } catch (e) {
    console.error('解析用户信息失败:', e)
  }
  return {
    roles: [],
    permissions: []
  }
}

/**
 * 获取当前用户角色
 */
export const getUserRole = (): string => {
  const userInfo = getUserInfo()
  // 兼容多种可能的字段名
  const role = userInfo.role || userInfo.roleName || userInfo.userRole || ''
  console.log('当前用户角色:', role)
  return role.toLowerCase()
}

/**
 * 获取当前用户权限列表
 */
export const getUserPermissions = (): string[] => {
  const userInfo = getUserInfo()
  return userInfo.permissions || []
}

/**
 * 检查用户是否有指定权限
 */
export const hasPermission = (permission: string): boolean => {
  if (isAdmin()) return true
  const userStore = useUserStore()
  const info = userStore.userInfo
  if (!info) return false
  const perms = info.permissions || []
  if (perms.includes('*') || perms.includes('system:*')) return true
  return perms.some(p => {
    if (p === permission) return true
    if (p.endsWith('*')) {
      return permission.startsWith(p.slice(0, -1))
    }
    return false
  })
}

/**
 * 检查用户是否有任意指定权限
 */
export const hasAnyPermission = (permissions: string[]): boolean => {
  if (isAdmin()) {
    return true
  }
  const userPermissions = getUserPermissions()
  return permissions.some(p => userPermissions.includes(p))
}

/**
 * 检查用户是否有所有指定权限
 */
export const hasAllPermissions = (permissions: string[]): boolean => {
  if (isAdmin()) {
    return true
  }
  const userPermissions = getUserPermissions()
  return permissions.every(p => userPermissions.includes(p))
}

/**
 * 检查用户角色
 */
export const hasRole = (role: string): boolean => {
  const userStore = useUserStore()
  const info = userStore.userInfo
  if (!info) return false
  const roles = info.roles || []
  return roles.includes(role)
}

/**
 * 检查用户是否有任意指定角色
 */
export const hasAnyRole = (roles: string[]): boolean => {
  const userRole = getUserRole()
  return roles.some(r => r.toLowerCase() === userRole)
}

/**
 * 检查是否已登录
 */
export const isLoggedIn = (): boolean => {
  const token = localStorage.getItem('token')
  return !!token
}

/**
 * 检查是否为管理员
 */
export const isAdmin = (): boolean => {
  const userStore = useUserStore()
  const info = userStore.userInfo
  if (!info) return false
  const roles = info.roles || []
  const perms = info.permissions || []
  return roles.includes('ADMIN') || roles.includes('SUPER_ADMIN') || perms.includes('*') || perms.includes('system:*')
}

/**
 * 获取权限对应的按钮禁用状态
 */
export const getPermissionDisabled = (permission: string): boolean => {
  return !hasPermission(permission)
}

/**
 * 获取当前用户角色列表（基于Pinia Store）
 */
export const getUserRoles = (): string[] => {
  const userStore = useUserStore()
  const info = userStore.getUserInfo()
  return info?.roles || []
}

/**
 * 检查用户是否有指定权限（基于Pinia Store，支持通配符）
 */
export const checkPermission = (permission: string): boolean => {
  const userStore = useUserStore()
  const info = userStore.getUserInfo()
  if (!info) return false
  if (info.permissions?.includes('*')) return true
  if (info.permissions?.includes('system:*')) return true
  return info.permissions?.some(p => {
    if (p === permission) return true
    if (p.endsWith('*')) {
      const prefix = p.slice(0, -1)
      return permission.startsWith(prefix)
    }
    return false
  }) || false
}

/**
 * 检查是否为管理员（基于Pinia Store）
 */
export const checkIsAdmin = (): boolean => {
  const userStore = useUserStore()
  const info = userStore.getUserInfo()
  if (!info) return false
  return info.roles?.includes('ADMIN') || checkPermission('system:*') || false
}

/**
 * 检查用户是否有指定角色（基于Pinia Store）
 */
export const checkRole = (role: string): boolean => {
  const userStore = useUserStore()
  const info = userStore.getUserInfo()
  if (!info) return false
  return info.roles?.includes(role) || false
}
