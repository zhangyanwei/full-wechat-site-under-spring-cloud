define(['angular'], function () {
    function carousel() {
        return {
            restrict: 'AE',
            link: function (scope, element, attributes) {
                $(element).carousel();
            }
        }
    }

    carousel.$inject = [];
    angular.module('app.directive.carousel', []).directive('carousel', carousel);
});