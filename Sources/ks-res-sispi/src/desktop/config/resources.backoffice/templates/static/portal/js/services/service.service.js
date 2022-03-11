import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function GetServicesTree(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/service/tree",
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

export function CreateService(service){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/service",
        async: false,
        type: "POST",
        data: JSON.stringify(service),
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

export function UpdateService(service){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/service",
        async: false,
        type: "PUT",
        data: JSON.stringify(service),
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

export function DeleteService(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/service/" + id,
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
            };
        },
    });
    return response;
}