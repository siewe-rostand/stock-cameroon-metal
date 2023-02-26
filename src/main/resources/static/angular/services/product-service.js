'use strict';

myApp.factory('productservice',['$http','$q','config',function($http,$q,config){
    let base_url = config.base_url;

    return {
        fetchAllProducts: fetchAllProducts,
        createProduct: createProduct,
        updateProduct: updateProduct,
        deleteProduct: deleteProduct
    };

    function fetchAllProducts() {
        let deferred = $q.defer();
        $http.get(base_url+'/products')
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

    function createProduct(product) {
        let deferred = $q.defer();
        $http.post(base_url + '/products', product)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function updateProduct(product) {
        let deferred = $q.defer();
        $http.put(base_url +'/products', product)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteProduct(id) {
        let deferred = $q.defer();
        $http.delete(base_url+ '/users/' + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error(errResponse);
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);