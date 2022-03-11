<#function icon name type >
    <#switch type>
        <#case "documento">
            <#switch (name?keep_after_last('.')?lower_case) >
                <#case "doc">
                <#case "docx">
                    <#return "file-word">
                <#case "pdf">
                    <#return "file-pdf">
                <#default>
                    <#return "file">
            </#switch>
        <#default>
            <#return "folder">
    </#switch>
</#function>

<#function si num>
    <#assign order     = num?round?c?length />
    <#assign thousands = ((order - 1) / 3)?floor />
    <#if (thousands < 0)><#assign thousands = 0 /></#if>
    <#assign siMap = [ {"factor": 1, "unit": ""}, {"factor": 1000, "unit": "K"}, {"factor": 1000000, "unit": "M"}, {"factor": 1000000000, "unit":"G"}, {"factor": 1000000000000, "unit": "T"} ]/>
    <#assign siStr = (num / (siMap[thousands].factor))?string("0.#") + siMap[thousands].unit />
    <#return siStr />
</#function>

<#function cell record,key1,key2 >
    <#if key1?is_string>
        <#return record[key2] />
    <#elseif colIndexes[key2]?? >
        <#return record[colIndexes[key2]] />
    <#else>
        <#return colIndexes[key2] />
    </#if>
</#function>

<#assign processNameCol = (params['facet.businessState']!'')?split(":")[1]!'' />

<#function evaluate record key >

    <#assign value = record[key]!"" />

    <#assign column = key?is_string?then(key,columns[key]) />

    <#if value?is_boolean>
        <#assign value = value?c />
    <#elseif value?is_number >
        <#assign value = value?c />
    <#elseif value?is_sequence >
        <#if column == (arrayField!"") >
            <#assign items = value >
            <#assign value = value?size >
        <#else>
            <#assign value = value?join(',') />
        </#if>
    </#if>

    <#if column==businessState >
        <#assign prefix = "facet."+businessState+"."+cell(record,key,processNameCol)!''+"." />
    <#else>
        <#assign prefix = "facet."+column+"." />
    </#if>

    <#--assign value = ((params[prefix+value])!utils.getDisplayName(value)) /-->
    <#assign href = "" />

    <#if value?matches(r"\d{4}-\d{2}-\d{2}T.*") >
        <#assign value = utils.datetime(value!"") />
    <#elseif column?ends_with('nstanceId') >
        <#assign href = "${context}/instanceDetail?id="+value />
        <#assign value = cell(record,key,'_des_'+column)!value />
    <#elseif column?ends_with('taskId') >
        <#assign href = "${context}/taskDetails?id="+value />
        <#assign value = cell(record,key,'_des_'+column)!value />
    <#elseif column == 'DOCNUM' >
        <#assign href = "docProfile?docnum="+ value />
        <#assign value = cell(record,key,'_des_'+column)!value />
    <#elseif column == 'workItemId' && cell(record,key,'instanceId')?? >
        <#assign href = "${context}/asyncWorkItemDetails?workItemId=" + value + "&processInstanceId="+cell(record,key,'instanceId') />
    </#if>

    <#return [value,href!""] />

</#function>