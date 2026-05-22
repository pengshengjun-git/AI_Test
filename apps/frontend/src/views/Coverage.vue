<template>
  <div class="coverage-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">测试覆盖率</h2>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            录入覆盖率
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-header">
            <span class="stat-label">总代码行数</span>
          </div>
          <div class="stat-value">{{ stats.totalLines.toLocaleString() }}</div>
          <div class="stat-unit">行</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-header">
            <span class="stat-label">已覆盖行数</span>
          </div>
          <div class="stat-value">{{ stats.totalCovered.toLocaleString() }}</div>
          <div class="stat-unit">行</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card highlight">
          <div class="stat-header">
            <span class="stat-label">覆盖率</span>
          </div>
          <div class="stat-value coverage-rate">
            <span class="rate-number">{{ stats.avgCoverage }}</span>
            <span class="rate-unit">%</span>
          </div>
          <div class="stat-bar">
            <el-progress :percentage="stats.avgCoverage" :stroke-width="8" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card title="覆盖率趋势">
          <div class="trend-container">
            <div v-if="stats.coverageHistory.length > 0">
              <div v-for="(item, index) in stats.coverageHistory" :key="index" class="trend-item">
                <span class="trend-date">{{ item.date }}</span>
                <div class="trend-bar-wrapper">
                  <div class="trend-bar" :style="{ width: item.rate + '%' }"></div>
                </div>
                <span class="trend-rate">{{ item.rate }}%</span>
              </div>
            </div>
            <div v-else class="empty-state">
              <el-icon size="48" color="#909399"><PieChart /></el-icon>
              <p>暂无覆盖率历史数据</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card title="覆盖率分布">
          <div class="distribution">
            <div class="distribution-item">
              <div class="distribution-label">已覆盖代码</div>
              <div class="distribution-bar">
                <div class="distribution-fill covered" :style="{ width: stats.avgCoverage + '%' }"></div>
              </div>
              <div class="distribution-value">{{ stats.avgCoverage }}%</div>
            </div>
            <div class="distribution-item">
              <div class="distribution-label">未覆盖代码</div>
              <div class="distribution-bar">
                <div class="distribution-fill uncovered" :style="{ width: (100 - stats.avgCoverage) + '%' }"></div>
              </div>
              <div class="distribution-value">{{ (100 - stats.avgCoverage).toFixed(1) }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="关联项目">
            <el-select v-model="filterForm.project_id" placeholder="请选择关联项目" clearable>
              <el-option v-for="project in projectOptions" :key="project.id" :label="project.name" :value="project.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-table :data="coverageList" v-loading="loading" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="project_id" label="项目ID" width="100" />
          <el-table-column prop="covered_lines" label="已覆盖行数" width="120" />
          <el-table-column prop="total_lines" label="总行数" width="100" />
          <el-table-column prop="coverage_rate" label="覆盖率" width="120">
            <template #default="{ row }">
              <el-tag :type="getCoverageType(row.coverage_rate)">{{ row.coverage_rate.toFixed(1) }}%</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="report_date" label="报告日期" width="150" />
          <el-table-column label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.created_at) }}
            </template>
          </el-table-column>
          <el-table-column label="修改时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.updated_at) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="pagination-row">
      <el-col :span="24">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-col>
    </el-row>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form ref="coverageFormRef" :model="coverageForm" :rules="coverageRules" label-width="100px">
        <el-form-item label="项目ID" prop="project_id">
          <el-input type="number" v-model="coverageForm.project_id" placeholder="请输入项目ID" />
        </el-form-item>
        <el-form-item label="已覆盖行数" prop="covered_lines">
          <el-input type="number" v-model="coverageForm.covered_lines" placeholder="请输入已覆盖行数" />
        </el-form-item>
        <el-form-item label="总行数" prop="total_lines">
          <el-input type="number" v-model="coverageForm.total_lines" placeholder="请输入总行数" />
        </el-form-item>
        <el-form-item label="报告日期" prop="report_date">
          <el-date-picker v-model="coverageForm.report_date" type="date" placeholder="选择报告日期" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { Plus, PieChart } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { getProjectOptions } from '@/api/project'

interface Coverage {
  id: number | null
  project_id: number
  covered_lines: number
  total_lines: number
  coverage_rate: number
  report_date: string
  created_at: string
  updated_at: string
}

interface CoverageStats {
  totalCovered: number
  totalLines: number
  avgCoverage: number
  coverageHistory: { date: string; rate: number }[]
}

interface CoverageQueryParams {
  project_id: number | null
}

const filterForm = reactive<CoverageQueryParams>({
  project_id: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const loading = ref(false)
const submitLoading = ref(false)
const coverageList = ref<Coverage[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑覆盖率' : '录入覆盖率')

/**
 * 项目选项列表
 */
const projectOptions = ref<{ id: number; name: string }[]>([])

const stats = reactive<CoverageStats>({
  totalCovered: 0,
  totalLines: 10000,
  avgCoverage: 0,
  coverageHistory: []
})

const coverageForm = reactive<Coverage>({
  id: null,
  project_id: 1,
  covered_lines: 0,
  total_lines: 1000,
  coverage_rate: 0,
  report_date: new Date().toISOString().split('T')[0],
  created_at: '',
  updated_at: ''
})

const coverageRules: FormRules = {
  project_id: [{ required: true, message: '请输入项目ID', trigger: 'blur' }],
  covered_lines: [{ required: true, message: '请输入已覆盖行数', trigger: 'blur' }],
  total_lines: [{ required: true, message: '请输入总行数', trigger: 'blur' }]
}

const coverageFormRef = ref<FormInstance>()

const getCoverageType = (rate: number) => {
  if (rate >= 80) return 'success'
  if (rate >= 60) return 'warning'
  return 'danger'
}

const loadCoverageList = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    if (filterForm.project_id) {
      params.set('project_id', filterForm.project_id.toString())
    }
    params.set('page', pagination.page.toString())
    params.set('size', pagination.size.toString())
    
    const response = await fetch(`/api/v1/coverage?${params.toString()}`)
    const data = await response.json()
    if (data.code === 0 || data.code === 200) {
      coverageList.value = data.data?.records || data.data?.list || data.records || data.list || []
      pagination.total = data.data?.total || coverageList.value.length
    }
  } catch (error) {
    console.error('加载覆盖率列表失败:', error)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const response = await fetch('/api/v1/coverage/stats')
    const data = await response.json()
    if (data.code === 0 || data.code === 200) {
      const result = data.data || data
      stats.totalCovered = result.totalCovered || 0
      stats.totalLines = result.totalLines || 10000
      stats.avgCoverage = result.avgCoverage || 0
      stats.coverageHistory = result.coverageHistory || []
    }
  } catch (error) {
    console.error('加载覆盖率统计失败:', error)
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: Coverage) => {
  isEdit.value = true
  Object.assign(coverageForm, row)
  dialogVisible.value = true
}

const handleDelete = async (row: Coverage) => {
  try {
    await ElMessageBox.confirm(`确定要删除覆盖率记录吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await fetch(`/api/v1/coverage/${row.id}`, { method: 'DELETE' })
    const data = await response.json()
    if (data.code === 0 || data.code === 200) {
      ElMessage.success('删除成功')
      loadCoverageList()
      loadStats()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除覆盖率记录失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!coverageFormRef.value) return
  
  await coverageFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const method = isEdit.value ? 'PUT' : 'POST'
        const url = isEdit.value ? `/api/v1/coverage/${coverageForm.id}` : '/api/v1/coverage'
        const response = await fetch(url, {
          method,
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(coverageForm)
        })
        const data = await response.json()
        
        if (data.code === 0 || data.code === 200) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadCoverageList()
          loadStats()
        }
      } catch (error) {
        console.error('提交失败:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  coverageFormRef.value?.resetFields()
  Object.assign(coverageForm, {
    id: null,
    project_id: 1,
    covered_lines: 0,
    total_lines: 1000,
    coverage_rate: 0,
    report_date: new Date().toISOString().split('T')[0],
    created_at: '',
    updated_at: ''
  })
}

const handleDialogClose = () => {
  resetForm()
}

const handleSearch = () => {
  pagination.page = 1
  loadCoverageList()
}

const handleReset = () => {
  filterForm.project_id = null
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  loadCoverageList()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadCoverageList()
}

onMounted(async () => {
  loadCoverageList()
  loadStats()
  try {
    projectOptions.value = await getProjectOptions()
  } catch (error) {
    console.error('加载选项失败:', error)
  }
})
</script>

<style scoped>
.coverage-container {
  padding: 20px;
}

.header-row {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.stat-card {
  text-align: center;
}

.stat-card.highlight {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.highlight .stat-label,
.stat-card.highlight .stat-unit,
.stat-card.highlight .stat-value {
  color: #fff;
}

.stat-header {
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 5px;
}

.stat-value.coverage-rate {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
}

.rate-number {
  font-size: 48px;
}

.rate-unit {
  font-size: 24px;
}

.stat-unit {
  font-size: 14px;
  color: #909399;
}

.stat-bar {
  margin-top: 10px;
}

.stat-bar :deep(.el-progress-bar__outer) {
  background: rgba(255, 255, 255, 0.3);
}

.trend-container {
  padding: 10px;
}

.trend-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.trend-date {
  width: 50px;
  font-size: 12px;
  color: #909399;
}

.trend-bar-wrapper {
  flex: 1;
  height: 20px;
  background: #f2f6fc;
  border-radius: 10px;
  overflow: hidden;
}

.trend-bar {
  height: 100%;
  background: linear-gradient(90deg, #409EFF 0%, #67c23a 100%);
  border-radius: 10px;
  transition: width 0.3s ease;
}

.trend-rate {
  width: 50px;
  font-size: 12px;
  font-weight: 600;
  color: #303133;
  text-align: right;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 0;
  color: #909399;
}

.empty-state p {
  margin-top: 10px;
  font-size: 14px;
}

.distribution {
  padding: 10px;
}

.distribution-item {
  margin-bottom: 15px;
}

.distribution-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.distribution-bar {
  height: 8px;
  background: #f2f6fc;
  border-radius: 4px;
  overflow: hidden;
}

.distribution-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

.distribution-fill.covered {
  background: linear-gradient(90deg, #67c23a 0%, #85ce61 100%);
}

.distribution-fill.uncovered {
  background: linear-gradient(90deg, #f56c6c 0%, #f89898 100%);
}

.distribution-value {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-top: 5px;
  text-align: right;
}

.filter-row {
  margin-bottom: 20px;
}

.pagination-row {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>