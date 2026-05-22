/**
 * 权限控制工具函数
 */

export interface UserInfo {
  id?: number
  username?: string
  role?: string
  permissions?: string[]
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = (): UserInfo => {
  try {
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      return JSON.parse(userInfoStr)
    }
  } catch (e) {
    console.error('解析用户信息失败:', e)
  }
  return {}
}

/**
 * 获取当前用户角色
 */
export const getUserRole = (): string => {
  const userInfo = getUserInfo()
  return userInfo.role || ''
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
  const permissions = getUserPermissions()
  // 管理员拥有所有权限
  if (getUserRole() === 'admin') {
    return true
  }
  return permissions.includes(permission)
}

/**
 * 检查用户是否有任意指定权限
 */
export const hasAnyPermission = (permissions: string[]): boolean => {
  if (getUserRole() === 'admin') {
    return true
  }
  const userPermissions = getUserPermissions()
  return permissions.some(p => userPermissions.includes(p))
}

/**
 * 检查用户是否有所有指定权限
 */
export const hasAllPermissions = (permissions: string[]): boolean => {
  if (getUserRole() === 'admin') {
    return true
  }
  const userPermissions = getUserPermissions()
  return permissions.every(p => userPermissions.includes(p))
}

/**
 * 检查用户角色
 */
export const hasRole = (role: string): boolean => {
  return getUserRole() === role
}

/**
 * 检查用户是否有任意指定角色
 */
export const hasAnyRole = (roles: string[]): boolean => {
  const userRole = getUserRole()
  return roles.includes(userRole)
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
  return getUserRole() === 'admin'
}

/**
 * 获取权限对应的按钮禁用状态
 */
export const getPermissionDisabled = (permission: string): boolean => {
  return !hasPermission(permission)
}
