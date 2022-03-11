<template>
  <div id="app">
    <notifications position="center"/>
    <router-view :key="$route.fullPath"/>
  </div>
</template>

<style>
#app {
  color: #2c3e50;
}
</style>
