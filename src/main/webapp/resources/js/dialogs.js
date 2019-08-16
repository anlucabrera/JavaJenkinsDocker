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
