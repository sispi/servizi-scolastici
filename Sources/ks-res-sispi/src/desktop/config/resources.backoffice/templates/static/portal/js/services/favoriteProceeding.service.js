import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function CreateFavoriteProceeding(id){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/favoriteProceeding",
        async: false,
        type: "POST",
        data: JSON.stringify({"proceedingId": id}),
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

export function IsPresentFavoriteProceeding(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/favoriteProceeding/" + id,
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

export function DeleteFavoriteProceeding(id){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/favoriteProceeding/" + id,
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

export function FindAllFavoriteProceedings(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/favoriteProceeding",
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
