angular
    .module('downloadableAppsListApp', [])
    .factory('AppsListService', function ($http) {
        return {
            fetchBundlesList: function () {
                return $http.get('./store/data');
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

    })
    .directive('bundleIdentifierLabel',function(){
        return {
            restrict: 'E',
            scope: {
                bundle: '=bundle',
                suffix: '=suffix'
            },
            template: function(){

                return '<span class="label label-warning pull-right">{{ bundle }}{{ suffix | prependIfNotNull:"." }}</span>';

            }
        };
    })
    .filter('prependIfNotNull',function(){

        return function(input, valueToPrepend){
            return (input) ? valueToPrepend + input : '';
        }

    });