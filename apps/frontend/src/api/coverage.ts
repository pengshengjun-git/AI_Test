import request from '@/utils/request'

export interface Coverage {
  id?: number
  project_id: number
  covered_lines: number
  total_lines: number
  coverage_rate?: number
  report_date: string
  created_at?: string
  updated_at?: string
}

export interface CoverageQueryParams {
  project_id?: number
  page?: number
  size?: number
}

export interface CoverageStats {
  totalCovered: number
  totalLines: number
  avgCoverage: number
  coverageHistory: Array<{ date: string; rate: number }>
}

export const getCoverageList = (params?: CoverageQueryParams) => {
  return request({
    url: '/api/v1/coverage',
    method: 'get',
    params
  })
}

export const getCoverageStats = () => {
  return request({
    url: '/api/v1/coverage/stats',
    method: 'get'
  })
}

export const createCoverage = (data: Coverage) => {
  return request({
    url: '/api/v1/coverage',
    method: 'post',
    data
  })
}

export const updateCoverage = (id: number, data: Coverage) => {
  return request({
    url: `/api/v1/coverage/${id}`,
    method: 'put',
    data
  })
}

export const deleteCoverage = (id: number) => {
  return request({
    url: `/api/v1/coverage/${id}`,
    method: 'delete'
  })
}
