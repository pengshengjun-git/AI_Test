<template>
  <div class="role-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>角色管理</span>
          <el-button type="primary" @click="handleCreate">新建角色</el-button>
        </div>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="code" label="角色编码" min-width="120" />
        <el-table-column prop="name" label="角色名称" min-width="120" />
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
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
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="菜单权限" prop="menuIds">
          <el-tree
            ref="menuTreeRef"
            :data="menuTree"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            :show-checkbox="true"
            default-expand-all
            style="max-height: 400px; overflow-y: auto"
          />
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
import type { ElTree } from 'element-plus'
import { roleApi, type Role, type RoleCreate, type RoleUpdate } from '@/api/role'
import { menuApi, type Menu } from '@/api/menu'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const menuTreeRef = ref<InstanceType<typeof ElTree>>()

const tableData = ref<Role[]>([])
const menuTree = ref<Menu[]>([])

const formData = reactive<RoleCreate & RoleUpdate & { id?: number }>({
  id: undefined,
  code: '',
  name: '',
  description: '',
  menuIds: [],
})

const formRules: FormRules = {
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
}

const dialogTitle = ref('新建角色')

const loadMenus = async () => {
  try {
    const res = await menuApi.getTree()
    menuTree.value = res.data || []
  } catch (error) {
    console.error('加载菜单树失败:', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await roleApi.list()
    tableData.value = res.data?.records || res.data?.list || []
  } catch (error) {
    console.error('加载角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  isEdit.value = false
  dialogTitle.value = '新建角色'
  formData.code = ''
  formData.name = ''
  formData.description = ''
  formData.menuIds = []
  dialogVisible.value = true
  setTimeout(() => {
    menuTreeRef.value?.setCheckedKeys([])
  }, 0)
}

const handleEdit = async (row: Role) => {
  isEdit.value = true
  dialogTitle.value = '编辑角色'
  try {
    const res = await roleApi.get(row.id)
    Object.assign(formData, res.data)
    dialogVisible.value = true
    setTimeout(() => {
      menuTreeRef.value?.setCheckedKeys(res.data.menuIds || [])
    }, 0)
  } catch (error) {
    console.error('获取角色详情失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      formData.menuIds = menuTreeRef.value?.getCheckedKeys(false) as number[] || []
      if (isEdit.value) {
        await roleApi.update(formData.id!, formData as RoleUpdate)
        ElMessage.success('更新成功')
      } else {
        await roleApi.create(formData as RoleCreate)
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

const handleDelete = async (row: Role) => {
  try {
    await ElMessageBox.confirm('确定要删除该角色吗？', '提示', { type: 'warning' })
    await roleApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

onMounted(() => {
  loadMenus()
  loadData()
})
</script>

<style scoped>
.role-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
