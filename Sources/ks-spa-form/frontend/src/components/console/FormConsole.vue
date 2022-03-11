<template>
    <div class="form-console">
        <console-toolbar @load-forms="loadForms"></console-toolbar>
        <form-list ref="formList"></form-list>
    </div>
</template>

<script>
import ConsoleToolbar from './ConsoleToolbar.vue'
import FormList from './FormList.vue'
export default {
  name: 'FormConsole',
  components: {
    ConsoleToolbar,
    FormList
  },
  methods: {
    loadForms () {
      this.$refs.formList.load()
    }
  }
}
</script>

<style>
    .toolbar {
      background-color: #0970b8;
      color: white;
    }

    .p-r-10 {
      padding-right: 10px;
    }

    .p-10 {
      padding: 10px;
    }

    a {
      color: #0970b8!important;
    }

    a:hover {
      color: #35495e!important;
    }

    .toolbar .header-link {
      color: white!important;
      cursor: pointer;
    }

    .toolbar .header-link:hover {
      color: lightgray!important;
      text-decoration: none;
    }

    .f-s-24 {
      font-size: 24px;
    }
</style>
