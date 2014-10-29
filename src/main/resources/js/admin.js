angular
    .module('adminApp',[])
    .factory('AppsListService', function ($http) {
        return {
            fetchBundlesList: function () {
                return $http.get('./download.json')
            }
        }
    })
    .service('UploadService', ['$http', function ($http) {

        function createFormDataObject(model){
            var fd = new FormData();
            angular.forEach(model,function(v,k){
                fd.append(k,v);
            });
            return fd;
        }

        this.send = function(formData, url){

            $http.post(url, createFormDataObject(formData), {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(){
                console.log('ok')
            })
            .error(function(){
                console.log('ko')
            });


        }
    }])
    .controller('AddNewAppFormCtrl', function ($scope, UploadService) {

        $scope.formData = {};

        $scope.submit = function(){

            UploadService.send($scope.formData,'/mad/admin/upload');
        }

    })
    .directive('ngFileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                var model = $parse(attrs.ngFileModel);
                var modelSetter = model.assign;

                element.bind('change', function(){
                    scope.$apply(function(){
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }])
    .directive('appDetail',function(){
        return {
            require: ['^myTabs', '^ngModel'],
            restrict: 'E',
            transclude: true,
            scope: {
                title: '@'
            },
            link: function(scope, element, attrs, controllers) {
                var tabsCtrl = controllers[0],
                    modelCtrl = controllers[1];

                tabsCtrl.addPane(scope);
            },
            templateUrl: 'my-pane.html'
        };
    })
    .controller('AppsListCtrl', function ($scope, $window, AppsListService) {

        AppsListService.fetchBundlesList().success(function (result) {

            var apps = [];

            angular.forEach(result.bundles,function(bundle){

                angular.forEach(bundle.profiles,function(profile){

                    angular.forEach(profile.applications,function(app){

                        apps.push({
                            title: app.title,
                            version: app.version

                        });

                    });

                });

            });

            $scope.applications = apps;

        });

        $scope.deleteApp = function(title, version, event){

            console.log(arguments);

        }

    });