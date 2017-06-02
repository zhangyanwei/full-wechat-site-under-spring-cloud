define(['angular'], function () {
    function adAnalytics() {
        return {
            restrict: 'AE',
            scope: {
                href: '='
            },
            link: function (scope, element, attributes) {
                (function () {
                    var _hmt = _hmt || [];
                    window._hmt = _hmt;
                    var urlArray = window.location.href.split("#");
                    _hmt.push(['_trackPageview', urlArray[1]]);
                    var hm = document.createElement("script");
                    hm.src = "//hm.baidu.com/hm.js?15042487321";
                    var s = document.getElementsByTagName("script")[0];
                    s.parentNode.insertBefore(hm, s);
                })();
            }
        }
    }

    adAnalytics.$inject = [];
    angular.module('app.directive.adAnalytics', []).directive('adAnalytics', adAnalytics);
});
