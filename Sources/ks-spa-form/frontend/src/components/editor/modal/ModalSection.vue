<template>
    <b-modal id="modalSection" ref="modalSection" title="Impostazioni Sezione" @shown="initSelect2" @show="prepareModal" @hidden="resetModal" @ok="handleOk">
      <form ref="formSection" @submit.stop.prevent="handleSubmit">
        <b-form-group label="Tipo" label-for="sectionType">
          <b-form-select id="sectionType" v-model="section.type" :options="types" @change="updateSectionType"></b-form-select>
        </b-form-group>
        <b-form-group label="Input Binding" label-for="inputBinding">
          <b-input-group>
            <select class="form-control" id="inputBinding" v-model="section.input">
              <option value=""></option>
            </select>
            <b-input-group-append>
              <b-button class="btn-binding-filter" v-if="toggleInputFilter" @click="toggleInput" v-b-tooltip.hover title="Filtro binding attivato">
                <font-awesome-icon icon="filter" />
              </b-button>
              <b-button class="btn-binding-filter" v-if="!toggleInputFilter" @click="toggleInput" v-b-tooltip.hover title="Filtro binding disattivato">
                <font-awesome-icon icon="asterisk" />
              </b-button>
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
        <b-form-group label="Ouput Binding" label-for="outputBinding">
          <b-input-group>
            <select class="form-control" id="outputBinding" v-model="section.output">
              <option value=""></option>
            </select>
            <b-input-group-append>
              <b-button class="btn-binding-filter" v-if="toggleOutputFilter" @click="toggleOutput" v-b-tooltip.hover title="Filtro binding attivato">
                <font-awesome-icon icon="filter" />
              </b-button>
              <b-button class="btn-binding-filter" v-if="!toggleOutputFilter" @click="toggleOutput" v-b-tooltip.hover title="Filtro binding disattivato">
                <font-awesome-icon icon="asterisk" />
              </b-button>
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
        <b-form-group label="Visibilità" label-for="sectionShow">
          <b-form-input id="sectionShow" v-model="section.show" placeholder="Inserisci la condizione di visibilità della sezione"></b-form-input>
        </b-form-group>
        <b-form-group label="Classe" label-for="sectionClass">
          <b-form-input id="sectionClass" v-model="section.class" placeholder="Inserisci le classi CSS della sezione"></b-form-input>
        </b-form-group>
        <b-form-group label="Stile CSS" label-for="sectionStyle">
          <b-form-input id="sectionStyle" v-model="section.style" placeholder="Inserisci lo stile CSS della sezione"></b-form-input>
        </b-form-group>
        <hr>
        <b-form-checkbox v-model="headerCheck" :class="headerCheck ? null : 'collapsed'" :aria-expanded="headerCheck ? 'true' : 'false'" @change="updateHeader">Header</b-form-checkbox>
        <b-collapse id="collapseHeader" :visible="section.header != null" class="mt-2">
          <b-form-group v-if="section.header != null" label="Titolo" label-for="sectionTitle" invalid-feedback="Campo obbligatorio." :state="titleState">
            <b-form-input id="sectionTitle" ref="sectionTitle" v-model="section.header.title" :state="titleState" placeholder="Inserisci il titolo della sezione" :required="headerCheck" trim></b-form-input>
          </b-form-group>
          <b-form-checkbox v-if="section.header != null" v-model="section.header.collapsible" class="mb-3">Collassabile</b-form-checkbox>
          <b-form-group v-if="section.header != null" label="Classe Header" label-for="sectionHeaderClass">
            <b-form-input id="sectionHeaderClass" v-model="section.header.class" placeholder="Inserisci le classi CSS dell'header"></b-form-input>
          </b-form-group>
          <b-form-group v-if="section.header != null" label="Stile CSS Header" label-for="sectionHeaderStyle">
            <b-form-input id="sectionHeaderStyle" v-model="section.header.style" placeholder="Inserisci lo stile CSS dell'header"></b-form-input>
          </b-form-group>
        </b-collapse>
        <hr>
        <b-form-checkbox v-model="repeatableCheck" :class="repeatableCheck ? null : 'collapsed'" :aria-expanded="repeatableCheck ? 'true' : 'false'" @change="updateRepeatable" disabled>Ripetibile</b-form-checkbox>
        <b-collapse id="collapseRepeatable" :visible="section.repeatable != null" class="mt-2">
          <b-form-group v-if="section.repeatable != null" label="Label" label-for="repeatableLabel" invalid-feedback="Campo obbligatorio." :state="labelState">
            <b-form-input id="repeatableLabel" ref="repeatableLabel" v-model="section.repeatable.addLabel" :state="labelState" placeholder="Inserisci la label del bottone di aggiunta item" :required="repeatableCheck" trim></b-form-input>
          </b-form-group>
          <b-form-row v-if="section.repeatable != null">
            <b-col>
              <b-form-group label="Min" label-for="minItem">
                <b-form-input id="minItem" min="0" v-model="section.repeatable.min" placeholder="Numero minimo di item" type="number"></b-form-input>
              </b-form-group>
            </b-col>
            <b-col>
              <b-form-group label="Max" label-for="maxItem">
                <b-form-input id="maxItem" min="0" v-model="section.repeatable.max" placeholder="Numero massimo di item" type="number"></b-form-input>
              </b-form-group>
            </b-col>
          </b-form-row>
        </b-collapse>
        <hr>
        <b-form-checkbox v-model="sharedCheck" :class="sharedCheck ? null : 'collapsed'" :aria-expanded="sharedCheck ? 'true' : 'false'" @change="updateShared">Shared</b-form-checkbox>
        <b-collapse id="collapseShared" :visible="section.shared != null" class="mt-2">
          <b-form-group v-if="section.shared != null" label="Key" label-for="sharedKey" invalid-feedback="Campo obbligatorio." :state="keyState">
            <vue-bootstrap-typeahead id="sharedKey" ref="sharedKey" v-model="query" :data="keys" :serializer="item => item.key" @hit="selectedKey = $event" @input="updateSharedKey" :state="keyState" placeholder="Inserisci la chiave di condivisione" :required="sharedCheck" trim />
            <!-- <b-form-input id="sharedKey" ref="sharedKey" v-model="section.shared.key" :state="keyState" placeholder="Inserisci la chiave di condivisione" :required="sharedCheck" trim></b-form-input> -->
          </b-form-group>
        </b-collapse>
      </form>
      <template #modal-footer="{ cancel, ok }">
        <b-button variant="secondary" @click="cancel()">Annulla</b-button>
        <b-button variant="primary" @click="ok()">Conferma</b-button>
      </template>
    </b-modal>
</template>

<script>
import Utils from '@/mixins/utils.js'
import VueBootstrapTypeahead from 'vue-bootstrap-typeahead'
import $ from 'jquery'
window.jQuery = $
window.$ = $
require('select2/dist/css/select2.min.css')
require('select2-bootstrap-theme/dist/select2-bootstrap.css')
require('select2')
require('select2/dist/js/i18n/it')
export default {
  name: 'ModalSection',
  mixins: [Utils],
  components: {
    VueBootstrapTypeahead
  },
  props: {
    tabs: {
      type: Array
    },
    tabIndex: null,
    sectionIndex: null,
    sharedSections: null,
    schema: null,
    activeSection: null
  },
  data () {
    return {
      section: {
        type: 'single',
        input: '',
        output: '',
        show: 'true',
        class: null,
        style: null,
        header: null,
        repeatable: null,
        shared: null,
        elements: []
      },
      headerCheck: false,
      repeatableCheck: false,
      sharedCheck: false,
      titleState: null,
      labelState: null,
      keyState: null,
      inputBindings: [],
      outputBindings: [],
      sharedElements: null,
      sharedKeyUsed: false,
      sharedKey: null,
      query: '',
      selectedKey: null,
      keys: [],
      toggleInputFilter: true,
      toggleOutputFilter: true,
      types: [
        { text: 'Singola', value: 'single' },
        { text: 'Multipla', value: 'multiple' },
        { text: 'Ripetibile', value: 'repeatable' }
      ]
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
    updateSectionType () {
      if (this.section.type === 'single') {
        this.repeatableCheck = false
        this.updateRepeatable()
      } else if (this.section.type === 'multiple') {
        this.repeatableCheck = false
        this.updateRepeatable()
      } else if (this.section.type === 'repeatable') {
        this.repeatableCheck = true
        this.updateRepeatable()
      }
      $('#inputBinding').val(null).trigger('change')
      $('#inputBinding').select2('destroy')
      $('#inputBinding').empty()
      $('#inputBinding').append('<option value=""></option>')
      $('#outputBinding').val(null).trigger('change')
      $('#outputBinding').select2('destroy')
      $('#outputBinding').empty()
      $('#outputBinding').append('<option value=""></option>')
      this.toggleInputFilter = true
      this.toggleOutputFilter = true
      this.initSelect2()
    },
    updateSharedKey () {
      if (this.section.shared != null) {
        this.section.shared.key = this.query
      }
      this.selectedKey = null
    },
    updateHeader () {
      if (this.section != null) {
        if (this.headerCheck) {
          this.section.header = {
            title: '',
            collapsible: false,
            class: null,
            style: null
          }
        } else {
          this.section.header = null
        }
      }
      this.titleState = null
    },
    updateRepeatable () {
      if (this.section != null) {
        if (this.repeatableCheck) {
          this.section.repeatable = {
            addLabel: '',
            min: 0,
            max: null
          }
        } else {
          this.section.repeatable = null
        }
      }
      this.labelState = null
    },
    updateShared () {
      if (this.section != null) {
        if (this.sharedCheck) {
          this.section.shared = {
            key: ''
          }
        } else {
          this.section.shared = null
        }
      }
      this.keyState = null
    },
    checkFormValidity () {
      let valid = this.$refs.formSection.checkValidity()
      if (this.$refs.sectionTitle != null) {
        this.titleState = this.$refs.sectionTitle.checkValidity()
      }
      if (this.$refs.repeatableLabel != null) {
        this.labelState = this.$refs.repeatableLabel.checkValidity()
      }
      if (this.$refs.sharedKey != null) {
        if (this.$refs.sharedKey.$refs.input.value !== '') {
          this.keyState = true
        } else {
          valid = false
          this.keyState = false
          this.$refs.sharedKey.$refs.input.classList.add('is-invalid')
        }
      }
      return valid
    },
    initSelect2 () {
      var vm = this
      var formatsInput = null
      if (this.toggleInputFilter) {
        if (this.section.type === 'single') {
          formatsInput = 'object'
        } else {
          formatsInput = 'array<object>'
        }
      }
      $('#inputBinding').closest('.modal-content').removeAttr('tabindex') // Fix ricerca select2 su modale non funzionante
      $('#inputBinding').select2({
        placeholder: 'Seleziona il binding di input',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: this.getPathHintsPlus(this.getPathHints(this.schema, true, null, formatsInput), 'input'),
        insertTag: function (data, tag) {
          // Insert the tag at the end of the results
          data.push(tag)
        },
        createTag: function (params) {
          // Non creare il tag se non risulta essere un path valido
          if (!vm.isPath(params.term)) {
            // Restituisce null per disabilitare la creazione del tag
            return null
          }

          return {
            id: params.term,
            text: params.term
          }
        },
        language: 'it'
      }).on('change', function () {
        vm.section.input = this.value
      })

      if (vm.section.input !== '') {
        $('#inputBinding').val(vm.section.input).trigger('change')
      }
      var formatsOutput = null
      if (this.toggleOutputFilter) {
        if (this.section.type === 'single') {
          formatsOutput = 'object'
        } else {
          formatsOutput = 'array<object>'
        }
      }
      $('#outputBinding').select2({
        placeholder: 'Seleziona il binding di output',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: this.getPathHintsPlus(this.getPathHints(this.schema, false, null, formatsOutput), 'output'),
        insertTag: function (data, tag) {
          // Insert the tag at the end of the results
          data.push(tag)
        },
        createTag: function (params) {
          // Non creare il tag se non risulta essere un path valido
          if (!vm.isPath(params.term)) {
            // Restituisce null per disabilitare la creazione del tag
            return null
          }

          return {
            id: params.term,
            text: params.term
          }
        },
        language: 'it'
      }).on('change', function () {
        vm.section.output = this.value
      })

      if (vm.section.output !== '') {
        $('#outputBinding').val(vm.section.output).trigger('change')
      }
    },
    getPathHintsPlus (hints, inputOutput) {
      var hintKeys = []
      hints.forEach(binding => {
        hintKeys.push(binding.id)
      })
      if (inputOutput === 'input') {
        if (this.section.input !== '' && !hintKeys.includes(this.section.input)) {
          hints.push(this.section.input)
        }
      } else if (inputOutput === 'output') {
        if (this.section.output !== '' && !hintKeys.includes(this.section.output)) {
          hints.push(this.section.output)
        }
      }

      return hints
    },
    prepareModal () {
      if (this.tabIndex != null) {
        if (this.tabs != null && this.tabs.length > 0) {
          var tab = this.tabs[this.tabIndex]
          if (this.sectionIndex != null) {
            var section = tab.sections[this.sectionIndex]
            this.section.type = section.type
            this.section.input = section.input
            var formatsInput = null
            if (this.section.type === 'single') {
              formatsInput = 'object'
            } else {
              formatsInput = 'array<object>'
            }
            var inputBindings = this.getPathHints(this.schema, true, null, formatsInput)
            var inputKeys = []
            if (inputBindings.length > 0) {
              inputBindings.forEach(binding => {
                inputKeys.push(binding.id)
              })
            }
            if (section.input === '' || inputKeys.includes(section.input)) {
              this.toggleInputFilter = true
            } else {
              this.toggleInputFilter = false
            }
            this.section.output = section.output
            var outputBindings = this.getPathHints(this.schema, false, null, formatsInput)
            var outputKeys = []
            if (outputBindings.length > 0) {
              outputBindings.forEach(binding => {
                outputKeys.push(binding.id)
              })
            }
            if (section.output === '' || outputKeys.includes(section.output)) {
              this.toggleOutputFilter = true
            } else {
              this.toggleOutputFilter = false
            }
            this.section.show = section.show
            this.section.class = section.class
            this.section.style = section.style
            if (section.header != null) {
              this.headerCheck = true
              this.section.header = {
                title: section.header.title,
                collapsible: section.header.collapsible,
                class: section.header.class,
                style: section.header.style
              }
            } else {
              this.headerCheck = false
              this.section.header = null
            }
            if (section.repeatable != null) {
              this.repeatableCheck = true
              this.section.repeatable = {
                addLabel: section.repeatable.addLabel,
                min: section.repeatable.min,
                max: section.repeatable.max
              }
            } else {
              this.repeatableCheck = false
              this.section.repeatable = null
            }
            if (section.shared) {
              this.sharedCheck = true
              this.section.shared = {
                key: section.elements
              }
              setTimeout(() => {
                this.$refs.sharedKey.inputValue = section.elements
              }, 300)
            } else {
              this.sharedCheck = false
              this.section.shared = null
            }
            this.section.elements = section.elements
          }
        }
      }
    },
    resetModal () {
      this.section = {
        type: 'single',
        input: '',
        output: '',
        show: 'true',
        class: null,
        style: null,
        header: null,
        repeatable: null,
        shared: null,
        elements: []
      }
      this.headerCheck = false
      this.repeatableCheck = false
      this.sharedCheck = false
      this.titleState = null
      this.labelState = null
      this.keyState = null
      this.sharedElements = null
      this.sharedKeyUsed = false
      this.sharedKey = null
      this.query = ''
      this.selectedKey = null
      this.keys = []
      this.$emit('reset-tab-section-index', null)
      $('#inputBinding').select2('destroy')
      $('#outputBinding').select2('destroy')
      this.toggleInputFilter = true
      this.toggleOutputFilter = true
      this.collapseActiveSection()
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
          var tab = this.tabs[this.tabIndex]
          if (this.sectionIndex != null) {
            var section = tab.sections[this.sectionIndex]
            section.type = this.section.type
            section.input = this.section.input
            section.output = this.section.output
            section.show = this.section.show
            section.class = this.section.class
            section.style = this.section.style
            if (this.section.header != null) {
              section.header = {
                title: this.section.header.title,
                collapsible: this.section.header.collapsible,
                class: this.section.header.class,
                style: this.section.header.style
              }
            } else {
              section.header = null
            }
            if (this.section.repeatable != null) {
              section.repeatable = {
                addLabel: this.section.repeatable.addLabel,
                min: this.section.repeatable.min,
                max: this.section.repeatable.max
              }
            } else {
              section.repeatable = null
            }
            if (this.section.shared != null) {
              this.sharedKey = this.section.shared.key
              if (!section.shared && Array.isArray(section.elements)) { // Caso in cui la seziona non era condivisa
                if (this.selectedKey != null) { // Caso in cui ho selezionato un risultato della ricerca delle chiavi condivise
                  const h = this.$createElement
                  const messageVNode = h('div', { class: ['foobar'] }, [
                    h('h3', { class: ['text-center'] }, ' ATTENZIONE! '),
                    h('p', { domProps: { innerHTML: 'Stai importando una <b>sezione condivisa</b>,' } }),
                    h('p', 'i controlli attualmente definiti andranno persi.'),
                    h('p', 'Sei sicuro di voler procedere?')
                  ])
                  if (this.sharedSections[this.sharedKey] == null) { // Se la chiave non risulta presente in locale...
                    if (section.elements.length > 0) { // Se la sezione ha attualmente degli elements...
                      this.$bvModal.msgBoxConfirm([messageVNode], { // Avverto l'utente che l'import sovrascriverà gli elements
                        title: 'Import Sezione Condivisa',
                        size: 'lg',
                        okTitle: 'Conferma',
                        cancelTitle: 'Annulla',
                        hideHeaderClose: false
                      }).then(value => {
                        if (value) {
                          // Se la chiave di condivisione selezionata non risulta presente in locale chiamo le API
                          this.$axios.get(`/sections/${this.sharedKey}/value`)
                            .then((res) => {
                              this.$emit('update-shared-section', this.sharedKey, res.data)
                              this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, res.data)

                              this.$nextTick(() => {
                                setTimeout(() => {
                                  this.$refs.modalSection.hide('modalSection')
                                }, 300)
                              })
                            })
                        }
                      }).catch(err => {
                        console.log(err)
                      })
                    } else { // Se la sezione non ha elements...
                      // Se la chiave di condivisione selezionata non risulta presente in locale chiamo le API
                      this.$axios.get(`/sections/${this.sharedKey}/value`)
                        .then((res) => {
                          this.$emit('update-shared-section', this.sharedKey, res.data)
                          this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, res.data)

                          this.$nextTick(() => {
                            setTimeout(() => {
                              this.$refs.modalSection.hide('modalSection')
                            }, 300)
                          })
                        })
                    }
                  } else { // Se la chiave risulta presente in locale...
                    if (section.elements.length > 0) { // Se la sezione ha attualmente degli elements...
                      this.$bvModal.msgBoxConfirm([messageVNode], { // Avverto l'utente che l'import sovrascriverà gli elements
                        title: 'Import Sezione Condivisa',
                        size: 'lg',
                        okTitle: 'Conferma',
                        cancelTitle: 'Annulla',
                        hideHeaderClose: false
                      }).then(value => {
                        if (value) {
                          // Se la chiave di condivisione selezionata non risulta presente in locale chiamo le API
                          this.$axios.get(`/sections/${this.sharedKey}/value`)
                            .then((res) => {
                              this.sectionElements = this.sharedSections[this.sharedKey]
                              this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, this.sectionElements)

                              this.$nextTick(() => {
                                setTimeout(() => {
                                  this.$refs.modalSection.hide('modalSection')
                                }, 300)
                              })
                            })
                        }
                      }).catch(err => {
                        console.log(err)
                      })
                    } else { // Se la sezione non ha elements...
                      this.sectionElements = this.sharedSections[this.sharedKey]
                      this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, this.sectionElements)

                      this.$nextTick(() => {
                        setTimeout(() => {
                          this.$refs.modalSection.hide('modalSection')
                        }, 300)
                      })
                    }
                  }
                } else { // Caso in cui non ho selezionato un risultato della ricerca delle chiavi condivise
                  if (this.sharedSections[this.sharedKey] == null) {
                    // Creo una nuova chiave di condivisione
                    this.sectionElements = this.section.elements
                    this.$emit('update-shared-section', this.sharedKey, this.sectionElements)
                  }

                  this.$nextTick(() => {
                    setTimeout(() => {
                      this.$refs.modalSection.hide('modalSection')
                    }, 300)
                  })
                }
              } else if (section.shared && !Array.isArray(section.elements)) { // Caso in cui la sezione era già condivisa
                if (this.selectedKey != null) { // Caso in cui ho selezionato un risultato della ricerca delle chiavi condivise
                  if (this.sharedSections[this.sharedKey] == null) {
                    // Se la chiave di condivisione selezionata non risulta presente in locale chiamo le API
                    this.$axios.get(`/sections/${this.sharedKey}/value`)
                      .then((res) => {
                        this.$emit('update-shared-section', this.sharedKey, res.data)
                        this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, res.data)
                      })
                  } else { // Se la chiave risulta presente in locale...
                    this.sectionElements = this.sharedSections[this.sharedKey]
                    this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, this.sectionElements)
                  }
                } else { // Caso in cui non ho selezionato un risultato della ricerca delle chiavi condivise
                  if (this.sharedSections[this.sharedKey] == null) {
                    // Creo una nuova chiave di condivisione
                    this.sectionElements = this.sharedSections[section.elements]
                    this.$emit('update-shared-section', this.sharedKey, this.sectionElements)
                  } else {
                    this.sectionElements = this.sharedSections[this.sharedKey]
                    this.$emit('update-form-builder', this.tabIndex, this.sectionIndex, this.sectionElements)
                  }
                }
                // Controllo se la chiave di condivisione è stata utilizzata altre volte nel form
                if (this.section.shared.key !== section.elements) { // Controllo se la chiave di condivisione è stata cambiata
                  this.sharedKeyUsed = this.$parent.isSharedKeyUsed(section.elements)
                  if (!this.sharedKeyUsed) {
                    this.$emit('remove-shared-section', section.elements)
                  }
                }

                this.$nextTick(() => {
                  setTimeout(() => {
                    this.$refs.modalSection.hide('modalSection')
                  }, 300)
                })
              }
              section.shared = true
              section.elements = this.sharedKey
            } else {
              if (section.shared && !Array.isArray(section.elements)) {
                this.sharedElements = this.sharedSections[section.elements]
                // Controllo se la chiave di condivisione è stata utilizzata altre volte nel form
                this.sharedKeyUsed = this.$parent.isSharedKeyUsed(section.elements)
                if (!this.sharedKeyUsed) {
                  this.$emit('remove-shared-section', section.elements)
                }
                section.elements = this.sharedElements
              }
              section.shared = false
              this.$nextTick(() => {
                setTimeout(() => {
                  this.$refs.modalSection.hide('modalSection')
                }, 300)
              })
            }
          } else {
            if (this.section.shared != null) {
              this.sharedKey = this.section.shared.key
              if (this.selectedKey != null) {
                if (this.sharedSections[this.sharedKey] == null) {
                  // Se la chiave di condivisione selezionata non e' presente in locale chiamo le API
                  this.$axios.get(`/sections/${this.sharedKey}/value`)
                    .then((res) => {
                      this.$emit('update-shared-section', this.sharedKey, res.data)
                    })
                }
              } else {
                if (this.sharedSections[this.sharedKey] == null) {
                  // Creo una nuova chiave di condivisione
                  this.sectionElements = this.section.elements
                  this.$emit('update-shared-section', this.sharedKey, this.sectionElements)
                }
              }
              this.section.shared = true
              this.section.elements = this.sharedKey
            } else {
              this.section.shared = false
            }
            tab.sections.push(this.section)
            this.section.shared = null
            this.$nextTick(() => {
              setTimeout(() => {
                this.$refs.modalSection.hide('modalSection')
              }, 300)
            })
          }
        }
      }
    },
    toggleInput () {
      var vm = this
      this.toggleInputFilter = !this.toggleInputFilter
      var formats = null
      if (this.toggleInputFilter) {
        formats = 'object|array<object>'
      }
      $('#inputBinding').empty()
      $('#inputBinding').append('<option></option>')
      $('#inputBinding').select2('destroy')
      $('#inputBinding').select2({
        placeholder: 'Seleziona il binding di input',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: this.getPathHintsPlus(this.getPathHints(this.schema, true, null, formats), 'input'),
        insertTag: function (data, tag) {
          // Insert the tag at the end of the results
          data.push(tag)
        },
        createTag: function (params) {
          // Non creare il tag se non risulta essere un path valido
          if (!vm.isPath(params.term)) {
            // Restituisce null per disabilitare la creazione del tag
            return null
          }

          return {
            id: params.term,
            text: params.term
          }
        },
        language: 'it'
      }).on('change', function () {
        vm.section.input = this.value
      })

      if (vm.section.input !== '') {
        $('#inputBinding').val(vm.section.input).trigger('change')
      }
    },
    toggleOutput () {
      var vm = this
      this.toggleOutputFilter = !this.toggleOutputFilter
      var formats = null
      if (this.toggleOutputFilter) {
        formats = 'object|array<object>'
      }
      $('#outputBinding').empty()
      $('#outputBinding').append('<option></option>')
      $('#outputBinding').select2('destroy')
      $('#outputBinding').select2({
        placeholder: 'Seleziona il binding di output',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: this.getPathHintsPlus(this.getPathHints(this.schema, false, null, formats), 'output'),
        insertTag: function (data, tag) {
        // Insert the tag at the end of the results
          data.push(tag)
        },
        createTag: function (params) {
        // Non creare il tag se non risulta essere un path valido
          if (!vm.isPath(params.term)) {
          // Restituisce null per disabilitare la creazione del tag
            return null
          }

          return {
            id: params.term,
            text: params.term
          }
        },
        language: 'it'
      }).on('change', function () {
        vm.section.output = this.value
      })

      if (vm.section.output !== '') {
        $('#outputBinding').val(vm.section.output).trigger('change')
      }
    }
  },
  watch: {
    // Effettuo una ricerca al cambiamento della input relativa alla key di condivisione
    query (newQuery) {
      this.$axios.get(`/sections?key=${newQuery}`)
        .then((res) => {
          this.keys = res.data.data
        })
    }
  }
}
</script>

<style>
  .list-group-item.active {
    color: #fff!important;
    background-color: #0970b8!important;
    border-collapse: #0970b8!important;
  }

  .select2-container {
    width: 91%!important;
  }

  .select2-container .select2-selection--single .select2-selection__clear {
    margin-top: -2px;
  }

  .btn-binding-filter {
    font-size: 13px;
    transition: ease-in 0.5s;
    background-color: #0970b8;
    border-color: #0970b8;
    color: #fff;
  }

  .btn-binding-filter:hover {
    background-color: #35495e;
    border-color: #35495e;
    color: #fff;
  }

  .custom-control-input[disabled] ~ .custom-control-label,
  .custom-control-input:disabled ~ .custom-control-label {
    color: #212529;
  }
</style>
