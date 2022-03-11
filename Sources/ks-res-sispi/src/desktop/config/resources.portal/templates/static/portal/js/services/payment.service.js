import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindAllPaymentByUser(){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/user",
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

export function FindAllPaymentByUserAndReference(bpmInstanceId, processId){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/" + bpmInstanceId +"/" + processId + "/PORTAL/user",
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

export function DownloadReceipt(format, uuid){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/" + uuid + "/receipt?format=" + format,
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
            };
        },
    });
    return response;
}

export function getPaymentByUuId(uuid){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/" + uuid,
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
            };
        },
    });
    return response;
}
