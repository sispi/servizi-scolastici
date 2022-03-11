<template>
    <div class="form-footer">
        <b-card no-body class="m-b-20" v-show="script != null">
            <b-card-header>
              <div v-b-toggle.script-body>
                <strong>
                  <a class="script-collapse-link" href="javascript:void(0);">
                    <font-awesome-icon v-if="isVisible" icon="minus" /><font-awesome-icon v-else icon="plus" />&nbsp; Script
                  </a>
                </strong>
                <a href="javascript:void(0);" class="float-right" v-b-tooltip.hover title="Elimina"><font-awesome-icon icon="times" @click="resetScript" /></a>
              </div>
            </b-card-header>
            <b-collapse id="script-body" v-model="isVisible" @show="expandScript" accordion="accordion" role="tabpanel">
              <b-card-body>
                <codemirror v-model="content" @input="updateScript" :options="cmOptions" />
              </b-card-body>
            </b-collapse>
        </b-card>
        <div class="row m-b-30" v-show="script == null">
          <b-button variant="default" class="addButton float-left mr-3" @click="initScript"><font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp;Aggiungi Script &nbsp;</b-button>
        </div>
    </div>
</template>

<script>
import { codemirror } from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/javascript/javascript.js'

export default {
  name: 'FormScript',
  components: {
    codemirror
  },
  props: {
    script: {
      required: true
    }
  },
  data () {
    return {
      content: '',
      isVisible: false,
      cmOptions: {
        tabSize: 4,
        mode: 'text/javascript',
        line: true,
        lineNumbers: true,
        lineWrapping: true,
        matchBrackets: true,
        autoCloseBrackets: true,
        styleActiveLine: true
      }
    }
  },
  methods: {
    initScript () {
      this.content = ''
      this.$emit('update-script', this.content)
    },
    expandScript () {
      if (this.script != null) {
        this.content = this.script
      }
    },
    updateScript () {
      this.$emit('update-script', this.content)
    },
    resetScript () {
      this.$emit('update-script', null)
    }
  }
}
</script>

<style scoped>
  .script-collapse-link:hover {
    text-decoration: none;
  }
</style>
