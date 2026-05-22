<template>
  <span 
    class="tooltip-text" 
    :class="{ 'has-tooltip': displayText.length > maxLength }"
    @mouseenter="showTooltip = true"
    @mouseleave="showTooltip = false"
    @mousemove="handleMouseMove"
  >
    {{ displayText.length > maxLength ? displayText.slice(0, maxLength) + '...' : displayText }}
    <div 
      v-if="showTooltip && displayText.length > maxLength" 
      class="tooltip-content"
      :style="tooltipStyle"
    >
      {{ displayText }}
    </div>
  </span>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = withDefaults(defineProps<{
  text?: string
  maxLength?: number
}>(), {
  maxLength: 10
})

const showTooltip = ref(false)
const tooltipPosition = ref({ x: 0, y: 0 })

const displayText = computed(() => props.text || '')

const tooltipStyle = computed(() => ({
  left: `${tooltipPosition.value.x + 10}px`,
  top: `${tooltipPosition.value.y - 10}px`
}))

const handleMouseMove = (event: MouseEvent) => {
  tooltipPosition.value = {
    x: event.clientX,
    y: event.clientY
  }
}
</script>

<style scoped>
.tooltip-text {
  position: relative;
  cursor: help;
  color: inherit;
}

.tooltip-text.has-tooltip {
  text-decoration: underline;
  text-decoration-style: dotted;
  text-underline-offset: 2px;
}

.tooltip-content {
  position: fixed;
  max-width: 300px;
  padding: 8px 12px;
  background: #303133;
  color: #fff;
  font-size: 14px;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.2);
  z-index: 9999;
  word-break: break-word;
  white-space: pre-wrap;
}

.tooltip-content::after {
  content: '';
  position: absolute;
  left: -8px;
  top: 50%;
  transform: translateY(-50%);
  border-width: 8px;
  border-style: solid;
  border-color: transparent #303133 transparent transparent;
}
</style>