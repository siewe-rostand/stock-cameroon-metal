'use strict';

myApp.factory('orderService',['$http','$q','config', function($http,$q,config){

    let base_url = config.base_url;

    return {
        createOrder: createOrder,
        updateOrder: updateOrder,
        getAllOrders:getAllOrders
    };


    function createOrder(order) {
        let deferred = $q.defer();
        $http.post(base_url + '/orders/create', order)
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


    function updateOrder(order) {
        let deferred = $q.defer();
        $http.post(base_url + '/orders/update', order)
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
            url:base_url+'/orders',
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