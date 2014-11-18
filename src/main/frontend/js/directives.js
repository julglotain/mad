angular
    .module('mad.directives', [])

    .directive('selectpicker', ['$parse', function ($parse) {

        return {
            restrict: 'A',
            link: function (scope, element, attrs) {

                var $el = $(element);

                $el.selectpicker('refresh');

                var model = $parse(attrs.ngModel);
                var modelSetter = model.assign;

                $el.on('change', function () {
                    modelSetter(scope, ($el.val() || ''));
                });

                scope.$watch(attrs.ngModel, function (newVal) {

                    scope.$parent[attrs.ngModel] = newVal;
                    $el.selectpicker('val', (newVal || ''));

                });

                scope.$on('$destroy', function () {
                    scope.$evalAsync(function () {
                        $el.selectpicker('destroy');
                    });
                });

            }
        };

    }])

    .directive('bsInputFile', ['$parse', function ($parse) {

        return {
            restrict: 'E',
            templateUrl: '/partials/bs-input-file.tpl.html',
            link: function (scope, element, attrs) {

                var $el = $(element);

                var inputFile = $el.find('input[type="file"]'),
                    filenameText = $el.find('input[type="text"]');

                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                inputFile.on('change', function () {
                    console.log(inputFile);
                    scope.$apply(function () {
                        modelSetter(scope, inputFile[0].files[0]);
                    });
                });

                scope.$watch(attrs.fileModel, function (newVal) {
                    if (newVal) {
                        filenameText.val(newVal.name);
                    } else {
                        filenameText.val('');
                        inputFile.val('');
                    }
                });
            }
        };
    }]);


