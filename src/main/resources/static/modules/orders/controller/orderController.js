
myApp.controller('orderController',['$scope','produitService','orderService','utils','customerControllerService','$window', function($scope, produitService,orderService,utils,customerControllerService,$window){


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
       id:null,customerId:'',deleted:'',products:''
   }


    var findItemById = function(items, id) {
        return _.find(items, function(item) {
            return item.produitId === id;
        });
    };

    $scope.initData = function (){
        $scope.loginUser = JSON.parse($window.localStorage.getItem('loginUser'));
        $scope.clickedCustomer = JSON.parse($window.localStorage.getItem('saved'));
        $scope.cart = JSON.parse($window.localStorage.getItem('cart'));
        $scope.cartTotal = JSON.parse($window.localStorage.getItem('cartTotal'));
        // console.log('cart'+$scope.cart)
        angular.forEach($scope.cart,function (cart,k){
            console.log(cart)
        })
        // console.log('clicked user'+$scope.cartTotal)
        $scope.order = {
            "customerId":$scope.clickedCustomer.id,
            "products":$scope.cart
        }
        console.log($scope.order)
        fetchAllCustomers();
    }

    $scope.addItem = function(itemToAdd) {
        var found = findItemById($scope.cart, itemToAdd.produitId);
        if (found) {
            found.metrage += itemToAdd.metrage;
        }
        else {
            $scope.cart.push(angular.copy(itemToAdd));}
        console.log($scope.cart)
        localStorage.setItem("cart", JSON.stringify($scope.cart));
    };

    $scope.clearCart = function() {
        $scope.cart.length = 0;
    };

    $scope.removeItem = function(item) {
        var index = $scope.cart.indexOf(item);
        $scope.cart.splice(index, 1);
        localStorage.setItem("cart", JSON.stringify($scope.cart));
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
        produitService.fetchAllProducts()
            .then(
                function (response) {
                    angular.forEach(response.content,function (produit,k){
                        produit = {
                            produitId:produit.id,
                            stock:produit.metrage,
                            name:produit.name,
                            productRef:produit.ref,
                            metrage:1};
                        products.push(produit);
                        $scope.produits = products;
                    })
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
                    $scope.allCustomers=d.content;
                    console.log()
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