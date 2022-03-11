

<div style="clear:both">

<span class="pull-right print-hide">
<a title="apri altra scheda in visualizzazione di stampa" target="_new" href="?wt=print${querystringParams}&pageSize=-1">
	<i class="glyphicon glyphicon glyphicon-print"></i>
</a>
&nbsp;
<a title="download csv" download="${qt!'report'}_${.now?date?iso_utc}.csv" href="?wt=csv${querystringParams}&pageSize=-1">
	<i class="fticon fticon-file-excel"></i>
</a>
</span>

<table class="table table-striped table-sm">
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
			<th id="header_${column}" name="col_${column}">
			<#if (sortSpecs[column?index]?has_content ) >
				<a class="order" href="?orderBy=${sortSpecs[column?index]}${querystringParams}">${column}<i class="glyphicon"></i></a>
			<#else>
				${column}
			</#if>
			</th>		
			</#list>
		</tr>
	</thead>
	<tbody>
		<#list data as record>
		<tr>
			<#list record as value>
			
				<#if columns[value?index] != (arrayField!"") > 
					<td name="cell_${columns[value?index]}" >
					<#if columns[value?index] == 'primaryProcessInstanceId'  >
					<a class="" href="/AppBPM/instanceDetail?id=${value?c!''}">${value?c!""}</a>
					<#elseif columns[value?index] == 'instanceId' || columns[value?index] == 'processInstanceId'  >
					<a class="" href="/AppBPM/instanceDetail?id=${value!''}">${value!""}</a>
					<#elseif columns[value?index] == 'taskId' >
					<a class="" href="/AppBPM/taskDetails?id=${value!''}">${value!""}</a>
					<#elseif columns[value?index] == 'DOCNUM' >
					<a class="" href="/AppDoc/viewProfile?sede=DOCAREA&docNum=${value!''}">${value!""}</a>
					<#elseif columns[value?index] == 'workItemId' && columns[(value?index)-1] == 'instanceId' >
					<a class="" href="/AppBPM/asyncWorkItemDetails?workItemId=${value!''}&processInstanceId=${record[(value?index)-1]}">${value!""}</a>
					<#else>
						<#if (value!"")?is_number >
							${value?c!""}
						<#else>
							${value!""}
						</#if>
					</#if>
					</td>
				<#else>
					<td name="cell_${columns[value?index]}" >
					<#assign items = value!"" >
					<#if (items?is_sequence)>
					${items?size}
					</#if>
					</td>
				</#if>
			</#list>
		</tr>
		<#if arrayField?? && items?is_sequence >
		<tr>
		<td name="cell_${arrayField}" colSpan="100" >
		<#list items as item >
			<span class="col-sm-12">
				<#list item?keys as key>
					<#if key!="TEXT" && key!=arrayField >
					<span>
					<b>${key}</b>
					<span>
						<#assign value = item[key] >
						<#if key == 'primaryProcessInstanceId'  >
						<a class="" href="/AppBPM/instanceDetail?id=${value?c!''}">${value?c!""}</a>
						<#elseif key == 'instanceId' || key == 'processInstanceId'  >
						<a class="" href="/AppBPM/instanceDetail?id=${value!''}">${value!""}</a>
						<#elseif key == 'taskId' >
						<a class="" href="/AppBPM/taskDetails?id=${value!''}">${value!""}</a>
						<#elseif key == 'DOCNUM' >
						<a class="" href="/AppDoc/viewProfile?sede=DOCAREA&docNum=${value!''}">${value!""}</a>
						<#elseif key == 'workItemId' && item['instanceId']?? >
						<a class="" href="/AppBPM/asyncWorkItemDetails?workItemId=${value!''}&processInstanceId=${item['instanceId']}">${value!""}</a>
						<#else>
							<#if (value!"")?is_number >
								${value?c!""}
							<#else>
								${value!""}
							</#if>
						</#if>
					</span>
					</span>
					</#if>
				</#list>
			</span>
			<#if item["TEXT"]?? >
			<span class="col-sm-12" >
				<i>${item["TEXT"]}</i>
			</span>
			</#if>
			<#sep><span class="col-sm-12" style="font-size:5px" >&nbsp;</span>
		</#list>
		</td>
		</tr>
		</#if>
		</#list>
	</tbody>
</table>

</div>