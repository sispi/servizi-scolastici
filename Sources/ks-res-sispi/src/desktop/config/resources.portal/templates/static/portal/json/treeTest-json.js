export function TreeTestJson(){
    var tree = {
        "main": {
          "tree": true,
          "title": "Documentale",
          "sections": [
            {
              "name": "Documentale",
              "menuItems": [
                {
                  "name": "documenti protocollati",
                  "url": "documenti/listaRisultati?fq=NUM_PG:*"
                },
                {
                  "name": "documenti registrati",
                  "url": "documenti/listaRisultati?fq=N_REGISTRAZ:*"
                },
                {
                  "name": "documenti",
                  "url": "documenti/listaRisultati"
                },
                {
                  "name": "lista vue",
                  "class": "k-green-badge",
                  "icon": "glyphicon-th-list",
                  "title": "lista dei documenti",
                  "url": "reportvue",
                  "target": "#main",
                  "badge": "documento",
                  "roles": [
                    "everyone"
                  ],
                  "buttons": [
                    {
                      "icon": "glyphicon-search",
                      "title": "ricerca",
                      "url": "autoform?qt=test&action=reportvue",
                      "roles": [
                        "everyone"
                      ]
                    },
                    {
                      "icon": "glyphicon-plus",
                      "title": "nuovo documento",
                      "url": "documenti/newdoc",
                      "roles": [
                        "everyone"
                      ]
                    }
                  ]
                },
                {
                  "name": "userinfo",
                  "icon": "glyphicon-user",
                  "url": "userinfo"
                },
                {
                  "name": "pagina test",
                  "icon": "glyphicon-th-list",
                  "title": "pagina test",
                  "url": "vuetest",
                  "badge": "related",
                  "roles": [
                    "everyone"
                  ]
                }
              ]
            },
            {
              "name": "Processi",
              "menuItems": [
                {
                  "name": "deployments",
                  "url": "bpm/deployments"
                },
                {
                  "name": "configurations",
                  "url": "bpm/configurations"
                },
                {
                  "name": "instances",
                  "url": "bpm/instances"
                },
                {
                  "name": "tasks",
                  "url": "bpm/tasks"
                },
                {
                  "name": "events",
                  "url": "bpm/events"
                },
                {
                  "name": "bookmarks",
                  "url": "bpm/bookmarks"
                }
      
              ]
            },
            {
              "name": "Reports",
              "menuItems": [
                {
                  "name": "lista report",
                  "icon": "glyphicon-th-list",
                  "url": "reportlist",
                  "target": "#main"
                },
                {
                  "name": "report ftl",
                  "icon": "glyphicon-th-list",
                  "url": "report?qt=test",
                  "target": "#main"
                },
                {
                  "name": "solr select",
                  "icon": "glyphicon-th-list",
                  "url": "report?qt=select",
                  "target": "#main"
                },
                {
                  "name": "solr select vue",
                  "icon": "glyphicon-th-list",
                  "url": "reportvue?qt=select",
                  "target": "#main"
                }
              ]
            },
            {
              "name": "Portale del cittadino",
              "menuItems": [
                {
                  "name": "Portale",
                  "icon": "glyphicon-th-list",
                  "url": "portalecittadino/index"
                }
              ]
            }
          ]
        },
      
        "protocollo" : {
      
          "title" : "Documentale",
          "useJtree" : false,
      
          "hidden" : [{
            "method" : "post",
            "roles" : ["PROTOCOLLATORI"]
          }
          ],
      
          "sections" : [ {
            "name" : "Documentale",
            "url" : "",
            "tooltip" : "Documentale",
            "roles" : [ "everyone" ],
            "menuItems" : [ {
              "name" : "lista (vue)",
              "icon" : "glyphicon-th-list",
              "tooltip" : "I miei documenti",
              "url" : "reportvue",
              "target" : "#main",
              "badge" : "badge-test",
              "roles" : [ "everyone" ],
              "buttons" : [ {
                "icon" : "glyphicon-search",
                "tooltip" : "new",
                "url" : "reports/testvue/form",
                "roles" : [ "everyone" ]
              }, {
                "icon" : "glyphicon-plus",
                "tooltip" : "edit",
                "url" : "/AppDoc/reports/testvue/form",
                "roles" : [ "everyone" ]
              } ],
              "attributes" : {
                "class" : "jClass",
                "target-data" : "page-content",
                "update-data" : "true"
              }
            }, {
              "name" : "lista (ftl)",
              "icon" : "glyphicon-th-list",
              "tooltip" : "I miei documenti",
              "url" : "reports/test",
              "target" : "#main",
              "badge" : "badge-test",
              "roles" : [ "everyone" ],
              "buttons" : [ {
                "icon" : "glyphicon-search",
                "tooltip" : "new",
                "url" : "reports/test/form",
                "roles" : [ "everyone" ]
              }, {
                "icon" : "glyphicon-plus",
                "tooltip" : "edit",
                "url" : "/AppDoc/reports/test/form",
                "roles" : [ "everyone" ]
              } ],
              "attributes" : {
                "class" : "jClass",
                "target-data" : "page-content",
                "update-data" : "true"
              }
            }, {
              "name" : "userinfo",
              "icon" : "glyphicon-search",
              "tooltip" : "Ricerca fascicoli",
              "url" : "userinfo",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              }
            }, {
              "name" : "pagina test",
              "icon" : "glyphicon-th-list",
              "tooltip" : "pagina test",
              "url" : "vuetest",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              }
            }, {
              "name" : "nuovo doc",
              "url" : "documenti/newdoc"
            } ]
          }, {
            "name" : "Documenti con attivitÃ ",
            "url" : "",
            "tooltip" : "AttivitÃ ",
            "roles" : [ "everyone" ],
            "menuItems" : [ {
              "name" : "Documenti in entrata",
              "icon" : "glyphicon-check",
              "tooltip" : "Documenti in entrata",
              "url" : "/AppDoc/doc/taskList?sort=modified_on%20desc&title=label.taskListIngresso",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              },
              "otherHtml" : "<span id=\"countPotentialActivity\" class=\"badge badge-notify\" hidden=\"\" style=\"display: none;\">&nbsp;0</span>"
            }, {
              "name" : "Documenti in carico",
              "icon" : "glyphicon-check",
              "tooltip" : "Documenti in carico",
              "url" : "/AppDoc/doc/taskAssigned?sort=modified_on%20desc&title=label.taskListIncarico",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              },
              "otherHtml" : "<span id=\"countActivity\" class=\"badge badge-notify\" hidden=\"\" style=\"display: none;\">&nbsp;0</span>"
            }, {
              "name" : "Documenti monitorati",
              "icon" : "glyphicon-check",
              "tooltip" : "Documenti monitorati",
              "url" : "/AppDoc/doc/taskStakeHolder?sort=modified_on%20desc&title=label.taskListStakeholder",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              }
            } ]
          }, {
            "name" : "AttivitÃ ",
            "url" : "#",
            "tooltip" : "AttivitÃ ",
            "roles" : [ "everyone" ],
            "menuItems" : [ {
              "name" : "AttivitÃ  in ingresso",
              "icon" : "glyphicon-share",
              "tooltip" : "AttivitÃ  in ingresso",
              "url" : "/AppDoc/taskList",
              "roles" : [ "everyone" ]
            }, {
              "name" : "AttivitÃ  in carico",
              "icon" : "glyphicon-check",
              "tooltip" : "AttivitÃ  in carico",
              "url" : "/AppDoc/taskAssigned",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Task in scadenza",
              "icon" : "glyphicon-bell",
              "tooltip" : "Task in scadenza",
              "url" : "/AppDoc/callTaskNotitications",
              "roles" : [ "everyone" ]
            }, {
              "name" : "AttivitÃ  di firma",
              "icon" : "glyphicon-pencil",
              "tooltip" : "AttivitÃ  di firma",
              "url" : "/AppDoc/getTaskFirmaRemotaByUser",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Messaggi",
              "icon" : "glyphicon-envelope",
              "tooltip" : "Messaggi",
              "url" : "/AppDoc/viewListMessages",
              "roles" : [ "everyone" ]
            } ]
          }, {
            "name" : "Processi",
            "url" : "#",
            "tooltip" : "Gestione processi",
            "roles" : [ "everyone" ],
            "menuItems" : [ {
              "name" : "Processi menÃ¹",
              "icon" : "glyphicon-user",
              "tooltip" : "Processi menÃ¹",
              "url" : "/AppDoc/viewMenuProcess",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Ricerca Instanza",
              "icon" : "glyphicon-search",
              "tooltip" : "Ricerca Instanza",
              "url" : "/AppDoc/searchProcess",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Cerca preferiti",
              "icon" : "glyphicon-star",
              "tooltip" : "Cerca preferiti",
              "url" : "/AppDoc/searchPreferredProcesses",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Esporta",
              "icon" : "glyphicon-export",
              "tooltip" : "Esporta",
              "url" : "/AppDoc/exportData",
              "roles" : [ "everyone" ]
            } ]
          }, {
            "name" : "Gestione",
            "closed" : true,
            "url" : "#",
            "tooltip" : "Gestione",
            "roles" : [ "everyone" ],
            "menuItems" : [ {
              "name" : "Db Navigator",
              "icon" : "glyphicon-share",
              "tooltip" : "Db Navigator",
              "url" : "/AppDoc/dbNavigator",
              "roles" : [ "everyone" ],
              "buttons" : [ {
                "icon" : "",
                "tooltip" : "",
                "url" : "",
                "roles" : [ ]
              } ]
            }, {
              "name" : "Processi",
              "icon" : "glyphicon-transfer blue",
              "tooltip" : "Processi",
              "url" : "/AppDoc/processList",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Code Dlq",
              "icon" : "glyphicon-align-justify blue",
              "tooltip" : "Code Dlq",
              "url" : "/AppDoc/listaCodeDlq",
              "roles" : [ "everyone" ]
            }, {
              "name" : "Gruppi di lavoro",
              "icon" : "glyphicon-th-list",
              "tooltip" : "Gruppi di lavoro",
              "url" : "/AppDoc/workgroupForm",
              "roles" : [ "everyone" ],
              "attributes" : {
                "target-data" : "page-content",
                "update-data" : "true"
              }
            } ]
          } ]
        }
      };
      return tree;
      
}