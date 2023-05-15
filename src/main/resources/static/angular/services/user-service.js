'use strict';

myApp.factory('UserService', ['$http', '$q','config', function ($http, $q,config) {

    let REST_SERVICE_URI = config.base_url;

    return {
        fetchAllUsers: fetchAllUsers,
        createUser: createUser,
        updateUser: updateUser,
        deleteUser: deleteUser
    };

    function fetchAllUsers(url) {
        let deferred = $q.defer();
        $http.get(REST_SERVICE_URI+url)
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

    function createUser(url,user) {
        let deferred = $q.defer();
        $http.post(REST_SERVICE_URI + url, user)
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

    function updateUser(url,user) {
        let deferred = $q.defer();
        $http.put(REST_SERVICE_URI +url, user)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while updating User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteUser(url,id) {
        let deferred = $q.defer();
        $http.delete(REST_SERVICE_URI+ url + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while deleting User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function login(user) {
        let deferred = $q.defer();
        $http.post(base_url + '/login', user)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while logging in User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function getUserIfo(token) {
        let deferred = $q.defer();
        $http.post(base_url + '/user', token)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while logging in User');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);