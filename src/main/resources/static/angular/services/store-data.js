myApp.service('storeData',function (){
    let cache;
    this.saveData = function (data){
        cache = data;
    }

    this.retrieveData = function(){
        return cache;
    };
})