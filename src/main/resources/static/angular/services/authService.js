'use strict';

myApp.factory('authService',['$http','$q','config', function($http,$q,config){

    let base_url = config.base_url;

    return {
        login: login,
        signUp: register,
        userInfo:getUserInfo
    };


    function login(data) {
        let deferred = $q.defer();
        $http.post(base_url + '/login', data)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('===Error while logging in the user User===');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function register(user) {
        let deferred = $q.defer();
        $http.post(base_url + '/register', user)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while creating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function getUserInfo()  {
        let token =localStorage.getItem('token')
        let deferred = $q.defer();
        $http({
            method:'GET',
            url:base_url+'/user',
            headers:{
                'Content-Type': 'application/json',
                'Authorization':'Bearer '+token
            }
        })
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching Users');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);