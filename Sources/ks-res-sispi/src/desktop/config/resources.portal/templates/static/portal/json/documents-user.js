export function DocumentsUserSentJson() {
    var documentSent = {
        "count": 2,
        "pageNumber": 1,
        "pageSize": 4096,
        "data":[
            {
                "id": 1,
                "agencyModule": {
                    "name": "Module 1",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": null
                },
                "agencyProceedings": {
                    "title": "AP TEST"
                },
                "documentFile": "file1",
                "documentSize": 100000,
                "uploadDate": "2021-01-01 11:12",
                "acquisitionDate": "2021-01-01 11:13",
                "receivedDate": null,
                "protocolNumber": 1,
                "protocolYear": 2021,
                "direction": "sent"
            },
            {
                "id": 2,
                "agencyModule": {
                    "name": "Module 2",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": "2021-01-02 11:21"
                },
                "agencyProceedings": {
                    "title": "AP TEST"
                },
                "documentFile": "file2",
                "documentSize": 200000,
                "uploadDate": "2021-01-02 11:22",
                "acquisitionDate": "2021-01-02 15:23",
                "receivedDate": null,
                "protocolNumber": 2,
                "protocolYear": 2021,
                "direction": "sent"
            }
        ]
    };
    return documentSent;
}

export function DocumentsUserReceivedJson() {
    var documentReceived = {
        "count": 1,
        "pageNumber": 1,
        "pageSize": 4096,
        "data":[
            {
                "id": 3,
                "agencyModule": {
                    "name": "Module 3",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": "2021-01-03 11:31"
                },
                "agencyProceedings": {
                    "title": "AP TEST"
                },
                "documentFile": "file3",
                "documentSize": 300000,
                "uploadDate": "2021-01-03 11:32",
                "acquisitionDate": null,
                "receivedDate": "2021-01-03 11:33",
                "protocolNumber": 3,
                "protocolYear": 2021,
                "direction": "received"
            }
        ]
    };
    return documentReceived;
}

export function DocumentsUserJson() {
    var documents = {
        "count": 2,
        "pageNumber": 1,
        "pageSize": 4096,
        "data":[
            {
                "id": 1,
                "agencyModule": {
                    "name": "Module 1",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": null
                },
                "agencyProceedings": {
                    "title": "AP TEST"
                },
                "documentFile": "file1",
                "documentSize": 100000,
                "uploadDate": "2021-01-01 11:12",
                "acquisitionDate": "2021-01-01 11:13",
                "receivedDate": null,
                "protocolNumber": 1,
                "protocolYear": 2021,
                "direction": "sent"
            },
            {
                "id": 2,
                "agencyModule": {
                    "name": "Module 2",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": "2021-01-02 11:21"
                },
                "agencyProceedings": {
                    "title": "Nulla Osta Porto d'armi"
                },
                "documentFile": "file2",
                "documentSize": 200000,
                "uploadDate": "2021-01-02 11:22",
                "acquisitionDate": "2021-01-02 15:23",
                "receivedDate": null,
                "protocolNumber": 2,
                "protocolYear": 2021,
                "direction": "sent"
            },
            {
                "id": 3,
                "agencyModule": {
                    "name": "Module 3",
                    "description": "Module description"
                },
                "proceedingsInstance": {
                    "startDate": "2021-01-02 11:21"
                },
                "agencyProceedings": {
                    "title": "Nulla Osta Porto d'armi"
                },
                "documentFile": "file3",
                "documentSize": 200000,
                "uploadDate": "2021-01-02 11:22",
                "acquisitionDate": "2021-01-02 15:23",
                "receivedDate": null,
                "protocolNumber": 2,
                "protocolYear": 2021,
                "direction": "received"
            }
        ]
    };
    return documents;
}