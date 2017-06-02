define(['angular', 'jquery.qrcode'], function (angular) {

    function qrCode() {
        return {
            restrict: 'AE',
            //绑定父作用域的text
            scope: {
                text: "="
            },
            //以下为独立作用域
            link: function (scope, element, attributes) {
                scope.$watch("text", function(){
                    if (scope.text) {

                        console && console.log(scope.text);

                        var options = {
                            width: attributes.width || 120,
                            height: attributes.height || 120,
                            text: scope.text
                        };
                        $(element).qrcode(options);
                    }
                });
            }
        };
    }

    qrCode.$inject = [];

    angular.module('app.directive.qrCode', []).directive('qrCode', qrCode);
});