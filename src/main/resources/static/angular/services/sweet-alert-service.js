(function(){
    'use strict';
        myApp.factory('SweetAlertService', SweetAlertService);

    SweetAlertService.$inject = [];
    function SweetAlertService( ) {

    	var swal = window.swal;
    	

    	//public methods
    	var self = {

    		swal: function ( arg1, arg2, arg3) {
    				if( typeof(arg2) === 'function' ) {
    					swal( arg1, function(isConfirm){
    							arg2(isConfirm);
    					}, arg3 );
    				} else {
    					swal( arg1, arg2, arg3 );
    				}
    		},
    		success: function(title, message) {
				swal( title, message, 'success' );
    		},
    		error: function(title, message) {
				swal( title, message, 'error' );
    		},
    		warning: function(title, message) {
				swal( title, message, 'warning' );
    		},
    		info: function(title, message) {
				swal( title, message, 'info' );
    		},
			autoCloseError: function(title, message){
				swal( {title: title, text: message, type: 'error', timer: 3000});
			},
			autoCloseInfo: function(title, message){
				swal( {title: title, text: message, type: 'info', timer: 750});
			},
			confirm: function(title, message, callback) {
				  swal({
				    title: title,
				    text: message,
				    type: "warning",
				    showCancelButton: true,
				    confirmButtonColor: '#33414e',
				    confirmButtonTexColor: '#FFF;',
				    closeOnConfirm: true,
				    closeOnCancel: true,
				    timer: 1500
				  },
				  function(isConfirm) {
				      callback(isConfirm);
				  });
				},
            deleteModal: function(message,callback){
                swal({
                    title: 'Are you sure?',
                    text: "Êtes-vous sûr de vouloir supprimer"+" "+ message + " " + "?cette action est irréversible",
                    type: 'warning',
                    showCancelButton: true,
                    confirmButtonText: 'Oui, Supprimer!',
                    cancelButtonText: 'Non, annuler!',
                    confirmButtonClass: 'btn btn-success margin-5',
                    cancelButtonClass: 'btn btn-danger margin-5',
                    buttonsStyling: false
                }, function(isConfirm){
					if (!isConfirm) {
					  swal("Cancelled", "Your imaginary file is safe :)", "error");
					} else {
					  // $timeout is sample code. Put your http call function into here.
					  $timeout(function(){
						swal("Deleted!", "Your imaginary file has been deleted.", "success");
					  },2000);
			  
					  /*$http({
						method: 'POST',
						url: 'Product.aspx/delete',
						headers: {
						  'Content-Type': 'application/json; charset=utf-8',
						  'dataType': 'json'
						},
						data: { qid: qid }
					  }).then(function (data) {
						  swal("Deleted!", "Your imaginary file has been deleted.", "success");
					  });*/
					};
				  })
            }
			
    	};
    	
    	return self;
     }
})();

const swalWithBootstrapButtons = Swal.mixin({
	customClass: {
	  confirmButton: 'btn btn-success margin-5',
	  cancelButton: 'btn btn-danger margin-5'
	},
	buttonsStyling: false
  })

  const base_url = 'http://localhost:8080';

  const Toast = Swal.mixin({
	toast: true,
	position: 'top-end',
	showConfirmButton: false,
	timer: 3000,
	timerProgressBar: true,
	didOpen: (toast) => {
	  toast.addEventListener('mouseenter', Swal.stopTimer)
	  toast.addEventListener('mouseleave', Swal.resumeTimer)
	}
  })