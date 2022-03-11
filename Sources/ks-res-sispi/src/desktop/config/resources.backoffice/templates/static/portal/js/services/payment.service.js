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

export function FindAllPaymentsPaged(pageNumber, pageSize, orderBy){
    var order = "";
    if(orderBy != null){
        order = "&orderBy=" + orderBy;
    }
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances?pageNumber=" + pageNumber + "&pageSize=" + pageSize + order,
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

export function CountPayments(){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/count",
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

export function PaymentsAmount(){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/total",
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

export function FindAllPaymentsTotalByMonth(startDate, endDate){
    var response = new Response;
    $.ajax({
        url: "/pagamenti/v1/paymentInstances/total/byMonth/" + startDate + "/" + endDate,
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
