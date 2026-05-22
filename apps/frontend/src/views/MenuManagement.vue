<template>
  <div class="menu-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>菜单管理</span>
          <el-button type="primary" @click="handleCreate(null)">新建菜单</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe row-key="id" default-expand-all>
        <el-table-column prop="name" label="菜单名称" min-width="150" />
        <el-table-column prop="path" label="路由路径" min-width="180" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="200" show-overflow-tooltip />
        <el-table-column prop="icon" label="图标" width="100" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column prop="type" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === 0 ? '' : 'warning'">
              {{ row.type === 0 ? '菜单' : '按钮' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permissionCode" label="权限编码" min-width="150" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleCreate(row)">添加下级</el-button>
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="路由路径" prop="path">
          <el-input v-model="formData.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="formData.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="formData.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="formData.sortOrder" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio :label="0">菜单</el-radio>
            <el-radio :label="1">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="权限编码">
          <el-input v-model="formData.permissionCode" placeholder="请输入权限编码" />
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
import { menuApi, type Menu, type MenuCreate, type MenuUpdate } from '@/api/menu'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const parentName = ref('')

const tableData = ref<Menu[]>([])

const formData = reactive<MenuCreate & MenuUpdate & { id?: number }>({
  id: undefined,
  name: '',
  parentId: 0,
  path: '',
  component: '',
  icon: '',
  sortOrder: 0,
  type: 0,
  permissionCode: '',
})

const formRules: FormRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
}

const dialogTitle = ref('新建菜单')

const loadData = async () => {
  loading.value = true
  try {
    const res = await menuApi.getTree()
    tableData.value = res.data || []
  } catch (error) {
    console.error('加载菜单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = (parent: Menu | null) => {
  isEdit.value = false
  dialogTitle.value = parent ? `添加下级菜单 - ${parent.name}` : '新建菜单'
  formData.name = ''
  formData.parentId = parent?.id || 0
  formData.path = ''
  formData.component = ''
  formData.icon = ''
  formData.sortOrder = 0
  formData.type = 0
  formData.permissionCode = ''
  parentName.value = parent?.name || '顶级菜单'
  dialogVisible.value = true
}

const handleEdit = async (row: Menu) => {
  isEdit.value = true
  dialogTitle.value = '编辑菜单'
  try {
    const res = await menuApi.get(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
  } catch (error) {
    console.error('获取菜单详情失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      if (isEdit.value) {
        await menuApi.update(formData.id!, formData as MenuUpdate)
        ElMessage.success('更新成功')
      } else {
        await menuApi.create(formData as MenuCreate)
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

const handleDelete = async (row: Menu) => {
  try {
    await ElMessageBox.confirm('确定要删除该菜单吗？', '提示', { type: 'warning' })
    await menuApi.delete(row.id)
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
.menu-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
