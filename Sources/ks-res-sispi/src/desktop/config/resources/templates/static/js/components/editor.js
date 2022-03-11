Vue.component('Editor', {
    template: '<div :id="id" style="width: 100%; height: 100%;"><slot></slot></div>',
    props: ['id', 'value', 'content', 'lang', 'theme','readOnly','showGutter', 'rows','min','max' ],
    data () {
        return {
            isJs: false,
            editor: Object,
            beforeContent: '',
            beforeObject: Object,
        }
    },
    watch: {
        'content' (value) {
            /*console.log("watched");
            if (typeof value == 'object'){
                if (this.beforeObject !== value) {
                    value = JSON.stringify(value,null,4);
                    this.editor.setValue(value, 1);
                }
            } else {
                if (this.beforeContent !== value) {
                    this.editor.setValue(value, 1);
                }
            }*/
        }
    },
    mounted () {

        this.id = this.id || 'ace_'+new Date().getTime();
        $(this.$el).attr("id",this.id);

        var lang;
        var theme = this.theme || 'dawn';

        var inner = $(this.$el).html().trim();

        var content = this.value || this.content || inner;

        this.editor = window.ace.edit(this.id);

        if (content!=null && typeof content == 'object'){
            this.isJs = true;
            lang = this.lang || 'json';
            value = JSON.stringify(content,null,4);
        } else {
            lang = this.lang || 'text';
            value = content || '';
        }

        this.editor.setValue(value, 1);
        //this.editor.setOption('lineHeight', "14px");

        //this.editor.setOption('showLineNumbers', false);
        this.editor.setOption('showGutter', this.showGutter=="true");

        var minLines = this.min || this.rows || "4";
        var maxLines = this.max || this.rows || "10";

        this.editor.setOption('minLines',Number(minLines));
        this.editor.setOption('maxLines',Number(maxLines));
        this.editor.setOption('theme','ace/theme/'+theme);

        //var lineHeight = this.editor.renderer.lineHeight;
        if (this.rows){
            //$("#"+this.id).css("min-height", ( (this.editor.renderer.lineHeight||22) * Number(this.rows )) + "px");
        }
        this.editor.resize();

        this.editor.getSession().setMode('ace/mode/'+lang);
        //this.editor.setTheme('ace/theme/'+theme);
        if (this.readOnly)
            this.editor.setReadOnly(true);
        //this.editor.setOptions({minLines: 5});

        this.editor.getSession().on('change', () => {
            var value = this.editor.getValue();
            //this.beforeContent = value;

            if (this.isJs){
                try{
                    var json = JSON.parse(value);
                    //this.beforeObject = JSON.parse(value);
                    //this.content = this.beforeObject;
                    //this.value = this.beforeObject;
                    this.$emit('change-content', json);
                    this.$emit('input', json);
                } catch(e) {
                }
            } else {
                //this.content = value;
                //this.value = value;
                this.$emit('change-content', value);
                this.$emit('input', value);
            }

        })
    }
})