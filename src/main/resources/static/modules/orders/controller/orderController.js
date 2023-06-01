
myApp.controller('orderController',['$scope','productService','orderService','utils','customerControllerService','$window', function($scope, productService,orderService,utils,customerControllerService,$window){


    $scope.fetchCustomers = fetchAllCustomers;
    $scope.getClickedCustomer = getClickUser;
    $scope.createOrder = createOrder;

    fetchAllProducts();
    $scope.cart = [];

    $scope.quantity = 1;
    $scope.total = 0;
    $scope.currentDate = new Date();
    var products = [];
   var produit;
   $scope.order ={};

   $scope.orderDto = {
       id:null,customerId:'',deleted:'',totalAmount:'',orderedProducts:''
   }


    var findItemById = function(items, id) {
        return _.find(items, function(item) {
            return item.productId === id;
        });
    };

    $scope.initData = function (){
        $scope.loginUser = JSON.parse($window.localStorage.getItem('loginUser'));
        $scope.clickedCustomer = JSON.parse($window.localStorage.getItem('saved'));
        $scope.cart = JSON.parse($window.localStorage.getItem('cart'));
        $scope.cartTotal = JSON.parse($window.localStorage.getItem('cartTotal'));
        console.log('cart'+$scope.cart)
        console.log('clicked user'+$scope.cartTotal)
        $scope.order = {
            "customerId":$scope.clickedCustomer.id,
            "prixTotal":$scope.cartTotal,
            "orderedProducts":$scope.cart
        }
        fetchAllCustomers();
    }
    $scope.getCost = function(item) {
        return item.quantity * item.price;
    };

    $scope.addItem = function(itemToAdd) {
        var found = findItemById($scope.cart, itemToAdd.productId);
        if (found) {
            found.quantity += itemToAdd.quantity;
        }
        else {
            itemToAdd.prixVente = itemToAdd.price;
            $scope.cart.push(angular.copy(itemToAdd));}
        localStorage.setItem("cart", JSON.stringify($scope.cart));
        localStorage.setItem("cartTotal", $scope.getTotal());
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

    function createOrder(){
        orderService.createOrder($scope.order)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'Commande enregistré avec success'
                    })
                    console.log("save new order response",res);
                    // $window.history.back();
                },
                function (errResponse) {
                    let error = errResponse.data.errorMessage;
                    let  error2 = errResponse.data.message;
                    console.error(errResponse.data.message)
                    if(error2.includes('insuffisant')){
                        // $scope.errorMessage = '===='
                        console.log('==============')
                    }
                    Toast.fire({
                        icon:'error',
                        title: 'une erreur s\'est produite'
                    })
                    console.error(errResponse.message)
                    console.error('Error while creating order');
                }
            );
    }

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
                                // produit = {product:product,qty:1};
                                produit = {
                                    productId:product.id,
                                    stock:product.stock,
                                    price:product.price,
                                    name:product.name,
                                    quantity:1};
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