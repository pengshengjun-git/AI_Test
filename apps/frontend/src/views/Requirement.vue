<template>
  <div class="requirement-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">需求管理</h2>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新建需求
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="需求名称">
            <el-input v-model="filterForm.name" placeholder="请输入需求名称" clearable />
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="filterForm.priority" placeholder="请选择优先级" clearable>
              <el-option label="全部" value="" />
              <el-option label="P0" value="P0" />
              <el-option label="P1" value="P1" />
              <el-option label="P2" value="P2" />
              <el-option label="P3" value="P3" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="草稿" value="draft" />
              <el-option label="待评审" value="pending" />
              <el-option label="已批准" value="approved" />
              <el-option label="进行中" value="in_progress" />
              <el-option label="已完成" value="completed" />
              <el-option label="已拒绝" value="rejected" />
              <el-option label="已关闭" value="closed" />
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
        <el-table :data="requirementList" v-loading="loading" style="width: 100%" border stripe>
          <el-table-column type="index" label="序号" width="70" index-method="(index) => index + 1" />
          <el-table-column prop="name" label="需求名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="type" label="需求类型" width="100">
            <template #default="{ row }">
              {{ getTypeText(row.type) }}
            </template>
          </el-table-column>
          <el-table-column prop="priority" label="优先级" width="90">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="90">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getRequirementStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="projectName" label="关联项目" min-width="120" show-overflow-tooltip>
            <template #default="{ row }">
              {{ row.projectName || getProjectName(row.projectId || row.project_id) || '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="creatorName" label="创建人" width="90" show-overflow-tooltip>
            <template #default="{ row }">
              {{ row.creatorName || row.proposer || (row.proposerId ? '用户' + row.proposerId : '-') }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime || row.created_at) }}
            </template>
          </el-table-column>
          <el-table-column label="修改时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.updateTime || row.updated_at) }}
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
      width="800px"
      @close="handleDialogClose"
    >
      <el-form ref="requirementFormRef" :model="requirementForm" :rules="requirementRules" label-width="120px">
        <el-form-item label="需求名称" prop="name">
          <InputWithLimit v-model="requirementForm.name" placeholder="请输入需求名称" :maxlength="100" />
        </el-form-item>
        <el-form-item label="需求类型" prop="type">
          <el-select v-model="requirementForm.type" placeholder="请选择需求类型">
            <el-option label="功能需求" value="feature" />
            <el-option label="非功能需求" value="non_feature" />
            <el-option label="Bug修复" value="bug_fix" />
            <el-option label="技术债务" value="tech_debt" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="requirementForm.priority" placeholder="请选择优先级">
            <el-option label="P0-紧急" value="P0" />
            <el-option label="P1-高" value="P1" />
            <el-option label="P2-中" value="P2" />
            <el-option label="P3-低" value="P3" />
          </el-select>
        </el-form-item>
        <el-form-item label="需求来源" prop="source">
          <el-select v-model="requirementForm.source" placeholder="请选择需求来源">
            <el-option label="内部需求" value="internal" />
            <el-option label="客户需求" value="customer" />
            <el-option label="市场调研" value="market" />
            <el-option label="技术改进" value="tech" />
          </el-select>
        </el-form-item>
        <el-form-item label="需求描述" prop="description">
          <InputWithLimit v-model="requirementForm.description" type="textarea" :rows="3" placeholder="请输入需求描述" :maxlength="500" />
        </el-form-item>
        <el-form-item label="提出人" prop="proposer">
          <InputWithLimit v-model="requirementForm.proposer" placeholder="请输入提出人" :maxlength="50" />
        </el-form-item>
        <el-form-item label="生效版本" prop="effective_version">
          <InputWithLimit v-model="requirementForm.effective_version" placeholder="请输入生效版本" :maxlength="20" />
        </el-form-item>
        <el-form-item label="验收标准" prop="acceptance_criteria">
          <InputWithLimit v-model="requirementForm.acceptance_criteria" type="textarea" :rows="3" placeholder="请输入验收标准" :maxlength="500" />
        </el-form-item>
        <el-form-item label="关联项目" prop="projectId">
          <el-select v-model="requirementForm.projectId" placeholder="请选择项目">
            <el-option v-for="project in projectList" :key="project.id" :label="project.name" :value="project.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="owner">
          <InputWithLimit v-model="requirementForm.owner" placeholder="请输入负责人" :maxlength="50" />
        </el-form-item>
        <el-form-item label="审核人" prop="reviewer">
          <InputWithLimit v-model="requirementForm.reviewer" placeholder="请输入审核人" :maxlength="50" />
        </el-form-item>
        <el-form-item label="需求状态" prop="status">
          <el-select v-model="requirementForm.status" placeholder="请选择需求状态">
            <el-option label="草稿" value="draft" />
            <el-option label="待评审" value="pending" />
            <el-option label="已批准" value="approved" />
            <el-option label="进行中" value="in_progress" />
            <el-option label="已完成" value="completed" />
            <el-option label="已拒绝" value="rejected" />
            <el-option label="已关闭" value="closed" />
          </el-select>
        </el-form-item>
        <el-form-item label="评审结果" prop="review_result">
          <el-select v-model="requirementForm.review_result" placeholder="请选择评审结果">
            <el-option label="通过" value="approved" />
            <el-option label="拒绝" value="rejected" />
            <el-option label="待定" value="pending" />
          </el-select>
        </el-form-item>
        <el-form-item label="评审意见" prop="review_comments">
          <InputWithLimit v-model="requirementForm.review_comments" type="textarea" :rows="2" placeholder="请输入评审意见" :maxlength="500" />
        </el-form-item>
        <el-form-item label="上线时间" prop="online_time">
          <el-date-picker v-model="requirementForm.online_time" type="datetime" placeholder="选择上线时间" />
        </el-form-item>
        <el-form-item label="关闭原因" prop="close_reason">
          <InputWithLimit v-model="requirementForm.close_reason" placeholder="请输入关闭原因" :maxlength="200" />
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
import { getRequirementStatusText, getPriorityText } from '@/utils/statusMap'
import InputWithLimit from '@/components/InputWithLimit.vue'
import TooltipText from '@/components/TooltipText.vue'
import { formatDateTime } from '@/utils/format'
import { getRequirementList, createRequirement, updateRequirement, deleteRequirement } from '@/api/requirement'
import { getProjectOptions } from '@/api/project'

interface Requirement {
  id: number | null
  name: string
  title?: string
  description: string
  type: string
  priority: string
  source: string
  proposer: string
  proposer_time: string
  effective_version: string
  acceptance_criteria: string
  projectId: number
  project_id?: number
  owner: string
  reviewer: string
  permission_scope: string
  status: string
  review_result: string
  review_comments: string
  online_time: string
  close_reason: string
  createTime?: string
  updateTime?: string
  created_at?: string
  updated_at?: string
}

interface RequirementQueryParams {
  name: string
  status: string
  priority: string
}

const filterForm = reactive<RequirementQueryParams>({
  name: '',
  status: '',
  priority: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const loading = ref(false)
const submitLoading = ref(false)
const requirementList = ref<Requirement[]>([])
const projectList = ref<{ id: number; name: string }[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑需求' : '新建需求')

const requirementForm = reactive<Requirement>({
  id: null,
  name: '',
  description: '',
  type: 'feature',
  priority: 'P2',
  source: 'internal',
  proposer: '',
  proposer_time: new Date().toISOString(),
  effective_version: '',
  acceptance_criteria: '',
  projectId: 1,
  project_id: 1,
  owner: '',
  reviewer: '',
  permission_scope: 'public',
  status: 'draft',
  review_result: '',
  review_comments: '',
  online_time: '',
  close_reason: '',
  createTime: '',
  updateTime: '',
  created_at: '',
  updated_at: ''
})

const requirementRules: FormRules = {
  name: [{ required: true, message: '请输入需求名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择需求类型', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  projectId: [{ required: true, message: '请选择关联项目', trigger: 'change' }]
}

const requirementFormRef = ref<FormInstance>()

const getTypeText = (type: string) => {
  const map: Record<string, string> = {
    'feature': '功能需求',
    'non_feature': '非功能需求',
    'bug_fix': 'Bug修复',
    'tech_debt': '技术债务'
  }
  return map[type] || type
}

const getPriorityType = (priority: string) => {
  const map: Record<string, string> = {
    'P0': 'danger',
    'P1': 'warning',
    'P2': 'primary',
    'P3': 'info'
  }
  return map[priority] || 'info'
}

const getStatusType = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'draft': 'info',
    'DRAFT': 'info',
    'pending': 'warning',
    'PENDING': 'warning',
    'approved': 'success',
    'APPROVED': 'success',
    'in_progress': 'primary',
    'IN_PROGRESS': 'primary',
    'completed': 'success',
    'COMPLETED': 'success',
    'rejected': 'danger',
    'REJECTED': 'danger',
    'closed': 'warning',
    'CLOSED': 'warning'
  }
  return map[status] || map[statusUpper] || map[statusLower] || 'info'
}

const getProjectName = (projectId: number | string | undefined): string => {
  if (!projectId) return '-'
  const id = typeof projectId === 'string' ? parseInt(projectId, 10) : projectId
  const project = projectList.value.find(p => p.id === id)
  return project?.name || (projectId?.toString() || '-')
}

const loadRequirementList = async () => {
  loading.value = true
  try {
    const response = await getRequirementList({
      name: filterForm.name,
      status: filterForm.status,
      priority: filterForm.priority,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      const list = data.records || data.list || data.items || []
      requirementList.value = list.map((item: any) => ({
        ...item,
        name: item.title || item.name
      }))
      pagination.total = data.total || requirementList.value.length
    }
  } catch (error) {
    console.error('加载需求列表失败:', error)
  } finally {
    loading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row: Requirement) => {
  isEdit.value = true
  Object.assign(requirementForm, {
    ...row,
    name: row.name || row.title,
    projectId: row.projectId || row.project_id,
    project_id: row.projectId || row.project_id
  })
  dialogVisible.value = true
}

const handleDelete = async (row: Requirement) => {
  try {
    await ElMessageBox.confirm(`确定要删除需求"${row.name}"吗？`, '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await deleteRequirement(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadRequirementList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除需求失败:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!requirementFormRef.value) return
  
  await requirementFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const submitData: any = {
          title: requirementForm.name,
          projectId: requirementForm.projectId,
          description: requirementForm.description,
          type: requirementForm.type,
          priority: requirementForm.priority,
          source: requirementForm.source,
          status: requirementForm.status,
          proposer: requirementForm.proposer,
          proposerTime: requirementForm.proposer_time,
          effectiveVersion: requirementForm.effective_version,
          acceptanceCriteria: requirementForm.acceptance_criteria,
          owner: requirementForm.owner,
          reviewer: requirementForm.reviewer,
          permissionScope: requirementForm.permission_scope,
          reviewResult: requirementForm.review_result,
          reviewComments: requirementForm.review_comments,
          onlineTime: requirementForm.online_time,
          closeReason: requirementForm.close_reason
        }
        
        let response
        if (isEdit.value && requirementForm.id) {
          // 编辑时更新
          response = await updateRequirement(requirementForm.id, submitData)
        } else {
          // 新建时，确保不包含id字段
          const createData = { ...submitData }
          delete createData.id
          response = await createRequirement(createData)
        }
        
        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadRequirementList()
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
  requirementFormRef.value?.resetFields()
  Object.assign(requirementForm, {
    id: null,
    name: '',
    description: '',
    type: 'feature',
    priority: 'P2',
    source: 'internal',
    proposer: '',
    proposer_time: new Date().toISOString(),
    effective_version: '',
    acceptance_criteria: '',
    projectId: null,
    project_id: null,
    owner: '',
    reviewer: '',
    permission_scope: 'public',
    status: 'draft',
    review_result: '',
    review_comments: '',
    online_time: '',
    close_reason: '',
    createTime: '',
    updateTime: '',
    created_at: '',
    updated_at: ''
  })
}

const handleDialogClose = () => {
  resetForm()
}

const handleSearch = () => {
  pagination.page = 1
  loadRequirementList()
}

const handleReset = () => {
  filterForm.name = ''
  filterForm.status = ''
  filterForm.priority = ''
  handleSearch()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  loadRequirementList()
}

const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadRequirementList()
}

const loadProjectList = async () => {
  try {
    const response = await getProjectOptions()
    projectList.value = response
  } catch (error) {
    console.error('加载项目列表失败:', error)
  }
}

onMounted(() => {
  loadRequirementList()
  loadProjectList()
})
</script>

<style scoped>
.requirement-container {
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
  .requirement-container {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .page-title {
    font-size: 20px;
  }
}

@media screen and (max-width: 768px) {
  .requirement-container {
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
  .requirement-container {
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
  .requirement-container {
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
