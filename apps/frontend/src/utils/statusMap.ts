export const statusMap: Record<string, string> = {
  active: '进行中',
  ACTIVE: '进行中',
  draft: '草稿',
  DRAFT: '草稿',
  pending: '待处理',
  PENDING: '待处理',
  completed: '已完成',
  COMPLETED: '已完成',
  planning: '规划中',
  PLANNING: '规划中',
  IN_PROGRESS: '进行中',
  in_progress: '进行中',
  ARCHIVED: '已归档',
  archived: '已归档',
  testing: '测试中',
  done: '已完成',
  new: '新建',
  open: '打开',
  closed: '关闭',
  resolved: '已解决',
  assigned: '已分配',
  ENABLED: '启用',
  DISABLED: '禁用',
  PASS: '通过',
  FAIL: '失败',
  '待测试': '待测试',
  '测试通过': '测试通过',
  '测试失败': '测试失败',
  reviewed: '已评审',
  approved: '已批准',
  deprecated: '已废弃'
}

export const priorityMap: Record<string, string> = {
  P0: 'P0-紧急',
  P1: 'P1-高',
  P2: 'P2-中',
  P3: 'P3-低',
  critical: '严重',
  high: '高',
  medium: '中',
  low: '低'
}

export const testStatusMap: Record<string, string> = {
  '待测试': '待测试',
  '测试通过': '测试通过',
  '测试失败': '测试失败',
  pending: '待测试',
  running: '执行中',
  passed: '通过',
  failed: '失败',
  blocked: '阻塞',
  skipped: '跳过'
}

export const requirementStatusMap: Record<string, string> = {
  draft: '草稿',
  pending: '待评审',
  approved: '已批准',
  in_progress: '进行中',
  completed: '已完成',
  rejected: '已拒绝',
  closed: '已关闭'
}

export function getStatusText(status: string | undefined): string {
  if (!status) return '-'
  return statusMap[status] || statusMap[status.toLowerCase()] || status
}

export function getPriorityText(priority: string | undefined): string {
  if (!priority) return '-'
  return priorityMap[priority] || priorityMap[priority.toLowerCase()] || priority
}

export function getTestStatusText(status: string | undefined): string {
  if (!status) return '待测试'
  return testStatusMap[status] || status
}

export function getRequirementStatusText(status: string | undefined): string {
  if (!status) return '-'
  return requirementStatusMap[status] || requirementStatusMap[status.toLowerCase()] || status
}