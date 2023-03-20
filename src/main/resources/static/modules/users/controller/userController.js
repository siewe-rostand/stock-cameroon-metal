myApp.controller('userController', ['$scope', 'UserService', 'utils', function ($scope, UserService, utils) {

    let self = this;
    $scope.user = {
        id: null, firstname: '', lastname: '', phone: '',password:'',
        phone2: '', city: '', quarter: '', email: '',
    };
    self.users = [];

    $scope.submit = submit;
    $scope.editUser = edit;
    $scope.remove = deleteUser;
    $scope.reset = reset;
    $scope.getClickUser = getClickUser;


    fetchAllUsers();

     $scope.initView=function (){
         let clickedUser = JSON.parse(localStorage.getItem('saved'));
         $scope.clickedUser = clickedUser;
         $scope.user = clickedUser;
         console.log($scope.clickedUser)
    }
    function fetchAllUsers() {
        UserService.fetchAllUsers('/all-users')
            .then(
                function (d) {
                    // utils.destroyDatatable('user_table');
                    $scope.allUsers=d.content;
                    // utils.loadDatatable('user_table');
                    // console.log(d)
                },
                function (errResponse) {
                    console.log(errResponse)
                    console.error('Error while fetching Users');
                }
            );
    }


    function createUser(user) {
        let url = '/register'
        UserService.createUser(url, user)
            .then(
                function (res){
                    console.log("save new user response",res);
                    fetchAllUsers();
                    reset();
                },
                function (errResponse) {
                    console.error(errResponse)
                    console.error('Error while creating User');
                }
            );
    }

    function updateUser(user) {
        UserService.updateUser('/users',user)
            .then(
                function(res){
                    Toast.fire({
                        icon:'success',
                        title: 'utilisateur modifié avec success'
                    })
                    fetchAllUsers();
                },
                function (errResponse) {
                    Toast.fire({
                        icon:'error',
                        title: 'une erreur s\'est produite'
                    })
                    console.log(errResponse);
                    console.error('Error while updating User');
                }
            );
    }

    function deleteUser(user) {
        
        swalWithBootstrapButtons.fire({
            title: 'Êtes-vous sûr?',
            html: " de vouloir supprimer"+" <strong>"+ user.fullname + "</strong> " + "?cette action est irréversible",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Oui, Supprimer!',
            cancelButtonText: 'Non, annuler!',
            reverseButtons:true,
            showLoaderOnConfirm: true,
            allowOutsideClick: () => !Swal.isLoading(),
            preConfirm: (user) => {     
                return UserService.deleteUser('/users/',user.id)
              .then(
                  function (res){
                    swalWithBootstrapButtons.fire("Supprimmer!", "utilisateur supprimé avec succès.", "success");
                      console.log(res);
                      fetchAllUsers();
                  },
                  function (errResponse) {
                    swalWithBootstrapButtons.fire("OUPS!", "une erreur s'est produite lors de la suppression de cet utilisateur si le problème persiste, veuillez appeler la cellule informatique ", "error");
                      console.error('Error while deleting User',errResponse);
                  }
              );
            },
          }).then((result) => {
            if (result.dismiss === Swal.DismissReason.cancel) {
                swalWithBootstrapButtons.fire("Annuler", "L'utilisateur ne sera pas supprimer!!", "error");
            }
          })
    }

    function submit() {
        console.log($scope.user)
        if ($scope.user.id === null) {
            console.log('Saving New User', $scope.user);
            createUser($scope.user);
        } else {
            updateUser();
            console.log('User updated with id ', $scope.user.id);
        }
        // reset();
    }

    function edit(id) {
        console.log('id to be edited', $scope.user);

    }


    function reset() {
        $scope.user = {
            id: null, firstname: '', lastname: '', phone: '',password:'',
            phone2: '', city: '', quarter: '', email: '',
        };
        $scope.myForm.$setPristine(); //reset Form
    }

    function getClickUser(user){
        localStorage.setItem("saved", JSON.stringify(user));
        console.log('$scope.clickedUser',user);
    }


}])