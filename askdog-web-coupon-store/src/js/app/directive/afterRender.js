define(['angular'], function (angular) {

    function afterRender($timeout) {
        return {
            restrict: 'A',
            scope: {
                afterRender: '&'
            },
            link: function (scope, element, attributes) {
                var renderCallback = scope.afterRender && scope.afterRender();
                if (typeof renderCallback == 'function') {
                    $timeout(function () {
                        renderCallback.call(element, element);
                    });
                }
            }
        };
    }

    angular.module('app.directive.afterRender', [])
        .directive('afterRender', ['$timeout', afterRender]);
});