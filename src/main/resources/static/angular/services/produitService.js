'use strict';

myApp.factory('produitService',['$http','$q','config',function($http, $q, config){
    let base_url = config.base_url;

    return {
        fetchAllProducts: fetchAllProducts,
        createProduct: createProduct,
        updateProduct: updateProduct,
        deleteProduct: deleteProduct,
        getProductImage:getProductImage
    };

    function fetchAllProducts() {
        let deferred = $q.defer();
        $http.get(base_url+'/produit')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Service: Error while fetching product');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function getProductImage(id){
        let deferred = $q.defer();
        $http.get(base_url+'/product-/'+id,{ responseType: 'arraybuffer' })
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Service: Error while fetching product image');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createProduct(product) {
        let deferred = $q.defer();
        $http.post(base_url + '/produit', product)
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
        $http.put(base_url +'/produit', product)
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
        $http.delete(base_url+ '/produit/' + id)
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