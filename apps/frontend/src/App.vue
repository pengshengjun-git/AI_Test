<template>
  <div v-if="!isLoggedIn && !isLoginPage" class="no-auth-wrapper">
    <router-view />
  </div>
  <div v-else-if="isLoginPage" class="login-wrapper">
    <router-view />
  </div>
  <div v-else class="app-container">
    <!-- 左侧导航 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo" @click="toggleSidebar">
          <div class="logo-icon">
            <svg class="w-5 h-5 text-on-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
            </svg>
          </div>
          <span v-if="!isSidebarCollapsed" class="logo-text">AI测试管理平台</span>
        </div>
      </div>
      
      <nav class="sidebar-nav">
        <router-link 
          to="/" 
          class="nav-item"
          :class="{ 'active': route.path === '/' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">🏠</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">工作台</span>
        </router-link>
        <router-link 
          to="/project" 
          class="nav-item"
          :class="{ 'active': route.path === '/project' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">📁</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">项目管理</span>
        </router-link>
        <router-link 
          to="/requirement" 
          class="nav-item"
          :class="{ 'active': route.path === '/requirement' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">📋</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">需求管理</span>
        </router-link>
        <router-link 
          to="/testcase" 
          class="nav-item"
          :class="{ 'active': route.path === '/testcase' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">📝</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">用例管理</span>
        </router-link>
        <router-link 
          to="/testplan" 
          class="nav-item"
          :class="{ 'active': route.path === '/testplan' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">📅</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">测试计划</span>
        </router-link>
        <router-link 
          to="/defect" 
          class="nav-item"
          :class="{ 'active': route.path === '/defect' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">⚠️</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">缺陷管理</span>
        </router-link>
        <router-link 
          to="/coverage" 
          class="nav-item"
          :class="{ 'active': route.path === '/coverage' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">📊</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">覆盖率</span>
        </router-link>
        <router-link 
          to="/strategy" 
          class="nav-item"
          :class="{ 'active': route.path === '/strategy' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">⚙️</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">策略管理</span>
        </router-link>
        <router-link 
          to="/ai-center" 
          class="nav-item"
          :class="{ 'active': route.path === '/ai-center' }"
          @click="handleNavClick"
        >
          <span class="nav-icon">🤖</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">AI中心</span>
        </router-link>
      </nav>
      
      <div class="sidebar-footer">
        <button class="collapse-btn" @click="toggleSidebar">
          <span :class="{ 'rotate-180': isSidebarCollapsed }">◀</span>
          <span v-if="!isSidebarCollapsed">收起侧栏</span>
        </button>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部栏 -->
      <header class="top-bar">
        <div class="search-box">
          <span class="search-icon">🔍</span>
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索项目、用例..." 
            @keyup.enter="handleSearch"
          />
        </div>
        
        <div class="top-bar-right">
          <button class="notification-btn" @click="showNotification = true">
            <span>🔔</span>
            <span class="notification-badge">3</span>
          </button>
          
          <div class="user-menu" ref="userMenuRef">
            <button class="user-btn" @click="toggleUserMenu">
              <div class="user-avatar">
                <span>👤</span>
              </div>
              <span v-if="!isSidebarCollapsed" class="user-name">管理员</span>
              <span class="expand-icon" :class="{ 'rotate-180': showUserMenu }">▼</span>
            </button>
            <div v-if="showUserMenu" class="user-dropdown">
              <a href="#" @click.prevent="handleProfile">
                <span>👤</span>
                <span>个人中心</span>
              </a>
              <a href="#" @click.prevent="handleSettings">
                <span>⚙️</span>
                <span>设置</span>
              </a>
              <a href="#" @click.prevent="handleLogout">
                <span>🚪</span>
                <span>退出登录</span>
              </a>
            </div>
          </div>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-wrapper">
        <router-view />
      </div>
    </main>

    <!-- 通知弹窗 -->
    <el-dialog v-model="showNotification" title="消息通知" width="400px">
      <div class="notification-list">
        <div class="notification-item" v-for="(item, index) in notifications" :key="index">
          <div class="notification-icon" :class="item.type">
            <el-icon :size="24">
              <CircleCheck v-if="item.type === 'success'" />
              <Warning v-else-if="item.type === 'warning'" />
              <InfoFilled v-else />
            </el-icon>
          </div>
          <div class="notification-content">
            <div class="notification-title">{{ item.title }}</div>
            <div class="notification-time">{{ item.time }}</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, reactive, onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { CircleCheck, Warning, InfoFilled } from '@element-plus/icons-vue';

const route = useRoute();
const router = useRouter();
const isLoginPage = computed(() => route.path === '/login');
const loggedIn = ref(!!localStorage.getItem('token'));
const isLoggedIn = computed(() => loggedIn.value);
const isSidebarCollapsed = ref(false);
const showUserMenu = ref(false);
const userMenuRef = ref<HTMLElement | null>(null);
const showNotification = ref(false);
const searchQuery = ref('');

const handleClickOutside = (event: MouseEvent) => {
  if (userMenuRef.value && !userMenuRef.value.contains(event.target as Node)) {
    showUserMenu.value = false;
  }
};

router.afterEach(() => {
  loggedIn.value = !!localStorage.getItem('token');
  showUserMenu.value = false;
});

const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

const handleNavClick = () => {
  // 点击导航项后的处理
};

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    ElMessage.info(`搜索: ${searchQuery.value}`);
  }
};

const notifications = reactive([
  { id: 1, title: '测试用例执行完成', type: 'success', time: '5分钟前' },
  { id: 2, title: '新缺陷已创建', type: 'warning', time: '10分钟前' },
  { id: 3, title: '项目状态已更新', type: 'info', time: '30分钟前' }
]);

const handleProfile = () => {
  ElMessage.info('个人中心功能开发中');
};

const handleSettings = () => {
  ElMessage.info('系统设置功能开发中');
};

const handleLogout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userInfo');
  router.push('/login');
  ElMessage.success('退出成功');
};

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style>
.material-symbols-outlined {
  font-family: 'Material Symbols Outlined';
  font-weight: normal;
  font-style: normal;
  font-size: 20px;
  line-height: 1;
  letter-spacing: normal;
  text-transform: none;
  display: inline-block;
  white-space: nowrap;
  word-wrap: normal;
  direction: ltr;
  -webkit-font-feature-settings: 'liga';
  -webkit-font-smoothing: antialiased;
}
</style>

<style scoped>
.login-wrapper,
.no-auth-wrapper {
  min-height: 100vh;
}

.app-container {
  display: flex;
  min-height: 100vh;
}

/* 左侧边栏 */
.sidebar {
  width: 200px;
  background: linear-gradient(180deg, #1e293b 0%, #0f172a 100%);
  color: #e2e8f0;
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  z-index: 100;
}

.sidebar.sidebar-collapsed {
  width: 60px;
}

.sidebar-header {
  padding: 16px;
  border-bottom: 1px solid #334155;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: #0ea5e9;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
}

.sidebar-nav {
  flex: 1;
  padding: 12px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  color: #cbd5e1;
  text-decoration: none;
  transition: all 0.2s ease;
  margin-bottom: 4px;
}

.nav-item:hover {
  background: rgba(14, 165, 233, 0.1);
  color: #fff;
}

.nav-item.active {
  background: rgba(14, 165, 233, 0.2);
  color: #0ea5e9;
}

.nav-icon {
  font-size: 20px;
}

.nav-text {
  font-size: 14px;
}

.sidebar-footer {
  padding: 12px;
  border-top: 1px solid #334155;
}

.collapse-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
  padding: 10px 16px;
  background: transparent;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease;
  font-size: 13px;
}

.collapse-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

/* 主内容区 */
.main-content {
  flex: 1;
  margin-left: 200px;
  transition: margin-left 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar-collapsed + .main-content {
  margin-left: 60px;
}

/* 顶部栏 */
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 24px;
  background: #fff;
  border-bottom: 1px solid #e2e8f0;
  position: sticky;
  top: 0;
  z-index: 50;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f1f5f9;
  padding: 8px 16px;
  border-radius: 8px;
  width: 300px;
}

.search-icon {
  color: #94a3b8;
  font-size: 18px;
}

.search-box input {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  font-size: 14px;
  color: #334155;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.top-bar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.notification-btn {
  position: relative;
  background: transparent;
  border: none;
  cursor: pointer;
  color: #64748b;
  padding: 8px;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.notification-btn:hover {
  background: #f1f5f9;
  color: #334155;
}

.notification-badge {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 18px;
  height: 18px;
  background: #ef4444;
  color: #fff;
  border-radius: 50%;
  font-size: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-menu {
  position: relative;
}

.user-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background: transparent;
  border: none;
  cursor: pointer;
  color: #334155;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.user-btn:hover {
  background: #f1f5f9;
}

.user-avatar {
  width: 32px;
  height: 32px;
  background: #0ea5e9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
}

.expand-icon {
  font-size: 16px;
  transition: transform 0.2s ease;
}

.user-dropdown {
  position: absolute;
  right: 0;
  top: calc(100% + 8px);
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 8px;
  min-width: 160px;
  z-index: 100;
}

.user-dropdown a {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  color: #334155;
  text-decoration: none;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.user-dropdown a:hover {
  background: #f1f5f9;
}

.user-dropdown a:last-child {
  color: #ef4444;
}

.user-dropdown a:last-child:hover {
  background: #fef2f2;
}

/* 内容区域 */
.content-wrapper {
  flex: 1;
  padding: 20px;
  background: #f8fafc;
}

/* 通知弹窗样式 */
.notification-list {
  padding: 10px 0;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.notification-item:hover {
  background-color: #fafafa;
}

.notification-icon {
  flex-shrink: 0;
  color: #67c23a;
}

.notification-icon.warning {
  color: #e6a23c;
}

.notification-icon.info {
  color: #909399;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.rotate-180 {
  transform: rotate(180deg);
}
</style>