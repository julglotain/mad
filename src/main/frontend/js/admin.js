angular
    .module('mad.administration', ['ui.bootstrap', 'mad.services', 'mad.directives', 'mad.partials', 'mad.config', 'mad.providers', 'ngAnimate', 'cgBusy', 'mad.dependencies'])

    .controller('AddNewAppFormCtrl', ['$scope', '$rootScope', 'UploadService', '$timeout',
        function ($scope, $rootScope, UploadService, $timeout) {

            $scope.formData = {};

            $scope.uploadResultMessage = "";
            $scope.uploadResultClass = "";

            $scope.message = 'Uploading app, please wait...';
            $scope.promise = null;

            function flashOK(msg) {
                $scope.uploadResultMessage = msg;
                $scope.uploadResultClass = "alert-success";
                clear(5000);
            }

            function flashKO(msg) {
                $scope.uploadResultMessage = msg;
                $scope.uploadResultClass = "alert-danger";
                clear(80000);
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

                        flashOK(response.message);

                        // let's others be aware of the happy event
                        $rootScope.$broadcast('admin:app-added');

                    })
                    .error(function (response) {

                        flashKO(response.message);

                    });

            };

        }])

    .controller('ManageAppsCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http', '_',
        function ($scope, $rootScope, BundlesListService, $http, _) {

            // bundles list holder
            $scope.bundles = [];

            $scope.message = 'Please wait...';
            $scope.promise = null;

            // when new app is added we want to refresh the bundles and apps list
            $rootScope.$on('admin:app-added', function(){
                $scope.fetchBundlesList();
            });

            // when a bundle is added or removed we want to refresh
            $rootScope.$on('admin:bundle-created', function (event, newBundle) {
                $scope.bundles.push(newBundle);
            });

            $rootScope.$on('admin:bundle-removed', function (event, bundleRemoved) {
                $scope.bundles = _.reject($scope.bundles, function (b) {
                    return b.identifier === bundleRemoved.identifier && b.profile === bundleRemoved.profile;
                });
            });

            // fetch bundles list
            $scope.fetchBundlesList = function () {
                $scope.promise = BundlesListService.fetch();

                $scope.promise
                    .success(function (result) {
                        $scope.bundles = result.bundles;
                    });
            };

            // remove an app version
            $scope.removeAppVersion = function (bundle, version) {
                console.log('About to remove app: ' + bundle.identifier + ' - ' + version.number);

                $scope.promise = $http.delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile + '/version/' + version.number);

                $scope.promise
                    .success(function () {

                        $scope.fetchBundlesList();

                        $rootScope.$broadcast('admin:app-removed');

                    });

            };

            // remove an a bundle
            $scope.removeBundle = function (bundle) {
                console.log('About to remove bundle: ' + bundle.identifier);

                $scope.promise = $http.delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile);

                $scope.promise
                    .success(function () {

                        $rootScope.$broadcast('admin:bundle-removed', bundle);
                    });
            };

        }])

    .controller('ManageBundlesCtrl', ['$scope', '$rootScope', 'BundlesListService', '$http', '$log', '$modal', '$interpolate', '$timeout',
        function ($scope, $rootScope, BundlesListService, $http, $log, $modal, $interpolate, $timeout) {

            $scope.bundles = [];
            $scope.bundleLoaded = false;
            $scope.formData = {};

            $scope.bundleCreationMessage = '';
            $scope.bundleCreationResultClass = '';

            $scope.message = 'Please Wait...';
            $scope.promise = null;

            function flashOK(msg) {
                $scope.bundleCreationMessage = msg;
                $scope.bundleCreationResultClass = "alert-success";
                clear(5000);
            }

            function flashKO(msg) {
                $scope.bundleCreationMessage = msg;
                $scope.bundleCreationResultClass = "alert-danger";
                clear(8000);
            }

            function clear(delay) {

                function reset() {
                    $scope.bundleCreationMessage = "";
                    $scope.bundleCreationResultClass = "";
                }

                if (delay) {
                    $timeout(reset, delay);
                } else {
                    reset();
                }
            }

            var confirmRemoveBundleExp = $interpolate('That you really want to remove the bundle <b>{{bundleIdentifier}}</b> ?');

            function createBundleObj(id, prof) {
                return {
                    identifier: id,
                    profile: prof,
                    versions: []
                };
            }

            // when new app is added we want to refresh the bundles and apps list
            $rootScope.$on('admin:app-added',function(){
                $scope.fetchBundlesList();
            });
            $rootScope.$on('admin:app-removed', function() {
                $scope.fetchBundlesList();
            });

            // when a bundle is added or removed we want to refresh
            $rootScope.$on('admin:bundle-created', function (event, newBundle) {
                $scope.bundles.push(newBundle);
            });

            $rootScope.$on('admin:bundle-removed', function (event, bundleRemoved) {
                $scope.bundles = _.reject($scope.bundles, function (b) {
                    return b.identifier === bundleRemoved.identifier && b.profile === bundleRemoved.profile;
                });
            });

            $scope.fetchBundlesList = function () {

                $scope.promise = BundlesListService.fetch();

                $scope.promise
                    .success(function (result) {
                        $scope.bundleLoaded = true;
                        $scope.bundles = result.bundles;
                    });
            };

            $scope.createBundle = function (bundle) {

                $log.info('About to create a bew bundle: ' + $scope.formData.identifier + ', ' + $scope.formData.profile);

                return $http
                    .post('./admin/bundle', $.param($scope.formData))
                    .success(function () {

                        flashOK('Bundle created.');

                        var newBundle = createBundleObj($scope.formData.identifier, $scope.formData.profile);

                        $rootScope.$broadcast('admin:bundle-created', newBundle);

                        // re-init form
                        $scope.formData = {};

                    })
                    .error(function (data) {

                        flashKO(data.message);

                    });
            };

            $scope.removeBundle = function (bundle) {

                $log.info('About to remove bundle: ' + bundle.identifier + ', ' + bundle.profile);

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

                        $scope.promise = $http.delete('./admin/bundle/' + bundle.identifier + '/profile/' + bundle.profile);

                        $scope.promise
                            .success(function () {

                                $rootScope.$broadcast('admin:bundle-removed', bundle);

                            })
                            .error(function () {

                                flashKO('An error has occured.');

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

        $scope.ok = function () {
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };

    });
