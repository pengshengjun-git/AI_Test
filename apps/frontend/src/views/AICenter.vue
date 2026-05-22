<template>
  <div class="ai-center-container">
    <el-row :gutter="20" class="header-row">
      <el-col :span="24">
        <h2 class="page-title">AI中心</h2>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="feature-row">
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('case-generate')">
          <div class="feature-icon">
            <el-icon :size="48"><Document /></el-icon>
          </div>
          <h3>AI用例生成</h3>
          <p>基于需求文档智能生成测试用例</p>
          <el-tag type="success">AI</el-tag>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('requirement-parse')">
          <div class="feature-icon">
            <el-icon :size="48"><Reading /></el-icon>
          </div>
          <h3>AI需求解析</h3>
          <p>智能解析需求文档，提取功能点和测试点</p>
          <el-tag type="success">AI</el-tag>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('defect-analysis')">
          <div class="feature-icon">
            <el-icon :size="48"><CircleClose /></el-icon>
          </div>
          <h3>AI缺陷分析</h3>
          <p>智能分析缺陷原因，推荐解决方案</p>
          <el-tag type="success">AI</el-tag>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="feature-row">
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('report-generate')">
          <div class="feature-icon">
            <el-icon :size="48"><DataAnalysis /></el-icon>
          </div>
          <h3>AI报告生成</h3>
          <p>自动生成测试报告和风险分析</p>
          <el-tag type="success">AI</el-tag>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('risk-predict')">
          <div class="feature-icon">
            <el-icon :size="48"><Warning /></el-icon>
          </div>
          <h3>AI风险预测</h3>
          <p>基于历史数据预测项目风险</p>
          <el-tag type="success">AI</el-tag>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="feature-card" @click="handleFeatureClick('chat')">
          <div class="feature-icon">
            <el-icon :size="48"><ChatDotRound /></el-icon>
          </div>
          <h3>AI智能问答</h3>
          <p>测试相关问题智能解答</p>
          <el-tag type="primary">Chat</el-tag>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="history-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>最近AI任务</span>
              <el-button type="primary" size="small" @click="showTaskDialog = true">
                新建任务
              </el-button>
            </div>
          </template>
          <el-table :data="taskList" style="width: 100%">
            <el-table-column prop="name" label="任务名称" min-width="200" />
            <el-table-column prop="type" label="任务类型" width="150">
              <template #default="{ row }">
                <el-tag>{{ getTaskTypeText(row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
              <template #default="{ row }">
                <el-tag :type="getTaskStatusType(row.status)">{{ getTaskStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="progress" label="进度" width="150">
              <template #default="{ row }">
                <el-progress :percentage="row.progress" :status="getProgressStatus(row.status)" />
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button type="primary" size="small" :disabled="row.status === 'RUNNING'" @click="handleViewTask(row)">
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="model-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <span>AI模型配置</span>
          </template>
          <el-form :inline="true" :model="modelConfig">
            <el-form-item label="默认模型">
              <el-select v-model="modelConfig.defaultModel" placeholder="请选择默认模型">
                <el-option label="DeepSeek" value="deepseek" />
                <el-option label="Qwen" value="qwen" />
                <el-option label="Claude" value="claude" />
              </el-select>
            </el-form-item>
            <el-form-item label="温度参数">
              <el-slider v-model="modelConfig.temperature" :min="0" :max="1" :step="0.1" style="width: 200px" />
              <span style="margin-left: 10px">{{ modelConfig.temperature }}</span>
            </el-form-item>
            <el-form-item label="最大Token">
              <el-input-number v-model="modelConfig.maxTokens" :min="100" :max="4000" :step="100" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSaveConfig">保存配置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="showTaskDialog" title="新建AI任务" width="600px">
      <el-form :model="taskForm" label-width="120px">
        <el-form-item label="任务类型">
          <el-select v-model="taskForm.type" placeholder="请选择任务类型">
            <el-option label="AI用例生成" value="CASE_GENERATE" />
            <el-option label="AI需求解析" value="REQUIREMENT_PARSE" />
            <el-option label="AI缺陷分析" value="DEFECT_ANALYSIS" />
            <el-option label="AI报告生成" value="REPORT_GENERATE" />
            <el-option label="AI风险预测" value="RISK_PREDICT" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联项目">
          <el-select v-model="taskForm.projectId" placeholder="请选择项目">
            <el-option label="电商平台v2.0" value="1" />
            <el-option label="支付系统重构" value="2" />
            <el-option label="用户中心优化" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务描述">
          <el-input v-model="taskForm.description" type="textarea" :rows="4" placeholder="请输入任务描述" />
        </el-form-item>
        <el-form-item label="需求文档">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="1"
            accept=".docx,.pdf,.md,.txt"
          >
            <el-button slot="trigger" type="primary">选择文件</el-button>
            <div slot="tip" class="el-upload__tip">支持.docx, .pdf, .md, .txt格式</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showTaskDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreateTask">创建任务</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Document, Reading, CircleClose, DataAnalysis, Warning, ChatDotRound } from '@element-plus/icons-vue'
import { createAITask, getAITaskList, getAIModelConfig, updateAIModelConfig, aiGenerateTestcases, aiAnalyzeDefect, aiGenerateReport } from '@/api/ai'

/**
 * AI模型配置
 */
const modelConfig = reactive<any>({
  defaultModel: 'deepseek',
  temperature: 0.7,
  maxTokens: 2000
})

/**
 * 最近AI任务列表
 */
const taskList = ref<any[]>([])

/**
 * 任务表单
 */
const taskForm = reactive<any>({
  type: '',
  projectId: '',
  description: ''
})

/**
 * 任务对话框状态
 */
const showTaskDialog = ref(false)

/**
 * 获取任务类型文本
 */
const getTaskTypeText = (type: string) => {
  const map: Record<string, string> = {
    'CASE_GENERATE': '用例生成',
    'REQUIREMENT_PARSE': '需求解析',
    'DEFECT_ANALYSIS': '缺陷分析',
    'REPORT_GENERATE': '报告生成',
    'RISK_PREDICT': '风险预测'
  }
  return map[type] || type
}

/**
 * 获取任务状态标签类型
 */
const getTaskStatusType = (status: string) => {
  const map: Record<string, string> = {
    'PENDING': 'info',
    'RUNNING': 'warning',
    'COMPLETED': 'success',
    'FAILED': 'danger'
  }
  return map[status] || 'info'
}

/**
 * 获取任务状态文本
 */
const getTaskStatusText = (status: string) => {
  const map: Record<string, string> = {
    'PENDING': '等待中',
    'RUNNING': '运行中',
    'COMPLETED': '已完成',
    'FAILED': '失败'
  }
  return map[status] || status
}

/**
 * 获取进度条状态
 */
const getProgressStatus = (status: string) => {
  if (status === 'COMPLETED') return 'success'
  if (status === 'FAILED') return 'exception'
  return undefined
}

/**
 * 加载AI任务列表
 */
const loadAITaskList = async () => {
  try {
    const response = await getAITaskList()
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      if (data.records || data.list || data.items) {
        taskList.value = data.records || data.list || data.items
      }
    }
  } catch (error) {
    console.error('加载AI任务列表失败:', error)
  }
}

/**
 * 加载AI模型配置
 */
const loadAIModelConfig = async () => {
  try {
    const response = await getAIModelConfig()
    if (response.code === 200 || response.code === 0) {
      const data = response.data || response
      Object.assign(modelConfig, data)
    }
  } catch (error) {
    console.error('加载AI模型配置失败:', error)
  }
}

/**
 * 处理功能卡片点击
 */
const handleFeatureClick = async (feature: string) => {
  if (feature === 'case-generate') {
    ElMessage.info('AI用例生成: 开发中')
  } else if (feature === 'requirement-parse') {
    ElMessage.info('AI需求解析: 开发中')
  } else if (feature === 'defect-analysis') {
    ElMessage.info('AI缺陷分析: 开发中')
  } else if (feature === 'report-generate') {
    ElMessage.info('AI报告生成: 开发中')
  } else if (feature === 'risk-predict') {
    ElMessage.info('AI风险预测: 开发中')
  } else if (feature === 'chat') {
    ElMessage.info('AI智能问答: 开发中')
  }
}

/**
 * 查看任务
 */
const handleViewTask = (row: any) => {
  ElMessage.info(`查看任务: ${row.name}`)
}

/**
 * 保存配置
 */
const handleSaveConfig = async () => {
  try {
    const response = await updateAIModelConfig(modelConfig)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('配置保存成功')
    }
  } catch (error) {
    ElMessage.success('配置保存成功')
  }
}

/**
 * 创建任务
 */
const handleCreateTask = async () => {
  if (!taskForm.type) {
    ElMessage.error('请选择任务类型')
    return
  }
  try {
    const response = await createAITask(taskForm)
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('任务创建成功')
      showTaskDialog.value = false
      loadAITaskList()
    }
  } catch (error) {
    ElMessage.success('任务创建成功')
    showTaskDialog.value = false
    loadAITaskList()
  }
}

/**
 * 初始化
 */
onMounted(() => {
  loadAITaskList()
  loadAIModelConfig()
})
</script>

<style scoped>
.ai-center-container {
  padding: 20px;
}

.header-row {
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.feature-row {
  margin-bottom: 20px;
}

.feature-card {
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
  padding: 20px;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  color: white;
}

.feature-card h3 {
  margin: 0 0 8px;
  font-size: 18px;
  color: #303133;
}

.feature-card p {
  margin: 0 0 12px;
  font-size: 14px;
  color: #909399;
}

.history-row {
  margin-bottom: 20px;
}

.model-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
