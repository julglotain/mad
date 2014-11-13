angular
    .module('mad.directives', [])
    .directive('ngFileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.ngFileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]);
    /*
    .directive('bsInputFile', function () {

        return {
            restrict: 'E',
            template: '<div class="input-group">' +
                        '<span class="input-group-btn">' +
                        '<span class="btn btn-primary btn-file">' +
                        'Browse&hellip; <input type="file">' +
                        '</span>' +
                        '</span>' +
                        '<input type="text" class="form-control" readonly>' +
                       '</div>',
            link: {
                post: function (scope, element, attrs) {


                    var $el = $(element);

                    var inputFile = $el.find('input[type="file"]'),
                        filenameText = $el.find('input[type="text"]');

                    console.log(inputFile);

                    inputFile.on('change', function () {
                        console.log(arguments);
                    })

                }
            }

        };

        */
