Vue.component('file', {
    template: `

    <div v-bind:style="{ 'background-color': color }" class="file-component p-2 border border-secondary rounded"  @dragleave="dragleave" @dragover="dragover" @drop.prevent="handleFileInput" >
            
            <input style="opacity: 0.0;filter: alpha(opacity=0); width:1px; height:1px; overflow: hidden; position: absolute;" :disabled="disabled" :accept="accept" class="input-file" type="file" :required="required" :id="identity+'_input'" ref="file" v-bind:multiple="multiple" @change="handleFileInput" >
            <label :for="identity+'_input'" role="button" class="w-100 label m-0" @mouseover="dragover" @mouseleave="dragleave">
                <slot name="header">
                    <i :class="icon || 'bi bi-upload'"></i>&nbsp;{{placeholder || 'Clicca qui o trascina per aggiungere un file'}}
                </slot>
            </label>
                
            <div class="list" v-for="(file, index) in files">
                <slot v-bind:file="file" v-bind:index = "index"  v-bind:remove="remove"> 
                    <a :download="file.name" :href="file.url">{{ file.name }}</a>
                    <button @click="remove(index)" class="p-0 pb-1 btn btn-link" data-toggle="tooltip" data-placement="top" title="Elimina"><i class="fas fa-times text-danger"></i></button>              
                </slot>
            </div>
            
            <slot name="footer">
                
            </slot>
    </div>`
    ,
    props: ['url','processor','accept','maxSize','maxLength','disabled','flatten','id','icon','value','multiple','placeholder','required','inBgColor','outBgColor'],
    data: function () {
        return {
            identity : null,
            incolor : this.inBgColor || '#e5e5e5',
            outcolor : this.outBgColor || '#f7f7f7',
            color:null,
            basepath: 'upload/path_'+new Date().getTime(),
            files:[],
            isMV : false,
            isMap: false
        };
    },
    mounted: function() {

        if (!this.id){
            this.identity = 'file_'+(new Date().getTime()+Math.floor(Math.random() * 100000));
        } else {
            this.identity = this.id;
        }
        $(this.$el).attr("id",this.identity);

        this.color = this.outcolor;
        this.isMV = Boolean (this.value && this.value.push) || this.multiple;

        var inp = [];
        if (this.multiple || this.isMV){
            if (this.value!=null && this.value.length > 0){
                inp = this.value;
            }
        } else if (this.value!=null) {
            inp = [this.value];
        }

        if (inp.length==0){
            if (this.flatten==null)
                this.isMap = false;
            else
                this.isMap = (this.flatten!='true');
        } else {
            this.isMap = (typeof inp[0] == 'object');
        }

        for( x in inp ){
            var item = this.isMap ? inp[x] : {url:inp[x]};
            if (item.url){
                if (!item.name){
                    item.name = item.url.substring(item.url.lastIndexOf('/')+1);
                }
                this.files.push(item);
            }
        }
    },
    methods: {

        dragover(event) {
            event.preventDefault();
            this.color=this.incolor;
        },
        dragleave(event) {
            this.color=this.outcolor;
        },

        handleFileInput(e) {
            e.preventDefault();
            this.color=this.outcolor;
            if(!this.multiple){
                this.files = [];
            }
            let files = (e.dataTransfer||e.target).files;
            if(!files)
                return false;

            if (this.maxSize){
                if ( (files.length+(this.files||[]).length) > Number(this.maxSize)){
                    alert("puoi selezionare al massimo "+this.maxSize+" file");
                    return;
                }
            }

            for( x in files){
                if (files[x].size == 0){
                    alert(files[x].name + " è vuoto");
                    return;
                }
                if (this.maxLength){
                    if (files[x].size > Number(this.maxLength)){
                        alert(files[x].name + "("+files[x].size+") supera il limite di "+this.maxLength+" bytes");
                        return;
                    }
                }
            }

            var fileClient = new FileClient(this.url);
            var self = this;
            fileClient.createMulti(files,this.basepath, (data)=> {
                var index = 0;

                if (self.processor)
                    data = self.processor(data);

                if (Object.keys(data).length != files.length){
                    alert("la procedura di upload ha risposto in modo errato");
                    console.log("createMulti:",data);
                    return;
                }

                for (key in data) {
                    var f = {
                        name: data[key],
                        url: "/docer/v1/files/"+key,
                        size: files[index++].size,
                    }
                    self.files.push(f);
                }
                this.emit();

            });
            return false;
        },
        remove(file){

            if (file==null){
                this.files = [];
            } else {
                if (typeof file == "object")
                    file = this.files.indexOf(file);
                this.files.splice(file, 1);
            }

            this.emit();

            return false;
        },
        emit(){

            var out = this.files;
            if (!this.isMap){
                out = [];
                for( x in this.files){
                    out.push(this.files[x].url);
                }
            }

            if (this.multiple || this.isMV)
                this.$emit('input', out);
            else if (this.files[0])
                this.$emit('input', out[0]);
            else
                this.$emit('input', null);
        }
    }
})