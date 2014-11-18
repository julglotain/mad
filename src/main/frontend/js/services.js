angular
    .module('mad.services', [])
    .service('BundlesListService', ['$http', function ($http) {
        return {
            fetch: function () {
                return $http.get('./store/data');
            }
        };
    }])
    .service('UploadService', ['$http', 'FormUtils', function ($http, FormUtils) {

        return {
            send: function (formData, url, success, failure) {

                $http
                    .post(url, FormUtils.createFormDataObject(formData), {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    })
                    .success(success)
                    .error(failure);

            }
        };

    }])
    .service('FormUtils', function () {

        return {
            createFormDataObject: function (model) {
                var fd = new FormData();
                angular.forEach(model, function (v, k) {
                    fd.append(k, v);
                });
                return fd;
            }
        };

    });

