'use strict';

myApp.factory('orderService',['$http','$q','config', function($http,$q,config){

    let base_url = config.base_url;

    return {
        createOrder: createOrder,
        updateOrder: updateOrder,
        getAllOrders:getAllOrders
    };


    function createOrder(data) {
        let deferred = $q.defer();
        $http.post(base_url + '/vente/create', data)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('++Error while createing new order ++');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function updateOrder(data) {
        let deferred = $q.defer();
        $http.post(base_url + '/vente/update', data)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while updating a particular order');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }


    function getAllOrders()  {
        let deferred = $q.defer();
        $http({
            method:'GET',
            url:base_url+'/vente',
            headers:{
                'Content-Type': 'application/json'
            }
        })
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching all orders');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);