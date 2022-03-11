/*

{onAction function || onKsFormAction}(model,action,response)
{onChange function || onKsFormChange}(newValue,oldValue,path,model)
onKsFormLoad(model)
onKsFormValidate(errors,model)

{actionUrlProcessor function}(response) => ["error message"] or [] if ok // default 200 OK else "Generic action failed message"
{validationUrlProcessor function}(response) => ["error message"] or [] if valid // default 200 OK else "Generic validation failed message"
{urlProcessor function}(response)  => [{id: "value", text: "label"}] or throw


 */

var MSG_CONFIRM = "Vuoi confermare l'operazione?";
var MSG_PROMPT = "Inserire un valore";
var ACTION_ERROR = "Il server ha risposto in modo inatteso";
var ACTION_SUCCESS = "Azione eseguita con successo";
var ADD_ROW = "Aggiungi";
var VALUE_MANDATORY = "Valore obbligatorio";
var VALUE_TOO_LITTLE = "Il valore non rispetta il min";
var VALUE_TOO_BIG = "Il valore non rispetta il max";
var VALUE_INVALID_STEP = "Il valore non rispetta lo step";
var VALUE_INVALID = "Valore non valido";
var VALIDATION_ERROR = "Sono presenti errori di validazione"
var VALIDATION_SUCCESS = "Non sono presenti errori di validazione"
var VALIDATION_SERVER_EXCEPTION = "Il server ha risposto in modo inatteso";

function parseTag(id,json){
    if (typeof id == "object" || id == null)
        return id;

    var tag = 	$(id[0]=='#'?id:"#"+id);
    if (tag.length!=1)
        return null;
    var tagName = tag.prop('tagName');
    if (tagName!='SCRIPT')
        return tag[0].outerHTML;

    var html = tag[0].innerHTML;

    if (!html)
        return html;

    if (tag.prop('type') == "application/json") {
        json = true;
    }

    if (json){
        html = $.parseHTML(html)[0].wholeText;
        html =  JSON.parse(html);
    } else if (html.indexOf("<")==-1){
        html = $('<div>').html(html).text();
    }

    return html;
}

function render(formId,modelId,tmplId) {

    if (formId && formId.startsWith("#")){
        formId = formId.substring(1);
    }
    var template = tmplId || "#"+formId+"_template";

    var model = parseTag(modelId,true);

    var form = $("#"+formId);

    if ( form.children().length==0 ){
        form.html(`
            <div :id="formId">
            <ks-form disabled="false" :form-id="formId"  ref="form" @callback="formevent" v-model="model" :template="template" />
            </div>
        `);
    } else if ($(template).length==0) {
        template = "#"+formId;
    }

    var formApp = new Vue({
        el : "#"+formId,
        data: {
            model: model ,
            formId: formId,
            template: template
        },
        methods: {
            formevent: function(evt) {
                this.emit("callback",evt);
            }
        }
    });
    return formApp;
}

Vue.component('ks-form', {
    props: ['model','el','template','formId','tabIndex','disabled','value'],

    created: function() {

        if (this.el){
            this.$options.el = this.el;
        }

        if (this.template){
            if (this.template[0]=='#' && $(this.template).prop('tagName') == 'SCRIPT'){
                this.$options.template = this.getXTemplate(this.template);
            } else {
                this.$options.template = this.template;
            }
        }
    },

    mounted: function() {
        if (this.form$tabidx != null){
            $("[data-tabindex='"+this.form$tabidx+"']").tab('show');
            this.$forceUpdate();
        } else {
            //this.form$tabidx = Number($(this.$el).find(".nav-tab.active").data("tabindex"));
            this.form$tabidx = Number($(this.$el).find(".tab-pane.active").data("contentindex"));

            if (isNaN(this.form$tabidx))
                this.form$tabidx = -1;
        }
        //this.form$wizard = $(this.$el).find(".wizard").length> 0 ;

        var x = this.form$tabidx;
        this.form$tabidx = -1;
        this.form$tabidx = x;
        //this.form$visited.push(this.form$tabidx);
        this.form$isTabVisited[''+this.form$tabidx] = true;

        var self = this;

        for( w in this.form$watchers ){
            var input = this.form$watchers[w];
            var xxx = function(){
                console.log(1);
            }
            eval( "function onchange_"+input+" (newValue) { self.$data['"+w+"'] = newValue; xxx(); }" );
            var f = eval("onchange_"+input);
            this.$watch( input , f );
        }

        //this.updateTabFeedbacks(this.form$tabidx,true);

        this.validate();

        var script = $("#form-script").text();

        if (script){
            eval(script);
        }

        var f = this.findFunc("onKsFormLoad");
        if (f)
            f.call(this,this.getClean());

        //this.validate(true);
    },
    updated: function() {
        //this.ontab();

        //this.ontab();
        if (!this.$onceupdated){
            this.validate();
            this.$onceupdated = true;
        }

    },
    data: function() {

        var x = this.template || this.el;

        if (x){
            if (x.startsWith("#")){
                if ($(x).prop('tagName') == 'SCRIPT'){
                    x = this.getXTemplate(x);
                } else {
                    x = $(x).html();
                }
            }

            var el = $('<div>').html(x);
            var self = this;

            var watchers = {};
            var clone = JSON.parse(JSON.stringify(this.model||this.value));

            el.find("[data-context],[data-input],[data-output]").each( function() {

                var input = $(this).data("input");
                var output = $(this).data("output");
                var context = $(this).data("context");

                if ($(this).hasClass('section-body')){

                    var isArr = context.indexOf("[]")>0;
                    if (isArr)
                        context = context.substring(context,context.length-2);

                    var s = clone[context];

                    if (typeof s == "undefined"){
                        self.$set(clone, context, isArr? [] : {});
                        s = clone[context];
                    }

                    if (output && output!=context)
                        clone[output] = s;

                    return;
                }

                if (input && /*input.indexOf(".")==-1 &&*/ typeof clone[input.split(".")[0]] == "undefined" ){
                    //self.$set(clone, input, null);
                    self.setPath(input,null,clone);
                }

                if (output && /*output.indexOf(".")==-1 &&*/ typeof clone[output.split(".")[0]] == "undefined" ){
                    //self.$set(clone, output, null);
                    self.setPath(output,null,clone);
                }

                if (input && output /*&& output.indexOf(".")==-1*/ && input != output) {
                    if (output.indexOf(".")==-1)
                        watchers[output] = input;

                    var v = self.getPath(input, clone);
                    if (v!=null)
                        self.setPath(output,v,clone);
                }
            });
        }

        this.$set(clone, "form$watchers", watchers);

        if (this.tabIndex == undefined || this.tabIndex=="")
            this.$set(clone, "form$tabidx", null);
        else
            this.$set(clone, "form$tabidx", Number(this.tabIndex) );

        if (this.form$errors === undefined)
            this.$set(clone, "form$errors", [] );

        if (this.form$message === undefined)
            this.$set(clone, "form$message", null );

        if (this.form$isTabVisited === undefined)
            this.$set(clone, "form$isTabVisited", {} );

        this.$set(clone, "form$modal", {} );

        //this.$set(this . model, "form$validation", [] );

        this.$set(clone, "form$invalids", {} );
        this.$set(clone, "form$isTabValid", {} );
        this.$set(clone, "form$isFormValid", false );
        this.$set(clone, "form$showAlerts", true );
        //this.$set(this. model, "form$wizard", null );

        this.backup = this.getClean(clone);

        this.hookScript();

        return clone;
    },

    methods:{

        reset: function(obj) {
            obj = obj || this.backup || {};
            for( x in obj ){
                this.$set(this.$data, x, obj[x] );
            }
            this.validate();
        },

        getClean: function(obj) {
            var clone = JSON.parse(JSON.stringify(obj||this.$data));
            for( x in clone ){
                if (x.startsWith("form$"))
                    delete clone[x];
				else if(this.form$watchers) {
					var input = this.form$watchers[x];
					if (input)
						clone[x] = clone[input];
				}
            }
            return clone;
        },

        getXTemplate: function(selector) {
            /*var html = $(selector).html();
            if (html.indexOf("<")==-1){
                html = $('<div>').html(html).text();
            }
            //return $('<div>').html($(selector).html()).text();
            return html;*/
            return parseTag(selector);
        },
        onchange: function(ctrl,path,value,func) {
            //console.log( (Array.isArray(value) ? "["+value+"]" : value) +" -> "+path,func);
            //this.$forceUpdate();

            var oldValue = $(ctrl).data("bind");

            $(ctrl).data("bind",value);

            this.updateFeedbacks();

            /*var preValid = (this.form$invalids['#'+$(ctrl).attr("id")]!=null);
            var postValid = (this.updateFeedback(ctrl)!=null);

            if (preValid != postValid){
                var tabidx = $(ctrl).closest('[data-contentindex]').data("contentindex");
                this.updateTabFeedbacks(tabidx,false);
            }*/

            var bubble = null;
            if (func){
                var f = this.findFunc(func) || this.findFunc("onKsFormChange");
                if (!f){
                    console.log("function "+func+" not found");
                } else {

                    try{
                        bubble = f.call(this,value,oldValue,path,this.getClean());
                        this.validate();
                    } catch (e) {
                        showError(e);
                        console.log(e);
                        return false;
                    }
                }
            }
            if (bubble !== false){
                var payload = {
                    newValue: value,
                    oldValue: oldValue,
                    source: ctrl,
                    path: path
                }
                this.$emit("onchange",payload);
                this.$emit("input", this.getClean() );
            }
            return bubble;
        },
        select2url: function(url) {
            var self = this;
            if (!url)
                return null;

            if (url.match(/^[$A-Z_][0-9A-Z_$]*$/i))
                return this.findFunc(url);

            return function(params) {
                var term = params.term||"";
                return url.replace("...",term+"*").replace("{{$q}}",term).replace("**","*");
            }
        },
        select2process: function(process){
            var self = this;
            var f = this.findFunc(process);
            if (!f)
                return null;
            return function(data) {
                var resp = f(data);
                if (resp.results)
                    return resp;
                else
                    return { results: resp }
            };
        },
        f: function(handler){
            return this.findFunc(handler);
        },
        findFunc: function(handler,args) {

            if (!handler)
                return null;

            var self = this;

            if (!handler.match(/^[$A-Z_][0-9A-Z_$]*$/i)) {
                func = function (arg) {
                    return self.evaluate(handler,arg);
                }
                return func;
            }

            /*var checkFnc = function(func,args) {

                if (!func)
                    return null;
                var args0 = func.toString().match(/\(([^)]*)\)/)[1].replace(" ","")+",";
                if (!args || args0.startsWith(args.join(",")+","))
                    return func;
                else
                    return null;
            }

            var fPar = checkFnc(this.$parent[handler]);
            var fForm = checkFnc(this.$parent.$el[handler]);
            var fWdw = checkFnc(window[handler],args);*/

            var fPar = this.$parent[handler];
            var fForm = this.$parent.$el[handler];
            var fWdw = window[handler];

            return fWdw || fForm || fPar;
        },
        ison: function(checked,value,subtype) {

            if (subtype=='radio')
                return checked == value;

            if (typeof checked == "number")
                return (checked&value)>0;
            else
                return (checked||[]).indexOf(value)>=0;
        },
        oncheck: function(oldvalue,required){
            if (oldvalue===true)
                return false;
            else
                return true;
        },
        toggle: function(checked,value,subtype,values) {

            if (subtype=='radio'){
                return value;
            }

            if (typeof checked == "number")
                return (checked&value) ? checked - value : checked | value;

            if (typeof checked == "string")
                return (checked.indexOf(value)>=0) ? checked.replace(value,'') : checked+value;

            if (checked==null){
                return [value];
                /*var first = values[0].id || values[0].value;
                if (typeof first == "number"){
                    var sum = 0;
                    var mask = 0;
                    for( x in values){
                        var v = values[x].id || values[x].value;
                        sum += v;
                        mask |= v;
                        if (sum!=mask)
                            return [value];
                    }
                } else {
                    for( x in values){
                        var v = values[x].id || values[x].value;
                        if (v.length>1)
                            return [value];
                    }
                }
                return value;*/
            }


            var idx0 = checked.indexOf(value);

            if (idx0>=0)
                checked.splice(idx0,1);
            else
                checked.push(value);

            return checked;
        },
        dateRead: function (val,offset) {
            if (!val || val.length<16)
                return val;

            var local = moment.utc(val).local();

            if (offset=='end'){
                local.subtract(1, "days");
                local = local.startOf("day");
            }

            if (offset=='start'){
                local = local.startOf("day");
            }

            return local.format().substring(0,10);
        },
        dateWrite: function (val,offset) {
            if (!val || val.length<10)
                return val;

            var local = moment(val);

            if (offset=='end'){
                local.add(1, "days");
                local = local.startOf("day");
            }

            if (offset=='start'){
                local = local.startOf("day");
            }

            return local.utc().format();
        },
        datetimeRead: function (val) {

            if (!val || val.length<16)
                return val;

            var local = moment.utc(val).local();

            return local.format().substring(0,16);
        },
        datetimeWrite: function (val) {

            if (!val || val.length<16)
                return val;

            var local = moment(val);

            return local.utc().format();
        },
        scrollTo: function(id,tabIndex) {

            if (tabIndex && tabIndex!=this.form$tabidx){
                $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                    $('html, body').scrollTop($("#"+($("#"+id).data("anchor")||id)).offset().top - 30);
                });
                this.show(tabIndex);
            } else {
                $('html, body').scrollTop($("#"+($("#"+id).data("anchor")||id)).offset().top - 30);
            }
        },
        nextIndex : function() {
            var x = this.form$tabidx;

            while(true){
                ++x;
                var tab = $("[data-tabindex='"+x+"']");
                if (tab.length==0)
                    return -1;

                if (tab.is(":visible")){
                    return x;
                }
            }
        },
        previousIndex : function() {
            var x = this.form$tabidx;

            while(true){
                --x;

                var tab = $("[data-tabindex='"+x+"']");
                if (tab.length==0 || x<0)
                    return -1;

                if (tab.is(":visible")){
                    return x;
                }
            }
        },
        ontab: function(idx){
            //if (idx!=null)
            this.form$tabidx = idx;

            //if (this.form$tabidx>=0) {
            //if (this.form$visited.indexOf(this.form$tabidx) == -1)
            //    this.form$visited.push(this.form$tabidx);
            //}
            this.form$isTabVisited[''+idx] = true;
            //this.updateTabFeedbacks(idx,true);

            this.validate();
        },
        show: function(index) {
            if (index>=0){
                var tab = $("[data-tabindex='"+index+"']");
                if (tab.is(":visible")){
                    tab.tab('show');
                    this.ontab(index);
                    return true;
                }
            }
            return false;
        },
        next: function() {
            return this.show(this.nextIndex());
        },
        previous: function() {
            return this.show(this.previousIndex());
        },
        confirm: function(msg,callback) {
            var self = this;
            if (!callback){
                callback=msg;
                msg = MSG_CONFIRM;
            }

            this.form$modal.message = msg;

            if ( typeof callback != "function"){
                var espr = callback;
                callback = function() {
                    return self.evaluate(espr,self.form$modal.value);
                }
            }

            this.$refs.confirm.show(callback);
        },
        prompt: function(msg,callback) {
            var self = this;
            if (!callback){
                callback=msg;
                msg = MSG_PROMPT;
            }
            this.form$modal.message = msg;

            if ( typeof callback != "function"){
                var espr = callback;
                callback = function() {
                    return self.evaluate(espr,self.form$modal.value);
                }
            }

            this.$refs.prompt.show(callback);

        },
        getName : function(context,path,$index) {
            return path.replace("$item",context).replace("[]","["+$index+"]");
        },
        getSimpleName : function(label,path) {
            return label || (path||'').substring(path.lastIndexOf(".")+1);
        },
        accessors : function  (path) {
            return path.replace(/\[(\d+)\]/g, '.$1').replace(/^\./, '').split('.').map(a => ({
                key: !isNaN(a) ? Number(a) : a,
                isArray: !isNaN(a)
            }))
        },
        updateTabFeedbacks: function(tabidx,recalc){

            /*if (typeof ctrl == "number"){
                if (this.form$visited.indexOf(ctrl)==-1)
                    return [];
                ctrl = $("[data-contentindex="+ctrl+"]");
            }*/

            var tab = $("[data-contentindex="+tabidx+"]")

            var ctrls = tab.find('[data-valid]');

            var valid = true;
            for( var l=0; l<ctrls.length; l++) {
                var x;
                if (recalc)
                    x = this.updateFeedback($(ctrls[l])[0]);
                else
                    x = this.form$invalids['#'+$(ctrls[l]).attr("id")];
                if (x!=null)
                    valid = false;
            }

            this.$set(this.form$isTabValid, ""+tabidx , valid );

            /*var isValid = true;
            for( x in this.form$isTabVisited ){
                isValid = isValid && this.form$isTabValid[""+x];
            }

            this.form$isFormValid = isValid;*/

            /*if (ctrl==null)
                ctrl = $(this.$el);
            else
                ctrl = $(ctrl);
            var ctrls;
            if (ctrl.data("valid"))
                ctrls = [ctrl];
            else
                ctrls = ctrl.find('[data-valid]');
            return ctrls;*/
        },
        isValid : function(){
            return this.form$isFormValid;
        },
        /*invalidFeedback: function(ctrl){
            var fb = this.invalidFeedbacks(ctrl)[0];
            return !fb ? fb : (fb.message || fb);
        },*/
        updateFeedback: function(ctrl){
            var x = null;
            var validity;
            var dr = $(ctrl).data("required");
            if (dr) {
                var value = $(ctrl).data("bind");
                var valued;
                if (Array.isArray(value))
                    valued = value.length > 0;
                else if (typeof value == "boolean")
                    valued = value == true;                
                else
                    valued = (value != null && value !== "");

                validity = {
                    valid : valued,
                    valueMissing : valued
                }
            } else {
                validity = ctrl.validity || {valid: true};
            }

            var valid = this.evaluate($(ctrl).data("valid"));
            var id = $(ctrl).attr("id");

            if (!validity.valid || !valid) {

                var msg = $(ctrl).data("invalid-feedback");
                var name = $(ctrl).data("simplename");

                if (name.indexOf("$")>0)
                    name = "";

                var pane = $(ctrl).closest('[data-contentindex]').data("contentindex");
                var tab = $("[data-tabindex='" + pane + "']").text().trim();

                //if (this.form$visited.indexOf(pane) == -1 && $("[data-contentindex='" + pane + "'].sticky").length == 0)
                //    continue;

                if (!msg) {
                    if (validity.valueMissing)
                        msg = VALUE_MANDATORY; //"valore obbligatorio";
                    else if (validity.rangeUnderflow || validity.tooShort)
                        msg = VALUE_TOO_LITTLE; //"valore troppo piccolo";
                    else if (validity.rangeOverflow || validity.tooLong)
                        msg = VALUE_TOO_BIG; //"valore troppo grande";
                    else if (validity.stepMismatch)
                        msg = VALUE_INVALID_STEP; //"valore non rispetta lo step";
                    else
                        msg = VALUE_INVALID; //"valore invalido";
                }

                x = {id: id, tabIndex: pane, tab: tab, name: name, message: msg};

                this.$set(this.form$invalids, '#'+id, x );

                //feedbacks.push({id: id, tabIndex: pane, tab: tab, name: name, message: msg});
            } else {
                this.$set(this.form$invalids, '#'+id, null );
                delete this.form$invalids['#'+id];
            }
            return x;
        },
        /*updateFeedbacks: function (tabidx){

            var ctrls = this.ctrlToCheck(tabidx);

            for( var l=0; l<ctrlslist.length; l++) {

                var ctrls = ctrlslist[l];

                for (var i = 0; i < ctrls.length; i++) {
                    updateFeedback($(ctrls[i])[0]);
                }
            }

            //return feedbacks;

        },
        updateTabValidity: function(idx) {

        },*/
        evaluate: function(expression,value) {
            try {
                if (expression == null)
                    return null;
                if (typeof expression == "boolean")
                    expression = "return ("+expression+")";
                else if (expression.indexOf && expression.indexOf("return ")==-1 && !expression.trim().endsWith(";") ){
                    expression = "return ("+expression+")";
                }
                const ret = Function("with(this){ value="+JSON.stringify(value)+"; "+expression+"}").call(this)
                return ret === undefined ? null : ret
            } catch (e) {
                console.log(e);
                return null
            }
        },
        render: function (template) {
            return template.replace(/{{.*?}}/g, match => {
                return this.evaluate(match.slice(2, -2))
            })
        },
        addsection: function(context){
            context.push({});
        },
        delsection: function(context,idx){
            this.confirm( () => context.splice(idx,1));
        },
        onerror: function(err){
            showError(err);
        },

        updateFeedbacks: function(){
            for( x in this.form$isTabVisited ){
                this.updateTabFeedbacks(x,true);
            }
            this.form$isFormValid = (Object.values(this.form$invalids).length==0);
            return this.form$invalids;
        },

        validate: function(showAlerts) {

            var self = this;

            this.$nextTick( function() {
                self.updateFeedbacks();

                var func = self.findFunc("onKsFormValidate");

                if (func!=null) {
                    func.call(self,Object.values(self.form$invalids),self.getClean());
                }
            });

            if (showAlerts!==undefined){
                this.form$showAlerts = showAlerts;
            }


        },

        /*onvalidate: function(errors) {
            var self = this;
            var func = this.findFunc("onvalidate");

            var bubble = true;
            if (func!=null) {
                try{
                    bubble = func(self . model,errors,self.formId,"onvalidate");
                } catch (e) {
                    showError(e);
                    console.log(e);
                    return false;
                }
            }

            if (bubble !== false){
                this.$emit("validation",errors);
            }
        },*/

        report: function(validationResponse){
            this.form$errors = validationResponse;
            if (validationResponse!=null && validationResponse.length>0){
                this.scrollTo("alert-panel");
                $("#alert-panel .collapse").collapse('show');
                return false;
            } else {
                return true;
            }
        },

        hookScript: function(){
            var functions = {};
            var scr = $("#" + this.formId+"_scripts").text();
            if (scr){
                var tags = [...scr.matchAll(/function (\w+)/g)];
                for (x in tags){
                    var name = tags[x][1];
                    if (window[name]){
                        this[name] = window[name];
                    }
                }
            }
        },

        onaction: function(actionResponse){
            this.form$message = actionResponse.message || ACTION_SUCCESS;
            this.scrollTo("alert-panel");
        },
        buttonclick: function(button){

            //this. model.form$validaton = this.invalidFeedbacks().concat(this.form$errors||[]);

            var actionName = button.customAction || button.action || button.role;

            var handler = button.onAction || actionName ;

            var validationUrl = button.validationUrl;
            var actionUrl = button.actionUrl;
            var self = this;

            var func0 = this[handler] || this.findFunc(handler);

            var func = func0 || this["onKsFormAction"] || this.findFunc("onKsFormAction");
            var validationProcess = this.findFunc(button.validationUrlProcessor);
            var actionProcess = this.findFunc(button.actionUrlProcessor);

            //identifier,handler||role,model,response

            var handle = function(actionResponse) {
                var bubble = true;
                if (func){
                    try{
                        bubble = func.call(self,self.getClean(),actionName,actionResponse);
                    } catch (e) {
                        showError(e);
                        console.log(e);
                        return false;
                    }
                }

                if (!func){
                    //console.log("function "+handler+" not found");

                    switch (button.action){
                        case "validate":
                            self.validate(true);
                            break;

                        case "clear":
                            self.reset();
                            break;

                        case "back":
                            self.previous();
                            break;

                        case "next":
                            self.next();
                            break;

                        case "submit":
                        case "save":
                            if (!actionResponse){
                                console.error("UNMANAGED ACTION:"+button.action);
                            }
                            break;

                        case "cancel":
                            console.error("UNMANAGED ACTION:"+button.action);
                            break;

                        default:
                            break;
                    }

                }

                if (bubble !== false){
                    var payload = {
                        response: actionResponse,
                        action : actionName,
                        model: self.getClean()
                    };
                    self.$emit("callback",payload);
                    self.$emit(actionName,payload);
                }
                return bubble;
            }

            var action = function() {
                axios
                    .post(actionUrl,self.getClean())
                    .then(response => {
                        var actionResponse = response.data;
                        if (actionProcess)
                            actionResponse = actionProcess(actionResponse);

                        if (!Array.isArray(actionResponse) || self.report(actionResponse)){
                            if (handle(actionResponse) !== false)
                                self.onaction(actionResponse);
                        }

                    })
                    .catch(err => self.onerror(err));
            }

            var validation = function() {
                axios
                    .post(validationUrl,self.getClean())
                    .then( response => {
                        var validationResponse = response.data;

                        if (validationProcess){
                            validationResponse = validationProcess(validationResponse);
                        }

                        if (!Array.isArray(validationResponse))
                            throw VALIDATION_SERVER_EXCEPTION;
                        if (self.report(validationResponse)){
                            if (actionUrl)
                                action();
                            else
                                handle();
                        }
                    })
                    .catch(err => self.onerror(err));
            }

            if (validationUrl || actionUrl)
                self.report(null);

            if (validationUrl){
                validation();
            } else if (actionUrl) {
                action();
            } else {
                handle();
            }

        },
        getPath (path,model,defaultValue) {
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
        setPath (path,value,model){
            if (path.startsWith("$"))
                return null;
            const a = this.accessors(path);

            var c = model || this;

            for (let i = 0; i < a.length - 1; i++) {
                var n = c[a[i].key];
                if (a[i + 1].isArray){
                    if (n==null || !Array.isArray(n))
                        this.$set(c, a[i].key, []);
                } else {
                    if (n==null || typeof n != "object"){
                        n = {};
                        this.$set(c, a[i].key, n);
                    }
                }
                c = n;
            }

            this.$set(c, a[a.length - 1].key, value)
            return value;
        },

    }
});

