myApp.controller('homeController',function ($scope,$http){
    getUsers();


   function getUsers (){
       let data = $scope.users;
       let url = 'https://localhost:8080/api/users';
       $http.post(url, data).success(function (response) {
           // Loader.destroy();
           console.log("success   ", response.data);
           // utilitaire.alert(true,"les données de l'utilisateur ont été enregistrer avec succès");
           $scope.users={};

       }).error(function (data, status, header) {
           // Loader.destroy();
           console.error(data);
           // utilitaire.gestionErreurs(status, data, header);
       });
   }
})