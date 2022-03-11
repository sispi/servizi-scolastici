import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindUoByGroupId(groupId){
    var response = new Response;
    $.ajax({
        url: "/docer/v1/gruppi/" + groupId,
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