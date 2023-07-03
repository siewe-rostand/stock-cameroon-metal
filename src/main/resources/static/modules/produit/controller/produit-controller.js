

myApp.controller('produitController',['$scope','produitService','utils','$rootScope','$window', function($scope, produitService, utils,$rootScope,$window){


    $scope.submit = submit;
    $scope.remove = deleteProduct;
    $scope.getClickProduct = getClickProduct;

    var products = [];
    $scope.product = {
        id: null, metrage: '', name : '',
        ref: '', color: '',epaiseur:null
    };

    fetchAllProduits();

    $rootScope.$on('fetchProducts',function (){
        $scope.fetchProducts = fetchAllProduits;
    })

    $scope.initView=function (){
        let clickedProduct = JSON.parse(localStorage.getItem('saved'));
        $scope.clickedProduct = clickedProduct;
        $scope.product = clickedProduct;
        console.log($scope.clickedProduct)
    }
    function fetchAllProduits() {
        produitService.fetchAllProducts()
            .then(
                function (response) {
                    utils.destroyDatatable('product_table');
                    $scope.products = response.content;
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
        produitService.createProduct(product)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'produit enregistré avec success'
                    })
                    console.log("save new product response",res);
                    $window.history.back();
                    fetchAllProduits();
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
        produitService.updateProduct(product)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'produit enregistré avec success'
                    })
                    console.log("save new product response",res);
                    fetchAllProduits();
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
                return produitService.deleteProduct(product.id)
                    .then(
                        function (res){
                            swalWithBootstrapButtons.fire("Supprimmer!", product.name + " supprimé avec succès.", "success");
                            console.log(res);
                            fetchAllProduits();
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