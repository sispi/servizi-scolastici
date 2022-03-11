<template>
  <div class="form-tab">
    <b-card no-body class="m-b-20" style="border: 0; padding: 0;">
      <b-card-body class="p-0">
        <draggable class="nav nav-tabs" tag="ul" role="tablist" :list="tabs" draggable=".draggable">
          <li class="nav-item draggable" v-for="(tab, index) in tabs" :key="'tab-' + index">
            <a class="nav-link" :class="{ active: activeTab == index }" :href="'#tab-' + index" :id="'tab-link-' + index" data-toggle="tab">
              <b>{{tab.title}}</b> &nbsp;
              <a href="javascript:void(0);" class="text-muted" v-show="tab.sticky" v-b-tooltip.hover title="Tab sticky" @click.stop><font-awesome-icon icon="thumbtack" /></a>
              <span style="margin-right: 20px"/>
              <a href="javascript:void(0);" v-b-tooltip.hover title="Modifica"><font-awesome-icon icon="pencil-alt" v-b-modal.modalTab @click="editTab(index)" /></a> &nbsp;
              <a href="javascript:void(0);" v-b-tooltip.hover title="Elimina"><font-awesome-icon icon="times" @click="removeTab(index)" /></a>
            </a>
          </li>
          <li class="nav-item">
            <a href="javascript:void(0);" class="nav-link" v-b-modal.modalTab><span style="font-size: 16px"><font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp; Aggiungi Tab</span></a>
          </li>
          <li class="nav-item ml-auto">
            <div class="nav-link custom-control custom-checkbox" role="button" style="font-size: 16px; border: 0;">
              <input type="checkbox" class="custom-control-input" v-model="wizard" id="wizardCheck" @change="updateWizard">
              <label class="custom-control-label" for="wizardCheck">Wizard</label>
            </div>
          </li>
        </draggable>
        <div class="tab-content m-t-20" id="accordion">
          <div class="tab-pane fade" :class="{ active: activeTab == index, show: activeTab == index }" v-for="(tab, index) in tabs" :id="'tab-' + index" :key="'tab-' + index">
            <form-section ref="section" :sections="tab.sections" :tabs="tabs" :sharedSections="sharedSections" :activeSection="activeSection" :tabId="index" :schema="schema"
              @edit-section="editSection(arguments)" @update-json-elements="updateJsonElements(arguments)" @update-active-section="updateActiveSection"
              @update-active-section-definition="updateActiveSectionDefinition" @remove-shared-section="removeSharedSection"></form-section>
            <b-button variant="default" class="addButton float-left mr-3" v-b-modal.modalSection @click="addSection(index)"><font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp;Aggiungi Sezione</b-button>
          </div>
        </div>
      </b-card-body>
    </b-card>
    <modal-tab :tabs="tabs" :tabIndex="tabIndex" @reset-tab-index="resetTabIndex" @update-active-tab="updateActiveTab" />
    <modal-section :tabs="tabs" :tabIndex="tabIndex" :sectionIndex="sectionIndex" :sharedSections="sharedSections" :schema="schema" :activeSection="activeSection"
      @update-shared-section="updateSharedSection(arguments)" @update-form-builder="updateFormBuilder(arguments)"
      @remove-shared-section="removeSharedSection" @reset-tab-section-index="resetTabSectionIndex" />
  </div>
</template>
<script>
import FormSection from './FormSection.vue'
import ModalTab from './modal/ModalTab.vue'
import ModalSection from './modal/ModalSection'
import draggable from 'vuedraggable'
export default {
  name: 'FormTab',
  components: {
    FormSection,
    ModalTab,
    ModalSection,
    draggable
  },
  props: {
    tabs: null,
    activeTab: null,
    activeSection: null,
    sharedSections: null,
    schema: null
  },
  data () {
    return {
      tabIndex: null,
      sectionIndex: null,
      wizard: false
    }
  },
  methods: {
    editTab (index) {
      this.tabIndex = index
    },
    updateActiveTab (index) {
      this.tabIndex = null
      this.$emit('update-active-tab', index)
    },
    removeTab (index) {
      this.$bvModal.msgBoxConfirm('Sei sicuro di voler eliminare il tab selezionato?', {
        title: 'Elimina Tab',
        okTitle: 'Conferma',
        cancelTitle: 'Annulla',
        hideHeaderClose: false
      }).then(value => {
        if (value) {
          var tab = this.tabs[index]
          var keys = []
          if (tab.sections != null && tab.sections.length > 0) {
            for (var i = 0; i < tab.sections.length; i++) {
              var section = tab.sections[i]
              if (section.shared && !Array.isArray(section.elements)) {
                if (keys.indexOf(section.elements) === -1) {
                  keys.push(section.elements)
                }
              }
            }
          }
          this.tabs.splice(index, 1)
          if (keys.length > 0) {
            for (var j = 0; j < keys.length; j++) {
              var key = keys[j]
              if (!this.isSharedKeyUsedOnce(key)) {
                this.removeSharedSection(key)
              }
            }
          }
          this.tabIndex = null
          this.$emit('update-active-tab', 0)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    resetTabIndex (tabIndex) {
      this.tabIndex = tabIndex
    },
    addSection (index) {
      this.tabIndex = index
    },
    editSection (args) {
      var tabId = args[0]
      var sectionId = args[1]
      this.tabIndex = tabId
      this.sectionIndex = sectionId
    },
    resetTabSectionIndex (index) {
      this.tabIndex = index
      this.sectionIndex = index
    },
    updateJsonElements (args) {
      var tabId = args[0]
      var sectionId = args[1]
      var formData = args[2]
      if (this.tabs != null && this.tabs.length > tabId) {
        var tab = this.tabs[tabId]
        if (tab.sections != null && tab.sections.length > sectionId) {
          var section = tab.sections[sectionId]
          if (Array.isArray(section.elements)) {
            section.elements = formData
          } else {
            this.$emit('update-shared-section', section.elements, formData)
          }
        }
      }
    },
    updateActiveSection (activeSection) {
      this.$emit('update-active-section', activeSection)
    },
    updateActiveSectionDefinition (activeSection) {
      this.$emit('update-active-section-definition', activeSection)
    },
    updateSharedSection (args) {
      var key = args[0]
      var elements = args[1]
      this.$emit('update-shared-section', key, elements)
    },
    updateFormBuilder (args) {
      var tabId = args[0]
      var sectionId = args[1]
      var formData = args[2]
      this.$emit('update-form-builder', tabId, sectionId, formData)
    },
    isSharedKeyUsed (key) {
      if (this.tabs != null && this.tabs.length > 0) {
        var n = 0
        for (var i = 0; i < this.tabs.length; i++) {
          var tab = this.tabs[i]
          if (tab.sections != null && tab.sections.length > 0) {
            for (var j = 0; j < tab.sections.length; j++) {
              var section = tab.sections[j]
              if (section.shared && section.elements === key) {
                n++
              }
            }
          }
        }
        // Se la chiave di condivisione è presente una volta sola nel form allora può essere eliminata
        if (n === 1) {
          return false
        // Altrimenti non può essere eliminata
        } else if (n > 1) {
          return true
        }
      }

      return false
    },
    isSharedKeyUsedOnce (key) {
      if (this.tabs != null && this.tabs.length > 0) {
        var n = 0
        for (var i = 0; i < this.tabs.length; i++) {
          var tab = this.tabs[i]
          if (tab.sections != null && tab.sections.length > 0) {
            for (var j = 0; j < tab.sections.length; j++) {
              var section = tab.sections[j]
              if (section.shared && section.elements === key) {
                n++
              }
            }
          }
        }
        // Se la chiave di condivisione è presente una volta sola nel form allora può essere eliminata
        if (n === 0) {
          return false
        // Altrimenti non può essere eliminata
        } else if (n >= 1) {
          return true
        }
      }

      return false
    },
    removeSharedSection (key) {
      this.$emit('remove-shared-section', key)
    },
    updateWizard () {
      this.$emit('update-wizard', this.wizard)
    }
  }
}
</script>

<style scoped>
  .m-t-20 {
    margin-top: 20px;
  }

  .m-10 {
    margin: 10px;
  }
</style>
