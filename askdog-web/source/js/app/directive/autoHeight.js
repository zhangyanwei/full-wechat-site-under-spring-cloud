define(['angular'], function () {

    function autoHeight() {
        return {
            restrict: 'AE',
            scope: {},
            link: function (scope, element, attributes) {
                var _height = isNaN(Number(attributes.height)) ? 50 : Number(attributes.height);
                $(element).on("input", function () {
                    if (this.scrollHeight > _height) {
                        this.style.height = this.scrollHeight + 'px';
                    }
                });
                $(element).on("propertychange", function () {
                    this.style.height = this.scrollHeight + 'px';
                });
                $(element).on("resetHeight", function () {
                    this.style.height = _height + 'px';
                });
            }
        }
    }

    autoHeight.$inject = [];
    angular.module('app.directive.autoHeight', []).directive('autoHeight', autoHeight);
});