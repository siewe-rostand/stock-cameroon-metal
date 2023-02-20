myApp.controller('homeController',function ($scope,$http,config,toaster){
    update();


   function getUsers (){
       let data = $scope.users;
       let url = 'http://localhost:8080/api/users?roles=USER';
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

    function update() {
        let data = $scope.users;
        console.log(config)
        let url = config.base_url+'/users?roles=user';
        $http({
            url: url,
            method: 'GET',
            contentType: 'application/json; charset=utf-8',
            // data: data
        }).then(function (result) { // this is the success
            console.log("update result",result.data.content);
            $scope.allUsers =result.data.content;
        }, function (data, status, header) {  // this is the error
            console.error(data);
        });
    }
})