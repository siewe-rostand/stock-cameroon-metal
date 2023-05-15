myApp.controller('homeController',function ($scope,$http,config,$window){
    getUsers();
    $scope.getClickUser = getClickUser;


    function getClickUser(user){
        $window.localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }
    function getUsers() {
        let data = $scope.users;
        console.log(config)
        let url = config.base_url+'/users?roles=user';
        $http({
            url: url,
            method: 'GET',
            contentType: 'application/json; charset=utf-8',
            // data: data
        }).then(function (result) { // this is the success
            console.log("get all users from home page",result.data.content);
            $scope.allUsers =result.data.content;
        }, function (data, status, header) {  // this is the error
            console.log('error while getting users',header)
            console.error(data);
        });
    }
})