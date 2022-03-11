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

// export function CreateBpmInstance(instance){
//     var response = new Response;
//     $.ajax({
//         headers: { 
//             'Content-Type': 'application/json' 
//         },
//         url: "/bpm/v1/instances",
//         async: false,
//         type: "POST",
//         data: JSON.stringify(instance),
//         success: function (data, status) {
//             response = {
//                 data,
//                 status,
//                 error: null
//             };
//         },
//         error: function (status, error) {
//             response = {
//                 data: null,
//                 status,
//                 error
//             };
//         },
//     });
//     return response;
// }

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

export function findCompilingInstance(instanceType, proceedingId){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance/find-can-compile/" + instanceType + "/" + proceedingId,
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
