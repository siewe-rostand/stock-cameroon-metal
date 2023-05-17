
myApp.controller('orderController',['$scope','productService','utils','customerControllerService','$window', function($scope, productService,utils,customerControllerService,$window){


    $scope.fetchCustomers = fetchAllCustomers;
    $scope.getClickedCustomer = getClickUser;

    fetchAllProducts();
    $scope.cart = [];
    $scope.qty = 1;
    $scope.total = 0;
    $scope.currentDate = new Date();
    var products = [];
   var produit;

   $scope.orderDto = {
       id:null,customerId:'',deleted:'',totalAmount:'',orderedProducts:''
   }


    var findItemById = function(items, id) {
        return _.find(items, function(item) {
            return item.product.id === id;
        });
    };

    $scope.initData = function (){
        $scope.loginUser = JSON.parse($window.localStorage.getItem('loginUser'));
        $scope.clickedCustomer = JSON.parse($window.localStorage.getItem('saved'));
        $scope.cart = JSON.parse($window.localStorage.getItem('cart'));
        $scope.cartTotal = JSON.parse($window.localStorage.getItem('cartTotal'));
        console.log('cart'+$scope.cart)
        console.log('clicked user'+$scope.cartTotal)
        fetchAllCustomers();
    }
    $scope.getCost = function(item) {
        return item.qty * item.product.price;
    };

    $scope.addItem = function(itemToAdd) {
        var found = findItemById($scope.cart, itemToAdd.product.id);
        if (found) {
            found.qty += itemToAdd.qty;
        }
        else {
            $scope.cart.push(angular.copy(itemToAdd));}
        localStorage.setItem("cart", JSON.stringify($scope.cart));
        localStorage.setItem("cartTotal", $scope.getTotal());
        console.log($scope.cart)
    };

    $scope.getTotal = function() {
        var total =  _.reduce($scope.cart, function(sum, item) {
            return sum + $scope.getCost(item);
        }, 0);
        console.log('total: ' + total);
        return total;
    };

    $scope.clearCart = function() {
        $scope.cart.length = 0;
    };

    $scope.removeItem = function(item) {
        var index = $scope.cart.indexOf(item);
        $scope.cart.splice(index, 1);
        localStorage.setItem("cart", JSON.stringify($scope.cart));
        localStorage.setItem("cartTotal", $scope.getTotal());
        console.log($scope.cart)
    };


    function fetchAllProducts() {
        productService.fetchAllProducts()
            .then(
                function (response) {
                    utils.destroyDatatable('product_table');
                    angular.forEach(response.content, function (product,k){

                        productService.getProductImage(product.id).then(
                            function (response){
                                // console.log('product image'+response)
                                var uint8Array = new Uint8Array(response);
                                var binaryString = String.fromCharCode.apply(null, uint8Array);
                                var base64String = btoa(binaryString);
                                $scope.productimage = 'data:image/JPEG;base64,' + base64String;
                                produit = {product:product,qty:1};
                                products.push(produit);
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

    function fetchAllCustomers() {
        customerControllerService.fetchAllCustomers()
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');
                    $scope.allCustomers=d.content;
                    angular.forEach(d.content,function (customer,k){
                        $scope.customer = customer;
                    })
                    // utils.loadDatatable('user_table');
                    // console.log(d)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('Error while fetching Users');
                }
            );
    }

    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        // console.log('$scope.clickedUser',user);
    }

}]);