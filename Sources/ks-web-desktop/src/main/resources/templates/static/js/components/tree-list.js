Vue.component("tree-list", {
    template:  `

<ul class="tree-list">
    <li v-for="(item, index) in list" :id="'li-'+item.id" :class="(_empty(item)?'empty ':'')+(item.class||'')+ (item.opened?' opened ':' closed ')+ _type(item)"  @click="toggle(item,$event)">
        
        <a @click="click(item,$event)" class="label" :class=" (item.id==value) ? 'selected':''" :title="_name(item)" >{{ _name(item) }}</a>
        
        <tree-list v-if="item.opened" :list="item.list" :url="url" :processResults="processResults"
            @click="click($event.item,$event.event)"
            v-model="value" >
        </tree-list>
    </li>
</ul>
`,
    props: {
        'list': { type: Array , default : [{'id':'','name':''}]},
        'url':null,
        'process-results': null,
        'value': null
    },
    watch: {
        list: function(newVal, oldVal) {
        }
    },
    data: function() {
        return {
        }
    },
    created: function() {

    },
    updated: function() {
        initDOM(this.$el);
    },
    mounted: function() {

    },
    methods: {
        _selected: function(item) {
            return (this.selected==item.id);
        },
        _empty: function(item) {
            if (typeof item.list == 'undefined')
                return null;
            return item.list==null || item.list.length==0;
        },
        _name: function(item) {
            return item.text|| item.name || item.id.split("@")[0];
        },
        _type: function(item) {
            return item.type || item.id.split("@")[1] || 'item';
        },
        toggle: function(item,evt) {

            if (evt.srcElement.tagName!='LI')
                return;

            evt.cancelBubble = true;
            evt.preventDefault();

            var self = this;

            if (typeof item.list == "undefined" && !item.opened){

                var _url = this.url;
                if (_url){
                    if (typeof _url == "function"){
                        var response = _url(item);
                        if (typeof response == "string"){
                            _url = response;
                        } else {
                            try {
                                if (self.processResults)
                                    item.list = self.processResults(response);
                                else
                                    item.list = response;
                                item.opened = true;
                                self.$forceUpdate();

                                self.$emit('toggle', {
                                    item: item,
                                    event: evt
                                });
                            } catch (err) {
                                processError(err);
                            }
                            return;
                        }

                    } else {
                        _url = _url.replace("...","") + encodeURIComponent(item.id);
                    }

                    axios.get(_url)
                        .then(function(response){
                            if (self.processResults)
                                item.list = self.processResults(response.data);
                            else
                                item.list = response.data.data;
                            item.opened = true;
                            self.$forceUpdate();

                            /*self.$emit('toggle', {
                                item: item,
                                event: evt
                            });*/
                        })
                        .catch(function(err){
                            processError(err);
                        });

                } else {
                    item.list = null;
                    item.opened = true;
                    self.$forceUpdate();

                    /*this.$emit('toggle', {
                        item: item,
                        event: evt
                    });*/
                }
            } else {
                item.opened = !item.opened;
                self.$forceUpdate();

                /*this.$emit('toggle', {
                    item: item,
                    event: evt
                });*/
            }

            return false;
        },
        click: function(item,evt){
            this.value = item.id;
            this.$emit('click', {
                item: item,
                event: evt
            });
            this.$emit('input', this.value);

        }
    }
});

