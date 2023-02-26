myApp.controller('userController', ['$scope', 'UserService', 'utils','toaster', function ($scope, UserService, utils, toaster) {

    let self = this;
    $scope.user = {
        id: null, firstname: '', lastname: '', phone: '',password:'',
        phone2: '', city: '', quarter: '', email: '',
    };
    self.users = [];

    $scope.submit = submit;
    $scope.editUser = edit;
    $scope.remove = remove;
    $scope.reset = reset;
    $scope.getClickUser = getClickUser;


    fetchAllUsers();

     $scope.initView=function (){
         let clickedUser = JSON.parse(localStorage.getItem('saved'));
         $scope.clickedUser = clickedUser;
         $scope.user = clickedUser;
         console.log($scope.clickedUser)
    }
    function fetchAllUsers() {
        toaster.options.onclick = function() { console.log('clicked'); }
        UserService.fetchAllUsers('/users?roles=user')
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');
                    $scope.allUsers=d.content;
                    // utils.loadDatatable('user_table');
                    // console.log(d)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('Error while fetching Users');
                }
            );
    }


    function createUser(user) {
        let url = '/register'
        UserService.createUser(url, user)
            .then(
                function (res){
                    console.log("save new user response",res);
                    fetchAllUsers();
                    reset();
                },
                function (errResponse) {
                    console.error(errResponse)
                    console.error('Error while creating User');
                }
            );
    }

    function updateUser(user) {
        UserService.updateUser('/users',user)
            .then(
                fetchAllUsers,
                function (errResponse) {
                    console.log(errResponse);
                    console.error('Error while updating User');
                }
            );
    }

    function deleteUser(id) {
        UserService.deleteUser('/users/',id)
            .then(
                function (res){
                    console.log(res);
                    fetchAllUsers();
                },
                function (errResponse) {
                    console.log(errResponse);
                    console.error('Error while deleting User');
                }
            );
    }

    function submit() {
        console.log($scope.user)
        if ($scope.user.id === null) {
            console.log('Saving New User', $scope.user);
            createUser($scope.user);
        } else {
            updateUser($scope.user);
            console.log('User updated with id ', $scope.user.id);
        }
        // reset();
    }

    function edit(id) {
        console.log('id to be edited', $scope.user);

    }

    function remove(id) {
        console.log('id to be deleted', id);
        if ($scope.user.id === id) {//clean form if the user to be deleted is shown there.
            deleteUser(id);
        }
    }

    function reset() {
        $scope.user = {
            id: null, firstname: '', lastname: '', phone: '',password:'',
            phone2: '', city: '', quarter: '', email: '',
        };
        $scope.myForm.$setPristine(); //reset Form
    }

    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }


}])