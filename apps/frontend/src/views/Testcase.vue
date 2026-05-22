<template>
  <div class="testcase-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">测试用例管理</h2>
          <div class="header-actions">
            <el-button type="primary" @click="showCreateDialog">
              <el-icon><Plus /></el-icon>
              新建用例
            </el-button>
            <el-button type="success" @click="handleAIGenerate">
              <el-icon><Cpu /></el-icon>
              AI生成
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="用例名称">
            <el-input v-model="filterForm.name" placeholder="请输入用例名称" clearable />
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
              <el-option label="待评审" value="PENDING_REVIEW" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="执行中" value="EXECUTING" />
              <el-option label="已完成" value="COMPLETED" />
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
        <el-table :data="testcaseList" v-loading="loading" style="width: 100%">
          <el-table-column type="selection" width="55" />
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="title" label="用例标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="类型" width="100">
            <template #default="{ row }">
              <span>{{ getTypeText(row.type) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="test_status" label="测试状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getTestStatusType(row.test_status)">{{ getTestStatusText(row.test_status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="test_module" label="测试模块" width="120" show-overflow-tooltip />
          <el-table-column label="关联项目" width="120">
            <template #default="{ row }">
              <span>{{ row.project_name || row.project_id || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="关联需求" width="120">
            <template #default="{ row }">
              <span>{{ row.requirement_name || row.requirement_id || '-' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="creator" label="创建人" width="100" />
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
              <el-button type="success" size="small" @click="handleExecute(row)">执行</el-button>
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
      <el-form ref="testcaseFormRef" :model="testcaseForm" :rules="testcaseRules" label-width="100px">
        <el-form-item label="用例标题" prop="title">
          <InputWithLimit v-model="testcaseForm.title" placeholder="请输入用例标题" :maxlength="100" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="testcaseForm.priority" placeholder="请选择优先级">
            <el-option label="P0" value="P0" />
            <el-option label="P1" value="P1" />
            <el-option label="P2" value="P2" />
            <el-option label="P3" value="P3" />
          </el-select>
        </el-form-item>
        <el-form-item label="用例类型" prop="type">
          <el-select v-model="testcaseForm.type" placeholder="请选择用例类型">
            <el-option label="功能测试" value="FUNCTIONAL" />
            <el-option label="接口测试" value="API" />
            <el-option label="性能测试" value="PERFORMANCE" />
            <el-option label="安全测试" value="SECURITY" />
            <el-option label="UI测试" value="UI" />
          </el-select>
        </el-form-item>
        <el-form-item label="测试状态" prop="test_status" required>
          <el-select v-model="testcaseForm.test_status" placeholder="请选择测试状态">
            <el-option label="待测试" value="待测试" />
            <el-option label="测试通过" value="测试通过" />
            <el-option label="测试失败" value="测试失败" />
          </el-select>
        </el-form-item>
        <el-form-item label="测试模块" prop="test_module">
          <InputWithLimit v-model="testcaseForm.test_module" placeholder="请输入测试模块" :maxlength="50" />
        </el-form-item>
        <el-form-item label="关联项目" prop="project_id">
          <el-select v-model="testcaseForm.project_id" placeholder="请选择关联项目">
            <el-option v-for="project in projectOptions" :key="project.id" :label="project.name" :value="project.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联需求" prop="requirement_id">
          <el-select v-model="testcaseForm.requirement_id" placeholder="请选择关联需求" clearable>
            <el-option v-for="req in requirementOptions" :key="req.id" :label="req.name" :value="req.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="前置条件" prop="precondition">
          <InputWithLimit v-model="testcaseForm.precondition" type="textarea" :rows="2" placeholder="请输入前置条件" :maxlength="500" />
        </el-form-item>
        <el-form-item label="测试步骤" prop="steps">
          <InputWithLimit v-model="testcaseForm.steps" type="textarea" :rows="4" placeholder="请输入测试步骤" :maxlength="500" />
        </el-form-item>
        <el-form-item label="预期结果" prop="expectedResult">
          <InputWithLimit v-model="testcaseForm.expectedResult" type="textarea" :rows="2" placeholder="请输入预期结果" :maxlength="500" />
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
import { Plus, Cpu } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { getTestcaseList, createTestcase, updateTestcase, deleteTestcase, executeTestcase, generateTestcaseByAI } from '@/api/testcase'
import type { Testcase, TestcaseQueryParams } from '@/api/testcase'
import { getPriorityText, getTestStatusText } from '@/utils/statusMap'
import { formatDateTime } from '@/utils/format'
import { getProjectOptions } from '@/api/project'
import { getRequirementOptions } from '@/api/requirement'
import InputWithLimit from '@/components/InputWithLimit.vue'

/**
 * 过滤器表单
 */
const filterForm = reactive<TestcaseQueryParams>({
  name: '',
  priority: '',
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
 * 测试用例列表数据
 */
const testcaseList = ref<Testcase[]>([])

/**
 * 对话框状态
 */
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑用例' : '新建用例')

/**
 * 用例表单
 */
const testcaseForm = reactive<Testcase>({
  id: undefined,
  title: '',
  priority: 'P2',
  type: 'FUNCTIONAL',
  precondition: '',
  steps: '',
  expectedResult: '',
  status: 'PENDING_REVIEW',
  test_status: '待测试',
  test_module: '',
  requirement_id: undefined,
  project_id: 1
})

/**
 * 表单验证规则
 */
const testcaseRules: FormRules = {
  title: [{ required: true, message: '请输入用例标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  type: [{ required: true, message: '请选择用例类型', trigger: 'change' }]
}

const testcaseFormRef = ref<FormInstance>()

/**
 * 项目选项列表
 */
const projectOptions = ref<{ id: number; name: string }[]>([])

/**
 * 需求选项列表
 */
const requirementOptions = ref<{ id: number; name: string }[]>([])

/**
 * 获取优先级标签类型
 */
const getPriorityType = (priority: string) => {
  const priorityUpper = priority.toUpperCase()
  const map: Record<string, string> = {
    'P0': 'danger',
    'P1': 'warning',
    'P2': 'primary',
    'P3': 'info',
    'CRITICAL': 'danger',
    'HIGH': 'warning',
    'MEDIUM': 'primary',
    'LOW': 'info'
  }
  return map[priorityUpper] || map[priority] || 'info'
}

/**
 * 获取类型文本
 */
const getTypeText = (type: string) => {
  const map: Record<string, string> = {
    'FUNCTIONAL': '功能测试',
    'API': '接口测试',
    'PERFORMANCE': '性能测试',
    'SECURITY': '安全测试',
    'UI': 'UI测试'
  }
  return map[type] || type
}

/**
 * 获取状态标签类型
 */
const getStatusType = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'PENDING_REVIEW': 'info',
    'pending_review': 'info',
    'APPROVED': 'success',
    'approved': 'success',
    'EXECUTING': 'warning',
    'executing': 'warning',
    'COMPLETED': 'primary',
    'completed': 'primary',
    'DRAFT': 'info',
    'draft': 'info'
  }
  return map[status] || map[statusUpper] || map[statusLower] || 'info'
}

/**
 * 获取状态文本
 */
const getStatusText = (status: string) => {
  const statusUpper = status?.toUpperCase() || ''
  const statusLower = status?.toLowerCase() || ''
  const map: Record<string, string> = {
    'PENDING_REVIEW': '待评审',
    'pending_review': '待评审',
    'APPROVED': '已通过',
    'approved': '已通过',
    'EXECUTING': '执行中',
    'executing': '执行中',
    'COMPLETED': '已完成',
    'completed': '已完成',
    'DRAFT': '草稿',
    'draft': '草稿'
  }
  return map[status] || map[statusUpper] || map[statusLower] || status || '-'
}

/**
 * 获取测试状态标签类型
 */
const getTestStatusType = (status: string) => {
  const map: Record<string, string> = {
    '待测试': 'info',
    '测试通过': 'success',
    '测试失败': 'danger'
  }
  return map[status] || 'info'
}

/**
 * 加载用例列表
 */
const loadTestcaseList = async () => {
  loading.value = true
  try {
    const response = await getTestcaseList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      testcaseList.value = data.records || data.list || data.items || data || []
      pagination.total = data.total || testcaseList.value.length
    }
  } catch (error) {
    console.error('加载用例列表失败:', error)
    ElMessage.error('加载用例列表失败，请稍后重试')
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
 * 编辑用例
 */
const handleEdit = (row: Testcase) => {
  isEdit.value = true
  Object.assign(testcaseForm, row)
  if (row.expectedResult !== undefined && !testcaseForm.expectedResult) {
    testcaseForm.expectedResult = row.expectedResult
  }
  if (row.steps === undefined) {
    testcaseForm.steps = ''
  }
  if (row.precondition === undefined) {
    testcaseForm.precondition = ''
  }
  dialogVisible.value = true
}

/**
 * 执行用例
 */
const handleExecute = async (row: Testcase) => {
  try {
    const response = await executeTestcase(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success(`开始执行用例: ${row.title}`)
      loadTestcaseList()
    }
  } catch (error) {
    console.error('执行用例失败:', error)
  }
}

/**
 * 删除用例
 */
const handleDelete = async (row: Testcase) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用例"${row.title}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await deleteTestcase(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadTestcaseList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用例失败:', error)
    }
  }
}

/**
 * AI生成用例
 */
const handleAIGenerate = async () => {
  try {
    const response: any = await generateTestcaseByAI({ projectId: 1, requirement: '' })
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('AI生成成功')
      loadTestcaseList()
    }
  } catch (error) {
    ElMessage.error('AI生成功能开发中')
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!testcaseFormRef.value) return
  
  await testcaseFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (isEdit.value) {
          response = await updateTestcase(testcaseForm.id!, testcaseForm)
        } else {
          response = await createTestcase(testcaseForm)
        }
        
        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadTestcaseList()
        } else {
          ElMessage.error(response.message || '操作失败，请稍后重试')
        }
      } catch (error: any) {
        console.error('提交失败:', error)
        ElMessage.error(error?.message || '操作失败，请稍后重试')
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
  testcaseFormRef.value?.resetFields()
  Object.assign(testcaseForm, {
    id: null,
    title: '',
    priority: 'P2',
    type: 'FUNCTIONAL',
    precondition: '',
    steps: '',
    expectedResult: '',
    status: 'PENDING_REVIEW'
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
  loadTestcaseList()
}

/**
 * 重置
 */
const handleReset = () => {
  filterForm.name = ''
  filterForm.priority = ''
  filterForm.status = ''
  handleSearch()
}

/**
 * 分页大小改变
 */
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadTestcaseList()
}

/**
 * 页码改变
 */
const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadTestcaseList()
}

/**
 * 初始化
 */
onMounted(async () => {
  loadTestcaseList()
  try {
    projectOptions.value = await getProjectOptions()
    requirementOptions.value = await getRequirementOptions()
  } catch (error) {
    console.error('加载选项失败:', error)
  }
})
</script>

<style scoped>
.testcase-container {
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

.header-actions {
  display: flex;
  gap: 10px;
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
