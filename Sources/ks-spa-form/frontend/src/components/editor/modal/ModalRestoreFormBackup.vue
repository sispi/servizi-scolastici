<template>
    <b-modal id="modalRestoreFormBackup" ref="modalRestoreFormBackup" size="lg" title="Ripristina backup form" @show="loadBackups" @hidden="resetModal" @ok="handleOk">
        <div class="m-3">
          <b-table fixed bordered responsive :items="backups" :fields="fields" :select-mode="selectMode" selectable @row-selected="onRowSelected" show-empty thead-class="hidden_header">
            <template #cell(backup)="data">
              <div class="text-center">
                Ultima modifica effettuata da <em id="updatedBy">{{data.item.lastModifiedBy}}</em> in data <em id="updatedTs">{{data.item.lastModifiedTs | formatDate}}</em>.
              </div>
            </template>
            <template #empty>
              <div class="text-center text-muted mb-0">Nessun backup trovato</div>
            </template>
          </b-table>
        </div>
        <template #modal-footer="{ cancel, ok }">
          <b-button variant="secondary" @click="cancel()">Annulla</b-button>
          <b-button variant="primary" @click="ok()" :disabled="selected.length == 0">Ripristina</b-button>
        </template>
    </b-modal>
</template>

<script>
export default {
  name: 'ModalRestoreFormBackup',
  props: {
    formId: null
  },
  data () {
    return {
      perPage: 5,
      currentPage: 1,
      fields: [
        {
          key: 'backup'
        }
      ],
      selectMode: 'single',
      selected: [],
      backups: []
    }
  },
  methods: {
    onRowSelected (items) {
      this.selected = items
    },
    loadBackups () {
      try {
        setTimeout(() => {
          this.$axios.get(`/forms/${encodeURIComponent(this.formId)}/backups`).then(response => {
            this.backups = response.data
          })
        }, 300)
      } catch (error) {
        this.backups = []
      }
    },
    resetModal () {
      this.selected = []
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    async handleSubmit () {
      if (this.selected.length === 0) {
        this.$bvModal.msgBoxOk('Seleziona un backup da ripristinare', {
          title: 'Nessun backup selezionato'
        })
      } else {
        var backupIndex = this.selected[0].index
        await this.$axios.get(`/forms/${encodeURIComponent(this.formId)}/backups/${backupIndex}/definition`)
          .then(response => {
            this.$notify({
              type: 'success',
              title: 'Backup ripristinato con successo!'
            })

            if (response.data != null) {
              this.$emit('restore-backup-definition', response.data)
            }
            this.$nextTick(() => {
              this.$refs.modalRestoreFormBackup.hide('modalRestoreFormBackup')
            })
          })
      }
    }
  }
}
</script>
