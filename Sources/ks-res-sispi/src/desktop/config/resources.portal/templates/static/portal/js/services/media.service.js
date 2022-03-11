import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindAllMediaPaged(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/media",
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

export function DeleteMedia(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/media/" + id,
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

export function CreateMedia(media){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/media",
        async: false,
        type: "POST",
        data: JSON.stringify(media),
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

export function FindOneMedia(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/media/" + id,
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

export function UpdateMedia(media){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/media",
        async: false,
        type: "PUT",
        data: JSON.stringify(media),
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

export function FindByKey(myKey){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/media/key/" + myKey,
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