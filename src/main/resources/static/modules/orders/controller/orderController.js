
myApp.controller('orderController',['$scope','productService','utils', function($scope, productService,utils){



    fetchAllProducts();
    $scope.cart = [];
    $scope.qty = 1;
    $scope.total = 0;
    var products = [];

    // Add a product to the shopping cart
    $scope.addToCart = function(product) {
        // Check if the product is already in the cart
        var found = false;
        for (var i = 0; i < $scope.cart.length; i++) {
            if ($scope.cart[i].product === product) {
                found = true;
                $scope.cart[i].quantity++;
                break;
            }
        }

        // If the product is not in the cart, add it
        if (!found) {
            $scope.cart.push({
                product: product,
                quantity: 1,
                total: product.price
            });
        }
        console.log($scope.cart + '////')

        // Update the total
        $scope.updateTotal();
    };
    // Remove a product from the shopping cart
    $scope.removeFromCart = function(item) {
        // Remove the item from the cart
        var index = $scope.cart.indexOf(item);
        $scope.cart.splice(index, 1);

        // Update the total
        $scope.updateTotal();
    };
    // Update the total cost of the shopping cart
    $scope.updateTotal = function() {
        var total = 0;
        for (var i = 0; i < $scope.cart.length; i++) {
            total += $scope.cart[i].total;
        }
        $scope.total = total;
    };

    function fetchAllProducts() {
        productService.fetchAllProducts()
            .then(
                function (response) {
                    utils.destroyDatatable('product_table');
                    angular.forEach(response.content, function (product,k){
                        console.log(product.id + ' ' +k)
                        productService.getProductImage(product.id).then(
                            function (response){
                                console.log('product image'+response)
                                var uint8Array = new Uint8Array(response);
                                var binaryString = String.fromCharCode.apply(null, uint8Array);
                                var base64String = btoa(binaryString);
                                 $scope.productimage = 'data:image/JPEG;base64,' + base64String;
                                 products.push(product);
                                    $scope.products =products;
                            },
                            function (errResponse) {
                                console.log(errResponse)
                                console.error('controller:Error while fetching products');
                            }
                        )
                    })
                    utils.loadDatatable('product_table');
                    console.log(response)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('controller:Error while fetching products');
                }
            );
    }

}]);