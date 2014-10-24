angular
    .module('downloadableAppsListApp', [])
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

        $scope.downloadManifest = function(manifestUrl){

            var isIOs = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );

            $window.location = ((isIOs) ? 'itms-services://?action=download-manifest&url=' : '') + manifestUrl;
        }

    });