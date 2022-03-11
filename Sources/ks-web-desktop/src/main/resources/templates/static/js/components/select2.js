Vue.component('select2', {
    props: {
        'id': String,

        'multiple':{
            type: String,
            default: "false"
        },
        'placeholder':{
            type: String
        },
        'allowClear':{
            type: String,
            default: "false"
        },
        'tags':{
            type: String,
            default: "false"
        },
        'minimumInputLength':{
            type: String,
            default: "1"
        },
        'maximumSelectionLength':{
            type: String,
            default: "10"
        },
        'closeOnSelect' :{
            type: String,
            default: "true"
        },
        'value' : null,
        'label' : null,
        'allowEdit' : null,
        'url' : null,
        'missingUrl' : null,
        'data' : null,
        'processResults' : {
            type: Function
        }
    },
    watch: {
        url: function(newVal, oldVal) { // watch it
            this.changedUrl(newVal, oldVal);
        },

        value: function (newVal, oldVal) {
            this.changedValue(newVal, oldVal);
        }
    },
    destroyed: function() {
        var select2 = $('#'+this.id);
        select2.off().select2("destroy");
    },
    data: function() {
        return {
            config: null,
            isMap: false,
            hasOptions: false
        }
    },
    template: `<select :id="id" style="width: 100%"><slot></slot></select>`,

    methods: {
        changedValue: function(newVal, oldVal) {

            var select2 = $(this.$el);

            var values = [];

            if (this.value==null){

            } else if (this.value.push){
                values = this.value;
            } else {
                values = [this.value];
            }

            if (values[0]!=null && typeof values[0] == "object"){
                this.isMap = true;
                if (!values[0].id){
                    this.value = null;
                    values = [];
                }
            }

            if (values.length>0){

                var labels;

                if (!this.value.push){
                    if (this.isMap){
                        values = [this.value.id];
                        labels = [this.value.text];
                    } else {
                        values = [this.value||" "];
                        labels = [this.label];
                    }
                } else {
                    values = [];
                    labels = [];
                    var defLabels = this.label || [];
                    defLabels = defLabels.push ? defLabels : [defLabels];
                    var cnt = this.value.push ? this.value : [this.value];
                    for ( x in cnt ){
                        var el = cnt[x];
                        if (this.isMap){
                            values.push(el.id);
                            labels.push(el.text);
                        } else {
                            values.push(el||" ");
                            labels.push( (defLabels)[x]);
                        }
                    }
                }
            }

            var missing = {};

            //if (!this.hasOptions)
            //    select2.empty();
            for( x in values ){
                if (select2.find("option[value='" + values[x] + "']").length==0) {
                    if (labels[x]==null){
                        labels[x] = values[x];
                        missing[x] = values[x];
                    }
                    select2.append(new Option(labels[x],values[x], true, true));
                }
            }

            var missingLabels = Object.values(missing);

            var event = jQuery.Event( "change" )
            event.internal = true;

            if (missingLabels.length>0 && this.missingUrl!=null ){
                var _url = this.missingUrl;
                var params = {}
                if (typeof _url == "function"){
                    _url = _url(missingLabels);
                } else {
                    _url = _url.replace("...",missingLabels.join(" "));
                }
                $.get( _url, function(response) {
                   var data = response.data;
                   var flag = false;
                   for( x in data ){
                       var idx = values.indexOf(data[x].id||data[x].sid);
                       if (missing[idx]){
                           var opt = select2.find("option[value='" + values[idx] + "']");
                           if (opt.length>0 && data[x].text){
                               //$(opt).text(data[x].text);
                               flag = true;
                               $(opt).remove();
                               select2.append(new Option(data[x].text||data[x].id||data[x].sid,values[idx], true, true));
                           }
                       }
                   }
                   if (flag)
                        select2.trigger(event);
                });
            }

            if (this.multiple == 'true')
                select2.val(values);
            else
                select2.val(values[0]);

            select2.trigger(event);
        },

        changedUrl: function (newVal, oldVal) {
            if (!newVal){
                this.config.ajax = null;
                $('#'+this.id).select2(this.config);
                return null;
            }

            var self = this;
            var _query = this.query;
            var _url = newVal;
            var _data = this.data;
            var _processResults = this.processResults;
            var _query = "q=";
            var authUrl = null;

            if (typeof _url == "string"){

                _url = _url.replace("...","");

                var idx = _url.lastIndexOf("&");
                if (idx==-1)
                    idx = _url.lastIndexOf("?");
                if (idx>=0){
                    var _url0 = _url.substring(idx+1);

                    _url = _url.substring(0,idx);

                    _data = function(params) {
                        var term = params.term || "__NULL__";
                        if (term.push){
                            return _url0 + "("+term.join(" ")+")";
                        } else
                            return _url0 + (term+"*").replace("**","*");
                    }
                }
                authUrl = _url;
            } else if (typeof _url == "function") {
                authUrl = _url({});
            }

            var headers = {};
            if (authUrl && authUrl.indexOf("$header.")!=-1){
                var params = new URLSearchParams(authUrl.split("?")[1]);
                for( x of params.keys() ) {
                    if (x.startsWith("$header.")){
                        headers[x.substring("$header.".length)] = params.get(x);
                    }
                }
            }

            if (_data==null){
                _data = function(params) {
                    return "";
                }
            }

            if (_processResults==null){
                _processResults = function (data) {
                    var data= $.map(data.data, function (item) {
                        var id = item.id || item.sid || (""+item);
                        return {
                            id :id,
                            text: item.text || item.name || id
                        };
                    });

                    data.results = data;
                    return data;
                }
            }

            this.config.ajax = {
                dataType: 'json',
                url: _url,
                data: _data,
                processResults: _processResults
            }

            if (Object.keys(headers).length>0)
                this.config.ajax.headers = headers;

            $('#'+this.id).select2(this.config);

            return _url;
        }
    },

    mounted: function(){

        var self = this;

        if (!this.id){
            this.id = 'select2_'+new Date().getTime();
            $(this.$el).attr("id",this.id);
        }

        function formatResult (state,arg) {
            if (!state.id) {
                return state.text;
            }
            if (state.text==null) {
                return ""+state;
            }

            if (state.id!=state.text && !(state.text||"").endsWith(")")){
                return state.text+" ("+state.id+")";
            } else {
                return state.text;
            }
        }

        window.changeTag = function (id){
            prompt("descrizione", function(obj) {
                if (self.value.push){

                } else {
                    self.value.text = obj.value;
                }
            });
        }

        function formatSelection (state) {
            if (!state.id) {
                return state.text || " ";
            }
            var display;
            if (state.id!=state.text && !state.text.endsWith(")")){
                display = state.text+" ("+state.id+")";
            } else {
                display = state.text;
            }
            if (self.allowEdit!='true')
                return display;
            return $("<span onclick='changeTag(\""+state.id+"\")'>"+display+"</span>");
        };

        if ($(this.$el).children().length>0)
            this.hasOptions = true;
        
        var minimumInputLength = 0;
        var tags = (this.tags=="true");
        if (!tags)
            minimumInputLength = Number(this.minimumInputLength);

        var config = {
            placeholder: this.placeholder || "",
            allowClear: this.allowClear=="true",
            closeOnSelect: this.closeOnSelect=="true",
            tags: tags,
            minimumInputLength: minimumInputLength,
            maximumSelectionLength: Number(this.maximumSelectionLength),
            multiple: this.multiple==="true",
            templateSelection: formatSelection,
            templateResult: formatResult
            //width: 'resolve',
        };

        this.config = config;

        var p = $(this.$el).closest(".modal");
        if (p.length>0)
            config.dropdownParent = p;

        this.changedUrl(this.url,null);

        $(this.$el).select2(config);

        this.changedValue(this.value,null);

        var select2 = $(this.$el);

        select2.trigger("change");

        select2.on("change", function(evt,arg) {

            if (evt.internal)
                return;

                var value = $(this).val();

                var values;

                if (value){
                    if (!value.push)
                        values = [value];
                    else
                        values = value;
                } else {
                    values = [];
                }

                var extValues=[];
                var extIds=[];
                if (self.isMap){

                    for (x in values){
                        var value = values[x];
                        var text = values[x];
                        var option = $(this).find("option[value='" + value + "']");

                        if (option.length){
                            text = option.text();
                        }

                        extValues.push({
                            id: value,
                            text: text
                        })
                        extIds.push(value);
                    }
                } else {
                    extValues = values;
                    extIds = values;
                }

                console.log(extIds,extValues);

                self.$emit('changed', {
                    values: extValues,
                    value : extValues[0],
                    ids: extIds,
                    id: extIds[0]
                });

                var evt;
                if (self.multiple=="true" && Number(self.maximumSelectionLength||"2")>1){
                    evt = extValues;
                } else {
                    evt = extValues[0];
                }

                self.$emit('input', evt);

            });


        /*$('#'+this.id).on('change.select2', function (e) {


        });*/



    }
})