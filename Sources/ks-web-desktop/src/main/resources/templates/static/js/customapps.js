function aggiornaAppName(appName){
	//var currentAppName = appName;
		//--console.log("DEBUG -- currentAppName: "+currentAppName);
		//var oldAppQuery = currentAppName;
		
		//console.log("DEBUG -- oldAppQuery: "+oldAppQuery);

		//var newAppQuery ="";

		var newUrl = appendAppNameToUrl(window.location.href, appName);

		history.replaceState(null, null, newUrl);

}


function appendAppNameToUrl(url, app){

	var modifiedUrl = new URL(url, window.location.href);
	var myAppName= appName;
	if(app != null){
		myAppName = app;
	}
    var oldAppQuery = myAppName;
	var newAppQuery="";

    var requestUrl = modifiedUrl.href;
    //console.log("DEBUG -- requestUrl:"+ requestUrl);
    var requestUrlProtocol = modifiedUrl.protocol; //es. http:
    var requestUrlHost = modifiedUrl.host; //Es. 192.168.0.12:8092
    var requestUrlPath = modifiedUrl.pathname;
    var requestUrlQuery = modifiedUrl.search;
    var requestUrlHash = modifiedUrl.hash;

    var requestUrlParams = new URLSearchParams(requestUrlQuery);

    if(requestUrlParams.has("change_app")){
        newAppQuery = "&changeApp="+requestUrlParams.get("change_app");
    }

    if((newAppQuery != null && newAppQuery.length ==0) || newAppQuery == null){
        //console.log("DEBUG -- inizio rimpiazzo la queryString");

        if(requestUrl.indexOf("?") >=0){
            oldAppQuery = "&change_app="+myAppName;
        }else{
            oldAppQuery = "?change_app="+myAppName;
        }
        requestUrlQuery = requestUrlQuery + oldAppQuery;


        requestUrl = requestUrlProtocol + "//" + requestUrlHost + requestUrlPath + requestUrlQuery + requestUrlHash;

        //history.replaceState(null, null, requestUrl);
        //console.log("DEBUG -- fine rimpiazzo la queryString con: "+ oldAppQuery);
    }

    return requestUrl;

}