import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function SendContactUsEmail(contactUs){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/portale/v1/mail/contactUs",
        async: false,
        type: "POST",
        data: JSON.stringify(contactUs),
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