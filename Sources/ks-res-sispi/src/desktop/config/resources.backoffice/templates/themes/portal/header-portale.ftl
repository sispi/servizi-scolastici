<link rel="stylesheet" href="${theme.common}/static/header.css" >
<link rel="stylesheet" href="/static/vendor/fontawesome/all.min.css" >
<script src="/static/vendor/fontawesome/all.min.js"></script>
<header id="k-topnav" style="height: unset;">
  <div class="it-header-slim-wrapper">
    <div class="container">
      <div class="row">
        <div class="col-12">
          <div class="it-header-slim-wrapper-content">
            <a class="d-none d-lg-block navbar-brand" href="/public/index">Regione Sicilia</a>
            <div class="nav-mobile">
              <nav>
                <a class="it-opener d-lg-none" data-toggle="collapse" href="#menu1" role="button" aria-expanded="false" aria-controls="menu1">
                  <span>Regione Sicilia</span>
                  &nbsp;<i class="fa fa-expand" aria-hidden="true"></i>
                </a>
                <div class="link-list-wrapper collapse" id="menu1">
                  <#if utils.userInfo.authenticated >
                    <ul class="link-list">
                      <li>
                        <a href="/portal/user/myProfile" class="list-item" title="${utils.userInfo.username}">
                          <i class="fas fa-user"></i> ${utils.userInfo.fullname}
                        </a>
                      </li>
                    </ul>
                  </#if>
                </div>
              </nav>
            </div>
            <div class="it-header-slim-right-zone">

              <div class="it-access-top-wrapper">
                <#if ($.userInfo.isGuest())>
                  <a class="btn btn-primary btn-sm" href="/auth/redirect?sso"><i class="fas fa-sign-in-alt"></i> Entra</a>
                  <a class="btn btn-primary btn-sm" href="/auth/login"><i class="fas fa-sign-in-alt"></i> Ks</a>
                <#else>
                  <a class="btn btn-primary btn-sm" href="/auth/logout"><i class="fas fa-sign-out-alt"></i> Esci</a>
                </#if>

              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</header>

<#assign parts = utils.request.servletPath?split('/')[1..] />
<div class="it-nav-wrapper">
  <div class="it-header-center-wrapper">
    <div class="container">
      <div class="row">
        <div class="col-12">
          <div class="it-header-center-content-wrapper">
            <div class="it-brand-wrapper">
              <a href="/public/index">
                <span id="logo-header"></span>
                <div class="it-brand-text">
                  <h2 class="no_toc menu-txt">Comune di Palermo</h2>
                  <h3 class="no_toc menu-txt d-none d-md-block">Citta' di Palermo</h3>
                </div>
              </a>
            </div>
            <div class="it-right-zone">
              <div class="it-search-wrapper">
                <!-- ricerca libera da implementare
                <span class="d-none d-md-block menu-search">Cerca <span id="tot-not"></span></span>
                <a class="search-link rounded-icon" aria-label="Cerca" href="#">
                  <i class="fas fa-search"></i>
                </a>
                -->
                <#if !$.userInfo.isGuest() >
                  <span class="d-none d-md-block menu-search">Notifiche</span>
                  <a style="padding-left: 10px;" class="btn btn-link text-white" href="/portal/features/myPractices">
                    <i class="fas fa-bell color-accent" aria-hidden="true"></i> <span class="badge badge-danger" id="badge-bell"></span>
                  </a>
                </#if>
                <#if (!(utils.getRequest().queryString!'')?ends_with("&inspector") && utils.hasGroup(['admins'])) >
                  <a class="btn btn-link text-white" href="${utils.getRequest().requestURI+'?'+(utils.getRequest().queryString!'')+'&inspector#inspector' }">
                    <i class="fa fa-eye" aria-hidden="true"></i>
                  </a>
                </#if>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <#if !$.userInfo.isGuest() >
    <script>
      var notificationResponse = {
        data: null,
        status: null,
        error: null
      };
      $.ajax({
        url: "/portale/v1/notification/count",
        async: false,
        type: "GET",
        success: function (data, status) {
          notificationResponse = {
            data,
            status,
            error: null
          };
        },
        error: function (status, error) {
          notificationResponse = {
            data: null,
            status,
            error
          }
        },
      });
      if(notificationResponse.status === 'success'){
        document.getElementById("badge-bell").innerHTML = notificationResponse.data.portal + notificationResponse.data.bpm;
      }
    </script>
  </#if>

  <#include "menu-portale.ftl" />

</div>

<div class="mt-5"></div>

<#if (utils.userInfo.admin && warning??) >
  ${ utils.ftl('error.ftl', { "exception": warning, "warning":true} ) }
</#if>
