<template>
  <div class="card-wrapper">
    <div v-if="title" class="card-header" @click="toggleCollapse">
      <h2 class="text-lg font-medium text-on-surface">{{ title }}</h2>
      <button v-if="collapsible" class="collapse-btn">
        <span class="material-symbols-outlined" :style="{ transform: collapsed ? 'rotate(-90deg)' : 'rotate(0deg)' }">expand_more</span>
      </button>
    </div>
    <div class="card-content" v-show="!collapsed">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  title?: string
  collapsible?: boolean
  defaultCollapsed?: boolean
  cardId?: string
}

const props = withDefaults(defineProps<Props>(), {
  collapsible: false,
  defaultCollapsed: false,
  cardId: ''
})

const collapsed = ref(props.defaultCollapsed)

const toggleCollapse = () => {
  collapsed.value = !collapsed.value
}
</script>

<style scoped>
.card-wrapper {
  background: #ffffff;
  border-radius: 0.125rem;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  border: 1px solid #c1c6d7;
  overflow: hidden;
}

.card-header {
  padding: 1.25rem;
  border-bottom: 1px solid #c1c6d7;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}

.collapse-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #414755;
}

.collapse-btn:hover {
  color: #0058bc;
}

.collapse-btn .material-symbols-outlined {
  transition: transform 0.3s ease;
}

.card-content {
  transition: opacity 0.3s ease;
}
</style>
