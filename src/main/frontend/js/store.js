angular
    .module('mad.store', ['mad.services'])
    .controller('AppsListCtrl', ['$scope', '$window', 'BundlesListService', function ($scope, $window, BundlesListService) {

        BundlesListService
            .fetch()
            .success(function (result) {
                $scope.bundles = result.bundles;
            });

        $scope.downloadManifest = function (manifestUrl) {

            var isIOs = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );

            $window.location = ((isIOs) ? 'itms-services://?action=download-manifest&url=' : '') + manifestUrl;
        };

    }]);