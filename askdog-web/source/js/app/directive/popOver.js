define(['angular'], function () {

    function popOver() {
        return {
            restrict: 'AE',
            scope: {},
            link: function (scope, element, attributes) {
                $(element).popover({
                    content: attributes.popContent,
                    html: !!attributes.popHtml,
                    trigger: attributes.popTrigger,
                    placement: attributes.popPlacement
                });
            }
        }
    }

    popOver.$inject = [];
    angular.module('app.directive.popOver', []).directive('popOver', popOver);
});