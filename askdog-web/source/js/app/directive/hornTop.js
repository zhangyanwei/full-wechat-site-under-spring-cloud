define(['angular'], function () {

    function hornTop() {
        return {
            restrict: 'AE',
            scope: {},
            link: function (scope, element, attributes) {
                $(element).append('<div class="horn-top hidden">' + '<i class="iconfont icon-noun134084" title="返回顶部" horn-top></i>' + '</div>');
                $(element).find('.horn-top i').on("click", function () {
                    $('#main').scrollTop(0);
                });

                $('#main').scroll(function () {
                    var scrollTop = $(this).scrollTop();
                    if (scrollTop == 0) {
                        $(element).find('.horn-top').addClass('hidden');
                    } else {
                        $(element).find('.horn-top').removeClass('hidden');
                    }
                });

            }
        }
    }

    hornTop.$inject = [];
    angular.module('app.directive.hornTop', []).directive('hornTop', hornTop);
});