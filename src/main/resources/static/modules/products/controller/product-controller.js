
myApp.controller('productController',['$scope','productService', function($scope, productService){

    fetchAllProducts();
    function fetchAllProducts() {
        productService.fetchAllProducts()
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');

                    // utils.loadDatatable('user_table');
                     console.log(d)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('controller:Error while fetching products');
                }
            );
    }
}]);