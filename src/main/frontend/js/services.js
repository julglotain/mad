angular
    .module('mad.services',[])
    .factory('BundlesListService',['$http', function ($http) {
        return {
            fetch: function () {
                return $http.get('./store/data');
            }
        };
    }]);