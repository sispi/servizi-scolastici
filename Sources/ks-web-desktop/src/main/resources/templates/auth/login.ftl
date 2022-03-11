<link rel="stylesheet" href="/static/css/login.css?no-cache" >
<div class="offset-md-3 col-md-6 col-sm-8 offset-sm-2">
	<div id="logo_login"><img id="logo" src="/static/images/logo_login_1.png" alt="logo_login"></div>
	<form id="executeLogin" action="/auth/executeLogin" method="POST">
		<h2 id="login_header" >Accesso Utente</h2>
		<input type="hidden" name="requestURL" value="${utils.request.getParameter('requestURL')!''}"/>

			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text glyphicon glyphicon-user" id="username_label"></span></span>
				</div>
				<input type="text" class="form-control" placeholder="Nome utente" aria-label="Nome Utente" aria-describedby="username_label" id="username" name='username' value="${utils.request.getParameter('username')!""}">
			</div>
			<div class="input-group mb-3">
				<div class="input-group-prepend">
					<span class="input-group-text glyphicon glyphicon-lock" id="password_label"></span>
				</div>
				<input type="password" class="form-control" placeholder="Password" aria-label="Password" aria-describedby="password_label" id="password" name='password' value="">
			</div>
			<#assign aoos = utils.actorsCache.getAOO() />
			<#assign codEnte = utils.userInfo.codEnte />
			<#assign codAoo = utils.userInfo.codAoo />
			<#assign visibles = 0 />
			<div>
				<span>Ente corrente:</span>&nbsp;<b>${utils.actorsCache.getEnte(codEnte).displayName}</b>
				<span class="vis0 d-none">Aoo corrente:</span>&nbsp;<b class="vis0 d-none">${utils.actorsCache.getAOO(codAoo).displayName}</b>
			</div>

			<div class="input-group mb-3" id="aoos" >
				<div class="input-group-prepend">
					<span class="input-group-text glyphicon glyphicon-bookmark" id="aoo_label_label"></span>
				</div>
				<select name='aoo' class='form-control' placeholder="Aoo" aria-label="Aoo" aria-describedby="aoo_label"/>
				<#list aoos as item>
					<#if item.codEnte == codEnte>
						<#assign visibles = visibles + 1 />
					<option ${ ( (utils.request.getParameter('aoo')!codAoo) == item.groupId )?string('selected', '')} value="${item.groupId}">${item.groupName}</option>
					</#if>
				</#list>
				</select>
				<#if visibles<2 >
					<style>
						#aoos {
							visibility:hidden;
							height:0px;
						}
						.vis0 {
							display: inline-block !important;
						}
					</style>
				</#if>
			</div>

		<div class="form-group">
			<div style="color:red">
				${utils.request.getParameter('message')!"&nbsp;"}
			</div>

			<button id="btn_accedi" class='btn btn-primary' name='submit' type='submit' value='Accedi'><span class="fa fa-key"></span>&nbsp;Accedi</button>
		</div>
	</form>
</div>



