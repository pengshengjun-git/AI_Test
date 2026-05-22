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
          <el-table-column label="项目ID" width="100">
            <template #default="{ row }">{{ row.project_id || row.projectId }}</template>
          </el-table-column>
          <el-table-column label="已覆盖行数" width="120">
            <template #default="{ row }">{{ row.covered_lines || row.coveredLines }}</template>
          </el-table-column>
          <el-table-column label="总行数" width="100">
            <template #default="{ row }">{{ row.total_lines || row.totalLines }}</template>
          </el-table-column>
          <el-table-column label="覆盖率" width="120">
            <template #default="{ row }">
              <el-tag :type="getCoverageType(row.coverage_rate || row.coverageRate || 0)">
                {{ (row.coverage_rate || row.coverageRate || 0).toFixed(1) }}%
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="报告日期" width="150">
            <template #default="{ row }">{{ row.report_date || row.reportDate }}</template>
          </el-table-column>
          <el-table-column label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.created_at || row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="修改时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.updated_at || row.updateTime) }}
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
        <el-form-item label="关联项目" prop="project_id">
          <el-select v-model="coverageForm.project_id" placeholder="请选择关联项目" style="width: 100%">
            <el-option v-for="project in projectOptions" :key="project.id" :label="project.name" :value="project.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="已覆盖行数" prop="covered_lines">
          <el-input-number v-model="coverageForm.covered_lines" :min="0" placeholder="请输入已覆盖行数" style="width: 100%" />
        </el-form-item>
        <el-form-item label="总行数" prop="total_lines">
          <el-input-number v-model="coverageForm.total_lines" :min="1" placeholder="请输入总行数" style="width: 100%" />
        </el-form-item>
        <el-form-item label="报告日期" prop="report_date">
          <el-date-picker v-model="coverageForm.report_date" type="date" placeholder="选择报告日期" style="width: 100%" />
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
import { getCoverageList, getCoverageStats, createCoverage, updateCoverage, deleteCoverage, type Coverage, type CoverageStats, type CoverageQueryParams } from '@/api/coverage'

const filterForm = reactive<CoverageQueryParams>({
  project_id: undefined
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

const coverageForm = reactive<Coverage & { 
  projectId?: number, 
  coveredLines?: number, 
  totalLines?: number, 
  coverageRate?: number, 
  reportDate?: string,
  createTime?: string,
  updateTime?: string
}>({
  id: undefined,
  project_id: 1,
  projectId: 1,
  covered_lines: 0,
  coveredLines: 0,
  total_lines: 1000,
  totalLines: 1000,
  coverage_rate: 0,
  coverageRate: 0,
  report_date: new Date().toISOString().split('T')[0],
  reportDate: new Date().toISOString().split('T')[0],
  created_at: '',
  updated_at: ''
})

const coverageRules: FormRules = {
  project_id: [{ required: true, message: '请选择项目', trigger: 'change' }],
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
    const response = await getCoverageList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      coverageList.value = data.records || data.list || data.items || data || []
      pagination.total = data.total || coverageList.value.length
    }
  } catch (error) {
    console.error('加载覆盖率列表失败:', error)
  } finally {
    loading.value = false
  }
}

const loadStats = async () => {
  try {
    const response = await getCoverageStats()
    if (response.code === 200 || response.code === 0) {
      const result = response.data || response
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

const handleEdit = (row: Coverage & { 
  projectId?: number, 
  coveredLines?: number, 
  totalLines?: number, 
  coverageRate?: number, 
  reportDate?: string 
}) => {
  isEdit.value = true
  Object.assign(coverageForm, {
    ...row,
    project_id: row.project_id || row.projectId,
    projectId: row.project_id || row.projectId,
    covered_lines: row.covered_lines || row.coveredLines,
    coveredLines: row.covered_lines || row.coveredLines,
    total_lines: row.total_lines || row.totalLines,
    totalLines: row.total_lines || row.totalLines,
    coverage_rate: row.coverage_rate || row.coverageRate,
    coverageRate: row.coverage_rate || row.coverageRate,
    report_date: row.report_date || row.reportDate,
    reportDate: row.report_date || row.reportDate
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Coverage) => {
  try {
    await ElMessageBox.confirm(`确定要删除覆盖率记录吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteCoverage(row.id!)
    if (response.code === 0 || response.code === 200) {
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
        // 确保两种格式的字段都有值
        const coveredLinesValue = coverageForm.covered_lines || coverageForm.coveredLines || 0
        const totalLinesValue = coverageForm.total_lines || coverageForm.totalLines || 1
        
        coverageForm.covered_lines = coveredLinesValue
        coverageForm.coveredLines = coveredLinesValue
        coverageForm.total_lines = totalLinesValue
        coverageForm.totalLines = totalLinesValue
        
        // 计算覆盖率
        if (totalLinesValue > 0) {
          const rate = Math.round((coveredLinesValue / totalLinesValue) * 100 * 10) / 10
          coverageForm.coverage_rate = rate
          coverageForm.coverageRate = rate
        }
        
        let response
        if (isEdit.value && coverageForm.id) {
          response = await updateCoverage(coverageForm.id, coverageForm)
        } else {
          response = await createCoverage(coverageForm)
        }
        
        if (response.code === 0 || response.code === 200) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadCoverageList()
          loadStats()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error: any) {
        console.error('提交失败:', error)
        ElMessage.error(error.message || '提交失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const resetForm = () => {
  coverageFormRef.value?.resetFields()
  const today = new Date().toISOString().split('T')[0]
  Object.assign(coverageForm, {
    id: null,
    project_id: 1,
    projectId: 1,
    covered_lines: 0,
    coveredLines: 0,
    total_lines: 1000,
    totalLines: 1000,
    coverage_rate: 0,
    coverageRate: 0,
    report_date: today,
    reportDate: today,
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
  filterForm.project_id = undefined
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
  width: 100%;
  box-sizing: border-box;
}

.header-row {
  margin-bottom: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
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

/* 确保表格宽度正确 */
:deep(.el-table) {
  width: 100% !important;
}

/* 响应式布局 */
@media screen and (max-width: 1200px) {
  .coverage-container {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .page-title {
    font-size: 20px;
  }
}

@media screen and (max-width: 768px) {
  .coverage-container {
    padding: 12px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .page-title {
    font-size: 18px;
  }
  
  /* 优化表格在移动端的显示 */
  :deep(.el-table) {
    font-size: 12px;
  }
  
  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 8px 4px;
  }
  
  /* 优化分页在移动端的显示 */
  :deep(.el-pagination) {
    flex-wrap: wrap;
    justify-content: center;
  }
  
  .pagination-row {
    justify-content: center;
  }
  
  /* 优化筛选表单在移动端的显示 */
  :deep(.el-form--inline .el-form-item) {
    margin-right: 0;
    margin-bottom: 12px;
    width: 100%;
  }
  
  :deep(.el-form--inline .el-form-item__content) {
    width: 100%;
  }
  
  :deep(.el-form--inline .el-input),
  :deep(.el-form--inline .el-select) {
    width: 100%;
  }
}

@media screen and (max-width: 576px) {
  .coverage-container {
    padding: 8px;
  }
  
  .page-title {
    font-size: 16px;
  }
  
  /* 进一步优化移动端表格 */
  :deep(.el-table) {
    font-size: 11px;
  }
  
  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 6px 2px;
  }
  
  /* 优化按钮在移动端的显示 */
  :deep(.el-button) {
    padding: 8px 12px;
    font-size: 12px;
  }
  
  /* 优化对话框在移动端的显示 */
  :deep(.el-dialog) {
    width: 90% !important;
    margin: 5vh auto !important;
  }
  
  /* 优化统计卡片在移动端的显示 */
  .rate-number {
    font-size: 36px;
  }
  
  .rate-unit {
    font-size: 18px;
  }
}

@media screen and (max-width: 480px) {
  .coverage-container {
    padding: 6px;
  }
  
  .header-row {
    margin-bottom: 12px;
  }
  
  .filter-row {
    margin-bottom: 12px;
  }
  
  .pagination-row {
    margin-top: 12px;
  }
}
</style>