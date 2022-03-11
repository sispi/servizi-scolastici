<template>
  <div class="preview">
    <form-renderer v-if="form"
      :identifier="form.id.toString()"
      :definition="form.definition"
      :value="formModel"
      @event="onFormEvent">
    </form-renderer>
  </div>
</template>

<script>
import FormRenderer from '@/components/renderer/FormRenderer.vue'

export default {
  name: 'Preview',
  components: {
    FormRenderer
  },
  props: {
    formId: {
      required: true
    }
  },
  data () {
    return {
      form: null,
      formModel: {
        value: 0
      }
    }
  },
  async created () {
    this.form = (await this.$axios.get(`/forms/${encodeURIComponent(this.formId)}?fetch=definition`)).data
  },
  methods: {
    onFormEvent: function (identifier, event, model) {
      this.$notify({
        type: 'success',
        title: `Evento '${event}' emesso`
      })
    }
  }

}
</script>
