Vue.component('ks-section', {
    props: ['schema','form','tabIndex','sectionIndex','selected'],
    template: `

    <div class="row section-editor" :class="{ selected: selected }"  >
    
        <div class="col-10 section-area" @mousemove="dragMove" :key="tick" @drop="onDrop" @dblclick="dblclick" @mousedown="select" :data-tab="tabIndex" :data-section="sectionIndex" :key="tick" v-sortable="{fallbackOnBody: true,swapThreshold: 0.65,dragoverBubble:false,handle:'.bi-arrows-move', onUpdate: onUpdate, group: 'list' , onAdd: onAdd }">
            <span class="row" v-for="(item,$index) in getSection().elements">
            <span  :data-index="$index" :id="'ctrl-'+id+'-'+$index" style="" :class="'ctrl offset-'+(item.offset??0)+' col-'+(item.width||12)"  >
    
                <span class="resize-corner" @dblclick="fill($index)" @mousedown.prevent="dragStart($index,$event.pageX,'ctrl-'+id+'-'+$index)" >{{item.width}}</span>
                <span class="offset-corner" @mousedown.prevent="dragStartOffset($index,$event.pageX,'ctrl-'+id+'-'+$index)" >{{item.offset??0}}</span>
    
                <i class="bi bi-arrows-move tb-icon" style="float:left;margin-left:-16px;cursor: move"></i>
                <i class="bi bi-x-circle-fill text-danger tb-icon" style="float:right;cursor:pointer;margin-right: -12px;margin-top: -3px;font-size: 14px;" @click="remove($index)" ></i>
                <i class="bi bi-pencil tb-icon" style="float:right;cursor:pointer;margin-right: -0px;margin-top: -3px;font-size: 14px;" @click.prevent="edit($index)" ></i>
    
                <input title="break" type="checkbox" v-model="item.break" style="float:right;margin-right:-32px;cursor: pointer;margin-top:21px" />
    
                <div v-if="item.type=='text'" class="form-group" >
                    <label>{{item.label}}</label>
                    <input disabled class="form-control" :value="item.output||item.input||'unbound'" style="height: 20px" type="text" />
                </div>
    
                <div v-if="item.type=='date'" class="form-group" >
                    <label>{{item.label}}</label>
                    <input disabled class="form-control" :value="item.output||item.input||'unbound'" style="height: 20px" :type="item.subtype=='date-time'?'datetime-local':item.subtype" />
                </div>
    
                <div v-if="item.type=='number'" class="form-group" >
                    <label>{{item.label}}</label>
                    <input disabled class="form-control" :value="item.output||item.input||'unbound'" style="height: 20px" type="text" />
                </div>
    
                <div v-if="item.type=='checkbox'" class="form-group" >
                    <span class="form-check mr-4">
                        <input disabled class="form-check-input" type="checkbox" />
                        <label class="form-check-label">{{item.label}}</label>
                    </span>
                    ({{item.output||item.input||'unbound'}})
                </div>
                
                <div class="placeholder" style="position: absolute; left: 50%; top:15%">
                    <div style="position: relative; left: -50%;">
                      {{item.type}}
                    </div>
                </div>
    
                <div v-if="item.type=='hidden'" class="form-group" >
                    {{item.output||item.input||'unbound'}}
                </div>
    
                <div v-if="item.type=='header'" class="form-group" >
                    
                    <h5>{{item.label||'undefined'}}</h5>
                </div>
    
                <div v-if="item.type=='html'" class="form-group" >
                    {{item.value||'undefined'}}
                </div>
    
                <div v-if="item.type=='checkbox-group'" class="form-group" >
                    <label>{{item.label}}</label>
                    <div v-if="item.source=='url'">
                        <span class="form-check mr-4">
                            <input disabled class="form-check-input" type="checkbox" />
                            <label class="form-check-label">url: {{item.url}}</label>
                        </span>
                        <span class="form-check mr-4">
                            <input disabled class="form-check-input" type="checkbox" />
                            <label class="form-check-label">processor: {{item.process}}</label>
                        </span>
                    </div>
                    <div v-else >
                        <span v-for="option in item.values" class="form-check mr-4">
                            <input disabled class="form-check-input" type="checkbox" />
                            <label class="form-check-label">{{option.text||option.label}} ({{option.value||option.id}})</label>
                        </span>
                    </div>
                        
                    
                </div>
    
                <div v-if="item.type=='radio-group'" class="form-group" >
                    <label>{{item.label}}</label>
                    <div v-if="item.source=='url'">
                        <span class="form-check mr-4">
                            <input disabled class="form-check-input" type="radio" />
                            <label class="form-check-label">url: {{item.url}}</label>
                        </span>
                        <span class="form-check mr-4">
                            <input disabled class="form-check-input" type="radio" />
                            <label class="form-check-label">processor: {{item.process}}</label>
                        </span>
                    </div>
                    <div v-else >
                        <span v-for="option in item.values" class="form-check mr-4">
                            <input disabled class="form-check-input" type="radio" />
                            <label class="form-check-label">{{option.text||option.label}} ({{option.value||option.id}})</label>
                        </span>
                    </div>
                </div>
    
                <div v-if="item.type=='select'" class="form-group" >
                    <label>{{item.label}}</label>
                    <select2 disabled="true" :value="item.output||item.input||'unbound'" :multiple="(item.subtype||item.mode)=='multiple'?'true':null" :placeholder="item.url" minimum-input-length="0">
                        <template v-if="item.source=='values'">
                            <option v-for="option in item.values">{{option.text||option.label}} ({{option.value||option.id}})</option>
                        </template>
                    </select2>
                </div>
    
                <div v-if="item.type=='file'" class="form-group" >
                    <label>{{item.label}}</label>
                    <file :placeholder="item.placeholder" disabled="true "></file>
                </div>
    
                <div v-if="item.type=='button'" class="form-group" >
                    <button class="btn btn-sm btn-primary disabled" disabled >{{item.label}}</button>
                </div>
                
                <div v-if="item.type=='resource'" class="form-group" >
                     {{item.url}}->{{item.output||item.input||'unbound'}}
                </div>
                
                <div class=" pl-1">{{item.description}}</div>
                <div class="invalid-feedback pl-1">{{item.invalidFeedback_}}</div>
    
            </span>
            <span v-if="item.break && rightspace($index)>0" style="text-align: center" class="pt-3 font-weight-bold text-info"  :class="'col-'+rightspace($index)" >BREAK {{rightspace($index)}}</span>
            </span>
    
        </div>
    
        <div class="col-2 toolbar " v-if="selected" >
            <span class="m-0 p-0" id="toolbar" v-sortable="{ sort:false, group: {name:'list',pull:'clone',put:false,handle: '.bi-arrows-move' } }">
                <span :data-tool="tool.type" class="ml-0 mr-0 my-0 px-1 py-1 list-group-item" v-for="tool in toolbar">
                    <i @dblclick="addcontrol(tool.type)" class="bi bi-arrows-move tb-icon" style="cursor: move"></i>
                    &nbsp;<span disabled style="display: inline-block;" >{{tool.type}}</span>
                </span>
            </span>
        </div>
            
        <simplemodal ref="options" buttons="ok|close">
            <ks-form :key="tick" v-if="current && current.context && current.type" v-model="current" template="#options" ></ks-form>
        </simplemodal>

        <simplemodal ref="confirm" buttons="ok|close">
            <span>Elemento copiato in clipboard. Vuoi rimuoverlo dalla sezione?</span>
        </simplemodal>
    </div>

    `,
    data: function() {
        return {
            toolbar: {
                resource : {
                    value: 'resource',
                    type: 'resource',
                    width: 12,
                    break: true
                },
                header : {
                    value: 'header',
                    type: 'header',
                    subtype: "h3",
                    width: 12,
                    required: false,
                    break: true,
                    coll: false,
                    types: ['string']
                },
                html : {
                    value: 'html',
                    type: 'html',
                    width: 12,
                    required: false,
                    break: true,
                    coll: false,
                    types: ['string']
                },
                hidden : {
                    type: 'hidden',
                    width: 12,
                    required: false,
                    break: true
                },
                text : {
                    label: 'Text field',
                    type: 'text',
                    required: false,
                    subtype: "text",
                    break: false,
                    width: 4,
                    coll: false,
                    types: ['string']
                },
                number : {
                    label: "Number",
                    type: 'number',
                    width: 4,
                    min: 0,
                    max: null,
                    step: 1,
                    required: false,
                    break: false,
                    coll: false,
                    types: ['number']
                },
                date: {
                    label: 'Date/Time',
                    type: 'date',
                    subtype: "date",
                    width: 4,
                    required: false,
                    break: false,
                    coll: false,
                    types: ['date','date-time','time']
                },
                checkbox : {
                    label: 'checkbox',
                    type: 'checkbox',
                    required: false,
                    break: false,
                    width: 4,
                    coll: false,
                    types: ['boolean']
                },
                "checkbox-group" : {
                    label: 'Checkbox group',
                    type: 'checkbox-group',
                    required: false,
                    break: false,
                    width: 4,
                    source: "values",
                    values: [{"id": "id0", "text": "text0"}, {"id": "id1", "text": "text1"}],
                    coll: true,
                    types: ['string','number']
                },
                "radio-group": {
                    type: 'radio-group',
                    label: 'Radio group',
                    width: 4,
                    required: false,
                    break: false,
                    source: "values",
                    values: [{"id": "id0", "text": "text0"}, {"id": "id1", "text": "text1"}],
                    coll: true,
                    types: ['string','number']
                },
                select: {
                    label: 'Select',
                    type: 'select',
                    width: 4,
                    required: false,
                    break: false,
                    source: "values",
                    values: [{"id": "id0", "text": "text0"}, {"id": "id1", "text": "text1"}]
                },
                file: {
                    label: 'File',
                    type: 'file',
                    width: 4,
                    required: false,
                    break: false,
                    types: ["file"]
                },
                button: {
                    label: 'Button',
                    type: 'button',
                    width: 4,
                    required: false,
                    break: false
                }

            },
            resize : false,
            drag : null,
            current : null,
            id : null,
            tick : 0,
            current: null
        }
    },

    mounted: function() {
        var selected = this.$parent.selected$section;
        if (selected==null){
            Vue.set(this.$parent,"selected$section", 'sec'+this.tabIndex+'-'+this.sectionIndex);
        }
    },

    methods : {

        dblclick: function() {
            var clipped = this.$parent.clipped;
            if (clipped){
                this.getSection().elements.push(this.clone(clipped));
            }
        },

        select: function(){
            this.$emit('select',this.id);
        },

        edit: function(index) {

            var item = this.getSection().elements[index];
            item.break_ = item.break;
            item.class_ = item.class;
            delete item.context;
            this.current = JSON.parse(JSON.stringify(item));
            this.current.context = this;

            var self = this;
            var ok = function() {
                self.current.break = self.current.break_;
                self.current.class = self.current.class_;
                //delete self.current.break_;
                //delete self.current.class_;
                delete self.current.context;

                var curr = JSON.parse(JSON.stringify(self.current));
                curr.context = self;
                Vue.set( self.getSection().elements, index , curr);
                self.tick++;
                self.$forceUpdate();
            }

            var modal = this.$refs.options;
            Vue.nextTick( () => { modal.show(ok); } );
        },

        remove: function(index) {
            var self = this;
            this.$parent.clipped = this.clone(this.getSection().elements[index]) ;
            this.$refs.confirm.show( () => self.getSection().elements.splice(index,1) );
        } ,

        rightspace: function(idx0){
            var x = 0;
            var els = this.getSection().elements;
            for( idx in els ){
                var el = els[idx];
                var cols = (el.width||12)+(el.offset||0);

                if ( (x + cols) > 12 || (els[idx-1]||{}).break){
                    x = cols;
                } else {
                    x = x + cols;
                }

                if (idx==idx0)
                    return 12-x;
            }
        },

        freespace : function(idx0){
            var x = 0;
            var els = this.getSection().elements;
            var found = false;
            for( idx in els ){
                var el = els[idx];
                var cols = (el.width||12)+(el.offset||0);

                if ( (x + cols) > 12 || (els[idx-1]||{}).break){
                    if (found)
                        break;

                    x = cols;
                } else {
                    x = x + cols;
                }

                if (idx==idx0)
                    found = true;
            }
            return 12-x;
        },

        fill: function(idx){
            this.getSection().elements[idx].width = this.getSection().elements[idx].width + this.freespace(idx);
        },

        dragStart: function(index,x0,ctrl) {
            this.drag = {
                offset: false,
                index: index,
                x0: x0,
                w0: $("#"+ctrl)[0].clientWidth,
                cols0: this.getSection().elements[index].width || 12,
                w: null
            }
        },

        dragStartOffset: function(index,x0,ctrl) {
            this.drag = {
                offset: true,
                index: index,
                x0: x0,
                w0: this.$el.clientWidth,
                cols0: this.getSection().elements[index].offset || 0,
                w: null,
                maxoffset: this.freespace(index)
            }
        },

        dragMove: function(e) {
            if (this.drag){
                if (e.buttons==0){
                    this.drag = null;
                } else {
                    with(this.drag){
                        var ratio =  Math.floor(12 * (e.pageX-x0)/w0);
                        if (offset){
                            this.getSection().elements[index].offset = Math.max(0,Math.min(maxoffset,cols0+ratio));
                            this.tick++;
                        } else {
                            this.getSection().elements[index].width = Math.max(2,Math.min(12,Math.floor( cols0 * (e.pageX-x0+w0)/w0 )));
                            this.tick++;
                        }
                    }
                }
            }
        },

        /*dragEnd: function() {
            this.drag = null;
        },*/

        onUpdate: function (event) {
            //var section = $(event.target).data("section");
            var oldIndex = $(event.item).data("index");
            var newIndex = event.newIndex;

            //console.log(section,oldIndex,newIndex);
            this.getSection().elements.splice(newIndex, 0, this.getSection().elements.splice(oldIndex, 1)[0]);
            this.tick++;
        },

        clone: function(type){
            return (typeof type == "string") ?
                JSON.parse(JSON.stringify(this.toolbar[type])) :
                JSON.parse(JSON.stringify(type));
        },

        addcontrol: function(type) {
            this.getSection().elements.push( this.clone(type) );
        },

        onAdd: function (event) {
            //event.preventDefault();
            var tool = $(event.item).data("tool");

            if (tool){
                this.getSection().elements.splice(event.newIndex, 0, this.clone(tool) );
            } else if (this.form) {
                var index = $(event.item).data("index");
                var sectionIndex = $(event.from).data("section");
                var tabIndex = $(event.from).data("tab");

                var old = this.form.tabs[tabIndex].sections[sectionIndex].elements.splice(index, 1)[0];
                this.getSection().elements.splice(event.newIndex, 0, old );
            }

            this.tick++;
        },

        onDrop: function (event) {
            //event.preventDefault();
            let x = event.dataTransfer||e.target;
            return;
        },

        isPath: function(string) {
            return string.match(/^[a-zA-Z_][a-zA-Z0-9_]*(\[\d+\])?(\.[a-zA-Z_][a-zA-Z0-9_]*(\[\d+\])?)*$/) != null;
        },

        accessors : function  (path) {
            return path.replace(/\[(\d+)\]/g, '.$1').replace(/^\./, '').split('.').map(a => ({
                key: !isNaN(a) ? Number(a) : a,
                isArray: !isNaN(a)
            }))
        },

        get: function(path,model,defaultValue) {
            const a = this.accessors(path);
            let v = model || this;
            for (let i = 0; i < a.length; i++) {
                if (v != null) {
                    v = v[a[i].key]
                } else {
                    break
                }
            }
            var out = (v == null ? (defaultValue === undefined ? null : defaultValue) : v);
            return out;
        },

        getSchema: function(schema,path) {
            if (path){
                var a = this.accessors(path);
                for (let i = 0; i < a.length; i++) {
                    schema = a[i].isArray ? schema.items : (schema[a[i].key] || (schema.properties||{})[a[i].key]);

                    if (schema==null)
                        break;
                }
            } else if (!schema.properties){
                schema = {
                    properties: schema
                }
            }
            return schema;
        },

        getSection: function(){
            return this.form.tabs[this.tabIndex].sections[this.sectionIndex];
        },

        getInputs: function(type,ctrl){
            var t = this.toolbar[type];
            var mode = ctrl.subtype||ctrl.mode;
            if (mode)
                t.coll = (mode == 'multiple');
            var hints = this.getPaths(this.schema.input,this.getSection().input,t.coll,t.types);

            hints.push({id:"-",text:"(---- altri ----)"});
            hints = hints.concat(this.getPaths(this.schema.input));
            return hints;
        },

        getOutputs: function(type,ctrl){
            var t = this.toolbar[type];
            var mode = ctrl.subtype||ctrl.mode;
            if (mode)
                t.coll = (mode == 'multiple');
            var hints = this.getPaths(this.schema.output,this.getSection().output,t.coll,t.types);

            hints.push({id:"-",text:"(---- altri ----)"});
            hints = hints.concat(this.getPaths(this.schema.output));
            return hints;
        },

        getPaths: function(schema,path,coll,types) {
            schema = this.getSchema(schema,path);
            var c = schema.properties || (schema.items||{}).properties;

            var hints = [];
            if (c){
                for ( x in c){
                    var s = c[x];
                    var isColl = s.type == "array";

                    if (coll!=null && (coll!=isColl))
                        continue;

                    if (isColl)
                        s = s.items;

                    var stype = s.format || (s.enum ? "enum" : s.type);

                    if (types!=null && types.indexOf(stype)==-1 )
                        continue;

                    var p = path ? "$item." + x : x;
                    var t = stype+(isColl?"[]":"");

                    hints.push( { id: p , text: p+" ("+t+")" } );
                }
            }
            return hints;
        },

        getPathHints: function(forPath, forType) {
            var schema = this.schema;
            const self = this
            return (function process (schema, path, hints) {
                const type = self.get('type', schema, 'any')
                const format = type === 'string' ? self.get('format', schema) : null
                const itemsType = type === 'array' ? self.get('items.type', schema) : null
                const itemsFormat = type === 'array' ? self.get('items.format', schema) : null
                const input = self.get('input', schema, true)
                const output = self.get('output', schema, true)
                const _type = `${type}${format ? ':' + format : ''}${itemsType ? '<' + itemsType + (itemsFormat ? ':' + itemsFormat : '') + '>' : ''}`
                const _path = forPath ? path.slice(forPath.length).replace(/^\.|\[0\]\.?/, '') : path
                if (
                    (_path) &&
                    ((!forInput && output) || input) &&
                    (!forPath || path.indexOf(`${forPath}.`) === 0 || path.indexOf(`${forPath}[0]`) === 0) &&
                    (!forType || RegExp(`^(${forType})$`).test(_type))
                ) {
                    hints.push({ id: _path, text: `${_path} (${_type})` })
                }
                if (type === 'object' && schema.properties) {
                    for (const [key, value] of Object.entries(schema.properties)) {
                        process(value, path ? `${path}.${key}` : key, hints)
                    }
                }
                if (type === 'array' && schema.items) {
                    process(schema.items, path ? `${path}[0]` : '[0]', hints)
                }
                return hints
            })(schema, '', [])
        }

        /*

        any
        string
        string:date
        string:time
        string:date-time
        string:email
        string:password
        string:text
        string:html
        string:uri-reference
        number
        boolean
        array
        array<...>
         */
    }
});
