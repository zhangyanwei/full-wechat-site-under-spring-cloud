define(['angular', 'jquery-plugin/qrcode/jquery.qrcode.min'], function () {

    /**
     * pageTitle - Directive for set Page title - mata title
     */
    function qrCode() {
        return {
            restrict: 'AE',
            scope: {
                qrLink: '='
            },
            link: function (scope, element, attributes) {
                scope.$watch(
                    function () {
                        return scope.qrLink;
                    },
                    function () {
                        $(element).qrcode({
                         width: 120,
                         height: 120,
                         text: scope.qrLink
                         });
                         var canvas = document.getElementsByTagName("canvas");
                         var image = new Image();
                         image.src = canvas[0].toDataURL("image/png");
                         $(element).empty();
                         $(element).append(image);
                    });
            }
        }
    }

    qrCode.$inject = [];
    angular.module('app.directive.qrCode', []).directive('qrCode', qrCode);
});