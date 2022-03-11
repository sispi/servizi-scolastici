<template>
    <div class="toolbar m-b-20">
        <div class="row ml-0 mr-0 p-2">
            <div class="col-md-8 text-left">
                <h2 class="mb-0">Form Console</h2>
            </div>
            <div class="col-md-4 m-auto">
                <div class="float-right">
                    <a class="header-link" v-b-tooltip.hover title="Crea nuovo form">
                      <font-awesome-icon class="f-s-24" icon="plus" v-b-modal.modalNewForm />
                    </a>
                </div>
            </div>
        </div>
        <modal-new-form @load-forms="loadForms"></modal-new-form>
    </div>
</template>

<script>
import ModalNewForm from './modal/ModalNewForm.vue'

export default {
  name: 'ConsoleToolbar',
  components: {
    ModalNewForm
  },
  methods: {
    loadForms () {
      this.$emit('load-forms')
    }
  }
}
</script>
