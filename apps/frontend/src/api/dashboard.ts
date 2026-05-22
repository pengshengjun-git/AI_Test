import request from '@/utils/request'

/**
 * 获取工作台统计数据
 */
export const getDashboardStats = () => {
  return request({
    url: '/api/v1/dashboard/stats',
    method: 'get'
  })
}

/**
 * 获取最近项目
 */
export const getRecentProjects = () => {
  return request({
    url: '/api/v1/dashboard/recent-projects',
    method: 'get'
  })
}

/**
 * 获取最近缺陷
 */
export const getRecentDefects = () => {
  return request({
    url: '/api/v1/dashboard/recent-defects',
    method: 'get'
  })
}

/**
 * 获取系统状态
 */
export const getSystemStatus = () => {
  return request({
    url: '/api/v1/dashboard/system-status',
    method: 'get'
  })
}

/**
 * 获取待办事项列表
 */
export const getTodoList = () => {
  return request({
    url: '/api/v1/dashboard/todo-list',
    method: 'get'
  })
}

/**
 * 切换待办事项状态
 */
export const toggleTodoStatus = (id: number, completed: boolean) => {
  return request({
    url: `/api/v1/dashboard/todo/${id}/status`,
    method: 'put',
    data: { completed }
  })
}