function signals(evt,payload,done,error){
    var body = {
        payload : payload
    }
    axios.post("/bpm/v1/events/"+evt+"/signals", body)
        .then(function(response){
            if (done)
                done(response.data);
        })
        .catch(function(err){
            processError(err,error);
        });
}

function BpmClient(){

    this.tasks = new baseclient("/bpm/v1/tasks");
    this.instances = new baseclient("/bpm/v1/instances");
    this.bookmarks = new baseclient("/bpm/v1/bookmarks");
    this.configurations = new baseclient("/bpm/v1/configurations");
    this.deployments = new baseclient("/bpm/v1/deployments");
    this.notifications = new baseclient("/bpm/v1/notifications");

    /*

    {
      "subject": "Hello ${name}",
      "body": "Bla bla bla...",
      "tags": [ "docNum:123456789" ],
      "toRecipients": [ "admins" ]
    }

    {
      "operationId": "123e4567-e89b-12d3-a456-426614174000",
      "email": true,
      "subject": "Hello ${name}",
      "body": "Bla bla bla...",
      "priority": false,
      "expireTs": "2020-11-11T14:57:48.000Z",
      "tags": [
        "document",
        "docNum:123456789"
      ],
      "toRecipients": [
        "admins"
      ],
      "ccRecipients": [],
      "bccRecipients": [],
      "attachments": [
        "file://storage/file.txt"
      ],
      "contextVariables": {
        "name": "Mario Rossi"
      }
    }

     */
    this.events = new baseclient("/bpm/v1/events");

    this.configurations.activate = function(id,payload,done,error){
        return this.post(id+"/actions/activate",payload,done,error);
    }

    this.deployments.activate = function(id,payload,done,error){
        return this.post(id+"/actions/activate",payload,done,error);
    }

    this.deployments.share = function(id,payload,done,error){
        return this.post(id+"/actions/share",payload,done,error);
    }

    this.deployments.createConfiguration = function(id,payload,done,error){
        return this.post(id+"/configurations",payload,done,error);
    }

    this.events.signalEvent = function(id,payload,done,error){
        return this.post(id+"/signals",payload,done,error);
    }

    this.notifications.markNotificationAsRead = function(id,done,error){
        return this.put(id+"/read",null,done,error);
    }

    this.instances.abort = function(id,payload,done,error){
        return this.post(id+"/actions/abort",payload,done,error);
    }

    this.instances.createBookmark = function(id,payload,done,error){
        return this.post(id+"/bookmarks",payload,done,error);
    }

    this.instances.getChat = function(id,done,error){
        return this.get(id+"/chat",done,error);
    }

    this.instances.getChatMessages = function(id,done,error){
        return this.get(id+"/chat/messages",done,error);
    }

    this.instances.publishChatMessage = function(id,payload,done,error){
        return this.post(id+"/chat/messages",payload,done,error);
    }

    this.instances.deleteChatMessage = function(id,messageId,done,error){
        return this.delete(id+"/chat/messages/"+messageId,done,error);
    }

    this.instances.markChatAsRead = function(id,done,error){
        return this.put(id+"/chat/read",null,done,error);
    }

    this.instances.getNodes = function(id,done,error){
        return this.get(id+"/nodes",done,error);
    }

    this.instances.getNode = function(id,nodeId,done,error){
        return this.get(id+"/nodes/"+nodeId,done,error);
    }

    this.instances.getNodeCommands = function(id,nodeId,done,error){
        return this.get(id+"/nodes/"+nodeId+"/commands",done,error);
    }

    this.instances.getNodeVariableValues = function(id,nodeId,done,error){
        return this.get(id+"/nodes/"+nodeId+"/variable-values",done,error);
    }

    this.instances.getNodeVariables = function(id,nodeId,done,error){
        return this.get(id+"/nodes/"+nodeId+"/variables",done,error);
    }

    this.instances.getNotifications = function(id,done,error){
        return this.get(id+"/notifications",done,error);
    }

    this.instances.getTasks = function(id,done,error){
        return this.get(id+"/tasks",done,error);
    }

    this.instances.getTreeInstances = function(id,done,error){
        return this.get(id+"/tree-instances",done,error);
    }

    this.instances.getTreeTasks = function(id,done,error){
        return this.get(id+"/tree-tasks",done,error);
    }

    this.instances.getVariables = function(id,done,error){
        return this.get(id+"/variables",done,error);
    }

    this.instances.updateVariable = function(id,name,value,done,error){
        return this.put(id+"/variables/"+name+"/value",value,done,error);
    }

    this.instances.retryNode = function(id,node,context,done,error){
        return this.post(id+"/nodes/"+node+"/actions/retry",context||{},done,error);
    }

    this.instances.completeNode = function(id,node,context,done,error){
        return this.post(id+"/nodes/"+node+"/actions/complete",context||{},done,error);
    }

    this.instances.abortNode = function(id,node,done,error){
        return this.post(id+"/nodes/"+node+"/actions/abort",null,done,error);
    }

    this.instances.startForm = function(processId,form,done,error){
        return this.postUrlEncoded("form?processId="+encodeURIComponent(processId),form,done,error);
    }

    this.instances.start = function(processId,input,done,error){
        return this.post({processId:processId,input:input},done,error);
    }

    this.tasks.completeForm = function(taskId,form,done,error){
        return this.postUrlEncoded(taskId+"/actions/complete/form",form,done,error);
    }

    this.tasks.complete = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/complete",payload,done,error);
    }

    this.tasks.claim = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/claim",payload,done,error);
    }

    this.tasks.delegate = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/delegate",payload,done,error);
    }

    this.tasks.forward = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/forward",payload,done,error);
    }

    this.tasks.release = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/release",payload,done,error);
    }

    this.tasks.save = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/save",payload,done,error);
    }

    this.tasks.skip = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/skip",payload,done,error);
    }

    this.tasks.refuse = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/refuse",payload,done,error);
    }

    this.tasks.claim = function(taskId,payload,done,error){
        return this.post(taskId+"/actions/claim",payload,done,error);
    }

    this.tasks.getComments = function(id,done,error){
        return this.get(id+"/comments",done,error);
    }

    this.tasks.createComment = function(id,payload,done,error){
        return this.post(id+"/comments",payload,done,error);
    }

    this.tasks.deleteComment = function(taskId,commentId,done,error){
        return this.delete(taskId+"/comments/"+commentId,done,error);
    }

    this.tasks.getFormTemplate = function(id,done,error){
        return this.get(id+"/form/template",done,error);
    }

    this.tasks.getFormView = function(id,done,error){
        return this.get(id+"/form/view",done,error);
    }

    this.tasks.getNotifications = function(id,done,error){
        return this.get(id+"/notifications",done,error);
    }
}

var BpmClientApi = new BpmClient();

/*function restoredraft(form,input){
    for ( x in input){
        var ctrl = form.find("[name='"+x+"']");
        if (ctrl.length>0){
            if (ctrl.attr("type")=="checkbox"){
                ctrl.prop('checked',input[x]);
            } else {
                ctrl.val(input[x]);
            }
        }
    }
}*/

function savedraft(taskId,input) {

    BpmClientApi.tasks.save(taskId, {
                                        input : input,
                                        mergeInput : true
                                    }, ()=>{console.log("saved")});
}