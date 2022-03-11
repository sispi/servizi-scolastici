<template>
    <div class="form-footer">
        <b-card no-body class="m-b-20" v-show="footer != null">
            <b-card-header>
              <div v-b-toggle.footer-body>
                <strong>
                  <a class="footer-collapse-link" href="javascript:void(0);">
                    <font-awesome-icon v-if="isVisible" icon="minus" /><font-awesome-icon v-else icon="plus" />&nbsp; Footer
                  </a>
                </strong>
                <a href="javascript:void(0);" class="float-right" v-b-tooltip.hover title="Elimina"><font-awesome-icon icon="times" @click="resetFooter" /></a>
              </div>
            </b-card-header>
            <b-collapse id="footer-body" v-model="isVisible" @show="expandFooter" accordion="accordion" role="tabpanel">
              <b-card-body>
                <ckeditor v-model="content" @input="updateFooter" :config="{
                  htmlEncodeOutput: false,
                  entities: false,
                  basicEntities: false
                }"></ckeditor>
              </b-card-body>
            </b-collapse>
        </b-card>
        <div class="row m-b-30" v-show="footer == null">
          <b-button variant="default" class="addButton float-left mr-3" @click="initFooter"><font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp;Aggiungi Footer &nbsp;</b-button>
        </div>
    </div>
</template>

<script>
export default {
  name: 'FormFooter',
  props: {
    footer: {
      required: true
    }
  },
  data () {
    return {
      content: '',
      isVisible: false
    }
  },
  methods: {
    initFooter () {
      this.content = ''
      this.$emit('update-footer', this.content)
    },
    expandFooter () {
      if (this.footer != null) {
        this.content = this.footer
      }
    },
    updateFooter () {
      this.$emit('update-footer', this.content)
    },
    resetFooter () {
      this.$emit('update-footer', null)
    }
  }
}
</script>

<style scoped>
  .footer-collapse-link:hover {
    text-decoration: none;
  }
</style>
