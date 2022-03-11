import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function FindUserByUsername(username){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/user/" + username,
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

export function UpdateUser(user){
    var response = new Response;
    $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        url: "/portale/v1/user/" +user.USER_ID, 
        async: false,
        type: "PATCH",
        data: JSON.stringify(user),
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

export function CreateUser(user){
    var response = new Response;
    $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        url: "/portale/v1/user",
        async: false,
        type: "POST",
        data: JSON.stringify(user),
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
