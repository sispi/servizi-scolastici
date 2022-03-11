function FileClient(uri0){
    var uri = uri0 || "/docer/v1/files";

    this.url = function(fileId){
        return uri+"/"+fileId;
    }

    this.createMulti = function(files,groupId,done,error){

        if (typeof groupId == "function"){
            error = done;
            done = groupId;
            groupId = null;
        }

        var formData = new FormData();

        for( var i=0; i<files.length; i++ ){
            formData.append("files", files[i]);
        }

        uri = uri+"/multiparts";
        if (groupId)
            uri += "?groupId="+groupId;

        axios.post(uri,formData)
            .then(function(response){
                var resp;
                if (Array.isArray(response.data)){
                    resp = {};
                    for( var i=0; i<files.length; i++ ){
                        resp[response.data[i].replaceAll("$","/")] = files[i].name;
                    }
                } else {
                    resp = response.data;
                }

                if (done)
                    done(resp);
                else
                    console.log(resp);

            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.create = function(file,done,error){

        var formData = new FormData();

        file = $(file)[0];
        if (file && file.files)
            file = file.files[0];

        formData.append("file", file);

        axios.post(uri+"/multipart?groupId="+((file||{}).groupId||""),formData)
            .then(function(response){
                if (done)
                    done(response.data.replaceAll("$","/"), file.name);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.createGroup = function(store,done,error){
        axios.post(uri+"/createGroup", {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.update = function(file,done,error){

        file = $(file)[0];
        if (file && file.files)
            file = file.files[0];

        var formData = new FormData();
        formData.append("file", file);

        axios.patch(uri+"/multipart?fileId="+(file.fileId||""),formData)
            .then(function(response){
                if (done)
                    done(file.fileId, file.name);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.delete = function(fileId, done, error){
        axios.delete(uri+"/"+fileId , {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(fileId);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.deleteGroup = function(groupId, done, error){
        axios.post(uri+"/deleteGroup/"+groupId , {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(groupId);
            })
            .catch(function(err){
                processError(err,error);
            });
    }
}

function baseclient(path){

    var uri;

    if (path.indexOf("/")==-1)
        uri = "/docer/v1/"+path;
    else
        uri = path;

    this.history = function(id, done, error){
        return this.get(id+"/history",done,error);
    }

    this.search = function(params, done, error){
        axios.get(uri , {params:params}, {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
                else
                    console.log(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.get = function(path, params, done, error){


        if (typeof path == "object"){
            //sono i params
            error = done;
            done = params;
            params = path;
            path = null;
        } else if (typeof path == "function"){
            // è il done
            error = params;
            done = path;
            path = null;
            params = null;
        } else if (typeof params == "function"){
            // è il done
            error = done;
            done = params;
            params = null;
        }

        var url;

        if (path){
            if (path.indexOf("/")==0 || path.indexOf("?")==0)
                url = uri + path;
            else
                url = uri + "/" + path;
        } else {
            url = uri;
        }

        url = url.replace("//","/");

        if (params){
            url += "?" + $.param(params);
        }

        axios.get(url , {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.post = function(path,payload, done, error){

        var url;

        if (typeof path == "object" ){
            url = uri;
            error = done;
            done = payload;
            payload = path;
        } else {
            url = uri;

            if (path && path.indexOf("/")==0 || path.indexOf("?")==0)
                url += path;
            else if (path)
                url += "/" + path;
        }

        url = url.replace("//","/");

        axios.post(url , payload, {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.create = this.post;

    this.put = function(path,payload, done, error){

        var url = uri;
        if (path)
            url += "/" + path;

        url = url.replace("//","/");

        axios.put(url , payload, {  headers: { "content-type":"application/json", "accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.replace = this.put;

    this.patch = function(path,payload, done, error){

        var url = uri;
        if (path)
            url += "/" + path;

        url = url.replace("//","/");

        axios.patch(url , payload, {headers: {"accept":"application/json"} } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.update = this.patch;

    this.delete = function(path,payload, done, error){

        var url = uri;
        if (path)
            url += ("/" + path);

        url = url.replace(/\/+/,"/")

        if (typeof payload == "function"){
            error = done;
            done = payload;
            payload = null;
        }

        var params = {
            data : payload,
            headers: {"accept":"application/json"}
        };

        axios.delete(url ,params )
            .then(function(response){
                if (done)
                    done();
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.postMultipart = function(path,payload, done, error){

        var url = uri;
        if (path)
            url += "/" + path;

        url = url.replace("//","/");

        if (!(payload instanceof FormData)){
            var formData = new FormData();
            for( x in Object.keys(payload)){
                formData.append(x, payload[x]);
            }
            payload = formData;
        }

        axios.post(url , payload, {
            headers: {
                "accept":"application/json"
            }
        } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.postUrlEncoded = function(path,payload, done, error){

        var url = uri;
        if (path)
            url += "/" + path;

        url = url.replace("//","/");

        if (typeof payload == "object"){
            payload = Object.entries(payload)
                .map(([key, val]) => `${key}=${encodeURIComponent(val)}`)
                .join('&');
        }

        axios.post(url , payload, {
            headers: {
                "accept":"application/json",
                "content-type": "application/x-www-form-urlencoded"
            }
        } )
            .then(function(response){
                if (done)
                    done(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

}

function report(qt,params,done, error){
    axios.get("/docer/v1/report/"+(qt||"") , {params:params}, {headers: {"accept":"application/json"} } )
        .then(function(response){
            if (done)
                done(response.data);
        })
        .catch(function(err){
            processError(err,error);
        });
}

function DocerClient(){

    this.utenti = new baseclient("utenti");
    this.gruppi = new baseclient("gruppi");
    this.titolari = new baseclient("titolari");
    this.anagrafiche = new baseclient("anagrafiche");
    this.cartelle = new baseclient("cartelle");
    this.fascicoli = new baseclient("fascicoli");
    this.documenti = new baseclient("documenti");

    this.documenti.create = function(payload,done,error){
        if (payload.push)
            return this.post("multi?relate=true",payload,done,error);
        else
            return this.post(payload,done,error);
    }

    this.documenti.related = function(id, done, error){
        return this.get(id+"/related",done,error);
    }
    this.documenti.relate = function(docnum,related, done, error){
        return this.post(docnum+"/related?related="+(related||""),null,done,error);
    }
    this.documenti.unrelate = function(docnum,related, done, error){
        return this.delete(docnum+"/related?related="+(related||""),done,error);
    }
    this.documenti.versions = function(docnum, done, error){
        return this.get(docnum+"/versions",done,error);
    }
    this.documenti.classifica = function(docnum,classifica, done, error){
        return this.post(docnum+"/classifica?classifica="+(classifica||""),done,error);
    }
    this.documenti.fascicola = function(docnum,primario, done, error){
        return this.post(docnum+"/fascicola?primario="+(primario||""),done,error);
    }
    this.documenti.registri = function(done, error){
        return this.get("registri",done,error);
    }

    this.report = report;

    this.ping = function (done,error){
        var t0 = new Date().getTime();
        axios.get("/docer/v1/system/ping")
            .then(function(response){
                t0 = new Date().getTime()-t0;
                if (done)
                    done(t0);
                else
                    console.log(t0);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

    this.login = function (aoo,username,password,done,error){
        axios.get("/docer/v1/system/login?username="+(username||"")+"&password="+(password||"")+"&codAoo="+(aoo||""))
            .then(function(response){
                if (done)
                    done(response.data);
                else
                    console.log(response.data);
            })
            .catch(function(err){
                processError(err,error);
            });
    }

}

var FileClientApi = new FileClient();
var DocerClientApi = new DocerClient();
var ClientApi = new baseclient('/');