
myApp.factory('utils',function ($http,$q){
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

myApp.factory('notifierService',function(toaster){
    return{
        notify: function(msg){
                toaster.pop('success', 'Update Successful', 'The ' + msg + ' setting was updated');
        },
        notifyError: function(msg){
                toaster.pop('error', 'Something Went Wrong', 'Please check with an administrator');
        },
        notifyInfo: function(msg){
                toaster.pop('info', 'Information', 'The ' + msg + 'just happened' );
        }
    };
})