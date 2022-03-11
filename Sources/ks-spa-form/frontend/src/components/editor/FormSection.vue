<template>
    <div class="form-section">
      <draggable :list="sections" draggable=".draggable" @start="collapseActiveSection" handle=".handle">
        <b-card no-body class="m-b-20 draggable" v-for="(section, subIndex) in sections" :key="'section-' + tabId + '-' + subIndex">
          <b-card-header>
              <div class="handle div-sezione-header" :id="'sezione-header-' + tabId + '-' + subIndex" v-b-toggle="'sezione-body-' + tabId + '-' + subIndex">
                <strong class="when-open">
                  <a class="section-collapse-link" href="javascript:void(0);">
                    <font-awesome-icon icon="minus" /> &nbsp; <span v-if="section.header != null">{{section.header.title}}</span>
                  </a>
                </strong> &nbsp;
                <strong class="when-closed">
                  <a class="section-collapse-link" href="javascript:void(0);">
                    <font-awesome-icon class="when-closed" icon="plus" /> &nbsp; <span v-if="section.header != null">{{section.header.title}}</span>
                  </a>
                </strong> &nbsp;
                <a href="javascript:void(0);" class="ml-2 shared-link text-muted" v-show="section.shared" v-b-tooltip.hover title="Sezione condivisa" @click.stop><font-awesome-icon icon="share-alt" /> {{section.elements}}</a> &nbsp;
                <span class="float-right">
                  <a href="javascript:void(0);" v-b-tooltip.hover title="Modifica" @click.stop><font-awesome-icon icon="pencil-alt" v-b-modal.modalSection @click="editSection(tabId, subIndex)" /></a> &nbsp;
                  <a href="javascript:void(0);" v-b-tooltip.hover title="Elimina" @click.stop><font-awesome-icon icon="times" @click="removeSection(subIndex)" /></a>
                </span>
              </div>
          </b-card-header>
          <b-collapse ref="collapse" :id="'sezione-body-' + tabId + '-' + subIndex" accordion="accordion" role="tabpanel" @shown="initFormBuilder(tabId, subIndex)" @hide="closeFormBuilder(subIndex)">
            <b-card-body>
              <form-builder ref="builder" :tabId="tabId" :sectionId="subIndex" :sectionInput="section.input" :sectionOutput="section.output" :schema="schema" :tabs="tabs" :sharedSections="sharedSections" @update-json-elements="updateJsonElements(arguments)"></form-builder>
            </b-card-body>
          </b-collapse>
        </b-card>
      </draggable>
    </div>
</template>

<script>
import FormBuilder from './builder/FormBuilder'
import draggable from 'vuedraggable'
export default {
  name: 'FormSection',
  components: {
    FormBuilder,
    draggable
  },
  props: {
    sections: null,
    tabs: null,
    tabId: null,
    sharedSections: null,
    activeSection: null,
    schema: null
  },
  data () {
    return {
      sectionIndex: null,
      formData: null
    }
  },
  methods: {
    collapseActiveSection () {
      if (this.activeSection != null) {
        var activeSectionSplit = this.activeSection.split('_')
        if (activeSectionSplit.length > 0) {
          var tabId = parseInt(activeSectionSplit[0])
          var sectionId = parseInt(activeSectionSplit[1])
          var collapseId = 'sezione-body-' + tabId + '-' + sectionId
          this.$root.$emit('bv::toggle::collapse', collapseId)
        }
      }
    },
    editSection (tabId, sectionId) {
      if (this.activeSection != null) {
        this.$emit('update-active-section-definition', this.activeSection)
      }
      this.$emit('edit-section', tabId, sectionId)
    },
    resetSectionIndex (sectionIndex) {
      this.$emit('reset-section-index', sectionIndex)
    },
    initFormBuilder (tabId, subIndex) {
      var section = tabId + '_' + subIndex
      this.$emit('update-active-section', section)
      var sectionElements = this.sections[subIndex].elements
      if (Array.isArray(sectionElements)) {
        this.formData = sectionElements
      } else {
        this.formData = this.sharedSections[sectionElements]
      }
      this.$refs.builder[subIndex].initFormBuilder(this.formData)
    },
    closeFormBuilder (subIndex) {
      this.$emit('update-active-section', null)
      this.$refs.builder[subIndex].closeFormBuilder()
    },
    updateJsonElements (args) {
      var tabId = args[0]
      var sectionId = args[1]
      var formData = args[2]
      this.$emit('update-json-elements', tabId, sectionId, formData)
    },
    removeSection (subIndex) {
      this.$bvModal.msgBoxConfirm('Sei sicuro di voler eliminare la sezione selezionata?', {
        title: 'Elimina Sezione',
        okTitle: 'Conferma',
        cancelTitle: 'Annulla',
        hideHeaderClose: false
      }).then(value => {
        if (value) {
          var section = this.sections[subIndex]
          if (section.shared) {
            if (!Array.isArray(section.elements)) {
              if (!this.$parent.isSharedKeyUsed(section.elements)) {
                this.$emit('remove-shared-section', section.elements)
              }
            }
          }
          this.collapseActiveSection()
          this.sections.splice(subIndex, 1)
        }
      }).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style>
  .div-sezione-header {
    cursor: pointer;
    outline: none;
  }

  .shared-link {
    cursor: default;
  }

  .section-collapse-link:hover,
  .shared-link:hover {
    text-decoration: none;
  }

  .collapsed > .when-open,
  .not-collapsed > .when-closed {
    display: none;
  }

  .form-group.input-wrap {
    width: 100%!important;
    margin-left: 0!important;
  }

  .form-wrap.form-builder .frmb .form-elements input[type='number'] {
    width: 100%!important;
  }

  .checkbox-field .option-actions {
    display: none;
  }

</style>
