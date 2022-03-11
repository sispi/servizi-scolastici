<template>
  <div class="editor">
    <template>
      <form-editor ref="formEditor" :form="form" :activeTab="activeTab" :activeSection="activeSection" @update-active-tab="updateActiveTab" @update-active-section="updateActiveSection" @update-wizard="updateWizard"></form-editor>
    </template>
  </div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Utils from '@/mixins/utils.js'
import FormEditor from '@/components/editor/FormEditor.vue'

export default {
  name: 'Editor',
  mixins: [Utils],
  components: {
    FormEditor
  },
  props: {
    formId: {
      required: true
    }
  },
  data () {
    return {
      form: {
        id: this.formId,
        name: null,
        lastModifiedBy: null,
        lastModifiedTs: null,
        schema: {},
        definition: {},
        backups: []
      },
      activeTab: 0,
      activeSection: null
    }
  },
  async created () {
    this.$axios
      .get(`/forms/${encodeURIComponent(this.formId)}?fetch=definition,schema`)
      .then(response => {
        if (response.data.definition.script === undefined) {
          response.data.definition.script = null
        }
        this.form = response.data
        this.$refs.formEditor.$refs.tab.wizard = this.form.definition.wizard
      })
  },
  methods: {
    updateActiveTab (index) {
      this.activeTab = index
    },
    updateActiveSection (activeSection) {
      this.activeSection = activeSection
    },
    updateWizard (wizard) {
      this.form.definition.wizard = wizard
    }
  }
}
</script>

<style>
  .editor {
    color: #2c3e50;
  }

  .m-b-20 {
    margin-bottom: 20px;
  }

  .m-b-30 {
    margin-bottom: 30px;
  }

  .m-l-10 {
    margin-left: 10px;
  }

  a {
    color: #0970b8!important;
  }

  a:hover {
    color: #35495e!important;
  }

  .addButton {
    color: #0970b8!important;
  }

  .addButton:hover {
    color: #35495e!important;
  }

  .custom-control-input:checked ~ .custom-control-label::before,
  .btn-primary {
    border-color: #0970b8!important;
    background-color: #0970b8!important;
  }

  .grabbable {
    cursor: move; /* fallback if grab cursor is unsupported */
    cursor: grab;
    cursor: -moz-grab;
    cursor: -webkit-grab;
  }

  /* (Optional) Apply a "closed-hand" cursor during drag operation. */
  .grabbable:active {
    cursor: grabbing;
    cursor: -moz-grabbing;
    cursor: -webkit-grabbing;
  }
</style>
