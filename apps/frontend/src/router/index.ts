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
      meta: { requiresAuth: true }
    },
    {
      path: '/project',
      name: 'Project',
      component: Project,
      meta: { requiresAuth: true }
    },
    {
      path: '/requirement',
      name: 'Requirement',
      component: Requirement,
      meta: { requiresAuth: true }
    },
    {
      path: '/testcase',
      name: 'Testcase',
      component: Testcase,
      meta: { requiresAuth: true }
    },
    {
      path: '/testplan',
      name: 'TestPlan',
      component: TestPlan,
      meta: { requiresAuth: true }
    },
    {
      path: '/defect',
      name: 'Defect',
      component: Defect,
      meta: { requiresAuth: true }
    },
    {
      path: '/coverage',
      name: 'Coverage',
      component: Coverage,
      meta: { requiresAuth: true }
    },
    {
      path: '/ai-center',
      name: 'AICenter',
      component: AICenter,
      meta: { requiresAuth: true }
    },
    {
      path: '/strategy',
      name: 'Strategy',
      component: Strategy,
      meta: { requiresAuth: true }
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
 * 路由守卫 - 检查登录状态
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
  
  next()
})

export default router