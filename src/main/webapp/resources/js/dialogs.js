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
