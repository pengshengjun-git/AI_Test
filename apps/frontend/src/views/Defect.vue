<template>
  <div class="defect-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">缺陷管理</h2>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            创建缺陷
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="缺陷标题">
            <el-input v-model="filterForm.title" placeholder="请输入缺陷标题" clearable />
          </el-form-item>
          <el-form-item label="严重程度">
            <el-select v-model="filterForm.severity" placeholder="请选择严重程度" clearable>
              <el-option label="全部" value="" />
              <el-option label="致命" value="FATAL" />
              <el-option label="严重" value="SERIOUS" />
              <el-option label="一般" value="NORMAL" />
              <el-option label="轻微" value="SLIGHT" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="新建" value="NEW" />
              <el-option label="确认中" value="CONFIRMING" />
              <el-option label="修复中" value="FIXING" />
              <el-option label="已修复" value="FIXED" />
              <el-option label="已关闭" value="CLOSED" />
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
        <el-table :data="defectList" v-loading="loading" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="title" label="缺陷标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="severity" label="严重程度" width="100">
            <template #default="{ row }">
              <el-tag :type="getSeverityType(row.severity)">{{ getSeverityText(row.severity) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="关联项目" width="120">
            <template #default="{ row }">
              <span>{{ row.project_name || row.project_id || row.projectId || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="关联需求" width="120">
            <template #default="{ row }">
              <span>{{ row.requirement_name || row.requirement_id || row.requirementId || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="handler" label="处理人" width="100" />
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
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="success" size="small" @click="handleAssign(row)">分配</el-button>
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
      width="800px"
      @close="handleDialogClose"
    >
      <el-form ref="defectFormRef" :model="defectForm" :rules="defectRules" label-width="100px">
        <el-form-item label="缺陷标题" prop="title">
          <InputWithLimit v-model="defectForm.title" placeholder="请输入缺陷标题" :maxlength="100" />
        </el-form-item>
        <el-form-item label="严重程度" prop="severity">
          <el-select v-model="defectForm.severity" placeholder="请选择严重程度">
            <el-option label="致命" value="FATAL" />
            <el-option label="严重" value="SERIOUS" />
            <el-option label="一般" value="NORMAL" />
            <el-option label="轻微" value="SLIGHT" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="defectForm.priority" placeholder="请选择优先级">
            <el-option label="P0" value="P0" />
            <el-option label="P1" value="P1" />
            <el-option label="P2" value="P2" />
            <el-option label="P3" value="P3" />
          </el-select>
        </el-form-item>
        <el-form-item label="缺陷模块" prop="module">
          <InputWithLimit v-model="defectForm.module" placeholder="请输入缺陷模块" :maxlength="50" />
        </el-form-item>
        <el-form-item label="关联项目" prop="project_id">
          <el-select v-model="defectForm.project_id" placeholder="请选择关联项目">
            <el-option v-for="project in projectOptions" :key="project.id" :label="project.name" :value="project.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联需求" prop="requirement_id">
          <el-select v-model="defectForm.requirement_id" placeholder="请选择关联需求" clearable>
            <el-option v-for="req in requirementOptions" :key="req.id" :label="req.name" :value="req.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="复现步骤" prop="steps">
          <InputWithLimit v-model="defectForm.steps" type="textarea" :rows="4" placeholder="请输入复现步骤" :maxlength="500" />
        </el-form-item>
        <el-form-item label="预期结果" prop="expectedResult">
          <InputWithLimit v-model="defectForm.expectedResult" type="textarea" :rows="2" placeholder="请输入预期结果" :maxlength="500" />
        </el-form-item>
        <el-form-item label="实际结果" prop="actualResult">
          <InputWithLimit v-model="defectForm.actualResult" type="textarea" :rows="2" placeholder="请输入实际结果" :maxlength="500" />
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
import { reactive, ref, computed, onMounted, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getDefectList, createDefect, updateDefect, deleteDefect, assignDefect } from '@/api/defect'
import type { Defect, DefectQueryParams } from '@/api/defect'
import { getPriorityText } from '@/utils/statusMap'
import { formatDateTime } from '@/utils/format'
import { getProjectOptions } from '@/api/project'
import { getRequirementOptions } from '@/api/requirement'
import InputWithLimit from '@/components/InputWithLimit.vue'

/**
 * 过滤器表单
 */
const filterForm = reactive<DefectQueryParams>({
  title: '',
  severity: '',
  status: ''
})

/**
 * 分页配置
 */
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

/**
 * 加载状态
 */
const loading = ref(false)
const submitLoading = ref(false)

/**
 * 缺陷列表数据
 */
const defectList = ref<Defect[]>([])

/**
 * 对话框状态
 */
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑缺陷' : '创建缺陷')

/**
 * 缺陷表单
 */
const defectForm = reactive<Defect & { 
  projectId?: number, 
  requirementId?: number, 
  stepsToReproduce?: string,
  createTime?: string,
  updateTime?: string
}>({
  id: undefined,
  title: '',
  severity: 'NORMAL',
  priority: 'P2',
  module: '',
  steps: '',
  stepsToReproduce: '',
  expectedResult: '',
  actualResult: '',
  status: 'NEW',
  project_id: undefined,
  projectId: undefined,
  requirement_id: undefined,
  requirementId: undefined
})

/**
 * 项目选项列表
 */
const projectOptions = ref<{ id: number; name: string }[]>([])

/**
 * 需求选项列表
 */
const requirementOptions = ref<{ id: number; name: string }[]>([])

/**
 * 表单验证规则
 */
const defectRules: FormRules = {
  title: [{ required: true, message: '请输入缺陷标题', trigger: 'blur' }],
  severity: [{ required: true, message: '请选择严重程度', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  project_id: [{ required: true, message: '请选择关联项目', trigger: 'change' }],
  requirement_id: [{ required: true, message: '请选择关联需求', trigger: 'change' }]
}

const defectFormRef = ref<FormInstance>()

/**
 * 获取严重程度标签类型
 */
const getSeverityType = (severity: string) => {
  const severityUpper = severity?.toUpperCase() || ''
  const map: Record<string, string> = {
    'FATAL': 'danger',
    'SERIOUS': 'warning',
    'NORMAL': 'primary',
    'SLIGHT': 'info',
    'CRITICAL': 'danger',
    'HIGH': 'warning',
    'MEDIUM': 'primary',
    'LOW': 'info'
  }
  return map[severityUpper] || map[severity] || 'info'
}

/**
 * 获取严重程度文本
 */
const getSeverityText = (severity: string) => {
  const map: Record<string, string> = {
    'FATAL': '致命',
    'SERIOUS': '严重',
    'NORMAL': '一般',
    'SLIGHT': '轻微'
  }
  return map[severity] || severity
}

/**
 * 获取优先级标签类型
 */
const getPriorityType = (priority: string) => {
  const priorityUpper = priority?.toUpperCase() || ''
  const map: Record<string, string> = {
    'P0': 'danger',
    'P1': 'warning',
    'P2': 'primary',
    'P3': 'info'
  }
  return map[priorityUpper] || map[priority] || 'info'
}

/**
 * 获取状态标签类型
 */
const getStatusType = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const map: Record<string, string> = {
    'NEW': 'info',
    'CONFIRMING': 'warning',
    'FIXING': 'warning',
    'FIXED': 'success',
    'CLOSED': ''
  }
  return map[statusUpper] || map[status] || 'info'
}

/**
 * 获取状态文本
 */
const getStatusText = (status: string) => {
  const map: Record<string, string> = {
    'NEW': '新建',
    'CONFIRMING': '确认中',
    'FIXING': '修复中',
    'FIXED': '已修复',
    'CLOSED': '已关闭'
  }
  return map[status] || status
}

/**
 * 加载缺陷列表
 */
const loadDefectList = async () => {
  loading.value = true
  try {
    const response = await getDefectList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      defectList.value = data.records || data.list || data.items || data || []
      pagination.total = data.total || defectList.value.length
    }
  } catch (error) {
    console.error('加载缺陷列表失败:', error)
  } finally {
    loading.value = false
  }
}

/**
 * 加载需求选项（按项目过滤）
 */
const loadRequirementOptionsByProject = async (projectId?: number) => {
  try {
    if (projectId) {
      requirementOptions.value = await getRequirementOptions(projectId)
    } else {
      requirementOptions.value = []
    }
  } catch (error) {
    console.error('加载需求选项失败:', error)
  }
}

/**
 * 监听项目选择变化
 */
watch(() => defectForm.project_id, (newProjectId) => {
  defectForm.requirement_id = undefined
  defectForm.requirementId = undefined
  loadRequirementOptionsByProject(newProjectId)
})

/**
 * 显示创建对话框
 */
const showCreateDialog = async () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/**
 * 编辑缺陷
 */
const handleEdit = async (row: Defect & { 
  projectId?: number, 
  requirementId?: number, 
  stepsToReproduce?: string 
}) => {
  isEdit.value = true
  const projectId = row.project_id || row.projectId
  Object.assign(defectForm, {
    ...row,
    project_id: projectId,
    projectId: projectId,
    requirement_id: row.requirement_id || row.requirementId,
    requirementId: row.requirement_id || row.requirementId,
    steps: row.steps || row.stepsToReproduce,
    stepsToReproduce: row.steps || row.stepsToReproduce
  })
  // 加载项目对应的需求选项
  if (projectId) {
    await loadRequirementOptionsByProject(projectId)
  }
  dialogVisible.value = true
}

/**
 * 分配缺陷
 */
const handleAssign = async (row: Defect) => {
  try {
    const response = await assignDefect(row.id!, '')
    if (response.code === 200 || response.code === 0) {
      ElMessage.success(`分配缺陷: ${row.title}`)
      loadDefectList()
    }
  } catch (error) {
    ElMessage.info(`分配缺陷: ${row.title}`)
  }
}

/**
 * 删除缺陷
 */
const handleDelete = async (row: Defect) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除缺陷"${row.title}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await deleteDefect(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadDefectList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除缺陷失败:', error)
    }
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!defectFormRef.value) return
  
  await defectFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        // 准备提交数据，统一字段名称
        const submitData = {
          ...defectForm,
          project_id: defectForm.project_id || defectForm.projectId,
          requirement_id: defectForm.requirement_id || defectForm.requirementId,
          steps: defectForm.steps || defectForm.stepsToReproduce,
          stepsToReproduce: defectForm.steps || defectForm.stepsToReproduce
        }
        
        let response
        if (isEdit.value && defectForm.id) {
          response = await updateDefect(defectForm.id, submitData)
        } else {
          response = await createDefect(submitData)
        }
        
        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadDefectList()
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

/**
 * 重置表单
 */
const resetForm = () => {
  defectFormRef.value?.resetFields()
  Object.assign(defectForm, {
    id: null,
    title: '',
    severity: 'NORMAL',
    priority: 'P2',
    module: '',
    steps: '',
    stepsToReproduce: '',
    expectedResult: '',
    actualResult: '',
    status: 'NEW',
    project_id: undefined,
    projectId: undefined,
    requirement_id: undefined,
    requirementId: undefined
  })
}

/**
 * 对话框关闭
 */
const handleDialogClose = () => {
  resetForm()
}

/**
 * 搜索
 */
const handleSearch = () => {
  pagination.page = 1
  loadDefectList()
}

/**
 * 重置
 */
const handleReset = () => {
  filterForm.title = ''
  filterForm.severity = ''
  filterForm.status = ''
  handleSearch()
}

/**
 * 分页大小改变
 */
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadDefectList()
}

/**
 * 页码改变
 */
const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadDefectList()
}

/**
 * 初始化
 */
onMounted(async () => {
  loadDefectList()
  try {
    projectOptions.value = await getProjectOptions()
  } catch (error) {
    console.error('加载选项失败:', error)
  }
})
</script>

<style scoped>
.defect-container {
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
  .defect-container {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .page-title {
    font-size: 20px;
  }
}

@media screen and (max-width: 768px) {
  .defect-container {
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
  
  /* 隐藏一些非必要的列 */
  :deep(.el-table .hidden-mobile) {
    display: none;
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
  .defect-container {
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
  .defect-container {
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