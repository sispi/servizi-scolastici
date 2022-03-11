<template>
  <div class="error">
    <img src="@/assets/logo.png">
    <h1>Oops, this page doesn't exists</h1>
    <a href="/">Reload</a>
  </div>
</template>

<script>
export default {
  name: 'Error404'
}
</script>

<style scoped>
.error {
  text-align: center;
  color: #2c3e50;
}
</style>
