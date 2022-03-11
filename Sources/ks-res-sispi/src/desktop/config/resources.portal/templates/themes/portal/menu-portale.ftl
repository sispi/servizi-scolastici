<#if menu??>
<div class="it-header-navbar-wrapper">
    <div class="container">
        <div class="row">
            <div class="col-12">
                <!--start nav-->
                <nav class="navbar navbar-expand-lg has-megamenu">
                    <button class="custom-navbar-toggler" type="button" aria-controls="nav02" aria-expanded="false" aria-label="Toggle navigation" data-target="#nav02">
                        <i class="fa fa-bars" aria-hidden="true"></i>
                    </button>
                    <div class="navbar-collapsable" id="nav02" style="display: none;">
                        <div class="overlay" style="display: none;"></div>
                        <div class="close-div sr-only">
                            <button class="btn close-menu" type="button"><span class="it-close"></span>close</button>
                        </div>
                        <div class="menu-wrapper">
                            <ul class="navbar-nav">
                                <#list (menu.items)![] as item>
                                <#if springMacroRequestContext.requestUri?contains("/index")>
                                    <li class="nav-item active">
                                        <a class="nav-link active" href="${item.url}">
                                            <span>${item.title} </span><span class="sr-only">current</span>
                                        </a>
                                    </li>
                                <#else>
                                    <li class="nav-item">
                                        <a class="nav-link" href="${item.url!''}">
                                            <span>${item.title!''} </span>
                                        </a>
                                    </li>
                                </#if>
                                </#list>

                                <#assign bacheca= (menu.bacheca![]) />
                                <#if (bacheca?size >0 && !$.userInfo.isGuest() ) >
                                    <li class="nav-item dropdown">
                                        <a class="nav-link dropdown-toggle" href="#" data-toggle="dropdown" aria-expanded="false">
                                            <span>Bacheca <i class="fa fa-chevron-down" aria-hidden="true"></i></span>
                                        </a>
                                        <div class="dropdown-menu" style="width: max-content;">
                                            <div class="link-list-wrapper">
                                                <ul class="link-list">
                                                    <li>
                                                        <h3 class="no_toc" id="heading-es-5">Bacheca</h3>
                                                    </li>
                                                    <#list bacheca as item>
                                                    <li>
                                                        <a class="list-item" href="${item.url}"><i class="${item.icon}" aria-hidden="true"></i> <span>${item.title}</span></a>
                                                    </li>
                                                    </#list>
                                                </ul>
                                            </div>
                                        </div>
                                    </li>
                                </#if>
                            </ul>
                        </div>
                    </div>
                </nav>
            </div>
        </div>
    </div>
</div>
</#if>