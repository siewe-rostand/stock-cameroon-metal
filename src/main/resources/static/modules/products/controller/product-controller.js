
myApp.controller('productController',['$scope','productService','utils','$rootScope','$window', function($scope, productService, utils,$rootScope,$window){


        $scope.submit = submit;
        $scope.remove = deleteProduct;
        $scope.getClickProduct = getClickProduct;
        $scope.getProductImage = getProductImage;

        var products = [];
        $scope.product = {
                id: null, name: '', price : '',
                 quantity: '', description: '',categoryId:null,categoryName:''
            };
            $scope.category = {
                id:$scope.product.id,name:''
            }

    fetchAllProducts();

    $rootScope.$on('fetchProducts',function (){
        $scope.fetchProducts = fetchAllProducts;
    })

     $scope.initView=function (){
             let clickedProduct = JSON.parse(localStorage.getItem('saved'));
             $scope.clickedProduct = clickedProduct;
             $scope.product = clickedProduct;
             console.log($scope.clickedProduct)
     }
    function fetchAllProducts() {
        productService.fetchAllProducts()
            .then(
                function (response) {
                    utils.destroyDatatable('product_table');
                    angular.forEach(response.content, function (product,k){
                        console.log(product.id + ' ' +k)
                        productService.getProductImage(product.id).then(
                            function (response){
                                console.log('product image'+response.data)
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

    function createProducts(product){
            productService.createProduct(product)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'produit enregistré avec success'
                    })
                    console.log("save new product response",res);
                    $window.history.back();
                    fetchAllProducts();
                },
                function (errResponse) {
                    Toast.fire({
                        icon:'error',
                        title: 'une erreur s\'est produite'
                    })
                    console.error(errResponse)
                    console.error('Error while creating User');
                }
            );
        }


        function updateProduct(product) {
            productService.updateProduct(product)
                .then(
                function (res){
                                    Toast.fire({
                                        icon:'success',
                                        title: 'produit enregistré avec success'
                                    })
                                    console.log("save new product response",res);
                                    fetchAllProducts();
                                },
                    function (errResponse) {
                        console.log(errResponse);
                        console.log("====",$scope.product);
                        console.error('Error while updating product');
                    }
                );
        }

        function deleteProduct(product) {
            swalWithBootstrapButtons.fire({
                title: 'Êtes-vous sûr?',
                html: " de vouloir supprimer"+" <strong>"+ product.name + "</strong> " + "?cette action est irréversible",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Oui, Supprimer!',
                cancelButtonText: 'Non, annuler!',
                reverseButtons:true,
                showLoaderOnConfirm: true,
                allowOutsideClick: () => !Swal.isLoading(),
                preConfirm: () => {
                    return productService.deleteProduct(product.id)
                    .then(
                        function (res){
                            swalWithBootstrapButtons.fire("Supprimmer!", product.name + " supprimé avec succès.", "success");
                              console.log(res);
                            fetchAllProducts();
                        },
                        function (errResponse) {
                            swalWithBootstrapButtons.fire("OUPS!", "une erreur s'est produite lors de la suppression de cet produit si le problème persiste, veuillez appeler la cellule informatique ", "error");
                              console.error('Error while deleting product',errResponse);
                        }
                    );
                },
              }).then((result) => {
                if (result.dismiss === Swal.DismissReason.cancel) {
                    swalWithBootstrapButtons.fire("Annuler", "Le produit ne sera pas supprimer!!", "error");
                }
              })

        }

        function submit() {
//            console.log($scope.product)
            if ($scope.product.id === null) {
                console.log('Saving New product', $scope.product);
                createProducts($scope.product);
            } else {
                updateProduct($scope.product);
                console.log('product updated with id ', $scope.product.id);
            }
            // reset();
        }

        function getClickProduct(product){
                localStorage.setItem("saved", JSON.stringify(product));
                console.log('$scope.clickedUser',product);
            }

}]);