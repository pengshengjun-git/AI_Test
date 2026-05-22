<template>
  <div class="card-wrapper">
    <div 
      class="card" 
      :class="{ dragging: isDragging, 'card-collapsible': collapsible }"
      ref="cardRef"
    >
      <div 
        v-if="title" 
        class="card-header"
        @mousedown="startDrag"
        @mouseup="stopDrag"
        @mouseleave="stopDrag"
      >
        <h2 class="text-lg font-medium text-on-surface">{{ title }}</h2>
        <button v-if="collapsible" @click.stop="toggleCollapse" class="collapse-btn">
          <span class="material-symbols-outlined" :style="{ transform: collapsed ? 'rotate(-90deg)' : 'rotate(0deg)' }">expand_more</span>
        </button>
      </div>
      <div class="card-content" v-show="!collapsed">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineProps<{
  id?: string
  title?: string
  collapsible?: boolean
}>()

const cardRef = ref<HTMLElement | null>(null)
const collapsed = ref(false)
const isDragging = ref(false)
const offsetX = ref(0)
const offsetY = ref(0)
const startX = ref(0)
const startY = ref(0)

const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}

const startDrag = (e: MouseEvent) => {
  if (!cardRef.value) return
  isDragging.value = true
  startX.value = e.clientX
  startY.value = e.clientY
  const rect = cardRef.value.getBoundingClientRect()
  offsetX.value = e.clientX - rect.left
  offsetY.value = e.clientY - rect.top
  window.addEventListener('mousemove', onDrag)
  window.addEventListener('mouseup', stopDrag)
}

const onDrag = (e: MouseEvent) => {
  if (!isDragging.value || !cardRef.value) return
  const dx = e.clientX - startX.value
  const dy = e.clientY - startY.value
  cardRef.value.style.transform = `translate(${dx}px, ${dy}px)`
  cardRef.value.style.zIndex = '1000'
}

const stopDrag = () => {
  isDragging.value = false
  window.removeEventListener('mousemove', onDrag)
  window.removeEventListener('mouseup', stopDrag)
}
</script>

<style scoped>
.card-wrapper {
  width: 100%;
}

.card {
  transition: box-shadow 0.2s ease, transform 0.2s ease;
  user-select: none;
}

.card:hover {
  box-shadow: 0 4px 20px rgba(0,0,0,0.1);
}

.card-header {
  padding: 1.25rem;
  border-bottom: 1px solid #c1c6d7;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: grab;
}

.card-header:active {
  cursor: grabbing;
}

.dragging {
  opacity: 0.8;
  box-shadow: 0 8px 30px rgba(0,0,0,0.2) !important;
  z-index: 1000;
  transition: box-shadow 0.2s ease, opacity 0.2s ease;
}

.collapse-btn {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  color: #717786;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: color 0.2s ease;
}

.collapse-btn:hover {
  color: #0058bc;
}

.collapse-btn .material-symbols-outlined {
  transition: transform 0.2s ease;
}

.card-content {
  width: 100%;
  transition: opacity 0.2s ease, max-height 0.2s ease;
}
</style>
