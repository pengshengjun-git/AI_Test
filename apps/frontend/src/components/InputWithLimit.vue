<template>
  <div class="input-with-limit">
    <el-input
      :type="type"
      v-model="inputValue"
      :placeholder="placeholder"
      :maxlength="maxlength"
      :rows="rows"
      @change="$emit('change', $event)"
      :class="{ 'input-exceeded': isExceeded }"
    />
    <div v-if="showCounter" class="counter" :class="{ 'counter-exceeded': isExceeded }">
      {{ currentLength }}/{{ maxlength }}
    </div>
    <div v-if="showTip && isExceeded" class="tip">
      <span class="material-symbols-outlined">error</span>
      <span>已超出 {{ currentLength - maxlength }} 个字符</span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

const props = withDefaults(defineProps<{
  modelValue: string | undefined
  type?: 'text' | 'textarea'
  placeholder?: string
  maxlength?: number
  rows?: number
  showCounter?: boolean
  showTip?: boolean
}>(), {
  type: 'text',
  placeholder: '',
  maxlength: 100,
  rows: 4,
  showCounter: true,
  showTip: true
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'change', event: Event): void
}>()

const inputValue = ref(props.modelValue || '')

watch(() => props.modelValue, (newVal) => {
  inputValue.value = newVal || ''
})

watch(inputValue, (newVal) => {
  if (newVal.length > props.maxlength) {
    inputValue.value = newVal.slice(0, props.maxlength)
    emit('update:modelValue', inputValue.value)
  } else {
    emit('update:modelValue', newVal)
  }
})

const currentLength = computed(() => (props.modelValue || '').length)

const isExceeded = computed(() => currentLength.value > props.maxlength)
</script>

<style scoped>
.input-with-limit {
  position: relative;
  display: flex;
  flex-direction: column;
}

.input-exceeded :deep(.el-input__inner) {
  border-color: #ef4444;
  background-color: #fef2f2;
}

.counter {
  align-self: flex-end;
  font-size: 12px;
  color: #9ca3af;
  margin-top: 4px;
}

.counter-exceeded {
  color: #ef4444;
}

.tip {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #ef4444;
  margin-top: 4px;
}

.tip .material-symbols-outlined {
  font-size: 14px;
}
</style>