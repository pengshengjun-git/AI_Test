import request from '@/utils/request'

export interface AITask {
  id?: number
  name: string
  type: 'CASE_GENERATE' | 'REQUIREMENT_PARSE' | 'DEFECT_ANALYSIS' | 'REPORT_GENERATE'
  status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED'
  progress?: number
  projectId?: number
  createTime?: string
}

export interface AIModelConfig {
  modelName: string
  apiKey?: string
  apiUrl?: string
  temperature?: number
  maxTokens?: number
  topP?: number
}

/**
 * 获取AI任务列表
 */
export const getAITaskList = () => {
  return request({
    url: '/api/v1/ai/tasks',
    method: 'get'
  })
}

/**
 * 创建AI任务
 */
export const createAITask = (data: AITask) => {
  return request({
    url: '/api/v1/ai/tasks',
    method: 'post',
    data
  })
}

/**
 * 获取AI模型配置
 */
export const getAIModelConfig = () => {
  return request({
    url: '/api/v1/ai/model/config',
    method: 'get'
  })
}

/**
 * 更新AI模型配置
 */
export const updateAIModelConfig = (data: AIModelConfig) => {
  return request({
    url: '/api/v1/ai/model/config',
    method: 'put',
    data
  })
}

/**
 * AI生成测试用例
 */
export const aiGenerateTestcases = (data: { projectId: number; requirement?: string }) => {
  return request({
    url: '/api/v1/ai/generate/testcases',
    method: 'post',
    data
  })
}

/**
 * AI需求解析
 */
export const aiParseRequirement = (data: { requirement: string }) => {
  return request({
    url: '/api/v1/ai/parse/requirement',
    method: 'post',
    data
  })
}

/**
 * AI缺陷分析
 */
export const aiAnalyzeDefect = (data: { defectId: number }) => {
  return request({
    url: '/api/v1/ai/analyze/defect',
    method: 'post',
    data
  })
}

/**
 * AI生成报告
 */
export const aiGenerateReport = (data: { projectId?: number; type?: string }) => {
  return request({
    url: '/api/v1/ai/generate/report',
    method: 'post',
    data
  })
}