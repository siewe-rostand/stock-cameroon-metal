'use strict';

myApp.factory('customerControllerService',['$http','$q','config', function($http,$q,config){

    let base_url = config.base_url;

    return {
        fetchAllCustomers: fetchAllCustomers,
        createCustomers: createCustomers,
        updateCustomers: updateCustomers,
        deleteCustomer: deleteCustomer
    };

    function fetchAllCustomers() {
        let deferred = $q.defer();
        $http.get(base_url+'/customers')
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

    function createCustomers(customer) {
        let deferred = $q.defer();
        $http.post(base_url + '/customers', customer)
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

    function updateCustomers(customer) {
        let deferred = $q.defer();
        $http.put(base_url +'/customers', customer)
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

    function deleteCustomer(id) {
        let deferred = $q.defer();
        $http.delete(base_url+ '/customers/' + id)
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

}]);