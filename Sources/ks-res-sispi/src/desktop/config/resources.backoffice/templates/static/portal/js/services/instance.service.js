import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindAllByTasksActiveAndRoot(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?activeOnly=true&rootOnly=true&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountActiveInstances(processId){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?activeOnly=true&rootOnly=true&processId=" + processId,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountCompletedInstances(processId){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?activeOnly=false&status=2&rootOnly=true&processId=" + processId,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CreateInstance(instance){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/instance",
        async: false,
        type: "POST",
        data: JSON.stringify(instance),
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            };
        },
    });
    return response;
}

export function UpdateInstance(instance){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/instance",
        async: false,
        type: "PUT",
        data: JSON.stringify(instance),
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            };
        },
    });
    return response;
}

export function CreateBpmInstance(instance){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/bpm/v1/instances",
        async: false,
        type: "POST",
        data: JSON.stringify(instance),
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            };
        },
    });
    return response;
}

export function MarkChatAsRead(id){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/bpm/v1/instances/" + id + "/chat/read",
        async: false,
        type: "PUT",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function PublishMessage(id, message){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/bpm/v1/instances/" + id + "/chat/messages",
        async: false,
        type: "POST",
        data: JSON.stringify(message),
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function GetChatMessages(instanceId){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances/" + instanceId + "/chat/messages?fetch=*",
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function DeletePortalInstance(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/" + id,
        async: false,
        type: "DELETE",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function FindAllInstanceTasks(instanceId, activeOnly){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances/" + instanceId + "/tasks?activeOnly=" + activeOnly,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CanCompileInstance(instanceType, proceedingId){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/can-compile/" + instanceType + "/" + proceedingId,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountInstances(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/count",
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountInstanceDocuments(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/document/count",
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountInstanceByStatus(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/count/byStatus",
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function FindAllInstances(pageNumber, pageSize, orderBy, fetch, rootOnly, searchableOnly){
    var order = "";
    var fetchVariables = "";
    var root = "";
    var searchable = "";
    if(orderBy != null){
        order = "&orderBy=" + orderBy;
    }
    if(fetch != null){
        fetchVariables = "&fetch=" + fetch;
    }
    if(rootOnly){
        root = "&rootOnly=true";
    } else {
        root = "&rootOnly=false";
    }
    if(searchableOnly){
        searchable = "&searchableOnly=true";
    } else {
        searchable = "&searchableOnly=false";
    }
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?pageNumber=" + pageNumber + "&pageSize=" + pageSize + order + fetchVariables + root + searchable,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}

export function CountInstanceGroupByMonth(startDate, endDate){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/count/byType/" + startDate + "/" + endDate,
        async: false,
        type: "GET",
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            }
        },
    });
    return response;
}
