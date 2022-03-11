<#if (totPage > 1) >
<style>
	.pager a {
		font-size: 0.9rem;
	}
</style>
<div class="pager d-flex justify-content-center" >

	<#assign end=[[pageNumber-5,1]?max+9,totPage]?min />

	<#if (pageNumber > 1 ) >
		<a class="btn btn-link" href="${baseUrl}?pageNumber=${pageNumber-1}&orderBy=${orderBy}${querystringParams}" >Indietro</a>
	</#if>

	<#list [end-9,1]?max..end as loop >
		<a class="btn btn-link ${ (loop == pageNumber)?then('disabled','') }" href="${baseUrl}?pageNumber=${loop}&orderBy=${orderBy}${querystringParams}" >${loop}</a>
	</#list>

	<#if (pageNumber < totPage) >
		<a class="btn btn-link" href="${baseUrl}?pageNumber=${pageNumber+1}&orderBy=${orderBy}${querystringParams}" >Avanti</a>
	</#if>

</div>
</#if>