<template>
    <b-modal id="modalCopyFormDefinition" ref="modalCopyFormDefinition" size="lg" title="Copia definizione form" @show="load" @hidden="resetModal" @ok="handleOk">
        <div class="m-3">
          <b-form-input autofocus type="search" placeholder="Cerca..." spellcheck="false" class="my-3" :value="filter" @input="onFilterChange" />
          <b-table bordered responsive show-empty no-local-sorting :fields="fields" :items="items" :select-mode="selectMode" selectable @row-selected="onRowSelected" @sort-changed="onSortChange">
            <template #cell(lastModifiedTs)="row">
              {{ row.item.lastModifiedTs | formatDate }}
            </template>
            <template #empty>
              <div class="text-center text-muted mb-0">Nessun form presente.</div>
            </template>
          </b-table>
          <b-pagination align="center" :value="pageNumber" :total-rows="count" :per-page="pageSize" @change="onPageChange" />
        </div>
        <template #modal-footer="{ cancel, ok }">
          <b-button variant="secondary" @click="cancel()">Annulla</b-button>
          <b-button variant="primary" @click="ok()" :disabled="selected.length == 0">Copia</b-button>
        </template>
    </b-modal>
</template>

<script>
import _ from 'lodash'

export default {
  name: 'ModalCopyFormDefinition',
  props: {
    formId: null
  },
  data () {
    return {
      fields: [
        {
          key: 'name',
          label: 'Nome',
          sortable: true,
          thStyle: { width: '50%' }
        },
        {
          key: 'lastModifiedBy',
          label: 'Autore',
          thStyle: { width: '20%' }
        },
        {
          key: 'lastModifiedTs',
          label: 'Ultima Modifica',
          sortable: true,
          thStyle: { width: '30%' }
        }
      ],
      filter: '',
      count: 0,
      pageNumber: 1,
      pageSize: 5,
      orderBy: 'name',
      items: [],
      selectMode: 'single',
      selected: []
    }
  },
  methods: {
    onRowSelected (items) {
      this.selected = items
    },
    load () {
      this.$axios
        .get(`/forms?name=${this.filter}&pageNumber=${this.pageNumber}&pageSize=${this.pageSize}&orderBy=${this.orderBy}`)
        .then(response => {
          this.count = response.data.count
          this.items = response.data.data
          var i = this.items.map(item => item.id).indexOf(this.formId)
          this.items.splice(i, 1)
          this.count -= 1
        })
    },
    resetModal () {
      this.pageNumber = 1
      this.count = 0
      this.filter = ''
      this.orderBy = 'name'
      this.selected = []
      this.items = []
    },
    onPageChange (page) {
      this.pageNumber = page
      this.load()
    },
    onFilterChange: _.debounce(function (filter) {
      this.pageNumber = 1
      this.filter = filter
      this.load()
    }, 500),
    onSortChange (sort) {
      this.pageNumber = 1
      if (sort.sortBy) {
        this.orderBy = sort.sortBy
        if (sort.sortDesc) {
          this.orderBy += ':desc'
        }
      } else {
        this.orderBy = 'name'
      }
      this.load()
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    async handleSubmit () {
      if (this.selected.length === 0) {
        this.$bvModal.msgBoxOk('Seleziona un form da copiare', {
          title: 'Nessun form selezionato'
        })
      } else {
        var formId = this.selected[0].id
        await this.$axios.get(`/forms/${encodeURIComponent(formId)}?fetch=definition`)
          .then(response => {
            this.$notify({
              type: 'success',
              title: 'Definizione copiata con successo!'
            })

            if (response.data != null && response.data.definition != null) {
              this.$emit('copy-form-definition', response.data.definition)
            }
            this.$nextTick(() => {
              this.$refs.modalCopyFormDefinition.hide('modalCopyFormDefinition')
            })
          })
      }
    },
    async created () {
      this.load()
    }
  }
}
</script>
