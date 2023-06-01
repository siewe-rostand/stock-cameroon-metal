
'use strict';

myApp.factory('categoryService',['$http','$q','config', function($http,$q,config){

    let base_url = config.base_url;

    return {
        fetchAllCategory: fetchAllCategory,
        createCategory: createCategory,
        updateCategory: updateCategory,
        deleteCategory: deleteCategory,
        fetchCategoryByProduct:fetchCategoryByProduct
    };

    function fetchAllCategory() {
        let deferred = $q.defer();
        $http.get(base_url+'/categories-enabled')
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching categories');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function fetchCategoryByProduct(id) {
        let deferred = $q.defer();
        $http.get(base_url+'/products-by-category/' + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while fetching products by category');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function createCategory(category) {
        let deferred = $q.defer();
        $http.post(base_url + '/categories', category)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while creating category');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function updateCategory(category) {
        let deferred = $q.defer();
        $http.put(base_url +'/categories', category)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while updating category');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

    function deleteCategory(id) {
        let deferred = $q.defer();
        $http.delete(base_url+ '/categories/' + id)
            .then(
                function (response) {
                    deferred.resolve(response.data);
                },
                function (errResponse) {
                    console.error('Error while deleting category');
                    deferred.reject(errResponse);
                }
            );
        return deferred.promise;
    }

}]);