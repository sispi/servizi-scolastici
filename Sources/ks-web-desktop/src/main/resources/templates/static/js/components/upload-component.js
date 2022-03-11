Vue.component('upload-component', {
    template: `<div style="border-radius: 7px; padding: 16px 5px 1px 16px;" @dragleave="fileDragOut" @drop="handleFileInput" @dragover="fileDragIn" v-bind:style="{ 'background-color': color }">    
        <div class="file-wrapper">
            <input type="file" :required="required" id="file" ref="file" v-bind:multiple="multiple" @change="handleFileInput" >
            <i class="fas fa-cloud-upload-alt"></i> {{placeholder || 'Clicca qui o trascina per aggiungere un file'}}
        </div>
        <ul>
            <li class="listaUpload" v-for="(file, index) in files">
                <input :name="name" class="mu-input mu-input-hidden" type="hidden" :value="file.url" />
                <div>
                    <a :href="file.url">{{ file.name }} ({{bytesToSize(file.size)}})</a>
                    <button @click="removeFile(index)" class="btn btn-link" data-toggle="tooltip" data-placement="top" title="Elimina"><i class="fas fa-times red"></i></button>
                </div>
                <div > <!-- visualizzo radio button solo per il primo file -->
                    <div v-if="( index==0 ? (optionFirst||[]) : (options||[]) ).length>0" class="form-check form-check-inline radioUpload" >
                      <template v-for="option in ( index==0 ? (optionFirst||[]) : (options||[]) )">
                      <input class="form-check-input" type="radio" v-model="uploadType[index]" :value="option" v-if="option" >
                      <label class="form-check-label mrg-dx">{{option}}</label>
                      </template>
                    </div>
                </div>
            </li>
        </ul>
        <button v-if="!hideButton" @click="submitFiles()" id="btn-uploadFile" type="submit" class="uploadFile btn btn-primary col-md-3">Conferma</button>
      </div>`
    ,
    props: ['multiple','options','option-first','placeholder','name','required','hide-button'],
    data: function () {
        return {
            name: name || 'file-input',
            files: [],
            color: '#f7f7f7',
            uploadType: [],

        };
    },
    mounted: function() {

    },
    methods: {
        bytesToSize: function (bytes) {
            var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
            if (bytes == 0) return '0 Byte';
            var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
            return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
        },
        handleFileInput(e) {
            e.preventDefault();
            this.color="#f7f7f7";
            if(!this.multiple){
                this.files = [];
                this.uploadType = [];
            }
            let files = (e.dataTransfer||e.target).files;
            if(!files)
                return false;

            var newFiles = [];
            // converto FileList in array
            ([...files]).forEach(f => {
                newFiles.push(f);
                if(this.uploadType.length==0){
                    if(!this.optionFirst){
                        this.uploadType.push("");
                    } else {
                        this.uploadType.push(this.optionFirst[0]);
                    }
                } else {
                    if(!this.options){
                        this.uploadType.push("");
                    } else {
                        this.uploadType.push(this.options[0]);
                    }
                }
                if(this.files){
                    $(this.$el).find('.uploadFile').show();
                }
            });
            this.uploadFiles(newFiles);
            return false;
        },
        removeFile(fileKey){
            this.files.splice(fileKey, 1);
            this.uploadType.splice(fileKey, 1);
            if(this.files.length==0){
                $(this.$el).find('.uploadFile').hide();
            }
        },
        fileDragIn(){
            this.color="#e5e5e5"
        },
        fileDragOut(){
            this.color="#f7f7f7"
        },
        uploadFiles: function(newFiles) {
            var fileClient = new FileClient();
            var self = this;
            fileClient.createMulti(newFiles,(data)=> {
                var index = 0;
                for (key in data) {
                    var f = {
                        name: data[key],
                        url: "/docer/v1/files/"+key,
                        size: newFiles[index++].size,
                    }
                    self.files.push(f);
                }
            });

        },
        submitFiles: function() {
            for (idx in this.files) {
                this.files[idx].option = this.uploadType[idx];
            }
            var response = {files:this.files}
            this.$emit('uploaded', response);
        },

    }
})