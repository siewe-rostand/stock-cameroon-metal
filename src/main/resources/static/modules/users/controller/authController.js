myApp.controller('authController', ['$scope', '$location','authService','$rootScope','$window', function ($scope, $location,authService,$rootScope ,$window) {


    $scope.httpError = '';
    $scope.user = {
        id: null, firstname: '', lastname: '', telephone: '',
       password:'',confirmPassword:''
    };
   $scope.loginData = {
       email:'',password:''
   }

    $scope.signIn = signIn;
    $scope.signUp = signUp;
    $scope.getClickUser = getClickUser;






    function getUserInfo() {
        authService.userInfo()
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');
                    // $scope.allCustomers=d.content;
                    // utils.loadDatatable('user_table');
                    $window.localStorage.setItem('loginUser',JSON.stringify(d.data));
                    console.log(d.data)
                    $location.path('/home')
                    $rootScope.authenticated =true;
                    Toast.fire({
                        icon:'success',
                        title: 'Connexion effectué avec success'
                    })
                },
                function (errResponse) {
                    $rootScope.authenticated =true;
                    console.log(errResponse)
                    console.error('Error while fetching login Users');
                }
            );
    }

    function register(user){
        authService.signUp(user)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'Client enregistré avec success'
                    })
                    console.log("save new customer response",res);
                    $scope.authenticated =true;
                },
                function (errResponse) {
                    Toast.fire({
                        icon:'error',
                        title: 'une erreur s\'est produite'
                    })
                    console.error(errResponse.data.error)
                    $scope.httpError = errResponse.data.error;
                    console.error('Error while registering User');
                }
            );
    }

    function login(data){
        console.log(data)
        authService.login(data)
            .then(
                function (res){
                    $window.localStorage.setItem('token',res.accessToken)
                    console.log("login token",res.accessToken);
                    getUserInfo();
                },
                function (errResponse) {
                    Toast.fire({
                        icon:'error',
                        title: 'une erreur s\'est produite'
                    })
                    console.error(errResponse)
                    console.error('Error while logging in the User');
                }
            );
    }


    function signIn() {
        login($scope.loginData);
        // reset();
    }

    function signUp() {
        register($scope.user);
        // reset();
    }



    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }

}])
