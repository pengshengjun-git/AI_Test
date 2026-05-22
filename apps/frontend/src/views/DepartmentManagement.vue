<template>
  <div class="department-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" @click="handleCreate(null)">新建部门</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe row-key="id" default-expand-all>
        <el-table-column prop="name" label="部门名称" min-width="200" />
        <el-table-column prop="description" label="描述" min-width="250" show-overflow-tooltip />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleCreate(row)">添加下级</el-button>
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级部门">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="部门名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { departmentApi, type Department, type DepartmentCreate, type DepartmentUpdate } from '@/api/department'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const parentName = ref('')

const tableData = ref<Department[]>([])
const formData = reactive<DepartmentCreate & DepartmentUpdate & { id?: number }>({
  id: undefined,
  name: '',
  parentId: undefined,
  description: '',
  sortOrder: 0,
})

const formRules: FormRules = {
  name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
}

const dialogTitle = ref('新建部门')

const loadData = async () => {
  loading.value = true
  try {
    const res = await departmentApi.getTree()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载部门列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = (parent: Department | null) => {
  isEdit.value = false
  dialogTitle.value = parent ? `添加下级部门 - ${parent.name}` : '新建部门'
  formData.name = ''
  formData.parentId = parent?.id || 0
  formData.description = ''
  formData.sortOrder = 0
  parentName.value = parent?.name || '顶级部门'
  dialogVisible.value = true
}

const handleEdit = async (row: Department) => {
  isEdit.value = true
  dialogTitle.value = '编辑部门'
  try {
    const res = await departmentApi.get(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取部门详情失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value) {
        await departmentApi.update(formData.id!, formData as DepartmentUpdate)
        ElMessage.success('更新成功')
      } else {
        await departmentApi.create(formData as DepartmentCreate)
        ElMessage.success('创建成功')
      }
      dialogVisible.value = false
      loadData()
    } catch (error) {
      console.error('保存失败:', error)
    } finally {
      submitting.value = false
    }
  })
}

const handleDelete = async (row: Department) => {
  try {
    await ElMessageBox.confirm('确定要删除该部门吗？', '提示', { type: 'warning' })
    await departmentApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.department-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
