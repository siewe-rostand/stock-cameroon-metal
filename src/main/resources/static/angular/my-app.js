
let  myApp =angular.module('POS',['ngRoute','ngStorage','ngAnimate' ,'toaster']).constant('config',{
    'base_url':'http://localhost:8080/api'
});

// myApp.config(['$httpProvider', function ($httpProvider) {
//     $httpProvider.interceptors.push('AuthInterceptor');
// }]);
