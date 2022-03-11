import { Response } from '/static/portal/js/models/response.model.js?no-cache';

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
