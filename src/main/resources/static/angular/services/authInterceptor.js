myApp.factory('TokenInterceptor', function($q, $window,$location,$rootScope) {
    return {
        request: function(config) {
            config.headers = config.headers || {};
            if ($window.localStorage.getItem('token')) {
                config.headers.Authorization = 'Bearer ' + $window.localStorage.getItem('token');
                // $location.path('/home');
                $rootScope.authenticated =true;
            }
            return config || $q.when(config);
        },
        response: function(response) {
            if (response.status === 401 || response.status === 403) {
                $location.path('/');
            }
            return response || $q.when(response);
        }
    };
});
myApp.config(function($httpProvider) {
    $httpProvider.interceptors.push('TokenInterceptor');
});

