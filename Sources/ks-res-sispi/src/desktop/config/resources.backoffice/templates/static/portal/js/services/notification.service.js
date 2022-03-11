import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindAllNotifications(){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/notification",
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

export function countNotification(){
    const response = FindAllNotifications();
    const data = response.data;
    console.log(data);
}
