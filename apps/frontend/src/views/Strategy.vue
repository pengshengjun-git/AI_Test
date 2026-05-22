<template>
  <div class="strategy-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <div class="page-header">
          <h2 class="page-title">策略管理</h2>
          <el-button type="primary" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>
            新建策略
          </el-button>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="filter-row">
      <el-col :span="24">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="策略名称">
            <el-input v-model="filterForm.name" placeholder="请输入策略名称" clearable />
          </el-form-item>
          <el-form-item label="策略类型">
            <el-select v-model="filterForm.type" placeholder="请选择类型" clearable>
              <el-option label="全部" value="" />
              <el-option label="冒烟测试" value="SMOKE" />
              <el-option label="回归测试" value="REGRESSION" />
              <el-option label="性能测试" value="PERFORMANCE" />
              <el-option label="安全测试" value="SECURITY" />
              <el-option label="集成测试" value="INTEGRATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filterForm.status" placeholder="请选择状态" clearable>
              <el-option label="全部" value="" />
              <el-option label="启用" value="ENABLED" />
              <el-option label="禁用" value="DISABLED" />
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
        <el-table :data="strategyList" v-loading="loading" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" index-method="(index) => index + 1" />
          <el-table-column prop="name" label="策略名称" min-width="200" />
          <el-table-column prop="type" label="策略类型" width="120">
            <template #default="{ row }">
              <el-tag :type="getTypeTagType(row.type)">{{ getTypeText(row.type) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="策略描述" min-width="250" show-overflow-tooltip />
          <el-table-column prop="priority" label="执行优先级" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="getPriorityTagType(row.priority)">{{ row.priority }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-switch
                v-model="row.statusSwitch"
                :active-value="'ENABLED'"
                :inactive-value="'DISABLED'"
                @change="handleStatusChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="creator" label="创建人" width="100" />
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column prop="updateTime" label="修改时间" width="180" />
          <el-table-column label="操作" width="320" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button type="success" size="small" @click="handleCopy(row)">复制</el-button>
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
      <el-form ref="strategyFormRef" :model="strategyForm" :rules="strategyRules" label-width="120px">
        <el-form-item label="策略名称" prop="name">
          <el-input v-model="strategyForm.name" placeholder="请输入策略名称" />
        </el-form-item>
        <el-form-item label="策略类型" prop="type">
          <el-select v-model="strategyForm.type" placeholder="请选择策略类型" style="width: 100%">
            <el-option label="冒烟测试" value="SMOKE" />
            <el-option label="回归测试" value="REGRESSION" />
            <el-option label="性能测试" value="PERFORMANCE" />
            <el-option label="安全测试" value="SECURITY" />
            <el-option label="集成测试" value="INTEGRATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行优先级" prop="priority">
          <el-select v-model="strategyForm.priority" placeholder="请选择优先级" style="width: 100%">
            <el-option label="P0" value="P0" />
            <el-option label="P1" value="P1" />
            <el-option label="P2" value="P2" />
            <el-option label="P3" value="P3" />
          </el-select>
        </el-form-item>
        <el-form-item label="策略描述" prop="description">
          <el-input
            v-model="strategyForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入策略描述"
          />
        </el-form-item>
        <el-form-item label="关联项目">
          <el-select v-model="strategyForm.projectIds" multiple placeholder="请选择关联项目" style="width: 100%">
            <el-option label="电商平台v2.0" value="1" />
            <el-option label="支付系统重构" value="2" />
            <el-option label="用户中心优化" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="测试用例范围">
          <el-checkbox-group v-model="strategyForm.caseScope">
            <el-checkbox label="ALL">全部用例</el-checkbox>
            <el-checkbox label="CRITICAL">核心功能</el-checkbox>
            <el-checkbox label="HIGH">高优先级</el-checkbox>
            <el-checkbox label="MEDIUM">中优先级</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="执行环境">
          <el-checkbox-group v-model="strategyForm.environments">
            <el-checkbox label="DEV">开发环境</el-checkbox>
            <el-checkbox label="TEST">测试环境</el-checkbox>
            <el-checkbox label="STAGING">预发布环境</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="定时执行">
          <el-switch v-model="strategyForm.scheduleEnabled" />
        </el-form-item>
        <el-form-item v-if="strategyForm.scheduleEnabled" label="执行周期">
          <el-radio-group v-model="strategyForm.scheduleType">
            <el-radio label="DAILY">每日</el-radio>
            <el-radio label="WEEKLY">每周</el-radio>
            <el-radio label="MONTHLY">每月</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="strategyForm.scheduleEnabled" label="执行时间">
          <el-time-picker
            v-model="strategyForm.scheduleTime"
            placeholder="选择执行时间"
            format="HH:mm"
            value-format="HH:mm"
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
import { getStrategyList, createStrategy, updateStrategy, deleteStrategy, toggleStrategyStatus } from '@/api/strategy'
import type { Strategy, StrategyQueryParams } from '@/api/strategy'

/**
 * 过滤器表单
 */
const filterForm = reactive<StrategyQueryParams>({
  name: '',
  type: '',
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
 * 策略列表数据
 */
const strategyList = ref<Strategy[]>([])

/**
 * 对话框状态
 */
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑策略' : '新建策略')

/**
 * 策略表单
 */
const strategyForm = reactive<Strategy>({
  id: undefined,
  name: '',
  type: 'SMOKE',
  description: '',
  priority: 'P2',
  status: 'ENABLED',
  projectIds: [],
  caseScope: [],
  environments: [],
  scheduleEnabled: false,
  scheduleType: 'DAILY',
  scheduleTime: '09:00'
})

/**
 * 表单验证规则
 */
const strategyRules: FormRules = {
  name: [{ required: true, message: '请输入策略名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择策略类型', trigger: 'change' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
}

const strategyFormRef = ref<FormInstance>()

/**
 * 获取策略类型标签类型
 */
const getTypeTagType = (type: string) => {
  const map: Record<string, string> = {
    'SMOKE': 'success',
    'REGRESSION': 'primary',
    'PERFORMANCE': 'warning',
    'SECURITY': 'danger',
    'INTEGRATION': 'info'
  }
  return map[type] || ''
}

/**
 * 获取策略类型文本
 */
const getTypeText = (type: string) => {
  const map: Record<string, string> = {
    'SMOKE': '冒烟测试',
    'REGRESSION': '回归测试',
    'PERFORMANCE': '性能测试',
    'SECURITY': '安全测试',
    'INTEGRATION': '集成测试'
  }
  return map[type] || type
}

/**
 * 获取优先级标签类型
 */
const getPriorityTagType = (priority: string) => {
  const map: Record<string, string> = {
    'P0': 'danger',
    'P1': 'warning',
    'P2': 'primary',
    'P3': 'info'
  }
  return map[priority] || 'info'
}

/**
 * 加载策略列表
 */
const loadStrategyList = async () => {
  loading.value = true
  try {
    const response = await getStrategyList({
      ...filterForm,
      page: pagination.page,
      size: pagination.size
    })
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      strategyList.value = data.records || data.list || data.items || data || []
      // 添加 statusSwitch 字段用于开关显示
      strategyList.value.forEach(item => {
        item.statusSwitch = item.status === 'ENABLED'
      })
      pagination.total = data.total || strategyList.value.length
    }
  } catch (error) {
    console.error('加载策略列表失败:', error)
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
 * 编辑策略
 */
const handleEdit = (row: Strategy) => {
  isEdit.value = true
  Object.assign(strategyForm, { ...row, statusSwitch: row.status === 'ENABLED' })
  dialogVisible.value = true
}

/**
 * 复制策略
 */
const handleCopy = (row: Strategy) => {
  isEdit.value = false
  Object.assign(strategyForm, {
    ...row,
    id: null,
    name: row.name + ' (副本)',
    status: 'DISABLED'
  })
  dialogVisible.value = true
}

/**
 * 删除策略
 */
const handleDelete = async (row: Strategy) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除策略"${row.name}"吗？删除后不可恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const response = await deleteStrategy(row.id!)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('删除成功')
      loadStrategyList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除策略失败:', error)
    }
  }
}

/**
 * 状态变更
 */
const handleStatusChange = async (row: Strategy) => {
  const newStatus = row.status === 'ENABLED' ? 'DISABLED' : 'ENABLED'
  try {
    const response = await toggleStrategyStatus(row.id!, newStatus)
    if (response.code === 200 || response.code === 0) {
      row.status = newStatus
      ElMessage.success(newStatus === 'ENABLED' ? '策略已启用' : '策略已禁用')
    }
  } catch (error) {
    row.status = newStatus
    ElMessage.success(newStatus === 'ENABLED' ? '策略已启用' : '策略已禁用')
  }
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!strategyFormRef.value) return

  await strategyFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (isEdit.value && strategyForm.id) {
          response = await updateStrategy(strategyForm.id, strategyForm)
        } else {
          response = await createStrategy(strategyForm)
        }

        if (response.code === 200 || response.code === 0) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadStrategyList()
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
  strategyFormRef.value?.resetFields()
  Object.assign(strategyForm, {
    id: null,
    name: '',
    type: '',
    description: '',
    priority: 'P2',
    status: 'ENABLED',
    projectIds: [],
    caseScope: [],
    environments: [],
    scheduleEnabled: false,
    scheduleType: 'DAILY',
    scheduleTime: '09:00'
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
  loadStrategyList()
}

/**
 * 重置
 */
const handleReset = () => {
  filterForm.name = ''
  filterForm.type = ''
  filterForm.status = ''
  handleSearch()
}

/**
 * 分页大小改变
 */
const handleSizeChange = (size: number) => {
  pagination.size = size
  loadStrategyList()
}

/**
 * 页码改变
 */
const handleCurrentChange = (page: number) => {
  pagination.page = page
  loadStrategyList()
}

/**
 * 初始化
 */
onMounted(() => {
  loadStrategyList()
})
</script>

<style scoped>
.strategy-container {
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
  .strategy-container {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .page-title {
    font-size: 20px;
  }
}

@media screen and (max-width: 768px) {
  .strategy-container {
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
  .strategy-container {
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
  .strategy-container {
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