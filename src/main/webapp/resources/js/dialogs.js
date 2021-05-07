$(document).ready(function(){
    //console.log('Entré');
    skin();
//    $('.ui-dialog-content').removeClass('ui-dialog-content');
});

function skin(){
    $('.ui-dialog-titlebar').addClass('panel-heading');
    $('.ui-dialog-titlebar').removeClass('ui-dialog-titlebar');
    $('.ui-dialog-content').addClass('panel-body');
}

function soloNumeros(e){
	var key = window.Event ? e.which : e.keyCode
	return (key ==46 || key >= 48 && key <= 57)
}

function contarCaracteresDictamen(){
	document.getElementById('frmModalTramitarBaja:txtCaracteresRestantes').value = 500 - document.getElementById('frmModalTramitarBaja:dictamenPsicopedagogia').value.length;
}

function contarCaracteresAcciones(){
        document.getElementById('frmModalTramitarBaja:txtCaracteresRestantes').value = 500 - document.getElementById('frmModalTramitarBaja:accionesTutor').value.length;
}

function contarCaracteresObservaciones(){
	document.getElementById('frmModalEditarObservaciones:txtCaracteresRestantes').value = 500 - document.getElementById('frmModalEditarObservaciones:observacionesDocumento').value.length;
}

function contarCaracteresProyectoEstadiaRegistro(){
	document.getElementById('frmModalRegistroInfEst:txtCaracteresRestantesR').value = 1000 - document.getElementById('frmModalRegistroInfEst:proyectoR').value.length;
}

function contarCaracteresProyectoEstadiaEdicion(){
	document.getElementById('frmModalEdicionInfEst:txtCaracteresRestantesA').value = 1000 - document.getElementById('frmModalEdicionInfEst:proyectoA').value.length;
}

function calcularSemanasEstadia(){
	var fechaini = new Date(document.getElementById('frmModalCapturaEmpProy:fechaInicio').value);
	var fechafin = new Date(document.getElementById('frmModalCapturaEmpProy:fechaFin').value);
	var diasdif= fechafin.getTime()-fechaini.getTime();
	var contdias = Math.round(diasdif/(1000*60*60*24));
	document.getElementById('frmModalCapturaEmpProy:txtCaracteresRestantes').value = contdias;
}
