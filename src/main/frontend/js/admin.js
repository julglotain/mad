angular
    .module('mad.administration', ['ui.bootstrap', 'mad.services', 'mad.directives'])
    .service('UploadService', ['$http', function ($http) {

        function createFormDataObject(model) {
            var fd = new FormData();
            angular.forEach(model, function (v, k) {
                fd.append(k, v);
            });
            return fd;
        }

        this.send = function (formData, url) {

            $http
                .post(url, createFormDataObject(formData), {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function () {
                    console.log('ajout app ok');
                })
                .error(function () {
                    console.log('ajout app ko');
                });

        };

    }])
    .controller('AddNewAppFormCtrl', ['$scope', 'UploadService' , function ($scope, UploadService) {

        $scope.formData = {};

        $scope.submit = function () {

            UploadService.send($scope.formData, './admin/upload');
        };

    }])
    .controller('ManageBundlesListCtrl', ['$scope', '$window', 'BundlesListService', function ($scope, $window, BundlesListService) {

        $scope.bundles = [];

        BundlesListService
            .fetch()
            .success(function (result) {
                $scope.bundles = result.bundles;
            });

        $scope.removeBundle = function (bundle) {
            console.log('About to remove bundle: ' + bundle.identifier);
        };

        $scope.removeAppVersion = function (bundle, version) {
            console.log('About to remove app: ' + bundle.identifier + ' - ' + version.number);
        };

    }])
    .controller('BundleListCompletionCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.getBundles = function (val) {
            return $http.get('./admin/bundle', {
                params: {
                    identifier: val
                }
            }).then(function (response) {
                return response.data.items;
            });
        };

        $scope.onSelect = function ($item, $model, $label) {
            $scope.formData.bundle = $item.identifier;
            $scope.formData.profile = $item.profile;
        };

    }]);