<#if params['output.report.columns']?? >
	<#assign rcolumns = params['report.columns']?split(",") />
<#else>
	<#assign rcolumns = columns />
</#if>

<table class="table table100 table-striped table-condensed">
    <thead>
    <#if (((groups![])?size) > 0) >
    <tr>
        <#list groups as group>
            <#if group?has_content >
                <th style="border:1px solid lightgrey;text-align:center" id="group_${group}" colspan="${spans[group?index]}" >
                ${group}
                </th>
            <#else>
                <th style="visibility:hidden" colspan="${spans[group?index]}" >

                </th>
            </#if>
        </#list>
    </tr>
    </#if>
    <tr>
    <#list columns as column >

        <#if column?starts_with("_des_") || !rcolumns?seq_contains(column) >
            <#continue />
        </#if>

        <th id="header_${column}" name="col_${column}">

            <#if (sortSpecs[column]??) >
                <a class="order" href="${baseUrl}?orderBy=${sortSpecs[column]}${querystringParams}">$[facet.${column}.label:${column}]
                    <#if (orderBy?contains(column)) >
                        <i class="glyphicon glyphicon-sort"></i>
                    </#if>
                </a>
            <#else>
                $[facet.${column}.label:${column}]
            </#if>

            <#if (params['facet.'+column+'.glyphicon'])??  >
                <i class="glyphicon glyphicon-${params['facet.'+column+'.glyphicon']}" />
            </#if>
        </th>
    </#list>
    </tr>
    </thead>
    <tbody>
    <#list data as item >
        <#assign items = ((record[colIndexes[arrayField]])![]) />

        <#if item?is_sequence>
            <#assign record = item>
        <#else>
            <#assign record = item?values />
        </#if>

    <tr>
        <#list record as cell >
		
            <#assign values = evaluate(record,cell?index) />
            <#assign value = values[0] />
            <#assign href = values[1] />

            <#if columns[cell?index]?starts_with("_des_") || !rcolumns?seq_contains(columns[cell?index]) >
                <#continue />
            </#if>

            <td name="cell_${columns[cell?index]}" >
                <#if href?has_content >
                    <a class="" href="${href}"><span class="limited ellipsis" title="${value}" >${value}</span></a>
                <#else>
                    <span class="limited ellipsis" title="${value}" >${value}</span>
                </#if>
            </td>
        </#list>
    </tr>
        <#if (items?size>0) >
        <tr>
            <td name="cell_array" colSpan="100" >
                <#list items as item >
                    <span class="col-sm-12">
                        <#list item?keys as key>
                            <#if key!="TEXT" && key!=arrayField >
                                <span>
						<b>${key}</b>
						<span>
						<#assign value = item[key] >

                            <#assign values = evaluate(record,key) />
                            <#assign value = values[0] />
                            <#assign href = values[1] />

                            <#if href?has_content >
                                <a class="" href="${href}">${value}</a>
                            <#else>
                            ${value}
                            </#if>
						</span>
					</span>
                            </#if>
                        </#list>
				</span>
                    <#if item["TEXT"]?? >
                        <span class="col-sm-12">
					<i>${item["TEXT"]}</i>
				</span>
                    </#if>
                </#list>
            </td>
        </tr>
        </#if>
    </#list>
    </tbody>
</table>
