<#macro autocompletion type mid name value des mandatory="true" class="" key="" q="*" fq="ENABLED:true"  fl="sid,name" display="" max="1" for="fascicolo" addnew="true" groupClass="" >


    <#assign data>sid,name</#assign>

<#--<#if display !='' >-->
<#--&lt;#&ndash;<#assign data>sid,${fl},name:[expression expr=/${display}/</#assign>&ndash;&gt;-->
<#--<#assign data>sid,name</#assign>-->
<#--</#if>-->

    <#assign svcUrl>autocompletionService?wild=true&fq=type:${type}&wild=true&fl=${fl}&q=${q}</#assign>

    <div class="form-group js-input ${class}">
        <#--<label class="col-md-3 control-label" for="${name}">label.${for}.${mid}</label>-->
        <span class="col-md-8">

                    <input class="auto-completion" theme="twitat"
    <#if (mandatory?? && mandatory=="true")>
        required="true"
    </#if>
                           data-type="${type}"
                           data-autocompletion-service-url="${svcUrl}"
                           display-code="false"
                           data-add-new="${addnew}"
                           data-addnew-service-url="Anag/saveObject?anagType=${type}"
                           data-get-old="true"
                           data-getold-service-url="getDynamicFTL?type_id=${type}&pathFTL=/templates/anagrafiche/&mode=&solrId=[id]"
                           data-des="${des}"
                           data-minChars='1'
                           key="${key}"
                           id="${mid}"
                           name="${name}"
                           type="text"
                           value="${value!""}"
                           data-tokenLimit="${max}"  data-tokenDelimiter=","  />
            <!-- <a class="btn btn-default btn-sm btn-dialog-legale" style="padding: 0.3em 10px;" href="#">...</a>-->

            </span>
    </div>
    <#if (addnew?? && addnew=="true")>
        <!--
    <div class="modal fade dynamic-form" id="modalForm-${mid}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Modal title</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Annulla</button>
                    <button type="button" class="btn btn-primary">Inserisci</button>
                </div>
            </div>
        </div>
    </div>
    -->

        <script>

            function setReadableValue(anagKey, anagType,fq, fl,inputName){

                var key = anagKey;
                var val = $("input[name="+ inputName +"]").val();
                if (key!="" && val != "") {

                    var url = "autocompletionService?ticket=admin&wild=true&fq=type:"+ anagType +" AND "+ anagKey +":(" + val.replace(new RegExp(";", 'g')," ") + ")&fl="+ fl +"";

                    // Extract exisiting get params
                    var ajax_params = {};
                    ajax_params.data = {};

                    var parts = url.split("?");
                    ajax_params.url = parts[0];

                    var param_array = parts[1].split("&");
                    $.each(param_array, function (index, value) {
                        var kv = value.split("=");
                        ajax_params.data[kv[0]] = kv[1];
                    });

                    // Prepare the request
                    //ajax_params.data["q"] = query;
                    ajax_params.type = "GET";
                    //ajax_params.dataType = $(input).data("settings").contentType;

                    // Attach the success callback
                    ajax_params.success = function(results) {
                        for (i in results) {
                            var item = results[i];

                            if ($("input.auto-completion[name="+ inputName +"]").size() > 0) {
                                $("input[name="+ inputName +"]").siblings("ul").find("span:contains(" + item.id + ")").text(item.name);
                            }else{
                                $("input[name="+ inputName +"]").val(item.name);
                            }

                        }
                    };

                    // Make the request
                    $.ajax(ajax_params);
                }

            }


            $(document).ready(function() {

                /*
                //attach event
                try {
                    var str = addNewEntryHandler + "";
                    $(document).on("newEntryInto-${mid}", addNewEntryHandler);
            } catch (e) {
                alert("Errore di caricamento del controllo label.fascicolo.${mid}.");
            }

            //make readable
            setReadableValue("${key}","${type}","${fq}","${fl}","${name}");
            */

                <#if (max?? && max!="1")>
                $("#${mid}").on('change', function() {
                    if ($(this).val().charAt(0)!='[')
                        $(this).val("["+$(this).val()+"]");
                });
                </#if>
            });

        </script>


    </#if>
</#macro>
