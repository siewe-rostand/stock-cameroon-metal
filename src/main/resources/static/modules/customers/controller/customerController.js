myApp.controller('customerController', ['$scope', 'customerControllerService','toaster', function ($scope, customerControllerService, toaster) {


    $scope.customer = {
        id: null, firstname: '', lastname: '', phone: '',
        phone2: '', city: '', quarter: '',
    };

    $scope.submit = submit;
    $scope.remove = remove;
    $scope.getClickUser = getClickUser;

    fetchAllCustomers();


    $scope.initView = function (){
        let clickedUser = JSON.parse(localStorage.getItem('saved'));
        $scope.clickedUser = clickedUser;
        $scope.customer = clickedUser;
        // console.log($scope.clickedUser)
   }

    function fetchAllCustomers() {
        // toaster.options.onclick = function() { console.log('clicked'); }
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
                console.log("save new customer response",res);
                fetchAllCustomers();
            },
            function (errResponse) {
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

    function deleteUser(id) {
        customerControllerService.deleteCustomer(id)
            .then(
                function (res){
                    console.log(res);
                    fetchAllCustomers();
                },
                function (errResponse) {
                    console.log(errResponse);
                    console.error('Error while deleting User');
                }
            );
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


    function remove(id) {
        console.log('id to be deleted', id);
        if ($scope.customer.id === id) {//clean form if the user to be deleted is shown there.
            deleteUser(id);
        }
    }

    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }
    
}])
