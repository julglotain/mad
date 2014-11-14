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

        this.send = function (formData, url, success, failure) {

            $http
                .post(url, createFormDataObject(formData), {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(success)
                .error(failure);

        };

    }])
    .controller('AddNewAppFormCtrl', ['$scope', '$rootScope', 'UploadService', '$timeout', function ($scope, $rootScope, UploadService, $timeout) {

        $scope.formData = {};

        $scope.uploadLabelMessageType = "";
        $scope.uploadLabelMessage = "";
        $scope.showUploadLabelMessage = false;

        function ok(msg) {
            $scope.uploadLabelMessageType = "label-success";
            $scope.uploadLabelMessage = msg;
            $scope.showUploadLabelMessage = true;
        }

        function ko(msg) {
            $scope.uploadLabelMessageType = "label-danger";
            $scope.uploadLabelMessage = msg;
            $scope.showUploadLabelMessage = true;
        }

        function clear() {
            $scope.uploadLabelMessageType = "";
            $scope.uploadLabelMessage = "";
            $scope.showUploadLabelMessage = false;
        }

        $scope.submit = function () {

            UploadService
                .send($scope.formData, './admin/upload',
                function (response) {

                    $scope.formData = {};
                    ok(response.message);
                    $timeout(clear, 3000);

                    // let's others be aware of the happy event
                    $rootScope.$broadcast('admin:app-added');

                },
                function (response) {
                    ko(response.message);
                    $timeout(clear, 10000);
                }
            );
        };

    }])
    .controller('ManageAppsCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http', function ($scope, $rootScope, BundlesListService, $http) {

        $scope.bundles = [];

        // fetch bundles list
        function fetch() {
            BundlesListService
                .fetch()
                .success(function (result) {
                    $scope.bundles = result.bundles;
                });
        }

        // when new app is added we want to refresh the bundles and apps list
        $rootScope.$on('admin:app-added', fetch);

        // when a bundle is added or removed we want to refresh
        $rootScope.$on('admin:bundle-added', fetch);
        $rootScope.$on('admin:bundle-remove', fetch);

        // immediatelly fetch bundles list
        fetch();

        // remove an app version
        $scope.removeAppVersion = function (bundle, version) {
            console.log('About to remove app: ' + bundle.identifier + ' - ' + version.number);

            $http
                .delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile + '/version/' + version.number)
                .success(function () {
                    fetch();
                    $rootScope.$broadcast('admin:app-removed');
                });

        };

        // remove an a bundle
        $scope.removeBundle = function (bundle) {
            console.log('About to remove bundle: ' + bundle.identifier);

            $http
                .delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile)
                .success(function () {
                    fetch();
                    $rootScope.$broadcast('admin:bundle-removed');
                });
        };


    }])
    .controller('ManageBundlesCtrl', ['$scope', 'BundlesListService', '$http', function ($scope, BundlesListService, $http) {

        $scope.bundles = [];

        function fetch() {
            BundlesListService
                .fetch()
                .success(function (result) {
                    $scope.bundles = result.bundles;
                });
        }

        $scope.removeBundle = function (bundle) {
            console.log('About to remove bundle: ' + bundle.identifier);

            $http
                .delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile, {
                }).success(function () {
                    console.log(arguments);
                });
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

        $scope.onSelect = function ($item) {
            $scope.formData.bundle = $item.identifier;
            $scope.formData.profile = $item.profile;
        };

    }]);
