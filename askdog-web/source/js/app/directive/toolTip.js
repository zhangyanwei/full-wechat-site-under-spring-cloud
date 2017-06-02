define(['angular'], function () {

    function toolTip() {
        return {
            restrict: 'AE',
            scope: {},
            link: function (scope, element, attributes) {
                $(element).tooltip({
                    title: attributes.tipTitle,
                    trigger: attributes.tipTrigger,
                    placement: attributes.tipPlacement
                });
            }
        }
    }

    toolTip.$inject = [];
    angular.module('app.directive.toolTip', []).directive('toolTip', toolTip);
});