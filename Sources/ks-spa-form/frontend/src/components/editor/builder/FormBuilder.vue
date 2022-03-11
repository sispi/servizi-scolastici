<template>
  <div :id="'fb-editor-' + tabId + '-' + sectionId"></div>
</template>

<script>
import Utils from '@/mixins/utils.js'
import $ from 'jquery'
window.jQuery = $
window.$ = $

require('bootstrap')
require('jquery-ui-sortable')
require('formBuilder')
require('@fortawesome/fontawesome-free/css/all.css')
require('select2/dist/css/select2.min.css')
require('select2-bootstrap-theme/dist/select2-bootstrap.css')
require('select2')
require('select2/dist/js/i18n/it')
let htmlCode = ''

export default {
  name: 'FormBuilder',
  mixins: [Utils],
  props: {
    tabId: null,
    sectionId: null,
    schema: null,
    tabs: null,
    sharedSections: null,
    sectionInput: null,
    sectionOutput: null
  },
  data () {
    return {
      formBuilderInit: true
    }
  },
  methods: {
    initFormBuilder (formData) {
      var vm = this
      // var defaultLocale = 'en-US'
      var dataType = 'json'
      var index = this.tabId
      var subIndex = this.sectionId
      const setCurrentFieldIdValues = value => {
        setTimeout(function () {
          vm.findCustomFieldOnLoad(value)
          vm.initSelect2(value)
        }, 500)
        const currentFieldIds = document.querySelectorAll('.current-field-id')
        currentFieldIds.forEach(field => {
          field.value = value
        })
      }
      const fields = [
        {
          type: 'checkbox',
          label: 'Checkbox'
        },
        {
          label: 'HTML',
          attrs: {
            type: 'html'
          },
          icon: '<i class="fab fa-html5" style="margin-right: 8px; font-size: 19px;"></i>'
        }
      ]

      const templates = {
        html: function (fieldData) {
          return {
            field: '<textarea id="' + fieldData.name + '"></textarea>',
            onRender: () => {
              window.CKEDITOR.replace(fieldData.name, {
                removePlugins: 'resize',
                htmlEncodeOutput: false,
                entities: false,
                basicEntities: false
              })
            }
          }
        }
      }

      const typeUserDisabledAttrs = {
        button: ['name'],
        hidden: ['name'],
        text: ['className', 'name'],
        textarea: ['className', 'name'],
        number: ['className', 'name'],
        date: ['className', 'name'],
        html: ['name'],
        checkbox: ['name', 'options'],
        'checkbox-group': ['name'],
        'radio-group': ['name'],
        select: ['className', 'name'],
        file: ['className', 'name']
      }

      const typeUserAttrs = {
        button: {
          action: {
            label: 'Action',
            type: 'select',
            options: {
              submit: 'Submit',
              save: 'Save',
              custom: 'Custom',
              validate: 'Validate',
              back: 'Back',
              next: 'Next',
              clear: 'Clear'
            },
            value: 'submit'
          },
          customAction: {
            label: 'Action Name (Custom)',
            type: 'text',
            value: ''
          },
          validationUrl: {
            label: 'Validation URL',
            type: 'text',
            value: ''
          },
          validationUrlProcessor: {
            label: 'Validation Processor',
            type: 'text',
            value: ''
          },
          actionUrl: {
            label: 'Action URL',
            type: 'text',
            value: ''
          },
          actionUrlProcessor: {
            label: 'Action Processor',
            type: 'text',
            value: ''
          },
          onAction: {
            label: 'On Action',
            type: 'text',
            value: ''
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Submit'
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: false
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: 'btn-primary'
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        header: {
          subtype: {
            label: 'Type',
            type: 'select',
            options: {
              h1: 'h1',
              h2: 'h2',
              h3: 'h3',
              h4: 'h4',
              h5: 'h5',
              h6: 'h6'
            },
            value: 'h1'
          },
          label: {
            label: 'Text',
            type: 'text',
            value: ''
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        html: {
          value: {
            label: 'Value',
            type: 'text',
            value: ''
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        hidden: {
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        text: {
          subtype: {
            label: 'Type',
            type: 'select',
            options: {
              text: 'Text',
              password: 'Password',
              email: 'Email'
            },
            value: 'text'
          },
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          regex: {
            label: 'Regex',
            type: 'text',
            value: ''
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        textarea: {
          subtype: {
            label: 'Type',
            type: 'select',
            options: {
              text: 'Text',
              editor: 'Editor',
              html: 'HTML',
              xml: 'XML',
              json: 'JSON',
              yaml: 'YAML',
              sql: 'SQL',
              properties: 'Properties'
            },
            value: 'text'
          },
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          height: {
            label: 'Height',
            type: 'text',
            value: '10'
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        number: {
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          step: {
            label: 'Step',
            type: 'text',
            value: '1'
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        date: {
          subtype: {
            label: 'Type',
            type: 'select',
            options: {
              'date-time': 'Datetime',
              date: 'Date',
              time: 'Time'
            },
            value: 'date'
          },
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          offset: {
            label: 'Offset',
            type: 'select',
            options: {
              start: 'Start',
              end: 'End'
            },
            value: 'start'
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        checkbox: {
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        'checkbox-group': {
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          source: {
            label: 'Source',
            type: 'select',
            options: {
              values: 'Values',
              model: 'Model'
            },
            value: 'values'
          },
          model: {
            label: 'Model',
            type: 'text',
            value: ''
          },
          rows: {
            label: 'Rows',
            type: 'text',
            value: '1'
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        'radio-group': {
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          source: {
            label: 'Source',
            type: 'select',
            options: {
              values: 'Values',
              model: 'Model'
            },
            value: 'values'
          },
          model: {
            label: 'Model',
            type: 'text',
            value: ''
          },
          rows: {
            label: 'Rows',
            type: 'text',
            value: '1'
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        select: {
          ref: {
            label: 'Ref',
            type: 'text',
            value: ''
          },
          mode: {
            label: 'Type',
            type: 'select',
            options: {
              single: 'Single',
              multiple: 'Multiple'
            },
            value: 'single'
          },
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          source: {
            label: 'Source',
            type: 'select',
            options: {
              values: 'Values',
              model: 'Model',
              url: 'URL'
            },
            value: 'values'
          },
          model: {
            label: 'Model',
            type: 'text',
            value: ''
          },
          url: {
            label: 'URL',
            type: 'text',
            value: ''
          },
          urlProcessor: {
            label: 'URL Processor',
            type: 'text',
            value: ''
          },
          tags: {
            label: 'Tags',
            type: 'checkbox',
            value: false
          },
          chars: {
            label: 'Min Search Chars',
            type: 'text',
            value: '0'
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        },
        file: {
          mode: {
            label: 'Type',
            type: 'select',
            options: {
              single: 'Single',
              multiple: 'Multiple'
            },
            value: 'single'
          },
          input: {
            label: 'Input',
            type: 'select',
            options: {}
          },
          output: {
            label: 'Output',
            type: 'select',
            options: {}
          },
          show: {
            label: 'Show',
            type: 'text',
            value: 'true'
          },
          disabled: {
            label: 'Disabled',
            type: 'text',
            value: 'false'
          },
          required: {
            label: 'Required',
            type: 'checkbox',
            value: true
          },
          valid: {
            label: 'Valid',
            type: 'text',
            value: 'true'
          },
          invalidFeedback: {
            label: 'Invalid Feedback',
            type: 'text',
            value: ''
          },
          url: {
            label: 'URL',
            type: 'text',
            value: ''
          },
          urlProcessor: {
            label: 'URL Processor',
            type: 'text',
            value: ''
          },
          extensions: {
            label: 'Extensions',
            type: 'text',
            value: ''
          },
          size: {
            label: 'Size (MB)',
            type: 'text',
            value: ''
          },
          min: {
            label: 'Min',
            type: 'text',
            value: ''
          },
          max: {
            label: 'Max',
            type: 'text',
            value: ''
          },
          onChange: {
            label: 'On Change',
            type: 'text',
            value: ''
          },
          label: {
            label: 'Label',
            type: 'text',
            value: 'Label'
          },
          placeholder: {
            label: 'Placeholder',
            type: 'text',
            value: ''
          },
          description: {
            label: 'Description',
            type: 'text',
            value: ''
          },
          margin: {
            label: 'Margin',
            type: 'text',
            value: ''
          },
          width: {
            label: 'Width',
            type: 'text',
            value: ''
          },
          break: {
            label: 'Break',
            type: 'checkbox',
            value: true
          },
          classes: {
            label: 'Class',
            type: 'text',
            value: ''
          },
          style: {
            label: 'Style',
            type: 'text',
            value: ''
          }
        }
      }

      const disabledAttrs = [
        'access',
        'className',
        'description',
        'inline',
        'label',
        'max',
        'maxlength',
        'min',
        'multiple',
        'name',
        // 'options',
        'other',
        'placeholder',
        'required',
        'rows',
        'step',
        'style',
        'subtype',
        'toggle',
        'value'
      ]

      const fbOptions = {
        // disableHTMLLabels: true,
        disabledAttrs,
        // allowStageSort: false,
        dataType,
        onAddField: fieldId => {
          setCurrentFieldIdValues(fieldId)
        },
        onOpenFieldEdit: field => {
          if ($(field).closest('.form-field').hasClass('html-field')) {
            $(field).find('.fld-value.form-control').val(htmlCode)
          }
        },
        onCloseFieldEdit: field => {
          vm.findCustomField(field)
          /* if ($(field).find('.fld-input.form-control').hasClass('select2-hidden-accessible')) {
            $(field).find('.fld-input.form-control').select2('destroy')
          }
          if ($(field).find('.fld-output.form-control').hasClass('select2-hidden-accessible')) {
            $(field).find('.fld-output.form-control').select2('destroy')
          } */
        },
        // onClearAll: () => formData.splice(index, 1),
        stickyControls: {
          enable: true
        },
        sortableControls: true,
        fields: fields,
        templates: templates,
        // inputSets: inputSets,
        typeUserDisabledAttrs: typeUserDisabledAttrs,
        typeUserAttrs: typeUserAttrs,
        disableInjectedStyle: false,
        // replaceFields: replaceFields,
        disabledActionButtons: ['data', 'save', 'clear'],
        disableFields: ['paragraph', 'autocomplete']
        /* controlPosition: 'right', // left|right,
        i18n: {
          override: {
            [defaultLocale]: {
              textarea: 'Text Area Input',
              text: 'Text Input',
              number: 'Number Input',
              date: 'Date Input'
            }
          }
        } */
      }

      if (formData) {
        fbOptions.formData = formData
      }

      $('#fb-editor-' + index + '-' + subIndex).formBuilder(fbOptions)
    },
    initSelect2 (value) {
      const vm = this
      const field = '#' + value
      if (!$(field).closest('.form-field').hasClass('header-field') && !$(field).closest('.form-field').hasClass('button-field')) {
        const inputSelect = $(field).find('.fld-input.form-control')
        const inputSelectIdSplit = inputSelect[0].id.split('-')
        const elementIndex = parseInt(inputSelectIdSplit[inputSelectIdSplit.length - 1]) - 1
        const sectionElements = this.tabs[this.tabId].sections[this.sectionId].elements
        let inputSelectVal = ''
        if (this.formBuilderInit) {
          if (Array.isArray(sectionElements) && sectionElements[elementIndex] != null) {
            inputSelectVal = sectionElements[elementIndex].input
          } else {
            if (this.sharedSections != null && this.sharedSections[sectionElements] != null) {
              if (this.sharedSections[sectionElements][elementIndex] != null) {
                inputSelectVal = this.sharedSections[sectionElements][elementIndex].input
              }
            }
          }
        } else {
          inputSelectVal = inputSelect.val()
        }
        const fieldType = this.getFieldType(field)
        let fieldSubType = this.getFieldSubType(field, fieldType)
        let fieldFormats = this.getFormatsFromFieldType(fieldType, fieldSubType)
        const inputWrap = $(field).find('.form-group.input-wrap').find('.input-wrap')
        inputWrap.addClass('input-group')
        const spanInputWrapAppend = $('<span/>', {
          class: 'input-group-btn'
        }).append(
          $('<button/>', {
            class: 'btn btn-binding-filter',
            'data-toggle': 'tooltip',
            title: 'Filtro binding attivato'
          }).on('click', function () {
            vm.toggleInput(this, fieldFormats)
          }).append(
            $('<i/>', {
              class: 'fas fa-filter'
            })
          )
        )
        inputWrap.find('.input-group-btn').remove()
        inputWrap.append(spanInputWrapAppend)
        const outputSelect = $(field).find('.fld-output.form-control')
        let outputSelectVal = ''
        if (this.formBuilderInit) {
          if (Array.isArray(sectionElements) && sectionElements[elementIndex] != null) {
            outputSelectVal = sectionElements[elementIndex].output
          } else {
            if (this.sharedSections != null && this.sharedSections[sectionElements] != null) {
              if (this.sharedSections[sectionElements][elementIndex] != null) {
                outputSelectVal = this.sharedSections[sectionElements][elementIndex].output
              }
            }
          }
        } else {
          outputSelectVal = outputSelect.val()
        }
        const outputWrap = $(field).find('.form-group.output-wrap').find('.input-wrap')
        outputWrap.addClass('input-group')
        const spanOutputWrapAppend = $('<span/>', {
          class: 'input-group-btn'
        }).append(
          $('<button/>', {
            class: 'btn btn-binding-filter',
            'data-toggle': 'tooltip',
            title: 'Filtro binding attivato'
          }).on('click', function () {
            vm.toggleOutput(this, fieldFormats)
          }).append(
            $('<i/>', {
              class: 'fas fa-filter'
            })
          )
        )
        outputWrap.find('.input-group-btn').remove()
        outputWrap.append(spanOutputWrapAppend)
        // outputWrap.append('<span class="input-group-btn"><button class="btn btn-binding-filter" data-toggle="tooltip" title="Filtro binding attivato" onclick="toggleOutput()"><i class="fas fa-filter"></i></button></span>')
        $('[data-toggle="tooltip"]').tooltip()
        let inputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, true, this.sectionInput, fieldFormats), inputSelectVal)
        inputSelect.empty()
        inputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
        inputSelect.select2({
          placeholder: 'Seleziona il binding di input',
          theme: 'bootstrap',
          tags: true,
          allowClear: true,
          data: inputBindings,
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
        })
        if (inputSelectVal) {
          inputSelect.val(inputSelectVal).trigger('change')
        }
        if (inputBindings.length !== this.getPathHints(this.schema, true, this.sectionInput, fieldFormats).length) {
          inputWrap.find('.btn-binding-filter').click()
        }
        let outputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, false, this.sectionOutput, fieldFormats), outputSelectVal)
        outputSelect.empty()
        outputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
        outputSelect.select2({
          placeholder: 'Seleziona il binding di output',
          theme: 'bootstrap',
          tags: true,
          allowClear: true,
          data: outputBindings,
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
        })
        if (outputSelectVal) {
          outputSelect.val(outputSelectVal).trigger('change')
        }
        if (outputBindings.length !== this.getPathHints(this.schema, false, this.sectionOutput, fieldFormats).length) {
          outputWrap.find('.btn-binding-filter').click()
        }
        if (fieldType === 'text' || fieldType === 'date') {
          $(field).find('.fld-subtype.form-control').off('change') // Resetto l'evento onchange
          $(field).find('.fld-subtype.form-control').change(function () {
            const buttonFilterInput = $(inputSelect).parent().find('.input-group-btn').find('.btn-binding-filter')
            const filterIconInput = $(buttonFilterInput).find('.fas')
            if (filterIconInput.hasClass('fa-asterisk')) {
              filterIconInput.removeClass('fa-asterisk')
              filterIconInput.addClass('fa-filter')
              $(buttonFilterInput).attr('data-original-title', 'Filtro binding attivato')
            }
            fieldSubType = $(this).val()
            fieldFormats = vm.getFormatsFromFieldType(fieldType, fieldSubType)
            inputBindings = vm.getPathHints(vm.schema, true, vm.sectionInput, fieldFormats)
            inputSelect.empty()
            inputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
            if ($(inputSelect).hasClass('select2-hidden-accessible')) {
              inputSelect.select2('destroy')
            }
            inputSelect.select2({
              placeholder: 'Seleziona il binding di input',
              theme: 'bootstrap',
              tags: true,
              allowClear: true,
              data: inputBindings,
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
            })
            const buttonFilterOutput = $(outputSelect).parent().find('.input-group-btn').find('.btn-binding-filter')
            const filterIconOutput = $(buttonFilterOutput).find('.fas')
            if (filterIconOutput.hasClass('fa-asterisk')) {
              filterIconOutput.removeClass('fa-asterisk')
              filterIconOutput.addClass('fa-filter')
              $(buttonFilterOutput).attr('data-original-title', 'Filtro binding attivato')
            }
            outputBindings = vm.getPathHints(vm.schema, false, vm.sectionOutput, fieldFormats)
            outputSelect.empty()
            outputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
            if ($(outputSelect).hasClass('select2-hidden-accessible')) {
              outputSelect.select2('destroy')
            }
            outputSelect.select2({
              placeholder: 'Seleziona il binding di output',
              theme: 'bootstrap',
              tags: true,
              allowClear: true,
              data: outputBindings,
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
            })
          })
        } else if (fieldType === 'select' || fieldType === 'file') {
          $(field).find('.fld-mode.form-control').off('change')
          $(field).find('.fld-mode.form-control').change(function () {
            const buttonFilterInput = $(inputSelect).parent().find('.input-group-btn').find('.btn-binding-filter')
            const filterIconInput = $(buttonFilterInput).find('.fas')
            if (filterIconInput.hasClass('fa-asterisk')) {
              filterIconInput.removeClass('fa-asterisk')
              filterIconInput.addClass('fa-filter')
              $(buttonFilterInput).attr('data-original-title', 'Filtro binding attivato')
            }
            fieldSubType = $(this).val()
            fieldFormats = vm.getFormatsFromFieldType(fieldType, fieldSubType)
            inputBindings = vm.getPathHints(vm.schema, true, vm.sectionInput, fieldFormats)
            inputSelect.empty()
            inputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
            if ($(inputSelect).hasClass('select2-hidden-accessible')) {
              inputSelect.select2('destroy')
            }
            inputSelect.select2({
              placeholder: 'Seleziona il binding di input',
              theme: 'bootstrap',
              tags: true,
              allowClear: true,
              data: inputBindings,
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
            })
            const buttonFilterOutput = $(outputSelect).parent().find('.input-group-btn').find('.btn-binding-filter')
            const filterIconOutput = $(buttonFilterOutput).find('.fas')
            if (filterIconOutput.hasClass('fa-asterisk')) {
              filterIconOutput.removeClass('fa-asterisk')
              filterIconOutput.addClass('fa-filter')
              $(buttonFilterOutput).attr('data-original-title', 'Filtro binding attivato')
            }
            outputBindings = vm.getPathHints(vm.schema, false, vm.sectionOutput, fieldFormats)
            outputSelect.empty()
            outputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
            if ($(outputSelect).hasClass('select2-hidden-accessible')) {
              outputSelect.select2('destroy')
            }
            outputSelect.select2({
              placeholder: 'Seleziona il binding di output',
              theme: 'bootstrap',
              tags: true,
              allowClear: true,
              data: outputBindings,
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
            })
          })
        }

        if (elementIndex === sectionElements.length - 1) {
          this.formBuilderInit = false
        }
      }
    },
    getFieldType (field) {
      let fieldType = ''
      if (field) {
        const formField = $(field).closest('.form-field')
        if (formField.hasClass('checkbox-group-field')) {
          fieldType = 'checkbox-group'
        } else if (formField.hasClass('checkbox-field')) {
          fieldType = 'checkbox'
        } else if (formField.hasClass('date-field')) {
          fieldType = 'date'
        } else if (formField.hasClass('file-field')) {
          fieldType = 'file'
        } else if (formField.hasClass('hidden-field')) {
          fieldType = 'hidden'
        } else if (formField.hasClass('number-field')) {
          fieldType = 'number'
        } else if (formField.hasClass('radio-group-field')) {
          fieldType = 'radio-group'
        } else if (formField.hasClass('select-field')) {
          fieldType = 'select'
        } else if (formField.hasClass('text-field')) {
          fieldType = 'text'
        } else if (formField.hasClass('textarea-field')) {
          fieldType = 'textarea'
        }
      }

      return fieldType
    },
    getFieldSubType (field, fieldType) {
      let fieldSubType = null
      if (fieldType === 'text' || fieldType === 'date') {
        fieldSubType = $(field).find('.fld-subtype.form-control').val()
      } else if (fieldType === 'select' || fieldType === 'file') {
        fieldSubType = $(field).find('.fld-mode.form-control').val()
      }

      return fieldSubType
    },
    getFormatsFromFieldType (fieldType, fieldSubType) {
      let fieldFormats = null
      if (fieldType !== 'text' && fieldType !== 'date' && fieldType !== 'select' && fieldType !== 'file') {
        switch (fieldType) {
          case 'checkbox-group':
            fieldFormats = 'array<string>'
            break
          case 'checkbox':
            fieldFormats = 'boolean'
            break
          case 'hidden':
            fieldFormats = null
            break
          case 'number':
            fieldFormats = 'number'
            break
          case 'radio-group':
            fieldFormats = 'string'
            break
          case 'textarea':
            fieldFormats = 'string'
            break
        }
      } else {
        if (fieldSubType) {
          if (fieldType === 'text') {
            switch (fieldSubType) {
              case 'text':
                fieldFormats = 'string|string:text'
                break
              case 'password':
                fieldFormats = 'string:password'
                break
              case 'email':
                fieldFormats = 'string:email'
                break
            }
          } else if (fieldType === 'date') {
            switch (fieldSubType) {
              case 'date':
                fieldFormats = 'string:date'
                break
              case 'time':
                fieldFormats = 'string:time'
                break
              case 'date-time':
                fieldFormats = 'string:date-time'
                break
            }
          } else if (fieldType === 'select') {
            switch (fieldSubType) {
              case 'single':
                fieldFormats = 'string'
                break
              case 'multiple':
                fieldFormats = 'array<string>'
                break
            }
          } else if (fieldType === 'file') {
            switch (fieldSubType) {
              case 'single':
                fieldFormats = 'string:uri-reference'
                break
              case 'multiple':
                fieldFormats = 'array<string:uri-reference>'
                break
            }
          }
        } else {
          switch (fieldType) {
            case 'date':
              fieldFormats = 'string:date|string:time|string:date-time'
              break
            case 'select':
              fieldFormats = 'string|array<string>'
              break
            case 'text':
              fieldFormats = 'string|string:text|string:password|string:email'
              break
            case 'file':
              fieldFormats = 'string:uri-reference|array<string:uri-reference>'
              break
          }
        }
      }

      return fieldFormats
    },
    createNewTagForSection (tagName, fieldFormats, forPath) {
      let newTagType = ''
      let newTagFormat = ''
      let fieldFormatsSplit = null
      if (fieldFormats) {
        if (fieldFormats.split('|').length > 1) {
          fieldFormatsSplit = fieldFormats[0].split(':')
          if (fieldFormatsSplit.length > 1) {
            newTagType = fieldFormatsSplit[0]
            newTagFormat = fieldFormatsSplit[1]
          } else {
            newTagType = fieldFormats[0]
          }
        } else {
          fieldFormatsSplit = fieldFormats.split(':')
          if (fieldFormatsSplit.length > 1) {
            newTagType = fieldFormatsSplit[0]
            newTagFormat = fieldFormatsSplit[1]
          } else {
            newTagType = fieldFormats
          }
        }
      }

      let newTag = null
      if (newTagFormat !== '') {
        newTag = {
          format: newTagFormat,
          type: newTagType
        }
      } else {
        newTag = {
          type: newTagType
        }
      }

      const formSchema = this.schema
      if (formSchema.properties != null) {
        if (forPath != null && formSchema.properties[forPath] != null) {
          if (formSchema.properties[forPath].type === 'object') {
            formSchema.properties[forPath].properties[tagName] = newTag
          } else if (formSchema.properties[forPath].type === 'array') {
            formSchema.properties[forPath].items[tagName] = newTag
          }
        } else {
          formSchema.properties[tagName] = newTag
        }
        this.$emit('update-form-schema', formSchema)
      }

      return newTagType
    },
    getPathHintsPlus (hints, selectVal) {
      var hintKeys = []
      hints.forEach(binding => {
        hintKeys.push(binding.id)
      })

      if (selectVal != null && selectVal !== '' && !hintKeys.includes(selectVal)) {
        hints.push(selectVal)
      }

      return hints
    },
    toggleInput (button, fieldFormats) {
      const vm = this
      $(button).tooltip('hide')
      const filterIcon = $(button).find('.fas')
      const inputSelect = $(button).closest('.input-wrap').find('.fld-input.form-control')
      const inputSelectVal = inputSelect.val()
      let inputBindings = null
      if (filterIcon.hasClass('fa-filter')) {
        filterIcon.removeClass('fa-filter')
        filterIcon.addClass('fa-asterisk')
        $(button).attr('data-original-title', 'Filtro binding disattivato')
        inputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, true, this.sectionInput, null), inputSelectVal)
      } else if (filterIcon.hasClass('fa-asterisk')) {
        filterIcon.removeClass('fa-asterisk')
        filterIcon.addClass('fa-filter')
        $(button).attr('data-original-title', 'Filtro binding attivato')
        inputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, true, this.sectionInput, fieldFormats), inputSelectVal)
      }
      inputSelect.empty()
      inputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
      inputSelect.select2('destroy')
      inputSelect.select2({
        placeholder: 'Seleziona il binding di input',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: inputBindings,
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
      })
      if (inputSelectVal) {
        inputSelect.val(inputSelectVal).trigger('change')
      }
    },
    toggleOutput (button, fieldFormats) {
      const vm = this
      $(button).tooltip('hide')
      const filterIcon = $(button).find('.fas')
      const outputSelect = $(button).closest('.output-wrap').find('.fld-output.form-control')
      const outputSelectVal = outputSelect.val()
      let outputBindings = null
      if (filterIcon.hasClass('fa-filter')) {
        filterIcon.removeClass('fa-filter')
        filterIcon.addClass('fa-asterisk')
        $(button).attr('data-original-title', 'Filtro binding disattivato')
        outputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, false, this.sectionOutput, null), outputSelectVal)
      } else if (filterIcon.hasClass('fa-asterisk')) {
        filterIcon.removeClass('fa-asterisk')
        filterIcon.addClass('fa-filter')
        $(button).attr('data-original-title', 'Filtro binding attivato')
        outputBindings = this.getPathHintsPlus(this.getPathHints(this.schema, false, this.sectionOutput, fieldFormats), outputSelectVal)
      }
      outputSelect.empty()
      outputSelect.append('<option></option>') // Aggiungo una option vuota per attivare il placeholder
      outputSelect.select2('destroy')
      outputSelect.select2({
        placeholder: 'Seleziona il binding di output',
        theme: 'bootstrap',
        tags: true,
        allowClear: true,
        data: outputBindings,
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
      })
      if (outputSelectVal) {
        outputSelect.val(outputSelectVal).trigger('change')
      }
    },
    findCustomFieldOnLoad (value) {
      if ($('#' + value).hasClass('html-field')) {
        htmlCode = $('#' + value).find('.fld-value.form-control').val() // Prendo il value nella modalita di edit
        window.CKEDITOR.instances[$('#' + value).find('textarea')[0].id].setData(htmlCode)
        $('#' + value).find('.toggle-form').click(function () {
          if ($(this).closest('.form-field').hasClass('html-field') && !$(this).hasClass('open')) {
            htmlCode = window.CKEDITOR.instances[$('#' + value).find('textarea')[0].id].getData().trim()
          }
        })
      } else if ($('#' + value).hasClass('checkbox-field')) {
        var labelFor = $('#' + value).find('.formbuilder-checkbox-label').attr('for')
        $(`#${value}`).find('.formbuilder-checkbox').prepend('<input type="checkbox" name="' + labelFor + '"> ')
      }
    },
    findCustomField (field) {
      if ($(field).closest('.form-field').hasClass('html-field')) {
        var htmlValue = $(field).find('.fld-value.form-control').val() // Prendo il value nella modalita di edit

        setTimeout(function () {
          // Imposto il value nel ckeditor
          window.CKEDITOR.instances[$($(field)[0].previousSibling).find('textarea')[0].id].setData(htmlValue)
        }, 500)
      } else if ($(field).closest('.form-field').hasClass('checkbox-field')) {
        var labelFor = $($(field)[0].previousSibling).find('.formbuilder-checkbox-label').attr('for')
        setTimeout(function () {
          $($(field)[0].previousSibling).find('.formbuilder-checkbox').prepend('<input type="checkbox" name="' + labelFor + '"> ')
        }, 300)
      }
    },
    closeFormBuilder () {
      const formRenderData = $('#fb-editor-' + this.tabId + '-' + this.sectionId).formBuilder('getData', 'json')
      this.$emit('update-json-elements', this.tabId, this.sectionId, JSON.parse(formRenderData))
      $('#fb-editor-' + this.tabId + '-' + this.sectionId).empty()
      this.formBuilderInit = true
    },
    updateFormBuilder (formData) {
      $('#fb-editor-' + this.tabId + '-' + this.sectionId).formBuilder('setData', formData)
    },
    updateJsonElements () {
      const formRenderData = $('#fb-editor-' + this.tabId + '-' + this.sectionId).formBuilder('getData', 'json')
      this.$emit('update-json-elements', this.tabId, this.sectionId, JSON.parse(formRenderData))
    }
  }
}
</script>

<style>
  a.cke_button {
    height: auto!important;
  }

  .input-wrap .select2-container,
  .output-wrap .select2-container {
    width: 100%!important;
  }

  .m-l-30 {
    margin-left: 30px;
  }

  .checkbox-field .required-asterisk,
  .checkbox-field .tooltip-element {
    display: none!important;
  }
</style>
