angular
    .module('mad.administration', ['ui.bootstrap', 'mad.services', 'mad.directives', 'mad.partials', 'mad.config', 'ngAnimate', 'cgBusy'])

    .controller('AddNewAppFormCtrl', ['$scope', '$rootScope', 'UploadService', '$timeout', function ($scope, $rootScope, UploadService, $timeout) {

        $scope.formData = {};

        $scope.uploadLabelMessageType = "";
        $scope.uploadLabelMessage = "";
        $scope.showUploadLabelMessage = false;

        $scope.message = 'Uploading app, please wait...';
        $scope.promise = null;

        function ok(msg) {
            $scope.uploadLabelMessageType = "alert-success";
            $scope.uploadLabelMessage = msg;
            $scope.showUploadLabelMessage = true;
        }

        function ko(msg) {
            $scope.uploadLabelMessageType = "alert-danger";
            $scope.uploadLabelMessage = msg;
            $scope.showUploadLabelMessage = true;
        }

        function clear() {
            $scope.uploadLabelMessageType = "";
            $scope.uploadLabelMessage = "";
            $scope.showUploadLabelMessage = false;
        }

        $scope.submit = function () {

            $scope.promise = UploadService.send($scope.formData, './admin/upload');

            $scope.promise
                .success(function (response) {

                    $scope.formData = {};

                    ok(response.message);
                    $timeout(clear, 6000);

                    // let's others be aware of the happy event
                    $rootScope.$broadcast('admin:app-added');

                })
                .error(function (response) {
                    ko(response.message);
                    $timeout(clear, 10000);
                });

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
    .controller('ManageBundlesCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http', '$log', function ($scope, $rootScope, BundlesListService, $http, $log) {

        $scope.bundles = [];
        $scope.bundleLoaded = false;
        $scope.formData = {};

        $scope.message = 'Please Wait...';
        $scope.promise = null;

        function fetch() {

            $scope.promise = BundlesListService.fetch();

            $scope.promise
                .success(function (result) {
                    $scope.bundleLoaded = true;
                    $scope.bundles = result.bundles;
                });

        }

        // immediatelly fetch bundles list
        fetch();

        // when new app is added we want to refresh the bundles and apps list
        $rootScope.$on('admin:app-added', fetch);
        $rootScope.$on('admin:app-removed', fetch);

        // when a bundle is added or removed we want to refresh
        $rootScope.$on('admin:bundle-added', fetch);
        $rootScope.$on('admin:bundle-remove', fetch);

        $scope.createBundle = function (bundle) {

            $log.info('About to create a bew bundle: ' + $scope.formData.identifier + ', ' + $scope.formData.profile);

            return $http
                .post('./admin/bundle', $.param($scope.formData))
                .success(function () {

                    $scope.formData = {};
                    $rootScope.$broadcast('admin:bundle-added');
                    fetch();

                });
        };

        $scope.removeBundle = function (bundle) {
            console.log('About to remove bundle: ' + bundle.identifier);

            $http
                .delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile, {
                }).success(function () {

                    $rootScope.$broadcast('admin:bundle-added');
                    fetch();

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
