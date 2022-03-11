import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function FindDocumentByYear(year, portalDirection){
    if(year=="Tutti"){
        year = "*";
    }
    var response = new Response;
    $.ajax({
        url: "/docer/v1/documenti?fq=CREATED:"+year+"-*&PORTAL_DIRECTION="+portalDirection+"&sort=CREATED%20desc&rows=500",
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

export function FindDocumentByProceed(portalId){
    var response = new Response;
    $.ajax({
        url: "/docer/v1/documenti?fq=PORTAL_ID:"+portalId+"&sort=CREATED%20desc",
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
