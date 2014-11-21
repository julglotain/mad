angular.module('mad.config', ['ngTouch','ngSanitize'])

    .config(['$httpProvider', '$compileProvider', function ($httpProvider, $compileProvider) {

        // Use x-www-form-urlencoded Content-Type
        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

        $compileProvider.debugInfoEnabled(false);

    }]);