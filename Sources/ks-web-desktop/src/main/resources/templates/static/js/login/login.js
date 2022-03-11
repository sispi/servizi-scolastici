$("#executeLogin").on("submit", function() {
	
	alert(1);
	return false;
	
});

function onsubmit(){

    var username = $('#username').val();
    var password = $('#password').val();
    $.ajax({
        type: "post",
        url: "auth/executePreLogin",
        dataType: "json",
        data: {
            username: username,
            password: password
        },
        success: function(aooList){
            var label ="";
            var s =$("<select name='COD_AOO' class='form-control'/>");
            $("#writeEnteUtente").empty();
            if(aooList.length>0) {
                label = $("<label for='username'>AOO</label>");
                for (var obj in aooList) {
                        var aooG = aooList[obj];
                        // if (j=='${codAoo}')
                        //     $('<option />', {value: j,selected: 'selected', text: aooList[obj][j]}).appendTo(s);
                        // else
                            $('<option />', {value: aooG.groupId, text: aooG.groupName}).appendTo(s);

                }
                label.appendTo('#writeEnteUtente');

                s.appendTo('#writeEnteUtente');

//					s.on('keypress', function(event) {
//						if(event.which === 13) {
//							$('form#executeLogin').submit();
//						}
//					});

                bindSelectionEvents();

                $("#username").attr('readonly', 'true');
                $("#password").attr('readonly', 'true');

                if(aooList.length==1){
                    s.attr('readonly', 'true');
                    $("#writeSubmit").empty();
                    $('#loading').show();
                    $( "#executeLogin" ).submit();
                    return;
                }else if(aooList.length>1){
                    $("#writeSubmit").empty();
                    s.show();
                    var submitChoose = $("<input class='btn btn-primary pull-right' name='submit' type='submit' value='Accedi'>");
                    submitChoose.appendTo("#writeSubmit");

                }
            }else {
                $("#writeEnteUtente").empty();
                s = $("<p style='color:red'>Non appartenente a nessun ente</p>");
                s.appendTo('#writeEnteUtente');
            }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            if (XMLHttpRequest.status===400)
                errorThrown = "Utente inesistente";
            else if (XMLHttpRequest.status===403)
                errorThrown = "Autenticazione negata";
            else
                errorThrown = "Errore del server";

            $("#writeEnteUtente").empty();
            s = $("<p style='color:red'>"+errorThrown+"</p>");
            s.appendTo('#writeEnteUtente');
        }
    });
}

