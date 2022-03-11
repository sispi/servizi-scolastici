import { MarkChatAsRead } from '/static/portal/js/services/instance.service.js?no-cache';
import { PublishMessage } from '/static/portal/js/services/instance.service.js?no-cache';
import { GetChatMessages } from '/static/portal/js/services/instance.service.js?no-cache';

Vue.component('App', { template: `<div class="container">
    <h2>Supporto e Assistenza dell'istanza {{instance.name}}</h2>

    <div v-for="message in messages">
    <div class="d-flex flex-row-reverse" v-if="username === message.senderUserId">
            <div class="p-3 mb-2 bg-light text-dark p-2" style="max-width: 60%; border-radius: 15px;">
                <small><strong>{{message.senderDisplayName}}</strong></small><br>
                {{message.text}}
            </div>
        </div>
        <div class="d-flex flex-row" v-else>
            <div class="p-3 mb-2 bg-info text-white p-2" style="max-width: 60%; border-radius: 15px;">
                <small><strong>{{message.senderDisplayName}}</strong></small><br>
                {{message.text}}
            </div>
        </div>
    </div>
    
    <div class="form-group">
        <div class="input-group">
            <!--
            <div class="input-group-prepend">
                <div class="input-group-text"><span class="glyphicon glyphicon-pencil"></span></div>
            </div>
            -->
            <textarea placeholder="Digita il testo qui" class="form-control" id="newMessage" name="newMessage" v-model="newMessage"></textarea>
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" @click="sendMessage">
                    <i class="fa fa-paper-plane" aria-hidden="true"></i> Invio
                </button>
            </div>
        </div>
    </div>
    
</div>
`, 
data() {
    const user = username;
    const instance = data("data-instance");
    const chatMessages = GetChatMessages(instance.id).data;
    return {
        username: user,
        messages: chatMessages.data,
        instance: instance,
        chat: data("data-chat"),
        newMessage: null
    };
},
methods: {
    sendMessage: function() {
        const message = {text: this.newMessage};
        const response = PublishMessage(this.instance.id, message);
        this.newMessage = null;
        const chatMessages = GetChatMessages(this.instance.id).data;
        this.messages = chatMessages.data;
        //window.location.reload();
    },
    markChatAsRead: function(){
        if(!this.chat.read){
            const response = MarkChatAsRead(this.instance.id);
        }
    }
},
mounted:function(){
    this.markChatAsRead();
}
}); 
new Vue({ el: "#app" });
