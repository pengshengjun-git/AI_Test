<template>
  <div class="project-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">项目管理</h2>
          <el-button 
            v-if="hasPermission('project:create') || isAdmin()"
            type="primary" 
            @click="showCreateDialog"
          >
            <el-icon><Plus /></el-icon>
            新建项目
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="项目名称">
            <el-input v-model="filterForm.name" placeholder="请输入项目名称" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="规划中" value="PLANNING" />
              <el-option label="进行中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已归档" value="ARCHIVED" />
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
        <el-table :data="projectList" v-loading="loading" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="name" label="项目名称" min-width="150" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="testcaseCount" label="用例数" width="100" align="center" />
          <el-table-column prop="defectCount" label="缺陷数" width="100" align="center" />
          <el-table-column label="创建人" width="120">
            <template #default="{ row }">
              {{ row.ownerName || (row.ownerId ? '用户' + row.ownerId : '-') }}
            </template>
          </el-table-column>
          <el-table-column label="创建时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime || row.created_at || row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="修改时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.updateTime || row.updated_at || row.updatedAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button 
                v-if="hasPermission('project:update') || isAdmin()"
                type="primary" 
                size="small" 
                @click="handleEdit(row)"
              >编辑</el-button>
              <el-button 
                v-if="hasPermission('project:delete') || isAdmin()"
                type="danger" 
                size="small" 
                @click="handleDelete(row)"
              >删除</el-button>
              <span v-if="!hasPermission('project:update') && !hasPermission('project:delete') && !isAdmin()" class="no-permission">无权限</span>
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
      <el-form ref="projectFormRef" :model="projectForm" :rules="projectRules" label-width="100px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="projectForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目编码">
          <el-input v-model="projectForm.code" placeholder="选填，不填则自动生成" disabled />
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-select v-model="projectForm.status" placeholder="请选择项目状态">
            <el-option label="规划中" value="PLANNING" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="projectForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入项目描述"
          />
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
import { getProjectList, createProject, updateProject, deleteProject } from '@/api/project'
import type { Project, ProjectQueryParams } from '@/api/project'
import { getPriorityText, getStatusText } from '@/utils/statusMap'
import { hasPermission, isAdmin } from '@/utils/permission'

/**
 * 过滤器表单
 */
const filterForm = reactive<ProjectQueryParams>({
  name: '',
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
 * 项目列表数据
 */
const projectList = ref<Project[]>([])

/**
 * 对话框状态
 */
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑项目' : '新建项目')

/**
 * 项目表单
 */
const projectForm = reactive<Project & { ownerId?: number }>({
  id: undefined,
  name: '',
  code: '',
  status: 'PLANNING',
  description: '',
  ownerId: 1
})

/**
 * 表单验证规则
 */
const projectRules: FormRules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择项目状态', trigger: 'change' }]
}

const projectFormRef = ref<FormInstance>()

/**
 * 日期格式化
 */
const formatDateTime = (dateStr: string | undefined): string => {
  if (!dateStr) return '-'
  try {
    const date = new Date(dateStr)
    if (isNaN(date.getTime())) return dateStr
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
  } catch {
    return dateStr
  }
}

/**
 * 获取状态标签类型
 */
const getStatusType = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'PLANNING': 'info',
    'IN_PROGRESS': 'primary',
    'COMPLETED': 'success',
    'ARCHIVED': 'warning',
    'ACTIVE': 'primary',
    'DRAFT': 'warning',
    '规划中': 'info',
    '进行中': 'primary',
    '已完成': 'success',
    '已归档': 'warning'
  }
  return map[statusUpper] || map[status] || map[statusLower] || 'info'
}

/**
 * 加载项目列表
 */
const loadProjectList = async () => {
  loading.value = true
  try {
    const response = await getProjectList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      projectList.value = data.records || data.list || data.items || data || []
      pagination.total = data.total || projectList.value.length
    }
  } catch (error) {
    console.error('加载项目列表失败:', error)
  } finally {
    loading.value = false
  }
}

/**
 * 显示创建对话框
 */
const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/**
 * 编辑项目
 */
const handleEdit = (row: Project) => {
  isEdit.value = true
  Object.assign(projectForm, row)
  dialogVisible.value = true
}

/**
 * 删除项目
 */
const handleDelete = async (row: Project) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除项目"${row.name}"吗？删除后不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await deleteProject(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadProjectList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除项目失败:', error)
    }
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!projectFormRef.value) return
  
  await projectFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (isEdit.value) {
          response = await updateProject(projectForm.id!, projectForm)
        } else {
          response = await createProject(projectForm)
        }
        
        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadProjectList()
        }
      } catch (error) {
        console.error('提交失败:', error)
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
  projectFormRef.value?.resetFields()
  Object.assign(projectForm, {
    id: null,
    name: '',
    code: '',
    status: 'PLANNING',
    description: '',
    ownerId: 1
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
  loadProjectList()
}

/**
 * 重置
 */
const handleReset = () => {
  filterForm.name = ''
  filterForm.status = ''
  handleSearch()
}

/**
 * 分页大小改变
 */
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadProjectList()
}

/**
 * 页码改变
 */
const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadProjectList()
}

/**
 * 初始化
 */
onMounted(() => {
  loadProjectList()
})
</script>

<style scoped>
.project-container {
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

.filter-row {
  margin-bottom: 20px;
}

.pagination-row {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.no-permission {
  color: #909399;
  font-size: 12px;
}
</style>
