myApp.config(function ($routeProvider,$locationProvider){
    $routeProvider
        .when('/',{
        templateUrl:'modules/home/home.html',
            controller:'homeController'
    }).when('/users',{
        templateUrl:'modules/users/views/create.html'
    })
})