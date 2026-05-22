<template>
  <div class="testplan-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">测试计划管理</h2>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新建测试计划
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="计划名称">
            <el-input v-model="filterForm.name" placeholder="请输入计划名称" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="草稿" value="draft" />
              <el-option label="规划中" value="planning" />
              <el-option label="执行中" value="executing" />
              <el-option label="已完成" value="completed" />
              <el-option label="已取消" value="cancelled" />
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
        <el-table :data="testplanList" v-loading="loading" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="name" label="计划名称" min-width="200" show-overflow-tooltip />
          <el-table-column label="关联项目" width="150">
            <template #default="{ row }">
              {{ row.project_name || row.project_id || row.projectId || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.start_date || row.startDate) }}
            </template>
          </el-table-column>
          <el-table-column label="结束时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.end_date || row.endDate) }}
            </template>
          </el-table-column>
          <el-table-column prop="owner" label="负责人" width="100">
            <template #default="{ row }">
              {{ row.owner || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime || row.created_at) }}
            </template>
          </el-table-column>
          <el-table-column label="修改时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.updateTime || row.updated_at) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="testplanFormRef" :model="testplanForm" :rules="testplanRules" label-width="100px">
        <el-form-item label="计划名称" prop="name">
          <InputWithLimit v-model="testplanForm.name" placeholder="请输入计划名称" :maxlength="100" />
        </el-form-item>
        <el-form-item label="计划描述" prop="description">
          <InputWithLimit v-model="testplanForm.description" type="textarea" :rows="3" placeholder="请输入计划描述" :maxlength="500" />
        </el-form-item>
        <el-form-item label="关联项目" prop="project_id">
          <el-select v-model="testplanForm.project_id" placeholder="请选择关联项目">
            <el-option v-for="project in projectOptions" :key="project.id" :label="project.name" :value="project.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="计划状态" prop="status">
          <el-select v-model="testplanForm.status" placeholder="请选择计划状态">
            <el-option label="草稿" value="draft" />
            <el-option label="规划中" value="planning" />
            <el-option label="执行中" value="executing" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="start_date">
          <el-date-picker v-model="testplanForm.start_date" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="end_date">
          <el-date-picker v-model="testplanForm.end_date" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
        <el-form-item label="负责人" prop="owner">
          <InputWithLimit v-model="testplanForm.owner" placeholder="请输入负责人" :maxlength="50" />
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
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import InputWithLimit from '@/components/InputWithLimit.vue'
import { formatDateTime } from '@/utils/format'
import { getProjectOptions } from '@/api/project'
import { getTestPlanList, createTestPlan, updateTestPlan, deleteTestPlan } from '@/api/testplan'

interface TestPlan {
  id: number | null
  name: string
  description: string
  project_id?: number
  projectId?: number
  status: string
  start_date?: string
  startDate?: string
  end_date?: string
  endDate?: string
  owner?: string
  created_at?: string
  updated_at?: string
  createTime?: string
  updateTime?: string
}

interface TestPlanQueryParams {
  name: string
  status: string
}

const filterForm = reactive<TestPlanQueryParams>({
  name: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const loading = ref(false)
const submitLoading = ref(false)
const testplanList = ref<TestPlan[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑测试计划' : '新建测试计划')

const testplanForm = reactive<TestPlan>({
  id: null,
  name: '',
  description: '',
  project_id: 1,
  projectId: 1,
  status: 'draft',
  start_date: '',
  startDate: '',
  end_date: '',
  endDate: '',
  owner: '',
  created_at: '',
  updated_at: '',
  createTime: '',
  updateTime: ''
})

/**
 * 项目选项列表
 */
const projectOptions = ref<{ id: number; name: string }[]>([])

const testplanRules: FormRules = {
  name: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择计划状态', trigger: 'change' }]
}

const testplanFormRef = ref<FormInstance>()

const getStatusType = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'draft': 'info',
    'DRAFT': 'info',
    'planning': 'info',
    'PLANNING': 'info',
    'executing': 'primary',
    'EXECUTING': 'primary',
    'running': 'primary',
    'RUNNING': 'primary',
    'completed': 'success',
    'COMPLETED': 'success',
    'finished': 'success',
    'FINISHED': 'success',
    'cancelled': 'warning',
    'CANCELLED': 'warning'
  }
  return map[status] || map[statusUpper] || map[statusLower] || 'info'
}

const getStatusText = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'draft': '草稿',
    'DRAFT': '草稿',
    'planning': '规划中',
    'PLANNING': '规划中',
    'executing': '执行中',
    'EXECUTING': '执行中',
    'running': '执行中',
    'RUNNING': '执行中',
    'completed': '已完成',
    'COMPLETED': '已完成',
    'finished': '已完成',
    'FINISHED': '已完成',
    'cancelled': '已取消',
    'CANCELLED': '已取消'
  }
  return map[status] || map[statusUpper] || map[statusLower] || status || '-'
}

// 防止重复请求的标志
let isLoadingTestPlan = false

const loadTestplanList = async () => {
  if (isLoadingTestPlan) return
  isLoadingTestPlan = true
  loading.value = true
  try {
    const response = await getTestPlanList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      testplanList.value = data.records || data.list || data.items || data || []
      pagination.total = data.total || testplanList.value.length
    } else {
      // 只在真正失败时提示
      ElMessage.error(response.message || '加载测试计划失败')
    }
  } catch (error: any) {
    console.error('加载测试计划列表失败:', error)
    // 避免重复提示，只在非取消错误时提示
    if (error.name !== 'CanceledError' && error.message !== 'canceled') {
      ElMessage.error('加载测试计划失败，请稍后重试')
    }
  } finally {
    loading.value = false
    isLoadingTestPlan = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: TestPlan) => {
  isEdit.value = true
  Object.assign(testplanForm, {
    ...row,
    project_id: row.project_id || row.projectId,
    projectId: row.project_id || row.projectId,
    start_date: row.start_date || row.startDate,
    startDate: row.start_date || row.startDate,
    end_date: row.end_date || row.endDate,
    endDate: row.end_date || row.endDate
  })
  dialogVisible.value = true
}

const handleDelete = async (row: TestPlan) => {
  try {
    await ElMessageBox.confirm(`确定要删除测试计划"${row.name}"吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteTestPlan(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadTestplanList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除测试计划失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!testplanFormRef.value) return
  
  await testplanFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 准备提交数据，确保不包含id（新建时）
        const submitData: any = {
          name: testplanForm.name,
          description: testplanForm.description,
          project_id: testplanForm.project_id || testplanForm.projectId,
          status: testplanForm.status,
          start_date: testplanForm.start_date || testplanForm.startDate,
          end_date: testplanForm.end_date || testplanForm.endDate,
          owner: testplanForm.owner
        }
        
        let response
        if (isEdit.value && testplanForm.id) {
          response = await updateTestPlan(testplanForm.id, submitData)
        } else {
          // 新建时确保有项目id
          if (!submitData.project_id && projectOptions.value.length > 0) {
            submitData.project_id = projectOptions.value[0].id
          }
          response = await createTestPlan(submitData)
        }
        
        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadTestplanList()
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
  testplanFormRef.value?.resetFields()
  Object.assign(testplanForm, {
    id: null,
    name: '',
    description: '',
    project_id: 1,
    projectId: 1,
    status: 'draft',
    start_date: '',
    startDate: '',
    end_date: '',
    endDate: '',
    owner: '',
    created_at: '',
    updated_at: '',
    createTime: '',
    updateTime: ''
  })
}

const handleDialogClose = () => {
  resetForm()
}

const handleSearch = () => {
  pagination.page = 1
  loadTestplanList()
}

const handleReset = () => {
  filterForm.name = ''
  filterForm.status = ''
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  loadTestplanList()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadTestplanList()
}

onMounted(async () => {
  loadTestplanList()
  try {
    projectOptions.value = await getProjectOptions()
  } catch (error) {
    console.error('加载选项失败:', error)
  }
})
</script>

<style scoped>
.testplan-container {
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
  .testplan-container {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .page-title {
    font-size: 20px;
  }
}

@media screen and (max-width: 768px) {
  .testplan-container {
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
  .testplan-container {
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
}

@media screen and (max-width: 480px) {
  .testplan-container {
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
