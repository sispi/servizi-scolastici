Vue.component('facets', {
    props: [
        'model','pageField','processName'
    ],
    methods: {
        label: function(facet,value){

            if (value){
                value = value.push ? value : [value];
                var vals = [];

                for(x in value)
                    vals.push(this.model.params["facet."+facet+"."+this.processName+"."+value[x]] || this.model.params["facet."+facet+"."+value[x]] || value[x]);

                return vals.join();

            } else {
                return this.model.params["facet."+facet+".label"] || facet.labelize(true);
            }
        },
        changeQs: changeQs,
        values: function(facet){
            return this.model.lreq[facet] || [];
        }
    },
    template: `

<div class="d-print-none facets-container">

	<div class="mb-1 facets container-fluid px-0">
		<div v-for="facet in model.facets" class="row px-0">
			<div class="col-12 px-0 facet-label" ><small>{{label(facet)}}</small></div>
			<div class="col-12 px-0 facet"  role="group" >
				<a style="max-width: 6vw" v-for="(value,key) in model.counts[facet]" 
				    :title='label(facet,key)'
					:class="{disabled: (key == '...'), 
					        'btn-success': (values(facet).indexOf(key)>=0),
					        'btn-primary': (values(facet).indexOf(key)<0) }"
					class="btn btn-sm col-2 mr-1"
					:href="changeQs( (values(facet).indexOf(key)>=0?'-':'+') + facet,key,pageField)" >
					<div class="row">
					    <div class="col-9 text-truncate pr-0">{{label(facet,key)}}</div>
					    <div class="col-3 font-weight-bold text-danger pl-0">{{value||''}}</div>
					</div>
				</a>
			</div>
		</div>
	</div>

	<div class="combos container-fluid px-0" >
	    <div class="row col-12 px-0 btn-group"  role="group">
            <div v-for="(multivalue,facet) in model.combos" class="combo m-1 dropdown col-3">
                <button style="max-width: 72%"
                        class="btn btn-link pr-0 pl-0" type="button"
                        :data-toggle="(multivalue || values(facet).length==0)?'dropdown':''" >
                    <div class="row">
                        <span style="max-width: 80%" v-if="values(facet).length>0" class="text-truncate mr-2" :title="label(facet)+'('+values(facet).length+'): '+label(facet,values(facet))" >
                            {{label(facet,values(facet))}}
                        </span>
                        <span v-else style="max-width: 80%" class="text-truncate mr-2" :title="label(facet)" >{{label(facet)}}</span>
                        <span class="dropdown-toggle pr-2" v-if="(multivalue || values(facet).length==0)" ></span>
                    </div>
                </button>
                
                <a v-if="values(facet).length>0" :title="label(facet,values(facet)[0])"
                   :href="changeQs(facet,null,pageField)" >
                   <small title="Rimuovi filtro" class="pl-1 text-danger glyphicon glyphicon-remove"></small>
                </a>                            
    
                <div v-if="multivalue || values(facet).length==0" style="max-width:50vw" class="dropdown-menu" >
                    <a v-for="(value,key) in model.counts[facet]"
                       :title="label(facet,key)"
                        class="dropdown-item btn btn-link"
                        :href="changeQs( (values(facet).indexOf(key)>=0?'-':'+') +facet,key,pageField)" >
                        <div class="row">
                            <b class="text-truncate col-10">{{label(facet,key)}}</b>
                            <div class="col-1 px-0 text-right">{{value||''}}</div>
                            <div v-if="values(facet).indexOf(key)>=0" class="col-1 px-0">
                                <small title="Rimuovi filtro" class="pl-1 text-danger glyphicon glyphicon-remove"></small>
                            </div>
                        </div>
                    </a>
                </div>
                
                
                <!--<i v-if="facet!=Object.keys(model.combos)[Object.keys(model.combos).length-1]" class="ml-2 fas fa-ellipsis-v"></i>-->
			</div>
		</span>
	    </div>
	</div>

</div>


    `
})

Vue.component('pager', {
    props: ['totPage','pageNumber'],
    methods: {
        start: function (){
            return Math.max(this.end()-9,1);
        },
        end: function (){
            return Math.min( Math.max (this.pageNumber-5,1)+9, this.totPage);
        },
        pages: function(){
            console.log("pager:",this.end(),this.start());
            return this.end()-this.start()+1;
        },
        changeQs: function(param,value){
            return (location.href.replace( new RegExp("([&?])"+param+"=[^&]*", 'gi') ,"$1") + (value? ( (location.href.indexOf("?")>0?"&":"?") + param + "=" + value) : "")).replace(/&+/g,"&");
        }

    },
    template: `


<div v-if="totPage>1" class="pager" >

	<a v-if="pageNumber>1" target="#" class="btn btn-link" :href="changeQs('pageNumber',pageNumber-1)" >Indietro</a>

	<a v-for="page in this.pages()" target="#" :class="{disabled: (pageNumber==page)}" class="btn btn-link" :href="changeQs('pageNumber',page+start()-1)" >{{page+start()-1}}</a>

	<a v-if="pageNumber < totPage" target="#" class="btn btn-link" :href="changeQs('pageNumber',pageNumber+1)" >Avanti</a>


</div>`
})

Vue.component('report-buttons', {
    props: ['model'],
    data: function() { return { location: location } },
    methods: {
        changeQs: changeQs
    },
    template: `
<span class="d-print-none">

    <a v-if="model.reset" title="Rimuovi tutti i filtri" class="btn-link"
        :href="changeQs()" >
        <i class="fa fa-broom"></i>&nbsp;
    </a>
    
    <a v-if="model.form" title="Vai alla form di ricerca" class="btn-link"
       :href="(model.form || 'autoform') + '?'+ changeQs('qt',model.qt,null,true) " >
        <i class="fa fa-search"></i>&nbsp;
    </a>

    <a title="apri altra scheda in visualizzazione di stampa" target="_tab" class="btn-link"
       :href="changeQs('pageSize',-1)+'&wt=print'">
        <i class="fa fa-print"></i>
    </a>
    &nbsp;
    <a title="download csv" :download="model.qt+'_'+new Date().toISOString()+'.csv'" class="btn-link"
       :href="'csv?pageSize=-1&wt=csv&' + '?'+ changeQs('qt',model.qt,null,true) ">
        <i class="fa fa-file-excel"></i>
    </a>
</span>`
})

Vue.component('report-table', {
    props: ['model','selected'],
    data: function() {
        return {
        }
    },
    methods: {
        /*getValue: function(row,column){
            if (row.push)
                return row[this.model.columns.indexOf(column)];
            else
                return row[column];
        },*/
        changeQs: changeQs,
        substitute: function(format,row,idx){
            var self = this.model;
            return format.replace(/({[^}]+})/g, function(match, contents, offset, input_string)
                {
                    var field = contents.substring(1,contents.length-1);
                    if (field==":idx")
                        return idx;

                    if (row[field]!=null)
                        return row[field];
                    else if (row[self.columns[field]]!=null)
                        return row[self.columns[field]];
                    else
                        return "";

                    //return field==":idx" ? idx : ( row[field] || row[self.columns[field]] || "");
                }
            );
        },
        columnLabel: function(column) {
            return this.model.params["column."+column+".label"] || column.labelize(true);
        },
        cellContent: function(column,row,idx,rowidx) {
            var values;
            if (typeof row[column] == "boolean")
                values = [row[column]];
            else if (typeof row[idx] == "boolean")
                value = [row[idx]];
            else{
                var value = row[column];
                if (value==null)
                    value = row[idx];
                if (value==null)
                    value = "";

                if (value.join)
                    values = value;
                else
                    values = [value];
            }

            var results = [];
            for (var x in values){
                var value = values[x];
                var ctx = row;
                if (typeof value == "object"){
                    ctx = cloneObject(row);
                    for( x in value)
                        ctx["."+x] = value[x];
                }
                var html = this.model.params["column."+column+".html"];
                var format = this.model.params["column."+column+".format"];
                if (typeof format == "function"){
                    value = format(value,ctx,rowidx);
                }

                if (html){
                    value = this.substitute(html,ctx,rowidx);
                } else {
                    value = this.model.params["column."+column+"."+value] || this.model.params["column.*."+value] || value;
                }
                var href = this.model.params["column."+column+".href"];
                if (href){
                    if (typeof href == "function"){
                        var svalue = value, sctx = ctx, srowidx = rowidx;
                        window["fnc_"+column+"_"+rowidx] = function(){
                            href(svalue,sctx,srowidx);
                        };
                        value = "<a href='javascript:fnc_"+column+"_"+rowidx+"()'>"+(this.model.params['column.'+column+'.href.label']||value)+"</a>";
                    } else {
                        var target = this.model.params["column."+column+".target"]||'';
                        value = "<a target='"+target+"' href='"+this.substitute(this.model.params['column.'+column+'.href'],ctx,rowidx)+"'>"+(this.model.params['column.'+column+'.href.label']||value)+"</a>";
                    }
                }
                results.push(value);
            }

            return results.join(this.model.params["column."+column+".separator"]);
        },
        cellTitle: function(column,row,idx,rowidx) {
            var values;

            var value = row[column] || row[idx] || "";
            if (value.join)
                values = value;
            else
                values = [value];

            var results = [];
            for (var x in values){
                var value = values[x];
                if (typeof value == "object")
                    value = "";
                else {
                    value = this.model.params["column."+column+"."+value] || this.model.params["column.*."+value] || value;
                }
                results.push(value);
            }
            return results.join();
        }
    },
    template: `
<table class="table table-striped table-sm" >
    <thead>
        <tr>
            <th :colspan="model.params['column.'+column+'.span']" class="text-truncate" scope="col" v-for="column in model.columns" v-if="(model.params['column.'+column+'.span']||1)<=model.columns.length" >
                <span class="text-truncate" v-if="model.sortSpecs && model.sortSpecs[column]">
                    <a class="order" :href="changeQs('orderBy',model.sortSpecs[column],'pageNumber')">{{columnLabel(column)}}
                        <i v-if="(model.orderBy||'').indexOf(column)>0" class="glyphicon glyphicon-sort"></i>
                    </a>
                </span>
                <span class="text-truncate" style="white-space: nowrap" v-else>{{columnLabel(column)}}</span>
            </th>
        </tr>
    </thead>
    <tbody>
    
        <template v-for="(row,rowidx) in model.data"  >
            <tr :class="(rowidx==selected?'selected':'')" >
                <td v-for="(key,idx) in model.columns" v-if="(model.params['column.'+key+'.span']||1)<=model.columns.length" :colspan="model.params['column.'+key+'.span']" class="text-truncate"  >
                    <span :title="cellTitle(key,row,idx,rowidx)" v-html="cellContent(key,row,idx,rowidx)" ></span>
                </td> 
            </tr>
            <tr v-for="(key,idx) in model.columns" v-if="model.params['column.'+key+'.span']>model.columns.length && cellContent(key,row,idx,rowidx)" class="rowfield" >
                <td :colspan="model.params['column.'+key+'.span']" >
                    <span class="rowfield-label">{{columnLabel(key)}}</span>
                    <span class="rowfield-content" :title="cellTitle(key,row,idx,rowidx)" v-html="cellContent(key,row,idx,rowidx)" ></span>
                </td>
            </tr>
        </template>
        
        
        
        <!--<tr v-for="(row,rowidx) in model.data" :class="rowidx==selected?'selected':''" >
            <td v-if="row[rowfield]" colspan="100" >
                <span :title="cellTitle(rowfield,row,rowfield,rowidx)" v-html="cellContent(rowfield,row,rowfield,rowidx)" ></span>
            </td>
        </tr>-->
        
        
    </tbody>
</table>`

})

Vue.component('results-stats', {
    props: ['totResults','elapsed'],
    template: `
<div>
    <span v-if="totResults==1">1 risultato.</span>
    <span v-if="totResults==0">Non ci sono risultati.</span>
    <span v-if="totResults>1">{{totResults}} risultati.</span>
    <span class="d-print-none" v-if="elapsed" >({{elapsed}}ms)</span>
</div>`
})