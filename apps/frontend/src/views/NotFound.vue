<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-code">
        <span>4</span>
        <el-icon :size="120"><Warning /></el-icon>
        <span>4</span>
      </div>
      <h1 class="error-title">页面未找到</h1>
      <p class="error-description">抱歉，您访问的页面不存在或已被移除</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
        <el-button @click="goBack">
          <el-icon><ArrowLeft /></el-icon>
          返回上页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { Warning, HomeFilled, ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.not-found-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px;
}

.not-found-content {
  text-align: center;
  padding: 60px 40px;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  max-width: 500px;
  width: 100%;
}

.error-code {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
}

.error-code span {
  font-size: 100px;
  font-weight: 800;
  color: #409eff;
  text-shadow: 2px 2px 0 rgba(64, 158, 255, 0.2);
}

.error-code .el-icon {
  color: #e6a23c;
}

.error-title {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 12px 0;
}

.error-description {
  font-size: 16px;
  color: #909399;
  margin: 0 0 40px 0;
  line-height: 1.6;
}

.error-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.error-actions .el-button {
  padding: 12px 32px;
  font-size: 16px;
}

@media screen and (max-width: 768px) {
  .not-found-content {
    padding: 40px 24px;
  }

  .error-code span {
    font-size: 72px;
  }

  .error-code .el-icon {
    font-size: 80px !important;
  }

  .error-title {
    font-size: 24px;
  }

  .error-description {
    font-size: 14px;
  }
}

@media screen and (max-width: 576px) {
  .not-found-content {
    padding: 32px 20px;
  }

  .error-code span {
    font-size: 56px;
  }

  .error-code .el-icon {
    font-size: 64px !important;
  }

  .error-title {
    font-size: 20px;
  }

  .error-description {
    font-size: 13px;
  }

  .error-actions {
    flex-direction: column;
    gap: 12px;
  }

  .error-actions .el-button {
    width: 100%;
    padding: 10px 24px;
    font-size: 14px;
  }
}
</style>
