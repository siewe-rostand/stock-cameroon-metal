
myApp.factory('utils',function ($http){
    let obj={};
    const base_url ='http://localhost:8080';

    obj.getBaseUrl = function (){
        console.log('base url')
        return base_url;
    }


    return obj;
})