import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function FindAllProviders(){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/providers/list",
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