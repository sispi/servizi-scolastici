<template>
    <div class="toolbar m-b-20">
        <div class="row ml-0 mr-0 p-2">
            <div class="col-md-8 text-left">
                <h2 class="mb-0">{{form.name}}</h2>
                <small class="mb-0">
                  Ultima modifica effettuata da <em id="updatedBy">{{form.lastModifiedBy}}</em> in data <em id="updatedTs">{{form.lastModifiedTs | formatDate}}</em>.
                </small>
            </div>
            <div class="col-md-4 m-auto">
                <div class="float-right">
                    <a @click="updateForm" class="header-link p-r-10" v-b-tooltip.hover title="Salva">
                        <font-awesome-icon class="f-s-24" icon="save" />
                    </a>
                    <router-link :to="{path: '/preview/' + encodeURIComponent(form.id)}" target="_blank" id="previewFormButton" class="header-link p-r-10" v-b-tooltip.hover title="Anteprima">
                        <font-awesome-icon class="f-s-24" icon="eye" />
                    </router-link>
                    <a class="header-link p-r-10" v-b-tooltip.hover title="Copia definizione">
                        <font-awesome-icon class="f-s-24" icon="copy" v-b-modal.modalCopyFormDefinition />
                    </a>
                    <a class="header-link p-r-10" v-b-tooltip.hover title="Ripristina definizione">
                        <font-awesome-icon class="f-s-24" icon="history" v-b-modal.modalRestoreFormBackup />
                    </a>
                    <a  class="header-link p-r-10" v-b-tooltip.hover title="Impostazioni">
                        <font-awesome-icon class="f-s-24" icon="cog" v-b-modal.modalFormSettings />
                    </a>
                </div>
            </div>
        </div>
        <modal-copy-form-definition :formId="form.id" @copy-form-definition="copyFormDefinition" />
        <modal-restore-form-backup :formId="form.id" @restore-backup-definition="restoreBackupDefinition" />
        <modal-form-settings :form="form" @reload-form="reloadForm" />
    </div>
</template>

<script>
import ModalCopyFormDefinition from './modal/ModalCopyFormDefinition.vue'
import ModalRestoreFormBackup from './modal/ModalRestoreFormBackup.vue'
import ModalFormSettings from './modal/ModalFormSettings.vue'

export default {
  name: 'Toolbar',
  components: {
    ModalCopyFormDefinition,
    ModalRestoreFormBackup,
    ModalFormSettings
  },
  props: {
    form: {
      type: Object,
      required: true
    },
    activeSection: null
  },
  data () {
    return {
      sharedKeys: [],
      jsonUpdateForm: null
    }
  },
  methods: {
    reloadForm (form) {
      this.form.lastModifiedBy = form.lastModifiedBy
      this.form.lastModifiedTs = form.lastModifiedTs
    },
    async updateForm () {
      if (Object.keys(this.form.definition.sections).length > 0) {
        if (JSON.stringify(Object.keys(this.form.definition.sections)) !== JSON.stringify(this.sharedKeys)) {
          var alertSharedSections = ''
          for (var k in this.form.definition.sections) {
            alertSharedSections += '- <i>' + k + '</i><br>'
          }
          const h = this.$createElement
          const messageVNode = h('div', { class: ['foobar'] }, [
            h('h3', { class: ['text-center'] }, ' ATTENZIONE! '),
            h('p', { domProps: { innerHTML: 'Stai modificando delle <b>sezioni condivise</b>.' } }),
            h('p', { domProps: { innerHTML: 'Salvando le modifiche potresti <b><u>modificare altri form presenti nel sistema</u></b>.' } }),
            h('p', 'Le chiavi di condivisione presenti nel form sono:'),
            h('p', { domProps: { innerHTML: alertSharedSections } }),
            h('p', 'Sei sicuro di voler procedere?')
          ])
          /* var alertSharedSections = 'ATTENZIONE!<br>' +
                                  'Stai modificando delle sezioni condivise.<br>' +
                                  'Salvando le modifiche potresti modificare altri form presenti sul sistema.<br>' +
                                  'Le chiavi di condivisione presenti nel form sono:<br>'
          alertSharedSections += 'Sei sicuro di voler procedere?' */
          this.$bvModal.msgBoxConfirm([messageVNode], {
            title: 'Salvataggio Form con Sezioni Condivise',
            size: 'lg',
            okTitle: 'Conferma',
            cancelTitle: 'Annulla',
            hideHeaderClose: false
          }).then(value => {
            if (value) {
              this.sharedKeys = Object.keys(this.form.definition.sections)

              this.callUpdateMethod()
            }
          }).catch(err => {
            console.log(err)
          })
        } else {
          this.callUpdateMethod()
        }
      } else {
        this.callUpdateMethod()
      }
    },
    callUpdateMethod () {
      if (this.activeSection != null) {
        this.$emit('update-active-section-definition', this.activeSection)
      }

      this.jsonUpdateForm = {
        definition: this.form.definition
      }

      this.$axios.put(`/forms/${encodeURIComponent(this.form.id)}`,
        this.jsonUpdateForm
      ).then((response) => {
        if (response.data != null) {
          this.$notify({
            type: 'success',
            title: 'Form salvato'
          })
          this.form.lastModifiedBy = response.data.lastModifiedBy
          this.form.lastModifiedTs = response.data.lastModifiedTs
        }
      })
    },
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
    restoreBackupDefinition (backupDefinition) {
      this.collapseActiveSection()
      if (backupDefinition != null) {
        setTimeout(() => {
          this.form.definition = backupDefinition
        }, 300)
      }
    },
    copyFormDefinition (formDefinition) {
      this.collapseActiveSection()
      if (formDefinition != null) {
        setTimeout(() => {
          this.form.definition = formDefinition
        }, 300)
      }
    }
  }
}
</script>

<style scoped>
    .toolbar {
      background-color: #0970b8;
      color: white
    }

    .p-r-10 {
      padding-right: 10px;
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
<style>
  .hidden_header {
    display: none;
  }

  .table.b-table > tbody > .table-active,
  .table.b-table > tbody > .table-active > th,
  .table.b-table > tbody > .table-active > td {
    background-color: #0970b8;
    color: white;
  }
</style>
