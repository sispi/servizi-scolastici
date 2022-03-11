import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function GetProceedingsByServiceId(serviceId){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/proceeding/service/" + serviceId,
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

export function GetOneProceeding(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/proceeding/" + id,
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

export function CreateProceeding(proceeding){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/proceeding",
        async: false,
        type: "POST",
        data: JSON.stringify(proceeding),
        success: function (data, status) {
            response = {
                data,
                status,
                error: null
            };
            console.log(response);
        },
        error: function (status, error) {
            response = {
                data: null,
                status,
                error
            };
            console.log(response);
        },
    });
    return response;
}

export function UpdateProceeding(proceeding){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/proceeding",
        async: false,
        type: "PUT",
        data: JSON.stringify(proceeding),
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

export function DeleteProceeding(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/proceeding/" + id,
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

export function FindAllProceedingsPaged(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/proceeding?orderBy=id:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
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

export function FindAllProceedings(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/proceeding",
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
