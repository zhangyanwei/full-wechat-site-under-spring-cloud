define(['angular'], function () {

    function originalTag() {
        return {
            restrict: 'AE',
            scope: {
                tags: '='
            },
            link: function (scope, element, attributes) {
                if (scope && scope.tags && scope.tags.length > 0) {
                    for (var index = 0; index < scope.tags.length; index++) {
                        if (scope.tags[index] === 'ORIGINAL') {
                            $(element).replaceWith('<div class="original-tag" >原创￥1</div>');
                            break;
                        }
                    }
                }
            }
        }
    }

    originalTag.$inject = [];

    angular.module('app.directive.originalTag', []).directive('originalTag', originalTag);
});