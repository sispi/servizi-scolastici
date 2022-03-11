<!-- start form-renderer -->
<#macro js object="" inline=true>
    <@compress single_line=true>
        <#if object?is_hash || object?is_hash_ex>
            <#assign first = "true">
            {<#t>
            <#list object?keys as key>
                <#if first == "false">, </#if><#t>
                <#assign value><@js object=object[key] inline=inline/></#assign>
                <#if inline>${key}<#else>"${key}"</#if>: ${value?trim}<#t>
                <#assign first="false">
            </#list>
            }<#t>
        <#elseif object?is_enumerable>
            <#assign first="true">
            [<#t>
            <#list object as item>
                <#if first="false">, </#if><#t>
                <#assign value><@js object=item inline=inline/></#assign>
                ${value?trim}<#t>
                <#assign first="false">
            </#list>
            ]<#t>
        <#elseif object?is_number>
            ${object?c}<#t>
        <#elseif object?is_boolean>
            ${object?string('true', 'false')}<#t>
        <#elseif object?is_date_like>
            "${object?datetime_if_unknown?iso_utc}"<#t>
        <#elseif object?has_content>
            <#if inline>"${object?js_string}"<#else>"${object?json_string}"</#if><#t>
        <#elseif object?is_string>
            ""<#t>
        <#else>
            null<#t>
        </#if>
    </@compress>
</#macro>
<#noparse>
<#macro js object="" inline=true>
    <@compress single_line=true>
        <#if object?is_hash || object?is_hash_ex>
            <#assign first = "true">
            {<#t>
            <#list object?keys as key>
                <#if first == "false">, </#if><#t>
                <#assign value><@js object=object[key] inline=inline/></#assign>
                <#if inline>${key}<#else>"${key}"</#if>: ${value?trim}<#t>
                <#assign first="false">
            </#list>
            }<#t>
        <#elseif object?is_enumerable>
            <#assign first="true">
            [<#t>
            <#list object as item>
                <#if first="false">, </#if><#t>
                <#assign value><@js object=item inline=inline/></#assign>
                ${value?trim}<#t>
                <#assign first="false">
            </#list>
            ]<#t>
        <#elseif object?is_number>
            ${object?c}<#t>
        <#elseif object?is_boolean>
            ${object?string('true', 'false')}<#t>
        <#elseif object?is_date_like>
            "${object?datetime_if_unknown?iso_utc}"<#t>
        <#elseif object?has_content>
            <#if inline>"${object?js_string}"<#else>"${object?json_string}"</#if><#t>
        <#elseif object?is_string>
            ""<#t>
        <#else>
            null<#t>
        </#if>
    </@compress>
</#macro>
</#noparse>

<#assign localId = "form_"+(.now?string["HHmmssSSS"]?number?c) >
<#noparse>
<#assign formId = identifier!</#noparse>'${localId}'<#noparse> >
<#assign formModel><@js inline=false object=(model!{})/></#assign>
</#noparse>
<#assign form = definition!description >

<#noparse>
<#assign render = render!'template' />
<#if render!='template'>
<!--<link rel="stylesheet" href="/static/js/components/renderer.css"  >
<script type="text/javascript" src="/static/js/components/renderer.js" ></script>-->

${'<script id="${formId}_template" type="text/x-template"  >'}
</#if>
</#noparse>
    <#assign tabs = form.tabs />
    <#assign dummy = 0 />
    <#assign hasPrev = false />
    <#assign hasNext = false />
    <#if (((form.toolbar.buttons)![])?size &gt; 0 || (form.wizard!false)) >
        <#assign elements = form.toolbar.buttons />
        <#if (form.wizard!false)>

            <#list elements as button >
                <#if ( (button.action!'') == 'next' ) >
                    <#assign hasNext = true />
                </#if>
                <#if ( (button.action!'') == 'back' ) >
                    <#assign hasPrev = true />
                </#if>
            </#list>

            <#if !hasNext >
                <#assign btn = { "label": "<i class='bi bi-arrow-right prevnext'></i>", "role" : "next", "onAction" : "next()", "class": "prevnext btn btn-outline-primary", "disabled" : "nextIndex()==-1 || (!form$isTabValid[''+form$tabidx] && !form$isTabVisited[''+nextIndex()])", "width" : "75" } />
                <#assign elements = [btn] + elements/>
            </#if>

            <#if !hasPrev >
                <#assign btn = { "label": "<i class='bi bi-arrow-left prevnext'></i>", "role" : "back", "onAction" : "previous()","class": "prevnext btn btn-outline-primary", "disabled" : "previousIndex()==-1", "width" : "75" } />
                <#assign elements = [btn] + elements/>
            </#if>

        </#if>

        <#if (((form.toolbar.position)!'top')=='top')  >
            <#assign dummy = 1 />
            <#assign section = { "style" : (form.toolbar.style!''), "class" : (form.toolbar.class!'') + " toolbar header" ,"toolbar" : true , "elements" : elements } />
            <#assign tabs = [{ "class" : "header" , "sticky" : true, "sections" : [section] }] + tabs />
        <#else>
            <#assign section = { "style" : (form.toolbar.style!''), "class" : (form.toolbar.class!'') + " toolbar footer" ,"toolbar" : true , "elements" : elements } />
            <#assign tabs = tabs + [{ "class" : "footer" , "sticky" : true, "sections" : [section] }]  />
        </#if>
    </#if>


    <!-- body -->
    <form novalidate class="form-renderer-vue" :class="{ 'no-alerts' : !form$showAlerts, 'valid' : form$isFormValid, 'invalid': !form$isFormValid }"  >

        <!-- header -->
        <#if form.header?has_content>
        <span class="form-header">${form.header}</span>
        </#if>

        <!-- validation -->

        <div id="alert-panel" >
            <div v-if="Object.keys(form$invalids).length==0 && form$errors.length==0" class="alert alert-info my-2 py-0 " role="alert">
                {{VALIDATION_SUCCESS}}
            </div>
            <div v-else class="alert alert-danger my-2 py-0 " role="alert">
                <button type="button" class="toggle p-0 font-weight-bold btn btn-link" data-toggle="collapse" data-target="#warnings" >
                    <span class="header-line"><i class="bi bi-arrow-down-up"></i>&nbsp;{{VALIDATION_ERROR}} ({{Object.keys(form$invalids).length + form$errors.length}})</span>
                </button>
                <div id="warnings" class="collapse">
                    <div v-if="msg" v-for="msg in Object.values(form$invalids)" >

                        <span v-if='msg.id' >
                            <a class="btn btn-link" @click="scrollTo(msg.id,msg.tabIndex)" >
                                <b v-if="msg.name">{{msg.name}}</b>
                                <span>{{msg.message||msg}}</span>
                                <i v-if="msg.tab">({{msg.tab}})</i>
                            </a>
                        </span>

                        <span class="mx-3" v-else >
                            <b v-if="msg.name">{{msg.name}}</b>
                            <span>{{msg.message||msg}}</span>
                            <i v-if="msg.tab">({{msg.tab}})</i>
                        </span>

                    </div>
                    <div v-for="msg in form$errors" >

                        <span v-if='msg.id' >
                            <a class="btn btn-link" @click="scrollTo(msg.id,msg.tabIndex)" >
                                <b v-if="msg.name">{{msg.name}}</b>
                                <span>{{msg.message||msg}}</span>
                                <i v-if="msg.tab">({{msg.tab}})</i>
                            </a>
                        </span>

                        <span class="mx-3" v-else >
                            <b v-if="msg.name">{{msg.name}}</b>
                            <span>{{msg.message||msg}}</span>
                            <i v-if="msg.tab">({{msg.tab}})</i>
                        </span>

                    </div>
                </div>
            </div>
        </div>

        <!-- form-body -->

        <div class="form-body tab-content container-fluid" style="display:flex;flex-direction:column;" >

        <#assign rtabs = 0 />
        <#list tabs as tab>
            <#assign sticky = tab.sticky!(tab.class!'')?split(" ")?seq_contains("sticky") />
            <#if !sticky >
                <#assign rtabs = rtabs +1 />
            </#if>
        </#list>

        <#if rtabs &gt; 1 >

        <nav class="tabs row">
            <#if (form.wizard!false) >
                <svg style="z-index:-1;position: relative;top:43px;margin:15px"  height="10" width="100%">
                    <line x1="0" y1="0" x2="100%" y2="0" style="stroke:#dddddd;stroke-width:8;" />
                </svg>
            </#if>
            <div class="nav nav-tabs col-auto ${(form.wizard!false)?then('wizard','')}" id="nav-tab" role="tablist">
                <#list tabs as tab>
                    <#assign sticky = tab.sticky!(tab.class!'')?split(" ")?seq_contains("sticky") />
                    <#if !sticky >
                        <#assign active = active???then(active,tab?index) />
                        <#if (form.wizard!false) >
                            <a style="pointer-events: none" v-show="${tab.show?has_content?then(tab.show,'true')}" @click="ontab(${tab.index!(tab?index-dummy)})" :data-tabindex="$index=${tab.index!(tab?index-dummy)}" id="tab${tab?index}" class="text-center nav-tab ${ (tab?index == active)?then('active','') } nav-item nav-link ${tab.class!''}" data-toggle="tab" href="#${localId}-nav-${tab?index}" role="tab" >
                                <div :class="{'text-muted bi-circle-fill' : !form$isTabVisited[''+$index],  'visited' : form$isTabVisited[''+$index] , 'text-success bi-check-circle-fill valid' : form$isTabVisited[''+$index] &&  form$isTabValid[$index], 'text-muted bi-circle-fill invalid' : form$isTabVisited[''+$index] && !form$isTabValid[$index] } " class="bi"></div>
                                ${tab.title!'(no title)'}
                            </a>
                        <#else>
                            <a v-show="${tab.show?has_content?then(tab.show,'true')}" @click="ontab(${tab.index!(tab?index-dummy)})" :data-tabindex="$index=${tab.index!(tab?index-dummy)}" id="tab${tab?index}" class="nav-tab ${ (tab?index == active)?then('active','') } nav-item nav-link ${tab.class!''}" data-toggle="tab" href="#${localId}-nav-${tab?index}" role="tab" >
                                ${tab.title!'(no title)'}
                                <i :class="{'text-muted bi-circle-fill' : !form$isTabVisited[''+$index], 'visited' : form$isTabVisited[''+$index] ,'text-success bi-check-circle-fill valid' : form$isTabVisited[''+$index] && form$isTabValid[$index], 'text-muted bi-circle-fill invalid' : form$isTabVisited[''+$index] && !form$isTabValid[$index] } " class="bi"></i>
                            </a>
                        </#if>

                    <#else>
                        <a v-show="false" class="sticky" :data-tabindex="$index=${tab.index!(tab?index-dummy)}" id="tab${tab?index}" >${tab.title!''}</a>
                    </#if>
                </#list>
            </div>
        </nav>
        </#if>

        <#list tabs as tab>
        <#assign sticky = tab.sticky!(tab.class!'')?split(" ")?seq_contains("sticky") />
        <div :data-contentindex="$index=${tab.index!(tab?index-dummy)}" v-if="${tab.show?has_content?then(tab.show,'true')}" class="${ (tab?index == (active!0))?then('active show','') } ${sticky?then( (tab?index > (active!0))?then('row sticky footer','row sticky')  ,'unsticky')} tab-pane fade ${tab.class!''}" id="${localId}-nav-${tab?index}" role="tabpanel" >
            <#if (sticky && tab.title?has_content)>
            <div class="row tab-header header-line mx-0">
                <span class="col-12">${tab.title}</span>
            </div>
            </#if>
            <#list tab.sections as section0>

            <#if section0?is_string >
                <#assign section = form.sections[section0] />
            <#else>
                <#assign section = section0 />
            </#if>

            <#assign sectionId = localId+'_tab'+tab?index+'_sec'+section0?index />
            <#assign tmp = 'form$tmp'+tab?index+"_"+section0?index />

            <#assign bindin = section.input?has_content?then(section.input,section.output?has_content?then(section.output,tmp)) />
            <#assign bindout = section.output?has_content?then(section.output,section.input?has_content?then(section.input,tmp)) />

            <#assign context = ( (section.type!'single')=='single')?then('['+bindin+']', bindin ) />
            <#assign ncontextIn = ( (section.type!'single')=='single')?then( bindin, bindin+"[]") />
            <#assign ncontext = ( (section.type!'single')=='single')?then( bindout, bindout+"[]") />


            <fieldset :data-sectionindex="$index=${section0?index}" id="${sectionId}"
                      :disabled="(${tab.disabled?has_content?then(tab.disabled,'false')}) || (disabled=='true')"
                      :class="{ disabled : (${tab.disabled?has_content?then(tab.disabled,'false')}) || (disabled=='true') }" class="section ${sticky?then('col-12','')}" v-if="${section.show?has_content?then(section.show,'true')}" >

                <#if (section.header.title)?? >
                <div class="section-header header-line row">

                    <div class="col-12 ${(section.header.class)!''}" style="${(section.header.style)!''}" >

                        <#if ((section.header.collapsible)!false) >
                        <button type="button" ${((section.header.collapsible)!false)?then('','disabled=\'true\' style=\'opacity:1\'')} class="p-0 font-weight-bold btn btn-link" data-toggle="collapse" data-target="#${sectionId}_body" >
                            <i class="bi bi-arrow-down-up"></i>&nbsp;${section.header.title}
                        </button>
                        <#else>
                        <span>${section.header.title}</span>
                        </#if>
                    </div>
                </div>
                <#elseif (section.type!'single')=='repeatable' && (section.class!'')?contains("table") >
                <div class="section-header header-line row table">
                    <#list (section.elements![]) as element>
                        <span class="col-${element.width?has_content?then(element.width,'12')}" >${element.label!''}</span>
                    </#list>
                </div>
                </#if>

                <div data-context="${ncontextIn}"
                        ${section.output?has_content?then('data-output="'+section.output+'"','')}
                        ${section.input?has_content?then('data-input="'+section.input+'"','')}
                        id="${sectionId}_body" class="section-body ${((section.header.collapsible)!false)?then('collapse','')} ${section.class!''}" style="${section.style!''}">

                    <div data-context="${ncontext}"  v-for="($item,$index) in ${context}" class="section-item" >

                        <#if (section.type!'single')=='repeatable' >
                            <span class="deletebutton" style="float:right;position: absolute; right: 0px;z-index: 1000">
                                <button type="button"
                                         ${(section.repeatable.min!'')?has_content?then(':disabled="'+context+'.length<='+section.repeatable.min+'"','')}
                                         @click="delsection(${context},$index)" class="btn btn-link del-button" >
                                    ${(section.repeatable.deleteLabel)!'<i class="bi bi-x-circle-fill text-danger"></i>'}
                                </button>
                            </span>
                        </#if>

                        <#if section.elements?is_string>
                            <#assign elements = form.sections[section.elements]![] />
                        <#else>
                            <#assign elements = section.elements![] />
                        </#if>

                        <div :class="{ even : ($index % 2)==1 }" class="${((section.type!'single')=='repeatable')?then('alternate','')} section-row row ${section.class!''}"  >

                        <#list elements as element>

                            <#assign tmp = "$item.form$tmp" + element?index />
                            <#assign elbindin = element.input?has_content?then(element.input,element.output?has_content?then(element.output,tmp)) />
                            <#assign elbindout = element.output?has_content?then(element.output,element.input?has_content?then(element.input,tmp)) />

                            <#assign elementVueId = "'"+sectionId+"_idx'+$index+'_"+element?index+"'" />
                            <#assign eType = element.type!'button' />

                            <#assign break = false />
                            <#assign wrap = !(section.toolbar!false) && !(eType=='hidden') && !(eType=='resource')   />

                            <#if wrap>
                            <div
                                    :id="${elementVueId}"
                                    v-if="${element.show?has_content?then(element.show,'true')}"
                                    style="${element.style!''}"
                                    class="form-group ctrl-${eType} ${element.margin?has_content?then('offset-'+element.margin,'')} col-${ element.width?has_content?then(element.width,'12')} "
                                    title="${element.title!''}"
                            >
                            </#if>
                            <#switch eType>
                                <#case 'checkbox'>
                                <!-- checkbox element -->
                                <span class="form-check">
                                    <input
                                            class="vhidden"
                                            :value="${elbindin}"
                                            :name="getName('${ncontext}','${elbindout}',$index)"

                                    />
                                    <#assign state = (element.required!false)?then('true','null') />
                                    <input
                                            :id="${elementVueId}+'_ctrl'"
                                            :disabled="${element.disabled?has_content?then(element.disabled,'false')}"
                                            :class="{ disabled : (${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true') }"
                                            class="form-check-input"
                                            type="checkbox"

                                            ${((element.required)!false)?then('data-required=\'true\'','')}
                                            ${((element.required)!false)?then('required=\'required\'','')}
                                            :value="${elbindin}"

                                            :indeterminate.prop = "(${elbindin})==null"

                                            @click = "${elbindin} = oncheck(${elbindin},${(element.required!false)?then('true','false')}); onchange('#'+${elementVueId}+'_ctrl','${elbindin}',${elbindin},${element.onChange???then("'"+element.onChange+"'",'null')}) ;"

                                            :checked="(${elbindin}===true)"

                                            :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"

                                            data-input="${elbindin}"
                                            data-output="${elbindout}"
                                            :data-bind="${elbindin}"

                                            data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                            data-invalid-feedback="${element.invalidFeedback!''}"

                                    />

                                    <label class="form-check-label" :for="${elementVueId}+'_ctrl'">${element.label!''}</label>
                                </span>

                                <#if element.description?has_content >
                                    <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>
                                <!-- end checkbox element -->
                                <#break>

                                <#case 'checkbox-group'>
                                <#case 'radio-group'>
                                <!-- checkbox group element -->

                                <#if (element.values![])?is_sequence>
                                    <#assign values><@js inline=false object = element.values /></#assign>
                                    <#assign width = element.rows???then("{width:'"+((99.9/((element.values?size)/(element.rows?number))?ceiling)?c)+"%'}",'') />
                                    <#assign dctx = '' />
                                <#else>
                                    <#assign values = element.values />
                                    <#assign width = element.rows???then("{width: 99.9/Math.ceil("+element.values+".length/"+(element.rows?number)+")+'%'}",'') />
                                    <#assign dctx = values+'[]' />
                                </#if>

                                <#assign subtype = eType?split("-")[0] />


                                <label class="form-check-group-label" :for="${elementVueId}+'_ctrl'">${element.label!''}</label>

                                <fieldset
                                        :class="{ disabled : (${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true') }"
                                        :disabled="${element.disabled?has_content?then(element.disabled,'false')}"
                                        :id="${elementVueId}+'_ctrl'"

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                        ${((element.required)!false)?then('data-required=\'true\'','')}
                                        data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                        data-invalid-feedback="${element.invalidFeedback!''}"
                                        :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"

                                >

                                    <input  v-if="(typeof (${elbindin})!='object')"
                                            class="vhidden"
                                            :value="${elbindin}"
                                            :name="getName('${ncontext}','${elbindout}',$index)"
                                    />

                                    <span data-context="${dctx}" :style="${width}" v-for="(option,$ckidx) in ${values?html}" class="form-check">
                                        <input
                                                :id="${elementVueId}+'_ctrl'+$ckidx"
                                                class="form-check-input"
                                                type="${subtype}"
                                                :name="(typeof (${elbindin})=='object') ? getName('${ncontext}','${elbindout}',$index) : ${elementVueId}"
                                                :value = "option.value??option.id??option"
                                                :checked = "ison(${elbindin},option.value??option.id??option,'${subtype}')"
                                                @click = "${elbindin} = toggle(${elbindin},option.value??option.id??option,'${subtype}',${values?html}); onchange('#'+${elementVueId}+'_ctrl','${elbindin}',${elbindin},${element.onChange???then("'"+element.onChange+"'",'null')}) ; "
                                        />

                                        <label class="form-check-label" :for="${elementVueId}+'_ctrl'+$ckidx" >{{option.label??option.text??option}}</label>
                                    </span>

                                </fieldset>

                                <#if element.description?has_content >
                                <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>
                                <!-- end checkbox-group element -->
                                <#break>

                                <#case 'select'>

                                <label class="select-label" :for="${elementVueId}+'_ctrl'">${element.label!''}</label>

                                <#assign closeOnSelect = "false" />
                                <#assign mode = (element.mode!(element.subtype!'single')) />
                                <#if (mode=="single" || (element.max!'') == '1') >
                                    <#assign closeOnSelect = "true" />
                                </#if>
                                <select2
                                        close-on-select="${closeOnSelect}"
                                        :id="${elementVueId}+'_ctrl'"
                                        :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"
                                        data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                        data-invalid-feedback="${element.invalidFeedback!''}"

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                        ${element.ref???then("ref=\""+element.ref+"\"",'')}

                                        ${element.url???then(":url=\"select2url(\'"+element.url?html+"\')\"",'')}
                                        ${element.urlProcessor???then(":process-results=\"select2process(\'"+(element.urlProcessor?js_string)+"\')\"",'')}

                                        ${(element.tags!false)?then("tags=\"true\"",'')}

                                        ${element.max???then('maximum-selection-length=\''+element.max+'\'','')}
                                        ${(element.required!false)?then('data-required=\'true\'','')}
                                        ${(!element.required!false)?then('allow-clear=\'true\'','')}

                                        :disabled="(${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true')"

                                        ${element.chars???then('minimum-input-length=\''+element.chars+'\'','')}

                                        ${(mode=='multiple')?then('multiple="true"','')}
                                        ${element.placeholder?has_content?then('placeholder=\''+element.placeholder+'\'','')}

                                        v-model="${elbindin}"

                                        v-on:input="onchange('#'+${elementVueId}+'_ctrl','${elbindin}',$event,${element.onChange???then("'"+element.onChange+"'",'null')})"


                                >
                                    <#if element.model?? >
                                        <option v-for="value in evaluate(`${element.model}`)" :value="value.value??value.id??value" >{{value.label??value.text??value}}</option>
                                    <#elseif element.values?? >
                                        <#if element.values?is_string>
                                            <option v-for="value in evaluate(`${element.values}`)" :value="value.value??value.id??value" >{{value.label??value.text??value}}</option>
                                        <#else>
                                            <#assign values><@js inline=false object = element.values /></#assign>
                                            <option v-for="value in ${values?html}" :value="value.value??value.id??value" >{{value.label??value.text??value}}</option>
                                        </#if>

                                    </#if>



                                </select2>

                                <#if element.description?has_content >
                                    <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>

                                <#break>


                                <#case 'hidden'>
                                <!-- hidden element -->
                                <input
                                        :id="${elementVueId}+'_ctrl'"
                                        type="hidden"
                                        :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"
                                        :name="getName('${ncontext}','${elbindout}',$index)"
                                        :value="${elbindin}"
                                        data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                        data-invalid-feedback="${element.invalidFeedback!''}"

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                        :data-anchor="${elementVueId}+'_anchor'"
                                />
                                <#if element.description?has_content >
                                <small class="form-text text-muted col-12">${element.description}</small>
                                </#if>
                                <#if element.valid?has_content >
                                <span :id="${elementVueId}+'_anchor'" class="invalid-feedback feedback col-12" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>
                                </#if>



                                <!-- end hidden element -->
                                <#break>

                                <#case 'resource'>
                                <!-- resource element -->
                                <resource
                                        :id="${elementVueId}+'_ctrl'"

                                        ${element.urlProcessor???then(":processor=\"findFunc(\'"+(element.urlProcessor?js_string)+"\')\"",'')}

                                        v-model="${elbindin}"
                                        type="${element.contentType!''}"
                                        url="${element.url!''}"
                                        source="${element.source!''}"

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                ></resource>

                                <!-- end resource element -->
                                <#break>

                                <#case 'file'>
                                <!-- file element -->
                                <label class="file-label" :for="${elementVueId}+'_ctrl'">${element.label!''}</label>

                                <div
                                    :id="${elementVueId}+'_ctrl'"
                                    :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"
                                    data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                    data-invalid-feedback="${element.invalidFeedback!''}"

                                    data-input="${elbindin}"
                                    data-output="${elbindout}"
                                    :data-bind="${elbindin}"

                                    ${((element.required)!false)?then('data-required=\'true\'','')}

                                >
                                    <file
                                            ${element.url???then("url='"+element.url+"'",'')}
                                            ${element.urlProcessor???then(":processor=\"findFunc(\'"+(element.urlProcessor?js_string)+"\')\"",'')}
                                            ${element.extensions???then("accept='"+element.extensions?join(',')+"'",'')}
                                            flatten="true"
                                            ${element.size???then('maxLength=\''+element.size?c+'\'','')}
                                            ${element.max???then('maxSize=\''+element.max+'\'','')}
                                            :disabled="${element.disabled?has_content?then(element.disabled,'false')}"

                                            ${((element.mode!(element.subtype!'single'))=='multiple')?then('multiple="true"','')}
                                            ${element.placeholder?has_content?then('placeholder=\''+element.placeholder+'\'','')}
                                            v-model="${elbindin}"

                                            v-on:input="onchange('#'+${elementVueId}+'_ctrl','${elbindin}',$event,${element.onChange???then("'"+element.onChange+"'",'null')})"

                                    ></file>

                                    <#if ((element.subtype!'single')=='multiple') >
                                        <input v-for="item in ${elbindin}"
                                               type="hidden"
                                               :value="item"
                                               :name="getName('${ncontext}','${elbindout}',$index)"
                                        />
                                    <#else>
                                        <input
                                               type="hidden"
                                               :value="${elbindin}"
                                               :name="getName('${ncontext}','${elbindout}',$index)"
                                        />
                                    </#if>



                                </div>

                                <#if element.description?has_content >
                                <small class="form-text text-muted col-12">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>

                                <!-- end file element -->

                                <#break>

                                <#case 'button'>

                                <!-- button element -->
                                <#assign args><@js inline=false object=(element!{}) /></#assign>

                                <#if wrap >
                                    <#assign class = 'btn form-control ' + element.classes!(element.class!'') />
                                    <#assign style = element.style!'' />
                                <label>&nbsp;</label>
                                <#else>
                                    <#assign class = 'btn ' + element.classes!(element.class!'') />
                                    <#assign style = element.style!'' />
                                    <#if element.margin?has_content && (''+element.margin) != '0'>
                                        <#assign style = style + ';margin-left:'+ element.margin + 'px;' />
                                    </#if>
                                    <#if element.width?has_content && (''+element.width) != '0' >
                                        <#assign style = style + ';width:'+ element.width + 'px;' />
                                    </#if>
                                </#if>

                                <button type="button"
                                        :title="render('${(element.description!'')?replace('\'','\\\'')!''}')"
                                        style="${style}"
                                        class="${class} "

                                        ${element.action?has_content?then('role="'+element.action+'"','')}


                                        :disabled="${element.disabled?has_content?then(element.disabled,'false')}"
                                        :class="{ disabled : (${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true') }"
                                        @click="buttonclick(JSON.parse(`${args?replace('\"','&quot;')}`),$event)"
                                >
                                    ${element.label}
                                </button>
                                <!-- end button element -->
                                <#break>
                                <#case 'header'>

                                <!-- header element -->
                                <#assign break = true />
                                ${'<'+element.subtype!'h5'} :id="${elementVueId}" v-if="${element.show?has_content?then(element.show,'true')}" class="${element.classes!(element.class!'')}" style="${element.style!''}" >
                                ${element.label!(element.value!'')}
                                ${'</'+element.subtype!'h5'}>
                                <#if element.description?has_content >
                                <small class="form-text text-muted">${element.description}</small>
                                </#if>
                                <!-- end header element -->

                                <#break>
                                <#case 'html'>

                                <!-- html element -->
                                <#assign break = true />
                                ${element.value!''}
                                <#if element.description?has_content >
                                <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <!-- end html element -->

                                <#break>
                            <#case 'number'>
                            <#case 'date'>
                            <#case 'text'>

                                <#assign onchange = ";onchange(\"#"+sectionId+"_idx\"+$index+\"_"+element?index+"_ctrl\",\""+elbindin+"\",$event.target.value,"+element.onChange???then("\""+element.onChange+"\"",'null')+");" />

                                <#assign vModel = "v-bind:value='"+(elbindin)+"' v-on:change='"+(elbindin)+" = $event.target.value"+onchange+"' " />

                                <#if eType == "number" >
                                    <#assign type = "number" />
                                    <#assign vModel = "v-bind:value:number='"+(elbindin)+"' v-on:change='"+(elbindin)+" = $event.target.value"+onchange+"' " />
                                <#elseif eType == "date">
                                    <#assign type = element.subtype!"date" />
                                    <#if type == "date-time">
                                        <#assign type = "datetime-local" />
                                    </#if>
                                    <#if type == "date">
                                        <#assign vModel = "v-bind:value='dateRead("+(elbindin)+",\""+(element.offset!'')+"\")' v-on:change='"+(elbindin)+" = dateWrite($event.target.value,\""+(element.offset!'')+"\")"+onchange+"'" />
                                    </#if>
                                    <#if type == "datetime-local">
                                        <#assign vModel = "v-bind:value='datetimeRead("+(elbindin)+")' v-on:change='"+(elbindin)+" = datetimeWrite($event.target.value)"+onchange+"'" />
                                    </#if>
                                <#else>
                                    <#assign type = element.subtype!"text" />
                                </#if>

                                <!-- text element -->

                                <#assign pattern = element.regex!'' />

                                <#if ((element.subtype!'text')=='text' && (element.min?? || element.max??) ) >
                                    <#assign pattern=".{" + (element.min!'') + "," + (element.max!'') + "}"  >
                                </#if>

                                <#assign lsfx = ((element.subtype!'text')=='text')?then('length','') />

                                <label :for="${elementVueId}+'_ctrl'">${element.label!''}</label>
                                <input
                                        autocomplete = "off"
                                        :id="${elementVueId}+'_ctrl'"
                                        :disabled="${element.disabled?has_content?then(element.disabled,'false')}"
                                        :class="{ disabled : (${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true') }"
                                        placeholder="${element.placeholder!''}"
                                        class="form-control "
                                        type="${type}"
                                        :name="getName('${ncontext}','${elbindout}',$index)"
                                        :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"

                                        ${vModel}

                                        ${((element.required)!false)?then('required=\'required\'','')}

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                        ${pattern?has_content?then('pattern="'+pattern+'"','')}
                                        ${element.step???then('step=\''+element.step+'\'','')}
                                        ${element.min???then('min=\''+element.min+'\'','')}
                                        ${element.max???then('max=\''+element.max+'\'','')}
                                        data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                        data-invalid-feedback="${element.invalidFeedback!''}"

                                />

                                <#if element.description?has_content >
                                <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>
                                <!-- end text element -->

                                <#break>

                            <#case 'textarea'>

                                <!-- textarea element -->

                                <label :for="${elementVueId}+'_ctrl'">${element.label!''}</label>

                                <#assign hideGutter = (element.classes!(element.class!''))?contains('hide-gutter')?then('hideGutter="true"','') />

                                <#assign lang = element.subtype!"text" />
                                <#assign rows = (element.height!1)?number />

                                <editor
                                        :id="${elementVueId}+'_ctrl'"
                                        ${hideGutter}

                                        ${ (rows>0)?then('rows=\"'+rows+'\"','rows=\"3\"')}
                                        theme = "dawn"
                                        lang = "${lang}"
                                        :disabled="${element.disabled?has_content?then(element.disabled,'false')}"
                                        :readOnly="${element.disabled?has_content?then(element.disabled,'false')}"
                                        :class="{ disabled : (${element.disabled?has_content?then(element.disabled,'false')}) || (disabled=='true') }"
                                        placeholder="${element.placeholder!''}"
                                        class="form-control "
                                        :name="getName('${ncontext}','${elbindout}',$index)"
                                        :data-simplename="getSimpleName('${element.label!''}','${elbindout}')"

                                        v-model="${elbindin}"

                                        v-on:change-content="onchange('#'+${elementVueId}+'_ctrl','${elbindin}',$event,${element.onChange???then("'"+element.onChange+"'",'null')})"

                                        ${((element.required)!false)?then('required=\'required\'','')}

                                        data-input="${elbindin}"
                                        data-output="${elbindout}"
                                        :data-bind="${elbindin}"

                                        ${pattern?has_content?then('pattern="'+pattern+'"','')}
                                        ${element.min???then('min=\''+element.min+'\'','')}
                                        ${element.max???then('max=\''+element.max+'\'','')}
                                        data-valid="${element.valid?has_content?then(element.valid,'true')}"
                                        data-invalid-feedback="${element.invalidFeedback!''}"

                                ></editor>

                                <#if element.description?has_content >
                                    <small class="form-text text-muted">${element.description}</small>
                                </#if>

                                <span class="invalid-feedback feedback" >
                                {{(form$invalids['#'+${elementVueId}+'_ctrl']||{}).message}}
                                </span>
                                <!-- end textarea element -->

                                <#break>

                            <#default>
                                <!-- empty -->
                            </#switch>
                            <#if wrap>
                            </div>
                            </#if>

                            <#if (element.break!break) >
                            ${'</div>'}
                            <#assign pull = ((section.toolbar!false) && (elements[(element?index+1)].action == "back"))!false />
                            ${'<div'} style="${section.style!''};${pull?then('float:right;margin-right:0px','')}" class="section-row row ${section.class!''}" >
                            </#if>

                        </#list>
                        </div>

                    </div>

                    <#if (section.type!'single')=='repeatable' >
                    <div class="row" >
                        <button type="button"
                                ${(section.repeatable.max!'')?has_content?then(':disabled="'+context+'.length>='+section.repeatable.max+'"','')}
                                @click="addsection(${context})" class="col-auto btn btn-link add-button" >
                            ${(section.repeatable.addLabel)!'<i class="bi bi-plus-circle-fill text-warning"></i>&nbsp;{{ADD_ROW}}'}
                        </button>
                    </div>
                    </#if>
                </div>

                <div class="section-footer">${section.footer!''}</div>

            </fieldset>
            </#list>

        </div>
        </#list>

        </div>

        <!-- footer -->
        <#if form.footer?has_content>
        <span class="form-footer">${form.footer}</span>
        </#if>

        <simplemodal ref="confirm" buttons="ok|close">
            <span>{{form$modal.message}}</span>
        </simplemodal>

        <simplemodal ref="prompt" buttons="ok|close">
            <span>{{form$modal.message}}</span>
            <input type="text" v-model="form$modal.value" />
        </simplemodal>

    </form>

<#noparse>
<#if render!='template'>
${'</script>'}

<div v-cloak id="${formId}">
    <ks-form disabled="false" form-id="${formId}"  ref="form" @input="model = $event" :model="this['model']? model : JSON.parse($('#${formId}_model').text())" template="#${formId}_template" />
</div>

<script id="${formId}_model" type="application/json">
    ${formModel}
</script>
</#if>
</#noparse>

<#if form.script?has_content>
${"<#if render!='template'>"}
<script id="${"$"+"{formId}"}_scripts">
${form.script!''}
</script>
${"</#if>"}
</#if>

<#noparse>

<#if (render=='application') >

<div id="form-renderer">
</div>

<script>
    var formApp = render("${formId}");
</script>

</#if>
</#noparse>

<!-- end form-renderer -->