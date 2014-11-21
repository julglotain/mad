angular
    .module('mad.administration', ['ui.bootstrap', 'mad.services', 'mad.directives', 'mad.partials', 'mad.config', 'mad.providers', 'ngAnimate', 'cgBusy'])

    .controller('AddNewAppFormCtrl', ['$scope', '$rootScope', 'UploadService', '$timeout',
        function ($scope, $rootScope, UploadService, $timeout) {

            $scope.formData = {};

            $scope.uploadResultMessage = "";
            $scope.uploadResultClass = "";

            $scope.message = 'Uploading app, please wait...';
            $scope.promise = null;

            function ok(msg) {
                $scope.uploadResultMessage = msg;
                $scope.uploadResultClass = "alert-success";
            }

            function ko(msg) {
                $scope.uploadResultMessage = msg;
                $scope.uploadResultClass = "alert-danger";
            }

            function clear(delay) {

                function reset() {
                    $scope.uploadResultMessage = "";
                    $scope.uploadResultClass = "";
                }

                if (delay) {
                    $timeout(reset, delay);
                } else {
                    reset();
                }
            }

            $scope.submit = function () {

                $scope.promise = UploadService.send($scope.formData, './admin/upload');

                $scope.promise
                    .success(function (response) {

                        $scope.formData = {};

                        ok(response.message);
                        clear(6000);

                        // let's others be aware of the happy event
                        $rootScope.$broadcast('admin:app-added');

                    })
                    .error(function (response) {

                        ko(response.message);
                        clear(10000);

                    });

            };

        }])

    .controller('ManageAppsCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http',
        function ($scope, $rootScope, BundlesListService, $http) {

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

    .controller('ManageBundlesCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http', '$log', '$modal', '$interpolate',
        function ($scope, $rootScope, BundlesListService, $http, $log, $modal, $interpolate) {

            $scope.bundles = [];
            $scope.bundleLoaded = false;
            $scope.formData = {};

            $scope.message = 'Please Wait...';
            $scope.promise = null;

            var confirmRemoveBundleExp = $interpolate('That you really want to remove the bundle <b>{{bundleIdentifier}}</b> ?');

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

                $modal.open({
                    templateUrl: '/partials/confirmation-modal.tpl.html',
                    controller: 'ConfirmationModalCtrl',
                    size: 'lg',
                    resolve: {
                        message: function () {
                            return confirmRemoveBundleExp({bundleIdentifier: bundle.identifier});
                        }
                    }
                }).result
                    .then(function () {
                        $http
                            .delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile, {
                            }).success(function () {
                                $rootScope.$broadcast('admin:bundle-added');
                                fetch();
                            });
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

    }])
    .controller('ConfirmationModalCtrl', function ($scope, $modalInstance, message) {

        $scope.confirmationMessage = message;

        console.log(message);

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });
