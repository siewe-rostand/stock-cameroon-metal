
myApp.factory('utils',function ($http){
    let obj={};
    const base_url ='http://localhost:8080';

    obj.getBaseUrl = function (){
        console.log('base url')
        return base_url;
    }

    obj.loadDatatable = function (tableId){
        $(document).ready( function () {
            $('#'+tableId).DataTable();
        } );
    }

    obj.destroyDatatable = function(tableId){
        $('#'+tableId).removeClass('initialized').DataTable().destroy();
    }

    obj.toast = function (trueorfalse, message){

    }

    return obj;
})