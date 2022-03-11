import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function FindAllChannelPaged(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/channels?orderBy=id:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
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

export function DeleteChannel(id){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/channels/" + id,
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

export function CreateChannel(channel){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/pagamenti/v1/channels",
        async: false,
        type: "POST",
        data: JSON.stringify(channel),
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

export function UpdateChannel(channel, id){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/pagamenti/v1/channels/" + id,
        async: false,
        type: "PUT",
        data: JSON.stringify(channel),
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

export function FindOneChannel(id){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/channels/" + id,
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

export function CreateChannelConfiguration(configuration, id){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/pagamenti/v1/channels/" + id + "/configurations",
        async: false,
        type: "POST",
        data: JSON.stringify(configuration),
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
