angular
    .module('mad.store', ['mad.services','mad.filters', 'mad.partials', 'cgBusy'])
    .controller('AppsListCtrl', ['$scope', '$window', 'BundlesListService', function ($scope, $window, BundlesListService) {

        $scope.bundles = [];
        $scope.loaded = false;
        $scope.numberTotalOfApps = 0;

        $scope.promise = null;
        $scope.loadingTemplateUrl = '/partials/loading.tpl.html';


        $scope.fetch = function () {

            $scope.promise = BundlesListService.fetch();

            $scope.promise.success(function (result) {
                $scope.loaded = true;
                $scope.bundles = result.bundles;
                $scope.numberTotalOfApps = $scope.bundles.map(function(b){return b.versions.length;}).reduce(function(prev, curr){return prev + curr;});
            });

        };

        // immediatelly fetch app bundles
        $scope.fetch();

        $scope.downloadManifest = function (manifestUrl) {

            var isIOs = ( navigator.userAgent.match(/(iPad|iPhone|iPod)/g) ? true : false );

            $window.location = ((isIOs) ? 'itms-services://?action=download-manifest&url=' : '') + manifestUrl;
        };

    }]);