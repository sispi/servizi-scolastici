import { Response } from '/static/portal/js/models/response.model.js?no-cache';

export function FindAllInstancesNotSended(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/portale/v1/instance?sent=false&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
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

export function FindAllCompletedInstances(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?activeOnly=false&rootOnly=true&status=2&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        //url: "/bpm/v1/instances?activeOnly=false&rootOnly=true&status=2&orderBy=startTs:DESC",
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

export function FindAllInstancesToCompileByUser(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?rootOnly=true&searchableOnly=true&status=1&taskOwner=true&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        //url: "/bpm/v1/instances?rootOnly=true&searchableOnly=true&status=1&taskOwner=true&orderBy=startTs:DESC",
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

export function FindAllInstancesToCompileByInstitution(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/instances?rootOnly=true&searchableOnly=true&status=1&taskOwner=false&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        //url: "/bpm/v1/instances?rootOnly=true&searchableOnly=true&status=1&taskOwner=false&orderBy=startTs:DESC",
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
