<template>
  <div v-if="!isLoggedIn && !isLoginPage" class="no-auth-wrapper">
    <router-view />
  </div>
  <div v-else-if="isLoginPage" class="login-wrapper">
    <router-view />
  </div>
  <div v-else-if="!hasAnyPermission" class="no-permission-wrapper">
    <NoPermission />
  </div>
  <div v-else class="app-container">
    <!-- 移动端菜单按钮 -->
    <button class="mobile-menu-btn" @click="toggleMobileMenu" v-if="!isLoginPage">
      <span>{{ isMobileMenuOpen ? '✕' : '☰' }}</span>
    </button>
    
    <!-- 侧边栏遮罩层 -->
    <div class="sidebar-overlay" :class="{ 'show': isMobileMenuOpen }" @click="closeMobileMenu"></div>
    
    <!-- 左侧导航 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': isSidebarCollapsed, 'mobile-open': isMobileMenuOpen }">
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
      
      <nav class="sidebar-nav" :key="menuKey">
        <router-link 
          v-for="item in filteredMenuItems" 
          :key="item.path"
          :to="item.path" 
          class="nav-item"
          :class="{ 'active': route.path === item.path }"
          @click="handleNavClick"
        >
          <span class="nav-icon">{{ item.icon }}</span>
          <span v-if="!isSidebarCollapsed" class="nav-text">{{ item.name }}</span>
        </router-link>

        <template v-if="showAdminMenu">
          <div class="nav-divider" v-if="!isSidebarCollapsed"></div>
          <div class="nav-group-title" v-if="!isSidebarCollapsed">系统管理</div>
          <div class="nav-group-title-icon" v-else>⚙️</div>

          <router-link 
            v-for="item in filteredAdminMenuItems" 
            :key="item.path"
            :to="item.path" 
            class="nav-item"
            :class="{ 'active': route.path.startsWith(item.path.split('/').slice(0, -1).join('/')) }"
            @click="handleNavClick"
          >
            <span class="nav-icon">{{ item.icon }}</span>
            <span v-if="!isSidebarCollapsed" class="nav-text">{{ item.name }}</span>
          </router-link>
        </template>
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
              <div v-if="!isSidebarCollapsed" class="user-info">
                <span class="user-name">{{ displayUserName }}</span>
                <span class="user-role" v-if="displayUserRole">{{ displayUserRole }}</span>
              </div>
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
import { computed, ref, reactive, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { CircleCheck, Warning, InfoFilled } from '@element-plus/icons-vue';
import { getUserInfo } from '@/utils/permission';
import { useUserStore } from '@/stores/user';
import NoPermission from '@/components/NoPermission.vue';

const userStore = useUserStore();

// 菜单权限配置
interface MenuItem {
  path: string
  name: string
  icon: string
  permission?: string
  roles?: string[]
}

const menuItems: MenuItem[] = [
  { path: '/', name: '工作台', icon: '🏠', permission: 'dashboard:read' },
  { path: '/project', name: '项目管理', icon: '📁', permission: 'project:view' },
  { path: '/requirement', name: '需求管理', icon: '📋', permission: 'requirement:view' },
  { path: '/testcase', name: '用例管理', icon: '📝', permission: 'testcase:view' },
  { path: '/testplan', name: '测试计划', icon: '📅', permission: 'testplan:read' },
  { path: '/defect', name: '缺陷管理', icon: '⚠️', permission: 'defect:view' },
  { path: '/coverage', name: '覆盖率', icon: '📊', permission: 'coverage:read' },
  { path: '/strategy', name: '策略管理', icon: '⚙️', permission: 'strategy:read' },
  { path: '/ai-center', name: 'AI中心', icon: '🤖', permission: 'ai:use' }
]

const adminMenuItems: MenuItem[] = [
  { path: '/system/users', name: '用户管理', icon: '👥', permission: 'user:manage' },
  { path: '/system/roles', name: '角色管理', icon: '🔑', permission: 'role:manage' },
  { path: '/system/departments', name: '部门管理', icon: '🏢', permission: 'system:department:*' },
  { path: '/system/menus', name: '菜单管理', icon: '📋', permission: 'system:menu:*' }
]

// 判断用户是否有权限
const hasMenuPermission = (item: MenuItem): boolean => {
  const userInfo = getUserInfo()
  if (!userInfo) return false
  
  const roles = userInfo.roles || []
  const permissions = userInfo.permissions || []
  
  console.log('检查菜单权限 - item:', item.name, 'roles:', roles, 'permissions:', permissions)
  
  // 如果是管理员，拥有所有权限
  if (roles.includes('SUPER_ADMIN') || roles.includes('ADMIN') || permissions.includes('*') || permissions.includes('system:*')) {
    console.log('管理员用户，允许访问菜单:', item.name)
    return true
  }
  
  // 检查角色
  if (item.roles && item.roles.length > 0) {
    if (item.roles.some(r => roles.includes(r))) {
      return true
    }
  }
  
  // 检查权限
  if (item.permission) {
    const hasPerm = permissions.some((p: string) => {
      if (p === item.permission) return true
      if (p.endsWith('*')) {
        return item.permission!.startsWith(p.slice(0, -1))
      }
      return false
    })
    if (hasPerm) return true
  }
  
  // 严格权限控制，必须明确有对应权限才显示
  return false
}

// 过滤后的菜单
const filteredMenuItems = computed(() => {
  return menuItems.filter(item => hasMenuPermission(item))
})

const filteredAdminMenuItems = computed(() => {
  return adminMenuItems.filter(item => hasMenuPermission(item))
})

// 判断是否为管理员
const showAdminMenu = computed(() => {
  return filteredAdminMenuItems.value.length > 0
});

// 判断是否为超级管理员（直接检查）
const isAdminUser = computed(() => {
  try {
    const userInfo = getUserInfo();
    if (!userInfo) return false;
    const roles = userInfo.roles || [];
    const permissions = userInfo.permissions || [];
    return roles.includes('SUPER_ADMIN') || permissions.includes('*') || permissions.includes('system:*');
  } catch (e) {
    console.error('检查管理员权限失败:', e);
    return false;
  }
});

// 判断用户是否有任何模块权限
const hasAnyPermission = computed(() => {
  // 管理员拥有所有权限
  if (isAdminUser.value) {
    console.log('管理员用户，直接授予所有权限');
    return true;
  }
  
  try {
    const userInfo = getUserInfo();
    if (!userInfo) {
      console.warn('未找到用户信息');
      return false;
    }
    
    const roles = userInfo.roles || [];
    const permissions = userInfo.permissions || [];
    
    console.log('用户权限检查 - roles:', roles, 'permissions:', permissions);
    
    // 检查是否有任何模块权限
    const requiredPermissions = [
      'dashboard:read',
      'project:read',
      'project:view',
      'requirement:read',
      'requirement:view',
      'testcase:read',
      'testcase:view',
      'testplan:read',
      'defect:read',
      'defect:view',
      'coverage:read',
      'strategy:read',
      'ai:use',
      'system:user:*',
      'system:role:*',
      'system:department:*',
      'system:menu:*'
    ];
    
    // 检查是否有任何匹配的权限
    const hasMatch = permissions.some(userPerm => {
      if (userPerm === '*') return true;
      return requiredPermissions.some(reqPerm => {
        if (userPerm === reqPerm) return true;
        if (userPerm.endsWith('*')) {
          return reqPerm.startsWith(userPerm.slice(0, -1));
        }
        return false;
      });
    });
    
    console.log('是否有模块权限:', hasMatch);
    return hasMatch;
  } catch (e) {
    console.error('检查模块权限失败:', e);
    // 严格权限控制，出错时默认无权限
    return false;
  }
});

// 强制刷新菜单的标记
const menuKey = ref(0)

const route = useRoute();
const router = useRouter();
const isLoginPage = computed(() => route.path === '/login');
const loggedIn = ref(!!localStorage.getItem('token'));
const isLoggedIn = computed(() => loggedIn.value);
const isSidebarCollapsed = ref(false);
const isMobileMenuOpen = ref(false);
const showUserMenu = ref(false);
const userMenuRef = ref<HTMLElement | null>(null);
const showNotification = ref(false);
const searchQuery = ref('');

// 获取用户名显示 - 直接从localStorage读取，确保初始化时就能正确显示
const displayUserName = computed(() => {
  try {
    const userInfo = getUserInfo();
    if (!userInfo) return '管理员';
    return userInfo.realName || userInfo.username || '管理员';
  } catch (e) {
    console.error('获取用户名失败:', e);
    return '管理员';
  }
});

// 获取用户角色名称显示 - 直接从localStorage读取，确保初始化时就能正确显示
const displayUserRole = computed(() => {
  try {
    const userInfo = getUserInfo();
    if (!userInfo) return '';
    const roles = userInfo.roles || [];
    if (roles.includes('SUPER_ADMIN')) return '超级管理员';
    if (roles.includes('PROJECT_MANAGER')) return '项目经理';
    if (roles.includes('TEST_ENGINEER')) return '测试工程师';
    if (roles.includes('USER')) return '普通用户';
    return '用户';
  } catch (e) {
    console.error('获取用户角色失败:', e);
    return '';
  }
});

const handleClickOutside = (event: MouseEvent) => {
  if (userMenuRef.value && !userMenuRef.value.contains(event.target as Node)) {
    showUserMenu.value = false;
  }
};

router.afterEach(() => {
  loggedIn.value = !!localStorage.getItem('token');
  showUserMenu.value = false;
  isMobileMenuOpen.value = false;
  // 刷新菜单
  menuKey.value++
});

const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value;
};

const toggleMobileMenu = () => {
  isMobileMenuOpen.value = !isMobileMenuOpen.value;
};

const closeMobileMenu = () => {
  isMobileMenuOpen.value = false;
};

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

const handleNavClick = () => {
  isMobileMenuOpen.value = false;
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
  userStore.logout();
  router.push('/login');
  ElMessage.success('退出成功');
};

onMounted(() => {
  // 初始化时从localStorage恢复用户信息
  userStore.getUserInfo()
  // 刷新菜单
  menuKey.value++
  
  // 输出调试信息
  console.log('=== App.vue 调试信息 ===')
  console.log('Token:', localStorage.getItem('token'))
  console.log('UserInfo:', localStorage.getItem('userInfo'))
  console.log('isLoggedIn:', isLoggedIn.value)
  console.log('isAdminUser:', isAdminUser.value)
  console.log('hasAnyPermission:', hasAnyPermission.value)
  console.log('filteredMenuItems:', filteredMenuItems.value)
  console.log('========================')
  
  document.addEventListener('click', handleClickOutside)
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
.no-auth-wrapper,
.no-permission-wrapper {
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
  transition: width 0.3s ease, transform 0.3s ease;
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  z-index: 100;
}

.sidebar.sidebar-collapsed {
  width: 60px;
}

/* 移动端侧边栏 */
.mobile-menu-btn {
  display: none;
  position: fixed;
  top: 16px;
  left: 16px;
  z-index: 200;
  background: #0ea5e9;
  color: white;
  border: none;
  border-radius: 8px;
  width: 40px;
  height: 40px;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  box-shadow: 0 2px 8px rgba(14, 165, 233, 0.3);
}

.sidebar-overlay {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 99;
  backdrop-filter: blur(2px);
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
  flex-shrink: 0;
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  color: #fff;
}

.sidebar-nav {
  flex: 1;
  padding: 12px;
  overflow-y: auto;
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

.nav-divider {
  height: 1px;
  background: #334155;
  margin: 12px 0;
}

.nav-group-title {
  font-size: 11px;
  color: #64748b;
  padding: 8px 16px 4px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.nav-group-title-icon {
  text-align: center;
  padding: 8px;
  font-size: 12px;
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
  flex-shrink: 0;
}

.nav-text {
  font-size: 14px;
  white-space: nowrap;
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
  min-width: 0;
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
  gap: 16px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f1f5f9;
  padding: 8px 16px;
  border-radius: 8px;
  width: 300px;
  min-width: 150px;
  flex-shrink: 1;
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
  min-width: 0;
}

.search-box input::placeholder {
  color: #94a3b8;
}

.top-bar-right {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
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
  flex-shrink: 0;
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
  flex-shrink: 0;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}

.user-role {
  font-size: 11px;
  color: #64748b;
  white-space: nowrap;
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
  overflow-y: auto;
  min-width: 0;
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
  min-width: 0;
}

.notification-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 4px;
  word-wrap: break-word;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.rotate-180 {
  transform: rotate(180deg);
}

/* 响应式布局 */
@media screen and (max-width: 1400px) {
  .search-box {
    width: 250px;
  }
}

@media screen and (max-width: 1200px) {
  .search-box {
    width: 200px;
  }
  
  .content-wrapper {
    padding: 16px;
  }
}

@media screen and (max-width: 992px) {
  .sidebar {
    transform: translateX(-100%);
    box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
  }
  
  .sidebar.mobile-open {
    transform: translateX(0);
  }
  
  .sidebar.sidebar-collapsed {
    transform: translateX(-100%);
    width: 60px;
  }
  
  .sidebar.mobile-open.sidebar-collapsed {
    transform: translateX(0);
  }
  
  .mobile-menu-btn {
    display: flex;
  }
  
  .sidebar-overlay.show {
    display: block;
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .sidebar-collapsed + .main-content {
    margin-left: 0;
  }
  
  .top-bar {
    padding: 12px 16px;
    padding-left: 64px;
  }
  
  .search-box {
    width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .top-bar {
    padding: 8px 12px;
    padding-left: 56px;
  }
  
  .search-box {
    padding: 6px 12px;
  }
  
  .search-box input {
    font-size: 13px;
  }
  
  .content-wrapper {
    padding: 12px;
  }
  
  .user-name {
    display: none;
  }
  
  .notification-btn {
    padding: 6px;
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
}

@media screen and (max-width: 576px) {
  .top-bar {
    flex-wrap: wrap;
    gap: 8px;
  }
  
  .search-box {
    order: 2;
    width: 100%;
  }
  
  .top-bar-right {
    order: 1;
  }
  
  .content-wrapper {
    padding: 8px;
  }
  
  /* 进一步优化移动端表格 */
  :deep(.el-table) {
    font-size: 11px;
  }
  
  :deep(.el-table th),
  :deep(.el-table td) {
    padding: 6px 2px;
  }
  
  /* 隐藏一些非必要的列 */
  :deep(.el-table .hidden-mobile) {
    display: none;
  }
}

@media screen and (max-width: 480px) {
  .mobile-menu-btn {
    width: 36px;
    height: 36px;
    font-size: 18px;
    top: 12px;
    left: 12px;
  }
  
  .top-bar {
    padding: 8px 12px;
    padding-left: 52px;
  }
}

/* 超小屏幕优化 */
@media screen and (max-width: 360px) {
  .search-box {
    padding: 4px 8px;
  }
  
  .search-icon {
    font-size: 16px;
  }
  
  .content-wrapper {
    padding: 6px;
  }
}
</style>