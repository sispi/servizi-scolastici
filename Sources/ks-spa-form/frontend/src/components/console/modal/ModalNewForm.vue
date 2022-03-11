<template>
    <b-modal id="modalNewForm" ref="modalNewForm" size="lg" title="Nuovo Form" lazy static @hidden="resetModal" @ok="handleOk">
      <form ref="newForm" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Nome" label-for="formName" invalid-feedback="Campo obbligatorio." :state="nameState">
          <b-form-input id="formName" v-model="form.name" :state="nameState" placeholder="Inserisci il nome del form" required trim></b-form-input>
        </b-form-group>
        <b-form-group label="Schema" label-for="formSchema">
          <v-jsoneditor id="formSchema" ref="formSchema" v-model="form.schema" :options="options" :plus="false" height="400px" @error="jsonContentError" />
        </b-form-group>
      </form>
      <template #modal-footer="{ cancel, ok }">
        <b-button variant="secondary" @click="cancel()">Annulla</b-button>
        <b-button variant="primary" @click="ok()" :disabled="confirmDisabled">Conferma</b-button>
      </template>
    </b-modal>
</template>

<script>
import VJsoneditor from 'v-jsoneditor'
export default {
  name: 'ModalNewForm',
  components: {
    VJsoneditor
  },
  data () {
    return {
      form: {
        name: '',
        schema: {},
        definition: {
          wizard: false,
          header: null,
          footer: null,
          toolbar: {
            position: 'bottom',
            buttons: []
          },
          tabs: [],
          sections: {},
          script: null
        }
      },
      nameState: null,
      schemaState: null,
      options: {
        onValidate: this.jsonContentFix,
        mode: 'code'
      },
      confirmDisabled: false
    }
  },
  methods: {
    checkFormValidity () {
      const valid = this.$refs.newForm.checkValidity()
      this.nameState = valid
      return valid
    },
    jsonContentFix () {
      this.confirmDisabled = false
    },
    jsonContentError () {
      this.confirmDisabled = true
    },
    resetModal () {
      this.form.name = ''
      this.form.schema = {}
      this.nameState = null
      this.schemaState = null
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    async handleSubmit () {
      if (!this.checkFormValidity()) {
        return
      }

      await this.$axios.post('/forms/',
        this.form
      ).then((response) => {
        if (response.data != null) {
          this.$notify({
            type: 'success',
            title: 'Form creato con successo!'
          })
          window.open(`./editor/${response.data.id}`, '_blank')
          this.$nextTick(() => {
            this.$refs.modalNewForm.hide('modalNewForm')
            this.$emit('load-forms')
          })
        }
      })
    }
  }
}
</script>

<style>
  .modal-title,
  label {
    color: #212529;
  }
</style>
