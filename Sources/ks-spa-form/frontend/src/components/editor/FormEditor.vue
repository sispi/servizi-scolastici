<template>
    <div id="accordion" role="tablist">
      <toolbar :form="form" :activeSection="activeSection" @update-active-section-definition="updateActiveSectionDefinition"></toolbar>
      <div class="form-editor">
        <form-header :header="form.definition.header" @update-header="updateHeader"></form-header>
        <button-toolbar :toolbar="form.definition.toolbar" v-show="form.definition.toolbar != null && form.definition.toolbar.position === 'top'" @edit-button="editButton"></button-toolbar>
        <form-tab ref="tab" :tabs="form.definition.tabs" :sharedSections="form.definition.sections" :schema="form.schema" :activeTab="activeTab" :activeSection="activeSection"
          @update-form-builder="updateFormBuilder(arguments)" @update-active-tab="updateActiveTab" @update-active-section="updateActiveSection" @update-active-section-definition="updateActiveSectionDefinition"
          @update-shared-section="updateSharedSection(arguments)" @remove-shared-section="removeSharedSection" @update-wizard="updateWizard"></form-tab>
        <button-toolbar :toolbar="form.definition.toolbar" v-show="form.definition.toolbar != null && form.definition.toolbar.position === 'bottom'" @edit-button="editButton"></button-toolbar>
        <form-footer :footer="form.definition.footer" @update-footer="updateFooter"></form-footer>
        <form-script :script="form.definition.script" @update-script="updateScript"></form-script>
        <modal-button :toolbar="form.definition.toolbar" :buttonIndex="buttonIndex" @reset-button-index="resetButtonIndex" />
      </div>
</template>

<script>
import Toolbar from './Toolbar.vue'
import FormHeader from './FormHeader.vue'
import ButtonToolbar from './ButtonToolbar.vue'
import ModalButton from './modal/ModalButton'
import FormTab from './FormTab.vue'
import FormFooter from './FormFooter.vue'
import FormScript from './FormScript.vue'

export default {
  name: 'FormEditor',
  components: {
    Toolbar,
    FormHeader,
    ButtonToolbar,
    ModalButton,
    FormTab,
    FormFooter,
    FormScript
  },
  props: {
    form: {
      type: Object,
      required: true
    },
    activeTab: null,
    activeSection: null
  },
  data () {
    return {
      buttonIndex: null
    }
  },
  methods: {
    updateHeader (header) {
      if (this.form != null) {
        this.form.definition.header = header
      }
    },
    updateFooter (footer) {
      if (this.form != null) {
        this.form.definition.footer = footer
      }
    },
    updateScript (script) {
      if (this.form != null) {
        this.form.definition.script = script
      }
    },
    updateActiveTab (index) {
      this.$emit('update-active-tab', index)
    },
    updateActiveSection (activeSection) {
      this.$emit('update-active-section', activeSection)
    },
    updateActiveSectionDefinition (activeSection) {
      var activeSectionSplit = activeSection.split('_')
      if (activeSectionSplit.length > 0) {
        var tabId = parseInt(activeSectionSplit[0])
        var sectionId = parseInt(activeSectionSplit[1])
        this.$refs.tab.$refs.section[tabId].$refs.builder[sectionId].updateJsonElements()
      }
    },
    updateFormBuilder (args) {
      var tabId = args[0]
      var sectionId = args[1]
      var formData = args[2]
      this.$refs.tab.$refs.section[tabId].$refs.builder[sectionId].updateFormBuilder(formData)
    },
    updateSharedSection (args) {
      var key = args[0]
      var elements = args[1]
      if (this.form != null) {
        if (this.form.definition != null && this.form.definition.sections != null) {
          this.form.definition.sections[key] = elements
        }
      }
    },
    removeSharedSection (key) {
      if (this.form != null) {
        if (this.form.definition != null && this.form.definition.sections != null) {
          delete this.form.definition.sections[key]
        }
      }
    },
    editButton (buttonIndex) {
      this.buttonIndex = buttonIndex
    },
    resetButtonIndex (buttonIndex) {
      this.buttonIndex = buttonIndex
    },
    updateWizard (wizard) {
      this.$emit('update-wizard', wizard)
    }
  }
}
</script>

<style scoped>
  .form-editor {
    margin: 10px;
  }
</style>
