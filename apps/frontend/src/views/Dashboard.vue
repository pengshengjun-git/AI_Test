<template>
  <main class="max-w-full mx-auto px-4 py-6 space-y-6 dashboard-container">
    <!-- 顶部指标卡片 -->
    <section class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      <div class="bg-surface-container-lowest p-6 rounded-lg shadow-sm border border-outline-variant flex items-center gap-6">
        <div class="p-4 bg-primary-fixed rounded-lg">
          <svg class="w-8 h-8 text-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
          </svg>
        </div>
        <div>
          <div class="text-3xl font-bold text-on-surface">{{ stats.projectCount || 0 }}</div>
          <div class="text-sm text-on-surface-variant">项目数量</div>
        </div>
      </div>
      <div class="bg-surface-container-lowest p-6 rounded-lg shadow-sm border border-outline-variant flex items-center gap-6">
        <div class="p-4 bg-tertiary-fixed-dim/20 rounded-lg">
          <svg class="w-8 h-8 text-success" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
          </svg>
        </div>
        <div>
          <div class="text-3xl font-bold text-on-surface">{{ stats.testcaseCount || 0 }}</div>
          <div class="text-sm text-on-surface-variant">测试用例</div>
        </div>
      </div>
      <div class="bg-surface-container-lowest p-6 rounded-lg shadow-sm border border-outline-variant flex items-center gap-6">
        <div class="p-4 bg-error-container rounded-lg">
          <svg class="w-8 h-8 text-error" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
          </svg>
        </div>
        <div>
          <div class="text-3xl font-bold text-on-surface">{{ stats.defectCount || 0 }}</div>
          <div class="text-sm text-on-surface-variant">缺陷数量</div>
        </div>
      </div>
      <div class="bg-surface-container-lowest p-6 rounded-lg shadow-sm border border-outline-variant flex items-center gap-6">
        <div class="p-4 bg-surface-variant rounded-lg">
          <svg class="w-8 h-8 text-on-surface-variant" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"></path>
          </svg>
        </div>
        <div>
          <div class="text-3xl font-bold text-on-surface">{{ stats.aiGenerated || 892 }}</div>
          <div class="text-sm text-on-surface-variant">AI生成</div>
        </div>
      </div>
    </section>

    <!-- 可拖拽的两列网格布局 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <div
        v-for="(card, index) in allCards"
        :key="card.id"
        draggable="true"
        @dragstart="handleDragStart($event, card, index)"
        @dragover.prevent="handleDragOver($event, card, index)"
        @dragenter="handleDragEnter($event)"
        @dragleave="handleDragLeave($event)"
        @drop="handleDrop($event, card, index)"
        @dragend="handleDragEnd"
        :class="['transition-all duration-200', {
          'opacity-50 scale-105': draggedCard && draggedCard.id === card.id,
          'ring-2 ring-primary ring-offset-2': dragOverCard && dragOverCard.id === card.id
        }]"
      >
        <!-- 计算效率卡片 -->
        <div v-if="card.id === 'efficiency'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards.efficiency ? 'height: 60px;' : 'min-height: 400px; max-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('efficiency')">
            <h2 class="text-lg font-medium text-on-surface">计算效率</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards.efficiency }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards.efficiency" class="p-4" style="height: calc(400px - 60px); overflow-y: auto;">
            <div class="grid grid-cols-1 lg:grid-cols-2 gap-4">
              <div class="space-y-2">
                <h3 class="text-sm font-medium text-on-surface-variant">AI 处理效率 (最近7天)</h3>
                <div class="h-32 w-full bg-surface-container rounded flex items-end justify-between p-3 gap-1">
                  <div v-for="(item, i) in aiEfficiencyData" :key="i" class="bg-primary w-full rounded-t transition-all" :style="{ height: item.value + '%' }"></div>
                </div>
                <div class="flex justify-between text-[10px] text-on-surface-variant">
                  <span v-for="(item, i) in aiEfficiencyData" :key="i">{{ item.date }}</span>
                </div>
              </div>
              <div class="flex flex-col items-center justify-center space-y-2">
                <h3 class="text-sm font-medium text-on-surface-variant">整体资源利用率</h3>
                <div class="relative w-32 h-32">
                  <svg viewBox="0 0 160 160" class="w-full h-full transform -rotate-90">
                    <circle cx="80" cy="80" fill="transparent" r="70" stroke="#eceef0" stroke-width="12"></circle>
                    <circle cx="80" cy="80" fill="transparent" r="70" stroke="#005bc1" :stroke-dasharray="440" :stroke-dashoffset="440 - (stats.resourceUtilization / 100) * 440" stroke-width="12"></circle>
                  </svg>
                  <div class="absolute inset-0 flex flex-col items-center justify-center">
                    <span class="text-xl font-bold text-on-surface">{{ stats.resourceUtilization }}%</span>
                    <span class="text-xs text-on-surface-variant">运行中</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 用例执行卡片 -->
        <div v-else-if="card.id === 'execution'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards.execution ? 'height: 60px;' : 'min-height: 400px; max-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('execution')">
            <h2 class="text-lg font-medium text-on-surface">用例执行</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards.execution }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards.execution" class="p-4" style="height: calc(400px - 60px); overflow-y: auto;">
            <div class="flex flex-wrap gap-2 mb-3">
              <button
                v-for="filter in timeFilters"
                :key="filter.value"
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', selectedTimeFilter === filter.value ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="selectedTimeFilter = filter.value"
              >
                {{ filter.label }}
              </button>
              <span class="text-xs text-on-surface-variant self-center">|</span>
              <button
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', executionScope === 'project' ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="executionScope = 'project'"
              >项目所有BUG</button>
              <button
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', executionScope === 'requirement' ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="executionScope = 'requirement'"
              >需求所有BUG</button>
            </div>
            <div class="flex items-center gap-4">
              <div class="relative w-28 h-28">
                <svg viewBox="0 0 144 144" class="w-full h-full transform -rotate-90">
                  <circle cx="72" cy="72" fill="transparent" r="64" stroke="#eceef0" stroke-width="10"></circle>
                  <circle cx="72" cy="72" fill="transparent" r="64" stroke="#3b82f6" :stroke-dasharray="402" :stroke-dashoffset="402 - (executionStats.passRate / 100) * 402" stroke-width="10" stroke-linecap="round"></circle>
                  <circle cx="72" cy="72" fill="transparent" r="64" stroke="#22c55e" :stroke-dasharray="402" :stroke-dashoffset="-executionStats.passRate / 100 * 402" stroke-width="10" stroke-linecap="round"></circle>
                  <circle cx="72" cy="72" fill="transparent" r="64" stroke="#ef4444" :stroke-dasharray="402" :stroke-dashoffset="-(executionStats.passRate + executionStats.executeRate) / 100 * 402" stroke-width="10" stroke-linecap="round"></circle>
                </svg>
                <div class="absolute inset-0 flex flex-col items-center justify-center">
                  <span class="text-xl font-bold text-on-surface">{{ executionStats.totalRate }}%</span>
                  <span class="text-xs text-on-surface-variant">执行率</span>
                </div>
              </div>
              <div class="space-y-2">
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 rounded-full bg-blue-500"></span>
                  <span class="text-sm text-on-surface-variant">通过率: {{ executionStats.passRate }}%</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 rounded-full bg-green-500"></span>
                  <span class="text-sm text-on-surface-variant">执行率: {{ executionStats.executeRate }}%</span>
                </div>
                <div class="flex items-center gap-2">
                  <span class="w-2 h-2 rounded-full bg-red-500"></span>
                  <span class="text-sm text-on-surface-variant">阻塞率: {{ executionStats.blockRate }}%</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 缺陷统计卡片 -->
        <div v-else-if="card.id === 'defect-stat'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['defect-stat'] ? 'height: 60px;' : 'min-height: 400px; max-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('defect-stat')">
            <h2 class="text-lg font-medium text-on-surface">缺陷统计</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['defect-stat'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards['defect-stat']" class="p-4" style="height: calc(400px - 60px); overflow-y: auto;">
            <div class="flex flex-wrap gap-2 mb-3">
              <button
                v-for="filter in timeFilters"
                :key="filter.value"
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', selectedDefectFilter === filter.value ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="selectedDefectFilter = filter.value"
              >
                {{ filter.label }}
              </button>
              <span class="text-xs text-on-surface-variant self-center">|</span>
              <button
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', defectScope === 'project' ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="defectScope = 'project'"
              >项目所有BUG</button>
              <button
                :class="['px-2 py-1 text-xs rounded border border-outline-variant transition-colors', defectScope === 'requirement' ? 'bg-primary text-primary-fixed' : 'hover:bg-surface-container-low']"
                @click.stop="defectScope = 'requirement'"
              >需求所有BUG</button>
            </div>
            <div class="h-32 w-full bg-surface-container rounded flex items-end justify-between p-3 gap-1">
              <div v-for="(item, i) in defectData" :key="i" class="w-full flex flex-col items-center gap-1">
                <div class="w-full flex flex-col gap-0.5" style="height: 100px">
                  <div class="w-full bg-red-500 rounded-t transition-all" :style="{ height: item.new + '%' }"></div>
                  <div class="w-full bg-green-500 rounded-b transition-all" :style="{ height: item.closed + '%' }"></div>
                </div>
                <span class="text-[10px] text-on-surface-variant">{{ item.date }}</span>
              </div>
            </div>
            <div class="flex justify-center gap-4 mt-3">
              <div class="flex items-center gap-2">
                <span class="w-2 h-2 rounded-sm bg-red-500"></span>
                <span class="text-xs text-on-surface-variant">新发现缺陷</span>
              </div>
              <div class="flex items-center gap-2">
                <span class="w-2 h-2 rounded-sm bg-green-500"></span>
                <span class="text-xs text-on-surface-variant">已关闭缺陷</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 需求完成进度卡片 -->
        <div v-else-if="card.id === 'requirement-progress'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['requirement-progress'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('requirement-progress')">
            <h2 class="text-lg font-medium text-on-surface">需求完成进度</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['requirement-progress'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards['requirement-progress']" class="p-5" style="height: calc(400px - 60px); overflow-y: auto;">
            <div v-for="item in requirementProgressDisplay" :key="item.name" class="space-y-2 mb-4">
              <div class="flex justify-between items-center">
                <span class="text-sm text-on-surface">{{ item.name }}</span>
                <span class="text-sm font-medium text-on-surface-variant">{{ item.percent }}%</span>
              </div>
              <div class="h-2 bg-surface-container rounded-full overflow-hidden">
                <div class="h-full rounded-full transition-all" :style="{ width: item.percent + '%', background: item.color }"></div>
              </div>
              <div class="flex justify-between text-xs text-on-surface-variant">
                <span>模块: {{ item.module }}</span>
                <span>优先级: {{ item.priority }}</span>
              </div>
            </div>
            <div v-if="requirementProgressDisplay.length === 0" class="text-center text-on-surface-variant py-4">暂无数据</div>
            <div class="mt-4 flex justify-center">
              <el-pagination
                :key="'req-pagination-' + pagination.requirementPage"
                v-model:current-page="pagination.requirementPage"
                :page-size="5"
                :total="requirementProgress.length"
                layout="prev, pager, next"
                @current-change="loadRequirementProgress"
                small
              />
            </div>
          </div>
        </div>

        <!-- 项目排期视图卡片 -->
        <div v-else-if="card.id === 'schedule'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards.schedule ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('schedule')">
            <h2 class="text-lg font-medium text-on-surface">项目排期视图</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards.schedule }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards.schedule" class="p-5" style="height: calc(400px - 60px); overflow-y: auto;">
            <div class="flex border-b border-outline-variant pb-3 mb-4">
              <div class="w-28"></div>
              <div class="flex-1 flex justify-around">
                <span v-for="date in scheduleDates" :key="date" class="text-xs text-on-surface-variant">{{ date }}</span>
              </div>
            </div>
            <div v-for="project in projectScheduleDisplay" :key="project.name" class="flex items-center mb-3">
              <div class="w-28 text-sm text-on-surface">{{ project.name }}</div>
              <div class="flex-1 h-4 bg-surface-container rounded relative">
                <div
                  class="absolute h-3 top-0.5 rounded"
                  :style="{ left: project.startPercent + '%', width: project.widthPercent + '%', background: project.color }"
                  :title="project.status"
                ></div>
              </div>
            </div>
            <div v-if="projectScheduleDisplay.length === 0" class="text-center text-on-surface-variant py-4">暂无数据</div>
            <div class="mt-4 flex justify-center">
              <el-pagination
                :key="'schedule-pagination-' + pagination.schedulePage"
                v-model:current-page="pagination.schedulePage"
                :page-size="5"
                :total="projectSchedule.length"
                layout="prev, pager, next"
                @current-change="loadSchedule"
                small
              />
            </div>
          </div>
        </div>

        <!-- 最近项目卡片 -->
        <div v-else-if="card.id === 'recent-projects'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['recent-projects'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('recent-projects')">
            <h2 class="text-lg font-medium text-on-surface">最近项目</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['recent-projects'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards['recent-projects']" class="p-5" style="height: calc(400px - 60px); overflow-y: auto;">
            <table class="w-full text-left">
              <thead>
                <tr class="text-on-surface-variant border-b border-outline-variant text-sm">
                  <th class="pb-4 font-normal">项目名称</th>
                  <th class="pb-4 font-normal">状态</th>
                  <th class="pb-4 font-normal">创建时间</th>
                </tr>
              </thead>
              <tbody class="text-on-surface text-sm">
                <tr v-for="project in recentProjectsDisplay" :key="project.id" class="border-b border-surface-container">
                  <td class="py-5">{{ project.name }}</td>
                  <td class="py-5">
                    <span :class="['px-2 py-1 text-xs rounded border border-outline-variant', getProjectStatusClass(project.status)]">
                      {{ getStatusText(project.status) }}
                    </span>
                  </td>
                  <td class="py-5 text-on-surface-variant">{{ formatDateTime(project.created_at || project.createdAt) }}</td>
                </tr>
                <tr v-if="recentProjectsDisplay.length === 0">
                  <td colspan="3" class="py-5 text-center text-on-surface-variant">暂无数据</td>
                </tr>
              </tbody>
            </table>
            <div class="mt-4 flex justify-center">
              <el-pagination
                :key="'project-pagination-' + pagination.projectPage"
                v-model:current-page="pagination.projectPage"
                :page-size="5"
                :total="projectPagination.total"
                layout="prev, pager, next"
                @current-change="loadRecentProjects"
                small
              />
            </div>
          </div>
        </div>

        <!-- 快捷入口卡片 -->
        <div v-else-if="card.id === 'quick-links'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['quick-links'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('quick-links')">
            <h2 class="text-lg font-medium text-on-surface">快捷入口</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['quick-links'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-show="!collapsedCards['quick-links']" class="p-5 grid grid-cols-2 gap-4" style="height: calc(400px - 60px);">
            <button
              class="flex flex-col items-center justify-center p-4 hover:bg-surface-container-low transition-colors rounded-lg border border-outline-variant group"
              @click.stop="handleQuickLink('project')"
            >
              <el-icon class="text-2xl text-primary mb-2 group-hover:scale-110 transition-transform"><Plus /></el-icon>
              <span class="text-sm text-on-surface">新建项目</span>
            </button>
            <button
              class="flex flex-col items-center justify-center p-4 hover:bg-surface-container-low transition-colors rounded-lg border border-outline-variant group"
              @click.stop="handleQuickLink('testcase')"
            >
              <el-icon class="text-2xl text-secondary mb-2 group-hover:scale-110 transition-transform"><Document /></el-icon>
              <span class="text-sm text-on-surface">创建用例</span>
            </button>
            <button
              class="flex flex-col items-center justify-center p-4 hover:bg-surface-container-low transition-colors rounded-lg border border-outline-variant group"
              @click.stop="handleQuickLink('defect')"
            >
              <el-icon class="text-2xl text-error mb-2 group-hover:scale-110 transition-transform"><Warning /></el-icon>
              <span class="text-sm text-on-surface">报告缺陷</span>
            </button>
            <button
              class="flex flex-col items-center justify-center p-4 hover:bg-surface-container-low transition-colors rounded-lg border border-outline-variant group"
              @click.stop="handleQuickLink('ai')"
            >
              <el-icon class="text-2xl text-tertiary mb-2 group-hover:scale-110 transition-transform"><Cpu /></el-icon>
              <span class="text-sm text-on-surface">AI生成</span>
            </button>
          </div>
        </div>

        <!-- 最近缺陷卡片 -->
        <div v-else-if="card.id === 'recent-defects'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['recent-defects'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('recent-defects')">
            <h2 class="text-lg font-medium text-on-surface">最近缺陷</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['recent-defects'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards['recent-defects']" class="p-5" style="height: calc(400px - 60px); overflow-y: auto;">
            <table class="w-full text-left">
              <thead>
                <tr class="text-on-surface-variant border-b border-outline-variant text-sm">
                  <th class="pb-4 font-normal">ID</th>
                  <th class="pb-4 font-normal">标题</th>
                  <th class="pb-4 font-normal">优先级</th>
                  <th class="pb-4 font-normal">状态</th>
                </tr>
              </thead>
              <tbody class="text-on-surface text-sm">
                <tr v-for="defect in recentDefectsDisplay" :key="defect.id" class="border-b border-surface-container">
                  <td class="py-5 text-on-surface-variant">#DEF-{{ defect.id }}</td>
                  <td class="py-5">{{ defect.title }}</td>
                  <td class="py-5">
                    <span :class="['px-2 py-1 text-xs rounded border border-outline-variant', getPriorityClass(defect.priority)]">
                      {{ getPriorityText(defect.priority) }}
                    </span>
                  </td>
                  <td class="py-5">{{ getStatusText(defect.status) }}</td>
                </tr>
                <tr v-if="recentDefectsDisplay.length === 0">
                  <td colspan="4" class="py-5 text-center text-on-surface-variant">暂无数据</td>
                </tr>
              </tbody>
            </table>
            <div class="mt-4 flex justify-center">
              <el-pagination
                :key="'defect-pagination-' + pagination.defectPage"
                v-model:current-page="pagination.defectPage"
                :page-size="5"
                :total="defectPagination.total"
                layout="prev, pager, next"
                @current-change="loadRecentDefects"
                small
              />
            </div>
          </div>
        </div>

        <!-- 待办事项卡片 -->
        <div v-else-if="card.id === 'todo-list'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['todo-list'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('todo-list')">
            <h2 class="text-lg font-medium text-on-surface">待办事项</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['todo-list'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-if="!collapsedCards['todo-list']" class="p-5" style="height: calc(400px - 60px); overflow-y: auto;">
            <div v-for="item in todoItemsDisplay" :key="item.id" class="flex items-start gap-3 cursor-pointer" @click.stop="toggleTodo(item)">
              <input
                class="mt-1 rounded border-outline-variant text-primary focus:ring-primary"
                type="checkbox"
                :checked="item.completed"
                @change.stop="toggleTodo(item)"
                @click.stop
              >
              <div class="flex-1">
                <p :class="['text-sm font-medium', { 'text-on-surface-variant line-through': item.completed, 'text-on-surface': !item.completed }]">
                  {{ item.title }}
                </p>
                <p class="text-xs text-on-surface-variant">截止时间: {{ item.deadline || '' }}</p>
              </div>
            </div>
            <div v-if="todoItemsDisplay.length === 0" class="text-center text-on-surface-variant py-4">暂无待办</div>
            <div class="mt-4 flex justify-center">
              <el-pagination
                :key="'todo-pagination-' + pagination.todoPage"
                v-model:current-page="pagination.todoPage"
                :page-size="5"
                :total="todoItems.length"
                layout="prev, pager, next"
                @current-change="loadTodoItems"
                small
              />
            </div>
          </div>
        </div>

        <!-- 系统状态卡片 -->
        <div v-else-if="card.id === 'system-status'" class="bg-surface-container-lowest rounded-lg shadow-sm border border-outline-variant transition-all duration-300" :style="collapsedCards['system-status'] ? 'height: 60px;' : 'min-height: 400px;'">
          <div class="p-5 border-b border-outline-variant flex items-center justify-between cursor-pointer" @click="toggleCard('system-status')">
            <h2 class="text-lg font-medium text-on-surface">系统状态</h2>
            <el-icon :class="{ 'rotate-180': collapsedCards['system-status'] }" class="transition-transform"><ArrowDown /></el-icon>
          </div>
          <div v-show="!collapsedCards['system-status']" class="p-5 space-y-6" style="height: calc(400px - 60px);">
            <div v-for="service in systemServices" :key="service.name" class="flex items-center justify-between">
              <span class="text-on-surface-variant">{{ service.name }}</span>
              <span :class="['px-2 py-1 text-xs rounded border border-outline-variant', service.status === 'online' ? 'bg-tertiary-fixed text-on-tertiary-fixed-variant' : 'bg-error-container text-error']">
                {{ service.status === 'online' ? '在线' : '离线' }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
@media screen and (max-width: 768px) {
  .dashboard-container {
    padding: 12px !important;
  }
}

@media screen and (max-width: 576px) {
  .dashboard-container {
    padding: 8px !important;
  }

  :deep(.el-pagination) {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>

<script setup lang="ts">
import { reactive, ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowDown, Plus, Document, Warning, Cpu } from '@element-plus/icons-vue'
import { getDashboardStats, getRecentProjects, getRecentDefects, getSystemStatus, getTodoList, toggleTodoStatus } from '@/api/dashboard'
import { getStatusText, getPriorityText } from '@/utils/statusMap'
import { showError } from '@/utils/message'

const router = useRouter()

const collapsedCards = reactive({
  'efficiency': false,
  'execution': false,
  'defect-stat': false,
  'requirement-progress': false,
  'schedule': false,
  'recent-projects': false,
  'quick-links': false,
  'recent-defects': false,
  'todo-list': false,
  'system-status': false
})

const toggleCard = (cardId: string) => {
  collapsedCards[cardId as keyof typeof collapsedCards] = !collapsedCards[cardId as keyof typeof collapsedCards]
}

const draggedCard = ref<{ id: string } | null>(null)
const draggedIndex = ref<number>(-1)
const dragOverCard = ref<{ id: string } | null>(null)

const allCards = reactive([
  { id: 'efficiency' },
  { id: 'execution' },
  { id: 'defect-stat' },
  { id: 'requirement-progress' },
  { id: 'schedule' },
  { id: 'recent-projects' },
  { id: 'quick-links' },
  { id: 'recent-defects' },
  { id: 'todo-list' },
  { id: 'system-status' }
])

const handleDragStart = (event: DragEvent, card: { id: string }, index: number) => {
  draggedCard.value = card
  draggedIndex.value = index
  if (event.dataTransfer) {
    event.dataTransfer.effectAllowed = 'move'
  }
}

const handleDragOver = (_event: DragEvent, _card: { id: string }, _index: number) => {
}

const handleDragEnter = (event: DragEvent) => {
  event.preventDefault()
}

const handleDragLeave = (_event: DragEvent) => {
}

const handleDrop = (_event: DragEvent, _card: { id: string }, dropIndex: number) => {
  if (draggedCard.value && draggedIndex.value !== dropIndex) {
    const [removed] = allCards.splice(draggedIndex.value, 1)
    allCards.splice(dropIndex, 0, removed)
  }
  dragOverCard.value = null
}

const handleDragEnd = () => {
  draggedCard.value = null
  draggedIndex.value = -1
  dragOverCard.value = null
}

const stats = reactive({
  projectCount: 0,
  testcaseCount: 0,
  defectCount: 0,
  aiGenerated: 892,
  resourceUtilization: 75
})

const aiEfficiencyData = [
  { date: '01-10', value: 45 },
  { date: '01-11', value: 58 },
  { date: '01-12', value: 42 },
  { date: '01-13', value: 65 },
  { date: '01-14', value: 52 },
  { date: '01-15', value: 72 },
  { date: '今天', value: 60 }
]

const timeFilters = [
  { label: '最近7天', value: '7d' },
  { label: '今天', value: 'today' },
  { label: '最近一月', value: '30d' }
]

const selectedTimeFilter = ref('7d')
const selectedDefectFilter = ref('7d')
const executionScope = ref('project')
const defectScope = ref('project')

const executionStats = reactive({
  passRate: 75,
  executeRate: 20,
  blockRate: 5,
  totalRate: 85
})

const defectData = [
  { date: '01-10', new: 60, closed: 40 },
  { date: '01-11', new: 70, closed: 50 },
  { date: '01-12', new: 50, closed: 60 },
  { date: '01-13', new: 80, closed: 45 },
  { date: '01-14', new: 40, closed: 70 },
  { date: '01-15', new: 65, closed: 55 },
  { date: '今天', new: 55, closed: 60 }
]

const requirementProgress = [
  { name: '用户登录模块', module: '认证', priority: 'P0', percent: 85, color: '#3b82f6' },
  { name: '订单管理功能', module: '交易', priority: 'P1', percent: 60, color: '#22c55e' },
  { name: '商品搜索优化', module: '搜索', priority: 'P2', percent: 45, color: '#eab308' },
  { name: '数据统计报表', module: '报表', priority: 'P1', percent: 30, color: '#a855f7' },
  { name: '系统配置管理', module: '系统', priority: 'P3', percent: 15, color: '#94a3b8' }
]

const scheduleDates = ['01-01', '01-08', '01-15', '01-22', '01-29']

const projectSchedule = [
  { name: '电商平台测试', status: '进行中', startPercent: 10, widthPercent: 60, color: '#3b82f6' },
  { name: '用户中心系统', status: '已完成', startPercent: 0, widthPercent: 45, color: '#22c55e' },
  { name: '支付模块测试', status: '进行中', startPercent: 30, widthPercent: 50, color: '#3b82f6' },
  { name: '数据分析平台', status: '待启动', startPercent: 70, widthPercent: 25, color: '#94a3b8' }
]

const recentProjects = ref<any[]>([])
const projectPagination = reactive({ total: 0 })

const recentDefects = ref<any[]>([])
const defectPagination = reactive({ total: 0 })

const todoItems = ref<any[]>([])

const pagination = reactive({
  projectPage: 1,
  defectPage: 1,
  todoPage: 1,
  requirementPage: 1,
  schedulePage: 1
})

const recentProjectsDisplay = computed(() => {
  const start = (pagination.projectPage - 1) * 5
  return recentProjects.value.slice(start, start + 5)
})

const recentDefectsDisplay = computed(() => {
  const start = (pagination.defectPage - 1) * 5
  return recentDefects.value.slice(start, start + 5)
})

const todoItemsDisplay = computed(() => {
  const start = (pagination.todoPage - 1) * 5
  return todoItems.value.slice(start, start + 5)
})

const requirementProgressDisplay = computed(() => {
  const start = (pagination.requirementPage - 1) * 5
  return requirementProgress.slice(start, start + 5)
})

const projectScheduleDisplay = computed(() => {
  const start = (pagination.schedulePage - 1) * 5
  return projectSchedule.slice(start, start + 5)
})

const systemServices = reactive([
  { name: '认证服务', status: 'online' as const },
  { name: '项目服务', status: 'online' as const },
  { name: '用例服务', status: 'online' as const },
  { name: 'AI服务', status: 'online' as const }
])

const getProjectStatusClass = (status: string) => {
  const classMap: Record<string, string> = {
    'PLANNING': 'bg-surface-container-high text-on-surface-variant',
    'ACTIVE': 'bg-primary-fixed text-on-primary-fixed-variant',
    'COMPLETED': 'bg-tertiary-fixed text-on-tertiary-fixed-variant',
    'CLOSED': 'bg-error-container text-error'
  }
  return classMap[status] || 'bg-surface-container-high text-on-surface-variant'
}

const getPriorityClass = (priority: string) => {
  const classMap: Record<string, string> = {
    'P0': 'bg-error-container text-error',
    'P1': 'bg-warning text-on-warning',
    'P2': 'bg-surface-container-high text-on-surface-variant',
    'P3': 'bg-surface-variant text-on-surface-variant'
  }
  return classMap[priority] || 'bg-surface-container-high text-on-surface-variant'
}

const formatDateTime = (dateTime: string | undefined) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleDateString('zh-CN')
}

const handleQuickLink = (type: string) => {
  const routes: Record<string, string> = {
    project: '/project',
    testcase: '/testcase',
    defect: '/defect',
    ai: '/ai-center'
  }
  router.push(routes[type])
}

const toggleTodo = async (item: any) => {
  item.completed = !item.completed
  try {
    await toggleTodoStatus(item.id, item.completed)
  } catch {
    item.completed = !item.completed
    showError('更新失败')
  }
}

const loadStats = async () => {
  try {
    const statsRes = await getDashboardStats()
    if (statsRes.code === 200 || statsRes.code === 0) {
      const data = statsRes.data || statsRes
      stats.projectCount = data.projectCount || 0
      stats.testcaseCount = data.testcaseCount || 0
      stats.defectCount = data.defectCount || 0
      stats.aiGenerated = data.aiGeneratedCount || 892
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadRecentProjects = async (page: number) => {
  pagination.projectPage = page
  try {
    const projectRes = await getRecentProjects()
    if (projectRes.code === 200 || projectRes.code === 0) {
      recentProjects.value = (projectRes.data || []).map((item: any) => ({
        id: item.id,
        name: item.name,
        status: item.status,
        createdAt: item.createdAt
      }))
      projectPagination.total = recentProjects.value.length || 0
    }
  } catch (error) {
    console.error('加载项目数据失败:', error)
  }
}

const loadRecentDefects = async (page: number) => {
  pagination.defectPage = page
  try {
    const defectRes = await getRecentDefects()
    if (defectRes.code === 200 || defectRes.code === 0) {
      recentDefects.value = (defectRes.data || []).map((item: any) => ({
        id: item.id,
        title: item.title,
        priority: item.priority,
        status: item.status
      }))
      defectPagination.total = recentDefects.value.length || 0
    }
  } catch (error) {
    console.error('加载缺陷数据失败:', error)
  }
}

const loadTodoItems = async (page: number) => {
  pagination.todoPage = page
  try {
    const todoRes = await getTodoList()
    if (todoRes.code === 200 || todoRes.code === 0) {
      todoItems.value = todoRes.data || []
    }
  } catch (error) {
    console.error('加载待办事项失败:', error)
  }
}

const loadRequirementProgress = (page: number) => {
  pagination.requirementPage = page
}

const loadSchedule = (page: number) => {
  pagination.schedulePage = page
}

onMounted(() => {
  loadStats()
  loadRecentProjects(1)
  loadRecentDefects(1)
  loadTodoItems(1)
})
</script>
