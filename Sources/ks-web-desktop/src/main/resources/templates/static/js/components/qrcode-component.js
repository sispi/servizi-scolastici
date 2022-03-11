Vue.component('qrcode', {

    props: ['text','typeNumber','correctionLevel','mb','mode','render'],
    watch: {
        text: function() { this.show() },
        typeNumber : function() { this.show() },
        correctionLevel : function() { this.show() },
        mb : function() { this.show() },
        mode : function() { this.show() },
        render : function() { this.show() }
    },
    destroyed: function() {
    },
    data: function() {
        return {

        }
    },
    template: `<div></div>`,
    mounted: function(){
        this.show();
    },
    methods:{
        show: function(){

            qrcode.stringToBytes = qrcode.stringToBytesFuncs[this.mb || 'UTF-8'];

            var qr = qrcode(this.typeNumber || 4, this.errorCorrectionLevel || 'M');
            qr.addData(this.text, this.mode || 'Byte');
            try{
                qr.make();
            } catch (e) {
                $(this.$el).html('');
                this.$emit('error', e);
            }

            var html;

            if (this.render=="table")
                html = qr.createTableTag();
            else if (this.render=="svg")
                html = qr.createSvgTag();
            else
                html = qr.createImgTag();

            $(this.$el).html(html);
        }
    }
})