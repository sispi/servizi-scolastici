<template>
    <div class="m-3">
      <b-form-input autofocus type="search" placeholder="Cerca..." spellcheck="false" class="my-3" :value="filter" @input="onFilterChange" />
      <b-table bordered hover responsive show-empty no-local-sorting :fields="fields" :items="items" @sort-changed="onSortChange" @row-clicked="onRowClick">
        <template #cell(actions)="row">
          <router-link :to="{path: '/editor/' + encodeURIComponent(row.item.id)}" target="_blank" class="header-link p-r-10" v-b-tooltip.hover title="Modifica">
              <font-awesome-icon icon="edit" />
          </router-link>
          <router-link :to="{path: '/preview/' + encodeURIComponent(row.item.id)}" target="_blank" class="header-link p-r-10" v-b-tooltip.hover title="Anteprima">
              <font-awesome-icon icon="eye" />
          </router-link>
          <a class="header-link p-r-10" v-b-tooltip.hover title="Elimina">
              <font-awesome-icon icon="times" @click="remove(row.item.id, row.item.name)" />
          </a>
        </template>
        <template #cell(lastModifiedTs)="row">
          {{ row.item.lastModifiedTs | formatDate }}
        </template>
        <template #empty>
          <div class="text-center text-muted mb-0">Nessun form presente.</div>
        </template>
      </b-table>
      <b-pagination align="center" :value="pageNumber" :total-rows="count" :per-page="pageSize" @change="onPageChange" />
    </div>
</template>

<script>
import _ from 'lodash'

export default {
  name: 'FormList',
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
          thStyle: { width: '30%' }
        },
        {
          key: 'lastModifiedTs',
          label: 'Ultima Modifica',
          sortable: true,
          thStyle: { width: '10%' }
        },
        {
          key: 'actions',
          label: '',
          thStyle: { width: '10%' }
        }
      ],
      filter: '',
      count: 0,
      pageNumber: 1,
      pageSize: 20,
      orderBy: 'name',
      items: []
    }
  },
  methods: {
    load () {
      this.$axios
        .get(`/forms?name=${this.filter}&pageNumber=${this.pageNumber}&pageSize=${this.pageSize}&orderBy=${this.orderBy}`)
        .then(response => {
          this.count = response.data.count
          this.items = response.data.data
        })
    },
    remove (formId, name) {
      if (confirm(`Sei sicuro di voler eliminare il form '${name}'?`)) {
        this.$axios
          .delete(`/forms/${encodeURIComponent(formId)}`, { data: {} })
          .then(response => {
            this.$notify({
              type: 'success',
              title: 'Form eliminato'
            })
            this.pageNumber = 1
            this.load()
          })
      }
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
    onRowClick (item) {
      window.open(`./editor/${encodeURIComponent(item.id)}`, '_blank')
    }
  },
  async created () {
    this.load()
  }
}
</script>
<style>
  .page-link {
    color: #0970b8;
  }
  .page-item.active .page-link {
    background-color: #0970b8;
    border-color: #0970b8;
  }
  [role=row] {
    cursor:pointer
  }
</style>
