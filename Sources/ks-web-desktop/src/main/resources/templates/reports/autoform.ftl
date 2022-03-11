<#assign report = webClient.getMap('/docer/v1/report?rows=0&qt={qt}') />

<#assign parameters = report.parameters />
<#assign request = query />

<div class="container-fluid">

    <h3>${request.title!""}</h3>
<span class=""><i>${request.description!""}</i></span>


<form action="${request['action']!'reportvue'}" method="GET" >

    <input type="hidden" name="qt" value="${query.qt}" />

    <div class="row" >
    <#list parameters?keys as parameter>
        <div class="col-sm-3 form-group">
            <label>${parameter}</label>

            <#if parameters[parameter] == 'Date'>
                <input name="${parameter}" type='date' class="form-control" value="${request[parameter]!''}" />
            <#elseif parameters[parameter] == 'Datetime'>
                <input name="${parameter}" type='datetime-local' class="form-control" value="${request[parameter]!''}" />
            <#elseif parameters[parameter] == 'Double'>
                <input name="${parameter}" type='number' class="form-control" value="${request[parameter]!''}" />
            <#elseif parameters[parameter] == 'Integer'>
                <input name="${parameter}" type='number' step="1" pattern="\d+" class="form-control" value="${request[parameter]!''}" />
            <#else>
                <input name="${parameter}" type='text' class="form-control" value="${request[parameter]!''}" />
            </#if>
        </div>
    </#list>

    </div>

    <span class="row col-12">
        <button class="btn btn-primary" type="submit"  >Esegui&nbsp;</button>
    </span>
</form>

</div>
