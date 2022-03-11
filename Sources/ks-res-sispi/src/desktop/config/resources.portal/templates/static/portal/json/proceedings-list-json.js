export function ProceedingsListJson(){
    var proceedingsList = [
        {
           "id":1,
           "name":"AAA Autenticazione",
           "position":1,
           "parentId":null,
           "logo":"ComeAutenticarsi_icon.png",
           "code":"A001",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":2,
           "name":"Accesso agli atti",
           "position":2,
           "parentId":null,
           "logo":"AccessoAgliAtti_icon.png",
           "code":"A002",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":3,
           "name":"Ambiente",
           "position":3,
           "parentId":null,
           "logo":"Ambiente_icon.png",
           "code":"A003",
           "valid":true,
           "externalService":false,
           "link":null,
           "children":[
              {
                 "id":4,
                 "name":"Caccia",
                 "position":1,
                 "parentId":3,
                 "children":[
                    
                 ],
                 "logo":"",
                 "code":"A003-1",
                 "agencyProceedingss":[
                    {
                       "id":1,
                       "title":"Nulla Osta installazione appostamento fisso di caccia in zona umida",
                       "body":"Le attestazioni di versamento devono essere consegnate al Servizio Ambiente ed Aree Verdi entro il 31 gennaio.",
                       "proceedingsBpmCode":"FlussoTestPortaleJMSmonza1.0",
                       "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolarea favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                       "costs":"2 marche da bollo vigenti; Euro 219,00 per ogni appostamento fisso",
                       "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                       "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                       "timeNeeded":"90 gg.",
                       "accountableStaff":"Mario Marino",
                       "accountableOffice":"Ufficio del Personale",
                       "operatorStaff":"Nicola Savino",
                       "isActive":true,
                       "activeCommunication":null,
                       "isOnline":true,
                       "filingPlan":null,
                       "updating":true,
                       "multipleInstance":true,
                       "version":1,
                       "startDate":"2018-01-01T00:00:00Z",
                       "endDate":"2021-06-26T23:00:00Z",
                       "steps":"attachments,paymentModules,payments,mandatoryModules,send,result",
                       "headerHtml":null,
                       "footerHtml":null,
                       "minAge":0,
                       "crs":2,
                       "customServiceType":false,
                       "customServiceTypeUrl":null,
                       "customServiceTypeLabel":null,
                       "maxAge":0,
                       "proceedings":{
                          "id":1,
                          "title":"Nulla Osta installazione appostamento fisso di caccia in zona umida",
                          "body":"Le attestazioni di versamento devono essere consegnate al Servizio Ambiente ed Aree Verdi entro il 31 gennaio.",
                          "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolare a favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                          "costs":"2 marche da bollo vigenti; Euro 219,00 per ogni appostamento fisso",
                          "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                          "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                          "timeNeeded":"90 gg.",
                          "tacitConsent":false,
                          "startCommunication":false,
                          "daysIntegrationWait":0,
                          "adviceRequest":false,
                          "finalMeasure":false,
                          "sendDocuments":false,
                          "finalAction":false,
                          "maxProceedingsDays":90,
                          "filingPlanCode":"",
                          "service":{
                             "id":4,
                             "name":"Caccia",
                             "position":1,
                             "logo":"",
                             "code":"A003-1",
                             "parentId":3
                          }
                       }
                    },
                    {
                       "id":2,
                       "title":"Nulla Osta Porto d'armi",
                       "body":"Le attestazioni di versamento devono essere consegnate al Servizio Ambiente ed Aree Verdi entro il 31 gennaio.",
                       "proceedingsBpmCode":"FlussoTestPortaleJMSmonza1.0",
                       "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolare a favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                       "costs":"2 marche da bollo vigenti; Euro 219,00 per ogni appostamento fisso",
                       "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                       "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                       "timeNeeded":"90 gg.",
                       "accountableStaff":"Mario Marino",
                       "accountableOffice":"Ufficio del Personale",
                       "operatorStaff":"Nicola Savino",
                       "isActive":true,
                       "activeCommunication":null,
                       "isOnline":true,
                       "filingPlan":null,
                       "updating":true,
                       "multipleInstance":false,
                       "version":1,
                       "startDate":"2018-01-01T00:00:00Z",
                       "endDate":"2021-10-29T23:00:00Z",
                       "steps":"attachments,result,send",
                       "headerHtml":null,
                       "footerHtml":null,
                       "minAge":0,
                       "crs":2,
                       "customServiceType":false,
                       "customServiceTypeUrl":null,
                       "customServiceTypeLabel":null,
                       "maxAge":0,
                       "proceedings":{
                          "id":2,
                          "title":"Nulla Osta Porto d'armi",
                          "body":"Le attestazioni di versamento devono essere consegnate al Servizio Ambiente ed Aree Verdi entro il 31 gennaio.",
                          "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolare a favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                          "costs":"3 marche da bollo vigenti",
                          "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                          "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                          "timeNeeded":"90 gg.",
                          "tacitConsent":false,
                          "startCommunication":false,
                          "daysIntegrationWait":20,
                          "adviceRequest":false,
                          "finalMeasure":false,
                          "sendDocuments":false,
                          "finalAction":false,
                          "maxProceedingsDays":30,
                          "filingPlanCode":"",
                          "service":{
                             "id":4,
                             "name":"Caccia",
                             "position":1,
                             "logo":"",
                             "code":"A003-1",
                             "parentId":3
                          }
                       }
                    }
                 ],
                 "externalService":false,
                 "link":null,
                 "used":false,
                 "leaf":true
              },
              {
                 "id":5,
                 "name":"Pesca",
                 "position":2,
                 "parentId":3,
                 "children":[
                    
                 ],
                 "logo":"",
                 "code":"A003-1",
                 "agencyProceedingss":[
                    {
                       "id":3,
                       "title":"Permesso per la pesca in acque internazionali",
                       "body":"Le attestazioni di versamento devono essere consegnate al Servizio guardia costiera entro il 31 gennaio.",
                       "proceedingsBpmCode":"FlussoTestPortaleJMSmonza1.0",
                       "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolare a favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                       "costs":"2 marche da bollo vigenti; Euro 219,00 per ogni appostamento fisso",
                       "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                       "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                       "timeNeeded":"90 gg.",
                       "accountableStaff":"Mario Marino",
                       "accountableOffice":"Ufficio del Personale",
                       "operatorStaff":"Nicola Savino",
                       "isActive":false,
                       "activeCommunication":null,
                       "isOnline":false,
                       "filingPlan":null,
                       "updating":false,
                       "multipleInstance":false,
                       "version":1,
                       "startDate":"2018-01-01T00:00:00Z",
                       "endDate":"2018-12-31T00:00:00Z",
                       "steps":null,
                       "headerHtml":null,
                       "footerHtml":null,
                       "minAge":0,
                       "crs":2,
                       "customServiceType":false,
                       "customServiceTypeUrl":null,
                       "customServiceTypeLabel":null,
                       "maxAge":0,
                       "proceedings":{
                          "id":3,
                          "title":"Permesso per la pesca in acque internazionali",
                          "body":"Le attestazioni di versamento devono essere consegnate al Servizio guardia costiera entro il 31 gennaio.",
                          "applicantRequirements":"Attualmente non vengono rilasciati nuovi nulla osta, consentito solo la sostituzione del titolare a favore di sostituti dello stesso appostamento fisso risultanti dalla autorizzazione per la stagione di caccia precedente e titolari di licenza di caccia.",
                          "costs":"2 marche da bollo vigenti",
                          "attachments":"presentazione di domanda (Modulo richiesta); attestazione di versamento in c/c postale.",
                          "howtoSubmit":"Domanda al Sindaco e domanda all'Amministrazione Provinciale, entro il 31 ottobre.",
                          "timeNeeded":"90 gg.",
                          "tacitConsent":false,
                          "startCommunication":false,
                          "daysIntegrationWait":0,
                          "adviceRequest":false,
                          "finalMeasure":false,
                          "sendDocuments":false,
                          "finalAction":false,
                          "maxProceedingsDays":20,
                          "filingPlanCode":"",
                          "service":{
                             "id":4,
                             "name":"Caccia",
                             "position":1,
                             "logo":"",
                             "code":"A003-1",
                             "parentId":3
                          }
                       }
                    }
                 ],
                 "externalService":false,
                 "link":null,
                 "used":false,
                 "leaf":true
              },
              {
                 "id":6,
                 "name":"Verde",
                 "position":3,
                 "parentId":3,
                 "children":[
                    
                 ],
                 "logo":"",
                 "code":"A003-1",
                 "agencyProceedingss":[
                    
                 ],
                 "externalService":false,
                 "link":null,
                 "used":false,
                 "leaf":true
              },
              {
                 "id":7,
                 "name":"Zone naturali",
                 "position":4,
                 "parentId":3,
                 "children":[
                    
                 ],
                 "logo":"",
                 "code":"A003-1",
                 "agencyProceedingss":[
                    
                 ],
                 "externalService":false,
                 "link":null,
                 "used":false,
                 "leaf":true
              }
           ]
        },
        {
           "id":8,
           "name":"Anagrafe e Stato Civile",
           "position":1,
           "parentId":null,
           "logo":"AnagrafeStatoCivile_icon.png",
           "code":"A004",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":9,
           "name":"Attività impiantistica",
           "position":1,
           "parentId":null,
           "logo":"Impiantistica_icon.png",
           "code":"A005",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":10,
           "name":"Attività di intrattenimento e sportive",
           "position":1,
           "parentId":null,
           "logo":"SportSpettacolo_icon.png",
           "code":"A006",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":14,
           "name":"Attività sanitarie e socio-assistenziali",
           "position":1,
           "parentId":null,
           "logo":"SanitaSocioAssistenza_icon.png",
           "code":"A007",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":15,
           "name":"Attività turistico-ricettive",
           "position":1,
           "parentId":null,
           "logo":"AttivitaTurismoRicettive_icon.png",
           "code":"A008",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":16,
           "name":"Casa - Edilizia",
           "position":1,
           "parentId":null,
           "logo":"CasaEdilizia_icon.png",
           "code":"A009",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":23,
           "name":"Concessione locali comunali",
           "position":1,
           "parentId":null,
           "logo":"ConcessioneLocaliComunali_icon.png",
           "code":"A010",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":28,
           "name":"Elettorale",
           "position":1,
           "parentId":null,
           "logo":"elettorale_icon.png",
           "code":"A011",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":29,
           "name":"Istruzione e infanzia",
           "position":1,
           "parentId":null,
           "logo":"istruzioneinfanzia_icon.png",
           "code":"A012",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":37,
           "name":"Lavorare per l'Amministrazione",
           "position":1,
           "parentId":null,
           "logo":"lavoroamministrazione.png",
           "code":"A013",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":38,
           "name":"Occupazione suolo pubblico",
           "position":1,
           "parentId":null,
           "logo":"occupazionesuolo_icon.png",
           "code":"A014",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":39,
           "name":"Patrimonio comunale",
           "position":1,
           "parentId":null,
           "logo":"patrimonioculturale_icon.png",
           "code":"A015",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":40,
           "name":"Permessi di circolazione",
           "position":1,
           "parentId":null,
           "logo":"permessicircolazione.png",
           "code":"A016",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":41,
           "name":"Sostegno a persone con disabilità",
           "position":1,
           "parentId":null,
           "logo":"sostegnodisabilita_icon.png",
           "code":"A017",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":42,
           "name":"Sostegno ad iniziative socio-culturali",
           "position":1,
           "parentId":null,
           "logo":"sostegnoattivitaculturali_icon.png",
           "code":"A018",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":43,
           "name":"Sostegno alle persone anziane",
           "position":1,
           "parentId":null,
           "logo":"sostegnoanziani_icon.png",
           "code":"A019",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":44,
           "name":"Strade e viabilità",
           "position":1,
           "parentId":null,
           "logo":"stradeviabilita_icon.png",
           "code":"A020",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":49,
           "name":"Urbanistica",
           "position":1,
           "parentId":null,
           "logo":"urbanistica_icon.png",
           "code":"A021",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        },
        {
           "id":50,
           "name":"Altro",
           "position":1,
           "parentId":null,
           "logo":"altro_icon.png",
           "code":"A022",
           "valid":true,
           "externalService":false,
           "link":null,
           "children": [
           ]
        }
     ];
     return proceedingsList;
}