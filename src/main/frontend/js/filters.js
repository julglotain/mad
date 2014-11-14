angular
    .module('mad.filters', [])
    .filter('removeBundleIfNoAppVersions', function () {
        return function (input) {

            var bundles = [];

            angular.forEach(input, function (bundle) {

                if (bundle.versions && bundle.versions.length) {
                    bundles.push(bundle);
                }

            });

            return bundles;

        };
    });