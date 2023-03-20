myApp.controller('customerController', ['$scope', 'customerControllerService', function ($scope, customerControllerService, ) {


    $scope.customer = {
        id: null, firstname: '', lastname: '', phone: '',
        phone2: '', city: '', quarter: '',
    };

    $scope.submit = submit;
    $scope.remove = deleteUser;
    $scope.getClickUser = getClickUser;

    fetchAllCustomers();


    $scope.initView = function (){
        let clickedUser = JSON.parse(localStorage.getItem('saved'));
        $scope.clickedUser = clickedUser;
        $scope.customer = clickedUser;
        // console.log($scope.clickedUser)
   }

    function fetchAllCustomers() {
        customerControllerService.fetchAllCustomers()
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');
                    $scope.allCustomers=d.content;
                    // utils.loadDatatable('user_table');
                    // console.log(d)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('Error while fetching Users');
                }
            );
    }

    function createCustomer(customer){
        customerControllerService.createCustomers(customer)
        .then(
            function (res){
                Toast.fire({
                    icon:'success',
                    title: 'Client enregistré avec success'
                })
                console.log("save new customer response",res);
                fetchAllCustomers();
            },
            function (errResponse) {
                Toast.fire({
                    icon:'error',
                    title: 'une erreur s\'est produite'
                })
                console.error(errResponse)
                console.error('Error while creating User');
            }
        );
    }


    function updateUser(customer) {
        customerControllerService.updateCustomers(customer)
            .then(
                fetchAllCustomers,
                function (errResponse) {
                    console.log(errResponse);
                    console.error('Error while updating User');
                }
            );
    }

    function deleteUser(user) {
        swalWithBootstrapButtons.fire({
            title: 'Êtes-vous sûr?',
            html: " de vouloir supprimer"+" <strong>"+ user.fullname + "</strong> " + "?cette action est irréversible",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Oui, Supprimer!',
            cancelButtonText: 'Non, annuler!',
            reverseButtons:true,
            showLoaderOnConfirm: true,
            allowOutsideClick: () => !Swal.isLoading(),
            preConfirm: () => {     
                return customerControllerService.deleteCustomer(user.id)
                .then(
                    function (res){
                        swalWithBootstrapButtons.fire("Supprimmer!", "utilisateur supprimé avec succès.", "success");
                          console.log(res);
                        fetchAllCustomers();
                    },
                    function (errResponse) {
                        swalWithBootstrapButtons.fire("OUPS!", "une erreur s'est produite lors de la suppression de cet utilisateur si le problème persiste, veuillez appeler la cellule informatique ", "error");
                          console.error('Error while deleting customer',errResponse);
                    }
                );
            },
          }).then((result) => {
            if (result.dismiss === Swal.DismissReason.cancel) {
                swalWithBootstrapButtons.fire("Annuler", "L'utilisateur ne sera pas supprimer!!", "error");
            }
          })
        
    }

    function submit() {
        console.log($scope.customer)
        if ($scope.customer.id === null) {
            console.log('Saving New User', $scope.customer);
            createCustomer($scope.customer);
        } else {
            updateUser($scope.customer);
            console.log('User updated with id ', $scope.customer.id);
        }
        // reset();
    }


    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }
    
}])
