<template>
  <div class="markdown-editor-stub">
    <div class="warning-message">
      Markdown Editor component has been disabled due to 'tui-editor' dependency issues.
    </div>
    <textarea
      class="fallback-textarea"
      :style="{height: height, width: '100%'}"
      :value="value"
      @input="$emit('input', $event.target.value)"
    />
  </div>
</template>

<script>
export default {
  name: 'MarkdownEditor',
  props: {
    value: {
      type: String,
      default: ''
    },
    id: {
      type: String,
      required: false,
      default: 'markdown-editor'
    },
    options: {
      type: Object,
      default() {
        return {}
      }
    },
    mode: {
      type: String,
      default: 'markdown'
    },
    height: {
      type: String,
      required: false,
      default: '300px'
    },
    language: {
      type: String,
      required: false,
      default: 'en_US'
    }
  },
  methods: {
    setValue(value) {
      this.$emit('input', value)
    },
    getValue() {
      return this.value
    },
    setHtml(value) {
      // no-op
    },
    getHtml() {
      return this.value
    }
  }
}
</script>

<style scoped>
.warning-message {
  padding: 10px;
  background-color: #fdf6ec;
  color: #e6a23c;
  border: 1px solid #faecd8;
  margin-bottom: 10px;
  border-radius: 4px;
}
.fallback-textarea {
  padding: 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-family: monospace;
}
</style>
