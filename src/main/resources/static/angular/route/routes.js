myApp.config(function ($routeProvider){
    $routeProvider
        .when('/',{
        templateUrl:'modules/home/home.html',
            controller:'homeController',
            controllerAs:'home'
    }).when('/users',{
        templateUrl:'modules/users/views/create.html',
        controller:'userController',
        controllerAs:'createUser'
    }).when('/users/list',{
        templateUrl:'modules/users/views/user-list.html',
        controller:'userController',
        controllerAs:'userList'
    }).when('/users/:id/edit',{
        templateUrl:'modules/users/views/update.html',
        controller:'userController',
        controllerAs:'updateUser'
    }).when('/customers/save',{
        templateUrl:'modules/customers/views/create.html',
        controller:'customerController',
        controllerAs:'createCustomer'
    }).when('/customers',{
        templateUrl:'modules/customers/views/customer-list.html',
        controller:'customerController',
        controllerAs:'customerList'
    }).when('/customers/:id/edit',{
        templateUrl:'modules/customers/views/update.html',
        controller:'customerController',
        controllerAs:'updateCustomer'
    }).when('/products/save',{
        templateUrl:'modules/products/views/create-product.html',
        controller:'productController',
        controllerAs:'saveProduct'
    }).when('/products',{
        templateUrl:'modules/products/views/product-list.html',
        controller:'productController',
        controllerAs:'productList'
    }).when('/products/:id/edit',{
        templateUrl:'modules/products/views/update_product.html',
        controller:'productController',
        controllerAs:'updateProduct'
    })










    .otherwise({
        template : "<h1>400</h1><h3>Error: 400 PAGE NOT FOUND !</h3><p>Nothing has been selected</p>"
      });
})