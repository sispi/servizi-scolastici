
<div class="container-fluid">
<#list list?keys as category>
<h3 class="col-sm-12">${category}</h3>

<!--<span class="row" style="font-weight:bold">
    <span class="col-sm-2">id</span>
    <span class="col-sm-3">title</span>
    <span class="col-sm-3">roles</span>
    <span class="col-sm-4"></span>
</span>-->

	<#list list[category] as report>
    <span class="col-sm-12">
			<span class="col-sm-4">
				<a href="reports/${report.qt}${ report.form???then('?form','') }">${report.title}</a>
			</span>
			<span class="col-sm-4" style="font-size:larger" >
				<a title="esegui" href="reports/${report.qt}"><i class="glyphicon glyphicon-play-circle"></i></a>
				<#if (report.form?has_content) >
                    <a title="mostra form" href="reports/${report.qt}?form"><i class="glyphicon glyphicon-list-alt"></i></a>
				</#if>
			</span>
			<span class="col-sm-4">
			</span>
		<#if (report.description?has_content) >
            <span class="col-sm-12">
				<i>${report.description!""}</i>
			</span>
		</#if>

		</span>
	</#list>
<span class="row">&nbsp;</span>
</#list>
</div>