<template>
    <div class="form-header">
        <b-card no-body class="m-b-20" v-show="header != null">
            <b-card-header>
              <div v-b-toggle.header-body>
                <strong>
                  <a class="header-collapse-link" href="javascript:void(0);">
                    <font-awesome-icon v-if="isVisible" icon="minus" /><font-awesome-icon v-else icon="plus" />&nbsp; Header
                  </a>
                </strong>
                <a href="javascript:void(0);" class="float-right" v-b-tooltip.hover title="Elimina"><font-awesome-icon icon="times" @click="resetHeader" /></a>
              </div>
            </b-card-header>
            <b-collapse id="header-body" v-model="isVisible" @show="expandHeader" accordion="accordion" role="tabpanel">
              <b-card-body>
                <ckeditor v-model="content" @input="updateHeader" :config="{
                  htmlEncodeOutput: false,
                  entities: false,
                  basicEntities: false
                }"></ckeditor>
              </b-card-body>
            </b-collapse>
        </b-card>
        <div class="m-b-30" v-show="header == null">
          <span class="m-l-10 text-muted"></span>
          <b-button variant="default" class="addButton float-left mr-3" @click="initHeader"><font-awesome-icon style="font-size: 19px" icon="plus-circle" />&nbsp;Aggiungi Header &nbsp;</b-button>
        </div>
    </div>
</template>

<script>
export default {
  name: 'FormHeader',
  props: {
    header: {
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
    initHeader () {
      this.content = ''
      this.$emit('update-header', this.content)
    },
    expandHeader () {
      if (this.header != null) {
        this.content = this.header
      }
    },
    updateHeader () {
      this.$emit('update-header', this.content)
    },
    resetHeader () {
      this.$emit('update-header', null)
    }
  }
}
</script>

<style scoped>
  .header-collapse-link:hover {
    text-decoration: none;
  }
</style>
