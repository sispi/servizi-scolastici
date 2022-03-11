<template>
    <b-modal id="modalButton" ref="modalButton" title="Impostazione Bottone" @show="prepareModal" @hidden="resetModal" @ok="handleOk">
      <form ref="formButton" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Azione" label-for="buttonAction">
          <b-form-select id="buttonAction" v-model="button.action" :options="actions"></b-form-select>
        </b-form-group>
        <b-form-group label="Nome Azione (Custom)" label-for="buttonCustomAction" v-show="button.action == 'custom'">
          <b-form-input id="buttonCustomAction" v-model="button.customAction" placeholder="Inserisci il nome dell'azione custom"></b-form-input>
        </b-form-group>
        <b-form-group label="URL Validazione" label-for="buttonValidationUrl">
          <b-form-input id="buttonValidationUrl" v-model="button.validationUrl" placeholder="Inserisci l'URL di validazione"></b-form-input>
        </b-form-group>
        <b-form-group label="URL Validazione Processor" label-for="buttonValidationUrlProcessor">
          <b-form-input id="buttonValidationUrlProcessor" v-model="button.validationUrlProcessor" placeholder="Inserisci la funzione di processo della validazione"></b-form-input>
        </b-form-group>
        <b-form-group label="URL Action" label-for="buttonActionUrl">
          <b-form-input id="buttonActionUrl" v-model="button.actionUrl" placeholder="Inserisci l'URL dell'azione"></b-form-input>
        </b-form-group>
        <b-form-group label="URL Action Processor" label-for="buttonActionUrlProcessor">
          <b-form-input id="buttonActionUrlProcessor" v-model="button.actionUrlProcessor" placeholder="Inserisci la funzione di processo dell'azione'"></b-form-input>
        </b-form-group>
        <b-form-group label="On Action" label-for="buttonOnAction">
          <b-form-input id="buttonOnAction" v-model="button.onAction" placeholder="Inserisci una funzione custom per la action"></b-form-input>
        </b-form-group>
        <b-form-group label="Condizione di Visibilità" label-for="buttonShow">
          <b-form-input id="buttonShow" v-model="button.show" placeholder="Inserisci la condizione di visibilità del bottone"></b-form-input>
        </b-form-group>
        <b-form-group label="Disabilitato" label-for="buttonDisabled">
          <b-form-input id="buttonDisabled" v-model="button.disabled" placeholder="Inserisci la condizione per disabilitare il bottone"></b-form-input>
        </b-form-group>
        <b-form-group label="Label" label-for="buttonLabel" invalid-feedback="Campo obbligatorio." :state="labelState">
          <b-form-input id="buttonLabel" ref="buttonLabel" v-model="button.label" :state="labelState" placeholder="Inserisci la label del bottone" required trim></b-form-input>
        </b-form-group>
        <b-form-row>
          <b-col>
            <b-form-group label="Margine" label-for="buttonMargin">
              <b-form-input id="buttonMargin" v-model="button.margin" placeholder="Margine del bottone" type="number"></b-form-input>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group label="Larghezza" label-for="buttonWidth">
              <b-form-input id="buttonWidth" v-model="button.width" placeholder="Larghezza del bottone" type="number"></b-form-input>
            </b-form-group>
          </b-col>
          <b-col class="m-auto text-center">
            <b-form-checkbox v-model="button.break">Break</b-form-checkbox>
          </b-col>
        </b-form-row>
        <b-form-group label="Classe" label-for="buttonClass">
          <b-form-input id="buttonClass" v-model="button.class" placeholder="Inserisci le classi CSS del bottone"></b-form-input>
        </b-form-group>
        <b-form-group label="Stile CSS" label-for="buttonStyle">
          <b-form-input id="buttonStyle" v-model="button.style" placeholder="Inserisci lo stile CSS del bottone"></b-form-input>
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
  name: 'ModalButton',
  props: {
    toolbar: {
      type: Object
    },
    buttonIndex: null
  },
  data () {
    return {
      button: {
        type: 'button',
        action: 'submit',
        customAction: '',
        actionUrl: '',
        actionUrlProcessor: '',
        validationUrl: '',
        validationUrlProcessor: '',
        onAction: '',
        show: '',
        disabled: '',
        label: 'Submit',
        margin: 0,
        width: 0,
        break: false,
        class: 'btn-primary',
        style: null
      },
      labelState: null,
      actions: [
        { text: 'Submit', value: 'submit' },
        { text: 'Save', value: 'save' },
        { text: 'Custom', value: 'custom' },
        { text: 'Validate', value: 'validate' },
        { text: 'Back', value: 'back' },
        { text: 'Next', value: 'next' },
        { text: 'Clear', value: 'clear' },
        { text: 'Cancel', value: 'cancel' }
      ]
    }
  },
  methods: {
    checkFormValidity () {
      const valid = this.$refs.formButton.checkValidity()
      this.labelState = this.$refs.buttonLabel.checkValidity()
      return valid
    },
    prepareModal () {
      if (this.buttonIndex != null && this.toolbar != null) {
        if (this.toolbar.buttons != null && this.toolbar.buttons.length > 0) {
          var button = this.toolbar.buttons[this.buttonIndex]
          this.button.type = button.type
          this.button.action = button.action
          this.button.customAction = button.customAction
          this.button.actionUrl = button.actionUrl
          this.button.actionUrlProcessor = button.actionUrlProcessor
          this.button.validationUrl = button.validationUrl
          this.button.validationUrlProcessor = button.validationUrlProcessor
          this.button.onAction = button.onAction
          this.button.show = button.show
          this.button.disabled = button.disabled
          this.button.label = button.label
          this.button.width = button.width
          this.button.margin = button.margin
          this.button.break = button.break
          this.button.class = button.class
          this.button.style = button.style
        }
      }
    },
    resetModal () {
      this.button = {
        type: 'button',
        action: 'submit',
        customAction: '',
        actionUrl: '',
        actionUrlProcessor: '',
        validationUrl: '',
        validationUrlProcessor: '',
        onAction: '',
        show: '',
        disabled: '',
        label: 'Submit',
        margin: 0,
        width: 0,
        break: false,
        class: 'btn-primary',
        style: null
      }
      this.labelState = null
      this.$emit('reset-button-index', null)
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    handleSubmit () {
      if (!this.checkFormValidity()) {
        return
      }
      if (this.button.action !== 'custom') {
        this.button.customAction = ''
      }
      if (this.buttonIndex != null && this.toolbar != null) {
        if (this.toolbar.buttons != null && this.toolbar.buttons.length > 0) {
          var button = this.toolbar.buttons[this.buttonIndex]
          button.type = this.button.type
          button.action = this.button.action
          button.customAction = this.button.customAction
          button.actionUrl = this.button.actionUrl
          button.actionUrlProcessor = this.button.actionUrlProcessor
          button.validationUrl = this.button.validationUrl
          button.validationUrlProcessor = this.button.validationUrlProcessor
          button.onAction = this.button.onAction
          button.show = this.button.show
          button.disabled = this.button.disabled
          button.label = this.button.label
          button.width = this.button.width
          button.margin = this.button.margin
          button.break = this.button.break
          button.class = this.button.class
        }
      } else {
        this.toolbar.buttons.push(this.button)
      }
      this.$nextTick(() => {
        this.$refs.modalButton.hide('modalButton')
      })
    }
  }
}
</script>
