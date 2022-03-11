Vue.component('object-viewer', {
    props: ['object','keys','span'],
    template: `
<div class="row">
  <div v-for="(value,key) in object" :class="'col-'+(span||3)" v-if="keys==null || keys=='' || keys=='*' || keys.split(',').indexOf(key) >= 0" >
  
    <b :for="key+'_label'">{{key}}</b>
    <div v-if="typeof value == 'boolean'" class="text-truncate" :id="key+'_label'">
        {{value?'Si':'No'}}
    </div>
    <div :title="value||''" v-else-if="!(value||'').push" style="display: block" class="text-truncate" :id="key+'_label'">
        {{value||'-'}}
    </div>
    <div v-else-if="(value||'').push" :id="key+'_label'">
        <span class="text-truncate" :title="value||''" style="display: block" v-for="item in value">{{item}}&nbsp;</span>
    </div>
  </div>
</div>
`
})