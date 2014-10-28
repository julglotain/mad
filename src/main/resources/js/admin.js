angular
    .module('adminApp',[])
    .factory('AppsListService', function ($http) {
        return {
            fetchBundlesList: function () {
                return $http.get('./download.json')
            }
        }
    })
    .controller('AppsListCtrl', function ($scope, $window, AppsListService) {

        AppsListService.fetchBundlesList().success(function (result) {
            $scope.bundles = result.bundles;
        });

    });