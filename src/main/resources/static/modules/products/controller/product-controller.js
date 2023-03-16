
myApp.controller('productController',['$scope','productService', function($scope, productService){


        $scope.submit = submit;
        $scope.remove = deleteProduct;
        $scope.getClickProduct = getClickProduct;

        $scope.product = {
                id: null, name: '', city: '', quarter: '',price : '',
                categoryName: '', stock: '', description: '',
            };

    fetchAllProducts();

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
                    // utils.destroyDatatable('user_table');
                    $scope.products = response.content;
                    // utils.loadDatatable('user_table');
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