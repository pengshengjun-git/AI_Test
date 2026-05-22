import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import Project from '@/views/Project.vue'
import Testcase from '@/views/Testcase.vue'
import Defect from '@/views/Defect.vue'
import AICenter from '@/views/AICenter.vue'
import Strategy from '@/views/Strategy.vue'
import Requirement from '@/views/Requirement.vue'
import TestPlan from '@/views/TestPlan.vue'
import Coverage from '@/views/Coverage.vue'
import Login from '@/views/Login.vue'
import NotFound from '@/views/NotFound.vue'
import UserManagement from '@/views/UserManagement.vue'
import RoleManagement from '@/views/RoleManagement.vue'
import DepartmentManagement from '@/views/DepartmentManagement.vue'
import MenuManagement from '@/views/MenuManagement.vue'
import { ElMessage } from 'element-plus'

/**
 * 路由配置
 */
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      name: 'Dashboard',
      component: Dashboard,
      meta: { requiresAuth: true, permission: 'dashboard:read' }
    },
    {
      path: '/project',
      name: 'Project',
      component: Project,
      meta: { requiresAuth: true, permission: 'project:view' }
    },
    {
      path: '/requirement',
      name: 'Requirement',
      component: Requirement,
      meta: { requiresAuth: true, permission: 'requirement:view' }
    },
    {
      path: '/testcase',
      name: 'Testcase',
      component: Testcase,
      meta: { requiresAuth: true, permission: 'testcase:view' }
    },
    {
      path: '/testplan',
      name: 'TestPlan',
      component: TestPlan,
      meta: { requiresAuth: true, permission: 'testplan:read' }
    },
    {
      path: '/defect',
      name: 'Defect',
      component: Defect,
      meta: { requiresAuth: true, permission: 'defect:view' }
    },
    {
      path: '/coverage',
      name: 'Coverage',
      component: Coverage,
      meta: { requiresAuth: true, permission: 'coverage:read' }
    },
    {
      path: '/ai-center',
      name: 'AICenter',
      component: AICenter,
      meta: { requiresAuth: true, permission: 'ai:use' }
    },
    {
      path: '/system/users',
      name: 'UserManagement',
      component: UserManagement,
      meta: { requiresAuth: true, roles: ['SUPER_ADMIN'], permission: 'user:manage' }
    },
    {
      path: '/system/roles',
      name: 'RoleManagement',
      component: RoleManagement,
      meta: { requiresAuth: true, roles: ['SUPER_ADMIN'], permission: 'role:manage' }
    },
    {
      path: '/system/departments',
      name: 'DepartmentManagement',
      component: DepartmentManagement,
      meta: { requiresAuth: true, roles: ['SUPER_ADMIN'], permission: 'system:department:*' }
    },
    {
      path: '/system/menus',
      name: 'MenuManagement',
      component: MenuManagement,
      meta: { requiresAuth: true, roles: ['SUPER_ADMIN'], permission: 'system:menu:*' }
    },
    {
      path: '/strategy',
      name: 'Strategy',
      component: Strategy,
      meta: { requiresAuth: true, permission: 'strategy:read' }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFound,
      meta: { requiresAuth: false }
    }
  ]
})

/**
 * 获取用户信息
 */
const getUserInfo = () => {
  try {
    const userInfoStr = localStorage.getItem('userInfo')
    if (userInfoStr) {
      return JSON.parse(userInfoStr)
    }
  } catch (e) {
    console.error('解析用户信息失败:', e)
  }
  return null
}

/**
 * 检查用户是否有指定权限
 */
const hasPermission = (permission: string): boolean => {
  const userInfo = getUserInfo()
  if (!userInfo) return false
  
  const roles = userInfo.roles || []
  const permissions = userInfo.permissions || []
  
  console.log('路由权限检查 - permission:', permission, 'roles:', roles, 'permissions:', permissions)
  
  if (roles.includes('SUPER_ADMIN') || permissions.includes('*') || permissions.includes('system:*')) {
    console.log('管理员用户，允许访问路由，权限:', permission)
    return true
  }
  
  const hasPerm = permissions.some((p: string) => {
    if (p === permission) return true
    if (p.endsWith('*')) {
      return permission.startsWith(p.slice(0, -1))
    }
    return false
  })
  
  console.log('权限检查结果:', hasPerm)
  return hasPerm
}

/**
 * 检查用户是否有指定角色
 */
const hasRole = (roles: string[]): boolean => {
  const userInfo = getUserInfo()
  if (!userInfo) return false
  
  const userRoles = userInfo.roles || []
  const userPermissions = userInfo.permissions || []
  
  console.log('角色检查 - requiredRoles:', roles, 'userRoles:', userRoles, 'userPermissions:', userPermissions)
  
  // 管理员拥有所有角色权限
  if (userRoles.includes('SUPER_ADMIN') || userPermissions.includes('*') || userPermissions.includes('system:*')) {
    console.log('管理员用户，允许访问所有角色权限')
    return true
  }
  
  const hasRequiredRole = roles.some(role => userRoles.includes(role))
  console.log('角色检查结果:', hasRequiredRole)
  return hasRequiredRole
}

/**
 * 路由守卫 - 检查登录状态和权限
 */
router.beforeEach((to, _from, next) => {
  if (to.name === 'NotFound') {
    next()
    return
  }
  
  const requiresAuth = to.meta.requiresAuth
  
  if (!requiresAuth) {
    next()
    return
  }
  
  const token = localStorage.getItem('token')
  
  if (!token) {
    next('/login')
    return
  }
  
  // 检查角色权限
  const requiredRoles = to.meta.roles as string[] | undefined
  if (requiredRoles && requiredRoles.length > 0) {
    if (!hasRole(requiredRoles)) {
      ElMessage.error('您没有权限访问该页面')
      // 阻止导航，让用户停留在当前页面
      next(false)
      return
    }
  }
  
  // 检查权限码
  const requiredPermission = to.meta.permission as string | undefined
  if (requiredPermission && !hasPermission(requiredPermission)) {
    ElMessage.error('您没有权限访问该页面')
    // 阻止导航，让用户停留在当前页面
    next(false)
    return
  }
  
  next()
})

export default router