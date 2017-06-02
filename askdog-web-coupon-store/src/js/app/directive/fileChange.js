define(['angular'], function() {

    /**
     * thirdLogin - Directive for third-party login link/icon
     */
    function fileInput($parse) {
        return {
            restrict: 'A',
            link: function($scope, element, $attrs) {
                var model = $parse("file");
                var modelSetter = model.assign;
                var fileChange = $parse($attrs.fileChange);

                element.bind('change', function(){
                    $scope.$apply(function(){
                        modelSetter($scope, element[0].files[0]);
                        fileChange($scope);
                    });
                });
            }
        };
    }

    angular.module('app.directive.fileChange',[]).directive('fileChange', ['$parse', fileInput]);
});