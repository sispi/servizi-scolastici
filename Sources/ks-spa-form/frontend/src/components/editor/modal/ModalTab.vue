<template>
    <b-modal id="modalTab" ref="modalTab" title="Impostazioni Tab" @show="prepareModal" @hidden="resetModal" @ok="handleOk">
      <form ref="formTab" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Titolo" label-for="tabTitle" invalid-feedback="Campo obbligatorio." :state="titleState">
          <b-form-input id="tabTitle" v-model="tab.title" :state="titleState" placeholder="Inserisci il titolo del tab" required trim></b-form-input>
        </b-form-group>
        <b-form-checkbox id="tabSticky" v-model="tab.sticky" class="mb-3">Sticky</b-form-checkbox>
        <b-form-group label="Disabilitato" label-for="tabDisabled">
          <b-form-input id="tabDisabled" v-model="tab.disabled" placeholder="Inserisci la condizione per disabilitare il tab"></b-form-input>
        </b-form-group>
      </form>
      <template #modal-footer="{ cancel, ok }">
        <b-button variant="secondary" @click="cancel()">Annulla</b-button>
        <b-button variant="primary" @click="ok()">Conferma</b-button>
      </template>
    </b-modal>
</template>

<script>
export default {
  name: 'ModalTab',
  props: {
    tabs: {
      type: Array
    },
    tabIndex: null
  },
  data () {
    return {
      tab: {
        title: 'New Tab',
        sticky: false,
        disabled: 'false',
        sections: []
      },
      titleState: null
    }
  },
  methods: {
    checkFormValidity () {
      const valid = this.$refs.formTab.checkValidity()
      this.titleState = valid
      return valid
    },
    prepareModal () {
      if (this.tabIndex != null) {
        if (this.tabs != null && this.tabs.length > 0) {
          var tab = this.tabs[this.tabIndex]
          this.tab.title = tab.title
          this.tab.sticky = tab.sticky
          this.tab.disabled = tab.disabled
        }
      }
    },
    resetModal () {
      this.tab = {
        title: 'New Tab',
        sticky: false,
        disabled: 'false',
        sections: []
      }
      this.titleState = null
      this.$emit('reset-tab-index', null)
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    handleSubmit () {
      if (!this.checkFormValidity()) {
        return
      }
      if (this.tabIndex != null) {
        if (this.tabs != null && this.tabs.length > 0) {
          this.tabs[this.tabIndex].title = this.tab.title
          this.tabs[this.tabIndex].sticky = this.tab.sticky
          this.tabs[this.tabIndex].disabled = this.tab.disabled
        }
      } else {
        this.tabs.push(this.tab)
        this.$emit('update-active-tab', this.tabs.length - 1)
      }
      this.$nextTick(() => {
        this.$refs.modalTab.hide('modalTab')
      })
    }
  }
}
</script>
