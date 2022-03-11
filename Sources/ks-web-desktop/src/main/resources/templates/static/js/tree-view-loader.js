var treeViewId = 'archiveTree';

$(function() {
    initTree();
});

function refreshTreeview() {
    $("#"+treeViewId).replaceWith( "<div id='"+treeViewId+"'></div>" );
    initTree();
}

function initTree() {

    var config = {
        data : null,
        autoOpen : false,
        dataUrl : function(node){
            return buildRequestUrl(node);
        },
        dataFilter : function(data) {
            return cleanData(data);
        },
        onCreateLi: function(node, $li) {

            if (node.type){
                var $title = $li.find('.jqtree-title');
				var name = node.name;
                if (node.type=="titolario"){
                    name = $title.text();

                    //fix per nascondere il piano classifciazione dalla label
                    if(name.match(/\$[12][0-9]{3}/g)){
                        name = name.replace(/\$[12][0-9]{3}/g, "");
                        $title.text(name);
                    }
                }
                $title.addClass("fticon-tree fticon-"+node.type);
				$title.attr('title',name);

                if (node.businessType)
                    $title.addClass("fticon-tree fticon-"+node.businessType);
            }

        }
    };

    $("#"+treeViewId).tree(config);

    $("#"+treeViewId).bind('tree.click', function(event) {
        var requestContentURL = buildRequestContentUrl(event.node);
        openFragment(requestContentURL, "page-content", true);
    });
	
	$("#"+treeViewId).bind('tree.open', function(event) {
		if (!event.node.parent.parent)
			$("#menu").hide();
    });
	
	$("#"+treeViewId).bind('tree.close', function(event) {
		if (!event.node.parent.parent)
			$("#menu").show();
    });

    $("#"+treeViewId).on('tree.init',function() {
		if (toOpen){
			var node = $('#'+treeViewId).tree('getNodeById', toOpen);
			$('#'+treeViewId).tree('openNode', node);
		}
        //var tree_data = $('#'+treeViewId).tree('getTree');
		
		/*for ( var id in toOpen ){
			var node = $('#'+treeViewId).tree('getNodeById', id);
			$('#'+treeViewId).tree('openNode', node);
		}*/
		
        //$("#"+treeViewId).tree('closeNode', tree_data.children[0],false);
    });
	
	$("#menu").show();

}

var toOpen = null;

function cleanData(data, profileName){

    var map = {};
	
	var p = getUrlParameter("PATH") || getUrlParameter("p") || getUrlParameter("sid") || getUrlParameter("docNum") || "";

    for(i=0;i<data.length;i++){

        data[i]["load_on_demand"]=true;
        data[i].nodeId = data[i].path;
        data[i].name = data[i].name;
        data[i].parent = data[i].path.replace(/\/[^/]+$/g,"");

        map[data[i].nodeId] = data[i];
    }

    var result = [];

    for(i=0;i<data.length;i++){

        var parent = data[i].parent;

        if (map[parent]){
            map[parent].children = map[parent].children || [];
            map[parent].children.push(data[i]);

            delete map[parent]["load_on_demand"];
			
        } else {
            result.push(data[i]);
        }
		
		if (p && (data[i].nodeId.endsWith(p) || data[i].id == p) ){
			toOpen = map[parent].id;
		}
    }

    return result;
}



var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
};

function buildRequestUrl(node) {
    if (node){
        var url = "query/treeview?wt=data&PATH=" +  encodeURIComponent(node.path) + "/*";
		return url;
    } else {
        var p = getUrlParameter("PATH") || getUrlParameter("p") || "";
        var sid = getUrlParameter("sid") || getUrlParameter("docNum") || "";
        var url= "loadRootNode?PATH="+ p + "&sid=" + sid ;
		return url;
    }
}

function buildRequestContentUrl(node) {
	var url = "loadContentTreeView?sort=classifica_sort asc,name asc&p=" +  encodeURIComponent(node.path) ;
    return url;
}

function htmlEncode(value){
    return $('<div/>').text(value).html();
}

function htmlDecode(value){
    return $('<div/>').html(value).text();
}



