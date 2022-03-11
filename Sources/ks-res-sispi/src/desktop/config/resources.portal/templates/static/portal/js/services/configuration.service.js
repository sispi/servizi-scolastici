import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function GetOneConfiguration(id){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/configurations/" + id,
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

export function FindAllChannelConfiguration(channelId){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/configurations/channels/" + channelId,
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

export function DeleteChannelConfiguration(id){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/configurations/" + id,
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

export function FindOneChannelConfiguration(configurationId){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/configurations/" + configurationId,
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

export function UpdateChannelConfiguration(configuration, id){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/pagamenti/v1/configurations/" + id,
        async: false,
        type: "PUT",
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

export function UpdateDefaultChannelConfiguration(configurationId, channelId){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/pagamenti/v1/configurations/" + configurationId + "/channels/" + channelId,
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
            };
        },
    });
    return response;
}
