<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <el-icon :size="48" color="#409EFF"><TrendCharts /></el-icon>
        <h1>AI测试管理平台</h1>
      </div>
      
      <el-form :model="loginForm" class="login-form">
        <el-form-item>
          <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        
        <el-form-item>
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" @keyup.enter="handleLogin" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-btn" :loading="loading">
            登录
          </el-button>
        </el-form-item>
        
        <el-form-item class="register-link">
          <span>还没有账号？</span>
          <span class="link" @click="showRegisterDialog">立即注册</span>
          <span class="divider">|</span>
          <span class="link" @click="showResetPasswordDialog">忘记密码</span>
        </el-form-item>
      </el-form>
    </div>

    <el-dialog title="用户注册" v-model="registerVisible" width="500px" center>
      <el-form :model="registerForm" class="register-form">
        <el-form-item>
          <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="registerForm.password" type="password" placeholder="密码（至少8位，包含大小写字母和数字）" prefix-icon="Lock" />
          <div class="password-hint">
            <span v-if="registerForm.password && validatePassword(registerForm.password)" class="valid">
              <span class="check-icon">✓</span> 密码格式正确
            </span>
            <span v-else-if="registerForm.password" class="invalid">
              <span class="cross-icon">✗</span> 必须包含大小写字母和数字，长度8-20位
            </span>
            <span v-else class="hint-text">
              密码必须包含大小写字母和数字，长度8-20位
            </span>
          </div>
        </el-form-item>
        <el-form-item>
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="registerForm.email" placeholder="邮箱" prefix-icon="Message" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="registerForm.realName" placeholder="真实姓名" prefix-icon="UserFilled" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="registerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRegister" :loading="registerLoading">注册</el-button>
      </template>
    </el-dialog>

    <el-dialog title="重置密码" v-model="resetPasswordVisible" width="500px" center>
      <el-form :model="resetPasswordForm" class="reset-password-form">
        <el-form-item>
          <el-input v-model="resetPasswordForm.username" placeholder="用户名" prefix-icon="User" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="resetPasswordForm.email" placeholder="邮箱" prefix-icon="Message" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="resetPasswordForm.newPassword" type="password" placeholder="新密码（至少8位，包含大小写字母和数字）" prefix-icon="Lock" />
          <div class="password-hint">
            <span v-if="resetPasswordForm.newPassword && validatePassword(resetPasswordForm.newPassword)" class="valid">
              <span class="check-icon">✓</span> 密码格式正确
            </span>
            <span v-else-if="resetPasswordForm.newPassword" class="invalid">
              <span class="cross-icon">✗</span> 必须包含大小写字母和数字，长度8-20位
            </span>
            <span v-else class="hint-text">
              密码必须包含大小写字母和数字，长度8-20位
            </span>
          </div>
        </el-form-item>
        <el-form-item>
          <el-input v-model="resetPasswordForm.confirmPassword" type="password" placeholder="确认新密码" prefix-icon="Lock" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetPasswordVisible = false">取消</el-button>
        <el-button type="primary" @click="handleResetPassword" :loading="resetPasswordLoading">重置密码</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { TrendCharts, User, Lock, Message, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { login, register, getUserInfo as getUserInfoApi } from '@/api/auth'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

/**
 * 登录表单数据
 */
const loginForm = reactive({
  username: '',
  password: ''
})

/**
 * 注册表单数据
 */
const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  realName: ''
})

/**
 * 重置密码表单数据
 */
const resetPasswordForm = reactive({
  username: '',
  email: '',
  newPassword: '',
  confirmPassword: ''
})

/**
 * 加载状态
 */
const loading = ref(false)
const registerLoading = ref(false)
const registerVisible = ref(false)
const resetPasswordLoading = ref(false)
const resetPasswordVisible = ref(false)

/**
 * 显示注册弹窗
 */
const showRegisterDialog = () => {
  registerVisible.value = true
}

/**
 * 显示重置密码弹窗
 */
const showResetPasswordDialog = () => {
  resetPasswordVisible.value = true
}

/**
 * 处理登录
 */
const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.error('请输入用户名和密码')
    return
  }
  
  loading.value = true
  
  try {
    const response: any = await login(loginForm)
    
    if (response.code === 200 || response.code === 0) {
        ElMessage.success('登录成功')
        const data = response.data || response
        if (data.token) {
          localStorage.setItem('token', data.token)
          userStore.setToken(data.token)
        }
        
        // 暂时跳过获取用户信息，直接使用登录返回的数据
        userStore.setUserInfo(data)
        localStorage.setItem('userInfo', JSON.stringify(data))
        
        router.push('/')
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || error.message || '登录失败，请检查网络或用户名密码')
  } finally {
    loading.value = false
  }
}

/**
 * 验证密码格式
 */
const validatePassword = (password: string) => {
  // 至少8位，包含大小写字母和数字
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,20}$/
  return passwordRegex.test(password)
}

/**
 * 处理注册
 */
const handleRegister = async () => {
  if (!registerForm.username || !registerForm.password) {
    ElMessage.error('请填写用户名和密码')
    return
  }
  
  if (!validatePassword(registerForm.password)) {
    ElMessage.error('密码格式不正确，必须包含大小写字母和数字，长度8-20位')
    return
  }
  
  if (registerForm.password !== registerForm.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  
  registerLoading.value = true
  
  try {
    const response: any = await register({
      username: registerForm.username,
      password: registerForm.password,
      email: registerForm.email
    })
    
    if (response.code === 200 || response.code === 0) {
      ElMessage.success('注册成功，请登录')
      registerVisible.value = false
      loginForm.username = registerForm.username
      registerForm.password = ''
      registerForm.confirmPassword = ''
    } else {
      ElMessage.error(response.message || '注册失败')
    }
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || error.message || '注册失败')
  } finally {
    registerLoading.value = false
  }
}

/**
 * 处理重置密码
 */
const handleResetPassword = async () => {
  ElMessage.info('重置密码功能开发中')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-box {
  width: 420px;
  background: white;
  border-radius: 8px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
  margin-bottom: 30px;
}

.login-header h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.login-form {
  padding: 10px 0;
}

.login-btn {
  width: 100%;
}

.register-link {
  text-align: center;
  margin-top: 10px;
}

.register-link span {
  color: #909399;
}

.register-link .link {
  color: #409EFF;
  cursor: pointer;
  margin-left: 8px;
}

.register-link .link:hover {
  text-decoration: underline;
}

.register-link .divider {
  color: #909399;
  margin: 0 8px;
}

.password-hint {
  margin-top: 5px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.password-hint .valid {
  color: #67C23A;
}

.password-hint .invalid {
  color: #F56C6C;
}

.password-hint .hint-text {
  color: #909399;
}

.password-hint .check-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  background-color: #67C23A;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
}

.password-hint .cross-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  background-color: #F56C6C;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 15px;
}

.register-form {
  width: 100%;
}

.register-form :deep(.el-input) {
  width: 100%;
}

.register-form :deep(.el-input__wrapper) {
  justify-content: flex-start;
}

.register-form :deep(.el-input__prefix) {
  margin-right: 8px;
}

.reset-password-form {
  width: 100%;
}

.reset-password-form :deep(.el-input) {
  width: 100%;
}

.reset-password-form :deep(.el-input__wrapper) {
  justify-content: flex-start;
}

.reset-password-form :deep(.el-input__prefix) {
  margin-right: 8px;
}

.reset-password-form :deep(.el-form-item) {
  margin-bottom: 15px;
}

/* 响应式布局 */
@media screen and (max-width: 576px) {
  .login-container {
    padding: 16px;
  }
  
  .login-box {
    width: 100%;
    max-width: 380px;
    padding: 30px 24px;
  }
  
  .login-header h1 {
    font-size: 20px;
  }
}

@media screen and (max-width: 480px) {
  .login-container {
    padding: 12px;
  }
  
  .login-box {
    padding: 24px 20px;
  }
  
  .login-header {
    gap: 12px;
    margin-bottom: 24px;
  }
  
  .login-header h1 {
    font-size: 18px;
  }
  
  /* 优化对话框在移动端的显示 */
  :deep(.el-dialog) {
    width: 90% !important;
    margin: 5vh auto !important;
  }
}

@media screen and (max-width: 360px) {
  .login-container {
    padding: 8px;
  }
  
  .login-box {
    padding: 20px 16px;
  }
  
  .login-header h1 {
    font-size: 16px;
  }
  
  .register-link {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .register-link .divider {
    display: none;
  }
  
  .register-link .link {
    margin-left: 0;
  }
}
</style>
