Vue.component('simplemodal', {

    props: ['title','size','buttons','hideHeader','hideFooter','hideX'],
    watch: {
        value: function (value) {
        }
    },
    destroyed: function() {
    },
    data: function() {
        return {
            hasOk: (this.buttons||'').split(/[|,]/).indexOf("ok")>=0,
            hasClose: (this.buttons||'').split(/[|,]/).indexOf("close")>=0,
            hasClear: (this.buttons||'').split(/[|,]/).indexOf("clear")>=0,
            callback: null,
            tick : 0
        }
    },
    template: `
        <div v-on:click1="hide()"class="modal backdrop" role="dialog" >
            <div class="modal-dialog"  @click.stop="return false" style="margin:47px 40px 0px 40px !important" role="document">
                <div class="modal-content" :key="tick" >
                    <div v-if="hideHeader!='true'" style="padding: 0.8rem;" class="modal-header">
                        <slot name="header">
                            <h5 v-if="title" >{{title}}</h5>
                        </slot>
                        <button v-if="hideX!='true'"  type="button" class="close" v-on:click="hide()">
                          <span aria-hidden="true">&times</span>
                        </button>
                     </div>
                     <div class="modal-body" style="padding: 0.8rem;">
                        <slot></slot> 
                     </div>
                     <div v-if="hideFooter!='true'" style="padding: 0.8rem;" class="modal-footer">
                        <slot name="footer">
                            <button v-if="hasClose" type="button" v-on:click="modalclose()" class="modal-close btn btn-secondary" data-dismiss="modal">Chiudi</button>
                            <button v-if="hasOk" type="button" v-on:click="modalok()" class="modal-ok btn btn-primary">OK</button>
                            <button v-if="hasClear" type="button" v-on:click="modalclear()" class="modal-clear btn btn-info">Pulisci</button>
                        </slot>                        
                     </div>
                </div>
            </div>
        </div>`,
    mounted: function(){

    },
    methods:{
        show: function(callback){

            this.tick++;

            this.callback = callback;
            this.$parent.$forceUpdate();

            $(this.$el).find(".modal-dialog").removeClass("modal-xl");
            $(this.$el).find(".modal-dialog").addClass("modal-"+(this.size||'xl'));
            $(this.$el).show();


            //$(this.$el).modal({backdrop: 'static', keyboard: false})

        },
        hide: function(){
            $(this.$el).hide();

        },
        modalok: function(){
            this.$emit('ok', {
                modal: $(this.$el)
            });
            if (this.callback!=null){
                this.callback({
                    modal: $(this.$el)
                });
            }
            this.hide();
        },
        modalclose: function(){
            this.$emit('close', {
                modal: $(this.$el)
            });
            this.hide();
        },
        modalclear: function(){
            this.$emit('clear', {
                modal: $(this.$el)
            });
        }
    }
})