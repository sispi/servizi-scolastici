import { Response } from '/static/portal/js/models/response.model.js?no-cache';
export function FindAllTasksForUser(pageNumber, pageSize){
    var response = new Response;
    $.ajax({
        //url: "/bpm/v1/tasks?activeOnly=true&assignedAs=PotentialOwner&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
        url: "/bpm/v1/tasks?activeOnly=true&orderBy=startTs:DESC&pageNumber=" + pageNumber + "&pageSize=" + pageSize,
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

export function CompleteTask(object, taskId){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/bpm/v1/tasks/" + taskId + "/actions/complete",
        async: false,
        type: "POST",
        data: JSON.stringify(object),
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

export function SaveTask(object, taskId){
    var response = new Response;
    $.ajax({
        headers: { 
            'Content-Type': 'application/json' 
        },
        url: "/bpm/v1/tasks/" + taskId + "/actions/save",
        async: false,
        type: "POST",
        data: JSON.stringify(object),
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

export function FindAllTasksForTheInstitution(pageNumber, pageSize, orderBy){
    var order = "";
    if(orderBy != null){
        order = "&orderBy=" + orderBy;
    }
    var response = new Response;
    $.ajax({
        url: "/bpm/v1/tasks?status=Ready&status=InProgress&assignedAs=PotentialOwner&pageNumber=" + pageNumber + "&pageSize=" + pageSize + order,
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

