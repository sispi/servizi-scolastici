<template>
    <b-modal id="modalFormSettings" ref="modalFormSettings" size="lg" title="Impostazioni Form" lazy static @show="prepareModal" @hidden="resetModal" @ok="handleOk">
      <form ref="formSettings" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Nome" label-for="formName" invalid-feedback="Campo obbligatorio." :state="nameState">
          <b-form-input id="formName" ref="formName" v-model="formName" :state="nameState" placeholder="Inserisci il nome del form" required trim></b-form-input>
        </b-form-group>
        <b-form-group label="Schema" label-for="formSchema">
          <v-jsoneditor id="formSchema" ref="formSchema" v-model="formSchema" :options="options" :plus="false" height="400px" @error="jsonContentError" />
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
  name: 'ModalFormSettings',
  components: {
    VJsoneditor
  },
  props: {
    form: {
      required: true
    }
  },
  data () {
    return {
      formName: '',
      formSchema: '',
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
      const valid = this.$refs.formSettings.checkValidity()
      this.nameState = this.$refs.formName.checkValidity()
      return valid
    },
    jsonContentFix () {
      this.confirmDisabled = false
    },
    jsonContentError () {
      this.confirmDisabled = true
    },
    prepareModal () {
      this.formName = this.form.name
      this.formSchema = this.form.schema
      this.nameState = null
      this.schemaState = null
    },
    resetModal () {
      this.formName = ''
      this.formSchema = ''
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

      var jsonUpdateForm = {
        name: this.formName,
        schema: this.formSchema
      }

      await this.$axios.put(`/forms/${encodeURIComponent(this.form.id)}`,
        jsonUpdateForm
      ).then((response) => {
        if (response.data != null) {
          this.$notify({
            type: 'success',
            title: 'Form modificato con successo!'
          })
          this.form.name = this.formName
          this.form.schema = this.formSchema
          this.$emit('reload-form', response.data)
          this.$nextTick(() => {
            this.$refs.modalFormSettings.hide('modalFormSettings')
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
