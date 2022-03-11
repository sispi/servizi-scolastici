<template>
  <div class="ks-form">
    <div class="container-fluid">
      <!-- header -->
      <div
        v-if="definition.header"
        class="row"
        v-html="render(definition.header, model)">
      </div>
      <!-- tabs -->
      <ul
        v-if="definition.tabs.length > 1"
        class="row nav nav-tabs auto"
        role="tablist">
        <li
          v-for="(tab, tabIndex) in definition.tabs"
          :key="tabIndex"
          class="nav-item">
          <a
            :class="['nav-link',  {active: tabIndex == 0}]" :id="`tab-${tabIndex}-nav`"
            data-toggle="tab"
            :href="`#tab-${tabIndex}`"
            role="tab" aria-controls="`tab-${tabIndex}`"
            :aria-selected="tabIndex == 0">
            <div v-text="render(tab.title, model)" />
          </a>
        </li>
      </ul>
      <div
        class="row tab-content">
        <div
          v-for="(tab, tabIndex) in definition.tabs"
          :key="tabIndex"
          :class="['tab-pane', 'fade',  {show: tabIndex == 0}, {active: tabIndex == 0}]"
          :id="`tab-${tabIndex}`" role="tabpanel"
          :aria-labelledby="`tab-${tabIndex}-nav`">
          <!-- sections -->
          {{tab.title}}
        </div>
      </div>
      <!-- temp -->
      <div class="row">
        <div class="col-2 form-group mt-4">
          <label class="active" for="exampleInputNumber">My Input</label>
          <input placeholder="" type="number" class="form-control" id="exampleInputNumber" :value="get('value', this.model)" @change="onInput('value', $event.target.value ? Number($event.target.value) : null)">
        </div>
        <div class="col-2 form-group mt-4">
          <label class="active" for="exampleInputNumber">My Input</label>
          <input placeholder=" " type="number" class="form-control" id="exampleInputNumber" :value="get('value2', this.model)" @change="onInput('value2', $event.target.value ? Number($event.target.value) : null)">
        </div>
      </div>
      <!-- buttons -->
      <div class="row">
        <div class="col-1">
          <button type="button" class="btn btn-primary" @click="onAction('submit')">Submit</button>
        </div>
        <div class="col-1">
          <button type="button" class="btn btn-primary" @click="onAction('save')">Save</button>
        </div>
      </div>
      <!-- footer -->
      <div
        v-if="definition.footer"
        class="row"
        v-html="render(definition.footer, model)">
      </div>
    </div>
    <!-- debug -->
    <p><b>Model:</b><pre>{{model}}</pre></p>
    <p><b>Definition:</b><pre>{{definition}}</pre></p>
  </div>
</template>

<script>
import Utils from '@/mixins/utils.js'

export default {
  name: 'FormRenderer',
  mixins: [Utils],
  components: {},
  props: {
    identifier: {
      type: String,
      required: true
    },
    definition: {
      type: Object,
      required: true
    },
    value: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      model: this.value || {}
    }
  },
  methods: {
    onInput: function (key, value) {
      this.set(key, value, this.model)
      this.$emit('event', this.identifier, 'input', this.model)
    },
    onAction: function (name) {
      this.$emit('event', this.identifier, name, this.model)
    }
  }
}
</script>

<style scoped>
.ks-form {
  margin: 10px;
}
</style>
