define(['angular', 'jquery.qrcode'], function (angular) {

    function qrCode() {
        return {
            restrict: 'AE',
            scope: {
                text: "="
            },
            link: function (scope, element, attributes) {
                scope.$watch("text", function () {
                    $(element).empty();
                    if (scope.text) {
                        
                        console && console.log(scope.text);

                        $(element).qrcode({
                            width: attributes.width || 120,
                            height: attributes.height || 120,
                            text: scope.text
                        });
                    }
                })
            }
        };
    }

    qrCode.$inject = [];

    angular.module('app.directive.qrCode', []).directive('qrCode', qrCode);
});