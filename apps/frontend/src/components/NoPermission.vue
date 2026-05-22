<template>
  <div class="no-permission-container">
    <!-- 顶部用户信息栏 -->
    <header class="user-info-bar">
      <div class="user-info-left">
        <div class="logo-section">
          <div class="logo-icon">
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
            </svg>
          </div>
          <span class="logo-text">AI测试管理平台</span>
        </div>
      </div>
      
      <div class="user-info-right">
        <div class="user-profile">
          <div class="user-avatar">
            <span>👤</span>
          </div>
          <div class="user-details">
            <span class="user-name">{{ displayUserName }}</span>
            <span class="user-role">{{ displayUserRole }}</span>
          </div>
        </div>
        <div class="user-actions">
          <button class="action-btn settings-btn" @click="handleSettings">
            <span>⚙️</span>
            <span>设置</span>
          </button>
          <button class="action-btn logout-btn" @click="handleLogout">
            <span>🚪</span>
            <span>退出登录</span>
          </button>
        </div>
      </div>
    </header>

    <!-- 主内容区域 -->
    <div class="no-permission-content">
      <div class="icon-wrapper">
        <svg class="no-permission-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
        </svg>
      </div>
      <h2>暂无模块权限</h2>
      <p>您当前账号暂无任何模块的访问权限，请联系管理员进行授权。</p>
      <div class="action-buttons">
        <button class="contact-btn" @click="handleContact">联系管理员</button>
      </div>
    </div>

    <!-- 用户信息弹窗 -->
    <el-dialog v-model="showUserInfoModal" title="个人信息" width="400px">
      <div class="user-info-modal-content">
        <div class="modal-avatar">
          <span>👤</span>
        </div>
        <div class="modal-info">
          <div class="info-row">
            <label>用户名</label>
            <span>{{ displayUserName }}</span>
          </div>
          <div class="info-row">
            <label>角色</label>
            <span>{{ displayUserRole }}</span>
          </div>
          <div class="info-row">
            <label>邮箱</label>
            <span>{{ userEmail }}</span>
          </div>
          <div class="info-row">
            <label>电话</label>
            <span>{{ userPhone }}</span>
          </div>
          <div class="info-row">
            <label>部门</label>
            <span>{{ userDepartment }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { ElMessage, ElDialog } from 'element-plus'
import { getUserInfo } from '@/utils/permission'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const router = useRouter()
const userStore = useUserStore()
const showUserInfoModal = ref(false)

// 获取用户名显示
const displayUserName = computed(() => {
  try {
    const userInfo = getUserInfo()
    if (!userInfo) return '未知用户'
    return userInfo.realName || userInfo.username || '未知用户'
  } catch (e) {
    return '未知用户'
  }
})

// 获取用户角色名称显示
const displayUserRole = computed(() => {
  try {
    const userInfo = getUserInfo()
    if (!userInfo) return ''
    const roles = userInfo.roles || []
    if (roles.includes('SUPER_ADMIN')) return '超级管理员'
    if (roles.includes('ADMIN')) return '管理员'
    if (roles.includes('PROJECT_MANAGER')) return '项目经理'
    if (roles.includes('TEST_ENGINEER')) return '测试工程师'
    if (roles.includes('USER')) return '普通用户'
    return '用户'
  } catch (e) {
    return ''
  }
})

// 获取用户邮箱
const userEmail = computed(() => {
  try {
    const userInfo = getUserInfo()
    return userInfo?.email || '未设置'
  } catch (e) {
    return '未设置'
  }
})

// 获取用户电话
const userPhone = computed(() => {
  try {
    const userInfo = getUserInfo()
    return userInfo?.phone || '未设置'
  } catch (e) {
    return '未设置'
  }
})

// 获取用户部门
const userDepartment = computed(() => {
  try {
    const userInfo = getUserInfo()
    return userInfo?.departmentName || '未设置'
  } catch (e) {
    return '未设置'
  }
})

const handleContact = () => {
  ElMessage.info('请通过企业微信或邮件联系管理员申请权限')
}

const handleSettings = () => {
  showUserInfoModal.value = true
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
  ElMessage.success('退出成功')
}
</script>

<style scoped>
.no-permission-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

/* 顶部用户信息栏 */
.user-info-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.user-info-left {
  display: flex;
  align-items: center;
}

.logo-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: #0ea5e9;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}

.user-info-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 6px 12px;
  background: #f8fafc;
  border-radius: 8px;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: #1e293b;
}

.user-role {
  font-size: 12px;
  color: #64748b;
}

.user-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.settings-btn {
  background: #f1f5f9;
  color: #64748b;
}

.settings-btn:hover {
  background: #e2e8f0;
  color: #334155;
}

.logout-btn {
  background: #fff;
  color: #ef4444;
  border: 1px solid #fecaca;
}

.logout-btn:hover {
  background: #fef2f2;
}

/* 主内容区域 */
.no-permission-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  padding: 60px 40px;
}

.icon-wrapper {
  width: 100px;
  height: 100px;
  margin-bottom: 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.no-permission-icon {
  width: 50px;
  height: 50px;
  color: white;
}

h2 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px;
}

p {
  font-size: 14px;
  color: #606266;
  margin: 0 0 32px;
  line-height: 1.6;
  max-width: 400px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.contact-btn {
  padding: 12px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.contact-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.contact-btn:active {
  transform: translateY(0);
}

/* 用户信息弹窗样式 */
.user-info-modal-content {
  padding: 20px;
}

.modal-avatar {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 40px;
}

.modal-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 16px;
  background: #f8fafc;
  border-radius: 8px;
}

.info-row label {
  font-size: 13px;
  color: #64748b;
  font-weight: 500;
}

.info-row span {
  font-size: 13px;
  color: #1e293b;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .user-info-bar {
    padding: 10px 16px;
  }

  .logo-text {
    display: none;
  }

  .user-details {
    display: none;
  }

  .action-btn span:last-child {
    display: none;
  }

  .action-btn {
    padding: 8px;
    border-radius: 8px;
  }

  .no-permission-content {
    padding: 40px 24px;
  }

  h2 {
    font-size: 20px;
  }

  p {
    font-size: 13px;
  }

  .icon-wrapper {
    width: 80px;
    height: 80px;
    margin-bottom: 20px;
  }

  .no-permission-icon {
    width: 40px;
    height: 40px;
  }

  .contact-btn {
    padding: 10px 24px;
    font-size: 13px;
  }
}

@media screen and (max-width: 576px) {
  .user-info-bar {
    padding: 8px 12px;
  }

  .logo-icon {
    width: 28px;
    height: 28px;
  }

  .user-avatar {
    width: 32px;
    height: 32px;
    font-size: 16px;
  }

  .user-profile {
    padding: 4px 8px;
  }

  .no-permission-content {
    padding: 30px 16px;
  }

  h2 {
    font-size: 18px;
  }

  p {
    font-size: 12px;
  }

  .contact-btn {
    padding: 8px 20px;
    font-size: 12px;
  }
}
</style>
