Vue.component('multi-upload', {
    props: ['addButton', 'maxFileBytes', 'muLabel', 'namePrefix', 'nameField', 'labelPrefix','label','muRequired'],
    data: function () {
        return {
            prefix: null,
            tipologie: null,
            required : false,
            name : null,
            placeholder : null,
            multiple: false,
            files:[]
        }
    },
    template: `
        <div class="my-1 form-value-target" >
            <file ref="file" flatten="false" :maxLength="maxFileBytes" :required="required" :name="name" :placeholder="placeholder" v-model="files" :multiple="multiple"  >  
                <template slot-scope="{ file, remove, index }">
                    <input :name=" (multiple? prefix+index : prefix) +'.'+name" class="mu-input mu-input-hidden" type="hidden" :value="serviceUrl(file.url)" />
                    <input :name=" (multiple? prefix+index : prefix) +'.fileId'" class="mu-input mu-input-hidden" type="hidden" :value="fileId(file.url)" />
                    
                    <a :href="file.url">{{ file.name }}  {{bytesToSize(file.size) ? '('+bytesToSize(file.size)+')' : ''}}</a>
                    <button @click="remove(index)" class="p-0 pb-1 btn btn-link" data-toggle="tooltip" data-placement="top" title="Elimina"><i class="fas fa-times text-danger"></i></button>
                    <select v-if="tipologie!=null && tipologie.length>0" :name="(multiple? prefix+index : prefix)+'.TYPE_ID'">
                        <option v-for="tipologia in tipologie" value="tipologia">{{tipologia}}</option>
                    </select>
                </template>
            </file>
            <slot></slot>
        </div>
    `,
    methods: {
        bytesToSize: function (bytes) {
            return bytesToSize(bytes);
        },
        serviceUrl: function(url) {
            return serviceUrl(url);
        },
        fileId: function(url) {
            return (url||"").substring(url.indexOf('/files')+7).replace("/","$");
        }
    },
    mounted: function(){
        var mus = $(this.$el).find('.mu-record');

        var self = this;
        this.$el.setValue = function (draft){

            var getFile = function(file) {
                var url = desktopUrl(file[self.name]);
                return {
                    url: url,
                    name: url.substring(url.lastIndexOf('/') + 1),
                    TYPE_ID: file.TYPE_ID
                }
            }

            if (self.multiple){
                for( field in draft ){
                    var m = field.match(new RegExp(self.prefix+"(\\d+)"));
                    if (m){
                        self.files[Number(m[1])] = getFile(draft[field]);
                        self.$refs.file.files = self.files;
                    }
                }
            } else if (draft[self.prefix]) {
                self.files = [getFile(draft[self.prefix])];
            }

            if (self.files.length>0)
                self.$refs.file.files = self.files;

        }

        var _map = typeof map == "undefined" ? null : map;

        //mu-label="Documento principale" mu-add-button="false" mu-prefix="PRINCIPALE" mu-name="filePath" mu-required="true"
        //add-button="true" mu-label="Documento principale" name-prefix="ALLEGATO" name-field="filePath" label-prefix="" label="Allegati"></multi-upload

        if (mus.length>0){
            var muLabel = mus.attr("mu-label");
            var muAddButton = mus.attr("mu-add-button");
            var muPrefix = mus.attr("mu-prefix");
            var muName = mus.attr("mu-name");
            var muRequired = mus.attr("mu-required");
            this.placeholder = muLabel;
            this.multiple = (muAddButton == 'true');
            this.prefix = muPrefix;
            this.tipologie = (_map||{})[this.prefix];
            this.name = muName;
            this.required = (muRequired == 'true');
        } else {
            this.placeholder = this.label;
            this.multiple = (this.addButton == 'true');
            this.prefix = this.namePrefix;
            this.tipologie = (_map||{})[this.prefix];
            this.name = this.nameField;
            this.required = (this.muRequired == 'true');
        }

        console.log(1);
    }
})