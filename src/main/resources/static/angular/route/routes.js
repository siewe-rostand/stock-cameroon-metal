myApp.config(function ($routeProvider){
    $routeProvider
        .when('/',{
        templateUrl:'modules/users/views/login.html',
            controller:'authController',
            controllerAs:'login'
    }).when('/login',{
        templateUrl:'modules/users/views/login.html',
            controller:'authController',
            controllerAs:'login'
    }).when('/home',{
        templateUrl:'modules/home/home.html',
            controller:'homeController',
            controllerAs:'home'
    }).when('/users',{
        templateUrl:'modules/users/views/create.html',
        controller:'userController',
        controllerAs:'createUser'
    }).when('/register',{
        templateUrl:'modules/users/views/register.html',
        controller:'authController',
        controllerAs:'registerUser'
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
        templateUrl:'modules/produit/views/save_produit.html',
        controller:'produitController',
        controllerAs:'saveProduct'
    }).when('/products',{
        templateUrl:'modules/produit/views/list_produit.html',
        controller:'produitController',
        controllerAs:'liste_produit'
    }).when('/products/:id/edit',{
        templateUrl:'modules/products/views/update_product.html',
        controller:'productController',
        controllerAs:'updateProduct'
    }).when('/products/category/:id',{
        templateUrl:'modules/products/views/update_product.html',
        controller:'categoryController',
        controllerAs:'updateProduct'
    }).when('/category',{
        templateUrl:'modules/category/views/update_product.html',
        controller:'categoryController',
        controllerAs:'categoryList'
    }).when('/orders',{
        templateUrl:'modules/orders/views/order_page.html',
        controller:'orderController',
        controllerAs:'orderPage'
    }).when('/orders/clients',{
        templateUrl:'modules/orders/views/customer_list.html',
        controller:'orderController',
        controllerAs:'orderPage'
    }).when('/orders/review',{
        templateUrl:'modules/orders/views/order_review.html',
        controller:'orderController',
        controllerAs:'orderReview'
    })










    .otherwise({
        template : "<h1>400</h1><h3>Error: 400 PAGE NOT FOUND !</h3><p>Nothing has been selected</p>"
      });
})