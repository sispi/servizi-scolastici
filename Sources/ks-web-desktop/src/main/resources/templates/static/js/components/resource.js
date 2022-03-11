Vue.component('resource', {

    props: ['source','type','url','value','processor'],
    data: function() {
        return {
            el: null
        }
    },
    template: `<template/>`,
    mounted: function(){
        if (this.source){
            var el = $("#"+this.source);
            if (el.length==1 && el.prop('tagName') == 'SCRIPT'){
                this.el = el[0];
                if (!this.type)
                    this.type = $(el).prop("type");
            }
        }

        this.load();
    },
    methods:{

        parse: function(data,ct) {

            if (data){
                ct = ct || this.type || "application/json";
                if (ct=="application/json"){
                    if (typeof data != 'object')
                        data = JSON.parse(data);
                } else {
                    if (typeof data == 'object')
                        data = JSON.stringify(data);
                }
                if (ct.endsWith('x-template')){
                    if (data.indexOf("<")==-1 && data.indexOf("&gt;")>=0 && data.indexOf("&lt;")>=0){
                        data = $('<div>').html(data).text();
                    }
                }
            }
            this.type = ct;

            if (this.processor!=null)
                data = this.processor(data);

            this.value = data;

            return data;
        },

        load: function(done,error){
            var self = this;

            if (this.el){
                var html = $(this.el).html();
                try{
                    var data = self.parse(html);
                    if (done)
                        done(data);
                    self.$emit('input',data);
                    console.log("sync",self.type,self.url);
                }catch (e) {
                    self.$emit('error',e);
                }

            } else {
                axios.get(this.url)
                    .then(function(response){
                        var data = self.parse(response.data,response.headers['content-type']);
                        if (done)
                            done(data);
                        self.$emit('input',data);
                        console.log("async",self.type,self.url);
                    })
                    .catch(function(err){
                        if (error)
                            error(error);
                        self.$emit('error',err);
                    });
            }


        }
    }
})