
myApp.directive('sidebar',function (){
    return {
        restrict:'E',
        templateUrl:'modules/pages/sidebar.html'
    }
});
myApp.directive('homeFooter',function (){
    return {
        restrict:'E',
        templateUrl:'modules/pages/footer.html'
    }
});
myApp.directive('header',function (){
    return {
        restrict:'E',
        templateUrl:'modules/pages/footer.html'
    }
});
myApp.directive('userList',function (){
    return {
        restrict:'E',
        templateUrl:'modules/users/views/directive/user_list.html'
    }
});
myApp.directive('customerList',function (){
    return {
        restrict:'E',
        templateUrl:'modules/customers/views/directive/customer_list.html'
    }
});