<template>
  <div class="console">
    <template>
      <form-console></form-console>
    </template>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import FormConsole from '@/components/console/FormConsole.vue'

export default {
  name: 'Console',
  components: {
    FormConsole
  }
}
</script>

<style scoped>
  .console {
    color: #2c3e50;
  }
</style>
