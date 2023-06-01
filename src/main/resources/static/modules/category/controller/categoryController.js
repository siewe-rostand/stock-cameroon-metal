

myApp.controller('categoryController',['$scope','categoryService','utils', function($scope, categoryService,utils){


    $scope.submit = submit;
    $scope.remove = deleteCategory();
    $scope.getClickedCategory = getClickedCategory();

    $scope.category = {
        id: null, name: '', enabled : ''
    };

    fetchAllCategory();

    $scope.initView=function (){
        let clickedCategory = JSON.parse(localStorage.getItem('saved'));
        $scope.clickedCategory = clickedCategory;
        $scope.category = clickedCategory;
        console.log($scope.clickedCategory)
    }
    function fetchAllCategory() {
        categoryService.fetchAllCategory()
            .then(
                function (response) {
                    utils.destroyDatatable('category_table');
                    $scope.categories = response.content;
                    utils.loadDatatable('category_table');
                    console.log(response)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('controller:Error while fetching categories');
                }
            );
    }

    function createCategory(category){
        categoryService.createCategory(category)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'category sauvegardé avec success'
                    })
                    console.log("save new category response",res);
                    fetchAllCategory();
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


    function updateCategory(category) {
        categoryService.updateCategory(category)
            .then(
                function (res){
                    Toast.fire({
                        icon:'success',
                        title: 'category esauvegardé avec success'
                    })
                    console.log("save new category response",res);
                    fetchAllCategory();
                },
                function (errResponse) {
                    console.log(errResponse);
                    console.log("====",$scope.category);
                    console.error('Error while updating category');
                }
            );
    }

    function deleteCategory(category) {
        swalWithBootstrapButtons.fire({
            title: 'Êtes-vous sûr?',
            html: " de vouloir supprimer"+" <strong>"+ category.name + "</strong> " + "? cette action est irréversible",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Oui, Supprimer!',
            cancelButtonText: 'Non, annuler!',
            reverseButtons:true,
            showLoaderOnConfirm: true,
            allowOutsideClick: () => !Swal.isLoading(),
            preConfirm: () => {
                return categoryService.deleteCategory(category.id)
                    .then(
                        function (res){
                            swalWithBootstrapButtons.fire("Supprimmer!", category.name + " supprimé avec succès.", "success");
                            console.log(res);
                            fetchAllCategory();
                        },
                        function (errResponse) {
                            swalWithBootstrapButtons.fire("OUPS!", "une erreur s'est produite lors de la suppression de cet category si le problème persiste, veuillez appeler la cellule informatique ", "error");
                            console.error('Error while deleting category',errResponse);
                        }
                    );
            },
        }).then((result) => {
            if (result.dismiss === Swal.DismissReason.cancel) {
                swalWithBootstrapButtons.fire("Annuler", "vous ne pouvez pas supprimmer cette categorie désolé!!", "error");
            }
        })

    }

    function submit() {
//            console.log($scope.product)
        if ($scope.category.id === null) {
            console.log('Saving New category', $scope.category);
            createCategory($scope.category);
        } else {
            updateCategory($scope.category);
            console.log('category updated with id ', $scope.category.id);
        }
        // reset();
    }

    function getClickedCategory(category){
        localStorage.setItem("saved", JSON.stringify(category));
        console.log('$scope.clicked category',category);
    }

}]);